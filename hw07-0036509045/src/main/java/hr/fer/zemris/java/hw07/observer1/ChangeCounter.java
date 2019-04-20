package hr.fer.zemris.java.hw07.observer1;

import java.util.Objects;

/**
 * Observer that counts number of value changes and prints it to standard output.
 *
 * @author Jan Capek
 */
public class ChangeCounter implements IntegerStorageObserver{

    private int count;

    /**
     * {@inheritDoc}
     * @throws NullPointerException If given storage is {@code null}.
     */
    @Override
    public void valueChanged(IntegerStorage istorage) {
        Objects.requireNonNull(istorage);

        count++;
        System.out.println("Number of value changes since tracking: " + count);
    }
}
