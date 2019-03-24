package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

import java.util.Objects;

/**
 * Node representing a command which generates some textual output dynamically.
 *
 * @author Jan Capek
 */
public class EchoNode extends Node {

    private Element[] elements;

    /**
     * Constructs an {@code EchoNode} with given elements.
     *
     * @param elements Elements used in echo.
     * @throws NullPointerException If {@code elements} is {@code null}.
     */
    public EchoNode(Element[] elements) {
        this.elements = Objects.requireNonNull(elements);
    }

    /**
     * @return Elements in the echo statement.
     */
    public Element[] getElements() {
        return elements;
    }
}
