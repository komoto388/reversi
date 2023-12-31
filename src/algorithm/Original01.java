package algorithm;

import common.Global;
import reversi.Board;
import reversi.Dimension;
import reversi.Disc;

/**
 * 四隅・端および反転できる石の個数に基づく評価を行うクラス
 * @author komoto
 */
public class Original01 extends Algorithm {

    /**
     * 評価値として使用する固定値
     */
    private enum EvaluatePoint {
        CORNER(2000), EDGE(500);

        private final int point;

        private EvaluatePoint(int point) {
            this.point = point;
        }

        int getPoint() {
            return point;
        }
    };

    /**
     * ランダムアルゴリズムの初期化を行う
     * @param board 現在のリバーシ盤の状態
     * @param playerDisc プレイヤーが使用する石
     */
    public Original01(Board board, Disc playerDisc) {
        super(board, playerDisc);
    }

    /**
     * 全てのマスに対して評価を行い、評価値が最大となる座標を算出する。
     */
    @Override
    public Dimension run() {
        final Dimension boardSize = board.getSize();
        Evaluate evaluate = new Evaluate(boardSize);

        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                Dimension target = new Dimension(i, j);
                int point = calcEvaluatePoint(board, target);
                evaluate.add(target, point);
            }
        }
        return evaluate.getMaxPointDimension();
    }

    /**
     * マスに対して評価値を求める。
     * @param board リバーシ盤の状態
     * @param playerIsBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @param target 対象の座標
     * @return 算出した評価点
     */
    private int calcEvaluatePoint(Board board, Dimension target) {
        int point = 0;

        // 対称座標に置いた時、反転できる相手の石の個数
        int countReversibleDisc = board.countReversibleDisc(target, playerDisc);

        // 石を置けないマスは評価値を最小にする
        if (countReversibleDisc == 0) {
            return MIN_POINT;
        }

        // 石を反転できる個数を元に評価する。返せる個数が多いほど評価を高くする。
        point += countReversibleDisc * 100;

        // 四隅・端の評価値を加算する
        point += getEdgePoint(target, board.getSize());

        // 1～99のランダム値を加算する
        if (Global.IS_ADD_RANDOM) {
            point += random.nextInt(100);
        }

        return point;
    }

    /**
     * 対象の座標に対して、角または端の評価値を取得する。
     * @param target 対象の座標
     * @return 角、端に応じた評価点を返す。端でなかった場合は {@code 0} を返す。
     */
    private int getEdgePoint(Dimension target, Dimension boardSize) {
        final int row = target.getRow();
        final int column = target.getColumn();
        final int maxRow = boardSize.getRow() - 1;
        final int maxColumn = boardSize.getColumn() - 1;

        // 四隅の場合
        if ((row == 0 || row == maxRow) && (column == 0 || column == maxColumn)) {
            return EvaluatePoint.CORNER.getPoint();
        }

        // 四隅以外の端の場合
        if (row == 0 || row == maxRow || column == 0 || column == maxColumn) {
            return EvaluatePoint.EDGE.getPoint();
        }

        return 0;
    }

}
