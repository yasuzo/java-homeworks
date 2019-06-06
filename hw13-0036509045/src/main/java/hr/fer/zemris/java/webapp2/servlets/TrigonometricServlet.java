package hr.fer.zemris.java.webapp2.servlets;

import hr.fer.zemris.java.webapp2.ContextConstants;
import hr.fer.zemris.java.webapp2.services.Renderer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Servlet that calculates sin and cos in range and renders table result.
 * Range is given in GET parameters as 'a' and 'b'.
 *
 * @author Jan Capek
 */
public class TrigonometricServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String valueA = req.getParameter("a");
        String valueB = req.getParameter("b");

        int a = 0;
        int b = 360;

        try{
            a = Integer.parseInt(valueA);
        } catch (NumberFormatException | NullPointerException e) {

        }

        try {
            b = Integer.parseInt(valueB);
        } catch (NumberFormatException | NullPointerException e) {

        }

//        swap if a - b > 0
        if(a > b) {
            int temp = a;
            a = b;
            b = temp;
        }

//        b too large
        if (b > a + 720) {
            b = a + 720;
        }

//        fill lists
        List<Double> cosList = new ArrayList<>(b - a);
        List<Double> sinList = new ArrayList<>(b - a);
        fillWithTrigonometricValues(a, b, sinList, cosList);

//        set attributes
        req.setAttribute("a", a);
        req.setAttribute("b", b);
        req.setAttribute("sinList", sinList);
        req.setAttribute("cosList", cosList);

//        render
        ((Renderer)getServletContext().getAttribute(ContextConstants.RENDERER))
                .render(req, resp, "mainLayout.jsp", "trigonometric.jsp");
    }

    /**
     * Fills two lists with sin and cos values of angles in range [start, end].
     * Angles are in degrees.
     *
     * @param start Starting number.
     * @param end Ending number (inclusive).
     * @param sinList List which will be filled with sin values.
     * @param cosList List which will be filled with cos values.
     * @throws IllegalArgumentException If  start > end.
     * @throws NullPointerException If lists are {@code null}.
     */
    private void fillWithTrigonometricValues(int start, int end, List<Double> sinList, List<Double> cosList) {
        Objects.requireNonNull(sinList);
        Objects.requireNonNull(cosList);
        if(start > end) {
            throw new IllegalArgumentException("Start must be greater or equal to end");
        }
        for(int i = start; i <= end; i++) {
            double angleRad = Math.toRadians(i);
            sinList.add(Math.sin(angleRad));
            cosList.add(Math.cos(angleRad));
        }
    }
}
