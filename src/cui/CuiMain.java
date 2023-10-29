package cui;

import java.util.Scanner;

import algorithm.AlgorithmType;
import reversi.Board;
import reversi.Dimension;
import reversi.Player;
import reversi.ResultType;
import reversi.Reversi;

/**
 * CUI版リバーシの処理を行うクラス
 * @author komoto
 */
public class CuiMain {
    private static Scanner scanner;
    private static Reversi reversi;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        reversi = new Reversi();

        /*** 仮処理 ************************************/
        Player player1 = new Player(AlgorithmType.CuiManual);
        Player player2 = new Player(AlgorithmType.CuiManual);

        do {
            // 描画
            System.out.printf("(%d手目) ", reversi.getTurnCount());
            if (reversi.getPlayerIsBlack()) {
                System.out.printf("黒 ◯ のターンです。\n");
            } else {
                System.out.printf("白 ● のターンです。\n");
            }
            reversi.getBoard().showCui();
            ;

            if (reversi.isSkip()) {
                System.out.printf("置ける場所がないため、スキップします。\n");
            } else {
                System.out.printf("置ける場所があります。\n");
                reversi.put(new Dimension(3 - 1, 4 - 1));

                reversi.next();
                break;
            }
        } while (true);

        reversi.getBoard().showCui();
        showResult(ResultType.Drow);

        /*** 仮処理 ************************************/

        scanner.close();
    }

    /**
     * スキャナーのインスタンスを返す
     * @return スキャナーのインスタンス
     */
    public static Scanner getScanner() {
        return scanner;
    }

    /**
     * ゲーム結果を表示する
     * @param result
     */
    private static void showResult(ResultType result) {
        System.out.printf("%d手、黒%2d個、白%2d個で、",
                reversi.getTurnCount(), reversi.getBoard().getBlackDiscNum(), reversi.getBoard().getWhiteDiscNum());

        switch (result) {
        case Black: {
            System.out.printf("先手・黒の勝ちです。\n");
            break;
        }
        case White: {
            System.out.printf("後手・白の勝ちです。\n");
            break;
        }
        case Drow: {
            System.out.printf("引き分けです。\n");
            break;
        }
        case None:
        default:
            throw new IllegalArgumentException("Unexpected value: " + result);
        }
    }
}
