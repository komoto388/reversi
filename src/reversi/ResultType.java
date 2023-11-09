package reversi;

/**
 * ゲームの勝敗結果を表す
 * @author komoto
 */
public enum ResultType {
    /** 勝敗がついていない */
    NONE,
    /** 引き分け */
    DRAW,
    /** 黒の勝ち */
    BLACK,
    /** 白の勝ち */
    WHITE,
}
