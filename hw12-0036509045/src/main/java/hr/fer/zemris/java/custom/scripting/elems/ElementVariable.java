package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Represents a variable element for storage.
 *
 * @author Jan Capek
 */
public class ElementVariable extends Element {

    private String name;

    /**
     * Constructs new {@code ElementVariable} object with given name.
     *
     * @param name Name of the variable.
     * @throws NullPointerException If {@code name} is {@code null}.
     */
    public ElementVariable(String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String asText() {
        return name;
    }

    @Override
    public String toString() {
        return asText();
    }

    /**
     * @return Variable's name
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementVariable that = (ElementVariable) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
