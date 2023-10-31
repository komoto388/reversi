package gui;

import algorithm.AlgorithmType;
import common.Convert;
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
    /** 1秒間に画面描画する回数(FPS) */
    private final int FPS = 30;

    /** 手動操作中の待ち時間（待ち状態を無限にする） */
    private final int WAIT_INFINITE = -1;

    /** ターン間のインターバル（ミリ秒） */
    private final int INTERVAL_WAIT = 500;

    /** マスの大きさ（縦・横同じ） */
    private final double GRID_SIZE = 60;

    /** 石の半径 */
    private final double DISC_RADIUS = (GRID_SIZE / 2.0 - 10);
    
    /** フレーム情報 */
    private Stage stage;

    /** リバーシ盤の手動入力を無効する時間（フレーム数） */
    private int waitFrame;

    /** 現在のFPS */
    private int currnetFps;
    
    /** リバーシを制御するインスタンス */
    private Reversi reversi;
    
    /** リバーシ盤のマス */
    private Pane[][] boardPane;

    /** リバーシ盤を描画するペイン */
    @FXML
    private GridPane gridPane;

    /** 現在のプレイヤーの石の色を表示するオブジェクト */
    @FXML
    private Circle currentDisc;

    /** 現在の経過ターン数を表示するラベル */
    @FXML
    private Label turnLabel;

    /** 現在の黒石の数を表示するラベル */
    @FXML
    private Label blackDiscNum;

    /** 現在の白石の数を表示するラベル */
    @FXML
    private Label whiteDiscNum;

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
     * @param board リバーシ盤の状態を表す
     */
    public void init(Stage stage, Reversi reversi) {
        this.stage = stage;
        this.reversi = reversi;
        Board board = reversi.getBoard();
        boardPane = new Pane[board.getSize().getRow()][board.getSize().getColumn()];
        waitFrame = Convert.convertFrame(2000, FPS);
        statusLabel.setText(null);
        debugLabel.setText(null);

        for (int i = 0; i < board.getSize().getRow() + 1; i++) {
            for (int j = 0; j < board.getSize().getColumn() + 1; j++) {
                if (i == 0) {
                    // リバーシ盤の列番号を付与する
                    if (j > 0) {
                        Label boardColumnNum = new Label(String.valueOf(Convert.convertIntToChar(j - 1)));
                        boardColumnNum.setId("board-column-num");
                        boardColumnNum.setPrefWidth(GRID_SIZE);
                        boardColumnNum.setPrefHeight(GRID_SIZE / 2);
                        gridPane.add(boardColumnNum, j, i);
                    }
                } else if (j == 0) {
                    // リバーシ盤の行番号を付与する
                    if (i > 0) {
                        Label boardRowNum = new Label(Integer.toString(i));
                        boardRowNum.setId("board-row-num");
                        boardRowNum.setPrefWidth(GRID_SIZE / 2);
                        boardRowNum.setPrefHeight(GRID_SIZE);
                        gridPane.add(boardRowNum, j, i);
                    }
                } else {
                    // リバーシ盤のマスを生成する
                    Dimension target = new Dimension(i - 1, j - 1);

                    // 描画用マスを作成する
                    boardPane[i - 1][j - 1] = new Pane();
                    boardPane[i - 1][j - 1].setPrefWidth(GRID_SIZE);
                    boardPane[i - 1][j - 1].setPrefHeight(GRID_SIZE);
                    boardPane[i - 1][j - 1].setOnMouseClicked(new GridHandler(target));
                    boardPane[i - 1][j - 1].setDisable(true);

                    if ((i + j) % 2 == 0) {
                        boardPane[i - 1][j - 1].setId("grid1");
                    } else {
                        boardPane[i - 1][j - 1].setId("grid2");
                    }
                    gridPane.add(boardPane[i - 1][j - 1], j, i);
                }
            }
        }

        // 画面描画イベントを設定する
        Timeline timer = new Timeline(new KeyFrame(new Duration(1000 / FPS), new TimerHandler()));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    /**
     * 画面描画など周期的に実行するイベントを定義するクラス
     */
    private class TimerHandler implements EventHandler<ActionEvent> {
        long startTime;
        int countFrame;

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
                        setWaitMode(WAIT_INFINITE, true);
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

            // リバーシ盤の状態を更新する
            for (int i = 0; i < board.getSize().getRow(); i++) {
                for (int j = 0; j < board.getSize().getColumn(); j++) {
                    Dimension target = new Dimension(i, j);

                    // マスに描画されている石を消去する
                    boardPane[i][j].getChildren().clear();

                    if (board.isEmpty(target) == false) {
                        // リバーシの石を描画する
                        Circle circle = new Circle(DISC_RADIUS);
                        circle.setLayoutX(GRID_SIZE / 2.0);
                        circle.setLayoutY(GRID_SIZE / 2.0);

                        if (board.isBlack(target)) {
                            circle.setId("disc-black");
                        } else {
                            circle.setId("disc-white");
                        }
                        boardPane[i][j].getChildren().add(circle);
                    }
                }
            }

            // 現在の手番、石の個数を更新する
            if (reversi.getPlayerIsBlack()) {
                currentDisc.setFill(Paint.valueOf("black"));
            } else {
                currentDisc.setFill(Paint.valueOf("white"));
            }
            turnLabel.setText(String.format("%d手目", reversi.getTurnCount()));
            blackDiscNum.setText(String.format("黒: %2d個", board.getBlackDiscNum()));
            whiteDiscNum.setText(String.format("白: %2d個", board.getWhiteDiscNum()));
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

    private Boolean play(Dimension target) {
        if (reversi.put(target)) {
            statusLabel.setText(
                    Convert.getPlayerColor(reversi.getPlayerIsBlack()) + " は " + target.getString() + " に石を置きました。");

            // 勝敗判定を行う
            ResultType result = reversi.judge();
            switch (result) {
            case None: {
                reversi.next();
                setWaitMode(INTERVAL_WAIT, false);
                break;
            }
            case Drow:
            case Black:
            case White: {
                FXMLLoader fxmlloader = null;
                Pane root = null;
                
                try {
                    fxmlloader = new FXMLLoader(getClass().getResource("../fxml/Result.fxml"));
                    root = (Pane) fxmlloader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ResultController controller = (ResultController) fxmlloader.getController();
                controller.init(reversi, result);

                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("../css/application.css").toExternalForm());

                stage.setScene(scene);
                stage.show();
                break;
//                
//                System.out.printf("引き分けです。\n");
//                statusLabel.setText("引き分けです。");
//                setWaitMode(WAIT_INFINITE, false);
//                break;
//            }
//                System.out.printf("黒の勝ちです。\n");
//                statusLabel.setText("黒の勝ちです。");
//                setWaitMode(WAIT_INFINITE, false);
//                break;
//            }
//                System.out.printf("白の勝ちです。\n");
//                statusLabel.setText("白の勝ちです。");
//                setWaitMode(WAIT_INFINITE, false);
//                break;
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
            waitFrame = Convert.convertFrame(waitMilliSec, FPS);
        } else {
            waitFrame = WAIT_INFINITE;
        }

        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                boardPane[i][j].setDisable(!isInputEnable);
            }
        }
    }
}
