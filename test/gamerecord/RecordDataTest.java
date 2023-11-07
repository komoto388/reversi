package gamerecord;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RecordDataTest {

    @Test
    void testRecordData() {
        RecordData data = new RecordData(1, true, 12, 34, -10, 10, "a1");

        assertAll("正常系1",
                () -> assertEquals(1, data.turn),
                () -> assertEquals("先手・黒", data.playerString),
                () -> assertEquals(12, data.blackDiscNum),
                () -> assertEquals(34, data.whiteDiscNum),
                () -> assertEquals(-10, data.increaseBlackNum),
                () -> assertEquals(10, data.increaseWhiteNum),
                () -> assertEquals("a1", data.dimString));

        RecordData data2 = new RecordData(2, false, 34, 12, 0, 20, null);

        assertAll("正常系2",
                () -> assertEquals(2, data2.turn),
                () -> assertEquals("後手・白", data2.playerString),
                () -> assertEquals(34, data2.blackDiscNum),
                () -> assertEquals(12, data2.whiteDiscNum),
                () -> assertEquals(0, data2.increaseBlackNum),
                () -> assertEquals(20, data2.increaseWhiteNum),
                () -> assertNull(data2.dimString));
    }

    @Test
    void testRecordDataExceptionTurn() {
        assertAll("turn が0よりも大きい場合、例外にならない",
                () -> assertDoesNotThrow(() -> new RecordData(1, true, 1, 1, 1, 1, "a1")));

        assertAll("turn が0以下の場合、例外になる",
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new RecordData(0, true, 1, 1, 1, 1, "a1")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new RecordData(-10, true, 1, 1, 1, 1, "a1")));
    }

    @Test
    void testRecordDataExceptionIsPlayerBlack() {
        assertAll("isPlayerBlack が true, false の場合、例外にならない",
                () -> assertDoesNotThrow(() -> new RecordData(1, true, 1, 1, 1, 1, "a1")),
                () -> assertDoesNotThrow(() -> new RecordData(1, false, 1, 1, 1, 1, "a1")));

        assertAll("isPlayerBlack が null の場合、例外になる",
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new RecordData(1, null, 1, 1, 1, 1, "a1")));
    }

    @Test
    void testRecordDataExceptionBlackDiscNum() {
        assertAll("blackDiscNum が0以上の場合、例外にならない",
                () -> assertDoesNotThrow(() -> new RecordData(1, true, 0, 1, 1, 1, "a1")),
                () -> assertDoesNotThrow(() -> new RecordData(1, true, 10, 1, 1, 1, "a1")));

        assertAll("blackDiscNum が負の値の場合、例外になる",
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new RecordData(1, true, -1, 1, 1, 1, "a1")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new RecordData(1, true, -10, 1, 1, 1, "a1")));
    }

    @Test
    void testRecordDataExceptionWhiteDiscNum() {
        assertAll("whiteDiscNum が0以上の場合、例外にならない",
                () -> assertDoesNotThrow(() -> new RecordData(1, true, 1, 0, 1, 1, "a1")),
                () -> assertDoesNotThrow(() -> new RecordData(1, true, 1, 10, 1, 1, "a1")));

        assertAll("whiteDiscNum が負の値の場合、例外になる",
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new RecordData(1, true, 1, -1, 1, 1, "a1")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new RecordData(1, true, 1, -10, 1, 1, "a1")));
    }
}
