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

    /** タイマーイベントのイベントハンドラー */
    private TimerHandler timerHandler;

    /** リバーシ盤の手動入力を無効する時間（フレーム数） */
    private int waitFrame;

    /** 実際のFPSの計測・算出するインスタンス */
    private Fps fps;

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

    /** 現在のイベントステータスを表示するラベル */
    @FXML
    private Label eventStatusLabel;

    /**
     * リバーシ盤を初期化する
     * @param stage フレーム情報
     * @param reversi リバーシの処理を行うインスタンス
     */
    public void init(Stage stage, Reversi reversi) {
        // 引数の正常性確認
        try {
            if (stage == null) {
                throw new IllegalArgumentException("引数 \"stage\" の値が NULL です");
            }
            if (reversi == null) {
                throw new IllegalArgumentException("引数 \"reversi\" の値が NULL です");
            }
        } catch (IllegalArgumentException e) {
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println("引数が想定されていない値のため、プログラムを異常終了します: 終了コード = " + exitCode);
            System.exit(exitCode);
        }

        this.stage = stage;
        this.reversi = reversi;
        this.fps = new Fps();

        statusLabel.setText(null);
        debugLabel.setText(null);
        eventStatusLabel.setText(null);

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
        timerHandler = new TimerHandler();
        timerHandler.setEventStatusWait();
        setWaitTime(Global.WAIT_MILLISEC_START);

        timer = new Timeline(new KeyFrame(new Duration(1000 / Global.FPS), timerHandler));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    /**
     * 画面描画など周期的に実行するイベントを定義するクラス
     */
    private class TimerHandler implements EventHandler<ActionEvent> {

        private enum EventStatus {
            PLAY_MANUAL, PLAY_ALGORITHM, SKIP, WAIT, JUDGE, WAIT_FINAL, FINISH
        }

        private EventStatus eventStatus;

        /** ゲームの勝敗を表す */
        private ResultType result;

        /**
         * 周期的に実行する処理を行う。
         * @param event イベントのインスタンス
         */
        @Override
        public void handle(ActionEvent event) {
            // ステータスを設定する
            if (eventStatus == EventStatus.PLAY_ALGORITHM || eventStatus == EventStatus.PLAY_MANUAL) {
                if (reversi.isSkip()) {
                    setEventStatusSkip();
                }
            }

            // イベントステータスに応じて処理を行う
            try {
                switch (eventStatus) {
                case PLAY_MANUAL: {
                    // 手動入力待ち状態にする。手動入力はここではなく、マウスのイベントハンドラーで処理する。
                    break;
                }
                case PLAY_ALGORITHM: {
                    // アルゴリズムに従い、石を置く処理を行う。
                    Dimension target = reversi.run();
                    put(target);
                    break;
                }
                case SKIP: {
                    // 石がどこにも置けない時のスキップ処理を定義
                    statusLabel.setText(Convert.getPlayerColor(reversi.getPlayerIsBlack()) + " はスキップします。");
                    reversi.next();
                    timerHandler.setEventStatusWait();
                    setWaitTime(Global.WAIT_MILLISEC_INTERVAL);
                    break;
                }
                case WAIT: {
                    // 待ち状態の場合は何もせず、画面描画のみ行う
                    if (waitFrame <= 0) {
                        setEventStatusPlay(reversi.isCurrentPlayerManual());
                    }
                    break;
                }
                case JUDGE: {
                    ResultType result = judge();
                    if (result != ResultType.None) {
                        setEventStatusWaitFinal(result);
                    } else {
                        setEventStatusWait();
                    }
                    setWaitTime(Global.WAIT_MILLISEC_INTERVAL);
                    break;
                }
                case WAIT_FINAL: {
                    if (waitFrame <= 0) {
                        setEventStatusFinish();
                    }
                    break;
                }
                case FINISH: {
                    // 完了処理を行い、結果画面を表示する
                    timer.stop();
                    showResult(result);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unexpected value: " + eventStatus);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();

            } finally {
                // 画面描画を行う
                fps.update();
                update();

                if (waitFrame > 0) {
                    waitFrame--;
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
            fpsLabel.setText(String.format("待ちフレーム数:%3d, %.2f fps", waitFrame, fps.getFps()));
            eventStatusLabel.setText(eventStatus.toString());
        }

        /**
         * アルゴリズムまたは人間のプレイヤーが操作・処理できる状態にする。
         * @param isManual 人間のプレイヤーが操作する場合は真 {@code true}, アルゴリズムの場合は偽 {@code false}
         */
        private void setEventStatusPlay(Boolean isManual) {
            if (isManual) {
                eventStatus = EventStatus.PLAY_MANUAL;
                setBoardEnable(true);
            } else {
                eventStatus = EventStatus.PLAY_ALGORITHM;
                setBoardEnable(false);
            }
        }

        /**
         * どのプレイヤーも操作できない状態にする状態にする。
         */
        private void setEventStatusWait() {
            eventStatus = EventStatus.WAIT;
            setBoardEnable(false);
        }

        /**
         * 盤面上でスキップが発生している状態にする。（プレイヤーは操作できない）
         */
        private void setEventStatusSkip() {
            eventStatus = EventStatus.SKIP;
            setBoardEnable(false);
        }

        /**
         * 勝利判定処理を行う必要がある状態にする。石の設置後に使用する。
         */
        private void setEventStatusJudge() {
            eventStatus = EventStatus.JUDGE;
            setBoardEnable(false);
        }

        /**
         * 結果画面の表示待ち状態にする。勝敗判定後に使用する。
         * @param ゲームの勝敗結果
         */
        private void setEventStatusWaitFinal(ResultType result) {
            this.result = result;
            eventStatus = EventStatus.WAIT_FINAL;
            setBoardEnable(false);
        }

        /**
         * 結果画面を表示する状態にする。
         * @param ゲームの勝敗結果
         */
        private void setEventStatusFinish() {
            eventStatus = EventStatus.FINISH;
            setBoardEnable(false);
        }

        /**
         * リバーシ盤の操作受付を有効／無効にする。
         * @param isEnable 画面操作を受け付ける場合は{@code true}, 受け付けない場合は {@code false} にする。
         */
        private void setBoardEnable(Boolean isEnable) {
            Dimension boardSize = reversi.getBoard().getSize();

            for (int i = 0; i < boardSize.getRow(); i++) {
                for (int j = 0; j < boardSize.getColumn(); j++) {
                    Pane pane = displayBoard.getBoardPane(i, j);
                    pane.setDisable(!isEnable);
                }
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
            put(dim);
        }
    }

    /**
     * 石の設置を行う
     * @param target プレイヤーが石を置く座標
     * @return 石の設置ができた場合は真 {@code true}, 既に石が存在している等で設置できなかった場合は偽 {@code false} を返す。
     */
    private Boolean put(Dimension target) {
        // 座標に対して、石を置けるか判定する
        Boolean isPut = false;
        try {
            isPut = reversi.put(target);
        } catch (IllegalArgumentException e) {
            // 例外が発生した場合、石を置けないと判断して処理を続ける。
            e.printStackTrace();
            isPut = false;
        }

        if (isPut) {
            String playerString = Convert.getPlayerColor(reversi.getPlayerIsBlack());
            statusLabel.setText(playerString + " は " + target.getString() + " に石を置きました。");

            timerHandler.setEventStatusJudge();
            setWaitTime(Global.WAIT_MILLISEC_INTERVAL);
        } else {
            statusLabel.setText(target.getString() + " には石を置けません");
        }

        return isPut;
    }

    /**
     * 勝敗判定を行う
     */
    private ResultType judge() {
        ResultType result;
        try {
            result = reversi.judge();
            switch (result) {
            case None: {
                reversi.next();
                timerHandler.setEventStatusWait();
                setWaitTime(Global.WAIT_MILLISEC_INTERVAL);
                break;
            }
            case Drow:
            case Black:
            case White: {
                break;
            }
            default:
                throw new IllegalArgumentException("Unexpected value: " + reversi.judge());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = ResultType.None;
        }

        return result;
    }

    /**
     * 処理の待ち時間を設定する
     * @param waitMilliSec 待ち時間。
     */
    private void setWaitTime(int waitMilliSec) {
        try {
            if (waitMilliSec <= 0) {
                throw new IllegalArgumentException("待ち時間の値が0以下です: " + waitMilliSec);
            }
            waitFrame = Convert.convertFrame(waitMilliSec, Global.FPS);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("処理待ち時間をなし、画面操作を可能な状態として設定します。");
            waitFrame = 0;
            timerHandler.setEventStatusPlay(reversi.isCurrentPlayerManual());
        }
    }

    /**
     * 結果画面を生成し、表示する
     * @param result 勝敗結果を表す
     */
    private void showResult(ResultType result) {
        FXMLLoader fxmlloader = null;
        BorderPane resultPane = null;
        String fxmlFile = "../fxml/Result.fxml";

        try {
            fxmlloader = new FXMLLoader(getClass().getResource(fxmlFile));
            resultPane = (BorderPane) fxmlloader.load();
        } catch (Exception e) {
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println(fxmlFile + "の読み込みで例外が発生したため、プログラムを異常終了します: 終了コード = " + exitCode);
            System.exit(exitCode);
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
