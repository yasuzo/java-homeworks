package hr.fer.zemris.java.hw15.web.routing;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Default servlet which will be invoked by {@link HttpRouterFilter} in case no mapping for current route was found.
 */
class DefaultServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendResponse(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendResponse(resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendResponse(resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendResponse(resp);
    }

    /**
     * Sends 404 response.
     *
     * @param resp Response used for sending data.
     * @throws IOException In case of an error.
     */
    private void sendResponse(HttpServletResponse resp) throws IOException {
        resp.setStatus(404);
        PrintWriter writer = resp.getWriter();
        writer.println("Error 404 - page not found.");
        writer.close();
    }
}
