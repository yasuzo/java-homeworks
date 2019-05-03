package coloring.algorithms;

import marcupic.opjj.statespace.coloring.Picture;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Coloring implements Consumer<Pixel>, Function<Pixel, List<Pixel>>, Predicate<Pixel>, Supplier<Pixel> {
    private Pixel reference;
    private Picture picture;
    private int fillColor;
    private int refColor;

    /**
     * Constructs a new coloring object used for coloring given picture.
     *
     * @param reference Reference pixel of the picture.
     *                  Pixels of the same color as this one that are around it will be colored to the color that is
     *                  provided as an argument.
     * @param picture   Picture which needs to be colored.
     * @param fillColor Fill color.
     * @throws NullPointerException     If pixel reference or picture is {@code null}.
     * @throws IllegalArgumentException If pixel reference is not inside picture's boundaries.
     */
    public Coloring(Pixel reference, Picture picture, int fillColor) {
        this.picture = Objects.requireNonNull(picture);

        Objects.requireNonNull(reference);
        if (isInPicture(reference.getX(), reference.getY()) == false) {
            throw new IllegalArgumentException("Pixel is not inside pictures boundaries.");
        }
        this.reference = reference;

        this.fillColor = fillColor;
        this.refColor = picture.getPixelColor(reference.getX(), reference.getY());
    }

    /**
     * Changes color of the given pixel on the picture given in the constructor to a color also given in a constructor.
     *
     * @param pixel Pixel whose color should be changed.
     * @throws NullPointerException If given pixel is {@code null}.
     */
    @Override
    public void accept(Pixel pixel) {
        Objects.requireNonNull(pixel);
        picture.setPixelColor(pixel.getX(), pixel.getY(), fillColor);
    }

    /**
     * Returns a list of other pixels that should be processed based on the given pixel.
     *
     * @param pixel Reference pixel based on which a list of other pixels will be returned.
     * @return List of other pixels that should be processed.
     * @throws NullPointerException If given pixel is {@code null}.
     */
    @Override
    public List<Pixel> apply(Pixel pixel) {
        Objects.requireNonNull(pixel);

        int refX = pixel.getX();
        int refY = pixel.getY();
        int[] nextX = {0, 1, 0, -1};
        int[] nextY = {1, 0, -1, 0};

        List<Pixel> list = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            int x = refX + nextX[i];
            int y = refY + nextY[i];
            if (isInPicture(x, y)) {
                list.add(new Pixel(x, y));
            }
        }
        return list;
    }

    /**
     * Checks if given coordinates are inside of picture's boundaries.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return {@code true} if given coordinate belong to the picture, {@code false} otherwise.
     */
    private boolean isInPicture(int x, int y) {
        if (x < 0 || x >= picture.getWidth()) {
            return false;
        }
        if (y < 0 || y >= picture.getHeight()) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a pixel is in picture and if its color matches reference color.
     *
     * @param pixel Pixel that needs to be checked.
     * @return {@code true} if a pixel is in picture and its color is equal to reference color, {@code false} otherwise.
     * @throws NullPointerException If given pixel is {@code null}.
     */
    @Override
    public boolean test(Pixel pixel) {
        Objects.requireNonNull(pixel);
        int x = pixel.getX();
        int y = pixel.getY();
        return isInPicture(x, y) && picture.getPixelColor(x, y) == refColor;
    }

    /**
     * @return Starting pixel.
     */
    @Override
    public Pixel get() {
        return reference;
    }
}
