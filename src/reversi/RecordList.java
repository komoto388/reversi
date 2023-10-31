package reversi;

import java.util.ArrayDeque;
import java.util.Queue;

import common.Convert;

/**
 * 棋譜を記録・操作するクラス
 * @author komoto
 */
public class RecordList {
    /**
     * １手分の棋譜を記録する内部クラス
     */
    private class Record {
        /** ターン数 */
        private final int turn;

        /** プレイヤーの石の色 */
        private final Boolean playerIsBlack;

        /** プレイヤーが打った石の座標 */
        private final Dimension dim;

        /** 黒石の個数 */
        private final int blackDiscNum;

        /** 白石の個数 */
        private final int whiteDiscNum;

        /**
         * 棋譜１手分の情報を格納する。
         * @param turn ターン数（手番）
         * @param playerIsBlack プレイヤーの石の色を表す。
         * @param dim プレイヤーが置いた石の座標
         * @param blackDiscNum 黒石の数
         * @param whiteDiscNum 白石の数
         */
        private Record(int turn, Boolean playerIsBlack, Dimension dim, int blackDiscNum, int whiteDiscNum) {
            this.turn = turn;
            this.dim = dim;
            this.playerIsBlack = playerIsBlack;
            this.blackDiscNum = blackDiscNum;
            this.whiteDiscNum = whiteDiscNum;
        }
    }

    /** 棋譜情報を格納するキュー */
    private Queue<Record> recordList = new ArrayDeque<Record>();

    /** 1つ前の手番での黒石の数 */
    private int previousBlackNum;

    /** 1つ前の手番での白石の数 */
    private int previousWhiteNum;

    public RecordList() {
        recordList = new ArrayDeque<Record>();
        previousBlackNum = 2;
        previousWhiteNum = 2;
    }

    /**
     * 棋譜をリストに追加する
     * @param turn ターン数（手番）
     * @param playerIsBlack プレイヤーの石の色を表す。
     * @param dim プレイヤーが置いた石の座標
     * @param blackDiscNum 黒石の数
     * @param whiteDiscNum 白石の数
     */
    public void add(int turn, Boolean playerIsBlack, Dimension target, int blackDiscNum, int whiteDiscNum) {
        recordList.add(new Record(turn, playerIsBlack, target, blackDiscNum, whiteDiscNum));
    }

    /**
     * スキップの棋譜をリストに追加する
     * @param turn ターン数（手番）
     * @param playerIsBlack プレイヤーの石の色を表す。
     * @param blackDiscNum 黒石の数
     * @param whiteDiscNum 白石の数
     */
    public void addSkip(int turn, Boolean playerIsBlack, int blackDiscNum, int whiteDiscNum) {
        recordList.add(new Record(turn, playerIsBlack, new Dimension(-1, -1), blackDiscNum, whiteDiscNum));
    }

    /**
     * 棋譜リストが空かどうかを返す。
     * @return 棋譜が空の場合は {@code true}, 空でない場合は {@code false} を返す。
     */
    public Boolean isEmpty() {
        if (recordList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * リストから棋譜情報を取り出す。取り出した棋譜はリストから削除される。
     * @return １手分の棋譜の文字列を返す。棋譜が空(EOF)の場合は {@code null} を返す。
     */
    public String poll() {
        if (recordList.isEmpty()) {
            return null;
        } else {
            Record record = recordList.poll();

            String target;
            if (record.dim.getRow() == -1 && record.dim.getColumn() == -1) {
                target = "Skip";
            } else {
                target = record.dim.getString();
            }

            // １つ前の手番との黒石・白石の増減を計算する。
            int increaseBlackNum = record.blackDiscNum - previousBlackNum;
            int increaseWhiteNum = record.whiteDiscNum - previousWhiteNum;
            previousBlackNum = record.blackDiscNum;
            previousWhiteNum = record.whiteDiscNum;

            return String.format("%2d手目  %s  %-4s  黒 %2d個 (%+3d)  白 %2d個 (%+3d)",
                    record.turn, Convert.getPlayerColor(record.playerIsBlack), target,
                    record.blackDiscNum, increaseBlackNum,
                    record.whiteDiscNum, increaseWhiteNum);
        }
    }
}
