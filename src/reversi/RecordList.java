package reversi;

import java.util.ArrayDeque;
import java.util.Queue;

import common.Global;

/**
 * 棋譜を記録・操作するクラス
 * @author komoto
 */
public class RecordList {

    /** 棋譜情報を格納するキュー */
    private Queue<Record> recordList = new ArrayDeque<Record>();

    /** 1つ前の手番での黒石の数 */
    private int previousBlackNum = 2;

    /** 1つ前の手番での白石の数 */
    private int previousWhiteNum = 2;

    /** 対戦が終了した理由を表す文字列 */
    private String comment = null;

    /**
     * 対戦が終了した理由を設定する
     * @param result 設定する文字列
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 対戦が終了した理由の文字列を取得する
     * @return 結果の文字列
     */
    public String getComment() {
        return comment;
    }

    /**
     * 棋譜をリストに追加する
     * @param turn ターン数（手番）
     * @param playerIsBlack プレイヤーの石の色を表す。
     * @param blackDiscNum 黒石の数
     * @param whiteDiscNum 白石の数
     * @param dim プレイヤーが置いた石の座標の文字列、またはスキップを表す文字列
     */
    public void add(int turn, Boolean playerIsBlack, int blackDiscNum, int whiteDiscNum, String dim) {
        int increaseBlackNum = blackDiscNum - previousBlackNum;
        int increaseWhiteNum = whiteDiscNum - previousWhiteNum;
        previousBlackNum = blackDiscNum;
        previousWhiteNum = whiteDiscNum;

        try {
            recordList.add(new Record(turn, playerIsBlack, blackDiscNum, whiteDiscNum, increaseBlackNum,
                    increaseWhiteNum, dim));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * スキップの棋譜をリストに追加する
     * @param turn ターン数（手番）
     * @param playerIsBlack プレイヤーの石の色を表す。
     * @param blackDiscNum 黒石の数
     * @param whiteDiscNum 白石の数
     */
    public void addSkip(int turn, Boolean playerIsBlack, int blackDiscNum, int whiteDiscNum) {
        this.add(turn, playerIsBlack, blackDiscNum, whiteDiscNum, "SKIP");
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
     * @return １手分の棋譜を返す。棋譜が空(EOF)の場合は {@code null} を返す。
     */
    public Record poll() {
        if (recordList.isEmpty()) {
            return null;
        } else {
            return recordList.poll();
        }
    }
}
