package reversi;

import java.rmi.UnexpectedException;

import algorithm.Algorithm;
import algorithm.AlgorithmType;
import algorithm.Original01;
import algorithm.RandomAlgorithm;
import common.Global;

/**
 * プレイヤーを定義・処理するクラス
 * @author komoto
 */
public class Player {

    /**
     * 使用する石の色を表す
     */
    private enum DiscColor {
        BLACK, WHITE
    }

    /** プレイヤーの名前 */
    private String name;

    /** プレイヤーが使用する石の色を表す */
    private DiscColor discColor;

    /** プレイヤーが使用するアルゴリズムの種類 */
    private AlgorithmType algorithmType;

    /** プレイヤーが使用するアルゴリズムのインスタンス */
    private Algorithm algorithm;

    /**
     * プレイヤーの初期設定を行う。
     * 使用するアルゴリズムを決定する。
     * @param name プレイヤーの名前
     * @param isBlack プレイヤーの石の色が黒かどうか
     * @param type 使用するアルゴリズムの種類
     * @param seed プレイヤーがアルゴリズムを使用する際に使用する乱数のseed値
     */
    public Player(String name, Boolean isBlack, AlgorithmType type, long seed) {
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

        if (isBlack) {
            this.discColor = DiscColor.BLACK;
        } else {
            this.discColor = DiscColor.WHITE;
        }
        this.name = name;
        this.algorithmType = type;

        switch (type) {
        case Manual: {
            algorithm = null;
            break;
        }
        case Random: {
            algorithm = new RandomAlgorithm(seed);
            break;
        }
        case Original_01: {
            algorithm = new Original01(seed);
            break;
        }
        default:
            throw new IllegalArgumentException("Unexpected value: " + type);
        }
    }

    /**
     * プレイヤーの名前を取得する
     * @return プレイヤーの名前の文字列
     */
    public String getName() {
        return name;
    }

    /**
     * プレイヤーの使用するアルゴリズムタイプを取得する
     * @return プレイヤーの使用するアルゴリズムタイプ
     */
    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    /**
     * プレイヤーの石の色が黒かどうか
     * @return 使用する石の色が黒の場合は真 {@code true}, 白の場合は偽 {@code false} を返す。
     */
    public Boolean isDiscBlack() {
        if (discColor == DiscColor.BLACK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * プレイヤーの使用するアルゴリズムが手動か返す
     * @return 使用アルゴリズムがマニュアルの場合は真 {@code true}, それ以外の場合は偽 {@code false} を返す。
     */
    public Boolean isManual() {
        if (algorithmType == AlgorithmType.Manual) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 石を置く座標を決定する
     * @param board リバーシ盤の情報
     * @return 決定した石を置く座標を返す。例外などにより決定できなかった場合は{@code NULL} を返す。
     * @throws UnexpectedException 手動アルゴリズムでの実行は想定されていない
     */
    public Dimension run(Board board) throws UnexpectedException {
        if (algorithmType != AlgorithmType.Manual) {
            return algorithm.run(board, isDiscBlack());
        } else {
            throw new UnexpectedException("手動アルゴリズムでの動作は想定されていません: " + algorithmType);
        }
    }
}
