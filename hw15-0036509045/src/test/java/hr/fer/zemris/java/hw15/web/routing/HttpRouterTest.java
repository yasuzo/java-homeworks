package hr.fer.zemris.java.hw15.web.routing;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServlet;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class HttpRouterTest {

    @Test
    public void registerRouteTest() {
        HttpRouter router = HttpRouter.getInstance();
        router.registerRoute(new RouteMapping("/blogs/{:user}/{:blogId}", new DummyHttpServlet()));
        router.registerRoute(new RouteMapping("/blogs/a/b/c", new DummyHttpServlet()));
        router.registerRoute(new RouteMapping("/", new DummyHttpServlet()));
        router.registerRoute(new RouteMapping("/.*", new DummyHttpServlet()));
    }

    @Test
    public void routeMappingTest() {
        RouteMapping m1 = new RouteMapping("/(index)/blogs/{:user}/{:blogId}", new DummyHttpServlet());
        assertIterableEquals(Arrays.asList("user", "blogId"), m1.getUrlAttributes());
        assertEquals("/(?:index)/blogs/([^/]+)/([^/]+)", m1.getRouteRegex());

        RouteMapping m2 = new RouteMapping("/blogs/{:user}/{:blogId}/(.*)", new DummyHttpServlet());
        assertIterableEquals(Arrays.asList("user", "blogId"), m2.getUrlAttributes());
        assertEquals("/blogs/([^/]+)/([^/]+)/(?:.*)", m2.getRouteRegex());

        RouteMapping m3 = new RouteMapping("/blogs/a/b/c", new DummyHttpServlet());
        assertIterableEquals(Collections.emptyList(), m3.getUrlAttributes());
        assertEquals("/blogs/a/b/c", m3.getRouteRegex());

    }

    private static class DummyHttpServlet extends HttpServlet {

    }
}