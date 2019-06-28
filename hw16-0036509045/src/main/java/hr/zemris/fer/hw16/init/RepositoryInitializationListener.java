package hr.zemris.fer.hw16.init;

import hr.zemris.fer.hw16.repositories.ImageFileRepository;
import hr.zemris.fer.hw16.repositories.RepositoryProvider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.nio.file.Paths;

/**
 * Initializes repositories.
 *
 * @author Jan Capek
 */
@WebListener
public class RepositoryInitializationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        RepositoryProvider.setImageRepository(
                new ImageFileRepository(Paths.get(sce.getServletContext().getRealPath("/WEB-INF/opisnik.txt")))
        );
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
