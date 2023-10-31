package algorithm;

import java.util.Random;

import reversi.Board;
import reversi.Dimension;

/**
 * アルゴリズムの処理を定義する抽象クラス
 * @author komoto
 */
public abstract class Algorithm {
    /** ランダム値を扱うインスタンス */
    Random random;

    /**
     * ランダムシード値を設定する
     */
    public Algorithm() {
        random = new Random(System.currentTimeMillis());
    }
    
    /**
     * 石を置く座標を決定する
     * @param reversi リバーシのゲーム情報
     * @param playerIsBlack プレイヤーの石の色が黒かどうか
     * @return 決定した石を置く座標
     */
    public abstract Dimension run(Board board, Boolean playerIsBlack);

    /**
     * 評価関数を定義・処理するクラス
     */
    protected class Evaluate {
        int[][] evaluateList;
        Dimension maxPointDim = null;
        int maxValue;

        /**
         * 評価関数を初期化する
         * @param boardSize リバーシ盤の大きさ
         * @param defaultValue 評価の初期値
         */
        public Evaluate(Dimension boardSize, int defaultValue) {
            evaluateList = new int[boardSize.getRow()][boardSize.getColumn()];
            maxValue = defaultValue - 1;

            for (int i = 0; i < boardSize.getRow(); i++) {
                for (int j = 0; j < boardSize.getColumn(); j++) {
                    evaluateList[i][j] = defaultValue;
                }
            }
        }

        /**
         * 評価値が最大の座標を返す
         * @return 評価値が最大の座標
         */
        public Dimension getMaxPointDimension() {
            if (maxPointDim != null) {
                return maxPointDim;
            } else {
                throw new NullPointerException("値が存在しません。評価が未実施の可能性があります。");
            }
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
            if (evaluateList[target.getRow()][target.getColumn()] > maxValue) {
                maxPointDim = target;
                maxValue = evaluateList[target.getRow()][target.getColumn()];
            }
        }
    }
}
