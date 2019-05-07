package hr.fer.zemris.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexPolynomialTest {

    @Test
    void orderWithZeroesAtBeginning() {
        ComplexPolynomial p = new ComplexPolynomial(Complex.ZERO, Complex.ONE, Complex.ONE_NEG);
        assertEquals(1, p.order());
    }

    @Test
    void orderNormal() {
        ComplexPolynomial p = new ComplexPolynomial(Complex.IM_NEG, Complex.ONE, Complex.ONE_NEG);
        assertEquals(2, p.order());
    }

    @Test
    void orderAllZeros() {
        ComplexPolynomial p = new ComplexPolynomial(Complex.ZERO, Complex.ZERO, Complex.ZERO);
        assertEquals(0, p.order());
    }

    @Test
    void multiply() {
        Complex[] factorsA = {
                new Complex(3, -4),
                new Complex(0, 0),
                new Complex(-12, 11.3423)
        };
        Complex[] factorsB = {
                new Complex(12, -7.5),
                new Complex(.12, 0),
                new Complex(-2, 113)
        };
        Complex[] expectedFactors = {
                factorsA[0].multiply(factorsB[0]),
                factorsA[0].multiply(factorsB[1]).add(factorsA[1].multiply(factorsB[0])),
                factorsA[0].multiply(factorsB[2]).add(factorsA[1].multiply(factorsB[1])).add(factorsA[2].multiply(factorsB[0])),
                factorsA[1].multiply(factorsB[2]).add(factorsA[2].multiply(factorsB[1])),
                factorsA[2].multiply(factorsB[2])
        };
        ComplexPolynomial a = new ComplexPolynomial(factorsA);
        ComplexPolynomial b = new ComplexPolynomial(factorsB);
        ComplexPolynomial expected = new ComplexPolynomial(expectedFactors);
        assertEquals(expected, a.multiply(b));
    }

    @Test
    void derive() {
        Complex[] factors = {
                new Complex(3, -4),
                new Complex(0, 0),
                new Complex(-12, 11.3423)
        };
        Complex[] expectedFactors = {
                factors[0].multiply(new Complex(2, 0)),
                factors[1].multiply(new Complex(1, 0))
        };
        ComplexPolynomial p = new ComplexPolynomial(factors);
        ComplexPolynomial expected = new ComplexPolynomial(expectedFactors);
        assertEquals(expected, p.derive());
    }

    @Test
    void apply() {
        ComplexPolynomial p = new ComplexPolynomial(new Complex(3, 0), Complex.ZERO, new Complex(5, 0));
        Complex z = new Complex(3, 2);
        Complex expected = new Complex(20, 36);
        assertEquals(expected, p.apply(z));
    }
}