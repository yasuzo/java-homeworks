package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception thrown by {@link SmartScriptParser} in case an error occurred.
 *
 * @author Jan Capek
 */
public class SmartScriptParserException extends RuntimeException {

    public SmartScriptParserException() {
    }

    public SmartScriptParserException(String message) {
        super(message);
    }
}
