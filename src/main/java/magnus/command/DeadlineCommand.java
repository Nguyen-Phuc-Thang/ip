package magnus.command;

import magnus.exception.CommandSyntaxException;
import magnus.exception.MagnusException;
import magnus.task.DeadlineTask;
import magnus.task.TaskList;

/**
 * Adds deadline tasks to a task list.
 */
public class DeadlineCommand implements Command {
    TaskList tasks;

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
     * @throws CommandSyntaxException If either the task description or deadline is missing.
     */
    @Override
    public void execute(String[] args) throws MagnusException {

        // Missing arguments
        if (args.length == 0) {
            throw new CommandSyntaxException("\tInvalid syntax! Please give me the task description.\n"
                    + "\tUsage: deadline <task description> /by <task deadline>");
        }

        if (args.length == 1) {
            throw new CommandSyntaxException("\tInvalid syntax! Please give me the task deadline.\n"
                    + "\tUsage: deadline <task description> /by <task deadline>");
        }

        String taskDescription = args[0];
        String taskDeadline = args[1];
        DeadlineTask newTask = new DeadlineTask(taskDescription, taskDeadline);
        tasks.addTask(newTask);

        System.out.println("\tI've added this Deadline task:\n");
        System.out.println("\t" + newTask);
    }
}
