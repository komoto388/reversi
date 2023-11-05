package algorithm;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import reversi.Board;
import reversi.Dimension;

class MiniMax01Test {

    MiniMax01 miniMax01;
    Board board;

    /**
     * privateメソッド evaluateMax() をリフレクションしたメソッド
     */
    int reflectEvaluateMax(int depth, Board currnetBoard, Dimension target) {
        int result = 0;
        try {
            Method method = MiniMax01.class.getDeclaredMethod("evaluateMax", int.class, Board.class, Dimension.class);
            method.setAccessible(true);
            result = (int) method.invoke(miniMax01, depth, board, target);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * privateメソッド evaluateMini() をリフレクションしたメソッド
     */
    int reflectEvaluateMini(int depth, Board currnetBoard, Dimension target) {
        int result = 0;
        try {
            Method method = MiniMax01.class.getDeclaredMethod("evaluateMini", int.class, Board.class, Dimension.class);
            method.setAccessible(true);
            result = (int) method.invoke(miniMax01, depth, board, target);
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
            Method method = MiniMax01.class.getDeclaredMethod("calcPoint", Board.class, Boolean.class);
            method.setAccessible(true);
            result = (int) method.invoke(miniMax01, board, isPlayerBlack);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    @BeforeEach
    void setUp() throws Exception {
        board = new Board(8, 8);
        miniMax01 = new MiniMax01(board, true);
    }

    @Test
    void testCalcPoint() {
        // ゲーム開始直後の評価
        assertEquals(0, reflectCalcPoint(board, true));
        assertEquals(0, reflectCalcPoint(board, false));

        // 黒がc4に石を置いた時の評価
        board.put(new Dimension(3, 2), true);
        assertEquals(300, reflectCalcPoint(board, true));
        assertEquals(-300, reflectCalcPoint(board, false));
    }

    @Test
    void testEvaluateMax() throws CloneNotSupportedException {
        System.out.println("深さ探索の結果");

        int point0 = reflectEvaluateMax(0, board, new Dimension(3, 2));
        System.out.println("深さ0の評価値: " + point0);
        assertEquals(300, point0);

        int point1 = reflectEvaluateMax(1, board, new Dimension(3, 2));
        System.out.println("深さ1の評価値: " + point1);
        assertEquals(0, point1);

        int point2 = reflectEvaluateMax(2, board, new Dimension(3, 2));
        System.out.println("深さ2の評価値: " + point2);
        assertEquals(300, point2);

        int point7 = reflectEvaluateMax(7, board, new Dimension(3, 2));
        System.out.println("深さ7の評価値: " + point7);
        assertEquals(200, point7);
    }

    @Test
    void testEvaluateMini() throws CloneNotSupportedException {
        System.out.println("深さ探索の結果");

        int point0 = reflectEvaluateMini(0, board, new Dimension(2, 4));
        System.out.println("深さ0の評価値: " + point0);
        assertEquals(300, point0);

        int point1 = reflectEvaluateMini(1, board, new Dimension(2, 4));
        System.out.println("深さ1の評価値: " + point1);
        assertEquals(0, point1);

        int point2 = reflectEvaluateMini(2, board, new Dimension(2, 4));
        System.out.println("深さ2の評価値: " + point2);
        assertEquals(300, point2);

        int point7 = reflectEvaluateMini(7, board, new Dimension(2, 4));
        System.out.println("深さ7の評価値: " + point7);
        assertEquals(200, point7);
    }

    @Test
    void testMiniMax01() {
        fail("まだ実装されていません");
    }

}
