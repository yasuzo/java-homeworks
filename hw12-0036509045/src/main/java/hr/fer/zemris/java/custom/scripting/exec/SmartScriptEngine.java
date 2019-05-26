package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Objects;

/**
 * Engine for smart script execution.
 *
 * @author Jan Capek
 */
public class SmartScriptEngine {

    /**
     * Document node of a script that needs to be executed.
     */
    private DocumentNode documentNode;

    /**
     * Request context used to send output to.
     */
    private RequestContext requestContext;

    /**
     * Multi stack used for execution.
     */
    private ObjectMultistack multistack = new ObjectMultistack();

    /**
     * Document node visitor that will do the execution.
     */
    private INodeVisitor visitor = new INodeVisitor() {
        @Override
        public void visitTextNode(TextNode node) {

        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {

        }

        @Override
        public void visitEchoNode(EchoNode node) {

        }

        @Override
        public void visitDocumentNode(DocumentNode node) {

        }
    };

    /**
     * Constructs a new engine for smart script execution.
     *
     * @param documentNode Document node of the parsed script.
     * @param requestContext Request context used to send output.
     * @throws NullPointerException If any of the arguments is {@code null}.
     */
    public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
        this.documentNode = Objects.requireNonNull(documentNode);
        this.requestContext = Objects.requireNonNull(requestContext);
    }

    /**
     * Executes parsed script.
     */
    public void execute() {
        documentNode.accept(visitor);
    }
}