package test;

import common.Global;

/**
 * テスト用のIOを提供するクラス
 * @implNote この機能は JUnit などのテストでの使用のみを想定しており、運用での使用は非推奨です。
 * @author komoto
 */
public class TestIO {

    /**
     * 乱数使用フラグが有効の時、無効にするメッセージを表示してプログラムを終了する 
     * @param className 呼び出し元のクラス名
     */
    public static void printRandomDisable(String className) {
        if (Global.IS_ADD_RANDOM == true) {
            String msg = String.format("******** %s ********\n"
                    + "Global.IS_ADD_RANDOM の値が True のため、テストプログラムを終了します。\n"
                    + "テストを行うには評価値に乱数が加算しないように Global.IS_ADD_RANDOM の値が false である必要があります。\n"
                    + "テストを続行するには Global.IS_ADD_RANDOM の値を一時的に false へ書き換えてください。\n"
                    + "*******************************************", className);
            System.out.println(msg);
            System.exit(1);
        }
    }

    public static void printRestoreSetting(String className) {
        String msg = String.format("******** %s ********\n"
                + "テストが完了しました。\n"
                + "テストを行わない場合は、Global.IS_ADD_RANDOM の値を True に戻すことを忘れないようにしてください\n"
                + "*******************************************", className);
        System.out.println(msg);
    }
}
