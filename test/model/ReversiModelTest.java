package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algorithm.AlgorithmType;
import common.Global;
import reversi.Player;
import reversi.ResultType;
import reversi.Reversi;

class ReversiModelTest {

    Player player1, player2;
    Reversi reversi;
    ReversiModel model;
    EventStatus eventStatus;

    @BeforeEach
    void setUp() throws Exception {
        player1 = new Player("TEST1", true, AlgorithmType.Manual);
        player2 = new Player("TEST2", false, AlgorithmType.Random);
        reversi = new Reversi(player1, player2);
        model = new ReversiModel(new ReversiData(reversi, player1, player2, true));
    }

    @Test
    void testGetReversi() {
        assertSame(reversi, model.getReversi());
    }

    @Test
    void testGetBoard() {
        assertSame(reversi.getBoard(), model.getBoard());
    }

    @Test
    void testGetBoardSize() {
        assertSame(reversi.getBoard().getSize(), model.getBoardSize());
    }

    @Test
    void testGetPlayerBlack() {
        assertSame(player1, model.getPlayerBlack());
    }

    @Test
    void testGetPlayerWhite() {
        assertSame(player2, model.getPlayerWhite());
    }

    @Test
    void testGetIsDebug() {
        assertTrue(model.getIsDebug());
    }

    @Test
    void testGetIsControll() {
        assertFalse(model.getIsControll());
    }

    @Test
    void testGetIsFinish() {
        assertFalse(model.getIsFinish());
    }

    @Test
    void testGetLatestTarget() {
        assertNull(model.getLatestTarget());
    }

    @Test
    void testGetEventStatus() {
        assertEquals(EventStatusValue.WAIT.getName(), model.getEventStatus());
    }

    @Test
    void testGetGameStatusString() {
        assertNull(model.getGameStatusString());
    }

    @Test
    void testGetDebugString() {
        assertEquals("デバッグ情報は特にありません", model.getDebugString());
    }

    @Test
    void testGetWaitFrame() {
        // デフォルト： 800ミリ秒待つ, FPS=30
        assertEquals(24, model.getWaitFrame());
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
    void testGenerateData() {
        ResultData data = model.generateData();

        assertSame(reversi, data.getReversi());
        assertSame(player1, data.getPlayerBlack());
        assertSame(player2, data.getPlayerWhite());
        assertEquals(ResultType.None, data.getResult());
        assertSame(ResultType.None, data.getResult());
    }

}
