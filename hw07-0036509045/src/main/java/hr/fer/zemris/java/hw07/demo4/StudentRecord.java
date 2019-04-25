package hr.fer.zemris.java.hw07.demo4;

import java.util.Objects;

/**
 * Objects of this class model a student record which contains student's jmbag, first and last name, points on midterm,
 * points on finals, laboratory points and final grade.
 *
 * @author Jan Capek
 */
public class StudentRecord {

    private String jmbag;
    private String firstName;
    private String lastName;
    private double midtermPoints;
    private double finalPoints;
    private double labPoints;
    private int finalGrade;

    /**
     * Constructs a new record with given attributes.
     *
     * @param jmbag         Student's jmbag.
     * @param firstName     Student's first name.
     * @param lastName      Student's last name.
     * @param midtermPoints Points on midterm exam.
     * @param finalPoints   Points on final exam.
     * @param labPoints     Laboratory points.
     * @param finalGrade    Final grade.
     * @throws NullPointerException If jmbag, first name or last name are {@code null}.
     */
    public StudentRecord(String jmbag, String firstName, String lastName, double midtermPoints, double finalPoints, double labPoints, int finalGrade) {
        this.jmbag = Objects.requireNonNull(jmbag);
        this.firstName = Objects.requireNonNull(firstName);
        this.lastName = Objects.requireNonNull(lastName);
        this.midtermPoints = midtermPoints;
        this.finalPoints = finalPoints;
        this.labPoints = labPoints;
        this.finalGrade = finalGrade;
    }

    /**
     * @return Student's jmbag.
     */
    public String getJmbag() {
        return jmbag;
    }

    /**
     * @return Student's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return Student's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return Student's points on midterm exam.
     */
    public double getMidtermPoints() {
        return midtermPoints;
    }

    /**
     * @return Student's points on final exam.
     */
    public double getFinalPoints() {
        return finalPoints;
    }

    /**
     * @return Student's laboratory points.
     */
    public double getLabPoints() {
        return labPoints;
    }

    /**
     * @return Student's final grade.
     */
    public int getFinalGrade() {
        return finalGrade;
    }

    /**
     * @return Total points a student has gained from midterm, final and laboratory.
     */
    public double getTotalPoints() {
        return midtermPoints + finalPoints + labPoints;
    }

    @Override
    public String toString() {
        return String.format(
                "%s\t%s\t%s\t%.2f\t%.2f\t%.2f\t%d",
                jmbag,
                lastName,
                firstName,
                midtermPoints,
                finalPoints,
                labPoints,
                finalGrade);
    }
}
