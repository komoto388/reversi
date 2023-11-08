package algorithm;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import reversi.Board;
import reversi.Dimension;
import test.ReflectMember;

class Original02Test {

    // テスト対象クラスのインスタンス
    Original02 original02;
    Board board;

    // テスト対象クラスの Private メンバを操作するインスタンス
    ReflectMember reflClazz;

    // リフレクションされた Private メソッド
    Method reflEvaluate;
    Method reflCalcPoint;

    // リフレクションされた Private フィールド

    @BeforeEach
    void setUp() throws Exception {
        board = new Board(8, 8);
        original02 = new Original02(board, true);

        reflClazz = new ReflectMember(Original02.class);

        reflEvaluate = reflClazz.getMethod("evaluate", int.class, Board.class, Boolean.class, Dimension.class);
        reflCalcPoint = reflClazz.getMethod("calcPoint", Board.class, Boolean.class);
    }

    @Test
    void testCalcPoint() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        // ゲーム開始直後の評価
        assertEquals(0, (int) reflCalcPoint.invoke(original02, board, true));
        assertEquals(0, (int) reflCalcPoint.invoke(original02, board, false));

        // 黒がc4に石を置いた時の評価
        board.put(new Dimension(3, 2), true);
        assertEquals(300, (int) reflCalcPoint.invoke(original02, board, true));
        assertEquals(300, (int) reflCalcPoint.invoke(original02, board, false));
        fail("値の算出について要確認");
    }

    @Test
    void testEvaluate() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int point0 = (int) reflEvaluate.invoke(original02, 0, board, true, new Dimension(3, 2));
        assertEquals(300, point0);

        int point1 = (int) reflEvaluate.invoke(original02, 1, board, true, new Dimension(3, 2));
        assertEquals(300, point1);

        int point2 = (int) reflEvaluate.invoke(original02, 2, board, true, new Dimension(3, 2));
        assertEquals(800, point2);

        int point7 = (int) reflEvaluate.invoke(original02, 7, board, true, new Dimension(3, 2));
        assertEquals(2400, point7);
    }
    
    @Test
    void testRun() {
        assertNotNull(original02.run());
    }
}
