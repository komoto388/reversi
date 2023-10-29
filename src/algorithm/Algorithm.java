package algorithm;

import reversi.Board;
import reversi.Dimension;

/**
 * アルゴリズムの処理を定義する抽象クラス
 * @author komoto
 */
public abstract class Algorithm {

    /**
     * 石を置く座標を決定する
     * @param reversi リバーシのゲーム情報
     * @param isBlack プレイヤーの石の色が黒かどうか
     * @return 決定した石を置く座標
     */
    public abstract Dimension run(Board board, Boolean isBlack);
}
