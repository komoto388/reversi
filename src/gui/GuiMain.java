package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import reversi.Reversi;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

public class GuiMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxmlloader = null;
        Pane root = null;
        Reversi reversi = new Reversi();

        primaryStage.setTitle("リバーシ");
        primaryStage.setResizable(false);

        try {
            fxmlloader = new FXMLLoader(getClass().getResource("../fxml/Reversi.fxml"));
            root = (Pane) fxmlloader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ReversiController controller = (ReversiController) fxmlloader.getController();
        controller.init(reversi);
        
        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(getClass().getResource("../css/application.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
