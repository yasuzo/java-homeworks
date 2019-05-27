package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Exception thrown when a variable is referenced in smart script but not initialized.
 *
 * @author Jan Capek
 */
public class VariableNotFoundException extends RuntimeException {
    public VariableNotFoundException() {
    }

    public VariableNotFoundException(String message) {
        super(message);
    }
}
