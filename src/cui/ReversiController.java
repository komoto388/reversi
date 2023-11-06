package cui;

import java.util.Scanner;

import common.Convert;
import model.ResultData;
import model.ReversiData;
import model.ReversiModel;
import reversi.Dimension;
import reversi.Reversi;

/**
 * CUI向けのリバーシのゲーム処理を行うコントローラー
 */
public class ReversiController {

    /** ゲーム実行のデータ処理を行うインスタンス（モデル） */
    private ReversiModel reversiModel;

    /** コマンドプロンプトからの入力を受け取るスキャナー */
    private static Scanner scanner;

    /**
     * 初期化をする
     * @param data リバーシゲーム処理を行うために必要なデータ
     * @param scanner 入力処理をするスキャナー
     */
    public ReversiController(ReversiData data, Scanner scanner) {
        this.reversiModel = new ReversiModel(data, false);
        ReversiController.scanner = scanner;
    }

    /**
     * ゲーム処理を実行する 
     * @return 結果画面に渡すデータ
     */
    public ResultData run() {
        int currnetTurn = 0;

        do {
            Reversi reversi = reversiModel.getReversi();

            reversiModel.run();

            // ターンが進んだ時のみ、コマンドプロンプトに描画する
            if (currnetTurn < reversi.getTurnCount()) {
                // 現在の盤面を表示する
                CuiMain.showBoard(reversi);
                currnetTurn = reversi.getTurnCount();

                System.out.printf("%s\n\n", reversiModel.getGameStatusString());
            }

            // 手動入力待ちの時、手動入力の処理を行う
            if (reversiModel.getIsControll()) {
                Dimension target = inputManual();
                reversiModel.put(target);
            }

        } while (!reversiModel.getIsFinish());

        ResultData data = reversiModel.generateData();

        return data;
    }

    /**
     * 手動で石を置く座標を指定する
     * @return プレイヤーが指定した石を置く座標
     */
    private Dimension inputManual() {
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
            Dimension boardSize = reversiModel.getBoardSize();

            if (row < 0 && row >= boardSize.getRow()) {
                System.err.println("行番号は想定されていない値です: " + row);
                System.err.flush();
            } else if (column < 0 && column >= boardSize.getColumn()) {
                System.err.println("列番号は想定されていない値です: " + intputList[0]);
                System.err.flush();
            } else {
                break;
            }
        } while (true);

        return new Dimension(row - 1, column);
    }
}
