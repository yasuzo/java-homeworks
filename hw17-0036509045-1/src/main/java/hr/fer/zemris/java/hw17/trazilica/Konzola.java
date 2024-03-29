package hr.fer.zemris.java.hw17.trazilica;

import com.yasuzo.search.SearchEngine;
import com.yasuzo.search.Vocabulary;
import com.yasuzo.shell.DefaultShell;
import com.yasuzo.shell.Environment;
import com.yasuzo.shell.NormalEnvironment;
import com.yasuzo.shell.ShellIOException;
import hr.fer.zemris.java.hw17.trazilica.commands.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Demo program demonstrating search.
 *
 * @author Jan Capek
 */
public class Konzola {
    public static void main(String[] args) throws IOException {
        if(args.length != 1 && args.length != 2) {
            System.out.println("Invalid number of arguments! - Program takes one or two arguments:");
            System.out.println("\t1) Directory which contains searchable documents.");
            System.out.println("\t2) File with stop words. (optional)");
            return;
        }

//        determine paths
        Path stopWords = args.length == 1 ? Path.of("src/main/resources/stopwords_hr.txt") : Path.of(args[1]);
        Path documentsDir = Path.of(args[0]);

//        create search engine & vocabulary
        SearchEngine searchEngine;
        Vocabulary vocabulary;
        try {
            vocabulary = createVocabulary(stopWords);
            searchEngine = createSearchEngine(documentsDir, vocabulary);
        } catch (IOException e) {
            System.out.println("Given paths could not be accessed.");
            return;
        }

//        create environment and register commands
        Scanner sc = new Scanner(System.in);
        Environment env = new NormalEnvironment(sc);
        env.setSharedData("searchEngine", searchEngine);
        env.registerCommand("exit", new ExitCommand());
        env.registerCommand("query", new QueryCommand());
        env.registerCommand("type", new TypeCommand());
        env.registerCommand("results", new ResultsCommand());
        env.registerCommand("help", new HelpCommand());

//        create shell and start it
        DefaultShell shell = new DefaultShell(env);
        try {
            System.out.println("Velicina rijecnika je "  + vocabulary.wordCount() + " rijeci.\n");
            shell.start();
        } catch (ShellIOException e) {
            System.err.println("FATAL ERROR! - exiting....");
        }
        sc.close();
    }

    /**
     * Creates a document search engine and fills it with documents read in given document directory.
     * Entities in the directory that are not readable will be ignored.
     *
     * @param documentDirectory Directory containing searchable documents.
     * @param vocabulary Vocabulary used by the indexer.
     * @return Document indexer.
     * @throws IOException If directory could not be accessed.
     * @throws NullPointerException If any of the arguments is {@code null}.
     */
    private static SearchEngine createSearchEngine(Path documentDirectory, Vocabulary vocabulary) throws IOException {
        Objects.requireNonNull(documentDirectory);
        Objects.requireNonNull(vocabulary);
        SearchEngine searchEngine = new SearchEngine(vocabulary);
        Stream<Path> pathStream = Files.list(documentDirectory);
        pathStream.forEach(path -> {
            try {
                searchEngine.addDocument(path.toString(), Files.readString(path));
            } catch (IOException ignorable) {
            }
        });
        pathStream.close();
        return searchEngine;
    }

    /**
     * Creates a vocabulary with stop words read from the file. Every line should contain one word in lower case.
     *
     * @param stopWordFile Path to a file with stop words.
     * @return Vocabulary with stop words read from the file.
     * @throws IOException If file could not be read.
     */
    private static Vocabulary createVocabulary(Path stopWordFile) throws IOException {
        Objects.requireNonNull(stopWordFile);
        Vocabulary vocabulary = new Vocabulary();
        Stream<String> lineStream = Files.lines(stopWordFile);
        lineStream.forEach(word -> {
            if (word.isBlank()) {
                return;
            }
            vocabulary.registerStopWord(word);
        });
        lineStream.close();
        return vocabulary;
    }
}
