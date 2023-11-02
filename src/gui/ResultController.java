package gui;

import common.Global;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import reversi.Record;
import reversi.RecordList;
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

    /** 詳細情報として最終盤面を表示するタブ */
    @FXML
    private Tab detailResultTab;

    /** 棋譜を表示するタブ */
    @FXML
    private Tab recordTab;
    
    /** 推移グラフを表示するタブ */
    @FXML
    private Tab graphTab;

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

        // 各タブ内の画面を生成する
        generateDetailResultPane(detailResultTab, reversi);
        RecordController recordController = generateRecordPane(recordTab);
        GraphResultController graphResultController = generateGraphResultPane(graphTab);
        
        // 棋譜リストを元に、棋譜・グラフを描画する
        RecordList recordList = reversi.getRecordList();
        for (int i = 1; recordList.isEmpty() == false; i++) {
            Record record = recordList.poll();
            recordController.addRecord(i, record);
            graphResultController.addData(i, record);
        }
    }

    /**
     * 詳細結果画面（最終盤面）を生成する
     * @param tabPane 生成したペインの描画先となるペイン
     * @param reversi リバーシの処理を行うインスタンス
     */
    private void generateDetailResultPane(Tab tabPane, Reversi reversi) {
        FXMLLoader fxmlloader = null;
        VBox pane = null;

        try {
            fxmlloader = new FXMLLoader(getClass().getResource("../fxml/DetailResult.fxml"));
            pane = (VBox) fxmlloader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pane.setPrefSize(Global.RESULT_TAB_PANE_WIDTH, Global.RESULT_TAB_PANE_HEIGHT);

        DetailResultController controller = (DetailResultController) fxmlloader.getController();
        controller.init(reversi.getBoard());
        
        tabPane.setContent(pane);
    }

    /**
     * 棋譜画面を生成する
     * @param tabPane 生成したペインの描画先となるペイン
     * @param 棋譜画面のコントローラー
     */
    private RecordController generateRecordPane(Tab tabPane) {
        FXMLLoader fxmlloader = null;
        ScrollPane pane = null;

        try {
            fxmlloader = new FXMLLoader(getClass().getResource("../fxml/Record.fxml"));
            pane = (ScrollPane) fxmlloader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pane.setPrefSize(Global.RESULT_TAB_PANE_WIDTH, Global.RESULT_TAB_PANE_HEIGHT);

        tabPane.setContent(pane);
        
        return (RecordController) fxmlloader.getController();
    }

    /**
     * グラフ画面を生成する
     * @param tabPane 生成したペインの描画先となるペイン
     * @param グラフ画面のコントローラー
     */
    private GraphResultController generateGraphResultPane(Tab tabPane) {
        FXMLLoader fxmlloader = null;
        AnchorPane pane = null;

        try {
            fxmlloader = new FXMLLoader(getClass().getResource("../fxml/GraphResult.fxml"));
            pane = (AnchorPane) fxmlloader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pane.setPrefSize(Global.RESULT_TAB_PANE_WIDTH, Global.RESULT_TAB_PANE_HEIGHT);

        tabPane.setContent(pane);
        
        return (GraphResultController) fxmlloader.getController();
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
