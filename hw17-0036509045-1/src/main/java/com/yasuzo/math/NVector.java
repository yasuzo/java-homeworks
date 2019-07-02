package com.yasuzo.math;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Instances of this class model a vector in n-dimensional space.
 *
 * @param <T> Type of vector coordinates.
 * @author Jan Capek
 */
public class NVector<T extends Number> {

    private List<T> coordinates;
    private double norm;
    private boolean cacheValid;

    /**
     * Constructs a new vector with given coordinates. This vector is n-dimensional which means that
     * if e.g. only 5 coordinates were given other coordinates will have zero value.<br>
     * First coordinate in an array is x coordinate, second is y, third is z etc.
     *
     * @param coordinates Vector's coordinates.
     * @throws NullPointerException If given array or any of its elements is {@code null}.
     */
    public NVector(T... coordinates) {
        Objects.requireNonNull(coordinates);
        this.coordinates = new ArrayList<>(coordinates.length);
        for (T coord : coordinates) {
            Objects.requireNonNull(coord);
            this.coordinates.add(coord);
        }
    }

    /**
     * Constructs a new zero-vector.
     */
    public NVector() {
        this.coordinates = new ArrayList<>(0);
    }

    /**
     * Constructs a new vector with initial number of dimensions.
     *
     * @param n Number of dimensions.
     * @throws IllegalArgumentException If {@code n < 0}.
     */
    public NVector(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number of dimensions cannot be less than 0!");
        }
        this.coordinates = new ArrayList<>(n);
    }

    /**
     * Refreshes cached data if there current cache is invalid. Does nothing otherwise.
     */
    private void refreshCache() {
        if (cacheValid) {
            return;
        }
        norm = calculateNorm();
        cacheValid = true;
    }

    /**
     * Invalidates cached data.
     */
    private void invalidateCache() {
        cacheValid = false;
    }

    /**
     * Calculates norm (length) of the vector.
     *
     * @return Vector's length.
     */
    private double calculateNorm() {
        double sumOfSquares = 0;
        for (T coord : coordinates) {
            sumOfSquares += coord.doubleValue() * coord.doubleValue();
        }
        return Math.sqrt(sumOfSquares);
    }

    /**
     * @return Length of this vector.
     */
    public double norm() {
        refreshCache();
        return norm;
    }

    /**
     * Calculates a scalar product of {@code this} and {@code other}.
     *
     * @param other Other vector operand.
     * @return Scalar product.
     * @throws NullPointerException If given vector is {@code null}.
     */
    public double dot(NVector<?> other) {
        Objects.requireNonNull(other);

//        find which vector has min number of coordinates & create iterator for another's coordinates
        Iterator<? extends Number> it;
        NVector<? extends Number> min;
        if (this.coordinates.size() < other.coordinates.size()) {
            min = this;
            it = other.coordinates.iterator();
        } else {
            min = other;
            it = this.coordinates.iterator();
        }

//        create result
        return min.coordinates.stream()
                .mapToDouble(coord -> coord.doubleValue() * it.next().doubleValue())
                .sum();
    }

    /**
     * Returns a cosign value of angle between {@code this} and {@code other}.
     *
     * @param other Other vector.
     * @return Cosign of angle between this vector and given vector. If any of the vectors is zero-vector NaN will be returned.
     * @throws NullPointerException If given vector is {@code null}.
     */
    public double cosAngle(NVector<?> other) {
        Objects.requireNonNull(other);
        return this.dot(other) / (this.norm() * other.norm());
    }

    /**
     * @return List of coordinates.
     */
    public List<T> getCoordinates() {
        return Collections.unmodifiableList(coordinates);
    }

    /**
     * Sets vector's coordinate at given index to a value returned by the setter.
     *
     * @param index     Index of the element that needs to be set.
     * @param zeroValue Zero value number; This is used in case index is not less than the current size of the
     *                  underlying collection of coordinates and all uninitialized coordinates up to and including that index
     *                  will need to be initialized to zero value.
     * @param setter    Setter that will take old value of the coordinate and return new value for that coordinate.
     * @throws NullPointerException     If zeroValue, setter OR new coordinate value is {@code null}.
     * @throws IllegalArgumentException If given index is less than 0.
     */
    public void setCoordinateAt(int index, T zeroValue, UnaryOperator<T> setter) {
        Objects.requireNonNull(zeroValue);
        Objects.requireNonNull(setter);

        T oldValue = getCoordinateAt(index, zeroValue);

//        need to check if new value is null
        T newValue = setter.apply(oldValue);
        Objects.requireNonNull(newValue, "Value cannot be null!");

//        if not already set, fill coordinates up to and including given index with zeroValue
        for (int i = coordinates.size(); i <= index; i++) {
            coordinates.add(zeroValue);
        }

//        set new value & invalidate cache
        coordinates.set(index, newValue);
        invalidateCache();
    }


    /**
     * Returns a coordinate at given index. If no coordinate is set at wanted index, zeroValue will be returned.
     *
     * @param index Index of the wanted coordinate.
     * @param zeroValue Zero value for current generic vector type.
     *                  This will be returned if no coordinate is set at wanted index.
     * @return Value of the coordinate at wanted index.
     * @throws IllegalArgumentException If index is less than 0.
     */
    public T getCoordinateAt(int index, T zeroValue) {
        if (index < 0) {
            throw new IllegalArgumentException("Coordinate indexes start from 0 - given index is " + index);
        }
        return index >= coordinates.size() ? zeroValue : coordinates.get(index);
    }

    /**
     * @return Number of dimensions this vector has.
     */
    public int numberOfDimensions() {
        return coordinates.size();
    }

    @Override
    public String toString() {
        return "[" + coordinates.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ")) + "]";
    }
}
