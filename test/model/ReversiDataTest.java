package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import algorithm.AlgorithmType;
import reversi.Disc;
import reversi.Player;
import reversi.Reversi;

class ReversiDataTest {

    Player player1 = new Player("TEST1", Disc.BLACK, AlgorithmType.MANUAL);
    Player player2 = new Player("TEST2", Disc.WHITE, AlgorithmType.RANDOM);
    Reversi reversi = new Reversi(player1, player2);
    ReversiData data = new ReversiData(reversi, player1, player2, true);

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
    void testGetIsDebug() {
        assertTrue(data.getIsDebug());
    }

}
