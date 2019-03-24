package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

/**
 * Class that models token returned by {@link Lexer} object.
 *
 * @author Jan Capek
 */
public class Token {

    private TokenType type;
    private Object value;

    /**
     * Constructs a new token of type {@code type} with value {@code value}.
     *
     * @param type Type of token.
     * @param value Value of token.
     * @throws NullPointerException If {@code type} is {@code null}.
     */
    public Token(TokenType type, Object value) {
        this.type = Objects.requireNonNull(type);
        this.value = value;
    }

    /**
     * @return Token's value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return Token's type.
     */
    public TokenType getType() {
        return type;
    }
}
