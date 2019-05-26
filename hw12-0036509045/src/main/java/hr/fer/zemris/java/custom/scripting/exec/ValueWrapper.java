package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.exec.util.ObjectConverter;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Instances of this class hold a value of type {@link Object}.
 *
 * @author Jan Capek
 */
public class ValueWrapper {
    private Object value;

    /**
     * Constructs a new value wrapper which holds given value.
     *
     * @param value Value that needs to be held.
     */
    public ValueWrapper(Object value) {
        this.value = value;
    }

    /**
     * Performs an arithmetic operation on objects {@code a} and {@code b}.
     *
     * @param a         First operand.
     * @param b         Second operand.
     * @param operation Operation that should be performed on the two operands.
     * @throws RuntimeException     If objects are not recognized {@code a} and {@code b} are not of
     *                              type Integer, Double or String (in which case string should represent a number)
     *                              or if {@code operation} threw an exception.
     * @throws NullPointerException If {@code operation} is {@code null}.
     * @throws ArithmeticException  If division by 0 happened and both operands are integers.
     */
    private static Number arithmeticOperation(Object a, Object b, BiFunction<Number, Number, Double> operation) {
        Objects.requireNonNull(operation);

        Number currentNum = ObjectConverter.convertToIntegerOrDouble(a);
        Number otherNum = ObjectConverter.convertToIntegerOrDouble(b);

//        perform operation
        Double result = operation.apply(currentNum, otherNum);

//        if any of the operands are Double, result will be double
        if (currentNum instanceof Double || otherNum instanceof Double) {
            return result;
        }

//        non of the operands are Double therefore result is must be an integer.
        if (result.isInfinite() || result.isNaN()) { // if this is true, division by 0 happened which is not allowed on integers
            throw new ArithmeticException("Division by 0 is not possible with integers.");
        }
        return result.intValue();
    }

    /**
     * @return Currently wrapped object value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets a new object value to be held by this wrapper.
     *
     * @param value New value.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Converts current object and given object to their integer or double representation if possible,
     * adds them together and updates currently held object to the result.
     *
     * @param incValue Value to increase current value by.
     * @throws RuntimeException If either current object or given object could not be converted
     *                          to their integer or double representations.
     */
    public void add(Object incValue) {
        value = arithmeticOperation(value, incValue, (a, b) -> a.doubleValue() + b.doubleValue());
    }

    /**
     * Converts current object and given object to their integer or double representation if possible,
     * subtracts them and updates currently held object to the result.
     *
     * @param decValue Value to decrease current value by.
     * @throws RuntimeException If either current object or given object could not be converted
     *                          to their integer or double representations.
     */
    public void subtract(Object decValue) {
        value = arithmeticOperation(value, decValue, (a, b) -> a.doubleValue() - b.doubleValue());
    }

    /**
     * Converts current object and given object to their integer or double representation if possible,
     * multiplies them together and updates currently held object to the result.
     *
     * @param mulValue Value to multiply current value with.
     * @throws RuntimeException If either current object or given object could not be converted
     *                          to their integer or double representations.
     */
    public void multiply(Object mulValue) {
        value = arithmeticOperation(value, mulValue, (a, b) -> a.doubleValue() * b.doubleValue());
    }

    /**
     * Converts current object and given object to their integer or double representation if possible,
     * divides them and updates currently held object to the result.
     *
     * @param divValue Value to divide current value by.
     * @throws RuntimeException    If either current object or given object could not be converted
     *                             to their integer or double representations.
     * @throws ArithmeticException If given value is a numeric representation of {@code 0} and both current value
     *                             and given value are integers or {@code null}.
     */
    public void divide(Object divValue) {
        value = arithmeticOperation(value, divValue, (a, b) -> a.doubleValue() / b.doubleValue());
    }

    /**
     * Compares current value object with given object.
     *
     * @param withValue Object that current object needs to be compared to.
     * @return Less than {@code 0} if current object is less than given value,
     * greater than {@code 0} if current object is greater than given object,
     * {@code 0} if two objects are considered of same value.
     * @throws RuntimeException If either current value or given value could not be converted
     *                          to their integer or double representations.
     */
    public int numCompare(Object withValue) {
        double result = arithmeticOperation(value, withValue, (a, b) -> a.doubleValue() - b.doubleValue()).doubleValue();
        return Double.compare(result, 0);
    }

}
