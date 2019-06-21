package hr.fer.zemris.java.hw15.forms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Model of an abstract form. This offers a method for checking validity of the data
 * and getting and adding of error messages.
 *
 * @author Jan Capek
 */
public abstract class AbstractForm {

    /**
     * List of error messages.
     */
    protected List<String> messages = new ArrayList<>(10);

    /**
     * @return List of error messages.
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Adds a message to internal list.
     *
     * @param message Message.
     * @throws NullPointerException If given message is {@code null}.
     */
    public void addMessage(String message) {
        Objects.requireNonNull(message);
        messages.add(message);
    }

    /**
     * Validates form data and returns {@code true} if data is valid or {@code false} if data is invalid.
     *
     * @return {@code true} if data is valid, {@code false} otherwise.
     */
    public abstract boolean isOk();
}
