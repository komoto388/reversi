package reversi;

/**
 * リバーシの石を定義するクラス
 * @author komoto
 */ 
public class Disc {
    /** 石の状態を表す値 */
    private enum DiscStatus {
        EMPTY, // 空（設置されていない） 
        BLACK, // 白
        WHITE, // 黒
    }

    /** 現在の石の状態 */
    private DiscStatus status;

    /**
     * 石の状態を初期状態（空）として設定する。
     */
    public Disc() {
        this.status = DiscStatus.EMPTY;
    }

    /**
     * 石が黒かどうかを返す
     * @return 黒であれば真 {@code true}、それ以外（白、空）であれば偽 {@code false} を返す。
     */
    public Boolean isBlack() {
        if (this.status == DiscStatus.BLACK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 石が白かどうかを返す
     * @return 白であれば真 {@code true}、それ以外（黒、空）であれば偽 {@code false} を返す。
     */
    public Boolean isWhite() {
        if (this.status == DiscStatus.WHITE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 石が置かれていないかどうかを返す
     * @return 未設置であれば真 {@code true}、設置済みであれば偽 {@code false} を返す。
     */
    public Boolean isEmpty() {
        if (this.status == DiscStatus.EMPTY) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 石を置き、状態を更新する
     * @param isBlack 黒として置く場合は真 {@code true}、白として置く場合は偽 {@code false} とする。
     * @return 設置できる場合は真 {@code true}、既に設置されている石の場合は偽 {@code false} を返す。
     */
    public Boolean put(Boolean isBlack) {
        if (status != DiscStatus.EMPTY) {
            return false;
        }

        if (isBlack) {
            status = DiscStatus.BLACK;
        } else {
            status = DiscStatus.WHITE;
        }

        return true;

    }

    /**
     * 石の白黒を反転させる
     */
    public void reverse() {
        if (status == DiscStatus.BLACK) {
            status = DiscStatus.WHITE;
        } else {
            status = DiscStatus.BLACK;
        }
    }
}
