package magnus.command;

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
     * @param args The command arguments, which are ignored.
     */
    @Override
    public void execute(String[] args) {
        System.out.println("\tHere's your task list:\n");
        tasks.printTasks();
    }
}
