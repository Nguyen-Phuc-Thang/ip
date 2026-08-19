package magnus.command;

import magnus.task.Task;
import magnus.task.TaskList;

public class AddTaskCommand implements Command {
    TaskList tasks;

    public AddTaskCommand(TaskList tasks) {
        this.tasks = tasks;
    }

    @Override
    public void execute(String[] args) {
        String taskDescription = args[0];
        tasks.addTask(new Task(taskDescription));
    }
}
