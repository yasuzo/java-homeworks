package hr.fer.zemris.java.hw14.servlets;

import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.models.Poll;
import hr.fer.zemris.java.hw14.models.PollOption;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main servlet for voting.
 *
 * @author Jan Capek
 */
public class GlasanjeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long pollId;
        try {
            pollId = Long.parseLong(req.getParameter("pollID"));
        } catch (Exception e) {
            req.setAttribute("message", "GET param 'pollID' is missing or invalid.");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
            return;
        }

        DAO dao = DAOProvider.getDao();

//        get poll
        Poll poll = dao.getPoll(pollId);
        if(poll == null) {
            req.setAttribute("message", "Requested poll not found.");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
            return;
        }

//        get options
        List<PollOption> options = dao.getPollOptions(pollId).stream()
                .sorted(Comparator.comparingLong(PollOption::getId))
                .collect(Collectors.toList());

//        set attributes
        req.setAttribute("poll", poll);
        req.setAttribute("options", options);
//        render
        req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
    }
}
