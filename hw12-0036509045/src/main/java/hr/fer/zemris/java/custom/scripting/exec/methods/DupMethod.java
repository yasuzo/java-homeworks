package hr.fer.zemris.java.custom.scripting.exec.methods;

import hr.fer.zemris.java.custom.scripting.exec.util.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Method that duplicates value at the top of the stack. Duplicated value will also be pushed on the stack.
 *
 * @author Jan Capek
 */
public class DupMethod implements SmartScriptMethod {
    @Override
    public void execute(RequestContext context, Stack<ValueWrapper> valueStack) {
        valueStack.push(new ValueWrapper(valueStack.peek().getValue()));
    }
}
