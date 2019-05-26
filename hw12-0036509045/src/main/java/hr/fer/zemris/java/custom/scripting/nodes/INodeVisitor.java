package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node visitor interface used in visitor design pattern.
 *
 * @author Jan Capek
 */
public interface INodeVisitor {

    /**
     * Visits {@link TextNode}.
     *
     * @param node Node that should be visited.
     * @throws NullPointerException If given node is {@code null}, and implementation does not accept nulls.
     */
    void visitTextNode(TextNode node);

    /**
     * Visits {@link ForLoopNode}.
     *
     * @param node Node that should be visited.
     * @throws NullPointerException If given node is {@code null}, and implementation does not accept nulls.
     */
    void visitForLoopNode(ForLoopNode node);

    /**
     * Visits {@link EchoNode}.
     *
     * @param node Node that should be visited.
     * @throws NullPointerException If given node is {@code null}, and implementation does not accept nulls.
     */
    void visitEchoNode(EchoNode node);

    /**
     * Visits {@link DocumentNode}.
     *
     * @param node Node that should be visited.
     * @throws NullPointerException If given node is {@code null}, and implementation does not accept nulls.
     */
    void visitDocumentNode(DocumentNode node);
}
