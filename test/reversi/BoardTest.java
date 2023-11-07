package reversi;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {
    Board board;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board(8, 8);
    }

    @Test
    void testBoard() {
        assertEquals(8, board.getSize().getRow());
        assertEquals(8, board.getSize().getColumn());
        assertEquals(2, board.getBlackDiscNum());
        assertEquals(2, board.getWhiteDiscNum());
        assertEquals(60, board.getEmptyNum());

        assertTrue(board.isEmpty(new Dimension(0, 0)));
        assertTrue(board.isWhite(new Dimension(3, 3)));
        assertTrue(board.isBlack(new Dimension(3, 4)));
    }

    @Test
    void testCanPutAll() {
        Boolean playerIsBlack = true;

        assertTrue(board.canPutAll(playerIsBlack));
        assertTrue(board.canPutAll(!playerIsBlack));
    }

    @Test
    void testCanPut() {
        Boolean playerIsBlack = true;

        // 石を置ける場合
        assertTrue(board.put(new Dimension(3, 2), playerIsBlack));
        assertTrue(board.put(new Dimension(2, 4), !playerIsBlack));

        // 既に石がある場合
        assertFalse(board.put(new Dimension(3, 3), playerIsBlack));
        assertFalse(board.put(new Dimension(4, 4), !playerIsBlack));

        // 空きマスだが、石を置けない場合
        assertFalse(board.put(new Dimension(0, 0), playerIsBlack));
        assertFalse(board.put(new Dimension(7, 7), !playerIsBlack));
        assertFalse(board.put(new Dimension(3, 2), !playerIsBlack));
        assertFalse(board.put(new Dimension(2, 4), playerIsBlack));
    }

    @Test
    void testCountReversibleDisc() {
        Boolean playerIsBlack = true;

        // 石を置ける場合
        assertEquals(1, board.countReversibleDisc(new Dimension(3, 2), playerIsBlack));
        assertEquals(1, board.countReversibleDisc(new Dimension(2, 4), !playerIsBlack));

        // 既に石がある場合
        assertEquals(0, board.countReversibleDisc(new Dimension(3, 3), playerIsBlack));
        assertEquals(0, board.countReversibleDisc(new Dimension(4, 4), !playerIsBlack));

        // 空きマスだが、石を置けない場合
        assertEquals(0, board.countReversibleDisc(new Dimension(0, 0), playerIsBlack));
        assertEquals(0, board.countReversibleDisc(new Dimension(0, 0), !playerIsBlack));
    }

    @Test
    void testPut() {
        Boolean playerIsBlack = true;

        // 石を置ける場合
        assertTrue(board.put(new Dimension(3, 2), playerIsBlack));
        assertTrue(board.put(new Dimension(2, 4), !playerIsBlack));

        // 既に石がある場合
        assertFalse(board.put(new Dimension(3, 3), playerIsBlack));
        assertFalse(board.put(new Dimension(4, 4), !playerIsBlack));

        // 空きマスだが、石を置けない場合
        assertFalse(board.put(new Dimension(0, 0), playerIsBlack));
        assertFalse(board.put(new Dimension(7, 7), !playerIsBlack));
        assertFalse(board.put(new Dimension(3, 2), !playerIsBlack));
        assertFalse(board.put(new Dimension(2, 4), playerIsBlack));
    }

    @Test
    void testCloneCopyed() {
        Dimension target = new Dimension(3, 2);

        Board boardSame = board;
        assertSame(boardSame, board);
        assertSame(boardSame.getSize(), board.getSize());

        try {
            Board boardClone = board.clone();
            assertNotSame(boardClone, board);
            assertNotSame(boardClone.getSize(), board.getSize());

            boardClone.put(target, true);
            assertAll("コピー先の操作がコピー元に反映されないことを確認する",
                    () -> assertTrue(board.isEmpty(target), "コピー元のマスに、置いていない石があります"),
                    () -> assertFalse(boardClone.isEmpty(target), "コピー先のマスに、置いた石がありません"));

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCloneOriginal() {
        Dimension target = new Dimension(3, 2);

        try {
            Board boardClone = board.clone();
            assertNotSame(boardClone, board);
            assertNotSame(boardClone.getSize(), board.getSize());

            board.put(target, true);
            assertAll("コピー元の操作がコピー先に反映されないことを確認する",
                    () -> assertFalse(board.isEmpty(target), "コピー元のマスに、置いた石がありません"),
                    () -> assertTrue(boardClone.isEmpty(target), "コピー先のマスに、置いていない石があります"));

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
