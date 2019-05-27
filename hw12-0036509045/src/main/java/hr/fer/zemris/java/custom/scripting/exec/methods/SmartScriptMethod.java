package hr.fer.zemris.java.custom.scripting.exec.methods;

import hr.fer.zemris.java.custom.scripting.exec.util.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Objects;
import java.util.Stack;

/**
 * Interface that models smart script methods.
 *
 * @author Jan Capek
 */
public interface SmartScriptMethod {

    /**
     * Executes the smart script method.
     *
     * @param context Request context.
     * @param valueStack Stack with arguments on which result will also be placed if there is any.
     * @throws java.util.EmptyStackException If given stack does not have appropriate number of arguments for the method.
     * @throws NullPointerException If given arguments are {@code null} and method does not support null values.
     * @throws RuntimeException If any other exception occurred.
     */
    void execute(RequestContext context, Stack<ValueWrapper> valueStack);
}
