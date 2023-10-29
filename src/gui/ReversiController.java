package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import reversi.Board;
import reversi.Dimension;
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
        Board board = reversi.getBoard();
        GridPane gridPane = new GridPane();

        boardPane = new Pane[board.getSize().getRow()][board.getSize().getColumn()];

        for (int i = 0; i < board.getSize().getRow(); i++) {
            for (int j = 0; j < board.getSize().getColumn(); j++) {
                Dimension target = new Dimension(i, j);
                
                // 描画用マスを作成する
                boardPane[i][j] = new Pane();
                boardPane[i][j].setPrefWidth(GRID_SIZE);
                boardPane[i][j].setPrefHeight(GRID_SIZE);
                
                if ((i + j) % 2 == 0) {
                    boardPane[i][j].setId("grid1");
                } else {
                    boardPane[i][j].setId("grid2");
                }
                
                // 石が初期から存在している場合、石をマスの上に描画する
                if(board.isEmpty(target) == false) {
                    boardPane[i][j].getChildren().add(generateCircle(board.isBlack(target)));
                }
                
                gridPane.add(boardPane[i][j], j, i);
            }
        }
        
        blackDiscNum.setText(String.format("黒: %2d", board.getBlackDiscNum()));
        whiteDiscNum.setText(String.format("白: %2d", board.getWhiteDiscNum()));
        
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
