package hr.fer.zemris.java.hw17.trazilica.commands;

import com.yasuzo.shell.Environment;
import com.yasuzo.shell.ShellCommand;
import com.yasuzo.shell.ShellStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shell command used to terminate shell.
 *
 * @author Jan Capek
 */
public class ExitCommand implements ShellCommand {

    private static String NAME;
    private static List<String> DESCRIPTION;

    static {
        NAME = "exit";

        DESCRIPTION = new ArrayList<>();
        DESCRIPTION.add("Command terminates the running shell.");
        DESCRIPTION.add("This command takes in no arguments (arguments will be ignored if there is any).");
        DESCRIPTION = Collections.unmodifiableList(DESCRIPTION);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        return ShellStatus.TERMINATE;
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
