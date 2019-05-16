package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Prim list model that adds prim numbers to the lists.
 *
 * @author Jan Capek
 */
public class PrimListModel implements ListModel<Integer> {

    List<Integer> prims;
    List<ListDataListener> listeners;

    /**
     * Constructs a new prim list model.
     */
    public PrimListModel() {
        prims = new ArrayList<>();
        listeners = new ArrayList<>();
        prims.add(1);
    }


    /**
     * Adds next prime to the list.
     */
    public void next() {
        int last = prims.get(getSize() - 1);
        prims.add(nextPrime(last));
        dispatch();
    }

    /**
     * Calculates a next prime number that is larger than number given as an argument.
     *
     * @param number Number that returned prime should be bigger than.
     * @return Prime greater than given number.
     */
    private int nextPrime(int number) {
        if (number < 1) {
            return 2;
        }

        /*
        Calculate next possible number. If given number is even
        next possible is number + 1 else next possible is number + 2
        */
        int nextNumber = number % 2 == 0 ? number + 1 : number + 2;

        while (isPrime(nextNumber) == false) {
            nextNumber += 2;
        }
        return nextNumber;
    }

    /**
     * Checks if given number is prime.
     *
     * @param number Number to check.
     * @return {@code true} if number is a prime, {@code false} otherwise.
     */
    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        if (number == 2) {
            return true;
        }
        if (number % 2 == 0) {
            return false;
        }
        int limit = (int) Math.sqrt(number);
        for (int i = 3; i <= limit; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Notifies all listeners.
     */
    private void dispatch() {
        listeners.forEach(l -> {
            l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, prims.size() - 1, prims.size() - 1));
        });
    }

    @Override
    public int getSize() {
        return prims.size();
    }

    /**
     * {@inheritDoc}
     * @throws IndexOutOfBoundsException If index is not valid.
     */
    @Override
    public Integer getElementAt(int index) {
        return prims.get(index);
    }

    /**
     * {@inheritDoc}
     * @throws NullPointerException If given listener is {@code null}.
     */
    @Override
    public void addListDataListener(ListDataListener l) {
        Objects.requireNonNull(l);
        listeners = new ArrayList<>(listeners);
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners = new ArrayList<>(listeners);
        listeners.remove(l);
    }
}
