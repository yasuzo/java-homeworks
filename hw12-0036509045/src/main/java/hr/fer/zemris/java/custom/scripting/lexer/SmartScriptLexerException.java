package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Exception thrown by {@link SmartScriptLexer} in case an error occurred.
 *
 * @author Jan Capek
 */
public class SmartScriptLexerException extends RuntimeException {

    public SmartScriptLexerException() {
    }

    public SmartScriptLexerException(String message) {
        super(message);
    }
}
