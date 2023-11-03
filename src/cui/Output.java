package cui;

/**
 * 画面出力の操作に関するクラス
 * @author komoto
 *
 */
public class Output {
    /** 文字列出力の有効・無効を表すフラグ */
    private static Boolean isVisible = true;

    /* 色コードを定義する定数 */
    /** 赤色の色コードを表す定数 */
    private static final String RED = "\u001b[00;31m";

    /** 黄色の色コードを表す定数 */
    private static final String YELLOW = "\u001b[00;33m";

    /** 青色の色コードを表す定数 */
    private static final String CYAN = "\u001b[00;36m";

    /** 色指定の終わりを表す定数 */
    private static final String END = "\u001b[00m";

    /**
     * 文字列の表示フラグを変更する
     * @param isVisible {@code true} は表示する。{@code false} は表示しない。
     */
    public static void set(Boolean isVisible) {
        Output.isVisible = isVisible;
    }

    /**
     * 文字列をプロンプトに表示する
     * @param str エラーとして表示する、書式付きの説明文
     * @param args 説明文の引数
     */
    public static void printf(String str, Object... args) {
        if (isVisible) {
            System.out.printf(str, args);
        }
    }

    /**
     * エラーを表示する
     * @param args 説明文の引数
     */
    public static void printfAsError(String str, Object... args) {
        if (isVisible) {
            System.out.printf(RED + "Error: " + str + END, args);
        }
    }

    /**
     * 警告を表示する
     * @param str 警告として表示する、書式付きの説明文
     * @param args 説明文の引数
     */
    public static void printfAsWarning(String str, Object... args) {
        if (isVisible) {
            System.out.printf(YELLOW + "Warning: " + str + END, args);
        }
    }

    /**
     * 情報を表示する
     * @param str 情報として表示する、書式付きの説明文
     * @param args 説明文の引数
     */
    public static void printfAsInfo(String str, Object... args) {
        if (isVisible) {
            System.out.printf(CYAN + "Info: " + str + END, args);
        }
    }
}
