package coloring.algorithms;

import java.util.Objects;

/**
 * Instance of this method holds information about a coordinate of the pixel on the canvas.
 *
 * @author Jan Capek
 */
public class Pixel {
    private int x;
    private int y;

    /**
     * Constructs a new pixel object with given coordinates.
     *
     * @param x X coordinate of the pixel.
     * @param y Y coordinate of the pixel.
     */
    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return X coordinate of the pixel.
     */
    public int getX() {
        return x;
    }

    /**
     * @return Y coordinate of the pixel.
     */
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pixel pixel = (Pixel) o;
        return x == pixel.x &&
                y == pixel.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
