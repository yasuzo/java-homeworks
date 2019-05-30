package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker that changes background color.
 *
 * @author Jan Capek
 */
public class BgColorWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        String color = context.getParameter("bgcolor");
        if(color == null || color.matches("[0-9a-fA-F]{6}") == false) {
            context.getDispatcher().dispatchRequest("/private/pages/bgColorNotSet.smscr");
            return;
        }
        context.setPersistentParameter("bgcolor", color);
        context.getDispatcher().dispatchRequest("/private/pages/bgColorSet.smscr");
    }
}
