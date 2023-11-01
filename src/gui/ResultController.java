package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import reversi.ResultType;
import reversi.Reversi;

/**
 * 対戦結果画面の処理を行うクラス
 * @author komoto
 */
public class ResultController {
    /** フレーム情報 */
    private Stage stage;

    /** 結果を表示するペイン（自分自身） */
    @FXML
    private BorderPane resultRootPane;

    /** 対戦結果を表すラベル */
    @FXML
    private Label resultLabel;

    /** 黒石の個数を表すラベル */
    @FXML
    private Label blackDiscNumLabel;

    /** 白石の個数を表すラベル */
    @FXML
    private Label whiteDiscNumLabel;

    /** 棋譜を表示するタブ */
    @FXML
    private Tab recordTab;

    /** ゲームを終了するボタン */
    @FXML
    private Button exitButton;

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

        String turnString = String.format("%d手をもって、", reversi.getTurnCount());
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

        recordTab.setContent(generateRecordPane(reversi));
    }

    /**
     * 結果画面を生成する
     * @param result 勝敗結果を表す値
     */
    private ScrollPane generateRecordPane(Reversi reversi) {
        FXMLLoader fxmlloader = null;
        ScrollPane recordPane = null;

        try {
            fxmlloader = new FXMLLoader(getClass().getResource("../fxml/Record.fxml"));
            recordPane = (ScrollPane) fxmlloader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RecordController controller = (RecordController) fxmlloader.getController();
        controller.init(reversi);

        return recordPane;
    }

    /**
     * 終了ボタンが押された時のウィンドウを閉じる
     * @param event イベントのインスタンス
     */
    @FXML
    void onExitButtonAction(ActionEvent event) {
        stage.close();
    }
}
