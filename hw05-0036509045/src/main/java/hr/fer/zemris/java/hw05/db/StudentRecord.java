package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Class that models a student record.
 * Objects of this class contain student's jmbag, first name, last name and final grade.
 *
 * @author Jan Capek
 */
public class StudentRecord {
    private String jmbag;
    private String firstName;
    private String lastName;
    private int finalGrade;

    /**
     * Constructs a new student record with given jmbag, first name and last name.
     *
     * @param jmbag      Student's jmbag.
     * @param firstName  Student's first name.
     * @param lastName   Student's last name.
     * @param finalGrade Student's final grade.
     * @throws NullPointerException     If any of the parameters are {@code null}.
     * @throws IllegalArgumentException If {@code finalGrade} is not in range [1, 5] or
     *                                  other attributes contain illegal characters.
     *                                  First and last name can only contain letters or
     *                                  in case of multiple last/first names, space/dash between them.
     *                                  Jmbag can only contain 10 digits and nothing more.
     */
    public StudentRecord(String jmbag, String firstName, String lastName, int finalGrade) {
        setJmbag(jmbag);
        setFirstName(firstName);
        setLastName(lastName);
        setFinalGrade(finalGrade);
    }

    /**
     * @return Student's jmbag.
     */
    public String getJmbag() {
        return jmbag;
    }

    /**
     * Sets student's jmbag.
     *
     * @param jmbag New jmbag.
     * @throws NullPointerException     If an argument is {@code null}.
     * @throws IllegalArgumentException If jmbag isn't made of only 10 digits.
     */
    private void setJmbag(String jmbag) {
        Objects.requireNonNull(jmbag);
        if (jmbag.matches("^\\d{10}$") == false) {
            throw new IllegalArgumentException("JMBAG needs to contain 10 digits and nothing more.");
        }
        this.jmbag = jmbag;
    }

    /**
     * @return Student's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets student's first name.
     *
     * @param firstName New first name.
     * @throws NullPointerException     If the argument is null.
     * @throws IllegalArgumentException If name contains illegal characters.
     *                                  Legal characters are letters and, in case of multiple names,
     *                                  space between names.
     */
    private void setFirstName(String firstName) {
        Objects.requireNonNull(firstName);
        if (firstName.matches(".*[\\d_].*")) {
            throw new IllegalArgumentException("First name must contain only letters.");
        }
        this.firstName = firstName;
    }

    /**
     * @return Student's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets student's last name.
     *
     * @param lastName New last name.
     * @throws NullPointerException     If the argument is null.
     * @throws IllegalArgumentException If name contains illegal characters.
     *                                  Legal characters are letters and, in case of multiple names,
     *                                  space between names.
     */
    private void setLastName(String lastName) {
        Objects.requireNonNull(lastName);
        if (lastName.matches(".*[\\d_].*")) {
            throw new IllegalArgumentException("Last name must contain only letters.");
        }
        this.lastName = lastName;
    }

    /**
     * @return Student's final grade.
     */
    public int getFinalGrade() {
        return finalGrade;
    }

    /**
     * Sets student's final grade.
     *
     * @param finalGrade New final grade.
     * @throws IllegalArgumentException If grade is not in range [1, 5].
     */
    private void setFinalGrade(int finalGrade) {
        if (finalGrade < 1 || finalGrade > 5) {
            throw new IllegalArgumentException("Grade must be in range [1, 5].");
        }
        this.finalGrade = finalGrade;
    }

    /**
     * Student records are equal if they have equal jmbags.
     *
     * @param o Object that needs to be checked for equality.
     * @return {@code true} if records are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentRecord that = (StudentRecord) o;
        return Objects.equals(jmbag, that.jmbag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jmbag);
    }
}
