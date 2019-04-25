package hr.fer.zemris.java.hw07.observer1;

import java.util.Objects;

/**
 * Prints out value stored in {@link IntegerStorage} multiplied by 2 for only n times, then unregisters itself.
 *
 * @author Jan Capek
 */
public class DoubleValue implements IntegerStorageObserver {
    private int n;

    /**
     * Constructs a new observer that observes for the first {@code n} times.
     *
     * @param n Number of observations wanted. Has to be at least 1.
     */
    public DoubleValue(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Number cannot be less than 1!");
        }
        this.n = n;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException If given storage is {@code null}.
     */
    @Override
    public void valueChanged(IntegerStorage istorage) {
        Objects.requireNonNull(istorage);

        if (n > 0) {
            System.out.println("Double value: " + istorage.getValue() * 2);
            n--;
        }

        if (n <= 0) {
            istorage.removeObserver(this);
        }
    }
}
