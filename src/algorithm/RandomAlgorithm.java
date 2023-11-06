package algorithm;

import reversi.Board;
import reversi.Dimension;

/**
 * ランダムに石を置くアルゴリズム
 */
public class RandomAlgorithm extends Algorithm {

    /**
     * ランダムアルゴリズムの初期化を行う
     * @param board 現在のリバーシ盤の状態
     * @param isPlayerBlack 使用するプレイヤーの石の色
     */
    public RandomAlgorithm(Board board, Boolean isPlayerBlack) {
        super(board, isPlayerBlack);
    }

    /**
     * 設置可能なマスに対して、ランダムに石を置くアルゴリズム
     */
    @Override
    public Dimension run() {
        final Dimension boardSize = board.getSize();
        Evaluate evaluate = new Evaluate(boardSize);

        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                // 石を置けるますの場合は、1～99のランダム値を加える。
                Dimension target = new Dimension(i, j);
                if (board.countReversibleDisc(target, isPlayerBlack) > 0) {
                    int point = random.nextInt(1000);
                    evaluate.add(target, point);
                    
                    System.out.println("Player: " + isPlayerBlack + ", rand: " + point);
                }
            }
        }
        return evaluate.getMaxPointDimension();
    }
}
