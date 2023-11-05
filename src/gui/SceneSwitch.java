package gui;

import common.Global;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
        scene.getStylesheets().add(getClass().getResource("../css/default.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("../css/player-select.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * リバーシのゲーム画面を生成し、表示する
     * @param reversi リバーシのゲーム処理を行うインスタンス
     * @param isDebug デバッグ情報を表示する場合は真 {@code true}, 表示しない場合は {@code false} を返す。
     */
    public void generateSceneReversi(Reversi reversi, Boolean isDebug) {
        FXMLLoader fxmlloader = null;
        BorderPane rootPane = null;
        String fxmlFile = "../fxml/Reversi.fxml";

        try {
            fxmlloader = new FXMLLoader(getClass().getResource(fxmlFile));
            rootPane = (BorderPane) fxmlloader.load();
        } catch (Exception e) {
            exceptionProcess(e, fxmlFile);
        }
        rootPane.setPrefSize(Global.ROOT_PANE_WIDTH, Global.ROOT_PANE_HEIGHT);

        ReversiController controller = (ReversiController) fxmlloader.getController();
        controller.init(this, reversi, isDebug);

        // 描画する画面をフレームに設定する
        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add(getClass().getResource("../css/default.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("../css/reversi.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("../css/board.css").toExternalForm());

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
        BorderPane rootPane = null;
        String fxmlFile = "../fxml/Result.fxml";

        try {
            fxmlloader = new FXMLLoader(getClass().getResource(fxmlFile));
            rootPane = (BorderPane) fxmlloader.load();
        } catch (Exception e) {
            exceptionProcess(e, fxmlFile);
        }
        rootPane.setPrefSize(Global.ROOT_PANE_WIDTH, Global.ROOT_PANE_HEIGHT);

        ResultController controller = (ResultController) fxmlloader.getController();
        controller.init(this, reversi, result);

        // 描画する画面をフレームに設定する
        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add(getClass().getResource("../css/default.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("../css/result.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("../css/board.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
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
