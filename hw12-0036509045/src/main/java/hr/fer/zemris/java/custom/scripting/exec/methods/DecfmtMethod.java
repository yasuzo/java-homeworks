package hr.fer.zemris.java.custom.scripting.exec.methods;

import hr.fer.zemris.java.custom.scripting.exec.util.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.text.DecimalFormat;
import java.util.Stack;

/**
 * Method that formats a number according to {@link java.text.DecimalFormat} specifications.
 * This takes two arguments, first is a value that needs to be formatted, second is string format.
 *
 * @author Jan Capek
 */
public class DecfmtMethod implements SmartScriptMethod {
    @Override
    public void execute(RequestContext context, Stack<ValueWrapper> valueStack) {
        String format = (String)valueStack.pop().getValue();
        ValueWrapper valueWrapper = valueStack.peek();

        DecimalFormat fmt = new DecimalFormat(format);
        String formatted = fmt.format(valueWrapper.getValue());
        valueWrapper.setValue(formatted);
    }
}
