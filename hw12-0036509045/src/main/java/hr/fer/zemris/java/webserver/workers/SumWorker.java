package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Simple worker, accepts two parameters a & b, adds them together and displays the result.
 *
 * @author Jan Capek
 */
public class SumWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        String paramA = context.getParameter("a");
        String paramB = context.getParameter("b");
        int a, b;
        try {
            a = Integer.parseInt(paramA);
        } catch (NumberFormatException | NullPointerException e) {
            a = 1;
        }
        try {
            b = Integer.parseInt(paramB);
        } catch (NumberFormatException | NullPointerException e) {
            b = 2;
        }
        int sum = a + b;
        context.setTemporaryParameter("varA", Integer.toString(a));
        context.setTemporaryParameter("varB", Integer.toString(b));
        context.setTemporaryParameter("zbroj", Integer.toString(sum));
        context.setTemporaryParameter("imgName", sum % 2 == 0 ? "2pac.gif" : "rave.gif");

        context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
    }
}
