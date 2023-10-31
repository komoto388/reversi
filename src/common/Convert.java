package common;

/**
 * 数値・文字列変換を定義するクラス
 */
public class Convert {
    /**
     * 数字を小文字のアルファベットに変換する。
     * 0 => a, 1 => b, 2 => c ...
     * @param num 0以上の整数
     * @return 変換した文字列。{@code num} が0未満の場合は空文字を返す。
     */
    public static char convertIntToChar(int num) {
        if (num >= 0) {
            return (char) ('a' + num);
        } else {
            return ' ';
        }
    }

    /**
     * 小文字のアルファベットを数値に変換する。
     * a => 0, b => 1, c => 2 ...
     * @param c 0以上の整数
     * @return 変換した数値。正しく変換できなかった場合は {@code -1} を返す。
     */
    public static int convertCharToInt(char c) {
        int num = c - 'a';
        if (num >= 0) {
            return num;
        } else {
            return -1;
        }
    }


    /**
     * 先手後手、石の色といった、プレイヤー情報の文字列を返す
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return プレイヤーを表す文字列（先手後手、石の色）
     */
    public static String getPlayerColor(Boolean isBlack) {
        if (isBlack) {
            return "先手・黒";
        } else {
            return "後手・白";
        }
    }

}
