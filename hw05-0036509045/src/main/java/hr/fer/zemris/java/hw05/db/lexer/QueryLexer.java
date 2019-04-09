package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

/**
 * Simple query lexer.
 *
 * @author Jan Capek.
 */
public class QueryLexer {

    private char[] data;
    private int currentPosition;
    private Token lastReturned;

    /**
     * Constructs a new lexer for given query.
     *
     * @param query Query to tokenize.
     * @throws NullPointerException If {@code query} is {@code null}.
     */
    public QueryLexer(String query) {
        data = Objects.requireNonNull(query).toCharArray();
    }

    /**
     * Returns most recently returned token.
     * This does not generate a new token.
     * If no tokens have been generated yet,
     * {@code null} will be returned.
     *
     * @return Most recently generated token or {@code null} if no tokens have been generated.
     */
    public Token getLastReturned() {
        return lastReturned;
    }

    /**
     * Generates and returns next token.
     *
     * @return Next token.
     * @throws QueryLexerException In case an error occurred.
     */
    public Token next() {
        if (hasNext() == false) {
            throw new QueryLexerException("No tokens left to return.");
        }

        skipBlanks();

        if (currentPosition >= data.length) {
            return lastReturned = new Token(TokenType.EOF, null);
        }

//        attribute, logical operator or comparison operator (only LIKE)
        if (Character.isLetter(data[currentPosition])) {
            return lastReturned = extractAttributeOrOperator();
        }

//        string literal
        if (data[currentPosition] == '"') {
            return lastReturned = extractStringLiteral();
        }

//        comparison operator
        return lastReturned = extractComparisonOperator();
    }

    /**
     * Extracts a comparison operator.
     *
     * @return Token of type {@link TokenType#COMPARISON_OPERATOR}.
     * @throws QueryLexerException If an operator was not recognized.
     */
    private Token extractComparisonOperator() {
        int start = currentPosition;
        while (currentPosition < data.length &&
                Character.isLetter(data[currentPosition]) == false &&
                Character.isWhitespace(data[currentPosition]) == false &&
                Character.isDigit(data[currentPosition]) == false) {
            currentPosition++;
        }
        String temp = String.copyValueOf(data, start, currentPosition - start);
        if (RecognizedOperators.comparisonOperators.contains(temp) == false) {
            throw new QueryLexerException("Unrecognized sequence.");
        }
        return new Token(TokenType.COMPARISON_OPERATOR, temp);
    }

    /**
     * Extracts a string literal from current position.
     *
     * @return Token of type {@link TokenType#STRING}.
     * @throws QueryLexerException If closing quote is missing.
     */
    private Token extractStringLiteral() {
        int start = ++currentPosition;
        while (currentPosition < data.length && data[currentPosition] != '"') {
            currentPosition++;
        }
        if (currentPosition >= data.length) {
            throw new QueryLexerException("Closing quote of string literal is missing.");
        }
        Token token = new Token(TokenType.STRING,
                String.copyValueOf(data, start, currentPosition - start));
        currentPosition++;
        return token;
    }

    /**
     * Extracts an attribute or an operator (logical or LIKE).
     *
     * @return Token of type {@link TokenType#ATTRIBUTE},
     * {@link TokenType#LOGIC_OPERATOR} or {@link TokenType#COMPARISON_OPERATOR}
     */
    private Token extractAttributeOrOperator() {
        int start = currentPosition;
        while (currentPosition < data.length && Character.isLetter(data[currentPosition])) {
            currentPosition++;
        }
        String temp = String.copyValueOf(data, start, currentPosition - start);

//        check if temp is a comparison operator
        if(RecognizedOperators.comparisonOperators.contains(temp.toLowerCase())) {
            return new Token(TokenType.COMPARISON_OPERATOR, temp);
        }
//        check if temp is a logical operator
        if (RecognizedOperators.logicalOperators.contains(temp.toLowerCase())) {
            return new Token(TokenType.LOGIC_OPERATOR, temp);
        }
//        temp is an attribute
        return new Token(TokenType.ATTRIBUTE, temp);
    }

    /**
     * @return {@code true} if there are more tokens left to return, {@code false} otherwise.
     */
    public boolean hasNext() {
        return lastReturned == null || lastReturned.getType() != TokenType.EOF;
    }

    /**
     * Skips blank characters.
     */
    public void skipBlanks() {
        while (currentPosition < data.length && Character.isWhitespace(data[currentPosition])) {
            currentPosition++;
        }
    }
}
