package hr.fer.zemris.java.hw14.dao;

import hr.fer.zemris.java.hw14.models.Poll;
import hr.fer.zemris.java.hw14.models.PollOption;

import java.util.List;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 *
 * @author marcupic
 */
public interface DAO {

    /**
     * @return List of all polls.
     * @throws DAOException In case an error occurred.
     */
    List<Poll> getPolls();

    /**
     * Finds all poll options associated with given poll id.
     *
     * @param pollId Poll id of the options that need to be retrieved.
     * @return List of poll options associated with given poll id.
     * @throws DAOException In case an error occurred.
     */
    List<PollOption> getPollOptions(long pollId);

    /**
     * Updates given poll option in the database.
     *
     * @param option Poll option that needs to be updated.
     * @throws NullPointerException In case given option is {@code null}.
     * @throws DAOException         In case an error occurred.
     */
    void updatePollOption(PollOption option);

    /**
     * Returns a poll option with given id.
     *
     * @param optionId Id of the poll option that is to be retrieved.
     * @return Poll option with given id if found; null otherwise.
     * @throws DAOException In case an error occurred.
     */
    PollOption getPollOption(long optionId);

    /**
     * Returns a poll with given id if there is any or null.
     *
     * @param pollId Id of the poll.
     * @return Poll if found; null otherwise.
     * @throws DAOException In case an error occurred.
     */
    Poll getPoll(long pollId);
}