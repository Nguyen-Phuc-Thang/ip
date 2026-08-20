package magnus.command;

import magnus.exception.MagnusException;

/**
 * Represents an executable command in Magnus.
 */
public interface Command {
    /**
     * Executes this command using the supplied arguments.
     *
     * @param args The arguments required by this command.
     * @throws MagnusException If the command cannot be executed with the supplied arguments.
     */
    void execute(String[] args) throws MagnusException;
}
