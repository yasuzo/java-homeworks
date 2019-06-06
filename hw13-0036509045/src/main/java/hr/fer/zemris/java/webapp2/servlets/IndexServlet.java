package hr.fer.zemris.java.webapp2.servlets;

import hr.fer.zemris.java.webapp2.ContextConstants;
import hr.fer.zemris.java.webapp2.GlobalConstants;
import hr.fer.zemris.java.webapp2.services.Renderer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Index servlet that serves index.jsp page.
 *
 * @author Jan Capek
 */
public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(GlobalConstants.TITLE, "Index");
        Renderer renderer = (Renderer) getServletContext().getAttribute(ContextConstants.RENDERER);
        renderer.render(req, resp, "mainLayout.jsp", "index.jsp");
    }
}
