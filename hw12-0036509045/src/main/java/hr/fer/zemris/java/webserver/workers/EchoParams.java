package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Simple worker that outputs request parameters.
 *
 * @author Jan Capek
 */
public class EchoParams implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        context.setMimeType("text/html");
        context.write("<html>\n<body>\n<table>\n");
        for (String param : context.getParameterNames()) {
            context.write(String.format("<tr><td>%s</td><td>%s</td></tr>\n", param, context.getParameter(param)));
        }
        context.write("</table>\n</body>\n</html>");
    }
}
