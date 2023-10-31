package reversi;

import algorithm.AlgorithmType;

/**
 * プレイヤーを定義・処理するクラス
 * @author komoto
 */
public class Player {
    /** プレイヤーの石の色が黒かどうか */
    Boolean isBlack;

    /** プレイヤーが使用するアルゴリズムの種類 */
    AlgorithmType type;

    /**
     * プレイヤーの初期設定を行う。
     * 使用するアルゴリズムを決定する。
     * @param isBlack プレイヤーの石の色が黒かどうか
     * @param type 使用するアルゴリズムの種類
     */
    public Player(Boolean isBlack, AlgorithmType type) {
        this.isBlack = isBlack;
        this.type = type;
    }

    /**
     * プレイヤーが使用するアルゴリズムの種類を返す
     * @return 使用するアルゴリズムの種類
     */
    public AlgorithmType getType() {
        return type;
    }
}
