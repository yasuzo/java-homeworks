package hr.fer.zemris.java.hw15.forms;

import java.util.Objects;

/**
 * Model of a login form.
 *
 * @author Jan Capek
 */
public class LoginForm extends AbstractForm {

    private String nick = "";
    private String password = "";

    /**
     * Constructs a form and initializes it.
     *
     * @param nick     Nickname.
     * @param password Password.
     */
    public LoginForm(String nick, String password) {
        setNick(nick);
        setPassword(password);
    }

    /**
     * Default constructor. All fields will be empty strings.
     */
    public LoginForm() {

    }

    /**
     * @return Nickname.
     */
    public String getNick() {
        return nick;
    }

    /**
     * Sets nickname.
     *
     * @param nick Nickname.
     */
    public void setNick(String nick) {
        this.nick = Objects.requireNonNullElse(nick, "");
    }

    /**
     * @return Password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password Password.
     */
    public void setPassword(String password) {
        this.password = Objects.requireNonNullElse(password, "");
    }

    @Override
    public boolean isOk() {
        messages.clear();
        if (nick.isBlank()) {
            addMessage("Nickname not provided.");
        }
        if (password.isEmpty()) {
            addMessage("Password not provided.");
        }
        return messages.isEmpty();
    }
}
