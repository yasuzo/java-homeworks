package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Instances of this class represent polynomials {@code f(z) = z0*(z-z1)*(z-z2)*...*(z-zn)} where z0 is a complex constant,
 * and z1-zn are complex roots.
 *
 * @author Jan Capek
 */
public class ComplexRootedPolynomial {

    private Complex constant;
    private Complex[] roots;

    /**
     * Creates a new polynomial with given arguments.
     *
     * @param constant Constant used in the polynomial.
     * @param roots    Polynomial roots.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public ComplexRootedPolynomial(Complex constant, Complex... roots) {
        this.constant = Objects.requireNonNull(constant);
        for (Complex c : Objects.requireNonNull(roots)) {
            Objects.requireNonNull(c, "Root must not be null.");
        }
        this.roots = roots.clone();
    }

    /**
     * Calculates a result of the polynomial which is a function {@code f(z)}.
     *
     * @param z Polynomial parameter.
     * @return Result of computation.
     * @throws NullPointerException If the parameter is {@code null}.
     */
    public Complex apply(Complex z) {
        Objects.requireNonNull(z);
        Complex result = constant;
        for (Complex root : roots) {
            Complex sub = z.sub(root);
            result = result.multiply(sub);
        }
        return result;
    }

    /**
     * Converts this polynomial to {@link ComplexPolynomial}.
     *
     * @return {@this} polynomial converted to {@link ComplexPolynomial}.
     */
    public ComplexPolynomial toComplexPolynom() {
        ComplexPolynomial result = new ComplexPolynomial(constant);
        for( Complex root : roots) {
            result = result.multiply(new ComplexPolynomial(Complex.ONE, root.negate()));
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("(%s)", constant));
        for (Complex root : roots) {
            sb.append(String.format("*(z-(%s))", root));
        }
        return sb.toString();
    }

    /**
     * Returns index of the closest root that is under given threshold compared to given complex number.
     *
     * @param z         Reference complex number. This can be {@code null} and this method will always return -1.
     * @param threshold Threshold that a number should be under in order for this method to find an index.
     *                  Must be equal or greater than 0.
     * @return Index of the closest matching root under the threshold compared to {@code z}.
     * If no root is under the threshold or given reference number is {@code null} -1 will be returned.
     * @throws IllegalArgumentException If the threshold is less than 0.
     */
    public int indexOfClosestRootFor(Complex z, double threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must be positive or 0.");
        }
        if (z == null || roots.length == 0) {
            return -1;
        }
        int minIndex = 0;
        double minDiff = z.sub(roots[0]).module();
        for (int i = 1; i < roots.length; i++) {
            double diff = z.sub(roots[i]).module();
            if (diff < minDiff) {
                minDiff = diff;
                minIndex = i;
            }
        }
        return minDiff <= threshold ? minIndex : -1;
    }
}