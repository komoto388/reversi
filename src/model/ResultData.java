package model;

import reversi.Player;
import reversi.ResultType;
import reversi.Reversi;

/**
 * 結果画面表示を実行するために必要なデータを定義するクラス
 * @author komoto
 */
public class ResultData {

    /** リバーシのインスタンス */
    private final Reversi reversi;

    /** 先手・黒のプレイヤーのインスタンス */
    private final Player playerBlack;

    /** 後手・白のプレイヤーのインスタンス */
    private final Player playerWhite;

    /** 対戦結果を表すインスタンス */
    private final ResultType result;

    /**
     * 値を設定する
     * @param reversi リバーシのインスタンス
     * @param playerBlack 先手・黒のプレイヤーのインスタンス
     * @param playerWhite 後手・白のプレイヤーのインスタンス
     * @param result 対戦結果を表すインスタンス
     */
    public ResultData(Reversi reversi, Player playerBlack, Player playerWhite, ResultType result) {
        this.reversi = reversi;
        this.playerBlack = playerBlack;
        this.playerWhite = playerWhite;
        this.result = result;
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
     * 対戦結果を表すインスタンスを取得する
     * @return 対戦結果を表すインスタンス
     */
    public ResultType getResult() {
        return result;
    }
}
