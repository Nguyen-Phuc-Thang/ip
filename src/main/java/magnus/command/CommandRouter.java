package magnus.command;

import java.util.Arrays;
import java.util.HashMap;

import magnus.exception.CommandNotFoundException;
import magnus.exception.MagnusException;
import magnus.parser.Parser;
import magnus.task.TaskList;

/**
 * Routes user input to the appropriate command for execution.
 * Recognized command words are matched to their corresponding commands.
 */
public class CommandRouter {
    HashMap<String, Command> commands;
    Parser parser;

    /**
     * Creates a command router whose commands operate on the specified task list.
     *
     * @param tasks The task list used by commands that manage tasks.
     */
    public CommandRouter(TaskList tasks) {
        // Initialize commands list
        this.commands = new HashMap<>() {{
            put("bye", new ExitCommand());
            put("list", new ListCommand(tasks));
            put("mark", new MarkCommand(tasks));
            put("unmark", new UnmarkCommand(tasks));
            put("todo", new ToDoCommand(tasks));
            put("deadline", new DeadlineCommand(tasks));
            put("event", new EventCommand(tasks));
            put("delete", new DeleteCommand(tasks));
        }};
        
        this.parser = new Parser();
    }

    /**
     * Parses the user input and executes the command identified by its first token.
     *
     * @param userInput The raw user input to route.
     * @throws CommandNotFoundException If the first token is not a recognized command word.
     * @throws MagnusException If the matching command cannot be executed.
     * @throws ArrayIndexOutOfBoundsException If the input is blank.
     */
    public void route(String userInput) throws MagnusException {
        // Parse user input
        String[] parsedCommand = parser.parse(userInput);
        String command = parsedCommand[0];
        String[] args = Arrays.copyOfRange(parsedCommand, 1, parsedCommand.length);

        // Route commands
        if (this.commands.containsKey(command)) {
            this.commands.get(command).execute(args);
        } else {
            throw new CommandNotFoundException("\tSorry, I don't know what you mean by '" + command + "'");
        }
    }   
}
