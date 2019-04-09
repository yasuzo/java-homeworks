package hr.fer.zemris.java.hw05.db;

import java.util.*;

/**
 * Instances of this class represent student databases.
 *
 * @author Jan Capek
 */
public class StudentDatabase {

    private Map<String, StudentRecord> recordsByIndex;
    private List<StudentRecord> records;

    /**
     * Creates a new database with list of recordsByIndex given as argument.
     * Each element in given list needs to represent student in format:
     * {@code [jmbag\tlastName\tfirstName\tfinalGrade]}.
     *
     * @param studentData List of recordsByIndex from db file.
     * @throws NullPointerException     If {@code studentData} is {@code null} or any of it's elements.
     * @throws IllegalArgumentException If data is invalid.
     */
    public StudentDatabase(List<String> studentData) {
        Objects.requireNonNull(studentData);
        recordsByIndex = new HashMap<>((int) (studentData.size() * 1.5));
        records = new ArrayList<>(studentData.size());

        for (String row : studentData) {
            Objects.requireNonNull(row);

            StudentRecord record = parseRow(row);
            if (recordsByIndex.containsKey(record.getJmbag())) {
                throw new IllegalArgumentException("List contains duplicates.");
            }
            records.add(record);
            recordsByIndex.put(record.getJmbag(), record);
        }
    }

    /**
     * Parses a row and returns a student record with parsed attributes.
     *
     * @param row String representing a single row (student record) in db file.
     * @return Student record with parsed attributes.
     * @throws IllegalArgumentException If the row is invalid.
     */
    private StudentRecord parseRow(String row) {
        String[] attributes = row.split("\\t");
        if (attributes.length != 4) {
            throw new IllegalArgumentException("Invalid record: " + row);
        }

        String jmbag = attributes[0];
        String lastName = attributes[1];
        String firstName = attributes[2];
        int grade;
        try {
            grade = Integer.parseInt(attributes[3]);
            return new StudentRecord(jmbag, firstName, lastName, grade);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid record: " + row);
        }
    }

    /**
     * Returns a student record with given jmbag.
     * If the record is not found, null is returned.
     *
     * @param jmbag Students jmbag.
     * @return Student record with given jmbag or {@code null} if record was not found.
     */
    public StudentRecord forJMBAG(String jmbag) {
        return recordsByIndex.get(jmbag);
    }

    /**
     * Filters a database with given filter.
     *
     * @param filter Filter used for filtration.
     * @return List of recordsByIndex after filtration.
     * @throws NullPointerException If given filter is {@code null}.
     */
    public List<StudentRecord> filter(IFilter filter) {
        Objects.requireNonNull(filter);

        List<StudentRecord> result = new ArrayList<>(records.size() / 2);
        for (StudentRecord record : records) {
            if (filter.accepts(record)) {
                result.add(record);
            }
        }

        return result;
    }

    /**
     * @return Number of records in the database.
     */
    public int size() {
        return records.size();
    }
}
