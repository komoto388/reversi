package model;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import algorithm.AlgorithmType;
import common.Global;
import reversi.Dimension;
import reversi.Disc;
import reversi.Player;
import reversi.ResultType;
import reversi.Reversi;
import test.ReflectMember;

class ReversiModelTest {

    @DisplayName("COM, GUI環境での初期値テスト")
    @Nested
    class TestInitializetComGui {
        Player player1, player2;
        Reversi reversi;
        ReversiModel model;

        // テスト対象クラスの Private メンバを操作するインスタンス
        ReflectMember reflClazz;

        // リフレクションされた Private メソッド
        Method refljudge;
        Method reflSetWaitTime;

        // リフレクションされた Private フィールド
        Field reflWaitInterval;
        Field reflWaitFrame;
        Field reflIsDebug;
        Field reflIsFinish;
        Field reflStatusString;
        Field reflDegugString;

        Field reflEventStatusField;
        Field reflResult;
        Field reflLatestTarget;

        @BeforeEach
        void setUp() throws Exception {
            player1 = new Player("COM1", Disc.BLACK, AlgorithmType.RANDOM);
            player2 = new Player("COM2", Disc.WHITE, AlgorithmType.RANDOM);
            reversi = new Reversi(player1, player2);
            model = new ReversiModel(new ReversiData(reversi, player1, player2, true), true);

            // Private メンバの取得
            reflClazz = new ReflectMember(ReversiModel.class);

            reflWaitInterval = reflClazz.getField("waitInterval");
            reflWaitFrame = reflClazz.getField("waitFrame");
            reflIsDebug = reflClazz.getField("isDebug");
            reflIsFinish = reflClazz.getField("isFinish");
            reflStatusString = reflClazz.getField("statusString");
            reflDegugString = reflClazz.getField("debugString");

            refljudge = reflClazz.getMethod("judge");
            reflSetWaitTime = reflClazz.getMethod("setWaitTime", int.class);

            reflEventStatusField = reflClazz.getField("eventStatus");
            reflResult = reflClazz.getField("result");
            reflLatestTarget = reflClazz.getField("latestTarget");
        }

        @Test
        void testReversiModel() throws IllegalArgumentException, IllegalAccessException {
            EventStatus reflEventStatus = (EventStatus) reflEventStatusField.get(model);

            assertAll("初期値が想定通り設定できていること",
                    () -> assertTrue((Boolean) reflIsDebug.get(model)),
                    () -> assertFalse((Boolean) reflIsFinish.get(model)),
                    () -> assertEquals(Global.WAIT_MILLISEC_INTERVAL, (int) reflWaitInterval.get(model)),
                    () -> assertEquals(24, (int) reflWaitFrame.get(model)),
                    () -> assertNull((String) reflStatusString.get(model)),
                    () -> assertEquals("デバッグ情報は特にありません", (String) reflDegugString.get(model)),
                    () -> assertEquals(EventStatusValue.PLAY, reflEventStatus.getStatus()),
                    () -> assertEquals(EventStatusValue.PLAY.getName(), reflEventStatus.getName()),
                    () -> assertFalse(reflEventStatus.canUserControll()),
                    () -> assertNull((Dimension) reflLatestTarget.get(model)),
                    () -> assertEquals(ResultType.NONE, reflResult.get(model)));
        }

        void testGetter() {
            assertAll("getterが初期値を取得できていること",
                    () -> assertTrue(model.isDebugMode()),
                    () -> assertFalse(model.canUserControll()),
                    () -> assertFalse(model.isGameFinish()),
                    () -> assertNull(model.getLatestTarget()),
                    () -> assertEquals(EventStatusValue.PLAY.getName(), model.getEventStatus()),
                    () -> assertNull(model.getGameStatusString()),
                    () -> assertEquals("デバッグ情報は特にありません", model.getDebugString()),
                    () -> assertEquals(24, model.getWaitFrame()));
        }
    }

    @DisplayName("マニュアル, CUI環境での初期値テスト")
    @Nested
    class TestInitializetManualNogui {
        Player player1, player2;
        Reversi reversi;
        ReversiModel model;

