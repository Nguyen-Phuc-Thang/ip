package magnus.command;

import magnus.exception.CommandSyntaxException;
import magnus.exception.MagnusException;
import magnus.task.EventTask;
import magnus.task.TaskList;

/**
 * Adds event tasks to a task list.
 */
public class EventCommand implements Command {
    TaskList tasks;

    /**
     * Creates a command that adds event tasks to the specified task list.
     *
     * @param tasks The task list to which event tasks are added.
     */
    public EventCommand(TaskList tasks) {
        this.tasks = tasks;        
    }

    /**
     * Adds an event task using its description, start time, and end time from the command arguments.
     *
     * @param args The command arguments, with the description at index 0, start time at index 1,
     *             and end time at index 2.
     * @throws CommandSyntaxException If the description, start time, or end time is missing.
     */
    @Override
    public void execute(String[] args) throws MagnusException {

        // Missing arguments
        if (args.length == 0 || args[0].isBlank()) {
            throw new CommandSyntaxException("\tInvalid syntax! Please give me the task description.\n"
                    + "\tUsage: event <task description> /from <dd/MM/yyyy HHmm> /to <dd/MM/yyyy HHmm>");
        }

        if (args.length < 3 || args[1].isBlank() || args[2].isBlank()) {
            throw new CommandSyntaxException("\tInvalid syntax! Please give me the task start and end time.\n"
                    + "\tUsage: event <task description> /from <dd/MM/yyyy HHmm> /to <dd/MM/yyyy HHmm>");
        }

        if (args.length > 3) {
            throw new CommandSyntaxException("\tInvalid syntax! The event command requires one /from and /to field.\n"
                    + "\tUsage: event <task description> /from <dd/MM/yyyy HHmm> /to <dd/MM/yyyy HHmm>");
        }

        String taskDescription = args[0];
        String taskStart = args[1];
        String taskEnd = args[2];
        EventTask newTask;
        try {
            newTask = new EventTask(taskDescription, taskStart, taskEnd);
        } catch (IllegalArgumentException exception) {
            throw new CommandSyntaxException(
                    "\tInvalid event time! Enter start and end times in dd/MM/yyyy HHmm format, "
                            + "for example 02/09/2026 1500.");
        }
        tasks.addTask(newTask);

        System.out.println("\tI've added this Event task:\n");
        System.out.println("\t" + newTask);
    }
}
