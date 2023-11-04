package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import reversi.RecordRow;

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
     * 画面の棋譜に行を追加して描画する
     * @param index 棋譜を描画する GridPane の行番号(0,1,...)
     * @param record 1手分の棋譜
     */
    public void addRecordRow(int index, RecordRow recordRow) {
        Label turnLabel = new Label(Integer.toString(recordRow.turn));
        Label playerLabel = new Label(recordRow.playerString);
        Label dimLabel = new Label(recordRow.dimString);
        Label blackNumLabel = new Label(String.format("%2d個 (%+3d)", recordRow.blackDiscNum, recordRow.increaseBlackNum));
        Label whiteNumLabel = new Label(String.format("%2d個 (%+3d)", recordRow.whiteDiscNum, recordRow.increaseWhiteNum));

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
    
    /**
     * 対戦が終了した理由として表示する文字列を設定する
     * @param comment 対戦が終了した理由として表示する文字列
     */
    public void setComment(String comment) {
        commentLabel.setText(comment);
    }
}
