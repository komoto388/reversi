package gamerecord;

import java.util.ArrayList;
import java.util.List;

/**
 * 棋譜を記録・操作するクラス
 * @author komoto
 */
public class GameRecord {

    /** 棋譜情報を格納するリスト */
    private List<RecordData> recordDataList;

    /** 1つ前の手番での黒石の数 */
    private int previousBlackNum;

    /** 1つ前の手番での白石の数 */
    private int previousWhiteNum;

    /** 対戦が終了した理由を表す文字列 */
    private String comment;

    /**
     * 棋譜の初期化
     */
    public GameRecord() {
        this.recordDataList = new ArrayList<RecordData>();
        this.previousBlackNum = 2;
        this.previousWhiteNum = 2;
        this.comment = "";
    }

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
     * 棋譜に記録を１行追加する
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

        RecordData data = new RecordData(turn, playerIsBlack, blackDiscNum, whiteDiscNum, increaseBlackNum,
                increaseWhiteNum, dim);

        try {
            recordDataList.add(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 棋譜にスキップの記録を１行追加する
     * @param turn ターン数（手番）
     * @param playerIsBlack プレイヤーの石の色を表す。
     * @param blackDiscNum 黒石の数
     * @param whiteDiscNum 白石の数
     */
    public void addAsSkip(int turn, Boolean playerIsBlack, int blackDiscNum, int whiteDiscNum) {
        add(turn, playerIsBlack, blackDiscNum, whiteDiscNum, null);
    }

    /**
     * 棋譜が空かどうかを返す。
     * @return 棋譜の要素の数
     */
    public int size() {
        return recordDataList.size();
    }

    /**
     * ターン数を取得する
     * @param index 要素の番号
     * @return 指定された番号の要素にあるターン数
     */
    public int getTurn(int index) {
        return recordDataList.get(index).turn;
    }

    /**
     * プレイヤー情報を取得する
     * @param index 要素の番号
     * @return 指定された番号の要素にあるプレイヤーの文字列
     */
    public String getPlayerString(int index) {
        return recordDataList.get(index).playerString;
    }

    /**
     * プレイヤーが打った石の座標を取得する。
     * スキップの場合は "Skip" とする。
     * @param index 要素の番号
     * @return 指定された番号の要素にあるプレイヤーが打った石の座標の文字列
     */
    public String getDimString(int index) {
        String str = recordDataList.get(index).dimString;
        if (str == null) {
            str = "Skip";
        }

        return str;
    }

    /**
     * 先手・黒の石の個数を取得する
     * @param index 要素の番号
     * @return 指定された番号の要素にある先手・黒の石の個数 
     */
    public int getBlackDiscNum(int index) {
        return recordDataList.get(index).blackDiscNum;
    }

    /**
     * 後手・白の石の個数を取得する
     * @param index 要素の番号
     * @return 指定された番号の要素にある後手・白の石の個数 
     */
    public int getWhiteDiscNum(int index) {
        return recordDataList.get(index).whiteDiscNum;
    }

    /**
     * 先手・黒の石の増減数を取得する
     * @param index 要素の番号
     * @return 指定された番号の要素にある先手・黒の石の増減数
     */
    public int getIncreaseBlackNum(int index) {
        return recordDataList.get(index).increaseBlackNum;
    }

    /**
     * 後手・白の石の増減数を取得する
     * @param index 要素の番号
     * @return 指定された番号の要素にある後手・白の石の増減数
     */
    public int getIncreaseWhiteNum(int index) {
        return recordDataList.get(index).increaseWhiteNum;
    }

}
