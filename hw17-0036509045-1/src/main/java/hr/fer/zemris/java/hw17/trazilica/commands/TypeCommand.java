package hr.fer.zemris.java.hw17.trazilica.commands;

import com.yasuzo.search.SearchEngine;
import com.yasuzo.shell.Environment;
import com.yasuzo.shell.ShellCommand;
import com.yasuzo.shell.ShellStatus;
import com.yasuzo.shell.argscheck.ArgumentChecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Command that reads a document that is a result of a query and outputs it to the environment.
 * This takes one argument which is an index of the search result.
 *
 * @author Jan Capek
 */
public class TypeCommand implements ShellCommand {

    private static String NAME;
    private static List<String> DESCRIPTION = new ArrayList<>();

    static {
        NAME = "type";
        DESCRIPTION.add("Command takes one argument which is an index of the searched result list " +
                "and prints content of the document at given index.");
    }


    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
//        check args
        List<String> args;
        try {
            args = ArgumentChecker.checkExecuteCommandArgs(env, arguments, 1);
        } catch (Exception e) {
            return ShellStatus.CONTINUE;
        }

//        find wanted index
        int index;
        try {
            index = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            env.writeln("Given argument must be an integer.");
            return ShellStatus.CONTINUE;
        }

//        get results from shared data
        List<SearchEngine.SearchResult> results = (List<SearchEngine.SearchResult>) env.getSharedData("searchResults");
        if (results == null) {
            env.writeln("No query has been performed yet.");
            return ShellStatus.CONTINUE;
        }

//        get wanted query result
        SearchEngine.SearchResult wantedDocument;
        try {
            wantedDocument = results.get(index);
        } catch (IndexOutOfBoundsException e) {
            env.writeln("There is no result at index [" + index + "].");
            return ShellStatus.CONTINUE;
        }

//        read lines and output them
        try {
            Stream<String> linesStream = Files.lines(Path.of(wantedDocument.getDocumentKey()));
            env.writeln("-------------------------------------------------------------------------------");
            env.writeln("Document: " + wantedDocument.getDocumentKey());
            env.writeln("-------------------------------------------------------------------------------");
            linesStream.forEach(env::writeln);
            env.writeln("-------------------------------------------------------------------------------");
            linesStream.close();
        } catch (IOException e) {
            env.writeln("Document at index [" + index + "] could not be read.");
            return ShellStatus.CONTINUE;
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
