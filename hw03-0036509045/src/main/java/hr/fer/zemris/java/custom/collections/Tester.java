package hr.fer.zemris.java.custom.collections;

/**
 * Interface for testing validity of an object.
 *
 * @author Jan Capek
 */
public interface Tester {

    /**
     * Takes an object and determines if the object is valid or not.
     *
     * @param o Object to be tested.
     * @return {@code true} if the object is valid, {@code false} otherwise.
     */
    boolean test(Object o);
}
