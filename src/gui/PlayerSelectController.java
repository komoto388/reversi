package gui;

import algorithm.AlgorithmType;
import common.Global;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import reversi.Reversi;

/**
 * プレイヤーの使用アルゴリズムを選択するクラス
 * @author komoto
 */
public class PlayerSelectController {
    
    /** フレーム情報 */
    private Stage primaryStage;

    /** 先手・黒側の使用アルゴリズム */
    AlgorithmType algorithmTypeBlack;

    /** 後手・白側の使用アルゴリズム */
    AlgorithmType algorithmTypeWhite;

    /** 先手・黒側のラベル */
    @FXML
    private Label blackLabel;

    /** 後手・白側のラベル */
    @FXML
    private Label whiteLabel;

    /** 先手・黒側の選択画面を表示するペイン */
    @FXML
    private VBox blackPane;

    /** 後手・白側の選択画面を表示するペイン */
    @FXML
    private VBox whitePane;

    /** ゲーム開始のボタン */
    @FXML
    private Button startButton;

    /**
     * 先手・後手両方のアルゴリズムを選択するラジオボタンを生成する
     * @param primaryStage フレーム情報
     */
    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;

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
            RadioButton radioButton = new RadioButton(String.format("%s", types[i]));
            radioButton.setId("select-radio-button");
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
        Boolean isBlack;

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
                algorithmTypeBlack = AlgorithmType.valueOf(selectedButton.getText());
            } else {
                algorithmTypeWhite = AlgorithmType.valueOf(selectedButton.getText());
            }
        }
    }

    /**
     * 選択したアルゴリズムを元に、ゲーム画面を描画する
     * @param event イベントのインスタンス
     */
    @FXML
    void onStartButtonAction(ActionEvent event) {
        FXMLLoader fxmlloader = null;
        BorderPane reversiPane = null;

        // 現在描画されているペインを取得する
        Pane previousPane = (Pane) primaryStage.getScene().getRoot();

        // リバーシのゲーム画面の呼び出し
        Reversi reversi = new Reversi(algorithmTypeBlack, algorithmTypeWhite);
        
        try {
            fxmlloader = new FXMLLoader(getClass().getResource("../fxml/Reversi.fxml"));
            reversiPane = (BorderPane) fxmlloader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ReversiController controller = (ReversiController) fxmlloader.getController();
        controller.init(primaryStage, reversi);

        // 画面内に複数ペインを描画するために、StackPaneでルートペインを作成する
        AnchorPane rootPane = new AnchorPane();
        rootPane.setPrefSize(Global.ROOT_PANE_WIDTH, Global.ROOT_PANE_HEIGHT);
        rootPane.getChildren().add(reversiPane);
        AnchorPane.setTopAnchor(reversiPane, 0d);
        AnchorPane.setBottomAnchor(reversiPane, 0d);
        AnchorPane.setLeftAnchor(reversiPane, 0d);
        AnchorPane.setRightAnchor(reversiPane, 0d);

        // 描画する画面をフレームに設定する
        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add(getClass().getResource("../css/default.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
