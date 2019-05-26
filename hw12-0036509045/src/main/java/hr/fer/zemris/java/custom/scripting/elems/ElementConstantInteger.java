package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Class that represents an integer constant.
 *
 * @author Jan Capek
 */
public class ElementConstantInteger extends Element {
    private int value;

    public ElementConstantInteger(int value) {
        this.value = value;
    }

    @Override
    public String asText() {
        return Integer.toString(value);
    }

    @Override
    public String toString() {
        return asText();
    }

    /**
     * @return Constant's value.
     */
    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementConstantInteger that = (ElementConstantInteger) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
