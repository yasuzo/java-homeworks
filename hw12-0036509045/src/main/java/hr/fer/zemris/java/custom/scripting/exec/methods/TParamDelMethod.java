package hr.fer.zemris.java.custom.scripting.exec.methods;

import hr.fer.zemris.java.custom.scripting.exec.util.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Removes association from request context's temporary parameter map.
 * This takes one argument and it is a string name of the value that needs to be removed.
 *
 * @author Jan Capek
 */
public class TParamDelMethod  implements SmartScriptMethod{
    @Override
    public void execute(RequestContext context, Stack<ValueWrapper> valueStack) {
        String name = (String) valueStack.pop().getValue();
        context.removeTemporaryParameter(name);
    }
}
