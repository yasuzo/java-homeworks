package hr.fer.zemris.java.hw01;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactorialTest {

    @Test
    public void factorialOfIntegerInRange() {
        int num = 20;
        assertEquals(2432902008176640000L, Factorial.calculateFactorial(num));
    }

    @Test
    public void factorialOfOutOfRangeInteger() {
        int num = 21;
        assertThrows(IllegalArgumentException.class, () -> Factorial.calculateFactorial(num));
    }

    @Test
    public void factorialOfTooLowInteger() {
        int num = 2;
        assertThrows(IllegalArgumentException.class, () -> Factorial.calculateFactorial(num));
    }

}