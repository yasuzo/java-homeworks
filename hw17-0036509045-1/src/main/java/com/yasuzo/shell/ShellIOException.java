package com.yasuzo.shell;

/**
 * Exception thrown by the {@link Environment} in case an error occurred.
 *
 * @author Jan Capek
 */
public class ShellIOException extends RuntimeException {
    public ShellIOException() {
    }

    public ShellIOException(String message) {
        super(message);
    }
}
