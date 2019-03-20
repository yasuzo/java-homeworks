package hr.fer.zemris.java.hw02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexNumberTest {

    private final static double DELTA = 1E-8;

    private static void testNumberForExpected(double real, double imaginary, double angle, double magnitude, ComplexNumber c) {
        assertEquals(imaginary, c.getImaginary(), DELTA);
        assertEquals(real, c.getReal(), DELTA);
        assertEquals(angle, c.getAngle(), DELTA);
        assertEquals(magnitude, c.getMagnitude(), DELTA);
    }

    //    constructor
    @Test
    void constructorTest() {
        ComplexNumber c = new ComplexNumber(0, 0);
        testNumberForExpected(0, 0, 0, 0, c);
    }

    //    parse()
    @Test
    void parseImaginaryPart() {
        assertEquals(new ComplexNumber(0, -3), ComplexNumber.parse("-3i"));
    }

    @Test
    void parseOnlyI() {
        assertEquals(new ComplexNumber(0, 1), ComplexNumber.parse("i"));
    }

    @Test
    void parseOnlyNegativeI() {
        assertEquals(new ComplexNumber(0, -1), ComplexNumber.parse("-i"));
    }

    @Test
    void parseInvalidExpression() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("-32+-i"));
    }

    @Test
    void parseInvalidExpression2() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("-32-+-i"));
    }

    @Test
    void parseEmpty() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(""));
    }

    @Test
    void parseNegative() {
        assertEquals(new ComplexNumber(-314.213, -23), ComplexNumber.parse("-314.213 - 23i"));
    }

    @Test
    void parseOnlyRealPart() {
        assertEquals(new ComplexNumber(314.213, 0), ComplexNumber.parse("314.213"));
    }

    @Test
    void parseOnlyRealPartWithPlusInFront() {
        assertEquals(new ComplexNumber(314.213, 0), ComplexNumber.parse("+314.213"));
    }

    @Test
    void parseFullNumber() {
        assertEquals(new ComplexNumber(314.213, 330), ComplexNumber.parse("+314.213 + 332i - i - i"));
    }

    //    fromReal()
    @Test
    void fromPositiveReal() {
        ComplexNumber c = ComplexNumber.fromReal(32);
        testNumberForExpected(32, 0, 0, 32, c);
    }

    @Test
    void fromNegativeReal() {
        ComplexNumber c = ComplexNumber.fromReal(-32);
        testNumberForExpected(-32, 0, Math.PI, 32, c);
    }

    //    fromImaginary
    @Test
    void fromPositiveImaginary() {
        ComplexNumber c = ComplexNumber.fromImaginary(32);
        testNumberForExpected(0, 32, Math.PI / 2, 32, c);
    }

    @Test
    void fromNegativeImaginary() {
        ComplexNumber c = ComplexNumber.fromImaginary(-32);
        testNumberForExpected(0, -32, 3 * Math.PI / 2, 32, c);

    }

    //    fromMagnitudeAndAngle()
    @Test
    void fromMagnitudeAndAngle() {
        ComplexNumber c = ComplexNumber.fromMagnitudeAndAngle(Math.sqrt(2), 3 * Math.PI / 4);
        testNumberForExpected(-1, 1, 3 * Math.PI / 4, Math.sqrt(2), c);
    }

    @Test
    void fromMagnitudeAndNegativeAngle() {
        ComplexNumber c = ComplexNumber.fromMagnitudeAndAngle(Math.sqrt(2), -5 * Math.PI / 4);
        testNumberForExpected(-1, 1, 3 * Math.PI / 4, Math.sqrt(2), c);
    }

    @Test
    void fromNegativeMagnitude() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.fromMagnitudeAndAngle(-32, 0));
    }

    //    add()
    @Test
    void addNull() {
        assertThrows(NullPointerException.class, () -> new ComplexNumber(2, 3).add(null));
    }

    @Test
    void addNormal() {
        assertEquals(new ComplexNumber(5, 5), new ComplexNumber(-1, 3).add(new ComplexNumber(6, 2)));
    }

    //    sub()
    @Test
    void subNull() {
        assertThrows(NullPointerException.class, () -> new ComplexNumber(2, 3).sub(null));
    }

    @Test
    void subNormal() {
        assertEquals(new ComplexNumber(-7, 1), new ComplexNumber(-1, 3).sub(new ComplexNumber(6, 2)));
    }

    //    mul()
    @Test
    void mulNull() {
        assertThrows(NullPointerException.class, () -> new ComplexNumber(2, 3).mul(null));
    }

    @Test
    void mulNormal() {
        ComplexNumber a = new ComplexNumber(3, 3);
        ComplexNumber b = new ComplexNumber(2, -1);
        assertEquals(new ComplexNumber(9, 3), a.mul(b));
        assertEquals(new ComplexNumber(9, 3), b.mul(a));
    }

    //    div()
    @Test
    void divNull() {
        assertThrows(NullPointerException.class, () -> new ComplexNumber(2, 3).div(null));
    }

    @Test
    void divNormal() {
        ComplexNumber a = new ComplexNumber(3, 3);
        ComplexNumber b = new ComplexNumber(2, -1);
        assertEquals(new ComplexNumber(3. / 5, 9. / 5), a.div(b));
    }

    @Test
    void divWithZero() {
        ComplexNumber a = new ComplexNumber(-3, 3);
        ComplexNumber b = new ComplexNumber(0, 0);

        ComplexNumber result = a.div(b);

        assertTrue(Double.isInfinite(result.getImaginary()));
        assertTrue(Double.isInfinite(result.getReal()) && result.getReal() < 0);
        assertEquals(3 * Math.PI / 4, result.getAngle());
        assertTrue(Double.isInfinite(result.getMagnitude()));
    }

    //    power()
    @Test
    void power() {
        ComplexNumber a = new ComplexNumber(2, -4);
        assertEquals(new ComplexNumber(7488, -2816), a.power(6));
    }

    @Test
    void powerOf1() {
        ComplexNumber a = new ComplexNumber(2, -4);
        assertEquals(new ComplexNumber(2, -4), a.power(1));
    }

    @Test
    void powerNegativeExponent() {
        assertThrows(IllegalArgumentException.class, () -> new ComplexNumber(3, 3).power(-1));
    }

    //    root()
    @Test
    void root() {
        ComplexNumber a = new ComplexNumber(32, -12);

        ComplexNumber[] roots = a.root(4);

        ComplexNumber[] expected = new ComplexNumber[]{
                new ComplexNumber(0.216573262841724, 2.40813653956864),
                new ComplexNumber(-2.40813653956864, 0.216573262841724),
                new ComplexNumber(-0.216573262841724, -2.40813653956864),
                new ComplexNumber(2.40813653956864, -0.216573262841724)
        };

        assertArrayEquals(expected, roots);
    }

    @Test
    void firstRoot() {
        ComplexNumber a = new ComplexNumber(32, -12);
        ComplexNumber[] roots = a.root(1);

        assertEquals(1, roots.length);
        assertEquals(a, roots[0]);
    }

    @Test
    void rootIllegal() {
        assertThrows(IllegalArgumentException.class, () -> new ComplexNumber(2, 2).root(0));
    }
}