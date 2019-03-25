package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

/**
 * Class used to analyze text and returning tokens from it.
 * <p>
 * If lexer is in {@link SmartScriptLexerState#BASIC} state, it will only return tokens of type
 * {@link SmartScriptTokenType#STRING} in whole until EOF or "{$" and {@link SmartScriptTokenType#OPEN_TAG_BRACKET} if found "{$".
 * <p>
 * If lexer is in {@link SmartScriptLexerState#TAG} state, it can return all token types enumerated in {@link SmartScriptTokenType}
 * except {@link SmartScriptTokenType#OPEN_TAG_BRACKET}.
 *
 * @author Jan Capek
 */
public class SmartScriptLexer {

    private char[] data;
    private int currentPosition;
    private SmartScriptToken currentToken;
    private SmartScriptLexerState state;

    /**
     * Creates an instance of {@code SmartScriptLexer} for analyzing given data.
     *
     * @param data Data to analyze.
     * @throws NullPointerException If {@code data} is {@code null}.
     */
    public SmartScriptLexer(String data) {
        this.data = Objects.requireNonNull(data).toCharArray();
        state = SmartScriptLexerState.BASIC;
    }

    /**
     * Sets state of the lexer.
     *
     * @param state State to change to.
     * @throws NullPointerException If {@code state} is {@code null}.
     */
    public void setState(SmartScriptLexerState state) {
        this.state = Objects.requireNonNull(state);
    }

    /**
     * Return's most recently generated token without generating a new one.
     *
     * @return Most recently generated token.
     */
    public SmartScriptToken getCurrentToken() {
        return currentToken;
    }

    /**
     * Generates and returns a new token.
     *
     * @return New token.
     * @throws SmartScriptLexerException If an error occurred.
     */
    public SmartScriptToken nextToken() {
        if (currentToken != null && currentToken.getType() == SmartScriptTokenType.EOF) {
            throw new SmartScriptLexerException("No more tokens to generate.");
        }

        if (currentPosition >= data.length) {
            return currentToken = new SmartScriptToken(SmartScriptTokenType.EOF, null);
        }

        if (state == SmartScriptLexerState.BASIC) {
            return nextTokenBasic();
        }

        return nextTokenTag();
    }

    /**
     * @return Next token in tag state.
     */
    private SmartScriptToken nextTokenTag() {
        skipSpaces();

//        closing tag bracket
        if(currentPosition + 1 < data.length && data[currentPosition] == '$' && data[currentPosition + 1] == '}'){
            currentPosition += 2;
            return currentToken = new SmartScriptToken(SmartScriptTokenType.CLOSED_TAG_BRACKET, "$}");
        }

//        keyword '='
        if (data[currentPosition] == '=') {
            return currentToken = new SmartScriptToken(SmartScriptTokenType.KEYWORD, String.valueOf(data[currentPosition++]));
        }

//        variable or keyword
        if (Character.isLetter(data[currentPosition])) {
            int start = currentPosition;
            while (currentPosition < data.length && (Character.isLetter(data[currentPosition]) || Character.isDigit(data[currentPosition]) || data[currentPosition] == '_')) {
                currentPosition++;
            }
            String stringValue = String.copyValueOf(data, start, currentPosition - start);
            if (isKeyword(stringValue)) {
                return currentToken = new SmartScriptToken(SmartScriptTokenType.KEYWORD, stringValue.toLowerCase());
            }
            return currentToken = new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, stringValue);
        }

//        function
        if (data[currentPosition] == '@') {
            currentPosition++;
            if (currentPosition < data.length && Character.isLetter(data[currentPosition])) {
                int start = currentPosition;
                while (currentPosition < data.length && (Character.isLetter(data[currentPosition]) || Character.isDigit(data[currentPosition]) || data[currentPosition] == '_')) {
                    currentPosition++;
                }
                return currentToken = new SmartScriptToken(SmartScriptTokenType.FUNCTION, String.copyValueOf(data, start, currentPosition - start));
            }

            throw new SmartScriptLexerException("Syntax error, unrecognized symbols.");
        }

//        number constants
        if ((data[currentPosition] == '-' && isNumberNext(currentPosition + 1)) || isNumberNext(currentPosition)) {
            int start = currentPosition++;
            boolean isDecimal = false;
            while (currentPosition < data.length && (data[currentPosition] == '.' || Character.isDigit(data[currentPosition]))) {
//                found a second dot => stop & return 1 character back
                if (data[currentPosition] == '.' && isDecimal) {
                    currentPosition--;
                    break;
                }
                if (data[currentPosition] == '.') {
                    isDecimal = true;
                }
                currentPosition++;
            }
            String stringValue = String.copyValueOf(data, start, currentPosition - start);

            if (isDecimal) {
                return currentToken = new SmartScriptToken(SmartScriptTokenType.DOUBLE_CONSTANT, Double.parseDouble(stringValue));
            }
            return currentToken = new SmartScriptToken(SmartScriptTokenType.INTEGER_CONSTANT, Integer.parseInt(stringValue));
        }

//        string literal
        if (data[currentPosition] == '"') {
            currentPosition++;
            StringBuilder sb = new StringBuilder();
            while (currentPosition < data.length && data[currentPosition] != '"') {
                if (data[currentPosition] == '\\') {
                    if (currentPosition + 1 < data.length && (data[currentPosition + 1] == '\\' || data[currentPosition + 1] == '"')) {
                        sb.append(data[currentPosition + 1]);
                        currentPosition += 2;
                        continue;
                    }
                    throw new SmartScriptLexerException("Wrong character escaping.");
                }
                sb.append(data[currentPosition]);
                currentPosition++;
            }
            if (data[currentPosition] != '"') {
                throw new SmartScriptLexerException("Missing closing string quote.");
            }
            currentPosition++;
            return currentToken = new SmartScriptToken(SmartScriptTokenType.STRING, sb.toString());
        }

//        operator
        if (isOperator(data[currentPosition])) {
            return currentToken = new SmartScriptToken(SmartScriptTokenType.OPERATOR, String.valueOf(data[currentPosition++]));
        }

