package gui;

import algorithm.AlgorithmType;
import javafx.application.Application;
import javafx.stage.Stage;
import reversi.Reversi;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * GUIのメイン処理を行うクラス
 * @author komoto
 */
public class GuiMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxmlloader = null;
        Pane root = null;
        Reversi reversi = new Reversi(AlgorithmType.Random, AlgorithmType.Random);

        primaryStage.setTitle("リバーシ");
        primaryStage.setWidth(800);
        primaryStage.setHeight(800);
        primaryStage.setResizable(false);

        try {
            fxmlloader = new FXMLLoader(getClass().getResource("../fxml/Reversi.fxml"));
            root = (Pane) fxmlloader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ReversiController controller = (ReversiController) fxmlloader.getController();
        controller.init(primaryStage, reversi);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../css/application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
