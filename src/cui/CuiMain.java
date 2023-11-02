package cui;

import java.util.Scanner;

import algorithm.AlgorithmType;
import common.Convert;
import reversi.Dimension;
import reversi.ResultType;
import reversi.Reversi;
import reversi.Record;

/**
 * CUI版リバーシの処理を行うクラス
 * @author komoto
 */
public class CuiMain {
    private static Scanner scanner;
    private static Reversi reversi;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        AlgorithmType typeBlack = selectAlgorithm("先手・黒");
        AlgorithmType typeWhite = selectAlgorithm("後手・白");
        reversi = new Reversi(typeBlack, typeWhite);

        ResultType result;
        do {
            reversi.showBoardCui();

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
        showResult(result);
        scanner.close();
    }

    /**
     * プレイヤーが使用するアルゴリズムを選択する
     * @return 選択したアルゴリズムを表す値
     */
    private static AlgorithmType selectAlgorithm(String playerName) {
        AlgorithmType[] typeList = AlgorithmType.values();
        int inputNum = 0;

        System.out.printf("%sが使用するアルゴリズムを選択してください。手動操作の場合は、「%s」を選択してください。\n", 
                playerName, AlgorithmType.Manual);
        
        do {
            for (int i = 0; i < typeList.length; i++) {
                System.out.printf("%2d. %s\n", i + 1, typeList[i]);
            }
            System.out.printf("\n選択肢？: ");

            try {
                inputNum = scanner.nextInt();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("数字以外の文字は想定されていません。");
                System.err.flush();
                System.out.println("使用するアルゴリズムを再度入力してください");
            }
            
            if (inputNum > 0 && inputNum <= typeList.length) {
                // 範囲内の数値が入力された場合は、次の処理に進む
                break;
            } else {
                // 範囲外の数値が入力された場合は、再入力を促す
                System.err.println("想定されていない値です: " + inputNum);
                System.err.flush();
                System.out.println("使用するアルゴリズムを再度入力してください");
                continue;
            }
        } while (true);
        
        AlgorithmType type = typeList[inputNum - 1];
        System.out.printf("「%d. %s」が選択されました", inputNum, type);

        return type;
    }

    /**
     * ゲーム結果を表示する
     * @param result
     */
    private static void showResult(ResultType result) {
        System.out.printf("\n------------------------------\n");
        System.out.printf("対戦結果は以下の通りです。\n\n");

        // 最終のリバーシ盤を表示する。
        System.out.printf("最終盤面\n");
        reversi.getBoard().showCui();

        // 譜面記録を表示する。
        System.out.printf("\n棋譜\n");
        while (reversi.getRecordList().isEmpty() == false) {
            Record record = reversi.getRecordList().poll();
            System.out.printf("%2d手目  %s  %-4s  黒 %2d個 (%+3d)  白 %2d個 (%+3d)\n",
                    record.turn, record.playerString, record.dimString, record.blackDiscNum, record.increaseBlackNum,
                    record.whiteDiscNum, record.increaseWhiteNum);
        }

        // 対戦結果を表示する。
        System.out.printf("\n結果\n%d手、黒%2d個、白%2d個を以って、",
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
        System.out.printf("------------------------------\n");
    }
}
