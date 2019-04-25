package hr.fer.zemris.java.hw07.demo4;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class containing methods for parsing student records from strings.
 *
 * @author Jan Capek
 */
public class StudentRecordParser {

    /**
     * Parses given string and returns parsed student record.
     * String has to be formatted in a way that it consists of:
     * <ol>
     * <li>jmbag</li>
     * <li>last name</li>
     * <li>first name</li>
     * <li>points on midterm</li>
     * <li>points on final</li>
     * <li>laboratory points</li>
     * <li>final grade</li>
     * </ol>
     * Each attribute has to be separated with a single tab ({@code \t}).
     *
     * @param record String representation of a record.
     * @return Parsed student record.
     * @throws IllegalArgumentException If string contains wrong number of attributes or points couldn't be parsed.
     * @throws NullPointerException     If given string is {@code null}.
     */
    public static StudentRecord parse(String record) {
        Objects.requireNonNull(record);
        String[] attributes = record.split("\t");

        if (attributes.length != 7) {
            throw new IllegalArgumentException("Wrong attribute count of record: '" + record + "'");
        }

        double midtermPoints, finalPoints, laboratoryPoints;
        int finalGrade;
        try {
            midtermPoints = Double.parseDouble(attributes[3]);
            finalPoints = Double.parseDouble(attributes[4]);
            laboratoryPoints = Double.parseDouble(attributes[5]);
            finalGrade = Integer.parseInt(attributes[6]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Illegal attributes in a student record string.");
        }

        return new StudentRecord(
                attributes[0],
                attributes[2],
                attributes[1],
                midtermPoints,
                finalPoints,
                laboratoryPoints,
                finalGrade
        );
    }

    /**
     * Parses a list of student records in string representation and
     * returns a list of {@link StudentRecord} parsed from strings. <br>
     * Each string represents a record with following attributes:
     * <ol>
     * <li>jmbag</li>
     * <li>last name</li>
     * <li>first name</li>
     * <li>points on midterm</li>
     * <li>points on final</li>
     * <li>laboratory points</li>
     * <li>final grade</li>
     * </ol>
     * Each attribute has to be separated with a single tab ({@code \t}).
     *
     * @param records Records in string format.
     * @return List of parsed records.
     * @throws NullPointerException If given list any of its elements is {@code null}.
     * @throws IllegalArgumentException If a record is invalid.
     */
    public static List<StudentRecord> parseAll(List<String> records) {
        Objects.requireNonNull(records);
        List<StudentRecord> parsed = new ArrayList<>(records.size());
        for (String rec : records) {
            parsed.add(parse(rec));
        }
        return parsed;
    }

}
