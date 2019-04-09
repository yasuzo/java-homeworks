package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Types of {@link Token}.
 *
 * @author Jan Capek
 */
public enum TokenType {
    /**
     * String literal.
     */
    STRING,
    /**
     * Record attribute.
     */
    ATTRIBUTE,
    /**
     * Comparison operator {<, >, <=, >=, !=, =, like}
     */
    COMPARISON_OPERATOR,
    /**
     * Logic operator { AND }
     */
    LOGIC_OPERATOR,
    /**
     * End of file/string.
     */
    EOF
}
