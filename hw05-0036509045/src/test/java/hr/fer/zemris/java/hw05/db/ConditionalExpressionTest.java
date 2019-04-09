package hr.fer.zemris.java.hw05.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConditionalExpressionTest {

    IComparisonOperator operator = ComparisonOperators.EQUALS;
    String literal = "Marko";
    IFieldValueGetter fieldGetter = FieldValueGetters.FIRST_NAME;

    @Test
    void constructorTest() {
        new ConditionalExpression(fieldGetter, literal, operator);
    }

    @Test
    void constructorTestNull1() {
        assertThrows(NullPointerException.class, () -> new ConditionalExpression(null, literal, operator));
    }

    @Test
    void constructorTestNull2() {
        assertThrows(NullPointerException.class, () -> new ConditionalExpression(fieldGetter, null, operator));
    }

    @Test
    void constructorTestNull3() {
        assertThrows(NullPointerException.class, () -> new ConditionalExpression(fieldGetter, literal, null));
    }

    @Test
    void expressionEvaluationTest() {
        ConditionalExpression expression = new ConditionalExpression(fieldGetter, literal, operator);
        StudentRecord record = new StudentRecord("0000000000", "Marko", "Markic", 5);

        assertTrue(
                expression
                        .getComparisonOperator()
                        .satisfied(
                                expression.getFieldGetter().get(record),
                                expression.getStringLiteral()));
    }

}