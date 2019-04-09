package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Exception thrown by {@link QueryLexer} in case an error occurred.
 *
 * @author Jan Capek
 */
public class QueryLexerException extends RuntimeException {
    public QueryLexerException() {
    }

    public QueryLexerException(String message) {
        super(message);
    }
}
