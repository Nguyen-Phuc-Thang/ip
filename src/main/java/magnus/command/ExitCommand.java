package magnus.command;

import magnus.exception.CommandSyntaxException;

/**
 * Displays Magnus's farewell message.
 */
public class ExitCommand implements Command {
    private static String EXIT_TEXT = "\tGoodbye. See you soon!";

    /**
     * Creates a command that displays Magnus's farewell message.
     */
    public ExitCommand() {
    }

    /**
     * Prints the farewell message to standard output.
     *
     * @param args The command arguments.
     */
    @Override
    public void execute(String[] args) throws CommandSyntaxException {
        if (args.length > 0) {
            throw new CommandSyntaxException("\tInvalid syntax! The bye command does not accept arguments.\n"
                    + "\tUsage: bye");
        }
        System.out.println(EXIT_TEXT);
    }
}
