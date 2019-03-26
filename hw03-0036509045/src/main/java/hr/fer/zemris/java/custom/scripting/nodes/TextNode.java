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

    /**
     * @return Text held by node.
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\{\\$", "\\\\\\{\\$");
    }
}
