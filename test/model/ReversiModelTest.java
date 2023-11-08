package model;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algorithm.AlgorithmType;
import common.Global;
import reversi.Dimension;
import reversi.Player;
import reversi.ResultType;
import reversi.Reversi;
import test.ReflectMember;

class ReversiModelTest {

    Player playerCom1, playerCom2, playerMaual1, playerMaual2;
    Reversi reversiCom, reversiManual;
    ReversiModel modelCom, modelManual;

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

    Field reflEventStatus;
    Field reflResult;
    Field reflLatestTarget;
    
    @BeforeEach
    void setUp() throws Exception {
        playerCom1 = new Player("COM1", true, AlgorithmType.Random);
        playerCom2 = new Player("COM2", false, AlgorithmType.Random);
        reversiCom = new Reversi(playerCom1, playerCom2);
        modelCom = new ReversiModel(new ReversiData(reversiCom, playerCom1, playerCom2, true), true);

        playerMaual1 = new Player("MANUAL1", true, AlgorithmType.Manual);
        playerMaual2 = new Player("MANUAL2", false, AlgorithmType.Manual);
        reversiManual = new Reversi(playerMaual1, playerMaual2);
        modelManual = new ReversiModel(new ReversiData(reversiManual, playerMaual1, playerMaual2, true), false);

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

        reflEventStatus = reflClazz.getField("eventStatus");
        reflResult = reflClazz.getField("result");
        reflLatestTarget = reflClazz.getField("latestTarget");
    }

    @Test
    void testReversiModel() throws IllegalArgumentException, IllegalAccessException {
        EventStatus reflEventStatusCom = (EventStatus) reflEventStatus.get(modelCom);
        EventStatus reflEventStatusManual = (EventStatus) reflEventStatus.get(modelManual);
        
        assertAll("プレイヤーがCOM かつ GUI での動作想定で、初期値が想定通り設定できていること",
                () -> assertTrue((Boolean) reflIsDebug.get(modelCom)),
                () -> assertFalse((Boolean) reflIsFinish.get(modelCom)),
                () -> assertEquals(Global.WAIT_MILLISEC_INTERVAL, (int) reflWaitInterval.get(modelCom)),
                () -> assertEquals(24, (int) reflWaitFrame.get(modelCom)),
                () -> assertNull((String) reflStatusString.get(modelCom)),
                () -> assertEquals("デバッグ情報は特にありません", (String) reflDegugString.get(modelCom)),
                () -> assertEquals(EventStatusValue.WAIT, reflEventStatusCom.getStatus()),
                () -> assertEquals(EventStatusValue.WAIT.getName(), reflEventStatusCom.getName()),
                () -> assertFalse(reflEventStatusCom.canUserControll()),
                () -> assertNull((Dimension) reflLatestTarget.get(modelCom)),
                () -> assertEquals(ResultType.None, reflResult.get(modelCom)));

        assertAll("プレイヤーがCOM かつ GUI での動作想定で、getterで取得した値が初期値であること",
                () -> assertTrue(modelCom.isDebugMode()),
                () -> assertFalse(modelCom.canUserControll()),
                () -> assertFalse(modelCom.isGameFinish()),
                () -> assertNull(modelCom.getLatestTarget()),
                () -> assertEquals(EventStatusValue.WAIT.getName(), modelCom.getEventStatus()),
                () -> assertNull(modelCom.getGameStatusString()),
                () -> assertEquals("デバッグ情報は特にありません", modelCom.getDebugString()),
                () -> assertEquals(24, modelCom.getWaitFrame()));

        assertAll("プレイヤーが Manual かつ CUI での動作想定で、初期値が想定通り設定できていること",
                () -> assertTrue((Boolean) reflIsDebug.get(modelManual)),
                () -> assertFalse((Boolean) reflIsFinish.get(modelManual)),
                () -> assertEquals(0, (int) reflWaitInterval.get(modelManual)),
                () -> assertEquals(0, (int) reflWaitFrame.get(modelManual)),
                () -> assertNull((String) reflStatusString.get(modelManual)),
                () -> assertEquals("デバッグ情報は特にありません", (String) reflDegugString.get(modelManual)),
                () -> assertEquals(EventStatusValue.WAIT, reflEventStatusManual.getStatus()),
                () -> assertEquals(EventStatusValue.WAIT.getName(), reflEventStatusManual.getName()),
                () -> assertFalse(reflEventStatusManual.canUserControll()),
                () -> assertNull((Dimension) reflLatestTarget.get(modelCom)),
                () -> assertEquals(ResultType.None, reflResult.get(modelManual)));
    }

