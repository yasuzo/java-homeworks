package hr.fer.zemris.java.hw17.trazilica.commands;

import com.yasuzo.search.SearchEngine;
import com.yasuzo.shell.Environment;
import com.yasuzo.shell.ShellCommand;
import com.yasuzo.shell.ShellStatus;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Command that takes a search query and lists most similar results.
 *
 * @author Jan Capek
 */
public class QueryCommand implements ShellCommand {

    private static String NAME;
    private static List<String> DESCRIPTION = new ArrayList<>();

    static {
        NAME = "query";
        DESCRIPTION.add("Query command takes a search query string argument (continuous string after given command keyword) " +
                "and returns 10 most similar results if there are any.");
        DESCRIPTION = Collections.unmodifiableList(DESCRIPTION);
    }

    /**
     * Maximal number of printed results.
     */
    private static int MAX_RESULTS = 10;

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Objects.requireNonNull(arguments);

        SearchEngine searchEngine = (SearchEngine) env.getSharedData("searchEngine");
        if (searchEngine == null) {
            env.writeln("ERROR - Cannot execute a command at this time.");
            return ShellStatus.CONTINUE;
        }

//        save results in shared data
        SortedSet<SearchEngine.SearchResult> searchResults = searchEngine.findSimilar(arguments);
        env.setSharedData("searchResults", searchResults.stream().limit(MAX_RESULTS).collect(Collectors.toList()));

//        print first MAX_RESULTS results
        env.writeln("Query is: [" + String.join(", ", searchEngine.getQueryWords()) + "]");
        if (searchResults.size() == 0) {
            env.writeln("No relevant documents found.");
        } else {
            env.writeln(MAX_RESULTS + " most relevant results:");
            int i = 0;
            for (SearchEngine.SearchResult result : searchResults) {
                env.writeln(String.format("[%d] (%.5f) %s", i++, result.getSimilarity(), result.getDocumentKey()));
                if (i >= MAX_RESULTS) {
                    break;
                }
            }
        }
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return NAME;
    }

    @Override
    public List<String> getCommandDescription() {
        return DESCRIPTION;
    }
}
