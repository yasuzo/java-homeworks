package hr.fer.zemris.java.hw15.forms;

import java.util.Objects;

/**
 * Model of a blog form.
 *
 * @author Jan Capek
 */
public class BlogForm extends AbstractForm {

    private String title = "";
    private String text = "";

    /**
     * Constructs a new blog form and initializes it.
     *
     * @param title Blog title.
     * @param text  Blog body.
     */
    public BlogForm(String title, String text) {
        setTitle(title);
        setText(text);
    }

    /**
     * Default constructor. All fields will be empty strings.
     */
    public BlogForm() {

    }

    /**
     * @return Blog title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets blog's title.
     *
     * @param title Blog title.
     */
    public void setTitle(String title) {
        this.title = Objects.requireNonNullElse(title, "");
    }

    /**
     * @return Blog text.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets blog's text.
     *
     * @param text Blog text.
     */
    public void setText(String text) {
        this.text = Objects.requireNonNullElse(text, "");
    }

    @Override
    public boolean isOk() {
        messages.clear();
        if (title.isBlank()) {
            addMessage("Title not provided.");
        }
        return messages.isEmpty();
    }
}
