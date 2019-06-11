package hr.fer.zemris.java.hw14.models;

import java.util.Objects;

/**
 * Model of a poll option.
 *
 * @author Jan Capek
 */
public class PollOption {
    private long id;
    private String title;
    private String link;
    private long pollId;
    private long voteCount;

    /**
     * Constructs a new poll option with given attributes.
     *
     * @param id        Option id.
     * @param title     Option title.
     * @param link      Link to option's video example.
     * @param pollId    Poll id of the option.
     * @param voteCount Vote count of the option.
     * @throws NullPointerException     If any of the objects is {@code null}.
     * @throws IllegalArgumentException If vote count is less than 0.
     */
    public PollOption(long id, String title, String link, long pollId, long voteCount) {
        this.id = id;
        this.title = Objects.requireNonNull(title);
        this.link = Objects.requireNonNull(link);
        this.pollId = pollId;
        setVoteCount(voteCount);
    }

    /**
     * Sets option's vote count.
     *
     * @param voteCount New vote count.
     * @throws IllegalArgumentException If vote count is less than 0.
     */
    public void setVoteCount(long voteCount) {
        if (voteCount < 0) {
            throw new IllegalArgumentException("Vote count cannot be less than 0.");
        }
        this.voteCount = voteCount;
    }

    /**
     * @return Option's id.
     */
    public long getId() {
        return id;
    }

    /**
     * @return Option's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return Link to option's video example.
     */
    public String getLink() {
        return link;
    }

    /**
     * @return Id of the poll whose option this is.
     */
    public long getPollId() {
        return pollId;
    }

    /**
     * @return Option's vote count.
     */
    public long getVoteCount() {
        return voteCount;
    }
}
