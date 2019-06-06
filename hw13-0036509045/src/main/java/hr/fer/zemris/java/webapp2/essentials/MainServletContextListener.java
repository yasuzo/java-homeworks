package hr.fer.zemris.java.webapp2.essentials;

import hr.fer.zemris.java.webapp2.ContextConstants;
import hr.fer.zemris.java.webapp2.services.Renderer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MainServletContextListener implements ServletContextListener {

    /**
     * Initializes objects used in servlets and stores them in the {@link ServletContext} as attributes.
     * All attributes whose names are in {@link ContextConstants} will be in ServletContext
     * instance and won't be null that is guaranteed.
     *
     * @param servletContextEvent Servlet request event.
     * @throws NullPointerException If the event is {@code null}.
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();

//        add renderer to context
        Renderer renderer = new Renderer("/WEB-INF/pages", "/WEB-INF/layouts");
        servletContext.setAttribute(ContextConstants.RENDERER, renderer);
        servletContext.setAttribute(ContextConstants.START_TIME, System.currentTimeMillis());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
