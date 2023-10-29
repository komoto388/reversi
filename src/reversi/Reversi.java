package reversi;

/**
 * リバーシのゲームを定義・処理するクラス
 * @author komoto
 */
public class Reversi {
    private Board board;

    /** 現在のプレイヤーが黒かどうかを表す。*/
    private Boolean playerIsBlack;
    
    /** 経過したターン数 */
    private int turnCount;
   
    /**
     * リバーシ盤の初期化を行う
     */
    public Reversi() {
        board = new Board();
        playerIsBlack = true;
        turnCount = 1;
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
}