        // テスト対象クラスの Private メンバを操作するインスタンス
        ReflectMember reflClazz;

        // リフレクションされた Private メソッド
        Method refljudge;
        Method reflSetWaitTime;

        // リフレクションされた Private フィールド
        Field reflWaitInterval;
        Field reflWaitFrame;
        Field reflIsDebug;
        Field reflIsFinish;
        Field reflStatusString;
        Field reflDegugString;

        Field reflEventStatusField;
        Field reflResult;
        Field reflLatestTarget;

        @BeforeEach
        void setUp() throws Exception {
            player1 = new Player("COM1", Disc.BLACK, AlgorithmType.MANUAL);
            player2 = new Player("COM2", Disc.WHITE, AlgorithmType.MANUAL);
            reversi = new Reversi(player1, player2);
            model = new ReversiModel(new ReversiData(reversi, player1, player2, true), false);

            // Private メンバの取得
            reflClazz = new ReflectMember(ReversiModel.class);

            reflWaitInterval = reflClazz.getField("waitInterval");
            reflWaitFrame = reflClazz.getField("waitFrame");
            reflIsDebug = reflClazz.getField("isDebug");
            reflIsFinish = reflClazz.getField("isFinish");
            reflStatusString = reflClazz.getField("statusString");
            reflDegugString = reflClazz.getField("debugString");

            refljudge = reflClazz.getMethod("judge");
            reflSetWaitTime = reflClazz.getMethod("setWaitTime", int.class);

            reflEventStatusField = reflClazz.getField("eventStatus");
            reflResult = reflClazz.getField("result");
            reflLatestTarget = reflClazz.getField("latestTarget");
        }

        @Test
        void testReversiModel() throws IllegalArgumentException, IllegalAccessException {
            EventStatus reflEventStatus = (EventStatus) reflEventStatusField.get(model);

            assertAll("初期値が想定通り設定できていること",
                    () -> assertTrue((Boolean) reflIsDebug.get(model)),
                    () -> assertFalse((Boolean) reflIsFinish.get(model)),
                    () -> assertEquals(0, (int) reflWaitInterval.get(model)),
                    () -> assertEquals(0, (int) reflWaitFrame.get(model)),
                    () -> assertNull((String) reflStatusString.get(model)),
                    () -> assertEquals("デバッグ情報は特にありません", (String) reflDegugString.get(model)),
                    () -> assertEquals(EventStatusValue.PLAY, reflEventStatus.getStatus()),
                    () -> assertEquals(EventStatusValue.PLAY.getName(), reflEventStatus.getName()),
                    () -> assertFalse(reflEventStatus.canUserControll()),
                    () -> assertNull((Dimension) reflLatestTarget.get(model)),
                    () -> assertEquals(ResultType.NONE, reflResult.get(model)));
        }

        void testGetter() {
            assertAll("getterが初期値を取得できていること",
                    () -> assertTrue(model.isDebugMode()),
                    () -> assertFalse(model.canUserControll()),
                    () -> assertFalse(model.isGameFinish()),
                    () -> assertNull(model.getLatestTarget()),
                    () -> assertEquals(EventStatusValue.PLAY.getName(), model.getEventStatus()),
                    () -> assertNull(model.getGameStatusString()),
                    () -> assertEquals("デバッグ情報は特にありません", model.getDebugString()),
                    () -> assertEquals(24, model.getWaitFrame()));
        }
    }

    @DisplayName("COM, GUI環境での動作テスト")
    @Nested
    class TestComGui {
        Player player1, player2;
        Reversi reversi;
        ReversiModel model;

        // テスト対象クラスの Private メンバを操作するインスタンス
        ReflectMember reflClazz;

        // リフレクションされた Private メソッド
        Method refljudge;
        Method reflSetWaitTime;

        // リフレクションされた Private フィールド
        Field reflWaitInterval;
        Field reflWaitFrame;
        Field reflEventStatusField;

