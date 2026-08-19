package magnus.command;

import magnus.task.TaskList;
import magnus.task.ToDoTask;

public class ToDoCommand implements Command {
    TaskList tasks;

    public ToDoCommand(TaskList tasks) {
        this.tasks = tasks;        
    }

    @Override
    public void execute(String[] args) {
        String taskDescription = args[0];
        ToDoTask newTask = new ToDoTask(taskDescription);
        tasks.addTask(newTask);

        System.out.println("\tI've added this To-Do task:\n");
        System.out.println("\t" + newTask);
    }
}
