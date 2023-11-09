package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import algorithm.AlgorithmType;
import gamerecord.GameRecord;
import reversi.Player;
import reversi.ResultType;
import reversi.Reversi;

class ResultDataTest {

    Player player1 = new Player("TEST1", true, AlgorithmType.MANUAL);
    Player player2 = new Player("TEST2", false, AlgorithmType.RANDOM);
    Reversi reversi = new Reversi(player1, player2);
    ResultData data = new ResultData(reversi, player1, player2, ResultType.DRAW, new GameRecord());

    @Test
    void testGetReversi() {
        assertSame(reversi, data.getReversi());
    }

    @Test
    void testGetPlayerBlack() {
        assertSame(player1, data.getPlayerBlack());
    }

    @Test
    void testGetPlayerWhite() {
        assertSame(player2, data.getPlayerWhite());
    }

    @Test
    void testGetResult() {
        assertEquals(ResultType.DRAW, data.getResult());
        assertSame(ResultType.DRAW, data.getResult());
    }

}
