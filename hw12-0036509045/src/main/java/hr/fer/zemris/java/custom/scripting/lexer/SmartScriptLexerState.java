package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeration of {@link SmartScriptLexer}'s states.
 *
 * @author Jan Capek
 */
public enum SmartScriptLexerState {
    /**
     * State outside of tags "{$ $}".
     */
    BASIC,
    /**
     * State inside of tags "{$ $}".
     */
    TAG
}
