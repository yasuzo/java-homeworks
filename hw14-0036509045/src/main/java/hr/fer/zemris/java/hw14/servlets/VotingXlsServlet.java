package hr.fer.zemris.java.hw14.servlets;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.models.PollOption;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Servlet that serves voting data in *.xls format.
 *
 * @author Jan Capek
 */
public class VotingXlsServlet extends HttpServlet {
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
//        read band data
        List<PollOption> sorted = DAOProvider.getDao().getPollOptions(pollId).stream()
                .sorted(Comparator.comparingLong(PollOption::getVoteCount).reversed())
                .collect(Collectors.toList());

        HSSFWorkbook workbook = createExcelDocument(sorted);
        resp.setHeader("Content-Type", "application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
        workbook.write(resp.getOutputStream());
    }

    /**
     * Creates an xls document from given collection data.
     *
     * @param options Collection of options.
     * @return Excel workbook.
     * @throws NullPointerException If given collection or any of its elements is {@code null}.
     */
    private HSSFWorkbook createExcelDocument(Collection<PollOption> options) {
        Objects.requireNonNull(options);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("Name");
        row.createCell(1).setCellValue("Votes");
        int i = 1;
        for (PollOption option : options) {
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(option.getTitle());
            row.createCell(1).setCellValue(option.getVoteCount());
            i++;
        }
        return workbook;
    }
}
