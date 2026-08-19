package magnus.command;

import magnus.task.TaskList;

public class MarkCommand implements Command{
    private TaskList tasks;
    
    public MarkCommand(TaskList tasks) {
        this.tasks = tasks;
    }

    @Override
    public void execute(String[] args) {
        int taskIndex = Integer.parseInt(args[0]);
        this.tasks.markTaskAsDone(taskIndex);
    }
}
