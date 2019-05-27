package hr.fer.zemris.java.custom.scripting.exec.util;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Instances of this class can hold multiple stacks, each under different keys of type {@link String}.
 * Each {@code ObjectMultistack} will offer functionality to use held stacks.
 * Neither keys "stack names" nor value wrappers cannot be {@code null}.
 *
 * @author Jan Capek
 */
public class ObjectMultistack {

    private Map<String, MultistackEntry> stacks;

    /**
     * Constructs a new empty {@link ObjectMultistack}.
     */
    public ObjectMultistack() {
        stacks = new HashMap<>();
    }

    /**
     * Pushes given value wrapper on top of the stack with given key.
     * It does not matter if stack under given key does not already exist, it will be created.
     *
     * @param keyName      Key of the stack that the wrapper should be pushed on.
     * @param valueWrapper Wrapper that should be pushed.
     * @throws NullPointerException If either of the arguments are {@code null}.
     */
    public void push(String keyName, ValueWrapper valueWrapper) {
        Objects.requireNonNull(keyName);
        Objects.requireNonNull(valueWrapper);

        MultistackEntry newEntry = new MultistackEntry(valueWrapper, stacks.get(keyName));
        stacks.put(keyName, newEntry);
    }

    /**
     * Pops a value wrapper from top of the stack stored under given key.
     *
     * @param keyName Key of the stack from which wrapper should be popped.
     * @return Value wrapper from top of the wanted stack.
     * @throws EmptyStackException If stack under given key is empty.
     */
    public ValueWrapper pop(String keyName) {
        if (isEmpty(keyName)) {
            throw new EmptyStackException();
        }
        MultistackEntry result = stacks.get(keyName);
        stacks.put(keyName, result.next);
        return result.value;
    }

    /**
     * Returns a value wrapper from the top of the stack stored under given key but does not remove it.
     *
     * @param keyName Key of the stack from which a wrapper should be returned.
     * @return Value wrapper on the top of the wanted stack.
     * @throws EmptyStackException If wanted stack is empty.
     */
    public ValueWrapper peek(String keyName) {
        if (isEmpty(keyName)) {
            throw new EmptyStackException();
        }
        return stacks.get(keyName).value;
    }

    /**
     * Checks if stack under given key is empty.
     *
     * @param keyName Key of the stack that should be checked.
     * @return {@code true} if stack is empty or does not exist, {@code false} otherwise.
     */
    public boolean isEmpty(String keyName) {
        return stacks.get(keyName) == null;
    }

    /**
     * Structure used as a stack entry.
     * It holds a entry value and a reference to the next entry in the stack.
     */
    private static class MultistackEntry {

        private ValueWrapper value;
        private MultistackEntry next;

        /**
         * Constructs a new entry with given value and a reference to the next entry in the stack.
         *
         * @param value Value of the entry.
         * @param next  Next entry in the stack.
         */
        private MultistackEntry(ValueWrapper value, MultistackEntry next) {
            this.value = value;
            this.next = next;
        }
    }
}
