package reversi;

import common.Convert;

/**
 * １手分の棋譜を記録するクラス
 * @author komoto
 */
public class Record {

    /** ターン数 */
    public final int turn;

    /** プレイヤーの文字列 */
    public final String playerString;

    /** プレイヤーが打った石の座標の文字列 */
    public final String dimString;

    /** 黒石の個数 */
    public final int blackDiscNum;

    /** 白石の個数 */
    public final int whiteDiscNum;

    /** 1手前からの黒石の増加量 */
    public final int increaseBlackNum;

    /** 1手前からの白石の増加量 */
    public final int increaseWhiteNum;

    /**
     * 棋譜１手分の情報を記録する。
     * @param turn ターン数（手番）
     * @param playerIsBlack プレイヤーの石の色を表す。
     * @param dim プレイヤーが置いた石の座標
     * @param blackDiscNum 黒石の数
     * @param whiteDiscNum 白石の数
     * @param increaseBlackNum 前回からの黒石の増減量
     * @param increaseWhiteNum 前回からの白石の増減量
     */
    public Record(int turn, Boolean playerIsBlack, Dimension dim, int blackDiscNum, int whiteDiscNum,
            int increaseBlackNum, int increaseWhiteNum) {
        this.turn = turn;
        this.playerString = Convert.getPlayerColor(playerIsBlack);
        this.blackDiscNum = blackDiscNum;
        this.whiteDiscNum = whiteDiscNum;
        this.increaseBlackNum = increaseBlackNum;
        this.increaseWhiteNum = increaseWhiteNum;

        if (dim.getRow() == -1 && dim.getColumn() == -1) {
            this.dimString = "Skip";
        } else {
            this.dimString = dim.getString();
        }
    }
}
