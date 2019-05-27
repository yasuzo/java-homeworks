package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Exception thrown by {@link MethodExecutor} in case nonexistent method is called.
 *
 * @author Jan Capek
 */
public class MethodNotFoundException extends RuntimeException {
    public MethodNotFoundException() {
    }

    public MethodNotFoundException(String message) {
        super(message);
    }

    public MethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotFoundException(Throwable cause) {
        super(cause);
    }
}
