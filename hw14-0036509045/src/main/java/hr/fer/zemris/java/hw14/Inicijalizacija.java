package hr.fer.zemris.java.hw14;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@WebListener
public class Inicijalizacija implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

//        load db settings
        Properties dbSettings = null;
        try {
            dbSettings = loadProperties(Path.of(servletContextEvent.getServletContext().getRealPath("/WEB-INF/dbsettings.properties")));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

//        create connection url
        String connectionURL = String.format(
                "jdbc:derby://%s:%s/%s;user=%s;password=%s",
                dbSettings.getProperty("host"),
                dbSettings.getProperty("port"),
                dbSettings.getProperty("name"),
                dbSettings.getProperty("user"),
                dbSettings.getProperty("password")
        );

        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        try {
            comboPooledDataSource.setDriverClass("org.apache.derby.jdbc.ClientDriver");
        } catch (PropertyVetoException e1) {
            throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
        }
        comboPooledDataSource.setJdbcUrl(connectionURL);

//        init database
        try {
            Connection connection = comboPooledDataSource.getConnection();
            initDB(connection);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        servletContextEvent.getServletContext().setAttribute("hr.fer.zemris.dbpool", comboPooledDataSource);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
        if (cpds != null) {
            try {
                DataSources.destroy(cpds);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Constructs a property object containing properties read from a file.
     *
     * @param propertyFile File containing desired properties.
     * @return Properties object containing properties from the given file.
     * @throws IOException          If file could not be read.
     * @throws NullPointerException If given path is {@code null}.
     */
    private Properties loadProperties(Path propertyFile) throws IOException {
        Objects.requireNonNull(propertyFile);
        Properties props = new Properties();
        try (Reader reader = Files.newBufferedReader(propertyFile)) {
            props.load(reader);
        } catch (IOException e) {
            throw new IOException(e);
        }
        return props;
    }

    /**
     * Initializes db's tables.
     *
     * @param connection Database connection.
     * @throws NullPointerException If given connection is {@code null}.
     * @throws SQLException         In case an error occurred.
     */
    private void initDB(Connection connection) throws SQLException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE Polls" +
                            " (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                            " title VARCHAR(150) NOT NULL," +
                            " message CLOB(2048) NOT NULL" +
                            ")"
            );
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32") == false) {
                throw new SQLException(e);
            }
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE PollOptions" +
                            " (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                            " optionTitle VARCHAR(100) NOT NULL," +
                            " optionLink VARCHAR(150) NOT NULL," +
                            " pollID BIGINT," +
                            " votesCount BIGINT," +
                            " FOREIGN KEY (pollID) REFERENCES Polls(id)" +
                            ")"
            );
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32") == false) {
                throw new SQLException(e);
            }
        }

        PreparedStatement preparedStatement = connection.prepareStatement("Select count(*) from Polls");
        ResultSet result = preparedStatement.executeQuery();
        if (result.next() && result.getLong(1) == 0) {
            insertDefaultDBData(connection);
        }
        result.close();
        preparedStatement.close();
    }

    /**
     * Inserts default data into tables.
     *
     * @param dbConnection Connection to the database.
     * @throws SQLException         In case db is inaccessible.
     * @throws NullPointerException If the connection is {@code null}.
     */
    private void insertDefaultDBData(Connection dbConnection) throws SQLException {
        List<Long> generatedKeys = new ArrayList<>(2);

//        insert first poll
        PreparedStatement preparedStatement = dbConnection.prepareStatement(
                "insert into Polls (title, message) values " +
                        "('Glasanje 1', 'Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!')",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        preparedStatement.executeUpdate();
        ResultSet key = preparedStatement.getGeneratedKeys();
        key.next();
        generatedKeys.add(key.getLong(1));
        key.close();
        preparedStatement.close();

//        insert second poll
        preparedStatement = dbConnection.prepareStatement(
                "insert into Polls (title, message) values " +
                        "('Glasanje 2', 'Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!')",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        preparedStatement.executeUpdate();
        key = preparedStatement.getGeneratedKeys();
        key.next();
        generatedKeys.add(key.getLong(1));
        key.close();

//        insert poll options
        for (Long pollId : generatedKeys) {
            String insertPollOptionsStatement = "insert into PollOptions (optionTitle, optionLink, pollID, votesCount) values " +
                    "('The Beatles', 'https://www.youtube.com/watch?v=z9ypq6_5bsg', " + pollId + ", 0)," +
                    "('The Platters', 'https://www.youtube.com/watch?v=H2di83WAOhU', " + pollId + ", 0)," +
                    "('The Beach Boys', 'https://www.youtube.com/watch?v=2s4slliAtQU', " + pollId + ", 0)," +
                    "('The Four Seasons', 'https://www.youtube.com/watch?v=y8yvnqHmFds', " + pollId + ", 0)," +
                    "('The Marcels', 'https://www.youtube.com/watch?v=qoi3TH59ZEs', " + pollId + ", 0)," +
                    "('The Everly Brothers', 'https://www.youtube.com/watch?v=tbU3zdAgiX8', " + pollId + ", 0)," +
                    "('The Mammas And The Papas', 'https://www.youtube.com/watch?v=N-aK6JnyFmk', " + pollId + ", 0)";
            PreparedStatement preparedStatement2 = dbConnection.prepareStatement(insertPollOptionsStatement);
            preparedStatement2.executeUpdate();
            preparedStatement2.close();
        }
    }

}