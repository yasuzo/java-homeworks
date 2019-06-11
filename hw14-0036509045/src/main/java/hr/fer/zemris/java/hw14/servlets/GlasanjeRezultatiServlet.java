package hr.fer.zemris.java.hw14.servlets;

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
 * Servlet that renders voting results.
 *
 * @author Jan Capek
 */
public class GlasanjeRezultatiServlet extends HttpServlet {
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

//        read options
        List<PollOption> sorted = DAOProvider.getDao().getPollOptions(pollId).stream()
                .sorted(Comparator.comparingLong(PollOption::getVoteCount).reversed())
                .collect(Collectors.toList());

        long maxVotes = sorted.size() > 0 ? sorted.get(0).getVoteCount() : 0;
        List<PollOption> winners = sorted.stream()
                .filter(option -> option.getVoteCount() == maxVotes)
                .collect(Collectors.toList());

        req.setAttribute("options", sorted);
        req.setAttribute("winners", winners);
        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
    }
}
