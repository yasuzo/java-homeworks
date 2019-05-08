package hr.fer.zemris.java.hw02;

import java.util.Objects;

/**
 * Class that represents model of a complex number.
 *
 * @author Jan Capek
 */
public class ComplexNumber {

    //    acceptable delta when comparing two doubles
    private static final double DELTA = 1E-8;

    //    real and imaginary parts
    private double real;
    private double imaginary;

    //    polar representation
    private double angle;
    private double magnitude;

    /**
     * Constructs a complex number from Cartesian form.
     *
     * @param real      Real part of complex number
     * @param imaginary Imaginary part of complex number
     */
    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
        calculatePolarCoordinates();
    }

    /**
     * Constructs a new complex number with just a real part.
     * Imaginary part will be 0.
     *
     * @param real Real part of complex number
     * @return Complex number with given real part and 0 as imaginary part
     */
    public static ComplexNumber fromReal(double real) {
        return new ComplexNumber(real, 0);
    }

    /**
     * Constructs a new complex number with just an imaginary part.
     * Real part will be 0.
     *
     * @param imaginary Imaginary part of complex number
     * @return Complex number with given imaginary part and 0 as real part
     */
    public static ComplexNumber fromImaginary(double imaginary) {
        return new ComplexNumber(0, imaginary);
    }

    /**
     * Constructs a new complex number from polar form.
     *
     * @param magnitude Magnitude of complex number
     * @param angle     Angle of complex number
     * @return Complex number created from polar form
     * @throws IllegalArgumentException If magnitude is negative
     */
    public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
        if (magnitude < 0) {
            throw new IllegalArgumentException("Magnitude must be a positive number!");
        }

        double imaginary = Math.sin(angle) * magnitude;
        double real = Math.cos(angle) * magnitude;

        return new ComplexNumber(real, imaginary);
    }

    /**
     * Parses given string to a complex number.
     *
     * @param expression String that should be parsed
     * @return Parsed complex number
     * @throws NullPointerException     If given string is {@code null}
     * @throws IllegalArgumentException If the expression is invalid
     */
    public static ComplexNumber parse(String expression) {
        Objects.requireNonNull(expression);

//        get rid of all spaces
        expression = expression.replaceAll("\\s+", "");

//        check if the string is empty or contains sequence that is not valid but could be interpreted as such e.g. "+-"
        if (expression.contains("+-") || expression.isEmpty()) {
            throw new IllegalArgumentException("Expression is not valid!");
        }

//        split by '+'
        String[] substrings = expression.split("\\+");

        double real = 0, imaginary = 0;
        for (String substring : substrings) {
//            split before each '-'
            String[] bits = substring.split("(?=-)");
            for (String s : bits) {
                if (s.isEmpty()) {
                    continue;
                }

                try {
                    if (s.endsWith("i")) {
                        s = s.replace("i", "");

//                    check for edge cases where imaginary part is given as 'i' or '-i'
                        if (s.isEmpty() || s.equals("-")) {
                            s += "1";
                        }

                        imaginary += Double.parseDouble(s);
                        continue;
                    }
                    real += Double.parseDouble(s);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Expression is not valid!");
                }
            }
        }

        return new ComplexNumber(real, imaginary);
    }

    /**
     * Calculates polar coordinates of complex number.
     */
    private void calculatePolarCoordinates() {
        angle = Math.atan2(imaginary, real);

//        if angle is negative, transform it to positive
        angle = angle < 0 ? angle + 2 * Math.PI : angle;

        magnitude = Math.sqrt(Math.pow(real, 2) + Math.pow(imaginary, 2));
    }

    /**
     * Adds two complex numbers and returns a resulting complex number.
     * This method will not change in any way current state of the object it is called on.
     *
     * @param c Complex number to add to the number this method is called on
     * @return Result of addition
     * @throws NullPointerException If given parameter is {@code null}
     */
    public ComplexNumber add(ComplexNumber c) {
        Objects.requireNonNull(c);

        return new ComplexNumber(c.real + this.real, c.imaginary + this.imaginary);
    }

    /**
     * Subtracts given complex number from the number this is called on.
     * This method will not change current state of the object it is called on.
     *
     * @param c Number to do subtraction with
     * @return Result of subtraction
     * @throws NullPointerException If given parameter is {@code null}
     */
    public ComplexNumber sub(ComplexNumber c) {
        Objects.requireNonNull(c);

        return new ComplexNumber(this.real - c.real, this.imaginary - c.imaginary);
    }

    /**
     * Multiplies two complex numbers and returns result of multiplication which is a new complex number.
     * This will not mutate original complex number but rather return a new one.
     *
     * @param c Complex number to multiply with
     * @return Result of multiplication
     * @throws NullPointerException If given parameter is {@code null}
     */
    public ComplexNumber mul(ComplexNumber c) {
        Objects.requireNonNull(c);

        double real = c.real * this.real - c.imaginary * this.imaginary;
        double imaginary = c.imaginary * this.real + c.real * this.imaginary;

        return new ComplexNumber(real, imaginary);
    }

    /**
     * Divides two complex numbers and returns a new complex number as result of division.
     *
     * @param c Number to divide by
     * @return Result of division
     * @throws NullPointerException If given parameter is {@code null}
     */
    public ComplexNumber div(ComplexNumber c) {
        Objects.requireNonNull(c);

//        check if c is 0
        if (Math.abs(c.real) < DELTA && Math.abs(c.imaginary) < DELTA) {
            return new ComplexNumber(this.real / 0.0, this.imaginary / 0.0);
        }

        ComplexNumber conjugateOfC = c.conjugate();
        ComplexNumber numerator = this.mul(conjugateOfC);
        ComplexNumber denominator = c.mul(conjugateOfC); // denominator will only have a real part

        return new ComplexNumber(numerator.real / denominator.real, numerator.imaginary / denominator.real);
    }

    /**
     * Return a new complex number which is conjugate of the number this is called on.
     * This will not mutate current number.
     *
     * @return Conjugate of current complex number
     */
    private ComplexNumber conjugate() {
        return new ComplexNumber(this.real, -this.imaginary);
    }

    /**
     * Calculates complex number to the power of {@code n} given as an argument.
     * {@code n} must not be less than 0.
     *
     * @param n Exponent
     * @return Current number raised to the power of {@code n}
     */
    public ComplexNumber power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot calculate to the power of less than 0");
        }

        if (n == 0) {
            return ComplexNumber.fromReal(1);
        }

        ComplexNumber result = new ComplexNumber(this.real, this.imaginary);
        for (int i = 0; i < n - 1; i++) {
            result = result.mul(this);
        }

        return result;
    }

    /**
     * Calculates nth root of the complex number.
     *
     * @param n nth root that should be calculated
     * @return nth roots of the complex number
     */
    public ComplexNumber[] root(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Cannot calculate root for n that is less than 1!");
        }

        ComplexNumber[] results = new ComplexNumber[n];

        double rootMagnitude = Math.pow(magnitude, 1.0 / n);
        for (int i = 0; i < n; i++) {
            double real = rootMagnitude * Math.cos((angle + 2 * Math.PI * i) / n);
            double imaginary = rootMagnitude * Math.sin((angle + 2 * Math.PI * i) / n);
            results[i] = new ComplexNumber(real, imaginary);
        }

        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexNumber that = (ComplexNumber) o;
        return Math.abs(that.real - real) < DELTA &&
                Math.abs(that.imaginary - imaginary) < DELTA &&
                Math.abs(that.angle - angle) < DELTA &&
                Math.abs(that.magnitude - magnitude) < DELTA;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary, angle, magnitude);
    }

    /**
     * @return String representation of the complex number
     */
    @Override
    public String toString() {
        if (imaginary < 0) {
            return String.format("%.4f - %.4fi", real, -imaginary);
        }

        return String.format("%.4f + %.4fi", real, imaginary);
    }

    /**
     * @return Real part of complex number
     */
    public double getReal() {
        return real;
    }

    /**
     * @return Imaginary part of complex number
     */
    public double getImaginary() {
        return imaginary;
    }

    /**
     * @return Angle of complex number in polar form
     */
    public double getAngle() {
        return angle;
    }

    /**
     * @return Magnitude of complex number in polar form
     */
    public double getMagnitude() {
        return magnitude;
    }
}
