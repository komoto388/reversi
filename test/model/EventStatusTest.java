package model;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import algorithm.AlgorithmType;
import reversi.Player;
import reversi.Reversi;
import test.ReflectMember;

class EventStatusTest {

    //    Player playerCom1, playerCom2, playerMaual1, playerMaual2;
    //    Reversi reversiCom, reversiManual;
    //    ReversiModel modelCom, modelManual;
    //    EventStatus eventStatusCom, eventStatusManual;

    //    ReflectMember reflclazz;
    //    Field reflReversi;

    @DisplayName("初期化、値取得のテスト")
    @Nested
    class TestGetter {
        Player player1, player2;
        Reversi reversi;
        ReversiModel model;
        EventStatus eventStatus;

        ReflectMember reflclazz;
        Field reflReversi;

        @BeforeEach
        void setUp() throws Exception {
            player1 = new Player("COM1", true, AlgorithmType.Random);
            player2 = new Player("COM2", false, AlgorithmType.Random);
            reversi = new Reversi(player1, player2);
            model = new ReversiModel(new ReversiData(reversi, player1, player2, true), true);
            eventStatus = new EventStatus(reversi, EventStatusValue.PLAY);

            reflclazz = new ReflectMember(EventStatus.class);
            reflReversi = reflclazz.getField("reversi");
        }

        @Test
        void testEventStatus() {
            assertAll("初期値が正しく設定されていること",
                    () -> assertNotNull(eventStatus),
                    () -> assertSame(reversi, (Reversi) reflReversi.get(eventStatus)),
                    () -> assertEquals(EventStatusValue.PLAY, eventStatus.getStatus()),
                    () -> assertFalse(eventStatus.canUserControll()),
                    () -> assertEquals(EventStatusValue.PLAY.getName(), eventStatus.getName()));
        }

        @Test
        void testGetStatus() {
            assertEquals(EventStatusValue.PLAY, eventStatus.getStatus());
        }

        @Test
        void testCanUserControll() {
            assertFalse(eventStatus.canUserControll());
        }

        @Test
        void testGetName() {
            assertEquals(EventStatusValue.PLAY.getName(), eventStatus.getName());
        }
    }

    @DisplayName("プレイヤーのアルゴリズムが COM の場合での設定変更テスト")
    @Nested
    class TestSetterCOM {
        Player player1, player2;
        Reversi reversi;
        ReversiModel model;
        EventStatus eventStatus;

        @BeforeEach
        void setUp() throws Exception {
            player1 = new Player("COM1", true, AlgorithmType.Random);
            player2 = new Player("COM2", false, AlgorithmType.Random);
            reversi = new Reversi(player1, player2);
            model = new ReversiModel(new ReversiData(reversi, player1, player2, true), true);
            eventStatus = new EventStatus(reversi, EventStatusValue.PLAY);
        }

        @Test
        void testSet() {
            // 正常系のテスト
            eventStatus.set(EventStatusValue.JUDGE);

            assertAll("イベントステータスを JUDGE にした時、イベントステータスが更新されること",
                    () -> assertEquals(EventStatusValue.JUDGE, eventStatus.getStatus()),
                    () -> assertFalse(eventStatus.canUserControll()),
                    () -> assertEquals(EventStatusValue.JUDGE.getName(), eventStatus.getName()));

            eventStatus.set(EventStatusValue.PLAY_COM);

            assertAll("プレイヤーがCOMでイベントステータスを PLAY_COM にした時、ステータスが PLAY になること",
                    () -> assertEquals(EventStatusValue.PLAY, eventStatus.getStatus()),
                    () -> assertFalse(eventStatus.canUserControll()),
                    () -> assertEquals(EventStatusValue.PLAY.getName(), eventStatus.getName()));
        }

