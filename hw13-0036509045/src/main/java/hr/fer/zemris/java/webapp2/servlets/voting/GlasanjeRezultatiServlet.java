package hr.fer.zemris.java.webapp2.servlets.voting;

import hr.fer.zemris.java.webapp2.ContextConstants;
import hr.fer.zemris.java.webapp2.services.Renderer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servlet that renders voting results.
 *
 * @author Jan Capek
 */
public class GlasanjeRezultatiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Path bandInfo = Path.of(req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt"));
        Path votesInfo = Path.of(req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt"));

//        read band data
        List<VotingDBUtil.BandData> sorted = VotingDBUtil.getSortedBandData(bandInfo, votesInfo);

        int maxVotes = sorted.size() > 0 ? sorted.get(0).getVotes() : 0;
        List<VotingDBUtil.BandData> winners = sorted.stream()
                .filter(bandData -> bandData.getVotes() == maxVotes)
                .collect(Collectors.toList());

        req.setAttribute("bands", sorted);
        req.setAttribute("winners", winners);
        ((Renderer) getServletContext().getAttribute(ContextConstants.RENDERER))
                .render(req, resp, "mainLayout.jsp", "glasanjeRez.jsp");
    }
}
