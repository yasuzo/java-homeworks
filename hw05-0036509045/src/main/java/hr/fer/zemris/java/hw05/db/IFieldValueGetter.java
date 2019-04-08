package hr.fer.zemris.java.hw05.db;

/**
 * Functional interface for returning field values of student record.
 *
 * @author Jan Capek
 */
public interface IFieldValueGetter {

    /**
     * Returns a value from given record.
     *
     * @param record Record to return value from.
     * @return Value from given record.
     */
    String get(StudentRecord record);
}
