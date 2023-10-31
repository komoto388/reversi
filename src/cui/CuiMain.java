package cui;

import java.util.Scanner;

import algorithm.AlgorithmType;
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
        reversi = new Reversi(AlgorithmType.Manual, AlgorithmType.Manual);

        ResultType result;
        do {
            // 描画
            reversi.showCui();

            if (reversi.isSkip()) {
                System.out.printf("置ける場所がないため、スキップします。\n");
                reversi.next();
            } else {
                Dimension target = null;
                do {
                    if(reversi.isCurrentPlayerManual()) {
                        // 手動で石を置く位置を指定する
                        int row, column;
                        
                        System.out.printf("石を置く位置を入力してください。\n横の位置？: ");
                        column = scanner.nextInt() - 1;
                        System.out.printf("縦の位置？: ");
                        row = scanner.nextInt() - 1;
                        
                        target = new Dimension(row, column);
                    } else {
                        System.out.printf("今のプレイヤーはCOMです。\n");
                    }
                    
                    // 石を置く処理を行う。置けない場合は再設置を促す。
                    if(reversi.put(target)) {
                        break;
                    } else {
                        System.out.printf("%sのマスには石を置けません。\n", target.getString());
                    }
                } while(true);
                
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
