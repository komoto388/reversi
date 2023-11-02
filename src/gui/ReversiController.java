package gui;

import common.Convert;
import common.Global;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import reversi.Board;
import reversi.Dimension;
import reversi.ResultType;
import reversi.Reversi;

/**
 * リバーシのゲーム画面を処理するコントローラー
 */
public class ReversiController {

    /** フレーム情報 */
    private Stage stage;

    /** タイマーイベントを制御するインスタンス */
    private Timeline timer;

    /** リバーシ盤の手動入力を無効する時間（フレーム数） */
    private int waitFrame;

    /** 現在のFPS */
    private int currnetFps;

    /** リバーシを制御するインスタンス */
    private Reversi reversi;

    /** リバーシ盤を描画するインスタンス */
    private DisplayBoard displayBoard;

    /** リバーシ画面のルートペイン */
    @FXML
    private BorderPane reversiRootPane;

    /** リバーシ盤を描画するペイン */
    @FXML
    private GridPane gridPane;

    /** 現在のプレイヤーの石の色を表示するオブジェクト */
    @FXML
    private Circle currentDiscCircle;

    /** 現在の経過ターン数を表示するラベル */
    @FXML
    private Label turnLabel;

    /** 現在の黒石の数を表示するラベル */
    @FXML
    private Label blackDiscNumLabel;

    /** 現在の白石の数を表示するラベル */
    @FXML
    private Label whiteDiscNumLabel;

    /** 現在のステータスを表示するラベル */
    @FXML
    private Label statusLabel;

    /** デバッグ情報を表示するラベル */
    @FXML
    private Label debugLabel;

    /** 現在のFPS情報を表示するラベル */
    @FXML
    private Label fpsLabel;

