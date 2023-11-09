package reversi;

/**
 * リバーシ盤の定義・処理をするクラス
 * @author komoto
 */
public class Board implements Cloneable {

    /** 石の状態を表す値 */
    private enum DiscStatus {
        EMPTY, // 空（設置されていない） 
        BLACK, // 白
        WHITE, // 黒
    }

    /** リバーシ盤の状態を表す */
    private DiscStatus[][] board;

    /** リバーシ盤のサイズ（マス） */
    private Dimension boardSize;

    /** 現在の黒石の数 */
    private int blackDiscNum;

    /** 現在の白石の数 */
    private int whiteDiscNum;

    /**
     * リバーシ盤の初期化を行う
     * @param boardWidth リバーシ盤の幅（マス）
     * @param boardHeight リバーシ盤の高さ（マス）
     * @throws IllegalArgumentException いずれかの引数が {@code 0} 以下の場合、エラーとする。
     */
    public Board(int boardWidth, int boardHeight) throws IllegalArgumentException {
        // 引数の正常性確認
        if (boardWidth <= 0) {
            throw new IllegalArgumentException("リバーシ盤の幅の値は0より大きい値を指定してください: " + boardWidth);
        }
        if (boardHeight <= 0) {
            throw new IllegalArgumentException("リバーシ盤の高さの値は0より大きい値を指定してください: " + boardHeight);
        }

        // フィールドの初期化
        this.board = new DiscStatus[boardHeight][boardWidth];
        this.boardSize = new Dimension(boardHeight, boardWidth);
        this.blackDiscNum = 0;
        this.whiteDiscNum = 0;

        // リバーシ盤の生成・初期化
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                // 盤の中心に初期の石を設置する
                if ((i == boardHeight / 2 - 1 && j == boardHeight / 2 - 1)
                        || (i == boardWidth / 2 && j == boardWidth / 2)) {
                    board[i][j] = DiscStatus.WHITE;
                    whiteDiscNum++;
                } else if ((i == boardHeight / 2 - 1 && j == boardWidth / 2)
                        || (i == boardHeight / 2 && j == boardWidth / 2 - 1)) {
                    board[i][j] = DiscStatus.BLACK;
                    blackDiscNum++;
                } else {
                    board[i][j] = DiscStatus.EMPTY;
                }
            }
        }
    }

    /**
     * インスタンスを複製する
     */
    @Override
    public Board clone() throws CloneNotSupportedException {
        Board cloneBoard = (Board) super.clone();
        cloneBoard.boardSize = boardSize.clone();

        // 二次元配列 board[][] の複製
        cloneBoard.board = board.clone();
        for (int i = 0; i < boardSize.getRow(); i++) {
            cloneBoard.board[i] = board[i].clone();
        }

        return cloneBoard;
    }

    /**
     * リバーシ盤のサイズを取得する
     * @return リバーシ盤のサイズ
     */
    public Dimension getSize() {
        return boardSize;
    }

    /**
     * 現在盤上にあるプレイヤーの石の個数を取得する
     * @param isPlayerBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 指定したプレイヤーの石の個数
     */
    public int getDiscNum(Boolean isPlayerBlack) {
        try {
            if (isPlayerBlack == null) {
                throw new NullPointerException("引数 \" isPlayerBlack \" の値が NULL です");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("戻り値を 0 として処理します。");
            return 0;
        }

        if (isPlayerBlack) {
            return blackDiscNum;
        } else {
            return whiteDiscNum;
        }
    }

    /**
     * 現在盤上で石が置かれていない場所の数を取得する
     * @return 石が置かれていない場所の数
     */
    public int getEmptyDiscNum() {
        return boardSize.getRow() * boardSize.getColumn() - blackDiscNum - whiteDiscNum;
    }

    /**
     * 対象のマスに黒の石があるかの真偽値を取得する
     * @param target 調べる石の座標
     * @return 黒であれば真 {@code true}、それ以外（白、空）であれば偽 {@code false}
     */
    public Boolean isDiscBlack(Dimension target) {
        if (board[target.getRow()][target.getColumn()] == DiscStatus.BLACK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 対象のマスに白の石があるかの真偽値を取得する
     * @param target 調べる石の座標
     * @return 白であれば真 {@code true}、それ以外（黒、空）であれば偽 {@code false}
     */
    public Boolean isDiscWhite(Dimension target) {
        if (board[target.getRow()][target.getColumn()] == DiscStatus.WHITE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 対象のマスが空（石が置いていない）であるか真偽値を取得する
     * @param target 調べる石の座標
     * @return 空である場合は真 {@code true}、空でない場合は偽 {@code false}
     */
    public Boolean isDiscEmpty(Dimension target) {
        if (board[target.getRow()][target.getColumn()] == DiscStatus.EMPTY) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 対象の座標に石を置けるか判定する<br>
     * 対称座標を中心として全方向に対して順次探索を行いますが、１方向でも設置可能と判明した場合は即座に {@code true} を返します。
     * 未探索の方向は探索しません。
     * @param target 石を置く座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 設置できる場合は真 {@code true}、石を設置できない場合は偽 {@code false}
     */
    public Boolean canPut(Dimension target, Boolean isBlack) {
        // マスに石が既に置かれていない確認する
        if (isDiscEmpty(target) == false) {
            return false;
        }

        // 反転できる石があるか
        if (countReversibleDiscUp(target, isBlack) > 0) {
            return true;
        }
        if (countReversibleDiscDown(target, isBlack) > 0) {
            return true;
        }
        if (countReversibleDiscLeft(target, isBlack) > 0) {
            return true;
        }
        if (countReversibleDiscRight(target, isBlack) > 0) {
            return true;
        }
        if (countReversibleDiscLeftUp(target, isBlack) > 0) {
            return true;
        }
        if (countReversibleDiscRightUp(target, isBlack) > 0) {
            return true;
        }
        if (countReversibleDiscLeftDown(target, isBlack) > 0) {
            return true;
        }
        if (countReversibleDiscRightDown(target, isBlack) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 全てのマスに対して、石を置けるか確認する
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転可能な石が存在する場合は {@code true}, 存在しない場合は {@code false}
     */
    public Boolean canPutAll(Boolean isBlack) {
        for (int i = 0; i < boardSize.getRow(); i++) {
            for (int j = 0; j < boardSize.getColumn(); j++) {
                if (canPut(new Dimension(i, j), isBlack)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 対象の座標にある石と、プレイヤーの石の色が異なるか判定する
     * @param target 対象の石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 石の色が異なる場合は真 {@true}, 同じ場合は偽 {@code false}
     */
    private Boolean isDifferentDisc(Dimension target, Boolean isBlack) {
        int row = target.getRow();
        int column = target.getColumn();

        if ((isBlack && board[row][column] == DiscStatus.WHITE)
                || (isBlack == false && board[row][column] == DiscStatus.BLACK)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 対象の座標に石を置いた時、反転する石の個数を返す<br>
     * 対称座標を中心として全方向に対して順次探索を行い、全ての方向の探索が完了するまで処理を続けます。
     * 個数に関わらず１個以上の石を反転できるかの判定については、処理時間の観点で {@code canPut()} を使用してください。
     * @param target 石を置く座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転可能な石の数
     */
    public int countReversibleDisc(Dimension target, Boolean isBlack) {
        // マスに石が既に置かれていない確認する
        if (isDiscEmpty(target) == false) {
            return 0;
        }

        // 反転できる石があるか
        int reuslt = 0;
        reuslt += countReversibleDiscUp(target, isBlack);
        reuslt += countReversibleDiscDown(target, isBlack);
        reuslt += countReversibleDiscLeft(target, isBlack);
        reuslt += countReversibleDiscRight(target, isBlack);
        reuslt += countReversibleDiscLeftUp(target, isBlack);
        reuslt += countReversibleDiscRightUp(target, isBlack);
        reuslt += countReversibleDiscLeftDown(target, isBlack);
        reuslt += countReversibleDiscRightDown(target, isBlack);

        return reuslt;
    }

    /**
     * 置いた石の上方向に対して、反転可能な石が何個あるか調べる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscUp(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;
        int column = target.getColumn();

        for (int i = target.getRow() - 1; i >= 0 && i < boardSize.getRow(); i--) {
            if (isDifferentDisc(new Dimension(i, column), isBlack)) {
                // 色違いで反転可能な石である場合は、反転対象としてカウントする
                reversibleDisc++;
            } else if (board[i][column] == DiscStatus.EMPTY) {
                // 空きマスの場合は自分の石で挟めておらず反転できないので、0を返す。
                return 0;
            } else {
                // 自分の石があった場合、これまでにカウントした反転対象の石の個数を返す。
                return reversibleDisc;
            }
        }
        // リバーシ盤の端に到達した場合は自分の石で挟めておらず反転できないので、0を返す。
        return 0;
    }

    /**
     * 置いた石の下方向に対して、反転可能な石が何個あるか調べる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscDown(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;
        int column = target.getColumn();

        for (int i = target.getRow() + 1; i >= 0 && i < boardSize.getRow(); i++) {
            if (isDifferentDisc(new Dimension(i, column), isBlack)) {
                reversibleDisc++;
            } else if (board[i][column] == DiscStatus.EMPTY) {
                return 0;
            } else {
                return reversibleDisc;
            }
        }
        return 0;
    }

    /**
     * 置いた石の左方向に対して、反転可能な石が何個あるか調べる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscLeft(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;
        int row = target.getRow();

        for (int j = target.getColumn() - 1; j >= 0 && j < boardSize.getColumn(); j--) {
            if (isDifferentDisc(new Dimension(row, j), isBlack)) {
                reversibleDisc++;
            } else if (board[row][j] == DiscStatus.EMPTY) {
                reversibleDisc = 0;
                return 0;
            } else {
                return reversibleDisc;
            }
        }
        return 0;
    }

    /**
     * 置いた石の右方向に対して、反転可能な石が何個あるか調べる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscRight(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;
        int row = target.getRow();

        for (int j = target.getColumn() + 1; j >= 0 && j < boardSize.getColumn(); j++) {
            if (isDifferentDisc(new Dimension(row, j), isBlack)) {
                reversibleDisc++;
            } else if (board[row][j] == DiscStatus.EMPTY) {
                return 0;
            } else {
                return reversibleDisc;
            }
        }
        return 0;
    }

    /**
     * 置いた石の左上方向に対して、反転可能な石が何個あるか調べる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscLeftUp(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;

        for (int i = target.getRow() - 1, j = target.getColumn() - 1; i >= 0 && j >= 0 && i < boardSize.getRow()
                && j < boardSize.getColumn(); i--, j--) {
            if (isDifferentDisc(new Dimension(i, j), isBlack)) {
                reversibleDisc++;
            } else if (board[i][j] == DiscStatus.EMPTY) {
                return 0;
            } else {
                return reversibleDisc;
            }
        }
        return 0;
    }

    /**
     * 置いた石の右上方向に対して、反転可能な石が何個あるか調べる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscRightUp(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;

        for (int i = target.getRow() - 1, j = target.getColumn() + 1; i >= 0 && j >= 0 && i < boardSize.getRow()
                && j < boardSize.getColumn(); i--, j++) {
            if (isDifferentDisc(new Dimension(i, j), isBlack)) {
                reversibleDisc++;
            } else if (board[i][j] == DiscStatus.EMPTY) {
                return 0;
            } else {
                return reversibleDisc;
            }
        }
        return 0;
    }

    /**
     * 置いた石の左下方向に対して、反転可能な石が何個あるか調べる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscLeftDown(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;

        for (int i = target.getRow() + 1, j = target.getColumn() - 1; i >= 0 && j >= 0 && i < boardSize.getRow()
                && j < boardSize.getColumn(); i++, j--) {
            if (isDifferentDisc(new Dimension(i, j), isBlack)) {
                reversibleDisc++;
            } else if (board[i][j] == DiscStatus.EMPTY) {
                return 0;
            } else {
                return reversibleDisc;
            }
        }
        return 0;
    }

    /**
     * 置いた石の右下方向に対して、反転可能な石が何個あるか調べる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscRightDown(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;

        for (int i = target.getRow() + 1, j = target.getColumn() + 1; i >= 0 && j >= 0 && i < boardSize.getRow()
                && j < boardSize.getColumn(); i++, j++) {
            if (isDifferentDisc(new Dimension(i, j), isBlack)) {
                reversibleDisc++;
            } else if (board[i][j] == DiscStatus.EMPTY) {
                return 0;
            } else {
                return reversibleDisc;
            }
        }
        return 0;
    }

    /**
     * 石を置き、状態を更新する
     * @param target 石を置く座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 石を設置できる場合は真 {@code true}、設置できない場合は偽 {@code false} を返す。
     * @throws IllegalArgumentException 引数 {@code target, isBlack} のいずれかが {@code NULL} の場合、エラーを返す。
     */
    public Boolean put(Dimension target, Boolean isBlack) throws IllegalArgumentException {
        // 引数の正常性確認
        if (target == null) {
            throw new IllegalArgumentException("引数 \"target\" が NULL です。");
        }
        if (isBlack == null) {
            throw new IllegalArgumentException("引数 \"isBlack\" が NULL です。");
        }

        // 対象の位置に石を置けるか判定する
        if (canPut(target, isBlack) == false) {
            return false;
        }

        // 全方向に対して操作し、反転可能な石を反転させる
        int reverseCount = 0;

        if (countReversibleDiscUp(target, isBlack) > 0) {
            reverseCount += reverseDiscUp(target, isBlack);
        }
        if (countReversibleDiscDown(target, isBlack) > 0) {
            reverseCount += reverseDiscDown(target, isBlack);
        }
        if (countReversibleDiscLeft(target, isBlack) > 0) {
            reverseCount += reverseDiscLeft(target, isBlack);
        }
        if (countReversibleDiscRight(target, isBlack) > 0) {
            reverseCount += reverseDiscRight(target, isBlack);
        }
        if (countReversibleDiscLeftUp(target, isBlack) > 0) {
            reverseCount += reverseDiscLeftUp(target, isBlack);
        }
        if (countReversibleDiscRightUp(target, isBlack) > 0) {
            reverseCount += reverseDiscRightUp(target, isBlack);
        }
        if (countReversibleDiscLeftDown(target, isBlack) > 0) {
            reverseCount += reverseDiscLeftDown(target, isBlack);
        }
        if (countReversibleDiscRightDown(target, isBlack) > 0) {
            reverseCount += reverseDiscRightDown(target, isBlack);
        }

        // 反転できる石が1つもないのはルール上石を置くことができないため、石を置く処理を行わず終了する。
        if (reverseCount == 0) {
            return false;
        }

        if (isBlack) {
            board[target.getRow()][target.getColumn()] = DiscStatus.BLACK;
            blackDiscNum++;
        } else {
            board[target.getRow()][target.getColumn()] = DiscStatus.WHITE;
            whiteDiscNum++;
        }
        return true;
    }

    /**
     * 石の白黒を反転させる
     * @param target 反転させる石の座標
     */
    private void reverse(Dimension target) {
        int row = target.getRow();
        int column = target.getColumn();

        if (board[row][column] == DiscStatus.BLACK) {
            board[row][column] = DiscStatus.WHITE;
            blackDiscNum--;
            whiteDiscNum++;
        } else {
            board[row][column] = DiscStatus.BLACK;
            blackDiscNum++;
            whiteDiscNum--;
        }
    }

    /**
     * 置いた石の上方向にある石を反転させる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転させた石の個数
     */
    private int reverseDiscUp(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;
        int column = target.getColumn();

        for (int i = target.getRow() - 1; i >= 0 && i < boardSize.getRow(); i--) {
            // プレイヤーの石の色とマスに置かれた石の色が異なる場合、マスに置かれた石を反転させる。
            Dimension reverseTarget = new Dimension(i, column);
            if (isDifferentDisc(reverseTarget, isBlack)) {
                reverse(reverseTarget);
                reverseDisc++;
            } else {
                break;
            }
        }
        return reverseDisc;
    }

    /**
     * 置いた石の下方向にある石を反転させる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転させた石の個数
     */
    private int reverseDiscDown(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;
        int column = target.getColumn();

        for (int i = target.getRow() + 1; i >= 0 && i < boardSize.getRow(); i++) {
            // プレイヤーの石の色とマスに置かれた石の色が異なる場合、マスに置かれた石を反転させる。
            Dimension reverseTarget = new Dimension(i, column);
            if (isDifferentDisc(reverseTarget, isBlack)) {
                reverse(reverseTarget);
                reverseDisc++;
            } else {
                break;
            }
        }
        return reverseDisc;
    }

    /**
     * 置いた石の左方向にある石を反転させる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転させた石の個数
     */
    private int reverseDiscLeft(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;
        int row = target.getRow();

        for (int j = target.getColumn() - 1; j >= 0 && j < boardSize.getColumn(); j--) {
            // プレイヤーの石の色とマスに置かれた石の色が異なる場合、マスに置かれた石を反転させる。
            Dimension reverseTarget = new Dimension(row, j);
            if (isDifferentDisc(reverseTarget, isBlack)) {
                reverse(reverseTarget);
                reverseDisc++;
            } else {
                break;
            }
        }
        return reverseDisc;
    }

    /**
     * 置いた石の右方向にある石を反転させる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転させた石の個数
     */
    private int reverseDiscRight(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;
        int row = target.getRow();

        for (int j = target.getColumn() + 1; j >= 0 && j < boardSize.getColumn(); j++) {
            // プレイヤーの石の色とマスに置かれた石の色が異なる場合、マスに置かれた石を反転させる。
            Dimension reverseTarget = new Dimension(row, j);
            if (isDifferentDisc(reverseTarget, isBlack)) {
                reverse(reverseTarget);
                reverseDisc++;
            } else {
                break;
            }
        }
        return reverseDisc;
    }

    /**
     * 置いた石の左上方向にある石を反転させる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転させた石の個数
     */
    private int reverseDiscLeftUp(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;

        for (int i = target.getRow() - 1, j = target.getColumn() - 1; i >= 0 && i < boardSize.getRow() && j >= 0
                && j < boardSize.getColumn(); i--, j--) {
            // プレイヤーの石の色とマスに置かれた石の色が異なる場合、マスに置かれた石を反転させる。
            Dimension reverseTarget = new Dimension(i, j);
            if (isDifferentDisc(reverseTarget, isBlack)) {
                reverse(reverseTarget);
                reverseDisc++;
            } else {
                break;
            }
        }
        return reverseDisc;
    }

    /**
     * 置いた石の右上方向にある石を反転させる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転させた石の個数
     */
    private int reverseDiscRightUp(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;

        for (int i = target.getRow() - 1, j = target.getColumn() + 1; i >= 0 && i < boardSize.getRow() && j >= 0
                && j < boardSize.getColumn(); i--, j++) {
            // プレイヤーの石の色とマスに置かれた石の色が異なる場合、マスに置かれた石を反転させる。
            Dimension reverseTarget = new Dimension(i, j);
            if (isDifferentDisc(reverseTarget, isBlack)) {
                reverse(reverseTarget);
                reverseDisc++;
            } else {
                break;
            }
        }
        return reverseDisc;
    }

    /**
     * 置いた石の左下方向にある石を反転させる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転させた石の個数
     */
    private int reverseDiscLeftDown(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;

        for (int i = target.getRow() + 1, j = target.getColumn() - 1; i >= 0 && i < boardSize.getRow() && j >= 0
                && j < boardSize.getColumn(); i++, j--) {
            // プレイヤーの石の色とマスに置かれた石の色が異なる場合、マスに置かれた石を反転させる。
            Dimension reverseTarget = new Dimension(i, j);
            if (isDifferentDisc(reverseTarget, isBlack)) {
                reverse(reverseTarget);
                reverseDisc++;
            } else {
                break;
            }
        }
        return reverseDisc;
    }

    /**
     * 置いた石の右下方向にある石を反転させる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの石の色 (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 反転させた石の個数
     */
    private int reverseDiscRightDown(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;

        for (int i = target.getRow() + 1, j = target.getColumn() + 1; i >= 0 && i < boardSize.getRow() && j >= 0
                && j < boardSize.getColumn(); i++, j++) {
            // プレイヤーの石の色とマスに置かれた石の色が異なる場合、マスに置かれた石を反転させる。
            Dimension reverseTarget = new Dimension(i, j);
            if (isDifferentDisc(reverseTarget, isBlack)) {
                reverse(reverseTarget);
                reverseDisc++;
            } else {
                break;
            }
        }
        return reverseDisc;
    }
}
