package hr.fer.zemris.java.webapp2.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet that stores 'pickedBgColor' in session.
 *
 * @author Jan Capek
 */
public class SetColorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String color = req.getParameter("color");
        if(color == null || color.matches("[0-9a-fA-F]{6}") == false) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.getSession().setAttribute("pickedBgCol", color);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
