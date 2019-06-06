package hr.fer.zemris.java.webapp2.servlets;

import hr.fer.zemris.java.webapp2.ContextConstants;
import hr.fer.zemris.java.webapp2.services.Renderer;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet that creates a Microsoft Excel document with table of powers on each page.
 * This takes three parameters:
 * 'n' in range [1, 5] (number of pages),
 * 'a' in range [-100, 100] (starting number),
 * 'b' in range [-100, 100] (ending number).
 *
 * @author Jan Capek
 */
public class PowersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int a, b, n;
        HSSFWorkbook workbook;
        try{
            a = Integer.parseInt(req.getParameter("a"));
            b = Integer.parseInt(req.getParameter("b"));
            n = Integer.parseInt(req.getParameter("n"));
            workbook = createExcelDocument(a, b, n);
        } catch (NullPointerException | IllegalArgumentException e) {
            req.setAttribute("message", "n must be in range [1, 5] and a & b must be in range [-100, 100] and a <= b");
            Renderer renderer = (Renderer) getServletContext().getAttribute(ContextConstants.RENDERER);
            renderer.render(req, resp, "mainLayout.jsp", "powers.jsp");
            return;
        }

        resp.setHeader("Content-Type", "application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
        workbook.write(resp.getOutputStream());
    }

    /**
     * Creates an Excel workbook.
     *
     * @param a Starting number.
     * @param b Ending number (inclusive).
     * @param n Number of sheets.
     * @return Excel workbook.
     * @throws IllegalArgumentException If arguments are invalid. n must be in range [1, 5] and a & b must be in range [-100, 100] and a <= b.
     */
    private HSSFWorkbook createExcelDocument(int a, int b, int n) {
        if(n < 1 || n > 5 || a < -100 || a > 100 || b < -100 || b > 100 || a > b) {
            throw new IllegalArgumentException("n must be in range [1, 5] and a & b must be in range [-100, 100] and a <= b");
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
        for(int i = 0; i < n; i++) {
            HSSFSheet sheet = workbook.createSheet(String.format("%d. sheet", i + 1));
            for(int j = a; j <= b; j++) {
                HSSFRow row = sheet.createRow(j - a);
                row.createCell(0).setCellValue(j);
                row.createCell(1).setCellValue(Math.pow(j, i + 1));
            }
        }
        return workbook;
    }

}
