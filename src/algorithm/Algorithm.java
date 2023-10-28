package algorithm;

import reversi.Dimension;
import reversi.Reversi;

/**
 * アルゴリズムの処理を定義する抽象クラス
 * @author komoto
 */
public abstract class Algorithm {

    /**
     * 石を置く座標を決定する
     * @param reversi リバーシのゲーム情報
     * @return 決定した石を置く座標
     */
    public abstract Dimension run(Reversi reversi);
}
