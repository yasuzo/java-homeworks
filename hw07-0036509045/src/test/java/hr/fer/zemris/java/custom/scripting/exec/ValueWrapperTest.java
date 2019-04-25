package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueWrapperTest {

    @Test
    void addString() {
        ValueWrapper wrapper = new ValueWrapper(5);
        wrapper.add("2");
        assertEquals(7, wrapper.getValue());
    }

    @Test
    void subtract() {
        ValueWrapper wrapper = new ValueWrapper("-21.5");
        wrapper.subtract("0.4");
        assertEquals(-21.9, wrapper.getValue());
    }

    @Test
    void multiplyIntegers() {
        ValueWrapper wrapper = new ValueWrapper(5);
        wrapper.multiply(2);
        assertEquals(10, wrapper.getValue());
    }

    @Test
    void multiplyDouble() {
        ValueWrapper wrapper = new ValueWrapper(5);
        wrapper.multiply(2.0);
        assertEquals(10.0, wrapper.getValue());
    }

    @Test
    void multiplyNull() {
        ValueWrapper wrapper = new ValueWrapper(null);
        wrapper.multiply(null);
        assertEquals(0, wrapper.getValue());
    }

    @Test
    void divideIntegerByZero() {
        ValueWrapper wrapper = new ValueWrapper(15);
        assertThrows(ArithmeticException.class, () -> wrapper.divide(0));
    }

    @Test
    void divideIntegerZeroByZero() {
        ValueWrapper wrapper = new ValueWrapper(0);
        assertThrows(ArithmeticException.class, () -> wrapper.divide(0));
    }

    @Test
    void divideDoubleByZero() {
        ValueWrapper wrapper = new ValueWrapper(15.0);
        wrapper.divide(0);
        Double result = (Double) wrapper.getValue();
        assertTrue(result.isInfinite());
    }

    @Test
    void divideDoubleZeroByZero() {
        ValueWrapper wrapper = new ValueWrapper(0);
        wrapper.divide(0.0);
        Double result = (Double) wrapper.getValue();
        assertTrue(result.isNaN());
    }

    @Test
    void numCompare() {
        ValueWrapper wrapper = new ValueWrapper(12);
        assertTrue(wrapper.numCompare(14) < 0);
        assertTrue(wrapper.numCompare(11) > 0);
        assertTrue(wrapper.numCompare(12) == 0);
    }

    @Test
    void numCompareNull() {
        ValueWrapper wrapper = new ValueWrapper(null);
        assertTrue(wrapper.numCompare(0) == 0);
        assertTrue(wrapper.numCompare(1) < 0);
        assertTrue(wrapper.numCompare(-1) > 0);
    }
}