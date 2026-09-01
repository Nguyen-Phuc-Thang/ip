package magnus.command;

import magnus.exception.CommandSyntaxException;
import magnus.task.TaskList;

/**
 * Displays tasks whose descriptions contain a specified query.
 */
public class FindCommand implements Command {
    private final TaskList tasks;

    /**
     * Creates a command that searches the specified task list.
     *
     * @param tasks The task list to search.
     */
    public FindCommand(TaskList tasks) {
        this.tasks = tasks;
    }

    /**
     * Formats tasks whose descriptions contain the supplied query, ignoring case.
     *
     * @param args The command arguments, containing exactly one query at index 0.
     * @return A message containing tasks whose descriptions match the query.
     * @throws CommandSyntaxException If the query is missing, blank, or split into multiple fields.
     */
    @Override
    public String execute(String... args) throws CommandSyntaxException {
        if (args.length == 0 || args[0].isBlank()) {
            throw new CommandSyntaxException("\tInvalid syntax! Please give me a search query.\n"
                    + "\tUsage: find <query>");
        }

        if (args.length > 1) {
            throw new CommandSyntaxException("\tInvalid syntax! The find command accepts one query.\n"
                    + "\tUsage: find <query>");
        }

        String query = args[0].strip();
        TaskList matchingTasks = this.tasks.filterTasksByDescription(query);
        return String.format("\tHere are the results that match \"%s\":%n%n%s",
                query, matchingTasks.formatTasks());
    }
}
