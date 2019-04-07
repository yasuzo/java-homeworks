package hr.fer.zemris.lsystems.impl.util;

/**
 * Exception thrown by the {@link SettingsParser} in case an error occurred.
 */
public class ParserException extends RuntimeException {
    public ParserException() {
    }

    public ParserException(String message) {
        super(message);
    }
}
