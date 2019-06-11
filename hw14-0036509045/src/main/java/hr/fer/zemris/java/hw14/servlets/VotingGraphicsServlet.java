package hr.fer.zemris.java.hw14.servlets;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.models.Poll;
import hr.fer.zemris.java.hw14.models.PollOption;
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
import java.util.List;

/**
 * Servlet that will draw a pie chart of voting data.
 *
 * @author Jan Capek
 */
public class VotingGraphicsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long pollId;
        try {
            pollId = Long.parseLong(req.getParameter("pollID"));
        } catch (Exception e) {
            req.setAttribute("message", "GET param 'pollID' is missing or is invalid.");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
            return;
        }

//        get poll
        Poll poll = DAOProvider.getDao().getPoll(pollId);

//        read options
        List<PollOption> options = DAOProvider.getDao().getPollOptions(pollId);

        resp.setContentType("image/png");
        OutputStream outputStream = resp.getOutputStream();

        PieDataset dataset = createDataset(options);
        JFreeChart chart = createChart(dataset, poll == null ? "NonexistentPoll" : poll.getTitle());

        ChartUtils.writeChartAsPNG(outputStream, chart, 700, 400);
    }

    /**
     * Creates a dataset from band data.
     *
     * @param options List of poll options.
     * @throws NullPointerException If given map or any of its elements is {@code null}.
     */
    private PieDataset createDataset(List<PollOption> options) {
        DefaultPieDataset result = new DefaultPieDataset();
        for (PollOption option : options) {
            result.setValue(option.getTitle(), option.getVoteCount());
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
