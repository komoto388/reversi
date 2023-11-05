package gui;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * GUIのメイン処理を行うクラス
 * @author komoto
 */
public class GuiMain extends Application {

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

        Image icon = new Image(getClass().getResourceAsStream("../resource/icon.png"));
        primaryStage.getIcons().add(icon);

        // 画面呼び出し
        SceneSwitch sceneSwitch = new SceneSwitch(primaryStage);
        sceneSwitch.generateScenePlayerSelect();
    }
}
