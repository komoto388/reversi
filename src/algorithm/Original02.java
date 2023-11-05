package algorithm;

import reversi.Board;
import reversi.Dimension;

public class Original02 extends Algorithm {
    /** 探索する深さ */
    private final int DEPTH = 5;

    /**
     * 初期化を行う
     * @param isPlayerBlack 使用するプレイヤーの石の色
     * @param seed 乱数生成のseed値
     */
    public Original02(Boolean isPlayerBlack, long seed) {
        super(isPlayerBlack, seed);
    }

    /**
     * 全てのマスに対して評価を行い、評価値が最大となる座標を算出する。
     * @param board リバーシ盤の状態
     */
    @Override
    public Dimension run(Board board) {
        final Dimension boardSize = board.getSize();
        Evaluate evaluate = new Evaluate(boardSize);

        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                Dimension target = new Dimension(i, j);
                try {
                    int point = 0;
                    point = evaluate(DEPTH, board, true, target);
                    point += random.nextInt(100);
                    evaluate.add(target, point);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    System.err.println("対象マスでの評価に失敗したため、評価値の加算はありません: " + target.getString());
                }
            }
        }

        return evaluate.getMaxPointDimension();
    }

    /**
     * 盤面にある自分と相手の石の個数に応じて、評価する
     * @param depth 手を読む深さ。0の場合は現在の盤面のみで評価する
     * @param board 現在のリバーシ盤の状態
     * @param isMe 評価を行う対象が自分の場合は {@code ture}, 相手の場合は {@code false}
     * @param target 石を置く予定の座標。この座標に石をおいた場合について評価する。
     * @return この盤面での評価値を返す
     * @throws CloneNotSupportedException 評価用にリバーシ盤を複製するのに失敗した
     */
    private int evaluate(int depth, Board board, Boolean isMe, Dimension target)
            throws CloneNotSupportedException {
        final Dimension boardSize = board.getSize();
        int point = 0;

        // 評価をするにあたり、この盤面でプレイしているプレイヤーを調べる
        Boolean currentPlayer;
        if (isMe) {
            currentPlayer = isPlayerBlack;
        } else {
            currentPlayer = !isPlayerBlack;
        }

        // 次の手の状態を表すリバーシ盤を作成する
        Board nextBoard = board.clone();
        if (nextBoard.put(target, currentPlayer) == false) {
            // 探索の深さに関わらず、石を置けない座標の場合は下限値(MIN_POINT)を返す
            return MIN_POINT;
        }

        // 次の手に進んだ評価を行う
        if (depth > 0) {
            for (int i = 0; i < boardSize.getRow(); i++) {
                for (int j = 0; j < boardSize.getColumn(); j++) {
                    int pointTmp = evaluate(depth - 1, nextBoard, !isMe, new Dimension(i, j));
                    if (pointTmp > point) {
                        point = pointTmp;
                    }
                }
            }
        }

        // 現在の盤面での評価値を加算する
        if (isMe) {
            point += calcPoint(nextBoard, currentPlayer);
        } else {
            point -= calcPoint(nextBoard, currentPlayer);
        }

        return point;
    }

    /**
     * 自分と相手の石の個数から、評価値を算出する
     * @param board 現在のリバーシ盤の状態
     * @param currentPlayer 評価する盤面をプレイしているプレイヤー
     * @return 盤面の評価値
     */
    private int calcPoint(Board board, Boolean currentPlayer) {
        // 現在の盤面での、自分の石と相手の石の個数で評価する
        int playerDiscNum, enemyDiscNum;

        if (currentPlayer) {
            playerDiscNum = board.getBlackDiscNum();
            enemyDiscNum = board.getWhiteDiscNum();
        } else {
            playerDiscNum = board.getWhiteDiscNum();
            enemyDiscNum = board.getBlackDiscNum();
        }

        return ((playerDiscNum - enemyDiscNum) * 100);
    }
}
