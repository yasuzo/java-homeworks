package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class that represents a model of a complex number.
 *
 * @author Jan Capek
 */
public class Complex {

    //    preloaded complex numbers
    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex ONE_NEG = new Complex(-1, 0);
    public static final Complex IM = new Complex(0, 1);
    public static final Complex IM_NEG = new Complex(0, -1);

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
     * @param real      Real part of complex number.
     * @param imaginary Imaginary part of complex number.
     */
    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
        calculatePolarCoordinates();
    }

    /**
     * Constructs a new complex number of value 0.
     */
    public Complex() {
        this(0, 0);
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
     * @param c Complex number to add to the number this method is called on.
     * @return Result of addition.
     * @throws NullPointerException If given parameter is {@code null}.
     */
    public Complex add(Complex c) {
        Objects.requireNonNull(c);

        return new Complex(c.real + this.real, c.imaginary + this.imaginary);
    }

    /**
     * Subtracts given complex number from the number this is called on.
     * This method will not change current state of the object it is called on.
     *
     * @param c Number to do subtraction with.
     * @return Result of subtraction.
     * @throws NullPointerException If given parameter is {@code null}.
     */
    public Complex sub(Complex c) {
        Objects.requireNonNull(c);

        return new Complex(this.real - c.real, this.imaginary - c.imaginary);
    }

    /**
     * Multiplies two complex numbers and returns result of multiplication which is a new complex number.
     * This will not mutate original complex number but rather return a new one.
     *
     * @param c Complex number to multiply with.
     * @return Result of multiplication.
     * @throws NullPointerException If given parameter is {@code null}.
     */
    public Complex multiply(Complex c) {
        Objects.requireNonNull(c);

        double real = c.real * this.real - c.imaginary * this.imaginary;
        double imaginary = c.imaginary * this.real + c.real * this.imaginary;

        return new Complex(real, imaginary);
    }

    /**
     * Divides two complex numbers and returns a new complex number as result of division.
     *
     * @param c Number to divide by.
     * @return Result of division.
     * @throws NullPointerException If given parameter is {@code null}.
     */
    public Complex divide(Complex c) {
        Objects.requireNonNull(c);

//        check if c is 0
        if (Math.abs(c.real) < DELTA && Math.abs(c.imaginary) < DELTA) {
            return new Complex(this.real / 0.0, this.imaginary / 0.0);
        }

        Complex conjugateOfC = c.conjugate();
        Complex numerator = this.multiply(conjugateOfC);
        Complex denominator = c.multiply(conjugateOfC); // denominator will only have a real part

        return new Complex(numerator.real / denominator.real, numerator.imaginary / denominator.real);
    }

    /**
     * Creates a new complex number that is equal to {@code -this}.
     *
     * @return Complex number {@code -this}.
     */
    public Complex negate() {
        return new Complex(-this.real, -this.imaginary);
    }

    /**
     * Return a new complex number which is conjugate of the number this is called on.
     * This will not mutate current number.
     *
     * @return Conjugate of current complex number.
     */
    private Complex conjugate() {
        return new Complex(this.real, -this.imaginary);
    }

    /**
     * Calculates complex number to the power of {@code n} given as an argument.
     * {@code n} must not be less than 0.
     *
     * @param n Exponent.
     * @return Current number raised to the power of {@code n}.
     * @throws IllegalArgumentException If {@code n} is less than 0.
     */
    public Complex power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot calculate to the power of less than 0");
        }

        if (n == 0) {
            return ONE;
        }

        Complex result = new Complex(this.real, this.imaginary);
        for (int i = 0; i < n - 1; i++) {
            result = result.multiply(this);
        }
        return result;
    }

    /**
     * Calculates nth root of the complex number.
     *
     * @param n nth root that should be calculated.
     * @return nth roots of the complex number.
     * @throws IllegalArgumentException If {@code n} is less than 1.
     */
    public List<Complex> root(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Cannot calculate root for n that is less than 1!");
        }

        List<Complex> results = new ArrayList<>(n);

        double rootMagnitude = Math.pow(magnitude, 1.0 / n);
        for (int i = 0; i < n; i++) {
            double real = rootMagnitude * Math.cos((angle + 2 * Math.PI * i) / n);
            double imaginary = rootMagnitude * Math.sin((angle + 2 * Math.PI * i) / n);
            results.add(new Complex(real, imaginary));
        }

        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex that = (Complex) o;
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
     * @return String representation of the complex number.
     */
    @Override
    public String toString() {
        if (imaginary < 0) {
            return String.format("%.2f-i%.2f", real, -imaginary);
        }

        return String.format("%.2f+i%.2f", real, imaginary);
    }

    /**
     * @return Real part of complex number.
     */
    public double getReal() {
        return real;
    }

    /**
     * @return Imaginary part of complex number.
     */
    public double getImaginary() {
        return imaginary;
    }

    /**
     * @return Angle of complex number in polar form.
     */
    public double getAngle() {
        return angle;
    }

    /**
     * @return Module of the complex number.
     */
    public double module() {
        return magnitude;
    }
}
