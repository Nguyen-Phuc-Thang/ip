package magnus.ui;

/**
 * Displays Magnus's welcome message and output dividers on the command line.
 */
public class Ui {
    private static final String UI_BANNER = """
            ███╗   ███╗ █████╗  ██████╗ ███╗   ██╗██╗   ██╗███████╗
            ████╗ ████║██╔══██╗██╔════╝ ████╗  ██║██║   ██║██╔════╝
            ██╔████╔██║███████║██║  ███╗██╔██╗ ██║██║   ██║███████╗
            ██║╚██╔╝██║██╔══██║██║   ██║██║╚██╗██║██║   ██║╚════██║
            ██║ ╚═╝ ██║██║  ██║╚██████╔╝██║ ╚████║╚██████╔╝███████║
            ╚═╝     ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝ ╚══════╝
            """;
    private static final String UI_GREETING = """
            Hello! I'm Magnus.
            How can I help you today?
            """;
    private static final String UI_DIVIDER = "____________________________________________________________";
    private static final String UI_INDENT = "\t";

    /**
     * Creates a command-line user interface.
     */
    public Ui() {
    }

    /**
     * Prints the application banner, greeting, and surrounding dividers.
     */
    public void showWelcome() {
        printDivider("");
        System.out.print(UI_BANNER);
        System.out.print(UI_GREETING);
        printDivider("");
    }

    /**
     * Prints an indented divider followed by a blank line.
     */
    public void printDivider() {
        printDivider(UI_INDENT);
    }

    /**
     * Prints a divider with the supplied prefix, followed by a blank line.
     *
     * @param prefix The text to print immediately before the divider.
     */
    private void printDivider(String prefix) {
        System.out.println(prefix + UI_DIVIDER);
        System.out.println();
    }
}
