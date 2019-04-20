package hr.fer.zemris.java.hw07.observer1;

/**
 * Observer interface for {@link IntegerStorage}.
 *
 * @author Jan Capek
 */
public interface IntegerStorageObserver {

    /**
     * This method will be called on each value change of {@link IntegerStorage} object.
     *
     * @param istorage IntegerStorage whose value has changed.
     */
    void valueChanged(IntegerStorage istorage);
}
