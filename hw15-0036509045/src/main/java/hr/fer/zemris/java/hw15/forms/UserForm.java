package hr.fer.zemris.java.hw15.forms;

/**
 * User form model.
 *
 * @author Jan Capek
 */
public class UserForm extends AbstractForm {
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String nick = "";
    private String password = "";

    /**
     * @return User's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets user's first name.
     *
     * @param firstName First name.
     */
    public void setFirstName(String firstName) {
        if (firstName == null) {
            return;
        }
        this.firstName = firstName;
    }

    /**
     * @return User's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets user's last name.
     *
     * @param lastName Last name.
     */
    public void setLastName(String lastName) {
        if (lastName == null) {
            return;
        }
        this.lastName = lastName;
    }

    /**
     * @return Email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets an email address.
     *
     * @param email Email address.
     */
    public void setEmail(String email) {
        if (email == null) {
            return;
        }
        this.email = email;
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
        if (nick == null) {
            return;
        }
        this.nick = nick;
    }

    /**
     * @return Password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets a password.
     *
     * @param password Password.
     */
    public void setPassword(String password) {
        if (password == null) {
            return;
        }
        this.password = password;
    }

    @Override
    public boolean isOk() {
        messages.clear();
        checkNameAndNick();
        checkEmail();
        checkPassword();
        return messages.isEmpty();
    }

    /**
     * Checks first name, last name and nickname and fills an error message list if something is wrong.
     */
    private void checkNameAndNick() {
        if (firstName.isBlank()) {
            addMessage("First name is not provided.");
        } else if (firstName.length() > 50) {
            addMessage("First name is too long.");
        }

        if (lastName.isBlank()) {
            addMessage("Last name is not provided.");
        } else if (lastName.length() > 50) {
            addMessage("Last name is too long.");
        }

        if (nick.isBlank()) {
            addMessage("Nickname is not provided.");
        } else if (nick.length() > 50) {
            addMessage("Nickname is too long.");
        }
    }

    /**
     * Checks an email address and fills an error message list if something is wrong.
     */
    private void checkEmail() {
        if (email.isBlank()) {
            addMessage("Email is not provided.");
        } else if (email.length() > 50) {
            addMessage("Nickname is too long.");
        } else if (email.matches("[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~.]+@[a-zA-Z0-9\\-]+\\.[a-zA-Z0-9\\-]+") == false) {
            addMessage("Email is invalid. It should be in form: john.doe@example.com");
        }
    }

    /**
     * Checks a password and fills an error message list if something is wrong.
     */
    private void checkPassword() {
        if (password.length() == 0) {
            addMessage("Password is not provided.");
        }
    }
}
