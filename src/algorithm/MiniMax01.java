package algorithm;

import common.Global;
import reversi.Board;
import reversi.Dimension;
import reversi.Disc;

/**
 * Mini_Max法のアルゴリズムで石を置く位置を決める
 * @author komoto
 */
public class MiniMax01 extends Algorithm {
    /** 探索する深さ */
    private final int DEPTH = 4;

    /**
     * 初期化を行う
     * @param board 現在のリバーシ盤の状態
     * @param playerDisc プレイヤーが使用する石
     */
    public MiniMax01(Board board, Disc playerDisc) {
        super(board, playerDisc);
    }

    /**
     * 全てのマスに対して評価を行い、評価値が最大となる座標を算出する
     */
    @Override
    public Dimension run() {
        final Dimension boardSize = board.getSize();
        Evaluate evaluate = new Evaluate(boardSize);

        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                Dimension target = new Dimension(i, j);
                evaluate.set(target, MIN_POINT);

                if (board.canPut(target, playerDisc)) {
                    // 置ける場合、評価を行う
                    try {
                        int point = evaluateMax(DEPTH, board, target);
                        evaluate.add(target, point);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                        System.err.println("対象マスでの評価に失敗したため、評価値の加算はありません: " + target.getString());
                    }
                }
            }
        }

        return evaluate.getMaxPointDimension();
    }

    /**
     * 盤面にある自分と相手の石の個数に応じて、評価する（自分のターン）
     * @param depth 手を読む深さ
     * @param currnetBoard 現在のリバーシ盤の状態
     * @param target 石を置く予定の座標。この座標に石をおいた場合について評価する。
     * @return この盤面での評価値
     * @throws CloneNotSupportedException 評価用にリバーシ盤を複製するのに失敗した
     * @throws RuntimeException 石を設置できない座標が指定された
     */
    private int evaluateMax(int depth, Board currnetBoard, Dimension target)
            throws CloneNotSupportedException, RuntimeException {
        // 次の手の状態を表すリバーシ盤を作成する
        Board nextBoard = currnetBoard.clone();
        if (nextBoard.put(target, playerDisc) == false) {
            throw new RuntimeException("石を置けない座標が指定されました" + target.getString());
        }

        // 深さ0の場合、探索を終えて評価する
        if (depth == 0) {
            return calcPoint(nextBoard);
        }

        // 次の相手の手を評価する
        int maxPoint = MIN_POINT;
        for (int i = 0; i < nextBoard.getSize().getRow(); i++) {
            for (int j = 0; j < nextBoard.getSize().getColumn(); j++) {
                Dimension nextTarget = new Dimension(i, j);

                // 相手の石が置けるマスのみ評価する
                if (nextBoard.canPut(nextTarget, playerDisc.next())) {
                    int point = evaluateMini(depth - 1, nextBoard, nextTarget);

                    // 自分にとって良い手（自分の評価値が高い手）か判定する
                    if (point > maxPoint) {
                        maxPoint = point;
                    }
                }
            }
        }
        return maxPoint;
    }

    /**
     * 盤面にある自分と相手の石の個数に応じて、評価する（自分のターン）
     * @param depth 手を読む深さ
     * @param currnetBoard 現在のリバーシ盤の状態
     * @param target 石を置く予定の座標。この座標に石をおいた場合について評価する。
     * @return この盤面での評価値を返す
     * @throws CloneNotSupportedException 評価用にリバーシ盤を複製するのに失敗した
     * @throws RuntimeException 石を設置できない座標が指定された
     */
    private int evaluateMini(int depth, Board currnetBoard, Dimension target)
            throws CloneNotSupportedException, RuntimeException {

        // 次の手の状態を表すリバーシ盤を作成する
        Board nextBoard = currnetBoard.clone();
        if (nextBoard.put(target, playerDisc.next()) == false) {
            throw new RuntimeException("石を置けない座標が指定されました" + target.getString());
        }

        // 深さ0の場合、探索を終えて評価する
        if (depth == 0) {
            return calcPoint(nextBoard);
        }

        // 次の自分の手を評価する
        int minPoint = MAX_POINT;
        for (int i = 0; i < nextBoard.getSize().getRow(); i++) {
            for (int j = 0; j < nextBoard.getSize().getColumn(); j++) {
                Dimension nextTarget = new Dimension(i, j);

                // 自分の石が置けるマスのみ評価する
                if (nextBoard.canPut(nextTarget, playerDisc)) {
                    int point = evaluateMax(depth - 1, nextBoard, nextTarget);

                    // 相手にとって良い手（自分の評価値が低い手）か判定する
                    if (point < minPoint) {
                        minPoint = point;
                    }
                }
            }
        }
        return minPoint;
    }

    /**
     * 自分と相手の石の個数から、プレイヤー自身に対する盤面の評価値を算出する
     * @param currnetBoard 現在のリバーシ盤の状態
     * @return プレイヤー自身に対する盤面の評価値
     */
    private int calcPoint(Board currnetBoard) {
        // 現在の盤面での、自分の石と相手の石の個数で評価する
        int playerDiscNum, enemyDiscNum;

        if (playerDisc == Disc.BLACK) {
            playerDiscNum = currnetBoard.getDiscNum(Disc.BLACK);
            enemyDiscNum = currnetBoard.getDiscNum(Disc.WHITE);
        } else {
            playerDiscNum = currnetBoard.getDiscNum(Disc.WHITE);
            enemyDiscNum = currnetBoard.getDiscNum(Disc.BLACK);
        }

        int point = (playerDiscNum - enemyDiscNum) * 100;
        if (Global.IS_ADD_RANDOM) {
            point += random.nextInt(100);
        }

        return point;
    }
}
