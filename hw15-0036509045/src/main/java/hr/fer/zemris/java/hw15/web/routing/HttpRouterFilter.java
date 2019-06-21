package hr.fer.zemris.java.hw15.web.routing;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Router filter, this should ALWAYS be last filter of the web application. Otherwise, other filters will not be executed.
 * This class will call {@link HttpServlet#service(ServletRequest, ServletResponse)} method on servlet mapped to the current route.
 * Which servlet will be invoked depends on {@link HttpRouter}. If no mapping was found, default servlet will be invoked.
 *
 * @author Jan Capek
 */
public class HttpRouterFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest == false || response instanceof HttpServletResponse == false) {
            throw new ServletException("Request and/or response is not an instance of HttpServletRequest/HttpServletResponse");
        }
        HttpServlet handler = HttpRouter.getInstance().resolve((HttpServletRequest) request);
        if (handler == null) {
            new DefaultServlet().service(request, response);
        } else {
            handler.service(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
