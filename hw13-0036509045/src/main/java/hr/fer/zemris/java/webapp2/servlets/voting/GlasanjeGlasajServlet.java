package hr.fer.zemris.java.webapp2.servlets.voting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * Servlet that actually votes for a band.
 * This takes one argument 'id' which has to be an integer and votes for the band with that id.
 *
 * @author Jan Capek
 */
public class GlasanjeGlasajServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stringBandInfoPath = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
        Path bandInfo = Path.of(stringBandInfoPath);
        String stringBandVotesPath = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        Path votesInfo = Path.of(stringBandVotesPath);

//        read band data
        Map<Integer, VotingDBUtil.BandData> bands = VotingDBUtil.getBandData(bandInfo, votesInfo);
        try {
            VotingDBUtil.BandData band = bands.get(Integer.parseInt((String)req.getParameter("id")));
            band.setVotes(band.getVotes() + 1);
        } catch (NullPointerException | NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/glasanje");
            return;
        }

        VotingDBUtil.saveBandData(votesInfo, bands);
        resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
    }
}
