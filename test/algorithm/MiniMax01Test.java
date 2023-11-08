package algorithm;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.Global;
import reversi.Board;
import reversi.Dimension;
import test.ReflectMember;
import test.TestIO;

class MiniMax01Test {

    // クラス名
    static String className;

    // テスト対象クラスのインスタンス
    MiniMax01 miniMax01;
    Board board;

    // テスト対象クラスの Private メンバを操作するインスタンス
    ReflectMember reflClazz;

    // リフレクションされた Private メソッド
    Method reflEvaluateMax;
    Method reflEvaluateMini;
    Method reflCalcPoint;

    // リフレクションされた Private フィールド

    @BeforeEach
    void setUp() throws Exception {
        board = new Board(8, 8);
        miniMax01 = new MiniMax01(board, true);

        reflClazz = new ReflectMember(MiniMax01.class);
        reflEvaluateMax = reflClazz.getMethod("evaluateMax", int.class, Board.class, Dimension.class);
        reflEvaluateMini = reflClazz.getMethod("evaluateMini", int.class, Board.class, Dimension.class);
        reflCalcPoint = reflClazz.getMethod("calcPoint", Board.class);
    }

    /*
     * 評価店への乱数加算がない状態で、プレイヤー黒の立場での評価をテストする<br>
     * デフォルトは加算がある
     */
    @Test
    void testCalcPointBlackNoRandom()
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        assertFalse(Global.IS_ADD_RANDOM);

        assertAll("プレイヤーが黒の時、ゲーム開始直後の評価",
                () -> assertEquals(0, (int) reflCalcPoint.invoke(miniMax01, board)));

        board.put(new Dimension(3, 2), true);

        assertAll("プレイヤー黒がc4に石を置いた時の、黒の立場での評価",
                () -> assertEquals(300, (int) reflCalcPoint.invoke(miniMax01, board)));
        fail("値の算出について要確認");
    }

    @Test
    void testCalcPointWhite() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        assertFalse(Global.IS_ADD_RANDOM);

        // プレイするプレイヤーを強制的に白に変更する
        Field reflIsPlayerBlack = reflClazz.getField("isPlayerBlack");
        reflIsPlayerBlack.set(miniMax01, false);

        assertAll("プレイヤーが白の時、ゲーム開始直後の評価",
                () -> assertEquals(0, (int) reflCalcPoint.invoke(miniMax01, board)));

        // 
        board.put(new Dimension(3, 2), true);

        assertAll("プレイヤー黒がc4に石を置いた時の、白の立場での評価",
                () -> assertEquals(-300, (int) reflCalcPoint.invoke(miniMax01, board)));
    }

    @Test
    void testEvaluateMax() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        assertFalse(Global.IS_ADD_RANDOM);

        int point0 = (int) reflEvaluateMax.invoke(miniMax01, 0, board, new Dimension(3, 2));
        assertEquals(300, point0);

        int point1 = (int) reflEvaluateMax.invoke(miniMax01, 1, board, new Dimension(3, 2));
        assertEquals(0, point1);

        int point2 = (int) reflEvaluateMax.invoke(miniMax01, 2, board, new Dimension(3, 2));
        assertEquals(300, point2);

        int point7 = (int) reflEvaluateMax.invoke(miniMax01, 7, board, new Dimension(3, 2));
        assertEquals(-200, point7);
    }

    @Test
    void testEvaluateMini() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        assertFalse(Global.IS_ADD_RANDOM);

        int point0 = (int) reflEvaluateMini.invoke(miniMax01, 0, board, new Dimension(2, 4));
        assertEquals(-300, point0);

        int point1 = (int) reflEvaluateMini.invoke(miniMax01, 1, board, new Dimension(2, 4));
        assertEquals(0, point1);

        int point2 = (int) reflEvaluateMini.invoke(miniMax01, 2, board, new Dimension(2, 4));
        assertEquals(-300, point2);

        int point7 = (int) reflEvaluateMini.invoke(miniMax01, 7, board, new Dimension(2, 4));
        assertEquals(200, point7);
    }
}
