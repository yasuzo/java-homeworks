package hr.fer.zemris.java.hw15.web.init;

import hr.fer.zemris.java.hw15.dao.jpa.JPAEMFProvider;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Initialization listener used to initialize JPA.
 */
@WebListener
public class JPAInitialization implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("baza.podataka.za.blog");
        sce.getServletContext().setAttribute("my.application.emf", emf);
        JPAEMFProvider.setEmf(emf);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAEMFProvider.setEmf(null);
        EntityManagerFactory emf = (EntityManagerFactory) sce.getServletContext().getAttribute("my.application.emf");
        if (emf != null) {
            emf.close();
        }
    }
}