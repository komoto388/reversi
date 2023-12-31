package gui;

import common.Convert;
import common.Global;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import reversi.Board;
import reversi.Dimension;
import reversi.Disc;

/**
 * GUIでリバーシ盤の描画を行うクラス
 * @author komoto
 */
public class BoardController {

    /** マスの大きさ */
    private final double gridSize;

    /** 石の半径 */
    private final double discRadius;

    /** リバーシ盤の幅（マス） */
    private final int boardWidth;

    /** リバーシ盤の高さ幅（マス） */
    private final int boardHeight;

    /** 描画するリバーシ盤のマスの配列 */
    private Pane[][] boardPane;

    /**
     * 描画するリバーシ盤の情報を設定し、GridPaneにリバーシ盤を描画する。石の描画が行わない。
     * @param gridPane リバーシ盤を描画する GridPane
     * @param boardSize リバーシ盤の大きさ（マス）
     * @param gridSize マス１つのサイズ（縦・横同じ） 
     */
    public BoardController(GridPane gridPane, Dimension boardSize, double gridSize) {
        // 引数の正常性確認
        try {
            if (gridPane == null) {
                throw new IllegalArgumentException("引数 \"gridPane\" の値が NULL です");
            }
            if (boardSize == null) {
                throw new IllegalArgumentException("引数 \"boardSize\" の値が NULL です");
            }
            if (gridSize <= 0) {
                throw new IllegalArgumentException("引数 \"gridSize\" の値が0以下です: " + gridSize);
            }
        } catch (IllegalArgumentException e) {
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println("引数が想定されていない値のため、プログラムを異常終了します: 終了コード = " + exitCode);
            System.exit(exitCode);
        }

        this.gridSize = gridSize;
        this.discRadius = (gridSize / 2.0 - 10);
        this.boardWidth = boardSize.getColumn();
        this.boardHeight = boardSize.getRow();

        gridPane.setPadding(new Insets(-gridSize, 0, 0, -gridSize));

        boardPane = new Pane[boardHeight][boardWidth];

        // リバーシ盤のマス毎に描画する
        for (int i = 0; i < boardHeight + 1; i++) {
            for (int j = 0; j < boardWidth + 1; j++) {
                // リバーシ盤の上部に列番号を描画する
                if (i == 0 && j > 0) {
                    Label label = generateRecordLabel("board-column-num",
                            String.valueOf(Convert.convertIntToChar(j - 1)));
                    gridPane.add(label, j, i);
                    continue;
                }

                // リバーシ盤の左側に行番号を描画する
                if (j == 0 && i > 0) {
                    Label label = generateRecordLabel("board-row-num", Integer.toString(i));
                    gridPane.add(label, j, i);
                    continue;
                }

                // マスの描画を行う
                if (i > 0 && j > 0) {
                    Pane pane = new Pane();
                    pane.setPrefWidth(gridSize);
                    pane.setPrefHeight(gridSize);

                    setFxid(pane, i - 1, j - 1);

                    // コンストラクタで生成した、リバーシ盤のマスを描画する
                    // 行番号・列番号を描画する関係で座標が右下に1ズレているため、boardPaneの要素番号に1を減算する。
                    boardPane[i - 1][j - 1] = pane;
                    gridPane.add(pane, j, i);
                }
            }
        }
    }

    /**
     * リバーシ盤の行番号・列番号のラベルを生成する
     * @param fxId ラベルに付与するfxid
     * @param numString 行番号または列番号に表示する文字列
     * @return 生成した行番号・列番号のラベル
     */
    private Label generateRecordLabel(String fxId, String numString) {
        Label boardLabel = new Label(numString);
        boardLabel.setId(fxId);
        boardLabel.setPrefWidth(gridSize);
        boardLabel.setPrefHeight(gridSize);

        return boardLabel;
    }

    /**
     * 描画するリバーシ盤のマスを取得する
     * @param row マスの座標(行)
     * @param column マスの座標(列)
     * @return 描画するリバーシ盤のマスのペイン
     */
    public Pane getBoardPane(int row, int column) {
        return boardPane[row][column];
    }

    /**
     * 描画するリバーシ盤のマスを取得する
     * @param target マスの座標
     * @return 描画するリバーシ盤のマスのペイン
     */
    public Pane getBoardPane(Dimension target) {
        return getBoardPane(target.getRow(), target.getColumn());
    }

    /**
     * リバーシ盤を更新し、石の再描画とプレイヤーの操作可否を設定する
     * @param board リバーシ盤の盤面情報
     * @param isControll リバーシ盤をプレイヤーが操作できる場合は真 {@code true}、できない場合は偽 {@code false}
     */
    public void update(Board board, Boolean isControll) {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                Dimension target = new Dimension(i, j);

                // マスに描画されている石を消去する
                boardPane[i][j].getChildren().clear();

                if (board.isDiscEmpty(target) == false) {
                    // リバーシの石を描画する
                    Circle circle = new Circle(discRadius);
                    circle.setLayoutX(gridSize / 2.0);
                    circle.setLayoutY(gridSize / 2.0);

                    if (board.isDiscBlack(target)) {
                        circle.setId(Disc.BLACK.getFxId());
                    } else {
                        circle.setId(Disc.WHITE.getFxId());
                    }
                    boardPane[i][j].getChildren().add(circle);
                }

                // マスの操作可否を設定する
                boardPane[i][j].setDisable(!isControll);
            }
        }
    }

    /**
     * 全てのマスのペインに対して、fxid を初期値に戻す
     */
    public void resetFxidAll() {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                setFxid(boardPane[i][j], i, j);
            }
        }
    }

    /**
     * マスのペインに fxid を割り当てる<br>
     * 列番号と行番号の和が偶数かどうかで割り当てる fxid を変える 
     * @param pane fxidを割り当てる対象のペイン
     * @param row リバーシ盤の行番号
     * @param column リバーシ盤の列番号
     */
    private void setFxid(Pane pane, int row, int column) {
        if ((row + column) % 2 == 0) {
            pane.setId(Global.FXID_GRID_1);
        } else {
            pane.setId(Global.FXID_GRID_2);
        }
    }
}
