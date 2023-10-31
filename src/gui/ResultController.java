package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import reversi.ResultType;
import reversi.Reversi;

/**
 * 対戦結果画面の処理を行うクラス
 * @author komoto
 */
public class ResultController {
    /** 対戦結果を表すラベル */
    @FXML
    private Label resultLabel;
    
    /** 黒石の個数を表すラベル */
    @FXML
    private Label blackDiscNumLabel;

    /** 白石の個数を表すラベル */
    @FXML
    private Label whiteDiscNumLabel;  

    /** 棋譜を表示するスクロールペイン */
    @FXML
    private ScrollPane recordPane;
    
    public void init(Reversi reversi, ResultType result) {
        // 対戦結果を表示する
        blackDiscNumLabel.setText(Integer.toString(reversi.getBoard().getBlackDiscNum()));
        whiteDiscNumLabel.setText(Integer.toString(reversi.getBoard().getWhiteDiscNum()));
        
        String turnString = String.format("%d手を以て、", reversi.getTurnCount());
        switch (result) {
        case Black: {
            resultLabel.setText(turnString + "先手・黒の勝ちです！");
            break;
        }
        case White: {
            resultLabel.setText(turnString + "後手・白の勝ちです！");
            break;
        }
        case Drow: {
            resultLabel.setText(turnString + "引き分けです！");
            break;
        }
        case None:
        default:
            throw new IllegalArgumentException("Unexpected value: " + result);
        }
        
        // 棋譜を表示する
        VBox recordVBox = new VBox();
        while(reversi.getRecordList().isEmpty() == false) {
            
            Label recordLabel = new Label(reversi.getRecordList().poll());
            recordLabel.setId("record");
            recordVBox.getChildren().addAll(recordLabel);
        }
        recordPane.setContent(recordVBox);
    }
}
