package reversi;

import common.Convert;

/**
 * 行・列の値を定義するクラス
 * @author komoto
 */
public class Dimension {
    /** 行 */
    private int row;
    
    /** 列 */
    private int column;

    /**
     * 行と列の値を設定する
     * @param row 行の値
     * @param column 列の値
     */
    public Dimension(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * 行の値を返す
     * @return 行の値
     */
    public int getRow() {
        return row;
    }
    
    /**
     * 列の値を返す
     * @return 列の値
     */
    public int getColumn() {
        return column;
    }
    
    /**
     * 座標の文字列を(x,y)表記で返す
     * @return (x,y)表記の座標の文字列
     */
    public String getString() {
        return String.format("%c%d", Convert.convertIntToChar(column), row + 1);
    }
}