        @BeforeEach
        void setUp() throws Exception {
            player1 = new Player("COM1", Disc.BLACK, AlgorithmType.RANDOM);
            player2 = new Player("COM2", Disc.WHITE, AlgorithmType.RANDOM);
            reversi = new Reversi(player1, player2);
            model = new ReversiModel(new ReversiData(reversi, player1, player2, true), true);

            // Private メンバの取得
            reflClazz = new ReflectMember(ReversiModel.class);

            reflWaitInterval = reflClazz.getField("waitInterval");
            reflWaitFrame = reflClazz.getField("waitFrame");
            refljudge = reflClazz.getMethod("judge");
            reflSetWaitTime = reflClazz.getMethod("setWaitTime", int.class);
            reflEventStatusField = reflClazz.getField("eventStatus");
        }

        @Test
        void testRun() {
            fail("未実装です");
        }

        @Test
        void testPut() {
            assertAll("置ける場所に設置した時、True を返す",
                    () -> assertTrue(model.put(new Dimension(3, 2))));

            assertAll("置けない場所に設置しようとした時、False を返す",
                    () -> assertFalse(model.put(new Dimension(1, 1))));

            assertAll("置ける場所の値がNULLの時、False を返してイベントステータスをSkipにする",
                    () -> assertFalse(model.put(null)),
                    () -> assertEquals("プレイヤーが石を置く座標を NULL と指定したのため、プレイヤーはスキップしたと判断します", model.getDebugString()),
                    () -> assertEquals(EventStatusValue.SKIP,
                            ((EventStatus) reflEventStatusField.get(model)).getStatus()),
                    () -> assertEquals(Global.WAIT_MILLISEC_INTERVAL, (int) reflWaitInterval.get(model)));
        }

        @Test
        void testJudge() throws IllegalAccessException, InvocationTargetException {
            refljudge.invoke(model);
            assertEquals(EventStatusValue.PLAY.getName() + " (待機中)", model.getEventStatus());
        }

        @Test
        void testSetWaitTime() throws IllegalAccessException, InvocationTargetException {
            assertAll("待機時間を1000ミリ秒とした時、waitFrameがFPSと同じ(30)になること",
                    () -> assertDoesNotThrow(() ->  reflSetWaitTime.invoke(model, 1000)),
                    () -> assertEquals(30, (int) reflWaitFrame.get(model)),
                    () -> assertEquals(EventStatusValue.PLAY.getName() + " (待機中)", model.getEventStatus()));

            assertAll("待機時間を0ミリ秒とした時、waitFrameが0になること",
                    () -> assertDoesNotThrow(() ->  reflSetWaitTime.invoke(model, 0)),
                    () -> assertEquals(0, (int) reflWaitFrame.get(model)),
                    () -> assertEquals(EventStatusValue.PLAY.getName(), model.getEventStatus()));

           
            assertAll("待機時間を1234ミリ秒とした時、waitFrameが38(37.02の切り上げ)になること",
                    () -> assertDoesNotThrow(() ->  reflSetWaitTime.invoke(model, 1234)),
                    () -> assertEquals(38, (int) reflWaitFrame.get(model)),
                    () -> assertEquals(EventStatusValue.PLAY.getName() + " (待機中)", model.getEventStatus()));

            reflSetWaitTime.invoke(model, -100);
            assertAll("待機時間に負の値を設定した時、waitFrameが0になること",
                    () -> assertDoesNotThrow(() -> reflSetWaitTime.invoke(model, -100)),
                    () -> assertEquals(0, (int) reflWaitFrame.get(model)),
                    () -> assertEquals(EventStatusValue.PLAY.getName(), model.getEventStatus()));
        }

        @Test
        void testExportForResult() {
            ResultData data = model.exportForResult();

            assertSame(reversi, data.getReversi());
            assertSame(player1, data.getPlayerBlack());
            assertSame(player2, data.getPlayerWhite());
            assertEquals(ResultType.NONE, data.getResult());
            assertSame(ResultType.NONE, data.getResult());
        }
    }

    @DisplayName("マニュアル, GUI環境での動作テスト")
    @Nested
    class TestManualNogui {
        Player player1, player2;
        Reversi reversi;
        ReversiModel model;

        // テスト対象クラスの Private メンバを操作するインスタンス
        ReflectMember reflClazz;

