package magnus.command;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import magnus.exception.CommandNotFoundException;
import magnus.exception.MagnusException;
import magnus.parser.Parser;
import magnus.task.TaskList;

/**
 * Routes user input to the appropriate command for execution.
 * Recognized command words are matched to their corresponding commands.
 */
public class CommandRouter {
    private final Map<CommandType, Command> commands;
    private final Parser parser;

    /**
     * Creates a command router whose commands operate on the specified task list.
     *
     * @param tasks The task list used by commands that manage tasks.
     */
    public CommandRouter(TaskList tasks) {
        this.commands = new EnumMap<>(CommandType.class);
        this.commands.put(CommandType.BYE, new ExitCommand());
        this.commands.put(CommandType.LIST, new ListCommand(tasks));
        this.commands.put(CommandType.MARK, new MarkCommand(tasks));
        this.commands.put(CommandType.UNMARK, new UnmarkCommand(tasks));
        this.commands.put(CommandType.TODO, new ToDoCommand(tasks));
        this.commands.put(CommandType.DEADLINE, new DeadlineCommand(tasks));
        this.commands.put(CommandType.EVENT, new EventCommand(tasks));
        this.commands.put(CommandType.DELETE, new DeleteCommand(tasks));
        
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
        String commandKeyword = parsedCommand[0];
        String[] args = Arrays.copyOfRange(parsedCommand, 1, parsedCommand.length);

        CommandType commandType;
        try {
            commandType = CommandType.fromKeyword(commandKeyword);
        } catch (IllegalArgumentException exception) {
            throw new CommandNotFoundException(
                    "\tSorry, I don't know what you mean by '" + commandKeyword + "'");
        }

        this.commands.get(commandType).execute(args);
    }   
}
