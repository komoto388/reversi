package gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import reversi.Board;
import reversi.Dimension;
import reversi.Reversi;

public class ReversiController {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label blackDiscNum;

    @FXML
    private Label whiteDiscNum;

    @FXML
    private Circle currentDisc;

    @FXML
    private Label turnLabel;

    /** リバーシ盤のマス */
    private Pane[][] boardPane;

    private final double GRID_SIZE = 60;
    private final double DISC_RADIUS = GRID_SIZE / 2.0 - 10;

    private Reversi reversi;

    /**
     * リバーシ盤を初期化する
     * @param board リバーシ盤の状態を表す
     */
    public void init(Reversi reversi) {
        this.reversi = reversi;
        Board board = reversi.getBoard();
        boardPane = new Pane[board.getSize().getRow()][board.getSize().getColumn()];

        for (int i = 0; i < board.getSize().getRow(); i++) {
            for (int j = 0; j < board.getSize().getColumn(); j++) {
                Dimension target = new Dimension(i, j);

                // 描画用マスを作成する
                boardPane[i][j] = new Pane();
                boardPane[i][j].setPrefWidth(GRID_SIZE);
                boardPane[i][j].setPrefHeight(GRID_SIZE);
                boardPane[i][j].setOnMouseClicked(new GridHandler(target));

                if ((i + j) % 2 == 0) {
                    boardPane[i][j].setId("grid1");
                } else {
                    boardPane[i][j].setId("grid2");
                }
                gridPane.add(boardPane[i][j], j, i);
            }
        }
        update();
    }

    /**
     * リバーシ盤の描画状態を更新する。
     * @param board リバーシ盤の状態を表す
     */
    private void update() {
        Board board = reversi.getBoard();

        for (int i = 0; i < board.getSize().getRow(); i++) {
            for (int j = 0; j < board.getSize().getColumn(); j++) {
                Dimension target = new Dimension(i, j);

                // マスに描画されている石がある場合は消去する
                boardPane[i][j].getChildren().clear();

                if (board.isEmpty(target) == false) {
                    boardPane[i][j].getChildren().add(generateCircle(board.isBlack(target)));
                }
            }
        }
        if (reversi.getPlayerIsBlack()) {
            currentDisc.setFill(Paint.valueOf("black"));
        } else {
            currentDisc.setFill(Paint.valueOf("white"));
        }
        turnLabel.setText(String.format("%d手目", reversi.getTurnCount()));
        blackDiscNum.setText(String.format("黒: %2d個", board.getBlackDiscNum()));
        whiteDiscNum.setText(String.format("白: %2d個", board.getWhiteDiscNum()));
    }

    /**
     * マスに描画する石を生成する
     * @param isBlack 描画する石の色を表す。黒の場合は真 {@code true}, 白の場合は偽 {@code false} を指定する。
     * @return 生成した石のインスタンス
     */
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

    /**
     * リバーシ盤のマスをクリックした時の動作を定義するクラス
     */
    private class GridHandler implements EventHandler<MouseEvent> {
        /** マスの座標を表す */
        private Dimension dim;

        /**
         * マスの座標を設定する
         * @param target マスの座標
         */
        public GridHandler(Dimension target) {
            this.dim = target;
        }

        /**
         * マスをクリックした時、石を置く
         */
        @Override
        public void handle(MouseEvent event) {
            if (reversi.put(dim)) {
                reversi.next();
                update();
            } else {
                System.out.printf("%sのマスには石を置けません。\n", dim.getString());
            }
        }
    }
}
