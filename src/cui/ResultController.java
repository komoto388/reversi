package cui;

import gamerecord.GameRecord;
import model.ResultData;
import model.ResultModel;

/**
 * CUIに結果を表示するコントロール
 * @author komoto
 */
class ResultController {

    /** 区切り線の長さ（表示する文字数） */
    private final int LINE_LENGTH = 60;

    /** 区切り線に表示する文字 */
    private final char LINE_CHARACTER = '-';

    /** 結果表示処理を行うインスタンス（モデル） */
    ResultModel resultModel;

    /**
     * 初期化する
     * @param data 結果表示処理で使用するデータ群
     */
    public ResultController(ResultData data) {
        resultModel = new ResultModel(data);
    }

    /**
     * 対戦結果をプロンプトに表示する
     */
    public void run() {
        Output.printLine(LINE_LENGTH, LINE_CHARACTER);

        System.out.printf("\n対戦結果は以下の通りです。\n\n");

        // 最終のリバーシ盤を表示する。
        System.out.printf("最終盤面\n");
        CuiMain.showBoard(resultModel.getReversi());

        Output.printLine(LINE_LENGTH, LINE_CHARACTER);

        // 棋譜の表示
        outputRecord();

        Output.printLine(LINE_LENGTH, LINE_CHARACTER);

        // 対戦結果を表示する。
        System.out.printf("\n結果\n");
        System.out.println(resultModel.getResultString());

        Output.printLine(LINE_LENGTH, LINE_CHARACTER);
    }

    /**
     * 棋譜に記録された、各手番の記録と対戦終了理由をプロンプトに表示する
     */
    private void outputRecord() {
        GameRecord record = resultModel.getRecord();
        System.out.printf("\n棋譜\n");

        for (int i = 0; i < record.size(); i++) {
            int turn = record.getTurn(i);
            String player = record.getPlayerString(i);
            String dim = record.getDimString(i);
            int blackDiscNum = record.getBlackDiscNum(i);
            int whiteDiscNum = record.getWhiteDiscNum(i);
            int increaseBlackNum = record.getIncreaseBlackNum(i);
            int increaseWhiteNum = record.getIncreaseWhiteNum(i);

            System.out.printf("%2d手目  %s  %-4s  黒 %2d個 (%+3d)  白 %2d個 (%+3d)\n",
                    turn, player, dim, blackDiscNum, increaseBlackNum, whiteDiscNum, increaseWhiteNum);
        }

        System.out.printf("\n終了理由: %s\n", record.getComment());
    }
}
