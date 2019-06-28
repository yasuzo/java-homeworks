package hr.zemris.fer.hw16.utilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Image loader utility. This exposes methods for loading image from file system.
 *
 * @author Jan Capek
 */
public class ImageLoader {

    /**
     * Loads an image from file system and returns it.
     *
     * @param directory Path to the directory where image is located.
     * @param imageName Name of the image (including an extension).
     *                  The name cannot contain '/' or '\'. If it does {@code null} will be returned.
     * @return Loaded image or {@code null} if image could not be found.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public static BufferedImage load(String directory, String imageName) {
        Objects.requireNonNull(directory);
        Objects.requireNonNull(imageName);

//        invalid name -> image with that name does not exist
        if (imageName.matches(".*[\\\\/].*")) {
            return null;
        }

        Path normalImagePath = Path.of(directory, imageName);
        BufferedImage loadedImg;
        try {
            loadedImg = ImageIO.read(normalImagePath.toFile());
        } catch (IOException e) {
            return null;
        }
        return loadedImg;
    }
}
