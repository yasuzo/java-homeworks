package hr.fer.zemris.java.hw14.servlets;

import hr.fer.zemris.java.hw14.dao.DAOProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet that displays available polls.
 *
 * @author Jan Capek
 */
public class MainVotingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("polls", DAOProvider.getDao().getPolls());

        req.getRequestDispatcher("/WEB-INF/pages/polls.jsp").forward(req, resp);
    }
}