    @Test
    void testRun() {
        assertNull(modelCom.run());
        assertNull(modelManual.run());
    }

    @Test
    void testPut() {
        assertAll("置ける場所に設置した時、True を返す",
                () -> assertTrue(modelCom.put(new Dimension(3, 2))));

        assertAll("置けない場所に設置しようとした時、False を返す",
                () -> assertFalse(modelManual.put(new Dimension(1, 1))));

        assertAll("置ける場所の値がNULLの時、False を返してイベントステータスをSkipにする",
                () -> assertFalse(modelCom.put(null)),
                () -> assertEquals("出力された石の座標が NULL のため、プレイヤーの手はスキップとします", modelCom.getDebugString()),
                () -> assertEquals(EventStatusValue.SKIP, ((EventStatus) reflEventStatus.get(modelCom)).getStatus()),
                () -> assertEquals(Global.WAIT_MILLISEC_INTERVAL, (int) reflWaitInterval.get(modelCom)));
    }

    @Test
    void testJudge() throws IllegalAccessException, InvocationTargetException {
        refljudge.invoke(modelCom);
        assertEquals(EventStatusValue.WAIT.getName(), modelCom.getEventStatus());

        refljudge.invoke(modelManual);
        assertEquals(EventStatusValue.WAIT.getName(), modelManual.getEventStatus());
    }

    @Test
    void testSetWaitTime() throws IllegalAccessException, InvocationTargetException {
        reflSetWaitTime.invoke(modelCom, 1000);
        assertAll("プレイヤーがCOMで待機時間を1000ミリ秒とした時、waitFrameがFPSと同じ(30)になること",
                () -> assertEquals(30, (int) reflWaitFrame.get(modelCom)),
                () -> assertEquals(EventStatusValue.WAIT.getName(), modelCom.getEventStatus()));

        reflSetWaitTime.invoke(modelCom, 0);
        assertAll("プレイヤーがCOMで待機時間を0ミリ秒とした時、waitFrameが0になること",
                () -> assertEquals(0, (int) reflWaitFrame.get(modelCom)),
                () -> assertEquals(EventStatusValue.WAIT.getName(), modelCom.getEventStatus()));

        reflSetWaitTime.invoke(modelCom, 1234);
        assertAll("プレイヤーがCOMで待機時間を1234ミリ秒とした時、waitFrameが38(37.02の切り上げ)になること",
                () -> assertEquals(38, (int) reflWaitFrame.get(modelCom)),
                () -> assertEquals(EventStatusValue.WAIT.getName(), modelCom.getEventStatus()));

        reflSetWaitTime.invoke(modelCom, -100);
        assertAll("プレイヤーがCOMで待機時間に負の値を設定した時、例外が発生してwaitFrameが0になること",
                () -> assertEquals(0, (int) reflWaitFrame.get(modelCom)),
                () -> assertEquals(EventStatusValue.PLAY_COM.getName(), modelCom.getEventStatus()));

        // プレイヤーがマニュアルの場合
        reflSetWaitTime.invoke(modelManual, 345);
        assertAll("プレイヤーがCOMで待機時間を345ミリ秒とした時、waitFrameが 11 (10.35の切り上げ)になること",
                () -> assertEquals(11, (int) reflWaitFrame.get(modelManual)),
                () -> assertEquals(EventStatusValue.WAIT.getName(), modelManual.getEventStatus()));

        reflSetWaitTime.invoke(modelManual, -100);
        assertAll("プレイヤーがマニュアルで待機時間に負の値を設定した時、例外が発生してwaitFrameが0になること",
                () -> assertEquals(0, (int) reflWaitFrame.get(modelManual)),
                () -> assertEquals(EventStatusValue.PLAY_MANUAL.getName(), modelManual.getEventStatus()));

    }

    @Test
    void testExportForResult() {
        ResultData data = modelCom.exportForResult();

        assertSame(reversiCom, data.getReversi());
        assertSame(playerCom1, data.getPlayerBlack());
        assertSame(playerCom2, data.getPlayerWhite());
        assertEquals(ResultType.None, data.getResult());
        assertSame(ResultType.None, data.getResult());
    }
}