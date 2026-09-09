package magnus.command;

import magnus.exception.CommandSyntaxException;
import magnus.exception.MagnusException;
import magnus.task.TaskList;
import magnus.task.ToDoTask;

/**
 * Adds to-do tasks to a task list.
 */
public class ToDoCommand implements Command {
    private static final String USAGE_MESSAGE = "\tUsage: todo <task description>";

    private final TaskList tasks;

    /**
     * Creates a command that adds to-do tasks to the specified task list.
     *
     * @param tasks The task list to which to-do tasks are added.
     */
    public ToDoCommand(TaskList tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a to-do task whose description is the first command argument.
     *
     * @param args The command arguments, with the task description at index 0.
     * @return A message describing the task that was added.
     * @throws CommandSyntaxException If the task description is missing or blank.
     */
    @Override
    public String execute(String... args) throws MagnusException {
        if (args.length == 0 || args[0].isBlank()) {
            throw new CommandSyntaxException("\tInvalid syntax! Please give me the task description.\n"
                    + USAGE_MESSAGE);
        }

        if (args.length > 1) {
            throw new CommandSyntaxException("\tInvalid syntax! The todo command accepts one description.\n"
                    + USAGE_MESSAGE);
        }

        String taskDescription = args[0];
        ToDoTask newTask = new ToDoTask(taskDescription);
        tasks.addTask(newTask);

        return "\tI've added this To-Do task:\n\n\t" + newTask;
    }
}
