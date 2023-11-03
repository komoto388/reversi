package gui;

import common.Global;
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
     * 棋譜を表示する
     * @param reversi リバーシのゲーム情報を持つインスタンス
     */
    public void init(Reversi reversi) {
        // 引数の正常性確認
        try {
            if (reversi == null) {
                throw new IllegalArgumentException("引数 \"reversi\" が NULL です。");
            }
        } catch (IllegalArgumentException e) {
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println("引数が想定されていない値のため、プログラムを異常終了します: 終了コード = " + exitCode);
            System.exit(exitCode);
        }

        // 棋譜の作成・描画
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
