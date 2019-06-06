package hr.fer.zemris.java.webapp2;

/**
 * Class containing constants for {@link javax.servlet.ServletContext#getAttribute(String)} method.
 *
 * @author Jan Capek
 */
public class ContextConstants {

    /**
     * Context attribute name under which {@link hr.fer.zemris.java.webapp2.services.Renderer} is stored.
     */
    public static String RENDERER = "renderer";

    /**
     * Context attribute name under which server start time is stored.
     * Time will be in milliseconds and of type {@link Long}.
     */
    public static final String START_TIME = "serverStartTime";
}
