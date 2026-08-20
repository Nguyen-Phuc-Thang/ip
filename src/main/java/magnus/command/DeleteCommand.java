package magnus.command;

import magnus.exception.CommandSyntaxException;
import magnus.exception.MagnusException;
import magnus.exception.TaskNotFoundException;
import magnus.task.Task;
import magnus.task.TaskList;

/**
 * Deletes tasks from a task list.
 */
public class DeleteCommand implements Command {
    
    private TaskList tasks;

    /**
     * Creates a command that deletes tasks from the specified task list.
     *
     * @param tasks The task list from which tasks are deleted.
     */
    public DeleteCommand(TaskList tasks) {
        this.tasks = tasks;
    }

    /**
     * Deletes the task identified by the one-based task number in the first command argument.
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
                    + "\tUsage: delete <task number>");
        }

        // Too many arguments
        if (args.length > 1) {
            throw new CommandSyntaxException("\tInvalid syntax! The delete command accepts only one argument.\n"
                    + "\tUsage: delete <task number>");
        }

        int taskNumber;

        try {
            taskNumber = Integer.parseInt(args[0]) - 1;
        } catch (NumberFormatException exception) {
            throw new CommandSyntaxException("\t'" + args[0] + "' is not a valid task number.\n"
                    + "\tUsage: delete <task number>");
        }

        // Task number out of range
        if (taskNumber < 0 || taskNumber >= this.tasks.getLength()) {
            throw new TaskNotFoundException("\tSorry, I can't find this task number");
        }
        
        Task removedTask = this.tasks.removeTask(taskNumber);
        System.out.println("\tBoooooom!!! I've made this task vanished:\n");
        System.out.println("\t" + removedTask);
    }
}
