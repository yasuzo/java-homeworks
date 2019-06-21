package hr.fer.zemris.java.hw15.web.init;

import hr.fer.zemris.java.hw15.web.routing.HttpRouter;
import hr.fer.zemris.java.hw15.web.routing.RouteMapping;
import hr.fer.zemris.java.hw15.web.servlets.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Initializes router routes.
 */
@WebListener
public class RouterInitialization implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HttpRouter router = HttpRouter.getInstance();
        router.registerRoute(new RouteMapping("/servleti/main", new MainServlet()));
        router.registerRoute(new RouteMapping("/(index(.jsp)?)?", new RedirectToMainServlet()));
        router.registerRoute(new RouteMapping("/servleti/register", new RegistrationServlet()));
        router.registerRoute(new RouteMapping("/servleti/logout", new LogoutServlet()));
        router.registerRoute(new RouteMapping("/servleti/author/{:nick}", new AuthorServlet()));
        router.registerRoute(new RouteMapping("/servleti/author/{:nick}/new", new NewBlogServlet()));
        router.registerRoute(new RouteMapping("/servleti/author/{:nick}/edit/{:blogId}", new EditBlogServlet()));
        router.registerRoute(new RouteMapping("/servleti/author/{:nick}/{:blogId}", new ShowBlogServlet()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
