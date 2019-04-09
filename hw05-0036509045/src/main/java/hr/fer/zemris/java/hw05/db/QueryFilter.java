package hr.fer.zemris.java.hw05.db;

import java.util.List;
import java.util.Objects;

/**
 * Object that filters records by list of conditional expressions.
 *
 * @author Jan Capek
 */
public class QueryFilter implements IFilter {

    private List<ConditionalExpression> conditionalExpressions;

    /**
     * Creates a new query filter with all conditional expressions elements need to fulfill.
     *
     * @param conditionalExpressions Conditional expression to fulfill.
     * @throws NullPointerException If given list is {@code null}.
     */
    public QueryFilter(List<ConditionalExpression> conditionalExpressions) {
        this.conditionalExpressions = Objects.requireNonNull(conditionalExpressions);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException If {@code record} is {@code null}.
     */
    @Override
    public boolean accepts(StudentRecord record) {
        Objects.requireNonNull(record);
        for (ConditionalExpression exp : conditionalExpressions) {
            boolean evaluated = exp.getComparisonOperator().satisfied(
                    exp.getFieldGetter().get(record),
                    exp.getStringLiteral()
            );
            if (evaluated == false) {
                return false;
            }
        }
        return true;
    }
}
