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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main servlet for voting.
 *
 * @author Jan Capek
 */
public class GlasanjeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stringPath = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
        Path dataPath = Path.of(stringPath);

//        read band data
        Map<Integer, VotingDBUtil.BandData> bands = VotingDBUtil.getBandData(dataPath, null);
        req.setAttribute(
                "bands",
                bands.values().stream()
                        .sorted(Comparator.comparingInt(VotingDBUtil.BandData::getId))
                        .collect(Collectors.toList())
        );

//        render
        ((Renderer) getServletContext().getAttribute(ContextConstants.RENDERER))
                .render(req, resp, "mainLayout.jsp", "glasanjeIndex.jsp");
    }
}
