package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker for home page.
 */
public class Home implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        String bg = context.getPersistentParameter("bgcolor");
        context.setTemporaryParameter("background", bg == null ? "7F7F7F" : bg);

        context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
    }
}
