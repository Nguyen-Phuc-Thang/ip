import java.util.*;

public class Magnus {
    public static void main(String[] args) {

        // Chat decorations
        String banner = """
                ███╗   ███╗ █████╗  ██████╗ ███╗   ██╗██╗   ██╗███████╗
                ████╗ ████║██╔══██╗██╔════╝ ████╗  ██║██║   ██║██╔════╝
                ██╔████╔██║███████║██║  ███╗██╔██╗ ██║██║   ██║███████╗
                ██║╚██╔╝██║██╔══██║██║   ██║██║╚██╗██║██║   ██║╚════██║
                ██║ ╚═╝ ██║██║  ██║╚██████╔╝██║ ╚████║╚██████╔╝███████║
                ╚═╝     ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝ ╚══════╝
                """;

        String divider = "____________________________________________________________\n";
        String indent = "\t";

        // Greeting and exit text
        String greeting = """
                Hello! I'm Magnus.
                How can I help you today?
                """;
        
        String exit = "Goodbye. See you soon!";

        // Greeting
        System.out.println(divider);
        System.out.print(banner);
        System.out.print(greeting);
        System.out.println(divider);

        // Chat loop
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        while (true) {
            System.out.println(indent + divider);

            // Bye case
            if (command.compareTo("bye") == 0) {
                System.out.println(indent + exit);
                System.out.println(indent + divider);
                break;
            }

            System.out.println(indent + command);

            System.out.println(indent + divider);
            command = scanner.nextLine();
        }

        // Exit
        scanner.close();
    }
}
