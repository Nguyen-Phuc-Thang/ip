package magnus.command;

/**
 * Represents an executable command in Magnus.
 */
public interface Command {
    /**
     * Executes this command using the supplied arguments.
     *
     * @param args The arguments required by this command.
     */
    void execute(String[] args);
}
