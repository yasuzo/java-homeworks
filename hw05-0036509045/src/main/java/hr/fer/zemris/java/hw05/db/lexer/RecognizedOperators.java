package hr.fer.zemris.java.hw05.db.lexer;

import java.util.HashSet;
import java.util.Set;

/**
 * Class containing recognized logical and comparison operators in queries used by {@link QueryLexer}.
 *
 * @author Jan Capek
 */
class RecognizedOperators {

    /**
     * Contains recognized logical operators.
     */
    static final Set<String> LOGICAL_OPERATORS;

    /**
     * Contains recognized comparison operators.
     */
    static final Set<String> COMPARISON_OPERATORS;

    static {
        LOGICAL_OPERATORS = new HashSet<>();
        LOGICAL_OPERATORS.add("and");

        COMPARISON_OPERATORS = new HashSet<>();
        COMPARISON_OPERATORS.add("=");
        COMPARISON_OPERATORS.add("!=");
        COMPARISON_OPERATORS.add(">=");
        COMPARISON_OPERATORS.add("<=");
        COMPARISON_OPERATORS.add("<");
        COMPARISON_OPERATORS.add(">");
        COMPARISON_OPERATORS.add("like");

    }
}
