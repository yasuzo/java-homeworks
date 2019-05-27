package hr.fer.zemris.java.custom.scripting.exec.methods;

import hr.fer.zemris.java.custom.scripting.exec.util.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Method that retrieves a context parameter on the stack if there is any. If not, default value will be pushed.
 * Takes two arguments, first is name of the context parameter that needs to be pushed, second is default value which
 * will be pushed if there is no parameter with given name.
 *
 * @author Jan Capek
 */
public class ParamGetMethod implements SmartScriptMethod {
    @Override
    public void execute(RequestContext context, Stack<ValueWrapper> valueStack) {
        ValueWrapper defaultValue = valueStack.pop();
        String paramName = (String) valueStack.pop().getValue();
        String param = context.getParameter(paramName);
        valueStack.push(param == null ? defaultValue : new ValueWrapper(param));
    }
}
