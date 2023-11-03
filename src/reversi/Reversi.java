package reversi;

import algorithm.AlgorithmType;
import common.Convert;
import common.Global;

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

    /** 現在のプレイヤーが黒かどうかを表す */
    private Boolean playerIsBlack;

    /** 経過したターン数 */
    private int turnCount;

    /** 棋譜の記録を行うインスタンス */
    private RecordList recordList;

    /**
     * リバーシ盤の初期化を行う
     * @param typeBlack 先手・黒が使用するアルゴリズム
     * @param typeWhite 後手・白が使用するアルゴリズム
     */
    public Reversi(AlgorithmType typeBlack, AlgorithmType typeWhite) {
        // 引数の正常性確認
        // アルゴリズムの指定が空の場合は、エラーを出力してデフォルトのアルゴリズムを設定する。
        try {
            if (typeBlack == null) {
                throw new IllegalArgumentException("先手・黒のアルゴリズム \"typeBlack\" が NULL です。");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            AlgorithmType[] types = AlgorithmType.values();
            typeBlack = types[Global.DEFAULT_ALGORITHM];
            System.err.printf("先手・黒のアルゴリズムをデフォルトの %s に設定します。", typeBlack);
        }

        try {
            if (typeWhite == null) {
                throw new IllegalArgumentException("後手・白のアルゴリズム \"typeWhite\" が NULL です。");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            AlgorithmType[] types = AlgorithmType.values();
            typeWhite = types[Global.DEFAULT_ALGORITHM];
            System.err.printf("後手・白のアルゴリズムをデフォルトの %s に設定します。", typeWhite);
        }

        // リバーシの初期化を行う
        board = new Board(Global.BOARD_WIDTH, Global.BOARD_HEIGHT);
        playerBlack = new Player(true, typeBlack);
        playerWhite = new Player(false, typeWhite);
        turnCount = 1;
        playerIsBlack = true;
        recordList = new RecordList();
    }

    /**
     * リバーシ盤の状態を返す
     * @return リバーシ盤の状態
     */
    public Board getBoard() {
        return board;
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
        return playerIsBlack;
    }

    /**
     * 記録された棋譜のインスタンスを返す
     * @return 記録された棋譜のインスタンス
     */
    public RecordList getRecordList() {
        return recordList;
    }

    /**
     * 現在のプレイヤーが手動かどうかを返す
     * @return 手動操作のプレイヤーである場合は真 {@code true}、自動処理のプレイヤーの場合は偽 {@code false} を返す。
     */
    public Boolean isCurrentPlayerManual() {
        if ((playerIsBlack && playerBlack.isManual()) ||
                (playerIsBlack == false && playerWhite.isManual())) {
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
        Dimension target = null;
        Player currentPlayer;

        if (playerIsBlack) {
            currentPlayer = playerBlack;
        } else {
            currentPlayer = playerWhite;
        }

        try {
            target = currentPlayer.run(board, playerIsBlack);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            target = null;
        }

        return target;
    }

    /**
     * プレイヤーが石を置けず、スキップが必要か判定する
     * @return スキップの場合は真 {@code true}、石を置ける場所がありスキップでない場合は偽 {@code false} を返す。
     */
    public Boolean isSkip() {
        if (board.canPutAll(playerIsBlack)) {
            return false;
        } else {
            recordList.addSkip(turnCount, playerIsBlack, board.getBlackDiscNum(), board.getWhiteDiscNum());
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
            isPut = board.put(target, playerIsBlack);
        } catch (IllegalArgumentException e) {
            // 石を置く処理で例外が発生した場合、異常終了する
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println("プログラムを異常終了します。 code: " + exitCode);
            System.exit(exitCode);
        }

        // 棋譜を記録する
        if (isPut) {
            recordList.add(turnCount, playerIsBlack, board.getBlackDiscNum(), board.getWhiteDiscNum(),
                    target.getString());
        }

        return isPut;
    }

    /**
     * 勝敗結果を判定する。
     * @return 結果を返す。勝敗がつかない場合は {@code Result.None} を返す。
     */
    public ResultType judge() {
        if (board.getEmptyNum() > 0) {
            // 盤上に空きがあっても片方の石が全てなくなった場合は勝敗をつける。
            if (board.getWhiteDiscNum() <= 0 && board.getBlackDiscNum() > 0) {
                return ResultType.Black;
            }
            if (board.getBlackDiscNum() <= 0 && board.getWhiteDiscNum() > 0) {
                return ResultType.White;
            }
        } else {
            // 盤上に空きがない場合は石の多さで勝敗をつける。
            if (board.getBlackDiscNum() == board.getWhiteDiscNum()) {
                return ResultType.Drow;
            } else if (board.getBlackDiscNum() > board.getWhiteDiscNum()) {
                return ResultType.Black;
            } else {
                return ResultType.White;
            }
        }
        return ResultType.None;
    }

    /**
     * 次の手番に進める
     */
    public void next() {
        turnCount++;

        // 次に打つプレイヤーを入れ替える
        if (playerIsBlack) {
            playerIsBlack = false;
        } else {
            playerIsBlack = true;
        }
    }

    /**
     * 現在の手番とリバーシ盤の状態を表示する
     */
    public void showBoardCui() {
        System.out.printf("(%d手目)\n", turnCount);
        board.showCui();
        System.out.printf("【%s】のターンです。\n", Convert.getPlayerColor(playerIsBlack));
    }
}
