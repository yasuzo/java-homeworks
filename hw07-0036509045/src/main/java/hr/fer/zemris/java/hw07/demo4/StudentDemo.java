package hr.fer.zemris.java.hw07.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Demo program that loads students records, processes them in certain ways using stream API
 * and prints out results of each processing.
 *
 * @author Jan Capek
 */
public class StudentDemo {
    public static void main(String[] args) {
        List<String> lines = getLines(Paths.get("src/main/resources/student_records.txt"));

        List<StudentRecord> records = getRecords(lines);

        StudentRecordFiltrations filtrations = new StudentRecordFiltrations(records);

        ArrayList<Action> actions = getActions(filtrations);

//        execute actions
        int i = 1;
        for(Action a : actions) {
            System.out.format("Zadatak %d%n=========%n", i);
            a.execute();
            i++;
        }
    }

    /**
     * Returns a list of lines in a file on success or prints out an appropriate message and exits.
     *
     * @param p Path to a file that needs to be read.
     * @return List of line in a file.
     */
    private static List<String> getLines(Path p) {
        try {
            return Files.readAllLines(Paths.get("src/main/resources/student_records.txt"));
        } catch (IOException e) {
            System.out.println("Could not read a file.");
            System.exit(1);
        }
        return null;
    }

    /**
     * Returns a list of parsed records on success or prints out an appropriate message and exits.
     *
     * @param lines List of student records in string form.
     * @return List of parsed student records.
     */
    private static List<StudentRecord> getRecords(List<String> lines) {
        try{
            return StudentRecordParser.parseAll(lines);
        } catch (IllegalArgumentException e) {
            System.out.println("Some records are invalid. Maybe there are empty lines in the file.");
        } catch (NullPointerException e) {
            System.out.println("Some of the lines are null.");
        }
        System.exit(1);
        return null;
    }

    /**
     * Returns a list with actions that should be executed by the program.
     *
     * @param filtrations Object that does filtrations on student records.
     * @return List of actions that should be executed.
     * @throws NullPointerException If an argument is {@code null}.
     */
    private static ArrayList<Action> getActions(StudentRecordFiltrations filtrations) {
        Objects.requireNonNull(filtrations);
        ArrayList<Action> actions = new ArrayList<>();

        actions.add(() -> System.out.println(filtrations.vratiBodovaViseOd25()));
        actions.add(() -> System.out.println(filtrations.vratiBrojOdlikasa()));
        actions.add(() -> filtrations.vratiListuOdlikasa().forEach(System.out::println));
        actions.add(() -> filtrations.vratiSortiranuListuOdlikasa().forEach(System.out::println));
        actions.add(() -> filtrations.vratiPopisNepolozenih().forEach(System.out::println));
        actions.add(() -> {
            filtrations.razvrstajStudentePoOcjenama().forEach((grade, list) -> {
                System.out.format("%d:%n", grade);
                list.forEach(System.out::println);
            });
        });
        actions.add(() -> {
            filtrations.vratiBrojStudenataPoOcjenama().forEach((grade, count) -> {
                System.out.format("%d => %d%n", grade, count);
            });
        });
        actions.add(() -> {
            filtrations.razvrstajProlazPad().forEach((prolaz, list) -> {
                System.out.format("%s:%n", prolaz ? "Prosli" : "Pali");
                list.forEach(System.out::println);
            });
        });

        return actions;
    }
}
