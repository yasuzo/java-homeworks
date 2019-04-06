package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.collections.ObjectStack;

import java.util.Objects;

/**
 * Objects of this class enable drawing of fractals.
 *
 * @author Jan Capek
 */
public class Context {

    /**
     * Stack for storing states.
     */
    private ObjectStack<TurtleState> states;

    /**
     * Constructs a new context.
     */
    public Context() {
        states = new ObjectStack<>();
    }

    /**
     * @return Current state of the turtle.
     * @throws java.util.NoSuchElementException If there are no states on the stack.
     */
    public TurtleState getCurrentState() {
        return states.peek();
    }

    /**
     * Pushes a given state on the stack which makes it current state.
     *
     * @param state New state.
     * @throws NullPointerException If {@code state} is {@code null}.
     */
    public void pushState(TurtleState state) {
        states.push(Objects.requireNonNull(state));
    }

    /**
     * Removes current state which then makes last state current.
     *
     * @throws java.util.EmptyStackException If there are no states to pop.
     */
    public void popState() {
        states.pop();
    }

}
