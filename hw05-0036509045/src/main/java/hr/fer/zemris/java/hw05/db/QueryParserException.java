package hr.fer.zemris.java.hw05.db;

/**
 * Exception thrown by {@link QueryParser} if a query is invalid.
 *
 * @author Jan Capek
 */
public class QueryParserException extends RuntimeException {
    public QueryParserException() {
    }

    public QueryParserException(String message) {
        super(message);
    }
}
