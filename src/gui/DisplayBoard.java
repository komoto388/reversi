package gui;

import common.Convert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import reversi.Board;
import reversi.Dimension;

/**
 * GUIでリバーシ盤の描画を行うクラス
 * @author komoto
 */
public class DisplayBoard {
    /** マスの大きさ */
    private final double GRID_SIZE;

    /** 石の半径 */
    private final double DISC_RADIUS;

    /** リバーシ盤の幅（マス） */
    private final int BOARD_WIDTH;

    /** リバーシ盤の高さ幅（マス） */
    private final int BOARD_HEIGHT;

    /** 描画するリバーシ盤のマスの配列 */
    private Pane[][] boardPane;

    /**
     * 描画するリバーシ盤の情報を設定し、GridPaneにリバーシ盤を描画する。石の描画が行わない。
     * @param gridPane リバーシ盤を描画する GridPane
     * @param boardSize リバーシ盤の大きさ（マス）
     * @param gridSize マス１つのサイズ（縦・横同じ） 
     */
    public DisplayBoard(GridPane gridPane, Dimension boardSize, double gridSize) {
        this.GRID_SIZE = gridSize;
        this.DISC_RADIUS = (gridSize / 2.0 - 10);
        this.BOARD_WIDTH = boardSize.getColumn();
        this.BOARD_HEIGHT = boardSize.getRow();

        boardPane = new Pane[BOARD_HEIGHT][BOARD_WIDTH];

        // リバーシ盤のマス毎に描画する
        for (int i = 0; i < BOARD_HEIGHT + 1; i++) {
            for (int j = 0; j < BOARD_WIDTH + 1; j++) {
                // リバーシ盤の上部に列番号を描画する
                if (i == 0 && j > 0) {
                    Label boardColumnNum = new Label(String.valueOf(Convert.convertIntToChar(j - 1)));
                    boardColumnNum.setId("board-column-num");
                    boardColumnNum.setPrefWidth(GRID_SIZE);
                    boardColumnNum.setPrefHeight(GRID_SIZE / 2);
                    gridPane.add(boardColumnNum, j, i);
                    continue;
                }

                // リバーシ盤の左側に行番号を描画する
                if (j == 0 && i > 0) {
                    Label boardRowNum = new Label(Integer.toString(i));
                    boardRowNum.setId("board-row-num");
                    boardRowNum.setPrefWidth(GRID_SIZE / 2);
                    boardRowNum.setPrefHeight(GRID_SIZE);
                    gridPane.add(boardRowNum, j, i);
                    continue;
                }

                // マスの描画を行う
                if (i > 0 && j > 0) {
                    boardPane[i - 1][j - 1] = new Pane();
                    boardPane[i - 1][j - 1].setPrefWidth(GRID_SIZE);

                    if ((i + j) % 2 == 0) {
                        boardPane[i - 1][j - 1].setId("grid1");
                    } else {
                        boardPane[i - 1][j - 1].setId("grid2");
                    }

                    // コンストラクタで生成した、リバーシ盤のマスを描画する
                    // 行番号・列番号を描画する関係で座標が右下に1ズレているため、-1を付けている
                    gridPane.add(boardPane[i - 1][j - 1], j, i);
                }
            }
        }
    }

    /**
     * 描画するリバーシ盤のマスを返す
     * @param row マスの座標(行)
     * @param column マスの座標(列)
     * @return 描画するリバーシ盤のマスのペイン
     */
    public Pane getBoardPane(int row, int column) {
        return boardPane[row][column];
    }

    /**
     * 描画するリバーシ盤のマスを返す
     * @param target マスの座標
     * @return 描画するリバーシ盤のマスのペイン
     */
    public Pane getBoardPane(Dimension target) {
        return getBoardPane(target.getRow(), target.getColumn());
    }

    /**
     * リバーシ盤に石を描画する
     * @param board リバーシ盤の盤面情報
     */
    public void drawStone(Board board) {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Dimension target = new Dimension(i, j);

                // マスに描画されている石を消去する
                boardPane[i][j].getChildren().clear();

                if (board.isEmpty(target) == false) {
                    // リバーシの石を描画する
                    Circle circle = new Circle(DISC_RADIUS);
                    circle.setLayoutX(GRID_SIZE / 2.0);
                    circle.setLayoutY(GRID_SIZE / 2.0);

                    if (board.isBlack(target)) {
                        circle.setId("disc-black");
                    } else {
                        circle.setId("disc-white");
                    }
                    boardPane[i][j].getChildren().add(circle);
                }
            }
        }
    }

}
