package searching.algorithms;

import java.util.Objects;

/**
 * Node for state-space searching. It holds current state (at the time of creation),
 * price that was paid to get to the state and parent node (node that holds information about previous state).
 *
 * @param <S> Type of state node has to hold.
 * @author Jan Capek
 */
public class Node<S> {
    private Node<S> parent;
    private S state;
    private double cost;

    /**
     * Constructs a new node that holds given parameters.
     *
     * @param parent Previous node.
     * @param state Current state.
     * @param cost Cost of getting to the state.
     * @throws NullPointerException If given state is {@code null}.
     * @throws IllegalArgumentException If cost is less than 0.
     */
    public Node(Node<S> parent, S state, double cost) {
        this.parent = parent;
        this.state = Objects.requireNonNull(state);
        if(cost < 0) {
            throw new IllegalArgumentException("Price cannot be less than 0.");
        }
        this.cost = cost;
    }

    /**
     * @return Parent node.
     */
    public Node<S> getParent() {
        return parent;
    }

    /**
     * @return State which this node holds.
     */
    public S getState() {
        return state;
    }

    /**
     * @return Cost of getting to the state this node holds.
     */
    public double getCost() {
        return cost;
    }
}
