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
        assertEquals(2, board.getDiscNum(Disc.BLACK));
        assertEquals(2, board.getDiscNum(Disc.WHITE));
        assertEquals(60, board.getEmptyDiscNum());

        assertTrue(board.isDiscEmpty(new Dimension(0, 0)));
        assertTrue(board.isDiscWhite(new Dimension(3, 3)));
        assertTrue(board.isDiscBlack(new Dimension(3, 4)));
    }

    @Test
    void testCanPutAll() {
        assertTrue(board.canPutAll(Disc.BLACK));
        assertTrue(board.canPutAll(Disc.WHITE));
    }

    @Test
    void testCanPut() {

        // 石を置ける場合
        assertTrue(board.put(new Dimension(3, 2), Disc.BLACK));
        assertTrue(board.put(new Dimension(2, 4), Disc.WHITE));

        // 既に石がある場合
        assertFalse(board.put(new Dimension(3, 3), Disc.BLACK));
        assertFalse(board.put(new Dimension(4, 4), Disc.WHITE));

        // 空きマスだが、石を置けない場合
        assertFalse(board.put(new Dimension(0, 0), Disc.BLACK));
        assertFalse(board.put(new Dimension(7, 7), Disc.WHITE));
        assertFalse(board.put(new Dimension(3, 2), Disc.WHITE));
        assertFalse(board.put(new Dimension(2, 4), Disc.BLACK));
    }

    @Test
    void testCountReversibleDisc() {

        // 石を置ける場合
        assertEquals(1, board.countReversibleDisc(new Dimension(3, 2), Disc.BLACK));
        assertEquals(1, board.countReversibleDisc(new Dimension(2, 4), Disc.WHITE));

        // 既に石がある場合
        assertEquals(0, board.countReversibleDisc(new Dimension(3, 3), Disc.BLACK));
        assertEquals(0, board.countReversibleDisc(new Dimension(4, 4), Disc.WHITE));

        // 空きマスだが、石を置けない場合
        assertEquals(0, board.countReversibleDisc(new Dimension(0, 0), Disc.BLACK));
        assertEquals(0, board.countReversibleDisc(new Dimension(0, 0), Disc.WHITE));
    }

    @Test
    void testPut() {
        // 石を置ける場合
        assertTrue(board.put(new Dimension(3, 2), Disc.BLACK));
        assertTrue(board.put(new Dimension(2, 4), Disc.WHITE));

        // 既に石がある場合
        assertFalse(board.put(new Dimension(3, 3), Disc.BLACK));
        assertFalse(board.put(new Dimension(4, 4), Disc.WHITE));

        // 空きマスだが、石を置けない場合
        assertFalse(board.put(new Dimension(0, 0), Disc.BLACK));
        assertFalse(board.put(new Dimension(7, 7), Disc.WHITE));
        assertFalse(board.put(new Dimension(3, 2), Disc.WHITE));
        assertFalse(board.put(new Dimension(2, 4), Disc.BLACK));
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

            boardClone.put(target, Disc.BLACK);
            assertAll("コピー先の操作がコピー元に反映されないことを確認する",
                    () -> assertTrue(board.isDiscEmpty(target), "コピー元のマスに、置いていない石があります"),
                    () -> assertFalse(boardClone.isDiscEmpty(target), "コピー先のマスに、置いた石がありません"));

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

            board.put(target, Disc.BLACK);
            assertAll("コピー元の操作がコピー先に反映されないことを確認する",
                    () -> assertFalse(board.isDiscEmpty(target), "コピー元のマスに、置いた石がありません"),
                    () -> assertTrue(boardClone.isDiscEmpty(target), "コピー先のマスに、置いていない石があります"));

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
