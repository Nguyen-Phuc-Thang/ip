package magnus.command;

import magnus.task.TaskList;

/**
 * Marks a task in a task list as incomplete.
 */
public class UnmarkCommand implements Command {
    private TaskList tasks;

    /**
     * Creates a command that unmarks tasks in the specified task list.
     *
     * @param tasks The task list containing the tasks to unmark.
     */
    public UnmarkCommand(TaskList tasks) {
        this.tasks = tasks;
    }

    /**
     * Marks the task at the zero-based index given by the first command argument
     * as incomplete.
     *
     * @param args The command arguments, with the task index at index 0.
     * @throws ArrayIndexOutOfBoundsException If no task index is supplied.
     * @throws NumberFormatException If the supplied task index is not an integer.
     * @throws IndexOutOfBoundsException If the task index is outside the task list.
     */
    @Override
    public void execute(String[] args) {
        int taskIndex = Integer.parseInt(args[0]);
        this.tasks.markTaskAsUndone(taskIndex);
    }
}
