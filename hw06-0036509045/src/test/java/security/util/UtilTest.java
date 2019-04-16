package security.util;

import org.junit.jupiter.api.Test;
import security.util.Util;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void hextobyteNormal() {
        String hex = "01aE22";
        assertArrayEquals(new byte[] {1, -82, 34}, Util.hextobyte(hex));
    }

    @Test
    void hextobyteNormal2() {
        String hex = "001aE22";
        assertArrayEquals(new byte[] {0, 1, -82, 34}, Util.hextobyte(hex));
    }

    @Test
    void hextobyteNormal3() {
        String hex = "FF01aE22";
        assertArrayEquals(new byte[] {-1, 1, -82, 34}, Util.hextobyte(hex));
    }

    @Test
    void hextobyteEmpty() {
        String hex = "";
        assertArrayEquals(new byte[] {}, Util.hextobyte(hex));
    }

    @Test
    void hextobyteIllegal() {
        String hex = "0č1aE22";
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte(hex));
    }

    @Test
    void hextobyteNull() {
        assertThrows(NullPointerException.class, () -> Util.hextobyte(null));
    }

    @Test
    void bytetohexNormal() {
        byte[] arr = {1, -82, 34};
        assertEquals("01ae22", Util.bytetohex(arr));
    }

    @Test
    void bytetohexNormal2() {
        byte[] arr = {0, 1, -82, 34};
        assertEquals("0001ae22", Util.bytetohex(arr));
    }

    @Test
    void bytetohexNormal3() {
        byte[] arr = {-1, 1, -82, 34};
        assertEquals("ff01ae22", Util.bytetohex(arr));
    }

    @Test
    void bytetohexEmpty() {
        byte[] arr = {};
        assertEquals("", Util.bytetohex(arr));
    }

    @Test
    void bytetohexNull() {
        assertThrows(NullPointerException.class, () -> Util.bytetohex(null));
    }
}