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

    /** 黒側のプレイヤー */
    private Player playerBlack;

    /** 白側のプレイヤー */
    private Player playerWhite;

    /** 現在プレイしているプレイヤー */
    private Player currentPlayer;

    /** 経過したターン数 */
    private int turnCount;

    /** 棋譜の記録を行うインスタンス */
    private GameRecord record;

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
        this.playerBlack = playerBlack;
        this.playerWhite = playerWhite;
        this.currentPlayer = playerBlack;
        this.turnCount = 1;
        this.record = new GameRecord();
    }

    /**
     * リバーシ盤の状態を返す
     * @return リバーシ盤の状態
     */
    public Board getBoard() {
        return board;
    }

    /**
     * 先手・黒のプレイヤー情報を取得する
     * @return 先手・黒のプレイヤーのインスタンス
     */
    public Player getPlayerBlack() {
        return playerBlack;
    }

    /**
     * 後手・白のプレイヤー情報を取得する
     * @return 後手・白のプレイヤーのインスタンス
     */
    public Player getPlayerWhite() {
        return playerWhite;
    }

    /**
     * 現在の経過ターン数を返す
     * @return 現在の経過ターン数
     */
    public int getTurnCount() {
        return turnCount;
    }

    /**
     * 現在のプレイヤーの石の色を返す
     * @return 現在の経過ターン数
     */
    public Boolean getPlayerIsBlack() {
        return currentPlayer.isDiscBlack();
    }

    /**
     * 記録された棋譜のインスタンスを返す
     * @return 記録された棋譜のインスタンス
     */
    public GameRecord getRecord() {
        return record;
    }

    /**
     * 現在のプレイヤーが手動かどうかを返す
     * @return 手動操作のプレイヤーである場合は真 {@code true}、自動処理のプレイヤーの場合は偽 {@code false} を返す。
     */
    public Boolean isCurrentPlayerManual() {
        if (currentPlayer.isManual()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * プレイヤー毎のアルゴリズムに基づき、石を打つ座標を決定する
     * @return 石を打つ座標。算出できない場合は{@code NULL}を返す。
     */
    public Dimension run() {
        return currentPlayer.run(board);
    }

    /**
     * プレイヤーが石を置けず、スキップが必要か判定する
     * @return スキップの場合は真 {@code true}、石を置ける場所がありスキップでない場合は偽 {@code false} を返す。
     */
    public Boolean isSkip() {
        if (board.canPutAll(currentPlayer.isDiscBlack())) {
            return false;
        } else {
            record.addAsSkip(turnCount, currentPlayer.isDiscBlack(), board.getBlackDiscNum(),
                    board.getWhiteDiscNum());
            return true;
        }
    }

    /**
     * 現在のプレイヤーの石をリバーシ盤に置く
     * @param target 石を置く座標
     * @return 対象の座標に石を置いた場合は真 {@code true}、ルールにより石を置けない場合は偽 {@code false} を返す。
     * @throws IllegalArgumentException 引数 {@code target} が {@code NULL} の場合、エラーを返す。
     */
    public Boolean put(Dimension target) throws IllegalArgumentException {
        // 引数の正常性確認
        if (target == null) {
            throw new IllegalArgumentException("変数 \"target\" が NULL です。");
        }

        // ボードに石を置く処理
        Boolean isPut = false;
        try {
            isPut = board.put(target, currentPlayer.isDiscBlack());
        } catch (IllegalArgumentException e) {
            // 石を置く処理で例外が発生した場合、異常終了する
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println("プログラムを異常終了します。 code: " + exitCode);
            System.exit(exitCode);
        }

        // 棋譜を記録する
        if (isPut) {
            record.add(turnCount, currentPlayer.isDiscBlack(), board.getBlackDiscNum(), board.getWhiteDiscNum(),
                    target.getString());
        }

        return isPut;
    }

    /**
     * 勝敗結果を判定する。
     * @return 結果を返す。勝敗がつかない場合は {@code Result.None} を返す。
     */
    public ResultType judge() {
        ResultType result;
        if (isGameFinish()) {
            if (board.getBlackDiscNum() == board.getWhiteDiscNum()) {
                result = ResultType.Drow;
            } else if (board.getBlackDiscNum() > board.getWhiteDiscNum()) {
                result = ResultType.Black;
            } else {
                result = ResultType.White;
            }
        } else {
            result = ResultType.None;
        }
        return result;
    }

    /**
     * ゲーム終了を判定する
     * @return ゲーム終了の場合は真 {@code true}, 続行の場合は偽 {@code false} を返す。
     */
    private Boolean isGameFinish() {
        // 盤上に空きがない場合
        if (board.getEmptyNum() <= 0) {
            record.setComment("全てのマスが埋まりました");
            return true;
        }

        // 片方のプレイヤーの石が0個になった場合
        if (board.getBlackDiscNum() <= 0) {
            record.setComment("先手・黒の石がなくなりました");
            return true;
        }
        if (board.getWhiteDiscNum() <= 0) {
            record.setComment("後手・白の石がなくなりました");
            return true;
        }

        // 両プレイヤーともに石を置く位置がなく、ともにスキップする場合
        if (board.canPutAll(true) == false && board.canPutAll(false) == false) {
            // 両者スキップする棋譜を追加する
            Boolean isPlayerBlack = currentPlayer.isDiscBlack();
            int blackDiscNum = board.getBlackDiscNum();
            int whiteDiscNum = board.getWhiteDiscNum();
            record.addAsSkip(++turnCount, isPlayerBlack, blackDiscNum, whiteDiscNum);
            record.addAsSkip(++turnCount, !isPlayerBlack, blackDiscNum, whiteDiscNum);
            record.setComment("両プレイヤーともにスキップが選択されました");
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
        if (currentPlayer.isDiscBlack()) {
            currentPlayer = playerWhite;
        } else {
            currentPlayer = playerBlack;
        }
    }
}
