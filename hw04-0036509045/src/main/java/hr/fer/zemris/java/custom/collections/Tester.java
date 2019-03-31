package hr.fer.zemris.java.custom.collections;

/**
 * Interface for testing validity of an object.
 *
 * @param <T> Type of object that should be tested.
 *
 * @author Jan Capek
 */
public interface Tester<T> {

    /**
     * Takes an object and determines if the object is valid or not.
     *
     * @param o Object to be tested.
     * @return {@code true} if the object is valid, {@code false} otherwise.
     */
    boolean test(T o);
}
