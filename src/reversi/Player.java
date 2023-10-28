package reversi;

import algorithm.Algorithm;
import algorithm.AlgorithmType;

/**
 * プレイヤーを定義・処理するクラス
 * @author komoto
 */
public class Player {
    /** プレイヤーが使用するアルゴリズムの種類 */
    Algorithm algorithm = null;

    /**
     * プレイヤーの初期設定を行う。
     * 使用するアルゴリズムを決定する。
     * @param type 使用するアルゴリズムの種類
     */
    public Player(AlgorithmType type) {
        switch (type) {
        case CuiManual: {
            break;
        }
        case GuiManual: {
            break;
        }
        case Random: {
            break;
        }
        default:
            throw new IllegalArgumentException("Unexpected value: " + type);
        }
    }
    
    /**
     * 石を置く座標を決定する
     * @param reversi リバーシのゲーム情報
     * @return 決定した石を置く座標
     */
    public Dimension play(Reversi reversi) {
        return algorithm.run(reversi);
    }
}
