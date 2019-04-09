package hr.fer.zemris.java.hw05.db;

import hr.fer.zemris.collections.ObjectStack;
import hr.fer.zemris.java.hw05.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw05.db.lexer.QueryLexerException;
import hr.fer.zemris.java.hw05.db.lexer.Token;
import hr.fer.zemris.java.hw05.db.lexer.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Instances of this class are used to parse queries.
 *
 * @author Jan Capek
 */
public class QueryParser {

    /**
     * Map of recognized attributes and their value getters.
     */
    private static final Map<String, IFieldValueGetter> ATTRIBUTE_VALUE_GETTERS = new HashMap<>();

    /**
     * Map of comparison operators with their string representations as keys.
     */
    private static final Map<String, IComparisonOperator> COMPARISON_OPERATORS = new HashMap<>();

    static {
        ATTRIBUTE_VALUE_GETTERS.put("firstName", FieldValueGetters.FIRST_NAME);
        ATTRIBUTE_VALUE_GETTERS.put("lastName", FieldValueGetters.LAST_NAME);
        ATTRIBUTE_VALUE_GETTERS.put("jmbag", FieldValueGetters.JMBAG);

        COMPARISON_OPERATORS.put("=", ComparisonOperators.EQUALS);
        COMPARISON_OPERATORS.put(">=", ComparisonOperators.GREATER_OR_EQUALS);
        COMPARISON_OPERATORS.put("<=", ComparisonOperators.LESS_OR_EQUALS);
        COMPARISON_OPERATORS.put("!=", ComparisonOperators.NOT_EQUALS);
        COMPARISON_OPERATORS.put(">", ComparisonOperators.GREATER);
        COMPARISON_OPERATORS.put("<", ComparisonOperators.LESS);
        COMPARISON_OPERATORS.put("like", ComparisonOperators.LIKE);

    }

    /**
     * Lexer for query.
     */
    private QueryLexer lexer;

    /**
     * Conditional expressions parsed in a query.
     */
    private List<ConditionalExpression> expressions;

    /**
     * Constructs a new query parser for given query.
     *
     * @param query Query to parse.
     * @throws NullPointerException If {@code query} is {@code null}.
     * @throws QueryParserException If query was invalid.
     */
    public QueryParser(String query) {
        expressions = new ArrayList<>();
        lexer = new QueryLexer(query);
        try {
            parse();
        } catch (QueryLexerException e) {
            throw new QueryParserException("Invalid query.");
        }
    }

    /**
     * Checks if query is direct.
     * Query is direct if only jmbag was queried with operator '='.
     *
     * @return {@code true} if query is direct, {@code false} otherwise.
     */
    public boolean isDirectQuery() {
        if (expressions.size() != 1) {
            return false;
        }
        ConditionalExpression exp = expressions.get(0);
        if (exp.getComparisonOperator() == ComparisonOperators.EQUALS
                && exp.getFieldGetter() == FieldValueGetters.JMBAG) {
            return true;
        }
        return false;
    }

    /**
     * Returns queried JMBAG if query is direct.
     *
     * @return Queried JMBAG.
     * @throws IllegalStateException If query isn't direct query.
     */
    public String getQueriedJMBAG() {
        if (isDirectQuery() == false) {
            throw new IllegalStateException("Query isn't direct query.");
        }
        return expressions.get(0).getStringLiteral();
    }

    /**
     * @return List of conditional expressions found in query.
     */
    public List<ConditionalExpression> getQuery() {
        return expressions;
    }

    /**
     * Parses a query.
     *
     * @throws QueryParserException If query was invalid.
     * @throws QueryLexerException  If some character sequences weren't recognized.
     */
    private void parse() {
        ObjectStack<Token> stack = new ObjectStack<>();
        for (Token t = lexer.next(); t.getType() != TokenType.EOF; t = lexer.next()) {
            TokenType type = t.getType();

//            attribute
            if (type == TokenType.ATTRIBUTE) {
                parseAttribute(stack, t);
                continue;
            }
//            string literal
            if (type == TokenType.STRING) {
                parseString(stack, t);
                continue;
            }
//            comparison operator
            if (type == TokenType.COMPARISON_OPERATOR) {
                parseComparisonOperator(stack, t);
                continue;
            }
//            logical operator AND
            expressions.add(createConditionalExpression(stack));
        }

        expressions.add(createConditionalExpression(stack));

        if (stack.size() != 0) {
            throw new QueryParserException("Invalid query.");
        }
    }

    /**
     * Creates a new conditional expression.
     *
     * @param stack Helper stack containing tokens.
     * @return New conditional expression.
     * @throws QueryParserException If there are not enough elements on the stack for expression.
     */
    private ConditionalExpression createConditionalExpression(ObjectStack<Token> stack) {
        if (stack.size() != 3) {
            throw new QueryParserException("Invalid query.");
        }
        String literal = stack.pop().getValue();
        IComparisonOperator operator = COMPARISON_OPERATORS.get(stack.pop().getValue());
        IFieldValueGetter valueGetter = ATTRIBUTE_VALUE_GETTERS.get(stack.pop().getValue());
        return new ConditionalExpression(valueGetter, literal, operator);
    }

    /**
     * Parses attribute token.
     *
     * @param stack Helper stack containing tokens.
     * @param t     Token to parse.
     * @throws QueryParserException If token wasn't expected or if attribute was not recognized.
     */
    private void parseAttribute(ObjectStack<Token> stack, Token t) {
        if (stack.size() != 0 || ATTRIBUTE_VALUE_GETTERS.get(t.getValue()) == null) {
            throw new QueryParserException("Invalid query.");
        }
        stack.push(t);
    }

    /**
     * Parses string literal token.
     *
     * @param stack Helper stack containing tokens.
     * @param t     Token to parse.
     * @throws QueryParserException If token wasn't expected.
     */
    private void parseString(ObjectStack<Token> stack, Token t) {
        if (stack.size() != 2 || stack.peek().getType() != TokenType.COMPARISON_OPERATOR) {
            throw new QueryParserException("Invalid query.");
        }
        stack.push(t);
    }

    /**
     * Parses comparison operator token.
     *
     * @param stack Helper stack containing tokens.
     * @param t     Token to parse.
     * @throws QueryParserException If token wasn't expected.
     */
    private void parseComparisonOperator(ObjectStack<Token> stack, Token t) {
        if (stack.size() != 1 || stack.peek().getType() != TokenType.ATTRIBUTE) {
            throw new QueryParserException("Invalid query.");
        }
        stack.push(t);
    }
}
