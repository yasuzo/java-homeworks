package hr.fer.zemris.java.webapp2.servlets.voting;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Servlet that serves voting data in *.xls format.
 *
 * @author Jan Capek
 */
public class VotingXlsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Path bandInfo = Path.of(req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt"));
        Path votesInfo = Path.of(req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt"));

        List<VotingDBUtil.BandData> sorted = VotingDBUtil.getSortedBandData(bandInfo, votesInfo);

        HSSFWorkbook workbook = createExcelDocument(sorted);
        resp.setHeader("Content-Type", "application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
        workbook.write(resp.getOutputStream());
    }

    /**
     * Creates an xls document from given collection data.
     *
     * @param bands Collection of bands.
     * @return Excel workbook.
     * @throws NullPointerException If given collection or any of its elements is {@code null}.
     */
    private HSSFWorkbook createExcelDocument(Collection<VotingDBUtil.BandData> bands) {
        Objects.requireNonNull(bands);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("Name");
        row.createCell(1).setCellValue("Votes");
        int i = 1;
        for (VotingDBUtil.BandData band : bands) {
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(band.getName());
            row.createCell(1).setCellValue(band.getVotes());
            i++;
        }
        return workbook;
    }
}
