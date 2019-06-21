package hr.fer.zemris.java.hw15.services;

import hr.fer.zemris.java.hw15.GlobalConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Object of this class is used to render *.jsp files inside the layout on http request.
 *
 * @author Jan Capek
 */
public class Renderer {

    /**
     * Instance of Renderer for singleton pattern.
     */
    private static Renderer renderer;

    /**
     * Base directory for pages.
     */
    private String pagesDirectory;

    /**
     * Base directory for layouts.
     */
    private String layoutsDirectory;

    /**
     * Flag that indicates if the renderer is configured.
     */
    private boolean isConfigured;

    /**
     * If necessary this will create an instance of {@link Renderer} but if an instance has already been created,
     * this will return existing one.
     *
     * @return An instance of renderer.
     */
    public static Renderer getInstance() {
        return renderer = renderer == null ? new Renderer() : renderer;
    }

    /**
     * Configures renderer to use given directories as base for pages and layouts.
     *
     * @param pagesDirectory   Base directory for pages. This will be base for
     *                         {@link Renderer#render(HttpServletRequest, HttpServletResponse, String, String)}
     *                         method which will concat {@code pageFileName} on <b>pagesDirectory</b>.<br>
     *                         This must be an absolute path <b>from webapp standpoint</b>.
     * @param layoutsDirectory Base directory for layouts. This will be base for
     *                         {@link Renderer#render(HttpServletRequest, HttpServletResponse, String, String)}
     *                         method which will concat {@code layoutFileName} on <b>layoutsDirectory</b>.<br>
     *                         This must be an absolute path <b>from webapp standpoint</b>.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public void setConfig(String pagesDirectory, String layoutsDirectory) {
        this.pagesDirectory = Objects.requireNonNull(pagesDirectory);
        this.layoutsDirectory = Objects.requireNonNull(layoutsDirectory);
        if (pagesDirectory.endsWith("/") == false) {
            this.pagesDirectory = pagesDirectory + "/";
        }
        if (layoutsDirectory.endsWith("/") == false) {
            this.layoutsDirectory = layoutsDirectory + "/";
        }
        isConfigured = true;
    }


    private Renderer() {
    }

    /**
     * Renders a page inside the layout with given name.
     *
     * @param req            Request object.
     * @param resp           Response object.
     * @param layoutFileName Name of the layout used including a file extension.
     * @param pageFileName   Name of the page that needs to be rendered inside the layout including a file extension.
     * @throws IllegalStateException If this renderer was not configured.
     * @throws NullPointerException  If any of the arguments are {@code null} except pageName which can be {@code null}.
     * @throws ServletException      In case an error occurred.
     * @throws IOException           If files could not be read.
     */
    public void render(HttpServletRequest req, HttpServletResponse resp, String layoutFileName, String pageFileName) throws ServletException, IOException {
        if (isConfigured == false) {
            throw new IllegalStateException("Renderer not configured.");
        }
        if (req == null || resp == null || layoutFileName == null) {
            throw new NullPointerException("Arguments must not be null.");
        }
        req.setAttribute(GlobalConstants.INCLUDE_PAGE_BODY, pagesDirectory + pageFileName);
        req.getRequestDispatcher(layoutsDirectory + layoutFileName).forward(req, resp);
    }
}
