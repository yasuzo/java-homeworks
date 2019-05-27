package hr.fer.zemris.java.custom.scripting.exec.methods;

import hr.fer.zemris.java.custom.scripting.exec.util.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Objects;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * This method is primarily used to perform arithmetic operations on
 * {@link ValueWrapper} with {@link Object} but it can be used for more.
 * ValueWrapper will be left on top of the stack.
 * This method takes two arguments.
 *
 * @author Jan Capek
 */
public class ArithmeticMethod implements SmartScriptMethod{

    /**
     * BiConsumer that is supposed to change given {@link ValueWrapper}.
     */
    private BiConsumer<ValueWrapper, Object> biConsumer;


    /**
     * Constructs a new arithmetic method.
     *
     * @param biConsumer BiConsumer that is supposed to change given {@link ValueWrapper}.
     */
    public ArithmeticMethod(BiConsumer<ValueWrapper, Object> biConsumer) {
        this.biConsumer = Objects.requireNonNull(biConsumer);
    }

    @Override
    public void execute(RequestContext context, Stack<ValueWrapper> valueStack) {
        ValueWrapper b = valueStack.pop();
        ValueWrapper a = valueStack.peek();
        biConsumer.accept(a, b.getValue());
    }
}
