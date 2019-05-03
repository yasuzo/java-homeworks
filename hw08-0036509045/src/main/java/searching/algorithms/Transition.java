package searching.algorithms;

import java.util.Objects;

/**
 * Instance of this class holds a state-price pair.
 *
 * @param <S> Type of state this will hold.
 */
public class Transition<S> {
    private S state;
    private double cost;

    /**
     * Constructs a new transition objects that holds given state and cost.
     *
     * @param state State this should hold.
     * @param cost  Price this should hold.
     * @throws IllegalArgumentException If cost is less than 0.
     * @throws NullPointerException     If given state is {@code null}.
     */
    public Transition(S state, double cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Price cannot be less than 0.");
        }
        this.cost = cost;
        this.state = Objects.requireNonNull(state);
    }

    /**
     * @return State this holds.
     */
    public S getState() {
        return state;
    }

    /**
     * @return Cost this holds.
     */
    public double getCost() {
        return cost;
    }
}
