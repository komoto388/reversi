package gui;

import common.Global;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import reversi.ResultType;
import reversi.Reversi;

/**
 * シーン切替処理を行うクラス
 * @author komoto
 */
class SceneSwitch {

    /** フレーム情報 */
    private Stage primaryStage;

    /**
     * 初期化する
     * @param primaryStage ウィンドウのインスタンス
     */
    public SceneSwitch(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * ウィンドウを閉じる
     */
    public void close() {
        primaryStage.close();
    }

    /**
     * プレイヤーの使用アルゴリズムを選択する画面を表示する
     */
    public void generateScenePlayerSelect() {
        FXMLLoader fxmlloader = null;
        VBox rootPane = null;
        String fxmlFile = "../fxml/PlayerSelect.fxml";

        try {
            fxmlloader = new FXMLLoader(getClass().getResource(fxmlFile));
            rootPane = (VBox) fxmlloader.load();
        } catch (Exception e) {
            exceptionProcess(e, fxmlFile);
        }
        rootPane.setPrefSize(Global.ROOT_PANE_WIDTH, Global.ROOT_PANE_HEIGHT);

        PlayerSelectController controller = (PlayerSelectController) fxmlloader.getController();
        controller.init(this);

        // 描画する画面をフレームに設定する
        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add(getClass().getResource("../css/player-select.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * リバーシのゲーム画面を生成し、表示する
     * @param reversi リバーシのゲーム処理を行うインスタンス
     */
    public void generateSceneReversi(Reversi reversi) {
        FXMLLoader fxmlloader = null;
        BorderPane reversiPane = null;
        String fxmlFile = "../fxml/Reversi.fxml";

        try {
            fxmlloader = new FXMLLoader(getClass().getResource(fxmlFile));
            reversiPane = (BorderPane) fxmlloader.load();
        } catch (Exception e) {
            exceptionProcess(e, fxmlFile);
        }

        ReversiController controller = (ReversiController) fxmlloader.getController();
        controller.init(this, reversi);

        // 画面内に複数ペインを描画するために、StackPaneでルートペインを作成する
        AnchorPane rootPane = new AnchorPane();
        rootPane.setPrefSize(Global.ROOT_PANE_WIDTH, Global.ROOT_PANE_HEIGHT);
        rootPane.getChildren().add(reversiPane);
        AnchorPane.setTopAnchor(reversiPane, 0d);
        AnchorPane.setBottomAnchor(reversiPane, 0d);
        AnchorPane.setLeftAnchor(reversiPane, 0d);
        AnchorPane.setRightAnchor(reversiPane, 0d);

        // 描画する画面をフレームに設定する
        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add(getClass().getResource("../css/default.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * 結果画面を生成し、表示する
     * @param reversi リバーシのゲーム処理を行うインスタンス
     * @param result 勝敗結果を表す
     */
    public void generateSceneResult(Reversi reversi, ResultType result) {
        FXMLLoader fxmlloader = null;
        BorderPane resultPane = null;
        String fxmlFile = "../fxml/Result.fxml";

        try {
            fxmlloader = new FXMLLoader(getClass().getResource(fxmlFile));
            resultPane = (BorderPane) fxmlloader.load();
        } catch (Exception e) {
            exceptionProcess(e, fxmlFile);
        }

        ResultController controller = (ResultController) fxmlloader.getController();
        controller.init(this, reversi, result);

        // 現在の画面のシーンとルートペインを取得する
        Scene scene = primaryStage.getScene();
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

    /**
     * FXMLファイル読み込み時に発生した時の例外処理
     * @param e 例外イベントのインスタンス
     * @param fxmlFile 読み込んだFXMLファイル名の文字列
     */
    private void exceptionProcess(Exception e, String fxmlFile) {
        e.printStackTrace();
        System.err.println(fxmlFile + "の読み込みで例外が発生したため、プログラムを異常終了します: 終了コード = " + Global.EXIT_FAILURE);
        System.exit(Global.EXIT_FAILURE);
    }
}
