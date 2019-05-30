package hr.fer.zemris.java.custom.scripting.exec.methods;

import hr.fer.zemris.java.custom.scripting.exec.util.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Stores a value into request context's persistent parameters map.
 * This takes two arguments, first is string value that needs to be stored,
 * second is name under which the value will be stored.
 *
 * @author Jan Capek
 */
public class PParamSetMethod implements SmartScriptMethod {
    @Override
    public void execute(RequestContext context, Stack<ValueWrapper> valueStack) {
        String name = (String) valueStack.pop().getValue();
        String value = valueStack.pop().getValue().toString();
        context.setPersistentParameter(name, value);
    }
}
