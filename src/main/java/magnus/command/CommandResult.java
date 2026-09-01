package magnus.command;

/**
 * Contains the type and user-facing message produced by an executed command.
 *
 * @param commandType The type of command that was executed.
 * @param message The message describing the result of the command.
 */
public record CommandResult(CommandType commandType, String message) {
}
