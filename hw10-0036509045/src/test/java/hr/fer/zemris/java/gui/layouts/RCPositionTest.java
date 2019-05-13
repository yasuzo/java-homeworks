package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RCPositionTest {

    @Test
    void fromString() {
        assertEquals(new RCPosition(2, 3), RCPosition.fromString("2,3"));
        assertEquals(new RCPosition(-2, 3), RCPosition.fromString("-2,3"));
        assertEquals(new RCPosition(2, -3), RCPosition.fromString("2,  -3"));
        assertEquals(new RCPosition(2, 3), RCPosition.fromString("   2  ,  3  "));
    }

    @Test
    void fromStringIllegal() {
        assertThrows(IllegalArgumentException.class, () -> RCPosition.fromString("23"));
        assertThrows(IllegalArgumentException.class, () -> RCPosition.fromString("2,a"));
        assertThrows(IllegalArgumentException.class, () -> RCPosition.fromString(""));
        assertThrows(IllegalArgumentException.class, () -> RCPosition.fromString(","));
        assertThrows(IllegalArgumentException.class, () -> RCPosition.fromString("--2, 3"));
    }

    @Test
    void fromStringNull() {
        assertThrows(NullPointerException.class, () -> RCPosition.fromString(null));
    }
}