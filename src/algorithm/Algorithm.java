package algorithm;

import java.util.Random;

import common.Global;
import reversi.Board;
import reversi.Dimension;

/**
 * アルゴリズムの処理を定義する抽象クラス
 * @author komoto
 */
public abstract class Algorithm {
    /** 評価関数のデフォルト値を表す定数 */
    private final int DEFAULT_POINT = 0;
    
    /** 評価関数の最小値を表す定数 */
    protected final int MIN_POINT = Integer.MIN_VALUE;

    /** 評価関数の最大値を表す定数 */
    protected final int MAX_POINT = Integer.MAX_VALUE;

    /** ランダム値を扱うインスタンス */
    protected Random random;

    /**
     * ランダムシード値を設定する
     */
    public Algorithm() {
        random = new Random(System.currentTimeMillis());
    }

    /**
     * 石を置く座標を決定する
     * @param board リバーシ盤の状態
     * @param playerIsBlack プレイヤーの石の色が黒かどうか
     * @return 決定した石を置く座標
     */
    public abstract Dimension run(Board board, Boolean playerIsBlack);

    /**
     * 評価関数を定義・処理するクラス
     */
    protected class Evaluate {
        /** 各マスの評価値を格納する配列 */
        private int[][] evaluateList;

        /** 現時点での評価値が最大であるマスの座標 */
        private Dimension maxPointDim;

        /** 現時点での最大の評価値 */
        private int maxPoint;

        /**
         * 評価関数を初期化する
         * @param boardSize リバーシ盤の大きさ
         */
        public Evaluate(Dimension boardSize) {
            evaluateList = new int[boardSize.getRow()][boardSize.getColumn()];
            maxPointDim = null;
            maxPoint = MIN_POINT;

            for (int i = 0; i < boardSize.getRow(); i++) {
                for (int j = 0; j < boardSize.getColumn(); j++) {
                    evaluateList[i][j] = DEFAULT_POINT;
                }
            }
        }

        /**
         * 評価値が最大の座標を返す
         * @return 評価値が最大の座標
         */
        public Dimension getMaxPointDimension() {
            try {
                if (maxPointDim == null) {
                    throw new NullPointerException("評価値が最大の座標がありません。評価が1度も実行されていない可能性があります。");
                } 
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return maxPointDim;
        }

        /**
         * 対象座標の評価値を返す
         * @return 対象座標の評価値
         */
        public int getValue(Dimension target) {
            return evaluateList[target.getRow()][target.getColumn()];
        }

        /**
         * 対象座標の評価値に値を上書きする
         * @param target 対象の座標
         * @param value 設定する評価値
         */
        public void set(Dimension target, int value) {
            evaluateList[target.getRow()][target.getColumn()] = value;
            updateMaxPoint(target);
        }

        /**
         * 対象座標の評価値に値を加算する
         * @param target 対象の座標
         * @param value 加算する値
         */
        public void add(Dimension target, int value) {
            evaluateList[target.getRow()][target.getColumn()] += value;
            updateMaxPoint(target);
        }

        /**
         * 対称座標の値と最大評価値を比較し、最大評価値を更新する
         * @param target 評価値を更新した対象の座標
         */
        private void updateMaxPoint(Dimension target) {
            if (evaluateList[target.getRow()][target.getColumn()] > maxPoint) {
                maxPointDim = target;
                maxPoint = evaluateList[target.getRow()][target.getColumn()];
            }
        }
    }
}
