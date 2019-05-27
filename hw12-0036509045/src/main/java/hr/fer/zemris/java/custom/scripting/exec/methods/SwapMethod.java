package hr.fer.zemris.java.custom.scripting.exec.methods;

import hr.fer.zemris.java.custom.scripting.exec.util.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Replaces the order of two topmost items on stack.
 *
 * @author Jan Capek
 */
public class SwapMethod implements SmartScriptMethod {
    @Override
    public void execute(RequestContext context, Stack<ValueWrapper> valueStack) {
        ValueWrapper a = valueStack.pop();
        ValueWrapper b = valueStack.pop();
        valueStack.push(a);
        valueStack.push(b);
    }
}
