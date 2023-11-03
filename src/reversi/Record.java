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
     * @param blackDiscNum 黒石の数
     * @param whiteDiscNum 白石の数
     * @param increaseBlackNum 前回からの黒石の増減量
     * @param increaseWhiteNum 前回からの白石の増減量
     * @param dim プレイヤーが置いた石の座標を表す文字列、またはスキップなど石を置かなかった場合の操作を表す文字列
     */
    public Record(int turn, Boolean playerIsBlack, int blackDiscNum, int whiteDiscNum,
            int increaseBlackNum, int increaseWhiteNum, String dim) {
        // 引数の正常性確認
        try {
            if (turn <= 0) {
                throw new IllegalArgumentException("引数 \"turn\" の値が0以下です: " + turn);
            }
        } catch (Exception e) {
            e.printStackTrace();
            turn = 0;
            System.err.println("\"turn\" の値として " + turn + " を設定します");
        }

        try {
            if (playerIsBlack == null) {
                throw new IllegalArgumentException("引数 \"playerIsBlack\" の値が NULL です");
            }
        } catch (Exception e) {
            e.printStackTrace();
            playerIsBlack = true;
            System.err.println("\"playerIsBlack\" の値として " + playerIsBlack + " を設定します");
        }

        try {
            if (blackDiscNum < 0) {
                throw new IllegalArgumentException("引数 \"blackDiscNum\" の値が0未満です: " + blackDiscNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
            blackDiscNum = 0;
            System.err.println("\"blackDiscNum\" の値として " + blackDiscNum + " を設定します");
        }

        try {
            if (whiteDiscNum < 0) {
                throw new IllegalArgumentException("引数 \"whiteDiscNum\" の値が0未満です: " + whiteDiscNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
            whiteDiscNum = 0;
            System.err.println("\"whiteDiscNum\" の値として " + whiteDiscNum + " を設定します");
        }

        try {
            if (dim == null) {
                throw new IllegalArgumentException("引数 \"dim\" の値が NULL です");
            }
        } catch (Exception e) {
            e.printStackTrace();
            dim = "NULL";
            System.err.println("\"dim\" の値として \"" + dim + "\" を設定します\n");
        }

        // 引数の値を設定します
        this.turn = turn;
        this.playerString = Convert.getPlayerColor(playerIsBlack);
        this.blackDiscNum = blackDiscNum;
        this.whiteDiscNum = whiteDiscNum;
        this.increaseBlackNum = increaseBlackNum;
        this.increaseWhiteNum = increaseWhiteNum;
        this.dimString = dim;
    }
}
