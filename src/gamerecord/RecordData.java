package gamerecord;

import reversi.Player;

/**
 * 棋譜の行を定義・処理するクラス
 * @author komoto
 */
class RecordData {

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
     * 棋譜の行情報を記録する。
     * @param turn ターン数（手番）
     * @param player 石を置いたプレイヤー
     * @param blackDiscNum 黒石の数
     * @param whiteDiscNum 白石の数
     * @param increaseBlackNum 前回からの黒石の増減量
     * @param increaseWhiteNum 前回からの白石の増減量
     * @param dim プレイヤーが置いた石の座標を表す文字列、またはスキップなど石を置かなかった場合の操作を表す文字列
     * @throws IllegalArgumentException 引数が想定されていない値である
     */
    public RecordData(int turn, Player player, int blackDiscNum, int whiteDiscNum,
            int increaseBlackNum, int increaseWhiteNum, String dim) throws IllegalArgumentException {
        // 引数の正常性確認
        if (turn <= 0) {
            throw new IllegalArgumentException("引数 \"turn\" の値が0以下です: " + turn);
        }

        if (player == null) {
            throw new IllegalArgumentException("引数 \"player\" の値が NULL です");
        }

        if (blackDiscNum < 0) {
            throw new IllegalArgumentException("引数 \"blackDiscNum\" の値が0未満です: " + blackDiscNum);
        }

        if (whiteDiscNum < 0) {
            throw new IllegalArgumentException("引数 \"whiteDiscNum\" の値が0未満です: " + whiteDiscNum);
        }

        // 引数の値を設定します
        this.turn = turn;
        this.playerString = player.getUseDisc().getPrefixForPlayerName();
        this.blackDiscNum = blackDiscNum;
        this.whiteDiscNum = whiteDiscNum;
        this.increaseBlackNum = increaseBlackNum;
        this.increaseWhiteNum = increaseWhiteNum;
        this.dimString = dim;
    }
}
