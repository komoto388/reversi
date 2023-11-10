package reversi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DiscTest {

    Disc discBlack = Disc.BLACK;
    Disc discWhite = Disc.WHITE;

    @Test
    void testGetName() {
        assertEquals("黒", discBlack.getName());
        assertEquals("白", discWhite.getName());
    }

    @Test
    void testGetPrefixForPlayerName() {
        assertEquals("先手・黒", discBlack.getPrefixForPlayerName());
        assertEquals("後手・白", discWhite.getPrefixForPlayerName());
    }

    @Test
    void testNext() {
        // 「白」の場合、次の要素の「黒」になる
        assertEquals(Disc.WHITE, discBlack.next());

        // 「黒」の場合、次の要素がないので最初の「黒」になる
        assertEquals(Disc.BLACK, discWhite.next());
    }

}