        @Test
        void testRelabelPlayerStatus() {
            assertAll("初期状態ではイベントステータスは PLAY, ユーザーコントローラー は false になること",
                    () -> assertEquals(EventStatusValue.PLAY, eventStatus.getStatus()),
                    () -> assertFalse(eventStatus.canUserControll()));

            // イベントステータス PLAY のラベル付け替え処理後の状態
            assertAll("初期状態ではイベントステータスは PLAY_COM, ユーザーコントローラー は false になること",
                    () -> assertDoesNotThrow(
                            () -> {
                                Boolean tmp = eventStatus.relabelPlayerStatus();
                                assertTrue(tmp);
                                assertEquals(EventStatusValue.PLAY_COM, eventStatus.getStatus());
                                assertFalse(eventStatus.canUserControll());
                            }));

            // 異常系のテスト
            assertAll("PLAYER以外のイベントステータスの時、メソッドからFalseが返され、値が変更されていないこと",
                    () -> {
                        // イベントステータスをJUDGEに設定する
                        eventStatus.set(EventStatusValue.JUDGE);
                        assertEquals(EventStatusValue.JUDGE, eventStatus.getStatus());
                        assertFalse(eventStatus.canUserControll());
                    },
                    () -> assertDoesNotThrow(
                            () -> {
                                Boolean tmp = eventStatus.relabelPlayerStatus();
                                assertFalse(tmp);
                                assertEquals(EventStatusValue.JUDGE, eventStatus.getStatus());
                                assertFalse(eventStatus.canUserControll());
                            }));
        }
    }

    @DisplayName("プレイヤーのアルゴリズムが Manual の場合での設定変更テスト")
    @Nested
    class TestSetterManual {
        Player player1, player2;
        Reversi reversi;
        ReversiModel model;
        EventStatus eventStatus;

        @BeforeEach
        void setUp() throws Exception {
            // COM のテストとは違い、プレイヤーのアルゴリズムがマニュアルになっている
            player1 = new Player("Manual1", true, AlgorithmType.Manual);
            player2 = new Player("Manual2", false, AlgorithmType.Manual);
            reversi = new Reversi(player1, player2);
            model = new ReversiModel(new ReversiData(reversi, player1, player2, true), true);
            eventStatus = new EventStatus(reversi, EventStatusValue.PLAY);
        }

        @Test
        void testSet() {
            // 正常系のテスト
            eventStatus.set(EventStatusValue.JUDGE);

            assertAll("イベントステータスを JUDGE にした時、イベントステータスが更新されること",
                    () -> assertEquals(EventStatusValue.JUDGE, eventStatus.getStatus()),
                    () -> assertFalse(eventStatus.canUserControll()),
                    () -> assertEquals(EventStatusValue.JUDGE.getName(), eventStatus.getName()));

            eventStatus.set(EventStatusValue.PLAY_COM);

            assertAll("プレイヤーがCOMでイベントステータスを PLAY_COM にした時、ステータスが PLAY になること",
                    () -> assertEquals(EventStatusValue.PLAY, eventStatus.getStatus()),
                    () -> assertFalse(eventStatus.canUserControll()),
                    () -> assertEquals(EventStatusValue.PLAY.getName(), eventStatus.getName()));
        }

        @Test
        void testRelabelPlayerStatus() {
            assertAll("初期状態ではイベントステータスは PLAY, ユーザーコントローラー は false になること",
                    () -> assertEquals(EventStatusValue.PLAY, eventStatus.getStatus()),
                    () -> assertFalse(eventStatus.canUserControll()));

            // イベントステータス PLAY のラベル付け替え処理後の状態
            assertAll("初期状態ではイベントステータスは PLAY_MANUAL, ユーザーコントローラー は true になること",
                    () -> assertDoesNotThrow(
                            () -> {
                                Boolean tmp = eventStatus.relabelPlayerStatus();
                                assertTrue(tmp);
                                assertEquals(EventStatusValue.PLAY_MANUAL, eventStatus.getStatus());
                                assertTrue(eventStatus.canUserControll());
                            }));

            // 異常系のテスト
            assertAll("PLAYER以外のイベントステータスの時、メソッドからFalseが返され、値が変更されていないこと",
                    () -> {
                        // イベントステータスをJUDGEに設定する
                        eventStatus.set(EventStatusValue.JUDGE);
                        assertEquals(EventStatusValue.JUDGE, eventStatus.getStatus());
                        assertFalse(eventStatus.canUserControll());
                    },
                    () -> assertDoesNotThrow(
                            () -> {
                                Boolean tmp = eventStatus.relabelPlayerStatus();
                                assertFalse(tmp);
                                assertEquals(EventStatusValue.JUDGE, eventStatus.getStatus());
                                assertFalse(eventStatus.canUserControll());
                            }));
        }
    }
}
