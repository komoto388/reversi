package algorithm;

import reversi.Board;
import reversi.Dimension;

/**
 * ランダムに石を置くアルゴリズム
 */
public class RandomAlgorithm extends Algorithm {
    
    /**
     * ランダムアルゴリズムの初期化を行う
     * @param seed 乱数生成のseed値
     */
    public RandomAlgorithm(long seed) {
        super(seed);
    }

    /**
     * 設置可能なマスに対して、ランダムに石を置くアルゴリズム
     */
    @Override
    public Dimension run(Board board, Boolean playerIsBlack) {
        final Dimension boardSize = board.getSize();
        Evaluate evaluate = new Evaluate(boardSize);

        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                // 石を置けるますの場合は、1～100のランダム値を加える。
                Dimension target = new Dimension(i, j);
                if (board.countReversibleDisc(target, playerIsBlack) > 0) {
                    evaluate.add(target, random.nextInt(100) + 1);
                }
            }
        }
        return evaluate.getMaxPointDimension();
    }
}
