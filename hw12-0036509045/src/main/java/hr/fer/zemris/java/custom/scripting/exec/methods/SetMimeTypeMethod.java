package hr.fer.zemris.java.custom.scripting.exec.methods;

import hr.fer.zemris.java.custom.scripting.exec.util.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Method that sets mime type of {@link RequestContext} to the type at the top of the stack.
 *
 * @author Jan Capek
 */
public class SetMimeTypeMethod implements SmartScriptMethod {
    @Override
    public void execute(RequestContext context, Stack<ValueWrapper> valueStack) {
        String type = (String)valueStack.pop().getValue();
        context.setMimeType(type);
    }
}
