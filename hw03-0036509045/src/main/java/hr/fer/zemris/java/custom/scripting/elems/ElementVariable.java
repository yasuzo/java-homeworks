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

    /**
     * @return Variable's name
     */
    public String getName() {
        return name;
    }
}
