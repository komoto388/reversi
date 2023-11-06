package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algorithm.AlgorithmType;
import reversi.Player;
import reversi.ResultType;
import reversi.Reversi;

class ReversiModelTest {

    Player playerCom1, playerCom2, playerMaual1, playerMaual2;
    Reversi reversiCom, reversiManual;
    ReversiModel modelCom, modelManual;
    EventStatus eventStatusCom, eventStatusManual;

    @BeforeEach
    void setUp() throws Exception {
        playerCom1 = new Player("COM1", true, AlgorithmType.Random);
        playerCom2 = new Player("COM2", false, AlgorithmType.Random);
        reversiCom = new Reversi(playerCom1, playerCom2);
        modelCom = new ReversiModel(new ReversiData(reversiCom, playerCom1, playerCom2, true), true);
        eventStatusCom = new EventStatus(reversiCom, EventStatusValue.PLAY);

        playerMaual1 = new Player("MANUAL1", true, AlgorithmType.Manual);
        playerMaual2 = new Player("MANUAL2", false, AlgorithmType.Manual);
        reversiManual = new Reversi(playerMaual1, playerMaual2);
        modelManual = new ReversiModel(new ReversiData(reversiManual, playerMaual1, playerMaual2, true), true);
        eventStatusManual = new EventStatus(reversiManual, EventStatusValue.PLAY);
    }

    @Test
    void testGetIsDebug() {
        assertTrue(modelCom.getIsDebug());
    }

    @Test
    void testGetIsControll() {
        assertFalse(modelCom.getIsControll());
    }

    @Test
    void testGetIsFinish() {
        assertFalse(modelCom.getIsFinish());
    }

    @Test
    void testGetLatestTarget() {
        assertNull(modelCom.getLatestTarget());
    }

    @Test
    void testGetEventStatus() {
        assertEquals(EventStatusValue.WAIT.getName(), modelCom.getEventStatus());
    }

    @Test
    void testGetGameStatusString() {
        assertNull(modelCom.getGameStatusString());
    }

    @Test
    void testGetDebugString() {
        assertEquals("デバッグ情報は特にありません", modelCom.getDebugString());
    }

    @Test
    void testGetWaitFrame() {
        // デフォルト： 800ミリ秒待つ, FPS=30
        assertEquals(24, modelCom.getWaitFrame());
    }

    @Test
    void testRun() {
        fail("まだ実装されていません");
    }

    @Test
    void testPut() {
        fail("まだ実装されていません");
    }

    @Test
    void testJudge() {
        fail("まだ実装されていません");
    }

    @Test
    void testSetWaitTime() {
        fail("まだ実装されていません");
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

    /*
     * EventStatus クラス
     */
    @Test
    void testEventStatusGetStatus() {
        assertEquals(EventStatusValue.PLAY_COM, eventStatusCom.getStatus());
        assertEquals(EventStatusValue.PLAY_MANUAL, eventStatusManual.getStatus());
    }

    @Test
    void testEventStatusGetIsControll() {
        assertFalse(eventStatusCom.getIsControll());
        assertTrue(eventStatusManual.getIsControll());
    }

    @Test
    void testEventStatusGetName() {
        assertEquals(EventStatusValue.PLAY_COM.getName(), eventStatusCom.getName());
        assertEquals(EventStatusValue.PLAY_MANUAL.getName(), eventStatusManual.getName());
    }

    @Test
    void testEventStatusSet() {
        // COMの時のテスト
        eventStatusCom.set(EventStatusValue.JUDGE);

        assertAll("プレイヤーがCOMでイベントステータスを JUDGE にした時、ユーザーのコントロールが不可になる",
                () -> assertEquals(EventStatusValue.JUDGE, eventStatusCom.getStatus()),
                () -> assertFalse(eventStatusCom.getIsControll()),
                () -> assertEquals(EventStatusValue.JUDGE.getName(), eventStatusCom.getName()));

        eventStatusCom.set(EventStatusValue.PLAY);

        assertAll("プレイヤーがCOMでイベントステータスを PLAY にした時、ステータスは PLAY_COM になり、ユーザーのコントロールが不可になる",
                () -> assertEquals(EventStatusValue.PLAY_COM, eventStatusCom.getStatus()),
                () -> assertFalse(eventStatusCom.getIsControll()),
                () -> assertEquals(EventStatusValue.PLAY_COM.getName(), eventStatusCom.getName()));

        // マニュアル時のテスト
        eventStatusManual.set(EventStatusValue.JUDGE);

        assertAll("プレイヤーがマニュアルでイベントステータスを JUDGE にした時、ユーザーのコントロールが不可になる",
                () -> assertEquals(EventStatusValue.JUDGE, eventStatusManual.getStatus()),
                () -> assertFalse(eventStatusManual.getIsControll()),
                () -> assertEquals(EventStatusValue.JUDGE.getName(), eventStatusManual.getName()));

        eventStatusManual.set(EventStatusValue.PLAY);

        assertAll("プレイヤーがマニュアルでイベントステータスを PLAY にした時、ステータスは PLAY_MANUAL になり、ユーザーのコントロールが可能になる",
                () -> assertEquals(EventStatusValue.PLAY_MANUAL, eventStatusManual.getStatus()),
                () -> assertTrue(eventStatusManual.getIsControll()),
                () -> assertEquals(EventStatusValue.PLAY_MANUAL.getName(), eventStatusManual.getName()));
    }
}
