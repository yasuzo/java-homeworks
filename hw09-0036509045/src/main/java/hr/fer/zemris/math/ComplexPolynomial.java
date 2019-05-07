package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Objects;

/**
 * Instances of this class represent polynomials in the form {@code f(z) = zn*z^n + ... + z1*z^1 + z0}.
 *
 * @author Jan Capek
 */
public class ComplexPolynomial {

    private Complex[] factors;

    /**
     * Constructs a new polynomial with given factors.
     * First factor has the highest order then comes 2nd highest, 3rd highest etc.
     *
     * @param factors Factors of a polynomial.
     *                If given array is empty, polynomial of order 0 will be created with only factor {@link Complex#ZERO}.
     * @throws NullPointerException If either given array or any of its contents is {@code null}.
     */
    public ComplexPolynomial(Complex... factors) {
//        check nulls
        Objects.requireNonNull(factors);
        for (Complex c : factors) {
            Objects.requireNonNull(c);
        }

//        skip first factors that are equal to Complex.ZERO
        int i;
        for (i = 0; i < factors.length && factors[i].equals(Complex.ZERO); i++) ;
        if (i == factors.length) {
//            all factors are zero
            this.factors = new Complex[]{Complex.ZERO};
        } else {
//            ignore first zeros
            this.factors = Arrays.copyOfRange(factors, i, factors.length);
        }
    }

    /**
     * @return Order of the polynomial.
     */
    public short order() {
        return (short) (factors.length - 1);
    }

    /**
     * Multiplies this polynomial with given polynomial.
     *
     * @param p Second operand for multiplication.
     * @return Polynomial created by multiplying {@code this} and {@code p}.
     * @throws NullPointerException If given polynomial is {@code null}.
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        Objects.requireNonNull(p);
        Complex[] newFactors = new Complex[p.order() + this.order() + 1];
        for (int i = 0; i < this.factors.length; i++) {
            for (int j = 0; j < p.factors.length; j++) {
                int factorIndex = (newFactors.length - 1) - ((this.order() - i) + (p.order() - j));
                Complex fact = newFactors[factorIndex] == null ? Complex.ZERO : newFactors[factorIndex];
                newFactors[factorIndex] = fact.add(this.factors[i].multiply(p.factors[j]));
            }
        }
        return new ComplexPolynomial(newFactors);
    }

    /**
     * Creates a new polynomial which is a derivative of {@code this}.
     *
     * @return Derivative of {@code this}.
     */
    public ComplexPolynomial derive() {
        Complex[] newFactors = new Complex[factors.length - 1];
        for(int i = 0; i < factors.length - 1; i++){
            Complex multiplier = new Complex(order() - i, 0);
            newFactors[i] = factors[i].multiply(multiplier);
        }
        return new ComplexPolynomial(newFactors);
    }

    /**
     * Calculates a result of polynomial which is a function of {@code z}.
     *
     * @param z Parameter of the polynomial - {@code f(z)}
     * @return Value of the polynomial for given {@code z}.
     * @throws NullPointerException If given argument is {@code null}.
     */
    public Complex apply(Complex z) {
        Objects.requireNonNull(z);
        Complex result = Complex.ZERO;
        for(int i = 0; i < factors.length; i++){
            int power = order() - i;
            Complex zToPower = z.power(power);
            result = result.add(factors[i].multiply(zToPower));
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < factors.length - 1; i++) {
            int power = order() - i;
            sb.append(String.format("(%s)*z^%d+", factors[i], power));
        }
        sb.append(String.format("(%s)", factors[factors.length - 1]));
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexPolynomial that = (ComplexPolynomial) o;
        return Arrays.equals(factors, that.factors);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(factors);
    }
}