package hr.fer.zemris.java.hw05.db;

/**
 * Interface used to check a {@link StudentRecord} against a statement.
 *
 * @author Jan Capek
 */
public interface IFilter {

    /**
     * Checks if a student record should be accepted or not.
     *
     * @param record Record to accept.
     * @return {@code true} if the record should be accepted, {@code false} otherwise.
     */
    boolean accepts(StudentRecord record);
}
