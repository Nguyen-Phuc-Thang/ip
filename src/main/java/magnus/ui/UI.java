package magnus.ui;

/**
 * Displays Magnus's welcome message and output dividers on the command line.
 */
public class UI {
    private static final String BANNER = """
            ███╗   ███╗ █████╗  ██████╗ ███╗   ██╗██╗   ██╗███████╗
            ████╗ ████║██╔══██╗██╔════╝ ████╗  ██║██║   ██║██╔════╝
            ██╔████╔██║███████║██║  ███╗██╔██╗ ██║██║   ██║███████╗
            ██║╚██╔╝██║██╔══██║██║   ██║██║╚██╗██║██║   ██║╚════██║
            ██║ ╚═╝ ██║██║  ██║╚██████╔╝██║ ╚████║╚██████╔╝███████║
            ╚═╝     ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝ ╚══════╝
            """;
    private static final String GREETING = """
            Hello! I'm Magnus.
            How can I help you today?
            """;
    private static final String DIVIDER = "____________________________________________________________";
    private static final String INDENT = "\t";

    /**
     * Creates a command-line user interface.
     */
    public UI() {
    }

    /**
     * Prints the application banner, greeting, and surrounding dividers.
     */
    public void showWelcome() {
        printDivider("");
        System.out.print(BANNER);
        System.out.print(GREETING);
        printDivider("");
    }

    /**
     * Prints an indented divider followed by a blank line.
     */
    public void printDivider() {
        printDivider(INDENT);
    }

    /**
     * Prints a divider with the supplied prefix, followed by a blank line.
     *
     * @param prefix The text to print immediately before the divider.
     */
    private void printDivider(String prefix) {
        System.out.println(prefix + DIVIDER);
        System.out.println();
    }
}
