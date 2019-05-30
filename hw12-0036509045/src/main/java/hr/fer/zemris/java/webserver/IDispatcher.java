package hr.fer.zemris.java.webserver;

/**
 * Interface for request dispatching.
 *
 * @author Jan Capek
 */
public interface IDispatcher {

    /**
     * Dispatches a request for given url.
     *
     * @param urlPath Requested url.
     * @throws Exception In case an exception occurred while processing a request.
     */
    void dispatchRequest(String urlPath) throws Exception;
}