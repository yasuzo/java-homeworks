package hr.fer.zemris.java.hw15.dao.jpa;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter that initializes JPA on request and closes it after processing request.
 */
public class JPAFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            JPAEMProvider.close();
        }
    }

    @Override
    public void destroy() {
    }

}