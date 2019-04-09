package hr.fer.zemris.java.hw05.db;

/**
 * Class containing comparison operators.
 *
 * @author Jan Capek
 */
public class ComparisonOperators {
    public static final IComparisonOperator LESS;
    public static final IComparisonOperator LESS_OR_EQUALS;
    public static final IComparisonOperator GREATER;
    public static final IComparisonOperator GREATER_OR_EQUALS;
    public static final IComparisonOperator EQUALS;
    public static final IComparisonOperator NOT_EQUALS;
    public static final IComparisonOperator LIKE;

    static {
        LESS = (value, expression) -> value.compareTo(expression) < 0;
        LESS_OR_EQUALS = (value, expression) -> value.compareTo(expression) <= 0;
        GREATER = (value, expression) -> value.compareTo(expression) > 0;
        GREATER_OR_EQUALS = (value, expression) -> value.compareTo(expression) >= 0;
        EQUALS = (value, expression) -> value.compareTo(expression) == 0;
        NOT_EQUALS = (value, expression) -> value.compareTo(expression) != 0;

        LIKE = (value, expression) -> {
            if (expression.matches(".*\\*.*\\*.*")) {
                throw new IllegalArgumentException("Multiple wildcards in an expression.");
            }
            expression = expression.replace("*", ".*");
            return value.matches(expression);
        };
    }
}
