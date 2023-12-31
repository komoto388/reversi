package model;

import gamerecord.GameRecord;
import reversi.ResultType;

public class ResultModel extends BaseModel {

    /** 対戦結果の情報を持つインスタンス */
    private ResultType result;

    /** 棋譜の情報を持つインスタンス */
    private GameRecord gameRecord;

    /**
     * 初期化する
     * @param data ゲーム画面から引き渡した、結果表示に必要なデータ
     */
    public ResultModel(ResultData data) {
        super(data.getReversi(), data.getPlayerBlack(), data.getPlayerWhite());
        result = data.getResult();
        gameRecord = data.getGameRecord();
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
        return gameRecord;
    }

    /**
     * 勝敗結果の文字列を取得する
     * @return 勝敗結果の文字列
     */
    public String getResultString() {
        String prefixStr = String.format("%d手をもって、", reversi.getTurnCount());
        String resutlStr;

        switch (result) {
        case BLACK: {
            resutlStr = String.format("%s 「%s」 の勝ちです！", playerBlack.getUseDisc().getPrefixForPlayerName(),
                    playerBlack.getName());
            break;
        }
        case WHITE: {
            resutlStr = String.format("%s 「%s」 の勝ちです！", playerWhite.getUseDisc().getPrefixForPlayerName(),
                    playerWhite.getName());
            break;
        }
        case DRAW: {
            resutlStr = "引き分けです！";
            break;
        }
        case NONE:
        default:
            throw new IllegalArgumentException("Unexpected value: " + result);
        }

        return prefixStr + resutlStr;
    }
}
