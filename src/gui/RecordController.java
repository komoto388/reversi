package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import reversi.Record;
import reversi.RecordList;
import reversi.Reversi;

public class RecordController {

    @FXML
    private Label recordHeaderLabel;

    @FXML
    private GridPane recordPane;

    /**
     * 棋譜を表示する
     * @param reversi リバーシのゲーム情報を持つインスタンス
     */
    public void init(Reversi reversi) {
        RecordList recordList = reversi.getRecordList();

        for (int i = 1; recordList.isEmpty() == false; i++) {
            Record record = recordList.poll();

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

            recordPane.add(turnLabel, 0, i);
            recordPane.add(playerLabel, 1, i);
            recordPane.add(dimLabel, 2, i);
            recordPane.add(blackNumLabel, 3, i);
            recordPane.add(whiteNumLabel, 4, i);
        }
    }
}
