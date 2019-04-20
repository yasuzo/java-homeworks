package hr.fer.zemris.java.hw07.observer2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Integer wrapper.
 *
 * @author Jan Capek
 */
public class IntegerStorage {
    private int value;
    private List<IntegerStorageObserver> observers;

    public IntegerStorage(int initialValue) {
        this.value = initialValue;
    }

    /**
     * Registers an observer if not already present.
     *
     * @param observer Observer that should be registered.
     * @throws NullPointerException If given observer is {@code null}.
     */
    public void addObserver(IntegerStorageObserver observer) {
        Objects.requireNonNull(observer, "Observer cannot be null!");
        if (observers == null) {
            observers = new ArrayList<>();
        }
        if (observers.contains(observer) == false) {
            observers = new ArrayList<>(observers);
            observers.add(observer);
        }
    }

    /**
     * Removes an observer from the internal collection.
     *
     * @param observer Observer that should be removed.
     */
    public void removeObserver(IntegerStorageObserver observer) {
        if(observers == null) {
            return;
        }
        int index = observers.indexOf(observer);
        if(index != -1) {
            copyOnWrite();
            observers.remove(index);
        }
    }

    /**
     * Copies underlying collection of observers in order to be safe for iteration.
     */
    private void copyOnWrite() {
        observers = new ArrayList<>(observers);
    }

    /**
     * Removes all observers from internal collection.
     */
    public void clearObservers() {
        if(observers == null || observers.size() == 0) {
            return;
        }
        copyOnWrite();
        observers.clear();
    }

    /**
     * @return Wrapped value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets a new wrapped value.
     *
     * @param value New value.
     */
    public void setValue(int value) {
        if (this.value == value) {
            return;
        }

//        create change instance
        IntegerStorageChange change = new IntegerStorageChange(this, this.value, value);

//        update
        this.value = value;

//            Notify all registered observers
        if (observers != null) {
            for (IntegerStorageObserver observer : observers) {
                observer.valueChanged(change);
            }
        }
    }
}
