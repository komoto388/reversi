package gui;

import common.Convert;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import reversi.Board;
import reversi.Dimension;
import reversi.Reversi;

public class DetailResultController {

    @FXML
    private VBox detailResultRootPane;

    @FXML
    private GridPane gridPane;

    /**
     * 詳細結果（最終盤面）を表示する
     * @param reversi リバーシのゲーム情報を持つインスタンス
     */
    public void init(Reversi reversi) {
        // マスの大きさ（縦・横同じ）
        final double GRID_SIZE = 50;

        // 石の半径
        final double DISC_RADIUS = (GRID_SIZE / 2.0 - 10);

        Board board = reversi.getBoard();
        final int BOARD_WIDTH = board.getSize().getColumn();
        final int BOARD_HEIGHT = board.getSize().getRow();
        Pane[][] boardPane = new Pane[BOARD_HEIGHT][BOARD_WIDTH];

        for (int i = 0; i < BOARD_WIDTH + 1; i++) {
            for (int j = 0; j < BOARD_HEIGHT + 1; j++) {
                if (i == 0) {
                    // リバーシ盤の列番号を付与する
                    if (j > 0) {
                        Label boardColumnNum = new Label(String.valueOf(Convert.convertIntToChar(j - 1)));
                        boardColumnNum.setId("board-column-num");
                        boardColumnNum.setPrefWidth(GRID_SIZE);
                        boardColumnNum.setPrefHeight(GRID_SIZE / 2);
                        gridPane.add(boardColumnNum, j, i);
                    }
                } else if (j == 0) {
                    // リバーシ盤の行番号を付与する
                    if (i > 0) {
                        Label boardRowNum = new Label(Integer.toString(i));
                        boardRowNum.setId("board-row-num");
                        boardRowNum.setPrefWidth(GRID_SIZE / 2);
                        boardRowNum.setPrefHeight(GRID_SIZE);
                        gridPane.add(boardRowNum, j, i);
                    }
                } else {
                    // リバーシ盤のマスを生成する
                    Dimension target = new Dimension(i - 1, j - 1);

                    // 描画用マスを作成する
                    boardPane[i - 1][j - 1] = new Pane();
                    boardPane[i - 1][j - 1].setPrefWidth(GRID_SIZE);
                    boardPane[i - 1][j - 1].setPrefHeight(GRID_SIZE);

                    if ((i + j) % 2 == 0) {
                        boardPane[i - 1][j - 1].setId("grid1");
                    } else {
                        boardPane[i - 1][j - 1].setId("grid2");
                    }

                    // リバーシの石を描画する
                    if (board.isEmpty(target) == false) {
                        Circle circle = new Circle(DISC_RADIUS);
                        circle.setLayoutX(GRID_SIZE / 2.0);
                        circle.setLayoutY(GRID_SIZE / 2.0);

                        if (board.isBlack(target)) {
                            circle.setId("disc-black");
                        } else {
                            circle.setId("disc-white");
                        }
                        boardPane[i - 1][j - 1].getChildren().add(circle);
                    }

                    gridPane.add(boardPane[i - 1][j - 1], j, i);
                }
            }
        }
    }
}
