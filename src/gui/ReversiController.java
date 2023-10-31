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

    /** 石をおいた後の待ち時間(ミリ秒) */
    private final int WAIT_MILLISEC = 1000;

    /** 手動操作時の待ち時間 */
    private final int WAIT_INFINITE = -1;

    /** リバーシを制御するインスタンス */
    private Reversi reversi;

    /** リバーシ盤の手動入力を無効する時間（フレーム数） */
    private int waitFrame;

    /** リバーシ盤のマス */
    private Pane[][] boardPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private Circle currentDisc;

    @FXML
    private Label turnLabel;

    @FXML
    private Label blackDiscNum;

    @FXML
    private Label whiteDiscNum;

    @FXML
    private Label statusLabel;

    /**
     * リバーシ盤を初期化する
     * @param board リバーシ盤の状態を表す
     */
    public void init(Reversi reversi) {
        this.reversi = reversi;
        Board board = reversi.getBoard();
        boardPane = new Pane[board.getSize().getRow()][board.getSize().getColumn()];
        waitFrame = 0;

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
     * 画面描画など周期的に実行するイベントを定義するクラス
     */
    private class TimerHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            gameRun();
            update();
        }

        /**
         * リバーシのスキップ判定、プレイヤーのプレイ処理、勝敗判定を行う
         */
        private void gameRun() {
            ///------ デバッグ用 -----------------------------
            if (waitFrame > 0) {
                statusLabel.setText(String.format("処理中です。残りフレーム: %d", waitFrame));
                waitFrame--;
            } else {
                statusLabel.setText("入力待ちです。");
            }
            ///------ デバッグ用 -----------------------------

            // 手動入力待ち状態出ない場合、スキップ判定とCOM側操作を行う。
            if (waitFrame == 0) {
                if (reversi.isSkip()) {
                    // 石がどこにも置けない時のスキップ処理を定義
                    System.out.printf("石を置く場所がないため、スキップします。\n");
                    reversi.next();
                } else {
                    // 石を置く処理を行う。手入力の場合は入力待ち、それ以外はアルゴリズムに従い処理を行う
                    System.out.printf("石を置く処理を行います。\n");
                    
                    if (reversi.isCurrentPlayerManual()) {
                        // 手動入力待ち状態にする。手動入力はここではなく、マウスのイベントハンドラーで処理する。
                        System.out.printf("今のプレイヤーはマニュアルです。\n");
                        waitFrame = WAIT_INFINITE;
                        setGridPaneDisable(false);
                    } else {
                        System.out.printf("今のプレイヤーはCOMです。\n");
                        Dimension target = null;
                        play(target);
                        waitFrame = FPS * WAIT_MILLISEC / 1000;
                        setGridPaneDisable(true);
                    }
                }
            }
        }

        /**
         * 画面描画を行う関数
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
            play(dim);
        }
    }

    private Boolean play(Dimension target) {
        if (reversi.put(target)) {
            statusLabel.setText(String.format("%sに石を置きました。\n", target.getString()));

            // 勝敗判定を行う
            switch (reversi.judge()) {
            case None: {
                reversi.next();
                waitFrame = FPS * WAIT_MILLISEC / 1000;
                break;
            }
            case Drow: {
                System.out.printf("引き分けです。\n");
                waitFrame = WAIT_INFINITE;
                break;
            }
            case Black: {
                System.out.printf("黒の勝ちです。\n");
                waitFrame = WAIT_INFINITE;
                break;
            }
            case White: {
                System.out.printf("白の勝ちです。\n");
                waitFrame = WAIT_INFINITE;
                break;
            }
            default:
                throw new IllegalArgumentException("Unexpected value: " + reversi.judge());
            }
            setGridPaneDisable(true);
            return true;
        } else {
            System.out.printf("%sのマスには石を置けません。\n", target.getString());
            return false;
        }
    }

    /**
     * リバーシ盤のクリックアクションを無効化する
     * @param isDisable アクションを無効にする場合は {@code true}, 有効にする場合は {@code false} を設定する。 
     */
    private void setGridPaneDisable(Boolean isDisable) {
        Dimension boardSize = reversi.getBoard().getSize();

        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                boardPane[i][j].setDisable(isDisable);
            }
        }
    }
}
