package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

import java.util.Objects;

/**
 * Basic class for all graph nodes.
 *
 * @author Jan Capek
 */
public class Node {

    private ArrayIndexedCollection children;

    /**
     * Adds given child to internally managed collection of children.
     *
     * @param child Child node.
     * @throws NullPointerException If {@code child} is {@code null}.
     */
    public void addChildNode(Node child) {
        if(children == null) {
            children = new ArrayIndexedCollection();
        }
        children.add(child);
    }

    /**
     * @return Number of immediate children of this node.
     */
    public int numberOfChildren() {
        return children == null ? 0 : children.size();
    }

    /**
     * Returns a child at given index.
     *
     * @param index Index of wanted child.
     * @return Child at given index.
     * @throws IndexOutOfBoundsException If there are no children or given index is invalid.
     */
    public Node getChild(int index) {
        if(children == null) {
            throw new IndexOutOfBoundsException("There are no children.");
        }
        return (Node)children.get(index);
    }
}
