package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Class that represents a String literal.
 *
 * @author Jan Capek
 */
public class ElementString extends Element {

    private String value;

    /**
     * Constructs a new string element with given value.
     *
     * @param value String value.
     * @throws NullPointerException If {@code value} is {@code null}.
     */
    public ElementString(String value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public String asText() {
        return value;
    }

    /**
     * @return String's value.
     */
    public String getValue() {
        return value;
    }
}
