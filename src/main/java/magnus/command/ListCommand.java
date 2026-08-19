package magnus.command;

import magnus.task.TaskList;

public class ListCommand implements Command{
    private TaskList tasks;

    public ListCommand(TaskList tasks) {
        this.tasks = tasks;
    }

    @Override
    public void execute(String[] args) {
        tasks.printTasks();
    }
}
