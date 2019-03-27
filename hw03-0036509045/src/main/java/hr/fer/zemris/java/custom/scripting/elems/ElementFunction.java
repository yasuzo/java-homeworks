package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Class that represents a function element.
 *
 * @author Jan Capek
 */
public class ElementFunction extends Element {

    private String name;

    /**
     * Construct a function element with given name.
     *
     * @param name Name of the function.
     * @throws NullPointerException If {@code name} is {@code null}.
     */
    public ElementFunction(String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String asText() {
        return name;
    }

    /**
     * @return Function's name.
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementFunction that = (ElementFunction) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
