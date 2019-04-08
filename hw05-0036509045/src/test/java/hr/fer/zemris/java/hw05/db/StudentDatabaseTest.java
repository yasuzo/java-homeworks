package hr.fer.zemris.java.hw05.db;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentDatabaseTest {

    private StudentDatabase db;

    StudentDatabaseTest() throws IOException {
        Path p = Paths.get("src/test/resources/database.txt");
        db = new StudentDatabase(Files.readAllLines(p));
    }

    @Test
    void forJMBAG() {
        StudentRecord expected = new StudentRecord("0000000008", "Marko", "Ćurić", 5);
        StudentRecord s1 = db.forJMBAG("0000000008");
        assertEquals(expected, s1);
    }

    @Test
    void filter() {
        List<StudentRecord> records = db.filter(r -> true);
        assertEquals(db.size(), records.size());
        records = db.filter(r -> false);
        assertEquals(0, records.size());
    }
}