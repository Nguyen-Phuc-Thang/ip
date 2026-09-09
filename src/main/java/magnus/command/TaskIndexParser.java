package magnus.command;

import magnus.exception.CommandSyntaxException;
import magnus.exception.TaskNotFoundException;

/**
 * Parses and validates the one-based task number accepted by task-specific commands.
 */
final class TaskIndexParser {
    private TaskIndexParser() {
    }

    /**
     * Returns the zero-based index identified by a command's single task-number argument.
     *
     * @param arguments The command arguments to validate.
     * @param commandKeyword The command keyword used in syntax guidance.
     * @param taskCount The number of tasks that can be selected.
     * @return The validated zero-based task index.
     * @throws CommandSyntaxException If the task number is missing, non-numeric, or accompanied
     *                                by extra arguments.
     * @throws TaskNotFoundException If the task number does not identify an existing task.
     */
    static int parseTaskIndex(String[] arguments, String commandKeyword, int taskCount)
            throws CommandSyntaxException, TaskNotFoundException {
        if (arguments.length == 0 || arguments[0].isBlank()) {
            throw new CommandSyntaxException("\tInvalid syntax! Please tell me the task number.\n"
                    + createUsageMessage(commandKeyword));
        }

        if (arguments.length > 1) {
            throw new CommandSyntaxException("\tInvalid syntax! The " + commandKeyword
                    + " command accepts only one argument.\n" + createUsageMessage(commandKeyword));
        }

        int taskIndex;
        try {
            taskIndex = Integer.parseInt(arguments[0]) - 1;
        } catch (NumberFormatException exception) {
            throw new CommandSyntaxException("\t'" + arguments[0] + "' is not a valid task number.\n"
                    + createUsageMessage(commandKeyword));
        }

        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new TaskNotFoundException("\tSorry, I can't find this task number");
        }
        return taskIndex;
    }

    /**
     * Creates task-number usage guidance for the specified command.
     *
     * @param commandKeyword The command keyword to display.
     * @return The formatted usage guidance.
     */
    private static String createUsageMessage(String commandKeyword) {
        return "\tUsage: " + commandKeyword + " <task number>";
    }
}
