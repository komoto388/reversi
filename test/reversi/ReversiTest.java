package reversi;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import algorithm.AlgorithmType;
import gamerecord.GameRecord;
import test.ReflectMember;

class ReversiTest {

    @Nested
    class Constracter {

        Reversi reversi;
        Player player1, player2;

        @Test
        void testReversi() {
            player1 = new Player("Player1", Disc.BLACK, AlgorithmType.MANUAL);
            player2 = new Player("Player2", Disc.WHITE, AlgorithmType.RANDOM);
            reversi = new Reversi(player1, player2);

            assertAll("インスタンスが正常に作成されていること",
                    () -> assertNotNull(reversi));
        }

        /* 
         * 異常系のテスト
         */
        @Disabled
        @Test
        void testReversiIllegal() {
            fail("異常終了するようにしているため、未実施");

            player1 = new Player("Player1", Disc.BLACK, AlgorithmType.MANUAL);
            player2 = new Player("Player2", Disc.WHITE, AlgorithmType.RANDOM);

            //            reversi = new Reversi(null, player2);
            //            assertAll("インスタンスが正常に作成されていなこと",
            //                    () -> assertNull(reversi));
            //
            //            reversi = new Reversi(player1, null);
            //            assertAll("インスタンスが正常に作成されていなこと",
            //                    () -> assertNull(reversi));
        }

    }

    @Nested
    class Getter {
        Reversi reversi;
        Player player1, player2;

        ReflectMember reflReversi;

        Field reflBoard;
        Field reflCurrentPlayerIndex;
        Field reflTurnCount;

        Method reflGetNextPlayerIndex;

        @BeforeAll
        static void setUpBeforeClass() throws Exception {
        }

        @BeforeEach
        void setUp() throws Exception {
            player1 = new Player("Player1", Disc.BLACK, AlgorithmType.MANUAL);
            player2 = new Player("Player2", Disc.WHITE, AlgorithmType.RANDOM);
            reversi = new Reversi(player1, player2);

            reflReversi = new ReflectMember(Reversi.class);

            reflBoard = reflReversi.getField("board");
            reflCurrentPlayerIndex = reflReversi.getField("currentPlayerIndex");
            reflTurnCount = reflReversi.getField("turnCount");

            reflGetNextPlayerIndex = reflReversi.getMethod("getNextPlayerIndex");
        }

        @Test
        void testGetBoard() {
            assertAll("リバーシ盤が生成されていること",
                    () -> assertNotNull(reversi.getBoard()),
                    () -> assertSame((Board) reflBoard.get(reversi), reversi.getBoard()));
        }

        @Test
        void testGetCurrentPlayer() {
            assertAll("1番目のプレイヤーを現在のプレイヤーを正しく取得できること",
                    () -> {
                        Player testPlayer = reversi.getCurrentPlayer();
                        assertNotNull(testPlayer);
                        assertSame(player1, testPlayer);
                    });

            assertAll("インデックスの値を変更した時、変更後の現在のプレイヤーを正しく取得できること",
                    () -> assertEquals(0, reflCurrentPlayerIndex.get(reversi)),
                    () -> assertDoesNotThrow(
                            () -> {
                                reflCurrentPlayerIndex.set(reversi, 1);

                                Player testPlayer = reversi.getCurrentPlayer();
                                assertNotNull(testPlayer);
                                assertSame(player2, testPlayer);
                            }));
        }

        @Test
        void testGetNextPlayer() {
            assertAll("2番目のプレイヤーを次のプレイヤーを正しく取得できること",
                    () -> {
                        Player testPlayer = reversi.getNextPlayer();
                        assertNotNull(testPlayer);
                        assertSame(player2, testPlayer);
                    });

            assertAll("インデックスの値が配列最後のプレイヤーを示す時、配列の最初にあるプレイヤーを示すこと",
                    () -> assertEquals(0, reflCurrentPlayerIndex.get(reversi)),
                    () -> assertDoesNotThrow(
                            () -> {
                                reflCurrentPlayerIndex.set(reversi, 1);

                                Player testPlayer = reversi.getNextPlayer();
                                assertNotNull(testPlayer);
                                assertSame(player1, testPlayer);
                            }));
        }

