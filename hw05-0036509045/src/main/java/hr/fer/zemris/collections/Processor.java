package hr.fer.zemris.collections;

/**
 * Processor interface whose method is meant to be called for an object that needs to be processed.
 *
 * @param <T> Type of object that should be processed.
 * @author Jan Capek
 */
public interface Processor<T> {

    /**
     * Processes given object.
     *
     * @param value Object that needs to be processed.
     */
    void process(T value);
}
