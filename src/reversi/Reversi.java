package reversi;

/**
 * リバーシのゲームを定義・処理するクラス
 * @author komoto
 */
public class Reversi {
    /** リバーシ盤の状態を表す */
    private Disc[][] board;

    /** リバーシ盤のサイズ（マス） */
    private final Dimension boardSize = new Dimension(8, 8);

    /** 現在のプレイヤーが黒かどうかを表す。初手は黒 */
    private Boolean playerIsBlack = true;
    
    
    /** 経過したターン数 */
    private int turnCount = 1;

    /** 現在の黒石の数 */
    private int blackDiscNum = 0;

    /** 現在の白石の数 */
    private int whiteDiscNum = 0;
   
    /**
     * リバーシ盤の初期化を行う
     */
    public Reversi() {
        board = new Disc[boardSize.getRow()][boardSize.getColumn()];

        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                board[i][j] = new Disc();

                // 盤の中心に初期の石を設置する
                if ((i == boardSize.getRow() / 2 - 1 && j == boardSize.getRow() / 2 - 1)
                        || (i == boardSize.getColumn() / 2 && j == boardSize.getColumn() / 2)) {
                    board[i][j].put(false);
                    whiteDiscNum++;
                } else if ((i == boardSize.getRow() / 2 - 1 && j == boardSize.getColumn() / 2)
                        || (i == boardSize.getRow() / 2 && j == boardSize.getColumn() / 2 - 1)) {
                    board[i][j].put(true);
                    blackDiscNum++;
                }
            }
        }
    }
    
    /**
     * リバーシ盤の状態を返す
     * @return リバーシ盤の状態
     */
    public Disc[][] getBoard() {
        return board;
    }
    
    /**
     * リバーシ盤の大きさを返す
     * @return リバーシ盤の大きさ
     */
    public Dimension getBoardSize() {
        return boardSize;
    }
    
    /**
     * 現在の経過ターン数を返す
     * @return 現在の経過ターン数
     */
    public int getTurnCount() {
        return turnCount;
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
        return boardSize.getRow() * boardSize.getColumn() - blackDiscNum - whiteDiscNum;
    }
    
    /**
     * リバーシ盤をコマンドプロンプト上に表示する
     */
    public void showCui() {
        System.out.printf(" ");
        for (int j = 0; j < boardSize.getColumn(); j++) {
            System.out.printf(" %2d", j + 1);
        }
        System.out.printf("\n");

        for (int i = 0; i < boardSize.getRow(); i++) {
            System.out.printf("%2d", i + 1);
            for (int j = 0; j < boardSize.getColumn(); j++) {
                if (board[i][j].isBlack()) {
                    System.out.printf(" ◯");
                } else if (board[i][j].isWhite()) {
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
