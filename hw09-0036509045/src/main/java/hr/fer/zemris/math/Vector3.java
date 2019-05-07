package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Instances of this class model a vector in 3D space. Each vector offers methods for basic arithmetic operations on it.
 *
 * @author Jan Capek
 */
public class Vector3 {

    private double x, y, z;

    /**
     * Constructs a new vector with given coordinates.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return Length of this vector.
     */
    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Creates a new normalized vector of this vector.
     *
     * @return Normalized vector.
     */
    public Vector3 normalized() {
        double norm = this.norm();
        double x = this.x / norm;
        double y = this.y / norm;
        double z = this.z / norm;
        return new Vector3(x, y, z);
    }

    /**
     * Creates a new vector which is a result of adding {@code this} and {@code other}.
     *
     * @param other Other vector operand.
     * @return Result of summation.
     * @throws NullPointerException If given vector is {@code null}.
     */
    public Vector3 add(Vector3 other) {
        Objects.requireNonNull(other);
        return new Vector3(other.x + this.x, other.y + this.y, other.z + this.z);
    }

    /**
     * Creates a new vector which is a result of subtracting {@code this} and {@code other}.
     * @param other Other vector operand.
     * @return Result of subtraction.
     * @throws NullPointerException If given vector is {@code null}.
     */
    public Vector3 sub(Vector3 other) {
        Objects.requireNonNull(other);
        return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Calculates a scalar product of {@code this} and {@code other}.
     *
     * @param other Other vector operand.
     * @return Scalar product.
     * @throws NullPointerException If given vector is {@code null}.
     */
    public double dot(Vector3 other) {
        Objects.requireNonNull(other);
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Calculates a vector product of {@code this} and {@code other}.
     *
     * @param other Other vector operand.
     * @return Vector product.
     * @throws NullPointerException If given vector is {@code null}.
     */
    public Vector3 cross(Vector3 other) {
        Objects.requireNonNull(other);
        double x = this.y * other.z - other.y * this.z;
        double y = this.z * other.x - other.z * this.x;
        double z = this.x * other.y - other.x * this.y;
        return new Vector3(x, y, z);
    }

    /**
     * Returns a new vector of the same direction of {@code this} scaled by given factor.
     *
     * @param s Scaling factor.
     * @return Scaled {@code this}.
     */
    public Vector3 scale(double s) {
        return new Vector3(this.x * s, this.y * s, this.z * s);
    }

    /**
     * Returns a cosign of angle between {@code this} and {@code other}.
     *
     * @param other Other vector.
     * @return Cosign of angle between this vector and given vector. If given any of the vectors are (0,0,0) NaN will be returned.
     * @throws NullPointerException If given vector is {@code null}.
     */
    public double cosAngle(Vector3 other) {
        Objects.requireNonNull(other);
        return this.dot(other) / (this.norm() * other.norm());
    }

    /**
     * @return Vector's x coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * @return Vector's y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * @return Vector's z coordinate.
     */
    public double getZ() {
        return z;
    }

    /**
     * @return Array representation of this vector {@code [x, y, z]}.
     */
    public double[] toArray() {
        return new double[] {x, y, z};
    }

    @Override
    public String toString() {
        return String.format("(%f, %f, %f)", x, y, z);
    }
}
