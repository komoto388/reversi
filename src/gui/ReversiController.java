package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import reversi.Disc;
import reversi.Reversi;

public class ReversiController {

    @FXML
    private BorderPane display;

    @FXML
    private Label blackDiscNum;

    @FXML
    private Label whiteDiscNum;

    /** リバーシ盤のマス */
    private Pane[][] boardPane;

    final double GRID_SIZE = 60;
    final double DISC_RADIUS = GRID_SIZE / 2.0 - 10;

    public void init(Reversi reversi) {
        Disc[][] board = reversi.getBoard();
        GridPane gridPane = new GridPane();

        boardPane = new Pane[reversi.getBoardSize().getRow()][reversi.getBoardSize().getColumn()];

        for (int i = 0; i < reversi.getBoardSize().getRow(); i++) {
            for (int j = 0; j < reversi.getBoardSize().getColumn(); j++) {
                // マスを作成する
                boardPane[i][j] = new Pane();
                boardPane[i][j].setPrefWidth(GRID_SIZE);
                boardPane[i][j].setPrefHeight(GRID_SIZE);
                
                if ((i + j) % 2 == 0) {
                    boardPane[i][j].setId("even-number-grid");
                } else {
                    boardPane[i][j].setId("odd-number-grid");
                }
                
                // 石が存在している場合、石をマスの上に描画する
                if(board[i][j].isEmpty() == false) {
                    boardPane[i][j].getChildren().add(generateCircle(board[i][j].isBlack()));
                }
                
                gridPane.add(boardPane[i][j], j, i);
            }
        }
        display.setCenter(gridPane);
        display.getCenter().setStyle("-fx-alignment: CENTER");
    }

    private Circle generateCircle(Boolean isBlack) {
        Circle circle = new Circle(DISC_RADIUS);
        circle.setLayoutX(GRID_SIZE / 2.0);
        circle.setLayoutY(GRID_SIZE / 2.0);

        if (isBlack) {
            circle.setId("disc-black");
        } else {
            circle.setId("disc-white");
        }
        
        return circle;
    }
}
