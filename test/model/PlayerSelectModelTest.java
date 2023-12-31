package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algorithm.AlgorithmType;
import common.Global;
import reversi.Disc;

class PlayerSelectModelTest {

    PlayerSelectModel model;

    @BeforeEach
    void setUp() throws Exception {
        model = new PlayerSelectModel();
    }

    @Test
    void testGetName() {
        assertEquals(Global.DEFAULT_PLAYER_NAME_BLACK, model.getName(true));
        assertEquals(Global.DEFAULT_PLAYER_NAME_WHITE, model.getName(false));
    }

    @Test
    void testGetAlgorithmType() {
        assertEquals(AlgorithmType.MANUAL, model.getAlgorithmType(true));
        assertEquals(AlgorithmType.MANUAL, model.getAlgorithmType(false));
    }

    @Test
    void testGetIsDebug() {
        assertTrue(model.getIsDebug());
    }

    @Test
    void testSetPlayerName() {
        assertFalse(model.setPlayerName(true, null));
        assertFalse(model.setPlayerName(false, null));
        assertFalse(model.setPlayerName(true, ""));
        assertFalse(model.setPlayerName(false, ""));

        assertAll("プレイヤー黒に名前を設定する",
                () -> assertTrue(model.setPlayerName(true, "あいうえお")),
                () -> assertEquals("あいうえお", model.getName(true)));

        assertAll("プレイヤー白に名前を設定する",
                () -> assertTrue(model.setPlayerName(false, "かきくけこ")),
                () -> assertEquals("かきくけこ", model.getName(false)));
    }

    @Test
    void testSetPlayerAlgorithm() {
        assertFalse(model.setPlayerAlgorithm(Disc.BLACK, null));
        assertFalse(model.setPlayerAlgorithm(Disc.WHITE, null));

        assertAll("プレイヤー黒にアルゴリズム種別を設定する",
                () -> assertTrue(model.setPlayerAlgorithm(Disc.BLACK, AlgorithmType.RANDOM)),
                () -> assertEquals(AlgorithmType.RANDOM, model.getAlgorithmType(true)));

        assertAll("プレイヤー黒にアルゴリズム種別を設定する",
                () -> assertTrue(model.setPlayerAlgorithm(Disc.WHITE, AlgorithmType.ORIGINAL_01)),
                () -> assertEquals(AlgorithmType.ORIGINAL_01, model.getAlgorithmType(false)));
    }

    @Test
    void testSetIsDebug() {
        assertTrue(model.getIsDebug());

        model.setIsDebug(false);
        assertFalse(model.getIsDebug());

        model.setIsDebug(true);
        assertTrue(model.getIsDebug());
    }

    @Test
    void testExportForReversi() {
        ReversiData data = model.exportForReversi();

        assertNotNull(data);
        assertNotNull(data.getReversi());
        assertNotNull(data.getPlayerBlack());
        assertNotNull(data.getPlayerWhite());

        assertEquals(Global.DEFAULT_PLAYER_NAME_BLACK, data.getPlayerBlack().getName());
        assertEquals(Global.DEFAULT_PLAYER_NAME_WHITE, data.getPlayerWhite().getName());
    }
}
