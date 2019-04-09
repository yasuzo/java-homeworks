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
    static final Set<String> logicalOperators;

    /**
     * Contains recognized comparison operators.
     */
    static final Set<String> comparisonOperators;

    static {
        logicalOperators = new HashSet<>();
        logicalOperators.add("and");

        comparisonOperators = new HashSet<>();
        comparisonOperators.add("==");
        comparisonOperators.add("!=");
        comparisonOperators.add(">=");
        comparisonOperators.add("<=");
        comparisonOperators.add("<");
        comparisonOperators.add(">");
        comparisonOperators.add("like");

    }
}
