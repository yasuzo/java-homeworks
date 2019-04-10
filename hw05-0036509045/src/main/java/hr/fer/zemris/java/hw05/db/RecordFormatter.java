package hr.fer.zemris.java.hw05.db;

import java.util.List;
import java.util.Objects;

/**
 * Instance of this class is used to format a list of records in table-like format.
 *
 * @author Jan Capek
 */
public class RecordFormatter {

    /**
     * String representing top and bottom table border.
     */
    private static String horizontalBorders;

    /**
     * Formats a given list of records in table-like structure.
     *
     * @param records Records to format.
     * @return String representation of records in table-like structure or empty string ({@code ""}) if given list is empty.
     * @throws NullPointerException If given list is {@code null}.
     */
    public static String format(List<StudentRecord> records) {
        Objects.requireNonNull(records);

        if (records.isEmpty()) {
            return  "+======================+\n| No records selected. |\n+======================+";
        }

        String rowFormat = createFormat(records);

        StringBuilder sb = new StringBuilder(horizontalBorders);
        for (StudentRecord rec : records) {
            String row = String.format(rowFormat, rec.getJmbag(), rec.getLastName(), rec.getFirstName(), rec.getFinalGrade());
            sb.append(String.format("%n%s", row));
        }
        sb.append(String.format("%n%s", horizontalBorders));

        return sb.toString();
    }

    /**
     * Creates a table row format used by {@link String#format(String, Object...)}.
     *
     * @param records Records that need to fit in the table.
     * @return Row format adjusted for given records.
     */
    private static String createFormat(List<StudentRecord> records) {
        int maxLenJmbag, maxLenLastName, maxLenFirstName, maxLenGrade;
        maxLenJmbag = maxLenLastName = maxLenFirstName = maxLenGrade = 0;

//        find max length for each attribute
        for (StudentRecord rec : records) {
            int jmbagLen = rec.getJmbag().length();
            if (jmbagLen > maxLenJmbag) {
                maxLenJmbag = jmbagLen;
            }
            int lastNameLen = rec.getLastName().length();
            if (lastNameLen > maxLenLastName) {
                maxLenLastName = lastNameLen;
            }
            int firstNameLen = rec.getFirstName().length();
            if (firstNameLen > maxLenFirstName) {
                maxLenFirstName = firstNameLen;
            }
            int gradeLen = String.valueOf(rec.getFinalGrade()).length();
            if (gradeLen > maxLenGrade) {
                maxLenGrade = gradeLen;
            }
        }

        buildHorizontalBorder(maxLenJmbag, maxLenLastName, maxLenFirstName, maxLenGrade);

        return String.format("| %%-%ds | %%-%ds | %%-%ds | %%-%ds |", maxLenJmbag, maxLenLastName, maxLenFirstName, maxLenGrade);
    }

    /**
     * Builds top and bottom border of the table.
     *
     * @param lengths Array that contains lengths of each column of the table.
     */
    private static void buildHorizontalBorder(int... lengths) {
        StringBuilder sb = new StringBuilder();

        for (int len : lengths) {
            sb.append('+');
            for (int i = 0; i < len + 2; i++) {
                sb.append('=');
            }
        }
        sb.append('+');

        horizontalBorders = sb.toString();
    }
}
