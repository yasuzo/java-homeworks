package hr.fer.zemris.java.hw01;

/**
 * Class representing rectangle
 *
 * @author Jan Capek
 *
 */
public class Rectangle {

    private double width;
    private double height;

    /**
     * Instantiates a rectangle with given width and height
     *
     * @param width Width of a rectangle
     * @param height Height of a rectangle
     * @throws IllegalArgumentException if width or height are negative
     */
    public Rectangle(double width, double height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and height of a rectangle must be positive numbers!");
        }
        this.width = width;
        this.height = height;
    }

    /**
     * Calculates an area of the rectangle
     *
     * @return Area of the rectangle
     */
    public double area() {
        return width * height;
    }

    /**
     * Calculates a perimeter of the rectangle
     *
     * @return Perimeter of the rectangle
     */
    public double perimeter() {
        return 2 * width + 2 * height;
    }

    @Override
    public String toString() {
        return String.format("Pravokutnik sirine %.2f i visine %.2f ima povrsinu %.2f te opseg %.2f.", width, height, area(), perimeter());
    }

}
