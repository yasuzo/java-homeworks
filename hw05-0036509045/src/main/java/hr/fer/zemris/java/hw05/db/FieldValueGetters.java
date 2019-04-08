package hr.fer.zemris.java.hw05.db;

/**
 * Class that contains {@link StudentRecord} field getters.
 *
 * @author Jan Capek
 */
public class FieldValueGetters {
    public static final IFieldValueGetter FIRST_NAME;
    public static final IFieldValueGetter LAST_NAME;
    public static final IFieldValueGetter JMBAG;

    static {
        FIRST_NAME = StudentRecord::getFirstName;
        LAST_NAME = StudentRecord::getLastName;
        JMBAG = StudentRecord::getJmbag;
    }
}
