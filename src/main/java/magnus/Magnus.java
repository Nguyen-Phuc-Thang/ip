package magnus;

import java.nio.file.Path;
import java.util.Scanner;

import magnus.command.CommandRouter;
import magnus.command.CommandType;
import magnus.exception.MagnusException;
import magnus.storage.Storage;
import magnus.task.TaskList;
import magnus.ui.Ui;

/**
 * Starts Magnus and coordinates its user interface, commands, and persistent task storage.
 */
public class Magnus {
    private static final Path DATA_FILE_PATH = Path.of("data", "magnus.txt");

    /**
     * Creates a Magnus application instance.
     */
    public Magnus() {
    }

    /**
     * Runs Magnus's command-line interaction loop.
     *
     * @param args Command-line arguments, which are currently ignored.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        // Chat resources
        Storage storage = new Storage(DATA_FILE_PATH);
        TaskList tasks;
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (MagnusException exception) {
            System.out.println(exception.getMessage());
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            CommandRouter router = new CommandRouter(tasks);

            // Chat loop
            while (scanner.hasNextLine()) {
                String userInput = scanner.nextLine();
                boolean shouldExit = false;

                // Start of result
                ui.printDivider();

                try {
                    CommandType commandType = router.route(userInput);
                    if (commandType.canChangeTaskList()) {
                        storage.saveTasks(tasks.getTasks());
                    }
                    shouldExit = commandType == CommandType.BYE;
                } catch (MagnusException exception) {
                    System.out.println(exception.getMessage());
                }

                if (shouldExit) {
                    break;
                }

                // End of result
                ui.printDivider();
            }
        }

        ui.printDivider();
    }
}
