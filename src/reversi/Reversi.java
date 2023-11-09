package reversi;

import common.Global;
import gamerecord.GameRecord;

/**
 * リバーシのゲームを定義・処理するクラス
 * @author komoto
 */
public class Reversi {

    /** リバーシ盤状態を表す */
    private Board board;

    /** ゲームに参加しているプレイヤーを格納する配列 */
    private Player[] players;

    /** 現在プレイしているプレイヤーを表すプレイヤー配列のインデック */
    private int currentPlayerIndex;

    /** 経過したターン数 */
    private int turnCount;

    /**
     * リバーシ盤の初期化を行う
     * @param playerBlack 先手・黒のプレイヤー情報
     * @param playerWhite 後手・白のプレイヤー情報
     */
    public Reversi(Player playerBlack, Player playerWhite) {
        // 引数の正常性確認
        try {
            if (playerBlack == null) {
                throw new IllegalArgumentException("先手・黒のプレイヤー \"playerBlack\" が NULL です。");
            }
            if (playerWhite == null) {
                throw new IllegalArgumentException("後手・白のアルゴリズム \"playerWhite\" が NULL です。");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.printf("プレイヤー情報が存在しないないため、異常終了します。");
            System.exit(Global.EXIT_FAILURE);
        }

        this.board = new Board(Global.BOARD_WIDTH, Global.BOARD_HEIGHT);
        this.players = new Player[2];
        this.players[0] = playerBlack;
        this.players[1] = playerWhite;
        this.currentPlayerIndex = 0;
        this.turnCount = 1;
    }

    /**
     * リバーシ盤の状態を取得する
     * @return リバーシ盤の状態
     */
    public Board getBoard() {
        return board;
    }

    /**
     * 現在プレイしているプレイヤーを取得する
     * @return 現在プレイしているプレイヤー
     */
    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    /**
     * 現在の経過ターン数を取得する
     * @return 現在の経過ターン数
     */
    public int getTurnCount() {
        return turnCount;
    }

    /**
     * プレイヤーが石を置けず、スキップが必要か判定する
     * @return スキップの場合は真 {@code true}、石を置ける場所がありスキップでない場合は偽 {@code false}
     */
    public Boolean isSkip() {
        // 全てのマスに対してプレイヤーが石を置けるか調べる
        // 石を置ける場合はスキップしない、置けない場合はスキップすると判断する
        if (board.canPutAll(getCurrentPlayer().isBlack())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 現在のプレイヤーの石をリバーシ盤に置く
     * @param target 石を置く座標
     * @return 対象の座標への石の設置に成功した場合は真 {@code true}、失敗した場合は偽 {@code false}
     * @throws IllegalArgumentException 引数 {@code target} が {@code NULL} の場合はエラーとする。
     */
    public Boolean put(Dimension target) throws IllegalArgumentException {
        // 引数の正常性確認
        if (target == null) {
            throw new IllegalArgumentException("変数 \"target\" が NULL です。");
        }

        // ボードに石を置く処理
        Boolean isPut = false;
        try {
            isPut = board.put(target, getCurrentPlayer().isBlack());
        } catch (IllegalArgumentException e) {
            // 石を置く処理で例外が発生した場合、異常終了する
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println("プログラムを異常終了します。 code: " + exitCode);
            System.exit(exitCode);
        }

        return isPut;
    }

    /**
     * 勝敗結果を判定する。
     * @return 対戦結果の値 (勝敗がつかない場合は {@code Result.None})
     */
    public ResultType judge(GameRecord gameRecord) {
        ResultType result;

        if (isGameFinish(gameRecord)) {
            if (board.getDiscNum(true) == board.getDiscNum(false)) {
                result = ResultType.DRAW;
            } else if (board.getDiscNum(true) > board.getDiscNum(false)) {
                result = ResultType.BLACK;
            } else {
                result = ResultType.WHITE;
            }
        } else {
            result = ResultType.NONE;
        }
        return result;
    }

    /**
     * ゲーム終了を判定する
     * @return ゲーム終了の場合は真 {@code true}, 続行の場合は偽 {@code false}
     */
    private Boolean isGameFinish(GameRecord gameRecord) {
        // 盤上に空きがない場合
        if (board.getEmptyDiscNum() <= 0) {
            gameRecord.setComment("全てのマスが埋まりました");
            return true;
        }

        // 片方のプレイヤーの石が0個になった場合
        if (board.getDiscNum(true) <= 0) {
            gameRecord.setComment("先手・黒の石がなくなりました");
            return true;
        }
        if (board.getDiscNum(false) <= 0) {
            gameRecord.setComment("後手・白の石がなくなりました");
            return true;
        }

        // 両プレイヤーともに石を置く位置がなく、ともにスキップする場合
        if (board.canPutAll(true) == false && board.canPutAll(false) == false) {
            // 両者スキップする棋譜を追加する
            Boolean isPlayerBlack = getCurrentPlayer().isBlack();
            int blackDiscNum = board.getDiscNum(true);
            int whiteDiscNum = board.getDiscNum(false);
            gameRecord.addAsSkip(++turnCount, isPlayerBlack, blackDiscNum, whiteDiscNum);
            gameRecord.addAsSkip(++turnCount, !isPlayerBlack, blackDiscNum, whiteDiscNum);
            gameRecord.setComment("両プレイヤーともにスキップが選択されました");

            return true;
        }

        return false;
    }

    /**
     * 次の手番に進める
     */
    public void next() {
        turnCount++;

        // 次に打つプレイヤーを入れ替える
        if (++currentPlayerIndex >= players.length) {
            currentPlayerIndex = 0;
        }
    }
}
