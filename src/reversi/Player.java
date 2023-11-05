package reversi;

import java.rmi.UnexpectedException;

import algorithm.Algorithm;
import algorithm.AlgorithmType;
import algorithm.Original01;
import algorithm.Original02;
import algorithm.RandomAlgorithm;
import common.Global;

/**
 * プレイヤーを定義・処理するクラス
 * @author komoto
 */
public class Player {

    /** プレイヤーの名前 */
    private String name;

    /** プレイヤーの石が黒かどうか */
    private Boolean isPlayerBlack;

    /** プレイヤーが使用するアルゴリズムの種類 */
    private AlgorithmType algorithmType;

    /**
     * プレイヤーの初期設定を行う。
     * 使用するアルゴリズムを決定する。
     * @param name プレイヤーの名前
     * @param isPlayerBlack プレイヤーの石の色が黒かどうか
     * @param type 使用するアルゴリズムの種類
     */

    public Player(String name, Boolean isPlayerBlack, AlgorithmType type) {
        // 引数の正常性確認
        try {
            if (isPlayerBlack == null) {
                throw new IllegalArgumentException("引数 \"isPlayerBlack\" の値が NULL です");
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

        // フィールドの初期化
        this.name = name;
        this.algorithmType = type;
        this.isPlayerBlack = isPlayerBlack;
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
        if (isPlayerBlack) {
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
     * @param board リバーシ盤の状態
     * @return 決定した石を置く座標を返す。例外などにより決定できなかった場合は{@code NULL} を返す。
     */
    public Dimension run(Board board) {
        Algorithm algorithm = null;

        // アルゴリズム種別でアルゴリズムを生成する
        try {
            algorithm = generateAlgorithm(board);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("例外発生のため、「ランダムアルゴリズム」を使用します");
            algorithm = new RandomAlgorithm(board, isPlayerBlack);
        }

        // アルゴリズムに基づいて石を置く座標を求める
        return algorithm.run();
    }

    /**
     * アルゴリズムの種類から、アルゴリズムのインスタンスを生成する
     * @param board リバーシ盤の状態
     * @return 生成したアルゴリズムのインスタンス
     * @throws UnexpectedException マニュアルなど、アルゴリズムの種類の値が期待されていない値である
     */
    private Algorithm generateAlgorithm(Board board) throws UnexpectedException {
        Algorithm algorithm;

        switch (algorithmType) {
        case Random: {
            algorithm = new RandomAlgorithm(board, isPlayerBlack);
            break;
        }
        case Original_01: {
            algorithm = new Original01(board, isPlayerBlack);
            break;
        }
        case Original_02: {
            algorithm = new Original02(board, isPlayerBlack);
            break;
        }
        case Manual: {
            throw new UnexpectedException("このメソッドは手動アルゴリズム時の実行は想定されていません: " + algorithmType);
        }
        default:
            throw new UnexpectedException("Unexpected value: " + algorithmType);
        }

        return algorithm;
    }
}
