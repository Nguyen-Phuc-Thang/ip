package magnus.command;

import magnus.task.TaskList;

public class UnmarkCommand implements Command {
    private TaskList tasks;
    
    public UnmarkCommand(TaskList tasks) {
        this.tasks = tasks;
    }

    @Override
    public void execute(String[] args) {
        int taskIndex = Integer.parseInt(args[0]);
        this.tasks.markTaskAsUndone(taskIndex);
    }
}
