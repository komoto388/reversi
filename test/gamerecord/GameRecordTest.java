package gamerecord;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.aggregator.ArgumentAccessException;

import test.ReflectMember;

class GameRecordTest {

    // テスト対象クラスのインスタンス
    GameRecord record;

    // テスト対象クラスの Private メンバを操作するインスタンス
    ReflectMember reflClazz;

    // リフレクションされた Private メソッド

    // リフレクションされた Private フィールド
    Field reflRecordDataList;
    Field reflPreviousBlackNum;
    Field reflPreviousWhiteNum;
    Field reflComment;

    @BeforeEach
    void setUp() throws Exception {
        record = new GameRecord();

        reflClazz = new ReflectMember(GameRecord.class);
        reflRecordDataList = reflClazz.getField("recordDataList");
        reflPreviousBlackNum = reflClazz.getField("previousBlackNum");
        reflPreviousWhiteNum = reflClazz.getField("previousWhiteNum");
        reflComment = reflClazz.getField("comment");
    }

    @Test
    void testGameRecord()
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

        // private変数の初期値を確認
        assertAll("クラスのインスタンス生成時の初期値が正しい値である",
                () -> assertNotNull(reflRecordDataList.get(record)),
                () -> assertEquals(2, (int) reflPreviousBlackNum.get(record)),
                () -> assertEquals(2, (int) reflPreviousWhiteNum.get(record)),
                () -> assertEquals("", (String) reflComment.get(record)),
                () -> assertEquals(0, ((String) reflComment.get(record)).length()));
    }

    @Test
    void testComment() {
        assertAll("棋譜生成時の初期値が正しく設定されている",
                () -> assertEquals("", record.getComment()),
                () -> assertEquals(0, record.getComment().length()));

        String str = "これはテストです";
        record.setComment(str);

        assertAll("変更した値が正しく設定されている",
                () -> assertEquals(str, record.getComment()));
    }

    @Test
    void testAdd() throws IllegalArgumentException, IllegalAccessException {

        record.add(1, true, 1, 1, "a1");

        @SuppressWarnings("unchecked")
        List<RecordData> recordDataList = (List<RecordData>) reflRecordDataList.get(record);

        // 通常値のデータ追加
        recordDataList.get(0);
        assertAll("データを追加処理で、棋譜に正しくデータが追加されているか",
                () -> assertEquals(1, record.size()),
                () -> assertNotNull(recordDataList.get(0)),
                () -> assertEquals(1, recordDataList.get(0).turn),
                () -> assertEquals("先手・黒", recordDataList.get(0).playerString),
                () -> assertEquals("a1", recordDataList.get(0).dimString),
                () -> assertEquals(1, recordDataList.get(0).blackDiscNum),
                () -> assertEquals(1, recordDataList.get(0).whiteDiscNum),
                () -> assertEquals(-1, recordDataList.get(0).increaseBlackNum),
                () -> assertEquals(-1, recordDataList.get(0).increaseWhiteNum));

        // 異常値データの追加（例外処理の発生）
        record.add(2, null, 1, 1, "b2");
        assertAll("データを追加処理で、例外が発生した場合はリストが追加されない",
                () -> assertEquals(1, record.size()));
    }

    @Test
    void testAddAsSkip() throws IllegalArgumentException, IllegalAccessException {
        record.addAsSkip(1, true, 1, 1);

        @SuppressWarnings("unchecked")
        List<RecordData> recordDataList = (List<RecordData>) reflRecordDataList.get(record);

        // 通常値のデータ追加
        recordDataList.get(0);
        assertAll("データを追加処理で、棋譜に正しくデータが追加されているか",
                () -> assertEquals(1, record.size()),
                () -> assertNotNull(recordDataList.get(0)),
                () -> assertEquals(1, recordDataList.get(0).turn),
                () -> assertEquals("先手・黒", recordDataList.get(0).playerString),
                () -> assertNull(recordDataList.get(0).dimString),
                () -> assertEquals(1, recordDataList.get(0).blackDiscNum),
                () -> assertEquals(1, recordDataList.get(0).whiteDiscNum),
                () -> assertEquals(-1, recordDataList.get(0).increaseBlackNum),
                () -> assertEquals(-1, recordDataList.get(0).increaseWhiteNum));
    }

    @Test
    void testSize() {
        // 初期値は0
        assertEquals(0, record.size());

        // リストにデータを追加する度にサイズが増える
        record.add(1, true, 1, 1, "a1");
        assertEquals(1, record.size());

        record.add(2, false, 1, 1, "b2");
        assertEquals(2, record.size());
    }

    @Test
    void testGetTurn() {
        record.add(10, true, 1, 2, "a1");
        record.add(20, false, 1, 3, "b2");

        assertEquals(10, record.getTurn(0));
        assertEquals(20, record.getTurn(1));

        // 範囲外のアクセス
        assertThrows(ArgumentAccessException.class,
                () -> record.getTurn(10));
        fail("まだ実装されていません");
    }

    @Test
    void testGetPlayerString() {
        record.add(1, true, 1, 1, "a1");
        record.add(2, false, 1, 1, "b2");

        assertEquals("先手・黒", record.getPlayerString(0));
        assertEquals("後手・白", record.getPlayerString(1));
    }

    @Test
    void testGetDimString() {
        record.add(1, true, 1, 1, "a1");
        record.add(2, false, 1, 1, "foo");

        assertEquals("a1", record.getDimString(0));
        assertEquals("foo", record.getDimString(1));
    }

    @Test
    void testGetBlackDiscNum() {
        record.add(1, true, 10, 1, "a1");
        record.add(2, false, 20, 1, "b2");

        assertEquals(10, record.getBlackDiscNum(0));
        assertEquals(20, record.getBlackDiscNum(1));
    }

    @Test
    void testGetWhiteDiscNum() {
        record.add(1, true, 1, 10, "a1");
        record.add(2, false, 1, 20, "b2");

        assertEquals(10, record.getWhiteDiscNum(0));
        assertEquals(20, record.getWhiteDiscNum(1));
    }

    @Test
    void testGetIncreaseBlackNum() {
        record.add(1, true, 10, 1, "a1");
        record.add(2, false, 5, 1, "b2");

        assertEquals(8, record.getIncreaseBlackNum(0));
        assertEquals(-5, record.getIncreaseBlackNum(1));
    }

    @Test
    void testGetIncreaseWhiteNum() {
        record.add(1, true, 1, 10, "a1");
        record.add(2, false, 1, 5, "b2");

        assertEquals(8, record.getIncreaseWhiteNum(0));
        assertEquals(-5, record.getIncreaseWhiteNum(1));
    }

}
