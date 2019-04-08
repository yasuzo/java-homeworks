package hr.fer.zemris.java.hw05.db;

/**
 * Functional interface for comparing a value against an expression.
 *
 * @author Jan Capek
 */
public interface IComparisonOperator {

    /**
     * Compares a string value against an expression.
     *
     * @param value      String to compare.
     * @param expression Expression to compare against.
     * @return {@code true} if value satisfies an expression, {@code false} otherwise.
     */
    boolean satisfied(String value, String expression);
}
