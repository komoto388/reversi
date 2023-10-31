package cui;

import java.util.Scanner;

import algorithm.AlgorithmType;
import common.Convert;
import reversi.Dimension;
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
        reversi = new Reversi(AlgorithmType.Manual, AlgorithmType.Random);

        ResultType result;
        do {
            reversi.showCui();

            if (reversi.isSkip()) {
                System.out.printf("置ける場所がないため、スキップします。\n");
                reversi.next();
            } else {
                // プレイヤーが石を置く座標
                Dimension target = null;

                do {
                    if (reversi.isCurrentPlayerManual()) {
                        // 手動で石を置く座標を指定する
                        System.out.printf("石を置く位置を入力してください。\n横の位置？(a,b,...): ");
                        char[] intputList = scanner.next().toCharArray();
                        int column = Convert.convertCharToInt(intputList[0]);

                        System.out.printf("縦の位置？(1,2,...): ");
                        int row = scanner.nextInt() - 1;

                        target = new Dimension(row, column);
                    } else {
                        // 手動以外のアルゴリズムを実行し、石を置く座標を算出する
                        target = reversi.run();
                    }

                    if (reversi.put(target)) {
                        // 指定した座標に石を置ける場合、次の処理に進める
                        System.out.printf("%s は %s に石を置きました。\n",
                                Convert.getPlayerColor(reversi.getPlayerIsBlack()), target.getString());
                        break;
                    } else {
                        // 指定した座標に石を置けない場合、座標を指定し直す。
                        System.out.printf("%sのマスには石を置けません。座標を再指定してください。\n", target.getString());
                    }
                } while (true);

                // 勝敗判定を行う
                result = reversi.judge();
                if (result == ResultType.None) {
                    reversi.next();
                } else {
                    break;
                }
            }
            System.out.printf("\n");
        } while (true);

        // 結果を表示し、ゲームを終了する
        reversi.showCui();
        showResult(result);
        scanner.close();
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
