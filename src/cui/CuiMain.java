package cui;

import java.util.Scanner;

import common.Convert;
import common.Global;
import model.ResultData;
import model.ReversiData;
import reversi.Reversi;
import reversi.Board;
import reversi.Dimension;
import reversi.Disc;

/**
 * CUI版リバーシの処理を行うクラス
 * @author komoto
 */
public class CuiMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println(Global.APP_TITLE);
        Output.printLine(50, '-');

        // プレイヤー選択処理
        PlayerSelectController playerSelectController = new PlayerSelectController(scanner);
        ReversiData reversiData = playerSelectController.run();

        // リバーシのゲームを実行する
        ReversiController reversiController = new ReversiController(reversiData, scanner);
        ResultData resultData = reversiController.run();

        // 結果を表示し、ゲームを終了する
        ResultController resultController = new ResultController(resultData);
        resultController.run();

        // .exe実行時に、自動的にウィンドウを閉じないようにするための対処
        System.out.print("\nプログラムを終了するには Enter キーを入力してください (Press Enter key):");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();
        System.out.println("終了します");
    }

    /**
     * 現在の手番とリバーシ盤の状態を表示する
     */
    static void showBoard(Reversi reversi) {
        Board board = reversi.getBoard();

        System.out.printf("(%d手目)\n", reversi.getTurnCount());

        showBoardOnly(reversi);

        System.out.printf("黒石◯: %2d\n", board.getDiscNum(Disc.BLACK));
        System.out.printf("白石●: %2d\n", board.getDiscNum(Disc.WHITE));

        System.out.printf("【%s】%s のターンです。\n",
                reversi.getCurrentPlayer().getUseDisc().getPrefixForPlayerName(),
                reversi.getCurrentPlayer().getName());
    }

    /**
     * リバーシの状態を表示する
     * @param reversi
     */
    static void showBoardOnly(Reversi reversi) {
        Board board = reversi.getBoard();
        Dimension boardSize = board.getSize();

        // リバーシ盤を表示する        
        // リバーシ盤の上部に列番号を表示する
        System.out.printf(" ");
        for (int j = 0; j < boardSize.getColumn(); j++) {
            System.out.printf("  %c", Convert.convertIntToChar(j));
        }
        System.out.printf("\n");

        for (int i = 0; i < boardSize.getRow(); i++) {
            // リバーシ盤の左側に行番号を表示する
            System.out.printf("%2d", i + 1);

            for (int j = 0; j < boardSize.getColumn(); j++) {
                Dimension target = new Dimension(i, j);

                if (board.isDiscBlack(target)) {
                    System.out.printf(" ◯");
                } else if (board.isDiscWhite(target)) {
                    System.out.printf(" ●");
                } else {
                    System.out.printf(" ―");
                }
            }
            System.out.printf("\n");
        }
    }
}
