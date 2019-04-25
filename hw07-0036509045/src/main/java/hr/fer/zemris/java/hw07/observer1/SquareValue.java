package hr.fer.zemris.java.hw07.observer1;

import java.util.Objects;

/**
 * Observer that prints out to standard output square of a new IntegerStorage value.
 *
 * @author Jan Capek
 */
public class SquareValue implements IntegerStorageObserver {

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException If given storage is {@code null}.
     */
    @Override
    public void valueChanged(IntegerStorage istorage) {
        Objects.requireNonNull(istorage);

        int newValue = istorage.getValue();
        int square = newValue * newValue;
        System.out.format("Provided new value: %d, square is %d%n", newValue, square);
    }
}
