package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import reversi.Record;
import reversi.ResultType;
import reversi.Reversi;

/**
 * 対戦結果画面の処理を行うクラス
 * @author komoto
 */
public class ResultController {
    /** フレーム情報 */
    private Stage stage;

    @FXML
    private VBox resultRootPane;

    /** 対戦結果を表すラベル */
    @FXML
    private Label resultLabel;

    /** 黒石の個数を表すラベル */
    @FXML
    private Label blackDiscNumLabel;

    /** 白石の個数を表すラベル */
    @FXML
    private Label whiteDiscNumLabel;

    /** 棋譜を表示する表ペイン */
    @FXML
    private GridPane recordPane;

    /** 棋譜を表示する表のヘッダーラベル */
    @FXML
    private Label recordHeaderLabel;

    /** 結果画面を閉じるボタン */
    @FXML
    private Button closeButton;

    /**
     * 対戦結果内容を画面に設定する
     * @param stage フレーム情報
     * @param reversi リバーシの処理を行うインスタンス
     * @param result 勝敗の結果
     */
    public void init(Stage stage, Reversi reversi, ResultType result) {
        this.stage = stage;

        // 対戦結果を表示する
        blackDiscNumLabel.setText(String.format("%d 個", reversi.getBoard().getBlackDiscNum()));
        whiteDiscNumLabel.setText(String.format("%d 個", reversi.getBoard().getWhiteDiscNum()));

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
        for (int i = 1; reversi.getRecordList().isEmpty() == false; i++) {
            Record record = reversi.getRecordList().poll();

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

    /**
     * 閉じるボタンが押された時に、結果ペインのみ閉じる
     * @param event イベントのインスタンス
     */
    @FXML
    void onCloseButtonAction(ActionEvent event) {
        Pane rootPane = (Pane) stage.getScene().getRoot();
        rootPane.getChildren().remove(resultRootPane);
    }
}
