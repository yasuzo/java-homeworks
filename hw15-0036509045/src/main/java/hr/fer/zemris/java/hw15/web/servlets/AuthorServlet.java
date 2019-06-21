package hr.fer.zemris.java.hw15.web.servlets;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogUser;
import hr.fer.zemris.java.hw15.services.Renderer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet that lists all blogs written by the author in the url.
 *
 * @author Jan Capek
 */
public class AuthorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nick = (String) req.getAttribute("nick");

//        check if user exists
        BlogUser user = DAOProvider.getDAO().getBlogUserByNick(nick);
        if (user == null) {
            resp.setStatus(404);
            PrintWriter writer = resp.getWriter();
            writer.write("Error 404 - Page not found.");
            writer.close();
            return;
        }

//        show edit option if requested user is logged user
        if (nick.equals(req.getSession().getAttribute("current.user.nick"))) {
            req.setAttribute("showEditOption", true);
        }

        req.setAttribute("blogs", user.getBlogs());
        Renderer.getInstance().render(req, resp, "mainLayout.jsp", "author.jsp");
    }
}
