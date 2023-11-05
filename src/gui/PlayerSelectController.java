package gui;

import algorithm.AlgorithmType;
import common.Global;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import reversi.Player;
import reversi.Reversi;

/**
 * プレイヤーの使用アルゴリズムを選択するクラス
 * @author komoto
 */
public class PlayerSelectController {

    /** シーン切替処理を行うインスタンス */
    private SceneSwitch sceneSwitch;

    /** 先手・黒側の使用アルゴリズム */
    private AlgorithmType algorithmTypeBlack;

    /** 後手・白側の使用アルゴリズム */
    private AlgorithmType algorithmTypeWhite;

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
     * 先手・後手両方のアルゴリズムを選択するラジオボタンを生成する
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

        this.sceneSwitch = sceneSwitch;
        blackNameFeild.setText(Global.DEFAULT_PLAYER_NAME_BLACK);
        whiteNameFeild.setText(Global.DEFAULT_PLAYER_NAME_WHITE);

        setGridPane(blackPane, true);
        setGridPane(whitePane, false);

        AlgorithmType[] algorithmTypes = AlgorithmType.values();
        algorithmTypeBlack = algorithmTypes[Global.DEFAULT_ALGORITHM];
        algorithmTypeWhite = algorithmTypes[Global.DEFAULT_ALGORITHM];

    }

    /**
     * 指定されたプレイヤー側のアルゴリズムを選択するラジオボタンを生成する
     * @param vbox 生成したラジオボタンの描画先のペイン
     * @param isBlack プレイヤーの石の色が黒かどうか
     */
    private void setGridPane(VBox vbox, Boolean isBlack) {
        AlgorithmType[] types = AlgorithmType.values();
        ToggleGroup group = new ToggleGroup();

        for (int i = 0; i < types.length; i++) {
            RadioButton radioButton = new RadioButton(String.format("%s", types[i].getName()));
            radioButton.setUserData(types[i]);
            radioButton.setToggleGroup(group);
            radioButton.setPrefWidth(Global.RADIO_BUTTON_WIDTH);
            radioButton.setPrefHeight(Global.RADIO_BUTTON_HEIGHT);

            if (i == Global.DEFAULT_ALGORITHM) {
                radioButton.setSelected(true);
            }
            vbox.getChildren().add(radioButton);
        }
        group.selectedToggleProperty().addListener(new ToggleButtonEvent(isBlack));
    }

    /**
     * ラジオボタンが選択された時のアクションを定義・処理する内部クラス
     */
    private class ToggleButtonEvent implements ChangeListener<Toggle> {
        /** 制御しているのは先手・黒のラジオボタンか */
        private Boolean isBlack;

        /**
         * ラジオボタンの情報の反映先を設定する
         * @param isBlack ラジオボタンの情報の反映先が先手・黒のプレイヤーか
         */
        public ToggleButtonEvent(Boolean isBlack) {
            this.isBlack = isBlack;
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
            if (isBlack) {
                algorithmTypeBlack = (AlgorithmType) selectedButton.getUserData();
            } else {
                algorithmTypeWhite = (AlgorithmType) selectedButton.getUserData();
            }
        }
    }

    /**
     * 選択したアルゴリズムを元に、ゲーム画面を描画する
     * @param event イベントのインスタンス
     */
    @FXML
    void onStartButtonAction(ActionEvent event) {
        String nameBlack, nameWhite;

        if (blackNameFeild.getText() == null) {
            nameBlack = Global.DEFAULT_PLAYER_NAME_BLACK;
        } else {
            nameBlack = blackNameFeild.getText();
        }

        if (whiteNameFeild.getText() == null) {
            nameWhite = Global.DEFAULT_PLAYER_NAME_WHITE;
        } else {
            nameWhite = whiteNameFeild.getText();
        }

        // プレイヤーとリバーシのインスタンスを作成する
        Player playerBlack = new Player(nameBlack, true, algorithmTypeBlack);
        Player playerWhite = new Player(nameWhite, false, algorithmTypeWhite);
        Reversi reversi = new Reversi(playerBlack, playerWhite);
        
        sceneSwitch.generateSceneReversi(reversi, debugModeChekBox.isSelected());
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
