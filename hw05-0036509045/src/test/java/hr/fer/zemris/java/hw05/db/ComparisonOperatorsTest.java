package hr.fer.zemris.java.hw05.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComparisonOperatorsTest {

    @Test
    void testLess() {
        String a = "aab";
        String b = "aan";
        IComparisonOperator operator = ComparisonOperators.LESS;
        assertTrue(operator.satisfied(a, b));
    }

    @Test
    void testLessOrEquals() {
        String a = "aab";
        String b = "aan";
        IComparisonOperator operator = ComparisonOperators.LESS_OR_EQUALS;
        assertTrue(operator.satisfied(a, b));
        assertTrue(operator.satisfied(a, "aab"));
    }

    @Test
    void testGreater() {
        String a = "aaa";
        String b = "ccc";
        IComparisonOperator operator = ComparisonOperators.GREATER;
        assertFalse(operator.satisfied(a, b));
        assertFalse(operator.satisfied("aaa", "aaa"));
        assertTrue(operator.satisfied(b, a));
    }

    @Test
    void testGreaterOrEqual() {
        String a = "aaa";
        String b = "ccc";
        IComparisonOperator operator = ComparisonOperators.GREATER_OR_EQUALS;
        assertFalse(operator.satisfied(a, b));

        assertTrue(operator.satisfied("aaa", "aaa"));
        assertTrue(operator.satisfied(b, a));
    }

    @Test
    void testEquals() {
        String a = "aaa";
        String b = "ccc";
        IComparisonOperator operator = ComparisonOperators.EQUALS;
        assertFalse(operator.satisfied(a, b));
        assertTrue(operator.satisfied("aaa", "aaa"));
        assertFalse(operator.satisfied(b, a));
    }

    @Test
    void testNotEquals() {
        String a = "aaa";
        String b = "ccc";
        IComparisonOperator operator = ComparisonOperators.NOT_EQUALS;
        assertTrue(operator.satisfied(a, b));
        assertFalse(operator.satisfied("aaa", "aaa"));
        assertTrue(operator.satisfied(b, a));
    }

    @Test
    void testLike() {
        String a = "aabbbbaa";
        IComparisonOperator operator = ComparisonOperators.LIKE;
        assertTrue(operator.satisfied(a, "aa*aa"));
        assertTrue(operator.satisfied(a, "aab*"));
        assertTrue(operator.satisfied(a, "*bbbaa"));
        assertTrue(operator.satisfied(a, "aabbbbaa"));
        assertFalse(operator.satisfied(a, "*bbb"));
        assertFalse(operator.satisfied(a, "aabbbbaaa"));
    }

    @Test
    void testLikeInvalidExpression() {
        IComparisonOperator operator = ComparisonOperators.LIKE;

        assertThrows(IllegalArgumentException.class, () -> operator.satisfied("aaas", "da**das"));
    }
}