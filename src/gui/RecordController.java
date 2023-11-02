package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import reversi.Record;
import reversi.RecordList;
import reversi.Reversi;

/**
 * 棋譜を表示するクラス
 * @author komoto
 */
public class RecordController {

    /** 棋譜を描画するグリッドペイン */
    @FXML
    private GridPane recordPane;

    /** 棋譜のヘッダー情報 */
    @FXML
    private Label recordHeaderLabel;

    /**
     * 棋譜を追加して描画する
     * @param index 棋譜を描画する GridPane の行番号(0,1,...)
     * @param record 1手分の棋譜
     */
    public void addRecord(int index, Record record) {
        Label turnLabel = new Label(Integer.toString(record.turn));
        Label playerLabel = new Label(record.playerString);
        Label dimLabel = new Label(record.dimString);
        Label blackNumLabel = new Label(String.format("%2d個 (%+3d)", record.blackDiscNum, record.increaseBlackNum));
        Label whiteNumLabel = new Label(String.format("%2d個 (%+3d)", record.whiteDiscNum, record.increaseWhiteNum));

        turnLabel.setMaxWidth(Double.MAX_VALUE);
        playerLabel.setMaxWidth(Double.MAX_VALUE);
        dimLabel.setMaxWidth(Double.MAX_VALUE);
        blackNumLabel.setMaxWidth(Double.MAX_VALUE);
        whiteNumLabel.setMaxWidth(Double.MAX_VALUE);

        recordPane.add(turnLabel, 0, index);
        recordPane.add(playerLabel, 1, index);
        recordPane.add(dimLabel, 2, index);
        recordPane.add(blackNumLabel, 3, index);
        recordPane.add(whiteNumLabel, 4, index);
    }
}
