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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type &&
                Objects.equals(value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {
        return String.format("Type: %s, Value: %s", type, value);
    }
}
