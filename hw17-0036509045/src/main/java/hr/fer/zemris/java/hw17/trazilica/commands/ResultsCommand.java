package hr.fer.zemris.java.hw17.trazilica.commands;

import com.yasuzo.search.SearchEngine;
import com.yasuzo.shell.Environment;
import com.yasuzo.shell.ShellCommand;
import com.yasuzo.shell.ShellStatus;
import com.yasuzo.shell.argscheck.ArgumentChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Prints results of the last search query to an environment.
 *
 * @author Jan Capek
 */
public class ResultsCommand implements ShellCommand {

    private static String NAME;
    private static List<String> DESCRIPTION = new ArrayList<>();

    static {
        NAME = "results";
        DESCRIPTION.add("Command takes no arguments and prints results of the most recently performed search query.");
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
//        check args
        try {
            ArgumentChecker.checkExecuteCommandArgs(env, arguments, 0);
        } catch (Exception e) {
            return ShellStatus.CONTINUE;
        }

//        find results
        List<SearchEngine.SearchResult> results = (List<SearchEngine.SearchResult>) env.getSharedData("searchResults");
        if (results == null) {
            env.writeln("No search has been performed yet.");
            return ShellStatus.CONTINUE;
        }

//        print results
        if (results.size() == 0) {
            env.writeln("No relevant documents found.");
        } else {
            int i = 0;
            for (SearchEngine.SearchResult result : results) {
                env.writeln(String.format("[%d] %s", i++, result.getDocumentKey()));
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