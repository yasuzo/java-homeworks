package com.yasuzo.shell.argscheck.parsing;

import java.util.Objects;

/**
 * Lexer for shell command arguments.
 *
 * @author Jan Capek
 */
public class CommandArgumentLexer {
    private char[] data;
    private int currentPosition;
    private String lastReturned;

    /**
     * Constructs a new lexer for lexing command arguments from given string.
     *
     * @param data Command arguments in string form.
     * @throws NullPointerException If given data is {@code null}.
     */
    public CommandArgumentLexer(String data) {
        this.data = Objects.requireNonNull(data).toCharArray(); // Use of requireNonNull() is unnecessary but it's more explicit
    }

    /**
     * @return Next command argument or {@code null} if lexer has reached end of string.
     * @throws RuntimeException If an argument is invalid.
     */
    public String next() {
        skipBlanks();

        if (currentPosition >= data.length) {
            return lastReturned = null;
        }

        if (data[currentPosition] == '"') {
            return lastReturned = extractString();
        }

        return lastReturned = extractArgumentWithoutQuotes();
    }

    /**
     * @return Most recently returned argument.
     */
    public String getLastReturned() {
        return lastReturned;
    }

    /**
     * Extracts an argument that is not enclosed in quotes.
     *
     * @return Argument.
     */
    private String extractArgumentWithoutQuotes() {
        int start = currentPosition;
        while (currentPosition < data.length && Character.isWhitespace(data[currentPosition]) == false) {
            currentPosition++;
        }
        return String.copyValueOf(data, start, currentPosition - start);
    }

    /**
     * Extracts an argument enclosed in quotes.
     * This method will also escape characters such as: '"', '\'.
     *
     * @return Argument without quotes and with escaped characters.
     * @throws RuntimeException If an argument is invalid.
     */
    private String extractString() {
        currentPosition++;
        StringBuilder sb = new StringBuilder();
        while (currentPosition < data.length && data[currentPosition] != '"') {
            if (data[currentPosition] == '\\' && currentPosition + 1 < data.length) {
                switch (data[currentPosition + 1]) {
                    case '"':
                    case '\\':
                        sb.append(data[currentPosition + 1]);
                        currentPosition += 2;
                        break;
                    default:
                        sb.append(data[currentPosition]);
                        sb.append(data[currentPosition + 1]);
                        currentPosition += 2;
                }
                continue;
            }
            sb.append(data[currentPosition]);
            currentPosition++;
        }
//        case where a closing quote of a string is missing (e.g. "abc)
        if (currentPosition >= data.length) {
            throw new RuntimeException("Closing quote is missing.");
        }
//        solve this case: "C:\fi le".txt -> that should be invalid.
        if (currentPosition + 1 < data.length && Character.isWhitespace(data[currentPosition + 1]) == false) {
            throw new RuntimeException("Invalid string, space is expected after argument enclosed in quotes.");
        }
        currentPosition++;
        return sb.toString();
    }

    /**
     * Skips whitespace characters.
     */
    private void skipBlanks() {
        while (currentPosition < data.length && Character.isWhitespace(data[currentPosition])) {
            currentPosition++;
        }
    }
}
