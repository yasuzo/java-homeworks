package hr.fer.zemris.java.hw14.servlets;

import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.models.PollOption;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet that actually votes for a band.
 *
 * @author Jan Capek
 */
public class GlasanjeGlasajServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long optionId;
        try {
            optionId = Long.parseLong(req.getParameter("optionID"));
        } catch (Exception e) {
            req.setAttribute("message", "GET param 'optionID' is missing or invalid.");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
            return;
        }

        DAO dao = DAOProvider.getDao();
        PollOption option = dao.getPollOption(optionId);
        if(option == null) {
            req.setAttribute("message", "Requested option not found.");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
            return;
        }
        option.setVoteCount(option.getVoteCount() + 1);
        dao.updatePollOption(option);
        resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + option.getPollId());
    }
}
