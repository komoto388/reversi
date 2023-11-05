package gui;

import common.Convert;
import common.Global;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import reversi.Board;
import reversi.Dimension;
import reversi.Player;
import reversi.ResultType;
import reversi.Reversi;

/**
 * リバーシのゲーム画面を処理するコントローラー
 */
public class ReversiController {

    /** ゲームのイベント状態の値 */
    private enum EventStatus {
        PLAY, SKIP, WAIT, JUDGE, WAIT_FINAL, FINISH
    }

    /** ゲームのイベント状態を表す */
    private EventStatus eventStatus;

    /** ゲームの勝敗結果を表す */
    private ResultType result;

    /** タイマーイベントを制御するインスタンス */
    private Timeline timer;

    /** リバーシ盤の手動入力を無効する時間（フレーム数） */
    private int waitFrame;

    /** 実際のFPSの計測・算出するインスタンス */
    private Fps fps;

    /** リバーシを制御するインスタンス */
    private Reversi reversi;

    /** リバーシ盤を描画するインスタンス */
    private BoardController boardController;

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

    /** 先手・黒のプレイヤー名を表示するラベル */
    @FXML
    private Label blackNameLabel;

    /** 後手・白のプレイヤー名を表示するラベル */
    @FXML
    private Label whiteNameLabel;

    /** 先手・黒のアルゴリズムを表示するラベル */
    @FXML
    private Label blackAlgorithmLabel;

    /** 後手・白のアルゴリズムを表示するラベル */
    @FXML
    private Label whiteAlgorithmLabel;

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
     * @param sceneSwitch シーン切替処理を行うインスタンス
     * @param reversi リバーシの処理を行うインスタンス
     */
    public void init(SceneSwitch sceneSwitch, Reversi reversi) {
        // 引数の正常性確認
        try {
            if (sceneSwitch == null) {
                throw new IllegalArgumentException("引数 \"sceneSwitch\" の値が NULL です");
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

        this.result = ResultType.None;
        this.reversi = reversi;
        this.fps = new Fps();

        Player player = reversi.getPlayerBlack();
        blackNameLabel.setText(player.getName());
        blackAlgorithmLabel.setText("( " + player.getAlgorithmType().getName() + " )");

        player = reversi.getPlayerWhite();
        whiteNameLabel.setText(player.getName());
        whiteAlgorithmLabel.setText("( " + player.getAlgorithmType().getName() + " )");

        statusLabel.setText(null);
        debugLabel.setText(null);
        eventStatusLabel.setText(null);

        // リバーシ盤の描画を行う
        Dimension boardSize = reversi.getBoard().getSize();
        boardController = new BoardController(gridPane, boardSize, Global.GRID_SIZE);

        // マスをクリックした時のイベントを設定する
        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                Pane pane = boardController.getBoardPane(i, j);
                pane.setOnMouseClicked(new GridHandler(new Dimension(i, j)));
                pane.setDisable(true);
            }
        }

        // 画面描画イベントを設定する
        setEventStatus(EventStatus.WAIT);
        setWaitTime(Global.WAIT_MILLISEC_START);

        timer = new Timeline(new KeyFrame(Duration.millis(1000 / Global.FPS), new EventHandler<ActionEvent>() {
            /**
             * 周期的に実行する処理を行う。
             * @param event イベントのインスタンス
             */
            @Override
            public void handle(ActionEvent event) {
                // ステータスを設定する
                if (eventStatus == EventStatus.PLAY) {
                    if (reversi.isSkip()) {
                        setEventStatus(EventStatus.SKIP);
                    }
                }

                try {
                    run();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 画面描画を行う
                fps.update();
                update();

                if (waitFrame > 0) {
                    waitFrame--;
                }
            }

            /**
             * リバーシのゲームイベントを処理する
             */
            private void run() {
                switch (eventStatus) {
                case PLAY: {
                    if (reversi.isCurrentPlayerManual()) {
                        // プレイヤーが手動入力の時は、何もしない
                    } else {
                        // プレイヤーが手動入力の時は、アルゴリズムに従い処理を行う
                        Dimension target = reversi.run();
                        put(target);
                    }
                    break;
                }
                case SKIP: {
                    // 石がどこにも置けない時のスキップ処理を定義
                    statusLabel.setText(Convert.getPlayerColor(reversi.getPlayerIsBlack()) + " はスキップします。");
                    reversi.next();
                    setEventStatus(EventStatus.WAIT);
                    setWaitTime(Global.WAIT_MILLISEC_INTERVAL);
                    break;
                }
                case WAIT: {
                    // 待ち状態の場合は何もせず、画面描画のみ行う
                    if (waitFrame <= 0) {
                        setEventStatus(EventStatus.PLAY);
                    }
                    break;
                }
                case JUDGE: {
                    judge();
                    if (result != ResultType.None) {
                        setEventStatus(EventStatus.WAIT_FINAL);
                    } else {
                        setEventStatus(EventStatus.WAIT);
                    }
                    setWaitTime(Global.WAIT_MILLISEC_INTERVAL);
                    break;
                }
                case WAIT_FINAL: {
                    if (waitFrame <= 0) {
                        setEventStatus(EventStatus.FINISH);
                    }
                    break;
                }
                case FINISH: {
                    // 完了処理を行い、結果画面を表示する
                    timer.stop();
                    sceneSwitch.generateSceneResult(reversi, result);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unexpected value: " + eventStatus);
                }
            }
        }));

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
        
        // 初期設定後に画面描画を行う
        update();
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
            boardController.resetFxidAll();
            Pane targetPane = boardController.getBoardPane(target);
            targetPane.setId(Global.FXID_GRID_PUT);
            String playerString = Convert.getPlayerColor(reversi.getPlayerIsBlack());
            statusLabel.setText(playerString + " は " + target.getString() + " に石を置きました。");

            setEventStatus(EventStatus.JUDGE);
            setWaitTime(Global.WAIT_MILLISEC_INTERVAL);
        } else {
            statusLabel.setText(target.getString() + " には石を置けません");
        }

        return isPut;
    }

    /**
     * 画面描画を行う関数
     */
    private void update() {
        Board board = reversi.getBoard();

        // リバーシ盤に石を描画する
        boardController.drawStone(board);

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
     * 勝敗判定を行う
     */
    private void judge() {
        try {
            result = reversi.judge();
            switch (result) {
            case None: {
                reversi.next();
                setEventStatus(EventStatus.WAIT);
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
    }

    /**
     * イベントステータスの値を設定する
     * @param eventStatus 設定するイベントステータスの値
     */
    private void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;

        try {
            switch (eventStatus) {
            case PLAY: {
                if (reversi.isCurrentPlayerManual()) {
                    setBoardEnable(true);
                } else {
                    setBoardEnable(false);
                }
                break;
            }
            case WAIT:
            case SKIP:
            case JUDGE:
            case WAIT_FINAL:
            case FINISH: {
                setBoardEnable(false);
                break;
            }
            default:
                throw new IllegalArgumentException("Unexpected value: " + eventStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * リバーシ盤の操作受付を有効／無効にする。
     * @param isEnable 画面操作を受け付ける場合は{@code true}, 受け付けない場合は {@code false} にする。
     */
    private void setBoardEnable(Boolean isEnable) {
        Dimension boardSize = reversi.getBoard().getSize();

        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                Pane pane = boardController.getBoardPane(i, j);
                pane.setDisable(!isEnable);
            }
        }
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
            setEventStatus(EventStatus.PLAY);
        }
    }
}
