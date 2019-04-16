package security.demo;

/**
 * Interface that models a command that needs to be executed.
 *
 * @author Jan Capek
 */
public interface Command {

    /**
     * Executes a command.
     */
    void execute();
}
