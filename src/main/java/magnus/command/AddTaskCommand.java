package magnus.command;

import magnus.task.Task;
import magnus.task.TaskList;

/**
 * Adds a new task to a task list.
 */
public class AddTaskCommand implements Command {
    TaskList tasks;

    /**
     * Creates a command that adds tasks to the specified task list.
     *
     * @param tasks The task list to which tasks are added.
     */
    public AddTaskCommand(TaskList tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task whose description is the first command argument.
     *
     * @param args The command arguments, with the task description at index 0.
     * @throws ArrayIndexOutOfBoundsException If no task description is supplied.
     */
    @Override
    public void execute(String[] args) {
        String taskDescription = args[0];
        tasks.addTask(new Task(taskDescription));
        System.out.println("\tadded: " + taskDescription);
    }
}