        @Test
        void testGetNextPlayerIndex() {
            assertAll("次のプレイヤーを表すインデックスを取得できること",
                    () -> assertEquals(0, reflCurrentPlayerIndex.get(reversi)),
                    () -> assertEquals(1, reflGetNextPlayerIndex.invoke(reversi)));

            assertAll("インデックスの値が配列最後のプレイヤーを示す時、配列の最初にあるプレイヤーを示すインデックスの値を取得できること",
                    () -> assertEquals(0, reflCurrentPlayerIndex.get(reversi)),
                    () -> assertDoesNotThrow(
                            () -> {
                                reflCurrentPlayerIndex.set(reversi, 1);
                                assertEquals(1, reflCurrentPlayerIndex.get(reversi));
                                assertEquals(0, reflGetNextPlayerIndex.invoke(reversi));
                            }));
        }

        @Test
        void testGetTurnCount() {
            assertAll("ターン数の初期値を正しく取得できること",
                    () -> assertEquals(1, reversi.getTurnCount()));
        }

        @Test
        void testIsSkip() {
            assertAll("初期状態のため、スキップする必要はないと取得できること",
                    () -> assertFalse(reversi.isSkip()));
        }
    }

    @Nested
    class Work {
        Reversi reversi;
        Player player1, player2;

        ReflectMember reflReversi;

        //        Field reflBoard;
        Field reflCurrentPlayerIndex;
        Field reflTurnCount;

        Method reflIsGameFinish;

        @BeforeAll
        static void setUpBeforeClass() throws Exception {
        }

        @BeforeEach
        void setUp() throws Exception {
            player1 = new Player("Player1", Disc.BLACK, AlgorithmType.MANUAL);
            player2 = new Player("Player2", Disc.WHITE, AlgorithmType.RANDOM);
            reversi = new Reversi(player1, player2);

            reflReversi = new ReflectMember(Reversi.class);

            //            reflBoard = reflReversi.getField("board");
            reflCurrentPlayerIndex = reflReversi.getField("currentPlayerIndex");
            reflTurnCount = reflReversi.getField("turnCount");

            reflIsGameFinish = reflReversi.getMethod("isGameFinish", GameRecord.class);
        }

        @Test
        void testPut() {
            assertAll("設置可能な座標に石を置いた時、Trueを返すこと",
                    () -> assertDoesNotThrow(
                            () -> {
                                assertTrue(reversi.put(new Dimension(3, 2)));
                            }));

            assertAll("既に石がある場所に石を置いた時、Falseを返すこと",
                    () -> assertDoesNotThrow(
                            () -> {
                                assertFalse(reversi.put(new Dimension(3, 3)));
                            }));

            assertAll("石の座標がNULLの時、例外を出力すること",
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> {
                                reversi.put(null);
                            }));
        }

        @Test
        void testJudge() {
            GameRecord gameRecord = new GameRecord();

            assertAll("初期状態の場合、ゲーム続行と判定すること",
                    () -> assertEquals(ResultType.NONE, reversi.judge(gameRecord)));

            fail("[未実装] ゲーム完了など、石が置けない状況のテストがまだ実装されていません");
        }

        @Test
        void testIsGameFinish() {
            GameRecord gameRecord = new GameRecord();

            assertAll("初期状態の場合、ゲーム続行と判定すること",
                    () -> assertEquals(false, reflIsGameFinish.invoke(reversi, gameRecord)));

            fail("[未実装] ゲーム完了など、石が置けない状況のテストがまだ実装されていません");
        }

        @Test
        void testNext() {
            assertAll("ターン数と現在のプレイヤーを表すインデックスが更新されていること",
                    () -> {
                        assertEquals(1, (int) reflTurnCount.get(reversi));
                        assertEquals(0, (int) reflCurrentPlayerIndex.get(reversi));
                    },
                    () -> {
                        reversi.next();
                        assertEquals(2, (int) reflTurnCount.get(reversi));
                        assertEquals(1, (int) reflCurrentPlayerIndex.get(reversi));
                    },
                    () -> {
                        reversi.next();
                        assertEquals(3, (int) reflTurnCount.get(reversi));
                        assertEquals(0, (int) reflCurrentPlayerIndex.get(reversi));
                    });
        }
    }
}
