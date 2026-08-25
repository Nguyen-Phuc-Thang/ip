package magnus.command;

import magnus.exception.CommandSyntaxException;
import magnus.task.TaskList;

/**
 * Displays all tasks in a task list.
 */
public class ListCommand implements Command {
    private TaskList tasks;

    /**
     * Creates a command that displays the specified task list.
     *
     * @param tasks The task list to display.
     */
    public ListCommand(TaskList tasks) {
        this.tasks = tasks;
    }

    /**
     * Prints every task in the task list to standard output.
     *
     * @param args The command arguments.
     */
    @Override
    public void execute(String[] args) throws CommandSyntaxException {
        if (args.length > 0) {
            throw new CommandSyntaxException("\tInvalid syntax! The list command does not accept arguments.\n"
                    + "\tUsage: list");
        }
        System.out.println("\tHere's your task list:\n");
        tasks.printTasks();
    }
}
