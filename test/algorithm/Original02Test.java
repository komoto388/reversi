package algorithm;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reversi.Board;
import reversi.Dimension;

class Original02Test {

    Original02 original02;
    Board board;

    /**
     * privateメソッド evaluate() をリフレクションしたメソッド
     */
    int reflectEvaluate(int depth, Board board, Boolean isPlayerBlack, Dimension target) {
        int result = 0;
        try {
            Method method = Original02.class.getDeclaredMethod("evaluate", int.class, Board.class, Boolean.class,
                    Dimension.class);
            method.setAccessible(true);
            result = (int) method.invoke(original02, depth, board, isPlayerBlack, target);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * privateメソッド calcPoint() をリフレクションしたメソッド
     */
    int reflectCalcPoint(Board board, Boolean isPlayerBlack) {
        int result = 0;
        try {
            Method method = Original02.class.getDeclaredMethod("calcPoint", Board.class, Boolean.class);
            method.setAccessible(true);
            result = (int) method.invoke(original02, board, isPlayerBlack);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    @BeforeEach
    void setUp() throws Exception {
        board = new Board(8, 8);
        original02 = new Original02(board, true);
    }

    @Test
    void testCalcPoint() throws CloneNotSupportedException {
        // ゲーム開始直後の評価
        assertEquals(0, reflectCalcPoint(board, true));
        assertEquals(0, reflectCalcPoint(board, false));

        // 黒がc4に石を置いた時の評価
        board.put(new Dimension(3, 2), true);
        assertEquals(300, reflectCalcPoint(board, true));
        assertEquals(-300, reflectCalcPoint(board, false));
    }

    @Test
    void testEvaluate() throws CloneNotSupportedException {
        System.out.println("深さ探索の結果");

        int point0 = reflectEvaluate(0, board, true, new Dimension(3, 2));
        System.out.println("深さ0の評価値: " + point0);
        assertEquals(300, point0);

        int point1 = reflectEvaluate(1, board, true, new Dimension(3, 2));
        System.out.println("深さ1の評価値: " + point1);
        assertEquals(300, point1);

        int point2 = reflectEvaluate(2, board, true, new Dimension(3, 2));
        System.out.println("深さ2の評価値: " + point2);
        assertEquals(800, point2);

        int point7 = reflectEvaluate(7, board, true, new Dimension(3, 2));
        System.out.println("深さ7の評価値: " + point7);
        assertEquals(3200, point7);
    }

    @Test
    void testRun() {
        assertNotNull(original02.run());
    }
}
