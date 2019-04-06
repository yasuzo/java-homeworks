package hr.fer.zemris.lsystems.impl.commands;

/**
 * Exception thrown by commands in case an error occurred.
 *
 * @author Jan Capek
 */
public class CommandException extends RuntimeException {

    public CommandException() {
    }

    public CommandException(String message) {
        super(message);
    }
}
