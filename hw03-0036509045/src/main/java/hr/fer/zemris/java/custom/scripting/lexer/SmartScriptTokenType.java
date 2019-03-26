package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeration of token types used by {@link SmartScriptLexer}.
 *
 * @author Jan Capek
 */
public enum SmartScriptTokenType {
    OPEN_TAG_BRACKET, // "{$"
    CLOSED_TAG_BRACKET, // "$}"
    KEYWORD, // for, end, =...
    IDENTIFIER, // variable
    STRING,
    INTEGER_CONSTANT,
    DOUBLE_CONSTANT,
    FUNCTION,
    OPERATOR, // '+', '-', '^', '/', '*'
    NORMAL_TEXT, // text outside of tags
    EOF
}
