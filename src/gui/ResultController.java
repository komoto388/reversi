package gui;

import common.Global;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import model.ResultData;
import reversi.Player;
import reversi.Record;
import reversi.RecordRow;
import reversi.ResultType;
import reversi.Reversi;

/**
 * 対戦結果画面の処理を行うクラス
 * @author komoto
 */
public class ResultController {

    /** シーン切替処理を行うインスタンス */
    private SceneSwitch sceneSwitch;

    /** vs.と表示されたラベル */
    @FXML
    private Label vsLabel;

    /** 先手・黒側の情報を表すヘッダーのラベル */
    @FXML
    private Label blackHeaderLabel;

    /** 後手・白側の情報を表すヘッダーのラベル */
    @FXML
    private Label whiteHeaderLabel;

    /** 先手・黒側の名前を表すヘッダーのラベル */
    @FXML
    private Label blackNameLabel;

    /** 後手・白側の名前を表すヘッダーのラベル */
    @FXML
    private Label whiteNameLabel;

    /** 黒石の個数を表すラベル */
    @FXML
    private Label blackDiscNumLabel;

    /** 白石の個数を表すラベル */
    @FXML
    private Label whiteDiscNumLabel;

    /** 対戦結果を表すラベル */
    @FXML
    private Label resultLabel;

    /** 詳細情報として最終盤面を表示するタブ */
    @FXML
    private Tab detailResultTab;

    /** 棋譜を表示するタブ */
    @FXML
    private Tab recordTab;

    /** 推移グラフを表示するタブ */
    @FXML
    private Tab graphTab;

    /** 最初の画面に戻るボタン */
    @FXML
    private Button returnButton;

    /** ゲームを終了するボタン */
    @FXML
    private Button exitButton;

    /**
     * 対戦結果内容を画面に設定する
     * @param sceneSwitch シーン切替処理を行うインスタンス
     * @param resultData 結果出力処理を実行するために必要なデータの集合
     */
    public void init(SceneSwitch sceneSwitch, ResultData resultData) {
        // 引数の正常性確認
        try {
            if (sceneSwitch == null) {
                throw new IllegalArgumentException("引数 \"sceneSwitch\" の値が NULL です");
            }
            if (resultData == null) {
                throw new IllegalArgumentException("引数 \"reversiData\" の値が NULL です");
            }
        } catch (IllegalArgumentException e) {
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println("引数が想定されていない値のため、プログラムを異常終了します: 終了コード = " + exitCode);
            System.exit(exitCode);
        }

        this.sceneSwitch = sceneSwitch;

        Reversi reversi = resultData.getReversi();
        Player blackPlayer = resultData.getPlayerBlack();
        Player whitePlayer = resultData.getPlayerWhite();
        ResultType result = resultData.getResult();

        // 対戦結果を表示する
        blackHeaderLabel.setText(String.format("先手・黒 ( %s )", blackPlayer.getAlgorithmType().getName()));
        whiteHeaderLabel.setText(String.format("後手・白 ( %s )", whitePlayer.getAlgorithmType().getName()));
        blackNameLabel.setText(blackPlayer.getName());
        whiteNameLabel.setText(whitePlayer.getName());
        blackDiscNumLabel.setText(String.format("%d 個", reversi.getBoard().getBlackDiscNum()));
        whiteDiscNumLabel.setText(String.format("%d 個", reversi.getBoard().getWhiteDiscNum()));

        String turnString = String.format("%d手をもって、", reversi.getTurnCount());
        switch (result) {
        case Black: {
            resultLabel.setText(turnString + "先手・黒「" + blackPlayer.getName() + "」の勝ちです！");
            break;
        }
        case White: {
            resultLabel.setText(turnString + "後手・白「" + whitePlayer.getName() + "」の勝ちです！");
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
        Record record = reversi.getRecord();
        for (int i = 1; record.isEmpty() == false; i++) {
            RecordRow recordRow = record.poll();
            recordController.addRecordRow(i, recordRow);
            graphResultController.addData(i, recordRow);
        }
        recordController.setComment("終了した理由: " + record.getComment());
    }

    /**
     * 詳細結果画面（最終盤面）を生成する
     * @param tabPane 生成したペインの描画先となるペイン
     * @param reversi リバーシの処理を行うインスタンス
     */
    private void generateDetailResultPane(Tab tabPane, Reversi reversi) {
        FXMLLoader fxmlloader = null;
        VBox pane = null;
        String fxmlFile = "../fxml/DetailResult.fxml";

        try {
            fxmlloader = new FXMLLoader(getClass().getResource(fxmlFile));
            pane = (VBox) fxmlloader.load();
        } catch (Exception e) {
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println(fxmlFile + "の読み込みで例外が発生したため、プログラムを異常終了します: 終了コード = " + exitCode);
            System.exit(exitCode);
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
        String fxmlFile = "../fxml/Record.fxml";

        try {
            fxmlloader = new FXMLLoader(getClass().getResource(fxmlFile));
            pane = (ScrollPane) fxmlloader.load();
        } catch (Exception e) {
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println(fxmlFile + "の読み込みで例外が発生したため、プログラムを異常終了します: 終了コード = " + exitCode);
            System.exit(exitCode);
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
        VBox pane = null;
        String fxmlFile = "../fxml/GraphResult.fxml";

        try {
            fxmlloader = new FXMLLoader(getClass().getResource(fxmlFile));
            pane = (VBox) fxmlloader.load();
        } catch (Exception e) {
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println(fxmlFile + "の読み込みで例外が発生したため、プログラムを異常終了します: 終了コード = " + exitCode);
            System.exit(exitCode);
        }
        pane.setPrefSize(Global.RESULT_TAB_PANE_WIDTH, Global.RESULT_TAB_PANE_HEIGHT);

        tabPane.setContent(pane);

        return (GraphResultController) fxmlloader.getController();
    }

    /**
     * 最初に戻るボタンが押された時に、プレイヤーの選択画面を表示させる
     * @param event イベントのインスタンス
     */
    @FXML
    void onReturnButtonAction(ActionEvent event) {
        sceneSwitch.generateScenePlayerSelect();
    }

    /**
     * 終了ボタンが押された時のウィンドウを閉じる
     * @param event イベントのインスタンス
     */
    @FXML
    void onExitButtonAction(ActionEvent event) {
        sceneSwitch.close();
    }
}
