package magnus.command;

import magnus.task.EventTask;
import magnus.task.TaskList;

public class EventCommand implements Command {
    TaskList tasks;

    public EventCommand(TaskList tasks) {
        this.tasks = tasks;        
    }

    @Override
    public void execute(String[] args) {
        String taskDescription = args[0];
        String taskStart = args[1];
        String taskEnd = args[2];
        EventTask newTask = new EventTask(taskDescription, taskStart, taskEnd);
        tasks.addTask(newTask);

        System.out.println("\tI've added this Event task:\n");
        System.out.println("\t" + newTask);
    }
}
