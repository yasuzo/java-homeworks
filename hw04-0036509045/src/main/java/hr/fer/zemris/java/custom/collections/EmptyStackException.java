package hr.fer.zemris.java.custom.collections;

/**
 * Exception thrown when value is tried to be retrieved from empty stack.
 *
 * @author Jan Capek
 */
public class EmptyStackException extends RuntimeException {

    public EmptyStackException() {
    }

    public EmptyStackException(String message) {
        super(message);
    }
}
