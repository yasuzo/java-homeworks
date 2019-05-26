package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Basic class for all graph nodes.
 *
 * @author Jan Capek
 */
public abstract class Node {

    private List<Node> children;

    /**
     * Adds given child to internally managed collection of children.
     *
     * @param child Child node.
     * @throws NullPointerException If {@code child} is {@code null}.
     */
    public void addChildNode(Node child) {
        if (children == null) {
            children = new ArrayList<>();
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
        if (children == null) {
            throw new IndexOutOfBoundsException("There are no children.");
        }
        return children.get(index);
    }

    /**
     * Method accepting {@link INodeVisitor} for visitor design pattern.
     *
     * @param visitor Visitor that needs to be accepted.
     * @throws NullPointerException If given visitor is {@code null}.
     */
    public void accept(INodeVisitor visitor) {
        children.forEach(child -> child.accept(visitor));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(children, node.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children);
    }
}
