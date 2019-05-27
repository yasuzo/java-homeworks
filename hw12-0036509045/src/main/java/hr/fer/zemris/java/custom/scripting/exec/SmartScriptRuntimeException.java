package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Exception thrown in case of an exception occurred while executing smart script.
 *
 * @author Jan Capek
 */
public class SmartScriptRuntimeException extends RuntimeException {
    public SmartScriptRuntimeException() {
    }

    public SmartScriptRuntimeException(String message) {
        super(message);
    }

    public SmartScriptRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmartScriptRuntimeException(Throwable cause) {
        super(cause);
    }
}
