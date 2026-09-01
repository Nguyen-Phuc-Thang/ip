package magnus;

import java.nio.file.Path;
import java.util.Scanner;

import magnus.command.CommandResult;
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

    private final Storage storage;
    private final TaskList tasks;
    private final CommandRouter router;
    private boolean isExitRequested;

    /**
     * Creates a Magnus application using the default task data file.
     *
     * @throws MagnusException If the saved tasks cannot be loaded.
     */
    public Magnus() throws MagnusException {
        this(DATA_FILE_PATH);
    }

    /**
     * Creates a Magnus application using the specified task data file.
     *
     * @param dataFilePath Path used to load and save tasks.
     * @throws MagnusException If the saved tasks cannot be loaded.
     */
    public Magnus(Path dataFilePath) throws MagnusException {
        this.storage = new Storage(dataFilePath);
        this.tasks = new TaskList(this.storage.loadTasks());
        this.router = new CommandRouter(this.tasks);
        this.isExitRequested = false;
    }

    /**
     * Processes one user command and returns its response message.
     * Task changes are saved before a successful response is returned.
     *
     * @param userInput The raw command entered by the user.
     * @return The command result message, or an error message if the command fails.
     */
    public String getResponse(String userInput) {
        this.isExitRequested = false;

        try {
            CommandResult commandResult = this.router.route(userInput);
            CommandType commandType = commandResult.commandType();
            if (commandType.canChangeTaskList()) {
                this.storage.saveTasks(this.tasks.getTasks());
            }
            this.isExitRequested = commandType == CommandType.BYE;
            return commandResult.message();
        } catch (MagnusException exception) {
            return exception.getMessage();
        }
    }

    /**
     * Returns whether the latest successfully processed command requested an exit.
     *
     * @return {@code true} if the latest command was {@code bye}; otherwise {@code false}.
     */
    public boolean isExitRequested() {
        return this.isExitRequested;
    }

    /**
     * Runs Magnus's command-line interaction loop.
     *
     * @param args Command-line arguments, which are currently ignored.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Magnus magnus;
        try {
            magnus = new Magnus();
        } catch (MagnusException exception) {
            System.out.println(exception.getMessage());
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            // Chat loop
            while (scanner.hasNextLine()) {
                String userInput = scanner.nextLine();

                // Start of result
                ui.printDivider();

                String response = magnus.getResponse(userInput);
                System.out.println(response);

                if (magnus.isExitRequested()) {
                    break;
                }

                // End of result
                ui.printDivider();
            }
        }

        ui.printDivider();
    }
}
