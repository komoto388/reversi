package gui;

import common.Global;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.ReversiData;
import model.ResultData;
import model.ReversiModel;
import reversi.Board;
import reversi.Dimension;
import reversi.Player;
import reversi.Reversi;

/**
 * リバーシのゲーム画面を処理するコントローラー
 */
public class ReversiController {

    /** プレイヤー選択・設定のデータ処理を行うインスタンス（モデル） */
    private ReversiModel model;

    /** タイマーイベントを制御するインスタンス */
    private Timeline timer;

    /** 実際のFPSの計測・算出するインスタンス */
    private Fps fps;

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

    /** 各種デバッグ情報を表示するペイン */
    @FXML
    private VBox debugPane;

    /** 現在のFPS情報を表示するラベル */
    @FXML
    private Label fpsLabel;

    /** 現在の待機フレーム数を表示するラベル */
    @FXML
    private Label waitFrameLabel;

    /** 現在のイベントステータスを表示するラベル */
    @FXML
    private Label eventStatusLabel;

    /** デバッグ情報を表示するラベル */
    @FXML
    private Label debugLabel;

    /**
     * リバーシ盤を初期化する
     * @param sceneSwitch シーン切替処理を行うインスタンス
     * @param reversiData ゲーム処理を実行するために必要なデータの集合
     * @param isDebug デバッグ情報を表示する場合は真 {@code true}, 表示しない場合は {@code false} を返す。
     */
    public void init(SceneSwitch sceneSwitch, ReversiData reversiData) {
        // 引数の正常性確認
        try {
            if (sceneSwitch == null) {
                throw new IllegalArgumentException("引数 \"sceneSwitch\" の値が NULL です");
            }
            if (reversiData == null) {
                throw new IllegalArgumentException("引数 \"reversiData\" の値が NULL です");
            }
        } catch (IllegalArgumentException e) {
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println("引数が想定されていない値のため、プログラムを異常終了します: 終了コード = " + exitCode);
            System.exit(exitCode);
        }

        model = new ReversiModel(reversiData);

        // 初期画面の描画
        Player player = model.getPlayerBlack();
        blackNameLabel.setText(player.getName());
        blackAlgorithmLabel.setText("( " + player.getAlgorithmType().getName() + " )");

        player = model.getPlayerWhite();
        whiteNameLabel.setText(player.getName());
        whiteAlgorithmLabel.setText("( " + player.getAlgorithmType().getName() + " )");

        // リバーシ盤の描画を行う
        Dimension boardSize = model.getReversi().getBoard().getSize();
        boardController = new BoardController(gridPane, boardSize, Global.GRID_SIZE);

        // マスをクリックした時のイベントを設定する
        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                Pane pane = boardController.getBoardPane(i, j);
                pane.setOnMouseClicked(new GridHandler(new Dimension(i, j)));
            }
        }

        // デバッグ情報の表示切替
        if (model.getIsDebug()) {
            this.fps = new Fps();
            debugPane.setVisible(true);
        } else {
            debugPane.setVisible(false);
        }

        // 画面描画イベントを設定する
        timer = new Timeline(new KeyFrame(Duration.millis(1000 / Global.FPS), new EventHandler<ActionEvent>() {

            /**
             * 周期的に実行する処理を行う。
             * @param event イベントのインスタンス
             */
            @Override
            public void handle(ActionEvent event) {
                model.run();

                // デバッグ情報表示が有効の場合、FPSの計測を行う
                if (model.getIsDebug()) {
                    fps.update();
                }

                // 画面描画を行う
                update();

                // ゲームが終了した場合、結果画面を表示する
                if (model.getIsFinish()) {
                    ResultData data = model.generateData();
                    timer.stop();
                    sceneSwitch.generateSceneResult(data);
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
            model.put(dim);
        }
    }

    /**
     * 画面描画を行う関数
     */
    private void update() {
        Reversi reversi = model.getReversi();
        Board board = model.getBoard();

        boardController.update(board, model.getIsControll());

        // 現在の手番、石の個数を更新する
        if (reversi.getPlayerIsBlack()) {
            currentDiscCircle.setFill(Paint.valueOf("black"));
        } else {
            currentDiscCircle.setFill(Paint.valueOf("white"));
        }
        turnLabel.setText(String.format("%d手目", reversi.getTurnCount()));
        blackDiscNumLabel.setText(String.format("黒: %2d個", board.getBlackDiscNum()));
        whiteDiscNumLabel.setText(String.format("白: %2d個", board.getWhiteDiscNum()));

        statusLabel.setText(model.getGameStatusString());

        // デバッグ情報の処理
        if (model.getIsDebug()) {
            fpsLabel.setText(String.format("%.2f fps", fps.getFps()));
            waitFrameLabel.setText(String.format("待ちフレーム数: %3d", model.getWaitFrame()));
            eventStatusLabel.setText(model.getEventStatus());
            debugLabel.setText(model.getDebugString());
        }

        // 最後に石をおいたマスにハイライトを付ける
        Dimension target = model.getLatestTarget();
        if (target != null) {
            boardController.resetFxidAll();
            Pane targetPane = boardController.getBoardPane(target);
            targetPane.setId(Global.FXID_GRID_PUT);
        }
    }
}
