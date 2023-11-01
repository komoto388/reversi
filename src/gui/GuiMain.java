package gui;

import algorithm.AlgorithmType;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import reversi.Reversi;

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
        FXMLLoader fxmlloader = null;
        Pane reversiPane = null;
        Reversi reversi = new Reversi(AlgorithmType.Random, AlgorithmType.Random);

        // フレームの設定
        primaryStage.setTitle("リバーシ");
        primaryStage.setWidth(ROOT_PANE_WIDTH);
        primaryStage.setHeight(ROOT_PANE_HEIGHT);
        primaryStage.setResizable(false);

        // リバーシのゲーム画面の呼び出し
        try {
            fxmlloader = new FXMLLoader(getClass().getResource("../fxml/Reversi.fxml"));
            reversiPane = (Pane) fxmlloader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ReversiController controller = (ReversiController) fxmlloader.getController();
        controller.init(primaryStage, reversi);

        // 画面内に複数ペインを描画するために、StackPaneでルートペインを作成する
        AnchorPane rootPane = new AnchorPane();
        rootPane.setPrefSize(ROOT_PANE_WIDTH, ROOT_PANE_HEIGHT);
        rootPane.getChildren().add(reversiPane);
        AnchorPane.setTopAnchor(reversiPane, 0d);
        AnchorPane.setBottomAnchor(reversiPane, 0d);
        AnchorPane.setLeftAnchor(reversiPane, 0d);
        AnchorPane.setRightAnchor(reversiPane, 0d);

        // 描画する画面をフレームに設定する
        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add(getClass().getResource("../css/application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
