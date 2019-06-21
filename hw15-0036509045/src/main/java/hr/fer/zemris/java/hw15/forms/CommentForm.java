package hr.fer.zemris.java.hw15.forms;


import java.util.Objects;

public class CommentForm extends AbstractForm {

    private String email = "";
    private String message = "";

    /**
     * Constructs a form and initializes it.
     *
     * @param email   Comment creator's email.
     * @param message Comment message.
     */
    public CommentForm(String email, String message) {
        setEmail(email);
        setMessage(message);
    }

    /**
     * Default constructor. All fields will be empty strings.
     */
    public CommentForm() {
    }

    /**
     * @return Comment message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets comment message.
     *
     * @param message Comment message.
     */
    public void setMessage(String message) {
        this.message = Objects.requireNonNullElse(message, "");
    }

    /**
     * @return Email of the comment creator.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email of the comment creator.
     *
     * @param email Creator's email.
     */
    public void setEmail(String email) {
        this.email = Objects.requireNonNullElse(email, "");
    }

    @Override
    public boolean isOk() {
        messages.clear();

//        check email
        if (email.isBlank()) {
            addMessage("Email is not provided.");
        } else if (email.length() > 50) {
            addMessage("Nickname is too long.");
        } else if (email.matches("[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~.]+@[a-zA-Z0-9\\-]+\\.[a-zA-Z0-9\\-]+") == false) {
            addMessage("Email is invalid. It should be in form: john.doe@example.com");
        }

//        check message
        if (message.isBlank()) {
            addMessage("Message not provided.");
        }
        return messages.isEmpty();
    }
}
