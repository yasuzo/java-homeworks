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

    @Override
    public String toString() {
        return String.format(
                "\"%s\"",
                asText().replaceAll("\\\\", "\\\\\\\\")
                        .replaceAll("\"", "\\\\\"")
                        .replaceAll("\n", "\\\\n")
                        .replaceAll("\r", "\\\\r")
                        .replaceAll("\t", "\\\\t")
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementString that = (ElementString) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
