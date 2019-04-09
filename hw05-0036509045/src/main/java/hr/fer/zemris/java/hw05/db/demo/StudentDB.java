package hr.fer.zemris.java.hw05.db.demo;

import hr.fer.zemris.java.hw05.db.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Program that accepts a query and returns rows that satisfy given query.
 * This uses a database with data from path "src/main/resources/database.txt".
 *
 * @author Jan Capek
 */
public class StudentDB {
    public static void main(String[] args) {

//        create db
        StudentDatabase db = createDatabaseOrExit(Paths.get("src/main/resources/database.txt"));

        Scanner sc = new Scanner(System.in);
//        ask for input
        System.out.print("> ");
        for (String in = sc.next().trim(); in.equals("exit") == false; in = sc.next().trim()) {
//            check if command is ok.
            if (in.equals("query") == false) {
                System.out.println("Command not recognized. Try again.");
                sc.nextLine();
                System.out.print("> ");
                continue;
            }
//            read query and print result
            readQueryAndPrintResult(sc, db);
            System.out.print("> ");
        }

        sc.close();
        System.out.println("Goodbye!");
    }

    /**
     * Reads a query and prints a result.
     *
     * @param sc Scanner to read query with.
     * @param db Student database where records will be searched.
     */
    private static void readQueryAndPrintResult(Scanner sc, StudentDatabase db) {
        String query = sc.nextLine();
        try {
            QueryParser parser = new QueryParser(query);

            List<StudentRecord> filteredRecords;

            if (parser.isDirectQuery()) {
//                direct query
                StudentRecord record = db.forJMBAG(parser.getQueriedJMBAG());
                filteredRecords = new ArrayList<>();
                if (record != null) {
                    filteredRecords.add(record);
                }
            } else {
//                indirect query
                IFilter filter = new QueryFilter(parser.getQuery());
                filteredRecords = db.filter(filter);
            }

            System.out.println(RecordFormatter.format(filteredRecords));
            System.out.println("Records selected: " + filteredRecords.size());

        } catch (QueryParserException e) {
            System.out.println("Invalid query. Try again.");
        } catch (IllegalArgumentException e) {
            System.out.println("Some arguments are illegal. Try again.");
        } catch (Exception e) {
            System.out.println("Unexpected error occurred. Try again.");
        }
    }

    /**
     * Creates a student database from file path or exits if an error happened.
     *
     * @param p Path to a records file.
     * @return New student database.
     */
    private static StudentDatabase createDatabaseOrExit(Path p) {
        try {
            List<String> lines = Files.readAllLines(
                    Paths.get("src/main/resources/database.txt"),
                    StandardCharsets.UTF_8
            );
            return new StudentDatabase(lines);
        } catch (IOException e) {
            System.out.println("Could not read the file. Exiting...");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println("Lines contain duplicates or are illegal. Exiting...");
            System.exit(1);
        }
        return null;
    }
}
