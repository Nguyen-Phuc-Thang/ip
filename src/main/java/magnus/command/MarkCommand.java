package magnus.command;

import magnus.exception.CommandSyntaxException;
import magnus.exception.MagnusException;
import magnus.exception.TaskNotFoundException;
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
     * Marks the task identified by the one-based task number in the first command argument.
     *
     * @param args The command arguments, with the one-based task number at index 0.
     * @throws CommandSyntaxException If the task number is missing, non-numeric, or accompanied
     *                                by extra arguments.
     * @throws TaskNotFoundException If the task number does not identify a task in the list.
     */
    @Override
    public void execute(String[] args) throws MagnusException {
        // Missing task number
        if (args.length == 0 || args[0].isBlank()) {
            throw new CommandSyntaxException("\tInvalid syntax! Please tell me the task number.\n"
                    + "\tUsage: mark <task number>");
        }

        // Too many arguments
        if (args.length > 1) {
            throw new CommandSyntaxException("\tInvalid syntax! The mark command accepts only one argument.\n"
                    + "\tUsage: mark <task number>");
        }

        int taskNumber;

        try {
            taskNumber = Integer.parseInt(args[0]) - 1;
        } catch (NumberFormatException exception) {
            throw new CommandSyntaxException("\t'" + args[0] + "' is not a valid task number.\n"
                    + "\tUsage: mark <task number>");
        }

        if (taskNumber < 0 || taskNumber >= this.tasks.getLength()) {
            throw new TaskNotFoundException("\tSorry, I can't find this task number");
        }

        this.tasks.markTaskAsDone(taskNumber);
        System.out.println("\tBrilliant!! I've marked this task as completed:\n");
        System.out.println("\t" + this.tasks.getTask(taskNumber));
    }
}
