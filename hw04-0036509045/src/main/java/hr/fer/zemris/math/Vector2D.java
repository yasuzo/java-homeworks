package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Class models a 2D vector with x and y coordinates.
 *
 * @author Jan Capek
 */
public class Vector2D {

    private static final double DELTA = 1E-10;

    private double x;
    private double y;

    /**
     * Constructs a new vector from given coordinates.
     *
     * @param x x coordinate.
     * @param y y coordinate.
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Translates the vector by given offset.
     *
     * @param offset Offset to translate by.
     */
    public void translate(Vector2D offset) {
        x += offset.x;
        y += offset.y;
    }

    /**
     * Creates and returns a new vector as a result of translating the vector on which this is called by given offset.
     *
     * @param offset Offset of a new vector relative to the vector on which this is called.
     * @return New vector.
     */
    public Vector2D translated(Vector2D offset) {
        Vector2D newVector = this.copy();
        newVector.translate(offset);
        return newVector;
    }

    /**
     * Rotates the vector for a given angle.
     *
     * @param angle Angle of rotation in radians.
     */
    public void rotate(double angle) {
        double length = Math.sqrt(x*x + y*y);
        double newAngle = Math.atan2(y, x) + angle;
        x = Math.cos(newAngle) * length;
        y = Math.sin(newAngle) * length;
    }

    /**
     * Creates a new vector as a result of rotation of the vector this method is called on by given angle.
     *
     * @param angle Angle a new vector should be rotated by relative to the vector this is called on.
     * @return New vector.
     */
    public Vector2D rotated(double angle) {
        Vector2D newVector = this.copy();
        newVector.rotate(angle);
        return newVector;
    }

    /**
     * Scales vector by a given scalar.
     *
     * @param scalar How much a vector should be scaled.
     */
    public void scale(double scalar) {
        x *= scalar;
        y *= scalar;
    }

    /**
     * Creates a new vector as a result of scaling the vector this is called on by a given scalar.
     *
     * @param scalar Value to scale by.
     * @return New scaled vector.
     */
    public Vector2D scaled(double scalar) {
        Vector2D newVector = this.copy();
        newVector.scale(scalar);
        return newVector;
    }

    /**
     * Creates a copy of a vector this is called on.
     *
     * @return Copy of the vector.
     */
    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return Math.abs(vector2D.x - x) < DELTA &&
                Math.abs(vector2D.y - y) < DELTA;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }
}
