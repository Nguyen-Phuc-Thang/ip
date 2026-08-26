package magnus.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import magnus.exception.CommandSyntaxException;
import magnus.parser.DateTimeParser;
import magnus.task.TaskList;

/**
 * Displays deadline tasks that fall on a specified date.
 */
public class ListDeadlineCommand implements Command {
    private final TaskList tasks;
    private final DateTimeParser dateTimeParser;

    /**
     * Creates a command that filters the specified task list by deadline date.
     *
     * @param tasks The task list to filter and display.
     */
    public ListDeadlineCommand(TaskList tasks) {
        this.tasks = tasks;
        this.dateTimeParser = new DateTimeParser();
    }

    /**
     * Parses the supplied date and prints all deadlines that fall on it.
     *
     * @param args The command arguments, containing exactly one date at index 0.
     * @throws CommandSyntaxException If the date is missing, malformed, or invalid.
     */
    @Override
    public void execute(String[] args) throws CommandSyntaxException {
        if (args.length == 0 || args[0].isBlank()) {
            throw new CommandSyntaxException("\tInvalid syntax! Please give me a date.\n"
                    + "\tUsage: list_deadline <dd/MM/yyyy>");
        }

        if (args.length > 1) {
            throw new CommandSyntaxException(
                    "\tInvalid syntax! The list_deadline command requires exactly one date.\n"
                            + "\tUsage: list_deadline <dd/MM/yyyy>");
        }

        LocalDate date;
        try {
            date = this.dateTimeParser.parseDate(args[0]);
        } catch (DateTimeParseException exception) {
            throw new CommandSyntaxException(
                    "\tInvalid date! Enter the date in dd/MM/yyyy format, for example 20/09/2026.");
        }

        TaskList filteredTasks = this.tasks.filterTaskOnDate(date);
        System.out.println("\tHere's your task list:\n");
        filteredTasks.printTasks();
    }
}
