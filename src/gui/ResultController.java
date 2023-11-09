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
import model.ResultModel;
import reversi.Board;
import reversi.Disc;
import reversi.Reversi;

/**
 * 対戦結果画面の処理を行うクラス
 * @author komoto
 */
public class ResultController {

    ResultModel resultModel;

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
                throw new IllegalArgumentException("引数 \"resultData\" の値が NULL です");
            }
        } catch (IllegalArgumentException e) {
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println("引数が想定されていない値のため、プログラムを異常終了します: 終了コード = " + exitCode);
            System.exit(exitCode);
        }

        this.resultModel = new ResultModel(resultData);
        this.sceneSwitch = sceneSwitch;

        Reversi reveris = resultModel.getReversi();
        Board board = resultModel.getBoard();

        // 対戦結果を表示する       
        blackHeaderLabel.setText(String.format("先手・黒 ( %s )", resultModel.getPlayerAlgorithmName(true)));
        whiteHeaderLabel.setText(String.format("後手・白 ( %s )", resultModel.getPlayerAlgorithmName(false)));
        blackNameLabel.setText(resultModel.getPlayerName(true));
        whiteNameLabel.setText(resultModel.getPlayerName(false));
        blackDiscNumLabel.setText(String.format("%d 個", board.getDiscNum(Disc.BLACK)));
        whiteDiscNumLabel.setText(String.format("%d 個", board.getDiscNum(Disc.WHITE)));
        resultLabel.setText(resultModel.getResultString());

        // 各タブ内の画面を生成する
        generateDetailResultPane(detailResultTab, reveris);
        generateRecordPane(recordTab);
        generateGraphResultPane(graphTab);
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
     */
    private void generateRecordPane(Tab tabPane) {
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

        RecordController controller = (RecordController) fxmlloader.getController();
        controller.setRecord(resultModel.getRecord());

        tabPane.setContent(pane);
    }

    /**
     * グラフ画面を生成する
     * @param tabPane 生成したペインの描画先となるペイン
     */
    private void generateGraphResultPane(Tab tabPane) {
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

        GraphResultController controller = (GraphResultController) fxmlloader.getController();
        controller.setGraph(resultModel.getRecord());

        tabPane.setContent(pane);
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
