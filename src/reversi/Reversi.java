package reversi;

/**
 * リバーシのゲームを定義・処理するクラス
 * @author komoto
 */
public class Reversi {
    private Board board;

    /** 現在のプレイヤーが黒かどうかを表す。*/
    private Boolean playerIsBlack;

    /** 経過したターン数 */
    private int turnCount;

    /**
     * リバーシ盤の初期化を行う
     */
    public Reversi() {
        board = new Board();
        playerIsBlack = true;
        turnCount = 1;
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
        if (board.put(target, playerIsBlack)) {
            return true;
        } else {
            return false;
        }
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
