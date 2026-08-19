package magnus.command;

import magnus.task.DeadlineTask;
import magnus.task.TaskList;

public class DeadlineCommand implements Command {
    TaskList tasks;

    public DeadlineCommand(TaskList tasks) {
        this.tasks = tasks;        
    }

    @Override
    public void execute(String[] args) {
        String taskDescription = args[0];
        String taskDeadline = args[1];
        DeadlineTask newTask = new DeadlineTask(taskDescription, taskDeadline);
        tasks.addTask(newTask);

        System.out.println("\tI've added this Deadline task:\n");
        System.out.println("\t" + newTask);
    }
}
