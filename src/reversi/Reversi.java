package reversi;

import algorithm.AlgorithmType;

/**
 * リバーシのゲームを定義・処理するクラス
 * @author komoto
 */
public class Reversi {
    /** リバーシ盤状態を表す */
    private Board board;

    /** 現在のプレイヤーが黒かどうかを表す */
    private Boolean playerIsBlack;

    /** 経過したターン数 */
    private int turnCount;

    /** 黒側のプレイヤー */
    Player playerBlack = null;

    /** 白側のプレイヤー */
    Player playerWhite = null;

    /**
     * リバーシ盤の初期化を行う
     */
    public Reversi() {
        board = new Board();
        playerIsBlack = true;
        turnCount = 1;
    }

    /**
     * プレイヤーを設定する
     * @param isBlack 設定対象のプレイヤーが黒の場合は真 {@code true}、白の場合は偽 {@code false} を指定する。
     * @oaram type プレイヤーが使用するアルゴリズムのタイプ
     */
    public void setPlayer(Boolean isBlack, AlgorithmType type) {
        if (isBlack) {
            this.playerBlack = new Player(true, type);
        } else {
            this.playerWhite = new Player(false, type);
        }
    }

    /**
     * リバーシ盤の状態を返す
     * @return リバーシ盤の状態
     */
    public Board getBoard() {
        return board;
    }

    /**
     * 現在の経過ターン数を返す
     * @return 現在の経過ターン数
     */
    public int getTurnCount() {
        return turnCount;
    }

    /**
     * 現在のプレイヤーの石の色を返す
     * @return 現在の経過ターン数
     */
    public Boolean getPlayerIsBlack() {
        return playerIsBlack;
    }

    /**
     * プレイヤーが石を置けず、スキップが必要か判定する
     * @return スキップの場合は真 {@code true}、石を置ける場所がありスキップでない場合は偽 {@code false} を返す。
     */
    public Boolean isSkip() {
        if (board.canPutAll(playerIsBlack)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 石を置く
     * @param target 石を置く座標
     * @return 対象の座標に石を置いた場合は真 {@code true}、ルールにより石を置けない場合は偽 {@code false} を返す。
     */
    public Boolean put(Dimension target) {
        return board.put(target, playerIsBlack);
    }

    /**
     * 勝敗結果を判定する。
     * @return 結果を返す。勝敗がつかない場合は {@code Result.None} を返す。
     */
    public ResultType judge() {
        if (board.getEmptyNum() > 0) {
            // 盤上に空きがあっても片方の石が全てなくなった場合は勝敗をつける。
            if (board.getWhiteDiscNum() <= 0 && board.getBlackDiscNum() > 0) {
                return ResultType.Black;
            }
            if (board.getBlackDiscNum() <= 0 && board.getWhiteDiscNum() > 0) {
                return ResultType.White;
            }
        } else {
            // 盤上に空きがない場合は石の多さで勝敗をつける。
            if (board.getBlackDiscNum() == board.getWhiteDiscNum()) {
                return ResultType.Drow;
            } else if (board.getBlackDiscNum() > board.getWhiteDiscNum()) {
                return ResultType.Black;
            } else {
                return ResultType.White;
            }
        }
        return ResultType.None;
    }

    /**
     * 次の手番に進める
     */
    public void next() {
        turnCount++;

        // 次に打つプレイヤーを入れ替える
        if (playerIsBlack) {
            playerIsBlack = false;
        } else {
            playerIsBlack = true;
        }
    }
}
