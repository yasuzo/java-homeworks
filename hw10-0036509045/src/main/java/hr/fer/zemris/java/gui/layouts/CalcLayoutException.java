package hr.fer.zemris.java.gui.layouts;

/**
 * Exception thrown by {@link CalcLayout} in case an illegal operation was performed
 * e.g. adding a component on taken place or on invalid place.
 *
 * @author Jan Capek
 */
public class CalcLayoutException extends RuntimeException {
    public CalcLayoutException() {
    }

    public CalcLayoutException(String message) {
        super(message);
    }
}
