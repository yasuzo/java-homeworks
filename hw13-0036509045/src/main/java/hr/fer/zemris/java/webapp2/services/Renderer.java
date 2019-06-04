package hr.fer.zemris.java.webapp2.services;

import hr.fer.zemris.java.webapp2.GlobalConstants;

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
     * Base directory for pages.
     */
    private String pagesDirectory;

    /**
     * Base directory for layouts.
     */
    private String layoutsDirectory;

    /**
     * Constructs a new renderer that uses given directories as base for pages and layouts.
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
    public Renderer(String pagesDirectory, String layoutsDirectory) {
        this.pagesDirectory = Objects.requireNonNull(pagesDirectory);
        this.layoutsDirectory = Objects.requireNonNull(layoutsDirectory);
        if (pagesDirectory.endsWith("/") == false) {
            this.pagesDirectory = pagesDirectory + "/";
        }
        if (layoutsDirectory.endsWith("/") == false) {
            this.layoutsDirectory = layoutsDirectory + "/";
        }
    }

    /**
     * Renders a page inside the layout with given name.
     *
     * @param req            Request object.
     * @param resp           Response object.
     * @param layoutFileName Name of the layout used including a file extension.
     * @param pageFileName   Name of the page that needs to be rendered inside the layout including a file extension.
     * @throws NullPointerException If any of the arguments are {@code null} except pageName which can be {@code null}.
     * @throws ServletException     In case an error occurred.
     * @throws IOException          If files could not be read.
     */
    public void render(HttpServletRequest req, HttpServletResponse resp, String layoutFileName, String pageFileName) throws ServletException, IOException {
        if (req == null || resp == null || layoutFileName == null) {
            throw new NullPointerException("Arguments must not be null.");
        }
        req.setAttribute(GlobalConstants.INCLUDE_PAGE_BODY, pagesDirectory + pageFileName);
        req.getRequestDispatcher(layoutsDirectory + layoutFileName).forward(req, resp);
    }
}
