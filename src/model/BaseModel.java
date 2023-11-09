package model;

import reversi.Board;
import reversi.Dimension;
import reversi.Player;
import reversi.Reversi;

/**
 * 処理モデルの抽象クラス
 * Reversi, Player クラスのインスタンスを使用しないモデルは、この抽象クラスを使用しなくても構いません。
 */
public abstract class BaseModel {

    /** リバーシを制御するインスタンス */
    protected Reversi reversi;

    /** リバーシ盤の状態を表すインスタンス */
    protected Board board;

    /** 先手・黒のプレイヤーのインスタンス */
    protected Player playerBlack;

    /** 後手・白のプレイヤーのインスタンス */
    protected Player playerWhite;

    /**
     * 初期化する
     * @param reversi リバーシのゲーム情報
     * @param playerBlack 先手・黒のプレイヤー情報
     * @param playerWhite 後手・白のプレイヤー情報
     */
    public BaseModel(Reversi reversi, Player playerBlack, Player playerWhite) {
        this.reversi = reversi;
        this.board = reversi.getBoard();
        this.playerBlack = playerBlack;
        this.playerWhite = playerWhite;
    }

    /**
     * リバーシのゲーム情報を取得する
     * @return リバーシのゲーム情報
     */
    public Reversi getReversi() {
        return reversi;
    }

    /**
     * リバーシ盤の情報を取得する
     * @return リバーシ盤の情報
     */
    public Board getBoard() {
        return reversi.getBoard();
    }

    /**
     * リバーシ盤のサイズを取得する
     * @return リバーシ盤のサイズ
     */
    public Dimension getBoardSize() {
        return reversi.getBoard().getSize();
    }

    /**
     * プレイヤー情報を取得する
     * @param isPlayerBlack 対象のプレイヤー (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 対象プレイヤーの情報
     */
    public Player getPlayer(Boolean isPlayerBlack) {
        if (isPlayerBlack) {
            return playerBlack;
        } else {
            return playerWhite;
        }
    }

    /**
     * プレイヤーの名前を取得する
     * @param isPlayerBlack 対象のプレイヤー (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 対象プレイヤーの名前の文字列
     */
    public String getPlayerName(Boolean isPlayerBlack) {
        if (isPlayerBlack) {
            return playerBlack.getName();
        } else {
            return playerWhite.getName();
        }
    }

    /**
     * プレイヤーが使用するアルゴリズムの名前を取得する
     * @param isPlayerBlack 対象のプレイヤー (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 対象プレイヤーが使用するアルゴリズムの名前の文字列
     */
    public String getPlayerAlgorithmName(Boolean isPlayerBlack) {
        if (isPlayerBlack) {
            return playerBlack.getAlgorithmType().getName();
        } else {
            return playerWhite.getAlgorithmType().getName();
        }
    }
}
