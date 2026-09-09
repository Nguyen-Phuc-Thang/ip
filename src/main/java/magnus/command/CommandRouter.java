package magnus.command;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import magnus.exception.CommandNotFoundException;
import magnus.exception.CommandSyntaxException;
import magnus.exception.MagnusException;
import magnus.parser.CommandParser;
import magnus.task.TaskList;

/**
 * Routes user input to the appropriate command for execution.
 * Recognized command words are matched to their corresponding commands.
 */
public class CommandRouter {
    private static final int COMMAND_KEYWORD_INDEX = 0;
    private static final int FIRST_ARGUMENT_INDEX = 1;

    private final Map<CommandType, Command> commands;
    private final CommandParser parser;

    /**
     * Creates a command router whose commands operate on the specified task list.
     *
     * @param tasks The task list used by commands that manage tasks.
     */
    public CommandRouter(TaskList tasks) {
        this.commands = new EnumMap<>(CommandType.class);
        this.commands.put(CommandType.BYE, new ExitCommand());
        this.commands.put(CommandType.LIST, new ListCommand(tasks));
        this.commands.put(CommandType.LIST_DEADLINE, new ListDeadlineCommand(tasks));
        this.commands.put(CommandType.LIST_EVENT, new ListEventCommand(tasks));
        this.commands.put(CommandType.FIND, new FindCommand(tasks));
        this.commands.put(CommandType.MARK, new MarkCommand(tasks));
        this.commands.put(CommandType.UNMARK, new UnmarkCommand(tasks));
        this.commands.put(CommandType.TODO, new ToDoCommand(tasks));
        this.commands.put(CommandType.DEADLINE, new DeadlineCommand(tasks));
        this.commands.put(CommandType.EVENT, new EventCommand(tasks));
        this.commands.put(CommandType.DELETE, new DeleteCommand(tasks));
        assert this.commands.size() == CommandType.values().length
                : "Every command type must have a registered command";

        this.parser = new CommandParser();
    }

    /**
     * Parses the user input and executes the command identified by its first token.
     *
     * @param userInput The raw user input to route.
     * @return The type and user-facing message produced by the executed command.
     * @throws CommandNotFoundException If the first token is not a recognized command word.
     * @throws MagnusException If the matching command cannot be executed.
     */
    public CommandResult route(String userInput) throws MagnusException {
        String[] parsedCommandParts = this.parser.parse(userInput);
        if (parsedCommandParts.length == 0) {
            throw new CommandSyntaxException("\tPlease enter a command.");
        }
        String commandKeyword = parsedCommandParts[COMMAND_KEYWORD_INDEX];
        String[] commandArguments = Arrays.copyOfRange(
                parsedCommandParts, FIRST_ARGUMENT_INDEX, parsedCommandParts.length);

        CommandType commandType;
        try {
            commandType = CommandType.parseKeyword(commandKeyword);
        } catch (IllegalArgumentException exception) {
            throw new CommandNotFoundException(
                    "\tSorry, I don't know what you mean by '" + commandKeyword + "'");
        }

        Command command = this.commands.get(commandType);
        assert command != null : "The parsed command type must have a registered command";
        String resultMessage = command.execute(commandArguments);
        assert resultMessage != null : "A successful command must return a response message";
        return new CommandResult(commandType, resultMessage);
    }
}
