package hr.fer.zemris.java.hw07.observer2;

import java.util.Objects;

/**
 * Class that encapsulates a change of {@link IntegerStorage} object.
 *
 * @author Jan Capek
 */
public class IntegerStorageChange {

    private IntegerStorage integerStorage;
    private int oldValue;
    private int newValue;

    /**
     * Constructs a new {@link IntegerStorageChange} which encapsulates object whose value has change, its old and new values.
     *
     * @param integerStorage Instance whose value has changed.
     * @param oldValue       Previous value.
     * @param newValue       New value.
     * @throws NullPointerException If given storage is {@code null}.
     */
    public IntegerStorageChange(IntegerStorage integerStorage, int oldValue, int newValue) {
        this.integerStorage = Objects.requireNonNull(integerStorage);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * @return Storage whose value has changed.
     */
    public IntegerStorage getIntegerStorage() {
        return integerStorage;
    }

    /**
     * @return Value before change.
     */
    public int getOldValue() {
        return oldValue;
    }

    /**
     * @return New changed value.
     */
    public int getNewValue() {
        return newValue;
    }
}
