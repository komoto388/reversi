package common;

/**
 * 数値・文字列変換を定義するクラス
 */
public class Convert {
    /**
     * 数字を小文字のアルファベットに変換する。
     * 0 => a, 1 => b, 2 => c ...
     * @param num 0以上の整数
     * @return 変換した文字列
     */
    public static char convertIntToChar(int num) throws IllegalArgumentException {
        if (num >= 0) {
            return (char) ('a' + num);
        } else {
            throw new IllegalArgumentException("引数が0以上でありません: " + num);
        }
    }

    /**
     * 小文字のアルファベットを数値に変換する。
     * a => 0, b => 1, c => 2 ...
     * @param c 0以上の整数
     * @return 変換した数値。正しく変換できなかった場合は {@code -1} を返す。
     */
    public static int convertCharToInt(char c) throws IllegalArgumentException {
        int num = c - 'a';
        if (num >= 0) {
            return num;
        } else {
            throw new IllegalArgumentException("引数が英小文字ではありません: " + c);
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

    /**
     * 指定したミリ秒をフレーム数に変換する
     * @param millisec 指定したミリ秒
     * @param fps 1秒間に描画するフレーム数(FPS)
     * @return 指定したミリ秒から算出したフレーム数。{@code millisec}の値が{@code 0}以下の場合、{@code 0}を返す。
     */
    public static int convertFrame(int millisec, int fps) {
        if (millisec > 0) {
            return (fps * millisec / 1000);
        } else {
            return 0;
        }
    }

}
