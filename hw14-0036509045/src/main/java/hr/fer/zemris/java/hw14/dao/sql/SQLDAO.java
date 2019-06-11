package hr.fer.zemris.java.hw14.dao.sql;

import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.models.Poll;
import hr.fer.zemris.java.hw14.models.PollOption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *
 * @author marcupic
 */
public class SQLDAO implements DAO {

    @Override
    public List<Poll> getPolls() {
        List<Poll> result = new ArrayList<>();
        Connection dbConnection = SQLConnectionProvider.getConnection();
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("select id, title, message from polls");
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                try {
                    while (resultSet.next()) {
                        Poll poll = new Poll(
                                resultSet.getLong("id"),
                                resultSet.getString("title"),
                                resultSet.getString("message"));
                        result.add(poll);
                    }
                } finally {
                    resultSet.close();
                }
            } finally {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return result;
    }

    @Override
    public List<PollOption> getPollOptions(long pollId) {
        List<PollOption> result = new ArrayList<>();
        Connection dbConnection = SQLConnectionProvider.getConnection();
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(
                    "select id, optionTitle, optionLink, pollID, votesCount " +
                            "from PollOptions " +
                            "where pollID=?"
            );
            preparedStatement.setLong(1, pollId);
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                try {
                    while (resultSet.next()) {
                        PollOption option = new PollOption(
                                resultSet.getLong(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getLong(4),
                                resultSet.getLong(5));
                        result.add(option);
                    }
                } finally {
                    resultSet.close();
                }
            } finally {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return result;
    }

    @Override
    public void updatePollOption(PollOption option) {
        Objects.requireNonNull(option);
        Connection dbConnection = SQLConnectionProvider.getConnection();
        PreparedStatement statement = null;
        try {
            statement = dbConnection.prepareStatement(
                    "update PollOptions " +
                            "set optionTitle=?, optionLink=?, pollID=?, votesCount=? " +
                            "where id=?"
            );
            statement.setString(1, option.getTitle());
            statement.setString(2, option.getLink());
            statement.setLong(3, option.getPollId());
            statement.setLong(4, option.getVoteCount());
            statement.setLong(5, option.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            try {
                statement.close();
            } catch (Exception ignorable) {
            }
        }
    }

    @Override
    public PollOption getPollOption(long optionId) {
        Connection dbConnection = SQLConnectionProvider.getConnection();
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(
                    "select id, optionTitle, optionLink, pollID, votesCount " +
                            "from PollOptions " +
                            "where id=?"
            );
            preparedStatement.setLong(1, optionId);
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                try {
                    if (resultSet.next()) {
                        PollOption option = new PollOption(
                                resultSet.getLong(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getLong(4),
                                resultSet.getLong(5));
                        return option;
                    }
                } finally {
                    resultSet.close();
                }
            } finally {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return null;
    }
}