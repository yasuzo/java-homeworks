package hr.fer.zemris.java.custom.collections;

/**
 * Processor interface whose method is meant to be called for an object that needs to be processed.
 *
 * @author Jan Capek
 */
public interface Processor {

    /**
     * Processes given object.
     *
     * @param value Object that needs to be processed.
     */
    void process(Object value);
}
