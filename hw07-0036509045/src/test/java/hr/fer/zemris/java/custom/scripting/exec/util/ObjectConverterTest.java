package hr.fer.zemris.java.custom.scripting.exec.util;

import org.junit.jupiter.api.Test;

import static hr.fer.zemris.java.custom.scripting.exec.util.ObjectConverter.convertToIntegerOrDouble;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObjectConverterTest {

    @Test
    void convertToIntegerOrDoubleIllegal() {
        assertThrows(RuntimeException.class, () -> convertToIntegerOrDouble(true));
    }

    @Test
    void convertToIntegerOrDoubleFromIntegerOrDouble() {
        assertEquals(2, convertToIntegerOrDouble(2));
        assertEquals(-5.1, convertToIntegerOrDouble(-5.1));
    }

    @Test
    void convertToIntegerOrDoubleFromNull() {
        assertEquals(0, convertToIntegerOrDouble(null));
    }

    @Test
    void convertToIntegerOrDoubleFromString() {
        assertEquals(0, convertToIntegerOrDouble("0"));
        assertEquals(-43.0, convertToIntegerOrDouble("-43.0"));
        assertEquals(121, convertToIntegerOrDouble("121"));
    }

    @Test
    void convertToIntegerOrDoubleFromIllegalString() {
        assertThrows(RuntimeException.class, () -> convertToIntegerOrDouble("0dsaf"));
    }
}