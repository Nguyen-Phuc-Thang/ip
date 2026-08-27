package magnus.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import magnus.exception.CommandSyntaxException;
import magnus.parser.DateTimeParser;
import magnus.task.TaskList;

/**
 * Displays event tasks enclosed by a specified date range.
 */
public class ListEventCommand implements Command {
    private final TaskList tasks;
    private final DateTimeParser dateTimeParser;

    /**
     * Creates a command that filters the specified task list by an inclusive date range.
     *
     * @param tasks The task list to filter and display.
     */
    public ListEventCommand(TaskList tasks) {
        this.tasks = tasks;
        this.dateTimeParser = new DateTimeParser();
    }

    /**
     * Parses the supplied date range and prints all events fully enclosed by it.
     *
     * @param args The command arguments containing the start and end dates.
     * @throws CommandSyntaxException If the dates are missing, malformed, invalid, or out of order.
     */
    @Override
    public void execute(String[] args) throws CommandSyntaxException {
        if (args.length == 0 || args[0].isBlank()) {
            throw new CommandSyntaxException("\tInvalid syntax! Please give me a start date and an end date.\n"
                    + "\tUsage: list_event <dd/MM/yyyy> <dd/MM/yyyy>");
        }

        if (args.length > 1) {
            throw createInvalidArgumentCountException();
        }

        LocalDate[] dates;
        try {
            dates = this.dateTimeParser.parseDateRange(args[0]);
        } catch (DateTimeParseException exception) {
            throw new CommandSyntaxException(
                    "\tInvalid date! Enter both dates in dd/MM/yyyy format, for example "
                            + "01/09/2026 30/09/2026.");
        } catch (IllegalArgumentException exception) {
            throw createInvalidArgumentCountException();
        }

        LocalDate startDate = dates[0];
        LocalDate endDate = dates[1];
        if (startDate.isAfter(endDate)) {
            throw new CommandSyntaxException(
                    "\tInvalid date range! The start date must be before or equal to the end date.");
        }

        TaskList filteredTasks = this.tasks.filterTaskWithinDateRange(startDate, endDate);
        System.out.println("\tHere's your task list:\n");
        filteredTasks.printTasks();
    }

    /**
     * Creates the syntax exception used when the command does not contain exactly two dates.
     *
     * @return An exception containing the expected command usage.
     */
    private CommandSyntaxException createInvalidArgumentCountException() {
        return new CommandSyntaxException(
                "\tInvalid syntax! The list_event command requires exactly two dates.\n"
                        + "\tUsage: list_event <dd/MM/yyyy> <dd/MM/yyyy>");
    }
}
