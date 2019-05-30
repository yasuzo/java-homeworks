package hr.fer.zemris.java.webserver;

/**
 * Interface that models web worker.
 *
 * @author Jan Capek
 */
public interface IWebWorker {

    /**
     * Processes a request with given context.
     *
     * @param context Request context.
     * @throws Exception Thrown in case an error occurred.
     */
    void processRequest(RequestContext context) throws Exception;
}