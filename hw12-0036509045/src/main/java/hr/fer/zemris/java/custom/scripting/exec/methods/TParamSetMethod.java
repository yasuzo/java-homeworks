package hr.fer.zemris.java.custom.scripting.exec.methods;

import hr.fer.zemris.java.custom.scripting.exec.util.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Stores a value into request context's temporary parameters map.
 * This takes two arguments, first is string value that needs to be stored,
 * second is name under which the value will be stored.
 *
 * @author Jan Capek
 */
public class TParamSetMethod implements SmartScriptMethod {
    @Override
    public void execute(RequestContext context, Stack<ValueWrapper> valueStack) {
        String name = (String) valueStack.pop().getValue();
        String value = (String) valueStack.pop().getValue();
        context.setTemporaryParameter(name, value);
    }
}
