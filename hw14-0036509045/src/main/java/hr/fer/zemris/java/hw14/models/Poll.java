package hr.fer.zemris.java.hw14.models;

/**
 * Model of a voting poll.
 *
 * @author Jan Capek
 */
public class Poll {
    private long id;
    private String title;
    private String message;

    /**
     * Constructs a new poll with given attributes.
     *
     * @param id      Poll id.
     * @param title   Poll title.
     * @param message Poll message.
     * @throws NullPointerException If title or message is {@code null}.
     */
    public Poll(long id, String title, String message) {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    /**
     * @return Poll's id.
     */
    public long getId() {
        return id;
    }

    /**
     * @return Poll's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return Poll's message.
     */
    public String getMessage() {
        return message;
    }
}
