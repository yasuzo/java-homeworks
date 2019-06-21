package hr.fer.zemris.java.hw15.web.routing;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Simple http router.
 *
 * @author Jan Capek
 */
public class HttpRouter {

    private static HttpRouter router;
    /**
     * List of route mappings.
     */
    private List<RouteMapping> routes;

    /**
     * Private constructor.
     */
    private HttpRouter() {
    }

    /**
     * @return Instance of the router.
     */
    public static HttpRouter getInstance() {
        return router = router == null ? new HttpRouter() : router;
    }

    /**
     * Registers a new route mapping.
     *
     * @param mapping New mapping.
     * @throws NullPointerException If given mapping is {@code null}.
     */
    public void registerRoute(RouteMapping mapping) {
        if (routes == null) {
            routes = new ArrayList<>();
        }
        routes.add(Objects.requireNonNull(mapping));
    }

    /**
     * Resolve method parses request route, extracts route parameters and determines which {@link HttpServlet}
     * should process given request and returns it. Parsed parameters will be available through
     * {@link HttpServletRequest#getAttribute(String)}.
     *
     * @param request Http request.
     * @return Servlet mapped to process current request or {@code null} if no servlet is mapped to current route.
     * @throws NullPointerException If request is {@code null}.
     */
    public HttpServlet resolve(HttpServletRequest request) {
        Objects.requireNonNull(request);
        if (routes == null) {
            return null;
        }

//        create route string
        String route = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());

//        todo: Remove following line after you tested everything
        System.out.println("ROUTE: " + route);

//        get servlet
        for (RouteMapping mapping : routes) {
            if (route.matches(mapping.getRouteRegex()) == false) {
                continue;
            }

//            add parsed path attributes
            Map<String, String> pathParams = mapping.getPathParameters(route);
            pathParams.forEach(request::setAttribute);

            return mapping.getHandler();
        }
        return null;
    }
}
