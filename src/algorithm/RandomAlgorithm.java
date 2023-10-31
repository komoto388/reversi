package algorithm;

import reversi.Board;
import reversi.Dimension;

/**
 * ランダムに石を置くアルゴリズム
 */
public class RandomAlgorithm extends Algorithm {
    @Override
    public Dimension run(Board board, Boolean playerIsBlack) {
        final Dimension boardSize = board.getSize();
        Evaluate evaluate = new Evaluate(boardSize, 0);

        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                // 石を置けるますの場合は、1～100のランダム値をあたえる。
                Dimension target = new Dimension(i, j);
                if (board.countReversibleDisc(target, playerIsBlack) > 0) {
                    evaluate.set(target, random.nextInt(100) + 1);
                    System.out.printf("設置可能な座標: %s, 評価値: %d\n", target.getString(), evaluate.getValue(target));
                }
            }
        }
        
        return evaluate.getMaxPointDimension();
    }
}
