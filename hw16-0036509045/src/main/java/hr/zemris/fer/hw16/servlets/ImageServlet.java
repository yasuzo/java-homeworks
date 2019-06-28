package hr.zemris.fer.hw16.servlets;

import hr.zemris.fer.hw16.utilities.ImageLoader;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Servlet that serves requested image. Which image is requested is indicated in 'image' parameter.
 *
 * @author Jan Capek
 */
public class ImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String imageName = req.getParameter("image");

        if (imageName == null) {
            resp.setStatus(404);
            return;
        }

        BufferedImage image = ImageLoader.load(req.getServletContext().getRealPath("/WEB-INF/slike"), imageName);
        if (image == null) {
            resp.setStatus(404);
            return;
        }

        String imageFileType = imageName.replaceFirst(".*\\.", "");
        resp.setHeader("Content-Type", "image/" + imageFileType);

        OutputStream outputStream = resp.getOutputStream();
        ImageIO.write(image, imageFileType, outputStream);
        outputStream.close();
    }
}
