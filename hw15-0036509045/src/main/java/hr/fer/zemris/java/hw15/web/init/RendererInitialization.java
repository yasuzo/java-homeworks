package hr.fer.zemris.java.hw15.web.init;

import hr.fer.zemris.java.hw15.services.Renderer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Renderer initialization listener.
 *
 * @author Jan Capek
 */
@WebListener
public class RendererInitialization implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Renderer.getInstance().setConfig("/WEB-INF/pages", "/WEB-INF/layouts");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
