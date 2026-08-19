package magnus.command;

import magnus.task.TaskList;

/**
 * Marks a task in a task list as completed.
 */
public class MarkCommand implements Command {
    private TaskList tasks;

    /**
     * Creates a command that marks tasks in the specified task list.
     *
     * @param tasks The task list containing the tasks to mark.
     */
    public MarkCommand(TaskList tasks) {
        this.tasks = tasks;
    }

    /**
     * Marks the task at the zero-based index given by the first command argument.
     *
     * @param args The command arguments, with the task index at index 0.
     */
    @Override
    public void execute(String[] args) {
        int taskIndex = Integer.parseInt(args[0]) - 1;
        this.tasks.markTaskAsDone(taskIndex);
        System.out.println("\tBrilliant!! I've marked this task as completed:\n");
        System.out.println("\t" + this.tasks.getTask(taskIndex));
    }
}
