package model;

import gamerecord.GameRecord;
import reversi.ResultType;

public class ResultModel extends BaseModel {

    private final ResultType result;

    /**
     * 初期化する
     * @param data ゲーム画面から引き渡した、結果表示に必要なデータ
     */
    public ResultModel(ResultData data) {
        super(data.getReversi(), data.getPlayerBlack(), data.getPlayerBlack());
        result = data.getResult();
    }

    /**
     * 対戦結果の情報を取得する
     * @return 対戦結果の情報
     */
    public ResultType getResultType() {
        return result;
    }

    /**
     * 棋譜の情報を取得する
     * @return 棋譜の情報
     */
    public GameRecord getRecord() {
        return reversi.getRecord();
    }

    /**
     * 勝敗結果の文字列を取得する
     * @return 勝敗結果の文字列
     */
    public String getResultString() {
        String prefixStr = String.format("%d手をもって、", reversi.getTurnCount());
        String resutlStr;

        switch (result) {
        case Black: {
            resutlStr = String.format("先手・黒「%s」の勝ちです！", playerBlack.getName());
            break;
        }
        case White: {
            resutlStr = String.format("後手・白「%s」の勝ちです！", playerWhite.getName());
            break;
        }
        case Drow: {
            resutlStr = "引き分けです！";
            break;
        }
        case None:
        default:
            throw new IllegalArgumentException("Unexpected value: " + result);
        }

        return prefixStr + resutlStr;
    }
}
