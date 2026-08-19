package magnus.command;

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
     * @param args The command arguments, which are ignored.
     */
    @Override
    public void execute(String[] args) {
        System.out.println(EXIT_TEXT);
    }
}
