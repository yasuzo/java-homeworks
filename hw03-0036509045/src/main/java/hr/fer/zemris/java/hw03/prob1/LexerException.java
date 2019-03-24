package hr.fer.zemris.java.hw03.prob1;

/**
 * Exception thrown by {@link Lexer} if an error occurred.
 *
 * @author Jan Capek
 */
public class LexerException extends RuntimeException {

    public LexerException() {}

    public LexerException(String message) {
        super(message);
    }
}
