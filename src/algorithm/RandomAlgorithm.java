package algorithm;

import reversi.Board;
import reversi.Dimension;

/**
 * ランダムに石を置くアルゴリズム
 */
public class RandomAlgorithm extends Algorithm {

    /**
     * ランダムアルゴリズムの初期化を行う
     * @param isPlayerBlack 使用するプレイヤーの石の色
     * @param seed 乱数生成のseed値
     */
    public RandomAlgorithm(Boolean isPlayerBlack, long seed) {
        super(isPlayerBlack, seed);
    }

    /**
     * 設置可能なマスに対して、ランダムに石を置くアルゴリズム
     * @param board 現在のリバーシ盤の状態
     */
    @Override
    public Dimension run(Board board) {
        final Dimension boardSize = board.getSize();
        Evaluate evaluate = new Evaluate(boardSize);

        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                // 石を置けるますの場合は、1～100のランダム値を加える。
                Dimension target = new Dimension(i, j);
                if (board.countReversibleDisc(target, isPlayerBlack) > 0) {
                    evaluate.add(target, random.nextInt(100) + 1);
                }
            }
        }
        return evaluate.getMaxPointDimension();
    }
}
