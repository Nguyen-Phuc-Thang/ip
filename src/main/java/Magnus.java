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

        // Chat resources
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        String[] tasks = new String[100];
        int numOfTasks = 0;

        // Chat loop
        while (true) {
            System.out.println(indent + divider);

            // Bye case
            if (command.compareTo("bye") == 0) {
                System.out.println(indent + exit);
                System.out.println(indent + divider);
                break;
            }

            // List case
            if (command.compareTo("list") == 0) {
                for (int i = 0; i < numOfTasks; i++) {
                    System.out.print(indent);
                    System.out.print(i + 1);
                    System.out.println(". " + tasks[i]);
                }
            } else { // Other cases
                tasks[numOfTasks] = command;
                numOfTasks++;
                System.out.println(indent + "Added: " + command);
            }
            System.out.println(indent + divider);
            command = scanner.nextLine();
        }

        // Exit
        scanner.close();
    }
}
