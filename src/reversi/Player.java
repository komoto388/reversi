package reversi;

import algorithm.Algorithm;
import algorithm.AlgorithmType;
import algorithm.Original01;
import algorithm.RandomAlgorithm;
import common.Global;

/**
 * プレイヤーを定義・処理するクラス
 * @author komoto
 */
class Player {

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
        // 引数の正常性確認
        try {
            if (isBlack == null) {
                throw new IllegalArgumentException("引数 \"isBlack\" の値が NULL です");
            }
            if (type == null) {
                throw new IllegalArgumentException("引数 \"reversi\" の値が NULL です");
            }
        } catch (IllegalArgumentException e) {
            int exitCode = Global.EXIT_FAILURE;
            e.printStackTrace();
            System.err.println("引数が想定されていない値のため、プログラムを異常終了します: 終了コード = " + exitCode);
            System.exit(exitCode);
        }

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
        case Original_01: {
            algorithm = new Original01();
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
