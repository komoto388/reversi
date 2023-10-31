package reversi;

import algorithm.Algorithm;
import algorithm.AlgorithmType;
import algorithm.RandomAlgorithm;

/**
 * プレイヤーを定義・処理するクラス
 * @author komoto
 */
public class Player {
    /** プレイヤーの石の色が黒かどうか */
    Boolean isBlack;

    /** プレイヤーが使用するアルゴリズムの種類 */
    AlgorithmType type;

    Algorithm algorithm;

    /**
     * プレイヤーの初期設定を行う。
     * 使用するアルゴリズムを決定する。
     * @param isBlack プレイヤーの石の色が黒かどうか
     * @param type 使用するアルゴリズムの種類
     */
    public Player(Boolean isBlack, AlgorithmType type) {
        this.isBlack = isBlack;
        this.type = type;

        switch (type) {
        case Manual: {
            algorithm = null;
            break;
        }
        case Random: {
            algorithm = new RandomAlgorithm();
            break;
        }
        default:
            throw new IllegalArgumentException("Unexpected value: " + type);
        }
    }

    /**
     * プレイヤーの使用するアルゴリズムが手動か返す
     * @return 使用アルゴリズムがマニュアルの場合は {@code true}, それ以外の場合は {@code false} を返す。
     */
    public Boolean isManual() {
        if (type == AlgorithmType.Manual) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 石を置く座標を決定する
     * @param reversi リバーシのゲーム情報
     * @param playerIsBlack プレイヤーの石の色が黒かどうか
     * @return 決定した石を置く座標
     * @throws IllegalAccessException アルゴリズムがマニュアルの場合に呼ばれた場合、エラーを返す
     */
    public Dimension run(Board board, Boolean playerIsBlack) throws IllegalAccessException {
        if (type != AlgorithmType.Manual) {
            return algorithm.run(board, playerIsBlack);
        } else {
            throw new IllegalAccessException("Unexpected Algorithm Type; Manual");
        }
    }
}