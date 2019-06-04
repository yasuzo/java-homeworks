package hr.fer.zemris.java.webapp2.essentials;

import hr.fer.zemris.java.webapp2.ServletContextAttributeConstants;
import hr.fer.zemris.java.webapp2.services.Renderer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * Main request listener used to initialize variables used by servlets.
 *
 * @author Jan Capek
 */
public class MainRequestListener implements ServletRequestListener {

    /**
     * Initializes objects used in servlets and stores them in the {@link ServletContext} as attributes.
     * All attributes whose names are in {@link ServletContextAttributeConstants} will be in ServletContext
     * instance and won't be null that is guaranteed.
     *
     * @param servletRequestEvent Servlet request event.
     * @throws NullPointerException If the event is {@code null}.
     */
    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        ServletContext servletContext = servletRequestEvent.getServletContext();

//        add renderer to context
        Renderer renderer = new Renderer("/WEB-INF/pages", "/WEB-INF/layouts");
        servletContext.setAttribute(ServletContextAttributeConstants.RENDERER, renderer);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

    }
}
