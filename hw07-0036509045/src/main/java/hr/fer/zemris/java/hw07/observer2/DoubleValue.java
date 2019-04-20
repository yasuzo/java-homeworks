package hr.fer.zemris.java.hw07.observer2;

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
        if(n < 1) {
            throw new IllegalArgumentException("Number cannot be less than 1!");
        }
        this.n = n;
    }

    /**
     * {@inheritDoc}
     * @throws NullPointerException If given argument is {@code null}.
     */
    @Override
    public void valueChanged(IntegerStorageChange storageChange) {
        Objects.requireNonNull(storageChange);

        if(n > 0) {
            System.out.println("Double value: " + storageChange.getNewValue() * 2);
            n--;
        }

        if(n <= 0) {
            storageChange.getIntegerStorage().removeObserver(this);
        }
    }
}
