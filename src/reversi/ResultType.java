package reversi;

/**
 * ゲームの勝敗結果を表す
 * @author komoto
 */
public enum ResultType {
    /** 勝敗がついていない */
    None,
    /** 引き分け */
    Drow,
    /** 黒の勝ち */
    Black,
    /** 白の勝ち */
    White,
}
