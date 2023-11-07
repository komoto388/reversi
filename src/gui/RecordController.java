package gui;

import gamerecord.GameRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

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

    /** 対戦が終了した理由を表すラベル */
    @FXML
    private Label commentLabel;

    /**
     * 画面の棋譜を描画する
     * @param record 棋譜
     */
    public void setRecord(GameRecord record) {
        for(int i = 0; i < record.size(); i++) {
            add(record, i);
        }

        // 対戦が終了した理由として表示する文字列を設定する
        commentLabel.setText("終了した理由: " + record.getComment());
    }

    /**
     * １手分のデータを取り出し、画面の棋譜に追加する
     * @param record 棋譜
     * @param index リストの要素番号
     */
    private void add(GameRecord record, int index) {
        String str;

        Label turnLabel = new Label();
        turnLabel.setText(Integer.toString(record.getTurn(index)));
        turnLabel.setMaxWidth(Double.MAX_VALUE);

        Label playerLabel = new Label();
        playerLabel.setText(record.getPlayerString(index));
        playerLabel.setMaxWidth(Double.MAX_VALUE);

        Label dimLabel = new Label();
        dimLabel.setText(record.getDimString(index));
        dimLabel.setMaxWidth(Double.MAX_VALUE);

        Label blackNumLabel = new Label();
        str = String.format("%2d個 (%+3d)", record.getBlackDiscNum(index), record.getIncreaseBlackNum(index));
        blackNumLabel.setText(str);
        blackNumLabel.setMaxWidth(Double.MAX_VALUE);

        Label whiteNumLabel = new Label();
        str = String.format("%2d個 (%+3d)", record.getWhiteDiscNum(index), record.getIncreaseWhiteNum(index));
        whiteNumLabel.setText(str);
        whiteNumLabel.setMaxWidth(Double.MAX_VALUE);

        // 画面では1行目がヘッダーなので、次の行から棋譜のデータを描画する
        index++;
        
        recordPane.add(turnLabel, 0, index);
        recordPane.add(playerLabel, 1, index);
        recordPane.add(dimLabel, 2, index);
        recordPane.add(blackNumLabel, 3, index);
        recordPane.add(whiteNumLabel, 4, index);
    }
}
