package magnus;

import java.util.Scanner;

import magnus.command.CommandRouter;
import magnus.exception.MagnusException;
import magnus.task.TaskList;

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

        // Greeting
        System.out.println(divider);
        System.out.print(banner);
        System.out.print(greeting);
        System.out.println(divider);

        // Chat resources
        Scanner scanner = new Scanner(System.in);
        TaskList tasks = new TaskList();
        CommandRouter router = new CommandRouter(tasks);

        // Chat loop
        while (true) {
            String userInput = scanner.nextLine();

            // Start of result
            System.out.println(indent + divider);

            try {
                router.route(userInput);
            } catch (MagnusException exception) {
                System.out.println(exception.getMessage());
            }

            if (userInput.equals("bye")) {
                break;
            }

            // End of result
            System.out.println(indent + divider);
        }

        System.out.println(indent + divider); 

        // Exit
        scanner.close();
    }
}
