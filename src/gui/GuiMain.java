package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;

/**
 * GUIのメイン処理を行うクラス
 * @author komoto
 */
public class GuiMain extends Application {

    /** ウィンドウの横幅 */
    private final int ROOT_PANE_WIDTH = 1000;

    /** ウィンドウの高さ */
    private final int ROOT_PANE_HEIGHT = 800;

    /**
     * メインメソッド
     * @param args プログラム実行時の引数
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * プログラム起動時の画面を描画する。
     * @param primaryStage ウィンドウのインスタンス
     */
    @Override
    public void start(Stage primaryStage) {
        // フレームの設定
        primaryStage.setTitle("リバーシ");
        primaryStage.setResizable(false);
        
        startPlayerSelect(primaryStage);
    }

    /**
     * プレイヤーの使用アルゴリズムを選択する画面を表示する
     * @param primaryStage ウィンドウのインスタンス
     */
    private void startPlayerSelect(Stage primaryStage) {
        FXMLLoader fxmlloader = null;
        VBox rootPane = null;

        try {
            fxmlloader = new FXMLLoader(getClass().getResource("../fxml/PlayerSelect.fxml"));
            rootPane = (VBox) fxmlloader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        rootPane.setPrefSize(ROOT_PANE_WIDTH, ROOT_PANE_HEIGHT);

        PlayerSelectController controller = (PlayerSelectController) fxmlloader.getController();
        controller.init(primaryStage);
        
        // 描画する画面をフレームに設定する
        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add(getClass().getResource("../css/player-select.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
