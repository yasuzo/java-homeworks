package hr.fer.zemris.java.webapp2.servlets;

import hr.fer.zemris.java.webapp2.ContextConstants;
import hr.fer.zemris.java.webapp2.services.Renderer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for picking background color of the page.
 *
 * @author Jan Capek
 */
public class ColorPickerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Renderer renderer = (Renderer)getServletContext().getAttribute(ContextConstants.RENDERER);
        renderer.render(req, resp, "mainLayout.jsp", "colors.jsp");
    }
}
