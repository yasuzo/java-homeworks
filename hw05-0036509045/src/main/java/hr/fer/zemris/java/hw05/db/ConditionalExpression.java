package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Class that models a conditional expression.
 *
 * @author Jan Capek
 */
public class ConditionalExpression {

    private IFieldValueGetter valueGetter;
    private String stringLiteral;
    private IComparisonOperator operator;

    /**
     * Constructs a new conditional expression which contains a comparison operator,
     * field getter and string literal.
     *
     * @param valueGetter   Getter for a field that needs to be compared.
     * @param stringLiteral String that a field needs to be compared against.
     * @param operator      Comparison operator.
     * @throws NullPointerException If any of the arguments are {@code null}.
     */
    public ConditionalExpression(IFieldValueGetter valueGetter, String stringLiteral, IComparisonOperator operator) {
        this.valueGetter = Objects.requireNonNull(valueGetter);
        this.stringLiteral = Objects.requireNonNull(stringLiteral);
        this.operator = Objects.requireNonNull(operator);
    }

    /**
     * @return Field value getter.
     */
    public IFieldValueGetter getFieldGetter() {
        return valueGetter;
    }

    /**
     * @return String literal that a field needs to be compared against.
     */
    public String getStringLiteral() {
        return stringLiteral;
    }

    /**
     * @return Comparison operator.
     */
    public IComparisonOperator getComparisonOperator() {
        return operator;
    }
}
