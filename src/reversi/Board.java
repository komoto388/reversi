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
     * 対象の座標に石を置けるか確認する
     * @param target 石を置く座標
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 設置できる場合は真 {@code true}、石を設置できない場合は偽 {@code false} を返す。
     */
    public Boolean canPut(Dimension target, Boolean isBlack) {
        // マスに石が既に置かれていない確認する
        if (board[target.getRow()][target.getColumn()] != DiscStatus.EMPTY) {
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転可能な石が存在する場合は {@code true}, 存在しない場合は {@code false} を返す。
     */
    public Boolean canPutAll(Boolean isBlack) {
        for (int i = 0; i < size.getRow(); i++) {
            for (int j = 0; j < size.getColumn(); j++) {
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 石の色が異なる場合は真 {@true}, 同じ場合は偽 {@code false} を返す。
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
     * 置いた石の上方向に対して、反転可能な石が何個あるか調べる
     * @param target プレイヤーが置く石の座標
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscUp(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;
        int column = target.getColumn();

        for (int i = target.getRow() - 1; i >= 0 && i < size.getRow(); i--) {
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscDown(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;
        int column = target.getColumn();

        for (int i = target.getRow() + 1; i >= 0 && i < size.getRow(); i++) {
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscLeft(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;
        int row = target.getRow();

        for (int j = target.getColumn() - 1; j >= 0 && j < size.getColumn(); j--) {
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscRight(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;
        int row = target.getRow();

        for (int j = target.getColumn() + 1; j >= 0 && j < size.getColumn(); j++) {
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscLeftUp(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;

        for (int i = target.getRow() - 1, j = target.getColumn() - 1; i >= 0 && j >= 0 && i < size.getRow()
                && j < size.getColumn(); i--, j--) {
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscRightUp(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;

        for (int i = target.getRow() - 1, j = target.getColumn() + 1; i >= 0 && j >= 0 && i < size.getRow()
                && j < size.getColumn(); i--, j++) {
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscLeftDown(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;

        for (int i = target.getRow() + 1, j = target.getColumn() - 1; i >= 0 && j >= 0 && i < size.getRow()
                && j < size.getColumn(); i++, j--) {
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転可能な石の個数
     */
    private int countReversibleDiscRightDown(Dimension target, Boolean isBlack) {
        int reversibleDisc = 0;

        for (int i = target.getRow() + 1, j = target.getColumn() + 1; i >= 0 && j >= 0 && i < size.getRow()
                && j < size.getColumn(); i++, j++) {
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 設置できる場合は真 {@code true}、既に設置されている石の場合は偽 {@code false} を返す。
     * @throws Exception 
     */
    public Boolean put(Dimension target, Boolean isBlack) {
        // 対象の位置に石を置けるか判定する
        if (canPut(target, isBlack) == false) {
            return false;
        }

        // 各方向に対して石を反転させる
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

        // 反転できる石がないのはルール違反のため、石を置く処理を行わず終了する。
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転させた石の個数
     */
    private int reverseDiscUp(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;
        int column = target.getColumn();

        for (int i = target.getRow() - 1; i >= 0 && i < size.getRow(); i--) {
            // プレイヤーが黒で石が白、またはプレイヤーが白で石が黒の場合、石を反転させる。
            Dimension target2 = new Dimension(i, column);
            if (isDifferentDisc(target2, isBlack)) {
                reverse(target2);
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転させた石の個数
     */
    private int reverseDiscDown(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;
        int column = target.getColumn();

        for (int i = target.getRow() + 1; i >= 0 && i < size.getRow(); i++) {
            // プレイヤーが黒で石が白、またはプレイヤーが白で石が黒の場合、石を反転させる。
            Dimension target2 = new Dimension(i, column);
            if (isDifferentDisc(target2, isBlack)) {
                reverse(target2);
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転させた石の個数
     */
    private int reverseDiscLeft(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;
        int row = target.getRow();

        for (int j = target.getColumn() - 1; j >= 0 && j < size.getColumn(); j--) {
            // プレイヤーが黒で石が白、またはプレイヤーが白で石が黒の場合、石を反転させる。
            Dimension target2 = new Dimension(row, j);
            if (isDifferentDisc(target2, isBlack)) {
                reverse(target2);
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転させた石の個数
     */
    private int reverseDiscRight(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;
        int row = target.getRow();

        for (int j = target.getColumn() + 1; j >= 0 && j < size.getColumn(); j++) {
            // プレイヤーが黒で石が白、またはプレイヤーが白で石が黒の場合、石を反転させる。
            Dimension target2 = new Dimension(row, j);
            if (isDifferentDisc(target2, isBlack)) {
                reverse(target2);
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転させた石の個数
     */
    private int reverseDiscLeftUp(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;

        for (int i = target.getRow() - 1, j = target.getColumn() - 1; i >= 0 && i < size.getRow() && j >= 0
                && j < size.getColumn(); i--, j--) {
            // プレイヤーが黒で石が白、またはプレイヤーが白で石が黒の場合、石を反転させる。
            Dimension target2 = new Dimension(i, j);
            if (isDifferentDisc(target2, isBlack)) {
                reverse(target2);
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転させた石の個数
     */
    private int reverseDiscRightUp(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;

        for (int i = target.getRow() - 1, j = target.getColumn() + 1; i >= 0 && i < size.getRow() && j >= 0
                && j < size.getColumn(); i--, j++) {
            // プレイヤーが黒で石が白、またはプレイヤーが白で石が黒の場合、石を反転させる。
            Dimension target2 = new Dimension(i, j);
            if (isDifferentDisc(target2, isBlack)) {
                reverse(target2);
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転させた石の個数
     */
    private int reverseDiscLeftDown(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;

        for (int i = target.getRow() + 1, j = target.getColumn() - 1; i >= 0 && i < size.getRow() && j >= 0
                && j < size.getColumn(); i++, j--) {
            // プレイヤーが黒で石が白、またはプレイヤーが白で石が黒の場合、石を反転させる。
            Dimension target2 = new Dimension(i, j);
            if (isDifferentDisc(target2, isBlack)) {
                reverse(target2);
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
     * @param isBlack プレイヤーの色を表す。黒の場合は真 {@code true}、白の場合は偽 {@code false} とする。
     * @return 反転させた石の個数
     */
    private int reverseDiscRightDown(Dimension target, Boolean isBlack) {
        int reverseDisc = 0;

        for (int i = target.getRow() + 1, j = target.getColumn() + 1; i >= 0 && i < size.getRow() && j >= 0
                && j < size.getColumn(); i++, j++) {
            // プレイヤーが黒で石が白、またはプレイヤーが白で石が黒の場合、石を反転させる。
            Dimension target2 = new Dimension(i, j);
            if (isDifferentDisc(target2, isBlack)) {
                reverse(target2);
                reverseDisc++;
            } else {
                break;
            }
        }
        return reverseDisc;
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
