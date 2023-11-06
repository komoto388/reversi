package model;

import algorithm.AlgorithmType;
import common.Global;
import reversi.Player;
import reversi.Reversi;

/**
 * プレイヤー選択・設定のデータ処理を行うクラス
 */
public class PlayerSelectModel {

    /** 先手・黒のプレイヤー名 */
    private String nameBlack;

    /** 先手・黒のプレイヤー名 */
    private String nameWhite;

    /** 先手・黒側の使用アルゴリズム */
    private AlgorithmType algorithmTypeBlack;

    /** 後手・白側の使用アルゴリズム */
    private AlgorithmType algorithmTypeWhite;

    /** デバッグ情報を表示するか */
    private Boolean isDebug;

    /**
     * プレイヤー選択処理の初期化を行う
     */
    public PlayerSelectModel() {
        // デフォルト値で初期化する
        nameBlack = Global.DEFAULT_PLAYER_NAME_BLACK;
        nameWhite = Global.DEFAULT_PLAYER_NAME_WHITE;

        AlgorithmType[] algorithmTypes = AlgorithmType.values();
        algorithmTypeBlack = algorithmTypes[Global.DEFAULT_ALGORITHM];
        algorithmTypeWhite = algorithmTypes[Global.DEFAULT_ALGORITHM];

        isDebug = true;
    }

    /**
     * プレイヤーの名前を取得する
     * @param isPlayerBlack 対象のプレイヤー (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return プレイヤーの名前の文字列
     */
    public String getName(Boolean isPlayerBlack) {
        if (isPlayerBlack) {
            return nameBlack;
        } else {
            return nameWhite;
        }
    }

    /**
     * プレイヤーのアルゴリズム種別を取得する
     * @param isPlayerBlack 対象のプレイヤー (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return プレイヤーのアルゴリズム種別
     */
    public AlgorithmType getAlgorithmType(Boolean isPlayerBlack) {
        if (isPlayerBlack) {
            return algorithmTypeBlack;
        } else {
            return algorithmTypeWhite;
        }
    }

    /**
     * デバッグ情報の表示・非表示の値を取得する
     * @return デバッグ情報の表示・非表示の値
     */
    public Boolean getIsDebug() {
        return isDebug;
    }

    /**
     * プレイヤーの名前を設定する
     * 引数 {@code name} が {@code NULL} の場合は設定変更せず、前の値かデフォルト値のままにする。
     * @param isPlayerBlack 対象のプレイヤー (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @param name プレイヤーの名前
     * @return 設定できた場合は真 {@code true}, 設定できなかった場合は偽 {@code false} を返す。
     */
    public Boolean setPlayerName(Boolean isPlayerBlack, String name) {
        if (name == null) {
            System.err.println("プレイヤー名が空のため、値は変更されませんでした");
            System.err.printf("現在値: (黒) %s, (白) %s\n", nameBlack, nameWhite);
            return false;
        }

        if (isPlayerBlack) {
            nameBlack = name;
        } else {
            nameWhite = name;
        }

        return true;
    }

    /**
     * プレイヤーの情報を設定する。
     * 引数 {@code algorithmType} が {@code NULL} の場合は設定変更せず、前の値かデフォルト値のままにする。
     * @param isPlayerBlack 対象のプレイヤー (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @param algorithmType 設定するアルゴリズム種別
     * @return 設定できた場合は真 {@code true}, 設定できなかった場合は偽 {@code false} を返す。
     */
    public Boolean setPlayerAlgorithm(Boolean isPlayerBlack, AlgorithmType algorithmType) {
        if (algorithmType == null) {
            System.err.println("アルゴリズム種別が空のため、値は変更されませんでした");
            System.err.printf("現在値: (黒) %s, (白) %s\n", algorithmTypeBlack.getName(), algorithmTypeWhite.getName());
            return false;
        }

        if (isPlayerBlack) {
            algorithmTypeBlack = algorithmType;
        } else {
            algorithmTypeWhite = algorithmType;
        }

        return true;
    }

    /**
     * デバッグ情報の表示・非表示の値を設定する。
     * @param isDebug デバッグ情報の表示・非表示の値
     */
    public void setIsDebug(Boolean isDebug) {
        this.isDebug = isDebug;
    }

    /**
     * 設定されたプレイヤー名とアルゴリズム種別から、リバーシとプレイヤーのインスタンスを生成する
     */
    public ReversiData generateData() {
        Player playerBlack = new Player(nameBlack, true, algorithmTypeBlack);
        Player playerWhite = new Player(nameWhite, false, algorithmTypeWhite);
        Reversi reversi = new Reversi(playerBlack, playerWhite);

        ReversiData data = new ReversiData(reversi, playerBlack, playerWhite, isDebug);

        return data;
    }
}
