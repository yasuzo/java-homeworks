package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

import java.util.Arrays;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{$= ");
        for(Element e : elements) {
            if(e instanceof ElementString) {
                sb.append(String.format("\"%s\"", e.asText().replaceAll("\"", "\\\\\"")));
            } else if (e instanceof ElementFunction){
                sb.append(String.format("@%s", e.asText()));
            } else {
                sb.append(e.asText());
            }
            sb.append(" ");
        }
        sb.append("$}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EchoNode echoNode = (EchoNode) o;
        return Arrays.equals(elements, echoNode.elements);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(elements);
        return result;
    }
}
