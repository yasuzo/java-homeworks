package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.exec.util.ObjectMultistack;
import hr.fer.zemris.java.custom.scripting.exec.util.ValueWrapper;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.util.*;

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
     * Object used for smart script method execution.
     */
    private MethodExecutor methodExecutor;

    /**
     * Document node visitor that will do the execution.
     */
    private INodeVisitor visitor = new INodeVisitor() {
        @Override
        public void visitTextNode(TextNode node) {
            try {
                requestContext.write(node.getText());
            } catch (IOException e) {
                throw new RuntimeException("Data could not be sent.", e);
            }
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            executeForLoop(node);
        }

        @Override
        public void visitEchoNode(EchoNode node) {
            try {
                executeEcho(node);
            } catch (IOException e) {
                throw new RuntimeException("Data could not be sent.", e);
            }
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            Objects.requireNonNull(node);
            int childrenNumber = node.numberOfChildren();
            for (int i = 0; i < childrenNumber; i++) {
                node.getChild(i).accept(this);
            }
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
        this.methodExecutor = new MethodExecutor(requestContext);
    }

    /**
     * Executes parsed script.
     */
    public void execute() {
        try {
            documentNode.accept(visitor);
        } catch (Exception e) {
            throw new SmartScriptRuntimeException("Exception thrown while executing the script.", e);
        }
    }

    /**
     * Executes echo tag.
     *
     * @param node Echo node that needs to be executed.
     * @throws IOException If data could not be sent.
     * @throws NullPointerException If given node is {@code null}.
     */
    private void executeEcho(EchoNode node) throws IOException {
        Objects.requireNonNull(node);
        Stack<ValueWrapper> stack = new Stack<>();
        for(Element e : node.getElements()) {
            if (e instanceof ElementFunction || e instanceof ElementOperator) {
                methodExecutor.execute(e.asText(), stack);
            } else {
                stack.push(new ValueWrapper(getValue(e)));
            }
        }
        for(ValueWrapper value : stack) {
            requestContext.write(value.getValue().toString());
        }
    }

    /**
     * Executes for-loop node.
     *
     * @param node Node that is to be executed.
     * @throws NullPointerException If given node is {@code null}.
     * @throws RuntimeException If some of the for-loop parts do not represent numbers.
     */
    private void executeForLoop(ForLoopNode node) {
        Objects.requireNonNull(node);
        ValueWrapper varValue = putVariable(node.getVariable().getName(), getValue(node.getStartExpression()));
        Object boundary = getValue(node.getEndExpression());
        Object step = getValue(node.getStepExpression());
        while (varValue.numCompare(boundary) <= 0) {
            int children = node.numberOfChildren();
            for (int i = 0; i < children; i++) {
                node.getChild(i).accept(visitor);
            }
            varValue.add(step);
        }
        multistack.pop(node.getVariable().getName());
    }

    /**
     * Returns a value stored in given element.<br>
     * Allowed elements are: {@link ElementConstantDouble}, {@link ElementConstantInteger}, {@link ElementString}, {@link ElementVariable}.
     *
     * @param element Element whose value needs to be returned.
     * @return Element's constant value.
     * @throws IllegalArgumentException If given element does not hold a constant.
     * @throws VariableNotFoundException If variable is not declared.
     */
    private Object getValue(Element element) {
        if(element instanceof ElementString) {
            return ((ElementString) element).getValue();
        }
        if (element instanceof ElementConstantInteger) {
            return ((ElementConstantInteger) element).getValue();
        }
        if (element instanceof ElementConstantDouble) {
            return ((ElementConstantDouble) element).getValue();
        }
        if (element instanceof ElementVariable) {
            return getVariable(((ElementVariable) element).getName()).getValue();
        }
        throw new IllegalArgumentException("Given element is not a constant.");
    }

    /**
     * Returns a value of the variable with given name.
     *
     * @param name Name of the variable whose value is to be returned.
     * @return Variable value.
     * @throws NullPointerException If given name is {@code null}.
     * @throws VariableNotFoundException If variable is not initialized.
     */
    private ValueWrapper getVariable(String name) {
        Objects.requireNonNull(name);
        if(multistack.isEmpty(name)) {
            throw new VariableNotFoundException("Variable '" + name + "' could not be found.");
        }
        return multistack.peek(name);
    }

    /**
     * Adds a variable to the current scope.
     *
     * @param name Name of the variable.
     * @param value Variable value.
     * @return Wrapper in which given value is stored.
     * @throws NullPointerException If given name is {@code null}.
     */
    private ValueWrapper putVariable(String name, Object value) {
        Objects.requireNonNull(name);
        ValueWrapper variableValue = new ValueWrapper(value);
        multistack.push(name, variableValue);
        return variableValue;
    }
}