package hr.fer.zemris.java.hw15.web.routing;

import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Route mapping class which encapsulates a route pattern and its handler.
 *
 * @author Jan Capek
 */
public class RouteMapping {

    /**
     * Internal route regex pattern.
     *
     * @see java.util.regex.Pattern
     */
    private String routeRegex;

    /**
     * List of attributes that need to be parsed from url.<br>
     * E. g. for {@code "/blogs/{user}/{blogId}"} list will contain {@code {"user", "blogId"}}.
     */
    private List<String> urlAttributes;

    /**
     * Handler that will handle requests.
     */
    private HttpServlet handler;

    /**
     * Constructs a new RouterMapping which maps a given route path with an {@link HttpServlet}.
     *
     * @param routePattern Route path pattern. This can be in format {@code "/blogs/{:user}/{:blogId}"} but can also contain
     *                     regular regex expressions. <br>
     *                     In above example, {@code user} and {@code blogId} will be extracted from url and will be put in
     *                     {@link javax.servlet.http.HttpServletRequest}'s parameter map.
     * @param handler      Http handler for a route.
     * @throws NullPointerException                   If any of the parameters is {@code null}.
     * @throws java.util.regex.PatternSyntaxException If pattern regex is invalid.
     */
    public RouteMapping(String routePattern, HttpServlet handler) {
        this.handler = Objects.requireNonNull(handler);
        setRouteRegex(routePattern);
    }

    /**
     * Parses a route path and returns a map with parsed path parameters.
     *
     * @param routePath Route path.
     * @return Map containing parameters parsed from url.
     * @throws NullPointerException If given string is {@code null}.
     * @throws RuntimeException     If given string does not match mapping's regex.
     */
    public Map<String, String> getPathParameters(String routePath) {
        if (routePath.matches(routeRegex) == false) {
            throw new RuntimeException("Path does not match a mapping.");
        }

        Matcher m = Pattern.compile(routeRegex).matcher(routePath);
        m.matches();

//        fill param map
        Map<String, String> params = new HashMap<>(urlAttributes.size() * 2);
        int i = 0;
        for (String param : urlAttributes) {
            params.put(param, m.group(++i));
        }

        return params;
    }

    /**
     * @return Names of required attributes that need to be parsed.
     */
    protected List<String> getUrlAttributes() {
        return urlAttributes;
    }

    /**
     * @return Route regex pattern.
     */
    protected String getRouteRegex() {
        return routeRegex;
    }

    /**
     * Sets route regex pattern.
     *
     * @param routePattern Route pattern given in the constructor.
     * @throws NullPointerException                   If given pattern is {@code null}.
     * @throws java.util.regex.PatternSyntaxException If pattern regex is invalid.
     */
    private void setRouteRegex(String routePattern) {
//        replace all capturing groups with non capturing ones
        routePattern = Pattern.compile("\\((?!\\?(?::|<?[!=]))(.*?)\\)")
                .matcher(routePattern)
                .replaceAll(matchResult -> String.format("(?:%s)", matchResult.group(1)));

//        create regex pattern for route
        this.routeRegex = routePattern.replaceAll("\\{:.+?}", "([^/]+)");

//        get parameter names
        Pattern pattern = Pattern.compile("\\{:(.+?)}");
        Matcher m = pattern.matcher(routePattern);
        urlAttributes = m.results()
                .map(matchResult -> matchResult.group(1))
                .collect(Collectors.toList());
    }

    /**
     * @return Servlet mapped to the route.
     */
    protected HttpServlet getHandler() {
        return handler;
    }
}
