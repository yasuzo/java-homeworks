package hr.fer.zemris.java.custom.scripting.exec.methods;

import hr.fer.zemris.java.custom.scripting.exec.util.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Method that calculates sine.
 *
 * @author Jan Capek
 */
public class SinMethod implements SmartScriptMethod {
    @Override
    public void execute(RequestContext context, Stack<ValueWrapper> valueStack) {
        ValueWrapper valueWrapper = valueStack.peek();
        valueWrapper.add(0.0);
        Double value = ((Double) valueWrapper.getValue());
        valueWrapper.setValue(Math.sin(value));
    }
}