    /**
     * リバーシ盤を初期化する
     * @param stage フレーム情報
     * @param reversi リバーシの処理を行うインスタンス
     */
    public void init(Stage stage, Reversi reversi) {
        this.stage = stage;
        this.reversi = reversi;

        statusLabel.setText(null);
        debugLabel.setText(null);

        // リバーシ盤の描画を行う
        Dimension boardSize = reversi.getBoard().getSize();
        displayBoard = new DisplayBoard(gridPane, boardSize, Global.GRID_SIZE);

        // マスをクリックした時のイベントを設定する
        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                Pane pane = displayBoard.getBoardPane(i, j);
                pane.setOnMouseClicked(new GridHandler(new Dimension(i, j)));
                pane.setDisable(true);
            }
        }

        // 画面描画イベントを設定する
        waitFrame = Convert.convertFrame(Global.WAIT_MILLISEC_START, Global.FPS);
        timer = new Timeline(new KeyFrame(new Duration(1000 / Global.FPS), new TimerHandler()));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    /**
     * 画面描画など周期的に実行するイベントを定義するクラス
     */
    private class TimerHandler implements EventHandler<ActionEvent> {
        private long startTime;
        private int countFrame;

        /**
         * タイムイベントの初期化を行う。
         */
        public TimerHandler() {
            startTime = System.currentTimeMillis();
            countFrame = 0;
            currnetFps = 0;
        }

        /**
         * 周期的に実行する処理を行う。
         * @param event イベントのインスタンス
         */
        @Override
        public void handle(ActionEvent event) {
            // リバーシのゲーム処理を行う
            gameRun();

            // 現在のFPSを計算する
            countFrame++;
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime > 1000) {
                currnetFps = (int) (countFrame * 1000 / (currentTime - startTime));
                startTime = currentTime;
                countFrame = 0;
            }

            // 画面描画を行う
            update();
        }

        /**
         * リバーシのスキップ判定、プレイヤーのプレイ処理、勝敗判定を行う
         */
        private void gameRun() {
            // 手動入力待ち状態出ない場合、スキップ判定とCOM側操作を行う。
            if (waitFrame == 0) {
                if (reversi.isSkip()) {
                    // 石がどこにも置けない時のスキップ処理を定義
                    statusLabel.setText(Convert.getPlayerColor(reversi.getPlayerIsBlack()) + " はスキップします。");
                    reversi.next();
                } else {
                    // 石を置く処理を行う。手入力の場合は入力待ち、それ以外はアルゴリズムに従い処理を行う
                    if (reversi.isCurrentPlayerManual()) {
                        // 手動入力待ち状態にする。手動入力はここではなく、マウスのイベントハンドラーで処理する。
                        setWaitMode(Global.WAIT_FRAME_INFINITE, true);
                    } else {
                        Dimension target = reversi.run();
                        play(target);
                    }
                }
            }
        }

        /**
         * 画面描画を行う関数
         */
        private void update() {
            Board board = reversi.getBoard();

            // リバーシ盤に石を描画する
            displayBoard.drawStone(board);

            // 現在の手番、石の個数を更新する
            if (reversi.getPlayerIsBlack()) {
                currentDiscCircle.setFill(Paint.valueOf("black"));
            } else {
                currentDiscCircle.setFill(Paint.valueOf("white"));
            }
            turnLabel.setText(String.format("%d手目", reversi.getTurnCount()));
            blackDiscNumLabel.setText(String.format("黒: %2d個", board.getBlackDiscNum()));
            whiteDiscNumLabel.setText(String.format("白: %2d個", board.getWhiteDiscNum()));
            fpsLabel.setText(Integer.toString(currnetFps) + " fps");

            // デバッグ文の表示
            if (waitFrame > 0) {
                debugLabel.setText(String.format("処理中です。待ちフレーム数:%3d", waitFrame));
                waitFrame--;
            } else {
                debugLabel.setText(String.format("入力可能です。待ちフレーム数:%3d", waitFrame));
            }
        }
    }

    /**
     * リバーシ盤のマスをクリックした時の動作を定義するクラス
     */
    private class GridHandler implements EventHandler<MouseEvent> {
        /** マスの座標を表す */
        private Dimension dim;

        /**
         * マスの座標を設定する
         * @param target マスの座標
         */
        public GridHandler(Dimension target) {
            this.dim = target;
        }

        /**
         * マスをクリックした時、石を置く
         */
        @Override
        public void handle(MouseEvent event) {
            play(dim);
        }
    }

    /**
     * 石の設置、勝利判定を行う
     * @param target プレイヤーが石を置く座標
     * @return 石の設置ができた場合は真 {@code true}, 既に石が存在している等で設置できなかった場合は偽 {@code false} を返す。
     */
    private Boolean play(Dimension target) {
        if (reversi.put(target)) {
            statusLabel.setText(
                    Convert.getPlayerColor(reversi.getPlayerIsBlack()) + " は " + target.getString() + " に石を置きました。");

            // 勝敗判定を行う
            ResultType result = reversi.judge();
            switch (result) {
            case None: {
                reversi.next();
                setWaitMode(Global.WAIT_MILLISEC_INTERVAL, false);
                break;
            }
            case Drow:
            case Black:
            case White: {
                timer.stop();
                showResult(result);
                break;
            }
            default:
                throw new IllegalArgumentException("Unexpected value: " + reversi.judge());
            }
            return true;
        } else {
            System.out.printf("%s のマスには石を置けません。\n", target.getString());
            return false;
        }
    }

    /**
     * 待ち時間を設定し、待ち時間中はリバーシ盤の操作を無効にする。
     * @param waitMilliSec 待ち時間。
     * @param isInputEnable 画面操作を受け付ける場合は{@code true}, 受け付けない場合は {@code false} にする。
     */
    private void setWaitMode(int waitMilliSec, Boolean isInputEnable) {
        Dimension boardSize = reversi.getBoard().getSize();

        if (waitMilliSec >= 0) {
            waitFrame = Convert.convertFrame(waitMilliSec, Global.FPS);
        } else {
            waitFrame = Global.WAIT_FRAME_INFINITE;
        }

        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                Pane pane = displayBoard.getBoardPane(i, j);
                pane.setDisable(!isInputEnable);
            }
        }
    }

    /**
     * 結果画面を生成し、表示する
     * @param result 勝敗結果を表す
     */
    private void showResult(ResultType result) {
        FXMLLoader fxmlloader = null;
        BorderPane resultPane = null;

        try {
            fxmlloader = new FXMLLoader(getClass().getResource("../fxml/Result.fxml"));
            resultPane = (BorderPane) fxmlloader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResultController controller = (ResultController) fxmlloader.getController();
        controller.init(stage, reversi, result);

        // 現在の画面のシーンとルートペインを取得する
        Scene scene = stage.getScene();
        AnchorPane rootPane = (AnchorPane) scene.getRoot();
        rootPane.getChildren().add(resultPane);

        // 結果画面のサイズを調整し、表示位置を中央にする
        resultPane.setPrefSize(rootPane.getWidth() * 0.8, rootPane.getHeight() * 0.9);
        double dWidth = rootPane.getWidth() - resultPane.getPrefWidth();
        double dHeight = rootPane.getHeight() - resultPane.getPrefHeight();
        AnchorPane.setTopAnchor(resultPane, dHeight / 2);
        AnchorPane.setBottomAnchor(resultPane, dHeight / 2);
        AnchorPane.setLeftAnchor(resultPane, dWidth / 2);
        AnchorPane.setRightAnchor(resultPane, dWidth / 2);
    }
}
