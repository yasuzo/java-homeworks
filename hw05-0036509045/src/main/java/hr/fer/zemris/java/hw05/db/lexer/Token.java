package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

/**
 * Instances of this class are returned by {@link QueryLexer}.
 */
public class Token {
    private TokenType type;
    private String value;

    /**
     * Constructs a new token of given type with given value.
     *
     * @param type  Token's type.
     * @param value Token's value.
     * @throws NullPointerException If type is {@code null}.
     */
    public Token(TokenType type, String value) {
        this.type = Objects.requireNonNull(type);
        this.value = value;
    }

    /**
     * @return Token's type.
     */
    public TokenType getType() {
        return type;
    }

    /**
     * @return Token's value.
     */
    public String getValue() {
        return value;
    }
}