        // リフレクションされた Private メソッド
        Method refljudge;
        Method reflSetWaitTime;

        // リフレクションされた Private フィールド
        Field reflWaitInterval;
        Field reflWaitFrame;
        Field reflEventStatusField;

        @BeforeEach
        void setUp() throws Exception {
            player1 = new Player("Manual1", Disc.BLACK, AlgorithmType.MANUAL);
            player2 = new Player("Manual2", Disc.WHITE, AlgorithmType.MANUAL);
            reversi = new Reversi(player1, player2);
            model = new ReversiModel(new ReversiData(reversi, player1, player2, true), false);

            // Private メンバの取得
            reflClazz = new ReflectMember(ReversiModel.class);

            reflWaitInterval = reflClazz.getField("waitInterval");
            reflWaitFrame = reflClazz.getField("waitFrame");
            refljudge = reflClazz.getMethod("judge");
            reflSetWaitTime = reflClazz.getMethod("setWaitTime", int.class);
            reflEventStatusField = reflClazz.getField("eventStatus");
        }

        @Test
        void testRun() {
            fail("未実装です");
        }

        @Test
        void testPut() {
            assertAll("置ける場所に設置した時、True を返す",
                    () -> assertTrue(model.put(new Dimension(3, 2))));

            assertAll("置けない場所に設置しようとした時、False を返す",
                    () -> assertFalse(model.put(new Dimension(1, 1))));

            assertAll("置ける場所の値がNULLの時、False を返してイベントステータスをSkipにする",
                    () -> assertFalse(model.put(null)),
                    () -> assertEquals("出力された石の座標が NULL のため、プレイヤーの手はスキップとします", model.getDebugString()),
                    () -> assertEquals(EventStatusValue.SKIP,
                            ((EventStatus) reflEventStatusField.get(model)).getStatus()),
                    () -> assertEquals(0, (int) reflWaitInterval.get(model)));
        }

        @Test
        void testJudge() throws IllegalAccessException, InvocationTargetException {
            refljudge.invoke(model);
            assertEquals(EventStatusValue.PLAY.getName(), model.getEventStatus());
        }

        @Test
        void testSetWaitTime() throws IllegalAccessException, InvocationTargetException {
            assertAll("待機時間を1000ミリ秒とした時、waitFrameがFPSと同じ(30)になること",
                    () -> assertDoesNotThrow(() ->  reflSetWaitTime.invoke(model, 1000)),
                    () -> assertEquals(30, (int) reflWaitFrame.get(model)),
                    () -> assertEquals(EventStatusValue.PLAY.getName() + " (待機中)", model.getEventStatus()));

            assertAll("待機時間を0ミリ秒とした時、waitFrameが0になること",
                    () -> assertDoesNotThrow(() ->  reflSetWaitTime.invoke(model, 0)),
                    () -> assertEquals(0, (int) reflWaitFrame.get(model)),
                    () -> assertEquals(EventStatusValue.PLAY.getName(), model.getEventStatus()));

           
            assertAll("待機時間を345ミリ秒とした時、waitFrameが 11 (10.35の切り上げ)になること",
                    () -> assertDoesNotThrow(() ->  reflSetWaitTime.invoke(model, 345)),
                    () -> assertEquals(11, (int) reflWaitFrame.get(model)),
                    () -> assertEquals(EventStatusValue.PLAY.getName() + " (待機中)", model.getEventStatus()));

            reflSetWaitTime.invoke(model, -100);
            assertAll("待機時間に負の値を設定した時、waitFrameが0になること",
                    () -> assertDoesNotThrow(() -> reflSetWaitTime.invoke(model, -100)),
                    () -> assertEquals(0, (int) reflWaitFrame.get(model)),
                    () -> assertEquals(EventStatusValue.PLAY.getName(), model.getEventStatus()));
        }

        @Test
        void testExportForResult() {
            ResultData data = model.exportForResult();

            assertSame(reversi, data.getReversi());
            assertSame(player1, data.getPlayerBlack());
            assertSame(player2, data.getPlayerWhite());
            assertEquals(ResultType.NONE, data.getResult());
            assertSame(ResultType.NONE, data.getResult());
        }
    }
}
