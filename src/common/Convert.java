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
     * 小文字のアルファベットを数値に変換する
     * a => 0, b => 1, c => 2 ...
     * @param c 0以上の整数
     * @return 変換した数値。正しく変換できなかった場合は {@code -1}
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
     * 指定したミリ秒をフレーム数に変換する
     * @param millisec 指定したミリ秒
     * @param fps 1秒間に描画するフレーム数(FPS)
     * @return 指定したミリ秒から算出したフレーム数。{@code millisec}の値が{@code 0}以下の場合、{@code 0}を返す。
     * @throws IllegalArgumentException 指定された引数が負の値である
     */
    public static int convertFrame(int millisec, int fps) throws IllegalArgumentException {
        int waitFrame = 0;

        if (millisec < 0 || fps < 0) {
            String str = String.format("指定された引数が負の値です: millisec=%d, fps=%d", millisec, fps);
            throw new IllegalArgumentException(str);
        }

        if (millisec > 0) {
            waitFrame = (int) Math.ceil(fps * millisec / (double) 1000);
        } else {
            waitFrame = 0;
        }

        return waitFrame;
    }
}
