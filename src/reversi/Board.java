package reversi;

/**
 * リバーシ盤の定義・処理をするクラス
 * @author komoto
 */
public class Board {
    /** 石の状態を表す値 */
    private enum DiscStatus {
        EMPTY, // 空（設置されていない） 
        BLACK, // 白
        WHITE, // 黒
    }

    /** リバーシ盤の状態を表す */
    private DiscStatus[][] board;

    /** リバーシ盤のサイズ（マス） */
    private final Dimension size = new Dimension(8, 8);

    /** 現在の黒石の数 */
    private int blackDiscNum;

    /** 現在の白石の数 */
    private int whiteDiscNum;
    
    public Board() {
        board = new DiscStatus[size.getRow()][size.getColumn()];
        blackDiscNum = 0;
        whiteDiscNum = 0;
        
        for (int i = 0; i < size.getRow(); i++) {
            for (int j = 0; j < size.getColumn(); j++) {
                // 盤の中心に初期の石を設置する
                if ((i == size.getRow() / 2 - 1 && j == size.getRow() / 2 - 1)
                        || (i == size.getColumn() / 2 && j == size.getColumn() / 2)) {
                    board[i][j] = DiscStatus.WHITE;
                    whiteDiscNum++;
                } else if ((i == size.getRow() / 2 - 1 && j == size.getColumn() / 2)
                        || (i == size.getRow() / 2 && j == size.getColumn() / 2 - 1)) {
                    board[i][j] = DiscStatus.BLACK;
                    blackDiscNum++;
                } else {
                    board[i][j] = DiscStatus.EMPTY;
                }
            }
        }
    }

    /**
     * リバーシ盤のサイズを返す
     * @return リバーシ盤のサイズ
     */
    public Dimension getSize() {
        return size;
    }
    
    /**
     * 現在盤上にある黒石の個数を返す
     * @return 現在の黒石の個数
     */
    public int getBlackDiscNum() {
        return blackDiscNum;
    }
    
    /**
     * 現在盤上にある白石の個数を返す
     * @return 現在の白石の個数
     */
    public int getWhiteDiscNum() {
        return whiteDiscNum;
    }

    /**
     * 現在盤上で石が置かれていない場所の数を返す
     * @return 石が置かれていない場所の数
     */
    public int getEmptyNum() {
        return size.getRow() * size.getColumn() - blackDiscNum - whiteDiscNum;
    }
    
    /**
     * 石が黒かどうかを返す
     * @param target 調べる石の座標
     * @return 黒であれば真 {@code true}、それ以外（白、空）であれば偽 {@code false} を返す。
     */
    public Boolean isBlack(Dimension target) {
        if (board[target.getRow()][target.getColumn()] == DiscStatus.BLACK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 石が白かどうかを返す
     * @param target 調べる石の座標
     * @return 白であれば真 {@code true}、それ以外（黒、空）であれば偽 {@code false} を返す。
     */
    public Boolean isWhite(Dimension target) {
        if (board[target.getRow()][target.getColumn()] == DiscStatus.WHITE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 石が置かれていないかどうかを返す
     * @param target 調べる石の座標
     * @return 未設置であれば真 {@code true}、設置済みであれば偽 {@code false} を返す。
     */
    public Boolean isEmpty(Dimension target) {
        if (board[target.getRow()][target.getColumn()] == DiscStatus.EMPTY) {
            return true;
        } else {
            return false;
        }
    }
    
    
    /**
     * 石を置き、状態を更新する
     * @param target 石を置く座標
     * @param isBlack 黒として置く場合は真 {@code true}、白として置く場合は偽 {@code false} とする。
     * @return 設置できる場合は真 {@code true}、既に設置されている石の場合は偽 {@code false} を返す。
     */
    public Boolean put(Dimension target, Boolean isBlack) {
        if(canPut(target, isBlack) == false) {
            return false;
        }

        if (isBlack) {
            board[target.getRow()][target.getColumn()] = DiscStatus.BLACK;
        } else {
            board[target.getRow()][target.getColumn()] = DiscStatus.WHITE;
        }

        return true;
    }
    
    // 対象の座標に石を置けるか確認する
    private Boolean canPut(Dimension target, Boolean isBlack) {
        // マスに石が既に置かれていない確認する
        if (board[target.getRow()][target.getColumn()] != DiscStatus.EMPTY) {
            return false;
        }

        return true;
    }
    
    
    
    
    
    
    /**
     * 石の白黒を反転させる
     * @param target 反転させる石の座標
     */
    private void reverse(Dimension target) {
        if (board[target.getRow()][target.getColumn()] == DiscStatus.BLACK) {
            board[target.getRow()][target.getColumn()] = DiscStatus.WHITE;
        } else {
            board[target.getRow()][target.getColumn()] = DiscStatus.BLACK;
        }
    }

    /**
     * リバーシ盤をコマンドプロンプト上に表示する
     */
    public void showCui() {
        System.out.printf(" ");
        for (int j = 0; j < size.getColumn(); j++) {
            System.out.printf(" %2d", j + 1);
        }
        System.out.printf("\n");

        for (int i = 0; i < size.getRow(); i++) {
            System.out.printf("%2d", i + 1);
            for (int j = 0; j < size.getColumn(); j++) {
                if (board[i][j] == DiscStatus.BLACK) {
                    System.out.printf(" ◯");
                } else if (board[i][j] == DiscStatus.WHITE) {
                    System.out.printf(" ●");
                } else {
                    System.out.printf(" ―");
                }
            }
            System.out.printf("\n");
        }

        System.out.printf("黒石◯: %2d\n", blackDiscNum);
        System.out.printf("白石●: %2d\n", whiteDiscNum);
    }  
}
