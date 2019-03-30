package hr.fer.zemris.java.custom.collections;

/**
 * Class representing stack data structure.
 * Uses {@code ArrayIndexedCollection} for it's internal storage.
 *
 * @author Jan Capek
 */
public class ObjectStack<T> {

    //    list for internal storage
    private ArrayIndexedCollection<T> list;

    /**
     * Constructs {@code ObjectStack} object
     */
    public ObjectStack() {
        list = new ArrayIndexedCollection<>();
    }

    /**
     * Checks if stack is empty.
     *
     * @return {@code true} if the stack is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns number of elements on the stack.
     *
     * @return Number of elements on the stack
     */
    public int size() {
        return list.size();
    }

    /**
     * Pushes a value on the stack. {@code null} values are not permitted and will result with an exception.
     *
     * @param value Value to push on the stack
     * @throws NullPointerException If the value is {@code null}
     */
    public void push(T value) {
        list.add(value);
    }

    /**
     * Returns a value on top of the stack and removes it from the stack.
     *
     * @return Value on top of the stack
     * @throws EmptyStackException If stack is empty
     */
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException("Can't pop from empty stack!");
        }

        T value = list.get(list.size() - 1);
        list.remove(list.size() - 1);
        return value;
    }

    /**
     * Returns a value on top of the stack but does not remove it from the stack.
     *
     * @return Value on top of the stack
     * @throws EmptyStackException If stack is empty
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException("Can't peek at empty stack!");
        }

        return list.get(list.size() - 1);
    }

    /**
     * Clears all elements from the stack.
     */
    public void clear() {
        list.clear();
    }
}
