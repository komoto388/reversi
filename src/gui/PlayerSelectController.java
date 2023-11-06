package gui;

import java.net.URL;
import java.util.ResourceBundle;

import algorithm.AlgorithmType;
import common.Global;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import model.ReversiData;
import model.PlayerSelectModel;

/**
 * プレイヤーの使用アルゴリズムを選択するクラス
 * @author komoto
 */
public class PlayerSelectController implements Initializable {

    /** プレイヤー選択・設定のデータ処理を行うインスタンス（モデル） */
    private PlayerSelectModel playerSelectModel;

    /** シーン切替処理を行うインスタンス */
    private SceneSwitch sceneSwitch;

    /** 先手・黒側のラベル */
    @FXML
    private Label blackLabel;

    /** 後手・白側のラベル */
    @FXML
    private Label whiteLabel;

    /** 先手・黒の名前を設定・表示するテキストフィールド */
    @FXML
    private TextField blackNameFeild;

    /** 後手・白の名前を設定・表示するテキストフィールド */
    @FXML
    private TextField whiteNameFeild;

    /** 先手・黒側の選択画面を表示するペイン */
    @FXML
    private VBox blackPane;

    /** 後手・白側の選択画面を表示するペイン */
    @FXML
    private VBox whitePane;

    /** デバッグ表示の有効・無効を切り替えるチェックボタン */
    @FXML
    private CheckBox debugModeChekBox;

    /** ゲーム開始のボタン */
    @FXML
    private Button startButton;

    /** ゲーム終了のボタン */
    @FXML
    private Button exitButton;

    /**
     * データ処理モデルの初期化、初期画面の描画を行う
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playerSelectModel = new PlayerSelectModel();

        blackNameFeild.setText(playerSelectModel.getName(true));
        whiteNameFeild.setText(playerSelectModel.getName(false));
        debugModeChekBox.setSelected(playerSelectModel.getIsDebug());

        setGridPane(blackPane, true);
        setGridPane(whitePane, false);
    }

    /**
     * パラメータを読み込み、初期化する
     * @param sceneSwitch シーン切替処理を行うインスタンス
     */
    public void init(SceneSwitch sceneSwitch) {
        // 引数の正常性確認
        try {
            if (sceneSwitch == null) {
                throw new IllegalArgumentException("引数 \"sceneSwitch\" の値が NULL です");
            }
        } catch (IllegalArgumentException e) {
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println("引数が想定されていない値のため、プログラムを異常終了します: 終了コード = " + exitCode);
            System.exit(exitCode);
        }

        // 初期化（シーン選択インスタンスの設定）
        this.sceneSwitch = sceneSwitch;
    }

    /**
     * 指定されたプレイヤー側のアルゴリズムを選択するラジオボタンを生成する
     * @param vbox 生成したラジオボタンの描画先のペイン
     * @param isPlayerBlack プレイヤーの石の色が黒かどうか
     */
    private void setGridPane(VBox vbox, Boolean isPlayerBlack) {
        AlgorithmType[] types = AlgorithmType.values();
        ToggleGroup group = new ToggleGroup();

        for (int i = 0; i < types.length; i++) {
            RadioButton radioButton = new RadioButton(String.format("%s", types[i].getName()));
            radioButton.setUserData(types[i]);
            radioButton.setToggleGroup(group);
            radioButton.setPrefWidth(Global.RADIO_BUTTON_WIDTH);
            radioButton.setPrefHeight(Global.RADIO_BUTTON_HEIGHT);

            // デフォルト値のラジオボタンにチェックを入れる
            if (types[i] == playerSelectModel.getAlgorithmType(isPlayerBlack)) {
                radioButton.setSelected(true);
            }

            vbox.getChildren().add(radioButton);
        }
        group.selectedToggleProperty().addListener(new ToggleButtonEvent(isPlayerBlack));
    }

    /**
     * ラジオボタンが選択された時のアクションを定義・処理する内部クラス
     */
    private class ToggleButtonEvent implements ChangeListener<Toggle> {
        /** 制御しているのは先手・黒のラジオボタンか */
        private Boolean isPlayerBlack;

        /**
         * ラジオボタンの情報の反映先を設定する
         * @param isPlayerBlack ラジオボタンの情報の反映先が先手・黒のプレイヤーか
         */
        public ToggleButtonEvent(Boolean isPlayerBlack) {
            this.isPlayerBlack = isPlayerBlack;
        }

        /**
         * ラジオボタンが選択された時にプレイヤーが使用するアルゴリズムを設定する
         */
        @Override
        public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
            // 選択中のボタンをクリックした場合
            if (newValue == null) {
                return;
            }

            ToggleButton selectedButton = (ToggleButton) newValue;
            playerSelectModel.setPlayerAlgorithm(isPlayerBlack, (AlgorithmType) selectedButton.getUserData());
        }
    }

    /**
     * 選択したアルゴリズムを元に、ゲーム画面を描画する
     * @param event イベントのインスタンス
     */
    @FXML
    void onStartButtonAction(ActionEvent event) {
        playerSelectModel.setPlayerName(true, blackNameFeild.getText());
        playerSelectModel.setPlayerName(false, whiteNameFeild.getText());
        playerSelectModel.setIsDebug(debugModeChekBox.isSelected());

        ReversiData data = playerSelectModel.generateData();
        sceneSwitch.generateSceneReversi(data);
    }

    /**
     * 画面を終了する
     * @param event イベントのインスタンス
     */
    @FXML
    void onExitButtonAction(ActionEvent event) {
        sceneSwitch.close();
    }
}
