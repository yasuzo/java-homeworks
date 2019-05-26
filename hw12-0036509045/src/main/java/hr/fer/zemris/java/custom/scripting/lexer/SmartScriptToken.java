package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

/**
 * Class that models token returned by {@link SmartScriptLexer} object.
 *
 * @author Jan Capek
 */
public class SmartScriptToken {
    private SmartScriptTokenType type;
    private Object value;

    /**
     * Constructs a new token of type {@code type} with value {@code value}.
     *
     * @param type  Type of token.
     * @param value Value of token.
     * @throws NullPointerException If {@code type} is {@code null}.
     */
    public SmartScriptToken(SmartScriptTokenType type, Object value) {
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
    public SmartScriptTokenType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmartScriptToken that = (SmartScriptToken) o;
        return type == that.type &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {
        return "SmartScriptToken{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
