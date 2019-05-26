package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

/**
 * Node representing a piece of textual data.
 *
 * @author Jan Capek
 */
public class TextNode extends Node {

    private String text;

    /**
     * Constructs a new {@code TextNode} which holds given text.
     *
     * @param text Node's text.
     * @throws NullPointerException If {@code text} is {@code null}.
     */
    public TextNode(String text) {
        this.text = Objects.requireNonNull(text);
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitTextNode(this);
    }

    /**
     * @return Text held by node.
     */
    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TextNode textNode = (TextNode) o;
        return Objects.equals(text, textNode.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), text);
    }
}