        throw new SmartScriptLexerException("Unrecognized character!");
    }

    /**
     * Checks if a character is an operator.
     *
     * @param c Character to check.
     * @return {@code true} if {@code c} is an operator, {@code false} otherwise.
     */
    private boolean isOperator(char c) {
        switch (c) {
            case '*':
            case '/':
            case '-':
            case '^':
                return true;
        }
        return false;
    }

    /**
     * Checks if positive number starts at given index.
     *
     * @param start Starting index.
     * @return {@code true} if a number starts on given index, {@code false} otherwise.
     */
    private boolean isNumberNext(int start) {
        if (start >= data.length) {
            return false;
        }
        if (Character.isDigit(data[start])) {
            return true;
        }
        if (data[start] == '.' && start + 1 < data.length && Character.isDigit(data[start + 1])) {
            return true;
        }
        return false;
    }

    /**
     * Checks if given string is a keyword.
     *
     * @param s String to check.
     * @return {@code true} is {@code s} is a keyword, {@code false} otherwise.
     */
    private boolean isKeyword(String s) {
        switch (s.toLowerCase()) {
            case "for":
            case "end":
            case "=":
                return true;
        }
        return false;
    }

    /**
     * @return Next token in basic state.
     */
    private SmartScriptToken nextTokenBasic() {
        if (currentPosition + 1 < data.length && data[currentPosition] == '{' && data[currentPosition + 1] == '$') {
            currentPosition += 2;
            return currentToken = new SmartScriptToken(SmartScriptTokenType.OPEN_TAG_BRACKET, "{$");
        }

        StringBuilder sb = new StringBuilder();
        while (currentPosition < data.length) {
//            check "{$"
            if (currentPosition + 1 < data.length && data[currentPosition] == '{' && data[currentPosition + 1] == '$') {
                break;
            }
//            check escaping
            if (data[currentPosition] == '\\') {
                if (currentPosition + 1 < data.length && data[currentPosition] == '\\' || data[currentPosition] == '{') {
                    sb.append(data[currentPosition + 1]);
                    currentPosition += 2;
                    continue;
                }
                throw new SmartScriptLexerException("Wrong character escaping.");
            }
//            normal characters
            sb.append(data[currentPosition]);
            currentPosition++;
        }
        return currentToken = new SmartScriptToken(SmartScriptTokenType.STRING, sb.toString());
    }

    /**
     * Positions {@link SmartScriptLexer#currentPosition} to the first character that is not a blank.
     */
    private void skipSpaces() {
        while (currentPosition < data.length && isSpace(data[currentPosition])) {
            currentPosition++;
        }
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


}
