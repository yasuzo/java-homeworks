package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.exec.methods.*;
import hr.fer.zemris.java.custom.scripting.exec.util.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

/**
 * Object used for execution of {@link hr.fer.zemris.java.custom.scripting.exec.methods.SmartScriptMethod}.
 *
 * @author Jan Capek
 */
public class MethodExecutor {

    /**
     * Request context.
     */
    private RequestContext requestContext;

    /**
     * Map containing available methods.
     */
    private Map<String, SmartScriptMethod> methodMap;

    /**
     * Constructs a new method executor for {@link hr.fer.zemris.java.custom.scripting.exec.methods.SmartScriptMethod}.
     *
     * @param requestContext Request context.
     * @throws NullPointerException If given context is {@code null}.
     */
    public MethodExecutor(RequestContext requestContext) {
        this.requestContext = Objects.requireNonNull(requestContext);
        this.methodMap = new HashMap<>();
        initMap();
    }

    /**
     * Initializes method map.
     */
    private void initMap() {
        methodMap.put("decfmt", new DecfmtMethod());
        methodMap.put("dup", new DupMethod());
        methodMap.put("paramGet", new ParamGetMethod());
        methodMap.put("pparamGet", new PParamGetMethod());
        methodMap.put("sin", new SinMethod());
        methodMap.put("swap", new SwapMethod());
        methodMap.put("setMimeType", new SetMimeTypeMethod());
        methodMap.put("pparamSet", new PParamSetMethod());
        methodMap.put("pparamDel", new PParamDelMethod());
        methodMap.put("tparamGet", new TParamGetMetod());
        methodMap.put("tparamSet", new TParamSetMethod());
        methodMap.put("tparamDel", new TParamDelMethod());
        methodMap.put("+", new ArithmeticMethod(ValueWrapper::add));
        methodMap.put("-", new ArithmeticMethod(ValueWrapper::subtract));
        methodMap.put("*", new ArithmeticMethod(ValueWrapper::multiply));
        methodMap.put("/", new ArithmeticMethod(ValueWrapper::divide));
    }

    /**
     * Executes a method with given method name.
     *
     * @param methodName Name of the method that is to be executed.
     * @param stack Stack used to get method params from.
     * @throws MethodNotFoundException If nonexistent method is called.
     * @throws NullPointerException If any of the parameters are {@code null}.
     */
    public void execute(String methodName, Stack<ValueWrapper> stack) {
        SmartScriptMethod method = methodMap.get(methodName);
        if(method == null) {
            throw new MethodNotFoundException();
        }
        method.execute(requestContext, stack);
    }
}
