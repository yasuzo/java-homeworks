package hr.fer.zemris.java.webapp2.servlets.voting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map;

/**
 * Servlet that will draw a pie chart of voting data.
 *
 * @author Jan Capek
 */
public class VotingGraphicsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Path bandInfo = Path.of(req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt"));
        Path votesInfo = Path.of(req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt"));

//        read band data
        Map<Integer, VotingDBUtil.BandData> bands = VotingDBUtil.getBandData(bandInfo, votesInfo);

        resp.setContentType("image/png");
        OutputStream outputStream = resp.getOutputStream();

        PieDataset dataset = createDataset(bands);
        JFreeChart chart = createChart(dataset, "Glasanje");

        ChartUtils.writeChartAsPNG(outputStream, chart, 700, 400);
    }

    /**
     * Creates a dataset from band data.
     *
     * @param bands Map with band data.
     * @throws NullPointerException If given map or any of its elements is {@code null}.
     */
    private PieDataset createDataset(Map<Integer, VotingDBUtil.BandData> bands) {
        DefaultPieDataset result = new DefaultPieDataset();
        for(VotingDBUtil.BandData band : bands.values()) {
            result.setValue(band.getName(), band.getVotes());
        }
        return result;
    }

    /**
     * Creates a chart.
     */
    private JFreeChart createChart(PieDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createPieChart3D(
                title,          // chart title
                dataset,        // data
                true,    // include legend
                true,
                false
        );
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }
}
