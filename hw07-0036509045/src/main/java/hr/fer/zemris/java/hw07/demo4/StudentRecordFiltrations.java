package hr.fer.zemris.java.hw07.demo4;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class containing required filtrations of student records.
 *
 * @author Jan Capek
 */
public class StudentRecordFiltrations {

    private List<StudentRecord> records;

    /**
     * Constructs a new filtrations object which can execute filtrations on given list of student records.
     *
     * @param records List of student records.
     * @throws NullPointerException If given list is {@code null}.
     */
    public StudentRecordFiltrations(List<StudentRecord> records) {
        this.records = Objects.requireNonNull(records);
    }

    /**
     * Returns number of students whose sum of points from midterm, final and laboratory exceed 25.
     *
     * @return Number of students who match the criteria.
     * @throws NullPointerException If any of the elements in underlying list is {@code null}.
     */
    public long vratiBodovaViseOd25() {
        return records.stream()
                .filter(record -> record.getTotalPoints() > 25)
                .count();
    }

    /**
     * Returns number of students whose final grade is 5.
     *
     * @return Number of students who match the criteria.
     * @throws NullPointerException If any of the elements in underlying list is {@code null}.
     */
    public long vratiBrojOdlikasa() {
        return records.stream()
                .filter(record -> record.getFinalGrade() == 5)
                .count();
    }

    /**
     * Returns student records whose final grade is 5.
     *
     * @return List of student records that match the criteria.
     * @throws NullPointerException If any of the elements in underlying list is {@code null}.
     */
    public List<StudentRecord> vratiListuOdlikasa() {
        return records.stream()
                .filter(record -> record.getFinalGrade() == 5)
                .collect(Collectors.toList());
    }

    /**
     * Returns sorted list of student records whose final grade is 5.
     *
     * @return List of student records that match the criteria.
     * @throws NullPointerException If any of the elements in underlying list is {@code null}.
     */
    public List<StudentRecord> vratiSortiranuListuOdlikasa() {
        Comparator<StudentRecord> studentRecordComparator = (a, b) -> Double.compare(b.getTotalPoints(), a.getTotalPoints());

        return records.stream()
                .filter(record -> record.getFinalGrade() == 5)
                .sorted(studentRecordComparator)
                .collect(Collectors.toList());
    }

    /**
     * Returns sorted list of students' jmbags whose final grade is 1.
     *
     * @return List of students' jmbags who match the criteria.
     * @throws NullPointerException If any of the elements in underlying list is {@code null}.
     */
    public List<String> vratiPopisNepolozenih() {
        return records.stream()
                .filter(record -> record.getFinalGrade() == 1)
                .map(StudentRecord::getJmbag)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Classifies students by their final grades.
     *
     * @return Map of records classified by final grade.
     * @throws NullPointerException If any of the elements in underlying list is {@code null}.
     */
    public Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama() {
        return records.stream()
                .collect(
                        Collectors.groupingBy(StudentRecord::getFinalGrade)
                );
    }

    /**
     * Counts number of students grouped by final grade.
     *
     * @return Grades mapped to number of students having them.
     * @throws NullPointerException If any of the elements in underlying list is {@code null}.
     */
    public Map<Integer, Integer> vratiBrojStudenataPoOcjenama() {
        return records.stream()
                .collect(
                        Collectors.toMap(StudentRecord::getFinalGrade, record -> 1, (oldVal, newVal) -> oldVal + 1)
                );
    }

    /**
     * Partitions students who have passed the class and those who have not.
     *
     * @return Map which keys are {@code true} and {@code false}.
     * Under key true is list of students who have passed the class and under false are those who have failed.
     * @throws NullPointerException If any of the elements in underlying list is {@code null}.
     */
    public Map<Boolean, List<StudentRecord>> razvrstajProlazPad() {
        return records.stream()
                .collect(
                        Collectors.partitioningBy(record -> record.getFinalGrade() > 1)
                );
    }
}
