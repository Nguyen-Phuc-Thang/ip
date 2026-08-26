package magnus.ui;

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

    public void showWelcome() {
        printDivider("");
        System.out.print(BANNER);
        System.out.print(GREETING);
        printDivider("");
    }

    public void printDivider() {
        printDivider(INDENT);
    }

    private void printDivider(String prefix) {
        System.out.println(prefix + DIVIDER);
        System.out.println();
    }
}
