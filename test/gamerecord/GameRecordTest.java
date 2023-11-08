package gamerecord;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import test.ReflectMember;

class GameRecordTest {

    // テスト対象クラスのインスタンス
    GameRecord record;
    
    @DisplayName("インスタンス生成直後の初期状態でのテスト")
    @Nested
    class Init {
        
        @Test
        void testGameRecord() {
            record = new GameRecord();

            ReflectMember reflClazz = new ReflectMember(GameRecord.class);
            Field reflRecordDataList = reflClazz.getField("recordDataList");
            Field reflPreviousBlackNum = reflClazz.getField("previousBlackNum");
            Field reflPreviousWhiteNum = reflClazz.getField("previousWhiteNum");
            Field reflComment = reflClazz.getField("comment");

            // private変数の初期値を確認
            assertAll("クラスのインスタンス生成時の初期値が正しい値であること",
                    () -> assertNotNull(reflRecordDataList.get(record)),
                    () -> assertEquals(2, (int) reflPreviousBlackNum.get(record)),
                    () -> assertEquals(2, (int) reflPreviousWhiteNum.get(record)),
                    () -> assertEquals("", (String) reflComment.get(record)),
                    () -> assertEquals(0, ((String) reflComment.get(record)).length()));
            
            assertAll("初期状態の棋譜リストのサイズが0であること",
                    () -> assertEquals(0, record.size()));
        }
    }

    @DisplayName("3手分の棋譜を追加した状態でのGetter系テスト")
    @Nested
    class TestGetter {

        @BeforeEach
        void setUp() throws Exception {
            record = new GameRecord();

            record.add(10, true, 3, 1, "a1");
            record.add(15, false, 0, 10, "b2");
            record.add(20, false, 10, 0, null);
        }

        @Test
        void testSize() {
            assertEquals(3, record.size());

            record.add(1, true, 1, 1, "a1");
            assertEquals(4, record.size());
        }

        @Test
        void testGetTurn() {
            assertEquals(10, record.getTurn(0));
            assertEquals(20, record.getTurn(2));

            // 範囲外のアクセス
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getTurn(3));
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getTurn(-1));
        }

        @Test
        void testGetPlayerString() {
            assertEquals("先手・黒", record.getPlayerString(0));
            assertEquals("後手・白", record.getPlayerString(2));

            // 範囲外のアクセス
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getPlayerString(3));
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getPlayerString(-1));
        }

        @Test
        void testGetDimString() {
            assertEquals("a1", record.getDimString(0));
            assertEquals("b2", record.getDimString(1));
            
            // NULLの場合、Skipに変換される
            assertEquals("Skip", record.getDimString(2));
            
            // 範囲外のアクセス
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getDimString(3));
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getDimString(-1));
        }

        @Test
        void testGetBlackDiscNum() {
            assertEquals(3, record.getBlackDiscNum(0));
            assertEquals(0, record.getBlackDiscNum(1));
            assertEquals(10, record.getBlackDiscNum(2));

            // 範囲外のアクセス
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getBlackDiscNum(3));
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getBlackDiscNum(-1));
        }
        @Test
        void testGetWhiteDiscNum() {
            assertEquals(1, record.getWhiteDiscNum(0));
            assertEquals(10, record.getWhiteDiscNum(1));
            assertEquals(0, record.getWhiteDiscNum(2));

            // 範囲外のアクセス
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getWhiteDiscNum(3));
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getWhiteDiscNum(-1));
        }

        @Test
        void testGetIncreaseBlackNum() {
            assertEquals(1, record.getIncreaseBlackNum(0));
            assertEquals(-3, record.getIncreaseBlackNum(1));
            assertEquals(10, record.getIncreaseBlackNum(2));

            // 範囲外のアクセス
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getIncreaseBlackNum(3));
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getIncreaseBlackNum(-1));
        }

        @Test
        void testGetIncreaseWhiteNum() {
            assertEquals(-1, record.getIncreaseWhiteNum(0));
            assertEquals(9, record.getIncreaseWhiteNum(1));
            assertEquals(-10, record.getIncreaseWhiteNum(2));

            // 範囲外のアクセス
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getIncreaseWhiteNum(3));
            assertThrows(IndexOutOfBoundsException.class,
                    () -> record.getIncreaseWhiteNum(-1));
        }
    }

    @DisplayName("Setterまたはリスト追加のテスト")
    @Nested
    class AddData {

        ReflectMember reflClazz;
        Field reflRecordDataList;

        @BeforeEach
        void setUp() throws Exception {
            record = new GameRecord();

            reflClazz = new ReflectMember(GameRecord.class);
            reflRecordDataList = reflClazz.getField("recordDataList");
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
    }
}
