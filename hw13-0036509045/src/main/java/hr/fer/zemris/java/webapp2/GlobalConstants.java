package hr.fer.zemris.java.webapp2;

/**
 * Constants used in {@link javax.servlet.http.HttpServletRequest#setAttribute(String, Object)} as attribute name.
 *
 * @author Jan Capek
 */
public class GlobalConstants {

    /**
     * Page title key.
     */
    public static final String TITLE = "title";

    /**
     * Key under which path to *.jsp file whose contents need to be inserted in another *.jsp file will be stored.<br>
     * This is used for rendering a page inside page layout.
     */
    public static final String INCLUDE_PAGE_BODY = "pageToRender";
}
