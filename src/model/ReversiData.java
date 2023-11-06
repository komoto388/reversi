package model;

import reversi.Player;
import reversi.Reversi;

/**
 * ゲーム処理を実行するために必要なデータを定義するクラス
 * @author komoto
 */
public class ReversiData {

    /** リバーシのインスタンス */
    private final Reversi reversi;

    /** 先手・黒のプレイヤーのインスタンス */
    private final Player playerBlack;

    /** 後手・白のプレイヤーのインスタンス */
    private final Player playerWhite;

    /** デバッグ情報を表示するか */
    private final Boolean isDebug;

    /**
     * 値を設定する
     * @param reversi リバーシのインスタンス
     * @param playerBlack 先手・黒のプレイヤーのインスタンス
     * @param playerWhite 後手・白のプレイヤーのインスタンス
     * @param isDebug デバッグ情報を表示するかを表す
     */
    public ReversiData(Reversi reversi, Player playerBlack, Player playerWhite, Boolean isDebug) {
        this.reversi = reversi;
        this.playerBlack = playerBlack;
        this.playerWhite = playerWhite;
        this.isDebug = isDebug;
    }

    /**
     * リバーシのインスタンスを取得する
     * @return リバーシのインスタンス
     */
    public Reversi getReversi() {
        return reversi;
    }

    /**
     * 先手・黒のプレイヤーのインスタンスを取得する
     * @return 生成した先手・黒のインスタンス
     */
    public Player getPlayerBlack() {
        return playerBlack;
    }

    /**
     * 後手・白のプレイヤーのインスタンスを取得する
     * @return 生成した後手・白のインスタンス
     */
    public Player getPlayerWhite() {
        return playerWhite;
    }

    /**
     * デバッグ情報の表示・非表示を表す値を取得する
     * @return デバッグ情報を表示するかを表す
     */
    public Boolean getIsDebug() {
        return isDebug;
    }
}
