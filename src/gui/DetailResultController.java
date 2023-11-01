package gui;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import reversi.Board;

/**
 * 詳細結果を画面表示するクラス
 * @author komoto
 */
public class DetailResultController {
    /** 詳細結果を表示する画面のルートペイン */
    @FXML
    private VBox detailResultRootPane;

    /** リバーシ盤を描画するグリッドペイン */
    @FXML
    private GridPane gridPane;

    /**
     * 詳細結果（最終盤面）を表示する
     * @param board リバーシ盤の盤面情報を持つインスタンス
     */
    public void init(Board board) {
        // マスの大きさ（縦・横同じ）
        final double GRID_SIZE = 50;

        DisplayBoard displayBoard = new DisplayBoard(gridPane, board.getSize(), GRID_SIZE);
        displayBoard.drawStone(board);
    }
}
