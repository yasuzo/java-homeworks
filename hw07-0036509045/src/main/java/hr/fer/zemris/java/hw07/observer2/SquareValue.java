package hr.fer.zemris.java.hw07.observer2;

import java.util.Objects;

/**
 * Observer that prints out to standard output square of a new IntegerStorage value.
 *
 * @author Jan Capek
 */
public class SquareValue implements IntegerStorageObserver {

    /**
     * {@inheritDoc}
     * @throws NullPointerException If given argument is {@code null}.
     */
    @Override
    public void valueChanged(IntegerStorageChange storageChange) {
        Objects.requireNonNull(storageChange);

        int newValue = storageChange.getNewValue();
        int square = newValue * newValue;
        System.out.format("Provided new value: %d, square is %d%n", newValue, square);
    }
}
