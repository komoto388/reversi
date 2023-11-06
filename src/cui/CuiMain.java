package cui;

import java.util.Scanner;

import common.Convert;
import model.ReversiData;
import reversi.Dimension;
import reversi.ResultType;
import reversi.Reversi;
import reversi.Record;
import reversi.RecordRow;

/**
 * CUI版リバーシの処理を行うクラス
 * @author komoto
 */
public class CuiMain {
    private static Scanner scanner;
    private static Reversi reversi;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        // プレイヤー選択処理
        PlayerSelectController playerSelectController = new PlayerSelectController(scanner);
        ReversiData reversiData = playerSelectController.run();

        // リバーシのゲームを実行する
        reversi = reversiData.getReversi();

        ResultType result;
        do {
            reversi.showBoardCui();
            result = playTurn();
            System.out.printf("\n");
        } while (result == ResultType.None);

        // 結果を表示し、ゲームを終了する
        showResult(result);
        scanner.close();
    }

    /**
     * 1ターン分のゲーム処理を行う
     * @return 勝敗結果を返す。勝敗がついていない場合は {@code ResultType.None} を返す。
     */
    private static ResultType playTurn() {
        // スキップする必要があるか判定する
        if (reversi.isSkip()) {
            System.out.printf("置ける場所がないため、スキップします。\n");
            reversi.next();
            return ResultType.None;
        }

        // 石を置く位置を決定する
        do {
            Dimension target;
            Boolean isPut = false;

            if (reversi.isCurrentPlayerManual()) {
                target = runManual();
            } else {
                // 手動以外のアルゴリズムを実行し、石を置く座標を算出する
                target = reversi.run();
            }

            try {
                isPut = reversi.put(target);
            } catch (IllegalArgumentException e) {
                // 石を置く処理で例外が発生した場合、再入力させる
                e.printStackTrace();
                isPut = false;
            }

            if (isPut) {
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
        ResultType result = reversi.judge();
        if (result == ResultType.None) {
            reversi.next();
        }

        return result;
    }

    /**
     * 手動で石を置く座標を指定する
     * @return プレイヤーが指定した石を置く座標
     */
    private static Dimension runManual() {
        int row, column;

        do {
            char[] intputList;

            System.out.println("石を置く位置を入力してください。");
            System.out.print("座標？(a1,b2,...): ");

            try {
                intputList = scanner.next().toCharArray();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            // 入力文字列に問題ないかの確認
            // 入力文字数が適正か
            if (intputList.length != 2) {
                System.err.println("入力された文字数が2文字ではありません: " + intputList.length);
                System.err.flush();
                continue;
            }

            // 座標を行番号と列番号に分離する
            try {
                column = Convert.convertCharToInt(intputList[0]);
                row = Integer.parseInt(Character.toString(intputList[1]));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            // 行番号、列番号が範囲内にあるか
            if (row < 0 && row >= reversi.getBoard().getSize().getRow()) {
                System.err.println("行番号は想定されていない値です: " + row);
                System.err.flush();
            } else if (column < 0 && column >= reversi.getBoard().getSize().getColumn()) {
                System.err.println("列番号は想定されていない値です: " + intputList[0]);
                System.err.flush();
            } else {
                break;
            }
        } while (true);

        return new Dimension(row - 1, column);
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
        Record record = reversi.getRecord();
        System.out.printf("\n棋譜\n");
        while (record.isEmpty() == false) {
            RecordRow recordRow = record.poll();
            System.out.printf("%2d手目  %s  %-4s  黒 %2d個 (%+3d)  白 %2d個 (%+3d)\n",
                    recordRow.turn, recordRow.playerString, recordRow.dimString, recordRow.blackDiscNum,
                    recordRow.increaseBlackNum, recordRow.whiteDiscNum, recordRow.increaseWhiteNum);
        }
        System.out.println("\n終了理由: " + record.getComment());

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
