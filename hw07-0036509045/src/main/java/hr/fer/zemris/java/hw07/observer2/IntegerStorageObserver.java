package hr.fer.zemris.java.hw07.observer2;

/**
 * Observer interface for {@link IntegerStorage}.
 *
 * @author Jan Capek
 */
public interface IntegerStorageObserver {

    /**
     * This method will be called on each value change of {@link IntegerStorage} object.
     *
     * @param istorage IntegerStorageChange with information whose value has changed and what are old and new values.
     */
    void valueChanged(IntegerStorageChange istorage);
}
