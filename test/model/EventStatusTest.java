package model;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algorithm.AlgorithmType;
import reversi.Player;
import reversi.Reversi;
import test.ReflectMember;

class EventStatusTest {

    Player playerCom1, playerCom2, playerMaual1, playerMaual2;
    Reversi reversiCom, reversiManual;
    ReversiModel modelCom, modelManual;
    EventStatus eventStatusCom, eventStatusManual;
    
    ReflectMember reflclazz;
    Field reflReversi; 
    
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
        modelManual = new ReversiModel(new ReversiData(reversiManual, playerMaual1, playerMaual2, true), false);
        eventStatusManual = new EventStatus(reversiManual, EventStatusValue.PLAY);
        
        reflclazz = new ReflectMember(EventStatus.class);
        reflReversi = reflclazz.getField("reversi");
    }

    @Test
    void testEventStatus() {
        assertAll("プレイヤーがCOMの時、初期値が正しく設定されていること",
                () -> assertNotNull(eventStatusCom),
                () -> assertSame(reversiCom, (Reversi) reflReversi.get(eventStatusCom)),
                () -> assertNotSame(reversiManual, (Reversi) reflReversi.get(eventStatusCom)),
                () -> assertEquals(EventStatusValue.PLAY_COM, eventStatusCom.getStatus()),
                () -> assertFalse(eventStatusCom.canUserControll()),
                () -> assertEquals(EventStatusValue.PLAY_COM.getName(), eventStatusCom.getName()));
        
        assertAll("プレイヤーがマニュアルの時、初期値が正しく設定されていること",
                () -> assertNotNull(eventStatusManual),
                () -> assertSame(reversiManual, (Reversi) reflReversi.get(eventStatusManual)),
                () -> assertNotSame(reversiCom, (Reversi) reflReversi.get(eventStatusManual)),
                () -> assertEquals(EventStatusValue.PLAY_MANUAL, eventStatusManual.getStatus()),
                () -> assertTrue(eventStatusManual.canUserControll()),
                () -> assertEquals(EventStatusValue.PLAY_MANUAL.getName(), eventStatusManual.getName()));
    }

    @Test
    void testGetStatus() {
        assertEquals(EventStatusValue.PLAY_COM, eventStatusCom.getStatus());
        assertEquals(EventStatusValue.PLAY_MANUAL, eventStatusManual.getStatus());
    }

    @Test
    void testCanUserControll() {
        assertFalse(eventStatusCom.canUserControll());
        assertTrue(eventStatusManual.canUserControll());
    }

    @Test
    void testGetName() {
        assertEquals(EventStatusValue.PLAY_COM.getName(), eventStatusCom.getName());
        assertEquals(EventStatusValue.PLAY_MANUAL.getName(), eventStatusManual.getName());
    }

    @Test
    void testSetCom() {
        // COMの時のテスト
        eventStatusCom.set(EventStatusValue.JUDGE);

        assertAll("プレイヤーがCOMでイベントステータスを JUDGE にした時、ユーザーのコントロールが不可になる",
                () -> assertEquals(EventStatusValue.JUDGE, eventStatusCom.getStatus()),
                () -> assertFalse(eventStatusCom.canUserControll()),
                () -> assertEquals(EventStatusValue.JUDGE.getName(), eventStatusCom.getName()));

        eventStatusCom.set(EventStatusValue.PLAY);

        assertAll("プレイヤーがCOMでイベントステータスを PLAY にした時、ステータスは PLAY_COM になり、ユーザーのコントロールが不可になる",
                () -> assertEquals(EventStatusValue.PLAY_COM, eventStatusCom.getStatus()),
                () -> assertFalse(eventStatusCom.canUserControll()),
                () -> assertEquals(EventStatusValue.PLAY_COM.getName(), eventStatusCom.getName()));
    }
    
    @Test
    void testSetManual() {
        // マニュアル時のテスト
        eventStatusManual.set(EventStatusValue.JUDGE);

        assertAll("プレイヤーがマニュアルでイベントステータスを JUDGE にした時、ユーザーのコントロールが不可になる",
                () -> assertEquals(EventStatusValue.JUDGE, eventStatusManual.getStatus()),
                () -> assertFalse(eventStatusManual.canUserControll()),
                () -> assertEquals(EventStatusValue.JUDGE.getName(), eventStatusManual.getName()));

        eventStatusManual.set(EventStatusValue.PLAY);

        assertAll("プレイヤーがマニュアルでイベントステータスを PLAY にした時、ステータスは PLAY_MANUAL になり、ユーザーのコントロールが可能になる",
                () -> assertEquals(EventStatusValue.PLAY_MANUAL, eventStatusManual.getStatus()),
                () -> assertTrue(eventStatusManual.canUserControll()),
                () -> assertEquals(EventStatusValue.PLAY_MANUAL.getName(), eventStatusManual.getName()));
    }
}
