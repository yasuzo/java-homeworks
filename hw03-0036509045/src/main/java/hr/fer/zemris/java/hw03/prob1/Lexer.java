package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

/**
 * Lexer class for analyzing text and returning tokens from it.
 *
 * @author Jan Capek
 */
public class Lexer {
    //    input text
    private char[] data;
    //    current token
    private Token token;
    //    index of first unprocessed character
    private int currentIndex;
    //    state
    private LexerState state;

    /**
     * Constructs a new Lexer for analyzing given text.
     *
     * @param text Text that needs to be analyzed.
     * @throws NullPointerException If {@code text} is {@code null},
     */
    public Lexer(String text) {
        Objects.requireNonNull(text);
        data = text.toCharArray();
        state = LexerState.BASIC;
    }

    /**
     * Sets state of the lexer.
     *
     * @param state State to change to.
     * @throws NullPointerException If {@code state} is {@code null}.
     */
    public void setState(LexerState state) {
        this.state = Objects.requireNonNull(state);
    }

    /**
     * Generates and returns new token in extended mode.
     *
     * @return New token in extended mode.
     */
    private Token nextTokenExtended() {
        if(data[currentIndex] == '#') {
            return token = new Token(TokenType.SYMBOL, data[currentIndex++]);
        }

        int startingIndex = currentIndex;
        while (currentIndex < data.length && isSpace(data[currentIndex]) == false && data[currentIndex] != '#') {
            currentIndex++;
        }
        return token = new Token(TokenType.WORD, String.copyValueOf(data, startingIndex, currentIndex - startingIndex));
    }

    /**
     * Generates and returns new token in basic mode.
     *
     * @return New token in basic mode.
     * @throws LexerException If an error occurred.
     */
    private Token nextTokenBasic() {
//        check for symbol
        if (Character.isLetter(data[currentIndex]) == false && Character.isDigit(data[currentIndex]) == false && data[currentIndex] != '\\') {
            return token = new Token(TokenType.SYMBOL, data[currentIndex++]);
        }

//        is letter
        if (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
            StringBuilder sb = new StringBuilder();
            for (; currentIndex < data.length && (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\'); currentIndex++) {
                if (Character.isLetter(data[currentIndex])) {
                    sb.append(data[currentIndex]);
                    continue;
                }
//                data[i] == '\\' -> check if this escapes number or another '\\'
                if (currentIndex + 1 < data.length && (Character.isDigit(data[currentIndex + 1]) || data[currentIndex + 1] == '\\')) {
                    sb.append(data[currentIndex + 1]);
                    currentIndex++; // make currentIndex be at the escaped char's index instead of '\\'
                    continue;
                }
//                wrong escaping
                throw new LexerException("Invalid input!");
            }

            return token = new Token(TokenType.WORD, sb.toString());
        }

//        is digit
        int startingIndex = currentIndex;
        while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
            currentIndex++;
        }
        try {
            return token = new Token(TokenType.NUMBER, Long.parseLong(String.copyValueOf(data, startingIndex, currentIndex - startingIndex)));
        } catch (NumberFormatException e) {
            throw new LexerException("Invalid input!");
        }
    }

    /**
     * Generates and returns next token.
     *
     * @return Next token.
     * @throws LexerException If an error occurred.
     */
    public Token nextToken() {
        if (token != null && token.getType() == TokenType.EOF) {
            throw new LexerException("No more tokens to generate.");
        }

//        skip spaces
        while (currentIndex < data.length && isSpace(data[currentIndex])) {
            currentIndex++;
        }

//        check for EOF
        if (currentIndex == data.length) {
            return token = new Token(TokenType.EOF, null);
        }

        if (state == LexerState.BASIC) {
            return nextTokenBasic();
        }

        return nextTokenExtended();
    }

    /**
     * Checks if given character is space.
     *
     * @param c Character to check.
     * @return {@code true} if {@code c} is space, {@code false} otherwise.
     */
    private boolean isSpace(char c) {
        switch (c) {
            case '\n':
            case '\r':
            case '\t':
            case ' ':
                return true;
        }
        return false;
    }


    /**
     * Returns most recently generated token.
     * This method can be called multiple times and will not generate new token.
     *
     * @return Most recently generated token.
     */
    public Token getToken() {
        return token;
    }
}
