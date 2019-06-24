package hr.zemris.fer.hw16.servlets;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * Serves requested images. Images are requested in "image" parameter of the GET method.
 *
 * @author Jan Capek
 */
public class ImageServlet extends HttpServlet {

    /**
     * Loads an image from cache folder or from base image folder and returns buffered image object.
     * Returned image will be resized to 150x150 pixels and cached if it wasn't already.
     *
     * @param imageFolder      Base image folder.
     * @param imageCacheFolder Base cache folder.
     * @param imageName        Image name.
     * @return Loaded image or {@code null} if image could not be loaded.
     * @throws NullPointerException If image folder or caching folder is {@code null}.
     */
    private static BufferedImage getImage(String imageFolder, String imageCacheFolder, String imageName) {
        Objects.requireNonNull(imageFolder);
        Objects.requireNonNull(imageCacheFolder);

        if (imageName == null) {
            return null;
        }

//        start with image path in cache
        Path cachedImagePath = Path.of(imageCacheFolder, imageName).toAbsolutePath().normalize();

//        check if image is in base image folder, if not return null
        if (cachedImagePath.toString().startsWith(imageFolder) == false) {
            return null;
        }

//        ------ load from CACHE ------
        if (Files.isReadable(cachedImagePath)) {
            try {
                return ImageIO.read(cachedImagePath.toFile());
            } catch (IOException e) {
                return null;
            }
        }

//        ------ load from BASE image folder & adjust -------
        Path normalImagePath = Path.of(imageFolder, imageName);
        BufferedImage loadedImg;
        try {
            loadedImg = ImageIO.read(normalImagePath.toFile());
        } catch (IOException e) {
            return null;
        }

//        no ImageReader could read the image -> return null
        if (loadedImg == null) {
            return null;
        }

//        resize image
        BufferedImage returnImage = scaleImage(loadedImg, 150, 150);

//        cache resized image
        if (saveImage(returnImage, cachedImagePath) == false) {
            System.out.println("*** Caching failed ***");
        }

        return returnImage;
    }

    /**
     * Saves an image to the given destination path. If saving was unsuccessful created file will be erased.
     *
     * @param image    Image that needs to be saved.
     * @param savePath Destination path.
     * @return {@code true} if saving operation was successful; {@code false} otherwise
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    private static boolean saveImage(BufferedImage image, Path savePath) {
        Objects.requireNonNull(image);
        Objects.requireNonNull(savePath);

        OutputStream outputStream = null;
        boolean savingSuccessful = false;
        try {
            Files.createDirectories(savePath.getParent());
            outputStream = Files.newOutputStream(savePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            savingSuccessful = ImageIO.write(image, savePath.toString().replaceFirst(".*\\.", ""), outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignorable) {
                }
            }
        }

//        cleanup on failure
        if (savingSuccessful == false) {
            try {
                Files.delete(savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return savingSuccessful;
    }

    /**
     * Scales given buffered image to the given width and height.
     *
     * @param image  Image that needs to be scaled.
     * @param width  Width of the new image.
     * @param height Height of the new image.
     * @return Scaled image.
     * @throws NullPointerException     If image is {@code null}.
     * @throws IllegalArgumentException If width or height is 0.
     */
    private static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        Image tmpImg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage returnImage = new BufferedImage(width, height, image.getType());

        Graphics2D g2d = returnImage.createGraphics();
        g2d.drawImage(tmpImg, 0, 0, null);
        g2d.dispose();

        return returnImage;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String imageFolder = req.getServletContext().getRealPath("/WEB-INF/slike");
        String imageCacheFolder = imageFolder + "/cache";
        BufferedImage image = getImage(imageFolder, imageCacheFolder, req.getParameter("image"));

//        image could not be loaded
        if (image == null) {
            resp.setStatus(404);
            return;
        }

//        write to output stream
        String imageFileType = req.getParameter("image").replaceFirst(".*\\.", "");
        resp.setHeader("Content-Type", "image/" + imageFileType);

        OutputStream outputStream = resp.getOutputStream();
        ImageIO.write(image, imageFileType, outputStream);
        outputStream.close();
    }
}
