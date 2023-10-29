package gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import reversi.Board;
import reversi.Dimension;
import reversi.Reversi;

public class ReversiController {

    /** 1秒間に画面描画する回数(FPS) */
    private final int FPS = 30;

    /** マスの大きさ（縦・横同じ） */
    private final double GRID_SIZE = 60;

    /** 石の半径 */
    private final double DISC_RADIUS = GRID_SIZE / 2.0 - 10;

    /** リバーシを制御するインスタンス */
    private Reversi reversi;

    /** リバーシ盤のマス */
    private Pane[][] boardPane;

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

        // 画面描画イベントを設定する
        Timeline timer = new Timeline(new KeyFrame(new Duration(1000 / FPS), new TimerHandler()));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    /**
     * 画面描写を行うクラス
     */
    private class TimerHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            update();
        }

        /**
         * 画面描画を行う
         */
        private void update() {
            Board board = reversi.getBoard();

            // リバーシ盤の状態を更新する
            for (int i = 0; i < board.getSize().getRow(); i++) {
                for (int j = 0; j < board.getSize().getColumn(); j++) {
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

            // 現在の手番、石の個数を更新する
            if (reversi.getPlayerIsBlack()) {
                currentDisc.setFill(Paint.valueOf("black"));
            } else {
                currentDisc.setFill(Paint.valueOf("white"));
            }
            turnLabel.setText(String.format("%d手目", reversi.getTurnCount()));
            blackDiscNum.setText(String.format("黒: %2d個", board.getBlackDiscNum()));
            whiteDiscNum.setText(String.format("白: %2d個", board.getWhiteDiscNum()));
        }
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
            // 石を置く処理を行う
            if (reversi.put(dim)) {
                // 勝敗判定を行う
                switch (reversi.judge()) {
                case None: {
                    reversi.next();
                    break;
                }
                case Drow: {
                    System.out.printf("引き分けです。\n");
                    break;
                }
                case Black: {
                    System.out.printf("黒の勝ちです。\n");
                    break;
                }
                case White: {
                    System.out.printf("白の勝ちです。\n");
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unexpected value: " + reversi.judge());
                }
                reversi.next();
            } else {
                System.out.printf("%sのマスには石を置けません。\n", dim.getString());
            }
        }
    }
}
