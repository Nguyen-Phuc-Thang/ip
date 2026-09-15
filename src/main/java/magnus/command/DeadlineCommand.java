package magnus.command;

import magnus.exception.CommandSyntaxException;
import magnus.exception.DuplicateTaskException;
import magnus.exception.MagnusException;
import magnus.task.DeadlineTask;
import magnus.task.TaskList;

/**
 * Adds deadline tasks to a task list.
 */
public class DeadlineCommand implements Command {
    private static final String USAGE_MESSAGE =
            "\tUsage: deadline <task description> /by <dd/MM/yyyy HHmm>";

    private final TaskList tasks;

    /**
     * Creates a command that adds deadline tasks to the specified task list.
     *
     * @param tasks The task list to which deadline tasks are added.
     */
    public DeadlineCommand(TaskList tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a deadline task using its description and deadline from the command arguments.
     *
     * @param args The command arguments, with the description at index 0 and deadline at index 1.
     * @return A message describing the deadline task that was added.
     * @throws CommandSyntaxException If either the task description or deadline is missing.
     */
    @Override
    public String execute(String... args) throws MagnusException {
        if (args.length == 0 || args[0].isBlank()) {
            throw new CommandSyntaxException("\tThat move is incomplete - please give me the task description.\n"
                    + USAGE_MESSAGE);
        }

        if (args.length == 1 || args[1].isBlank()) {
            throw new CommandSyntaxException("\tThe clock is missing - please give me the task deadline.\n"
                    + USAGE_MESSAGE);
        }

        if (args.length > 2) {
            throw new CommandSyntaxException("\tToo many clocks in that move - the deadline command requires "
                    + "exactly one /by field.\n"
                    + USAGE_MESSAGE);
        }

        String taskDescription = args[0];
        String taskDeadline = args[1];
        DeadlineTask newTask;
        try {
            newTask = new DeadlineTask(taskDescription, taskDeadline);
        } catch (IllegalArgumentException exception) {
            throw new CommandSyntaxException(
                    "\tThe clock rejects that deadline time - enter it in dd/MM/yyyy HHmm format, "
                            + "for example 02/09/2026 1500.");
        }
        if (tasks.containsTaskWithSameDetails(newTask)) {
            throw new DuplicateTaskException(
                    "\tThat piece is already on the board - an identical task already exists.");
        }
        tasks.addTask(newTask);

        return "\tClock set - I've added this Deadline task:\n\n\t" + newTask;
    }
}
