package magnus.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages the tasks in Magnus's task list.
 */
public class TaskList {
    private List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the end of this task list.
     *
     * @param task The task to add.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Marks the task at the specified zero-based index as completed.
     *
     * @param index The zero-based index of the task to mark.
     * @throws IndexOutOfBoundsException If the index is outside the task list.
     */
    public void markTaskAsDone(int index) {
        this.tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at the specified zero-based index as incomplete.
     *
     * @param index The zero-based index of the task to mark.
     * @throws IndexOutOfBoundsException If the index is outside the task list.
     */
    public void markTaskAsUndone(int index) {
        this.tasks.get(index).markAsUndone();
    }

    /**
     * Prints all tasks to standard output as a one-based numbered list.
     */
    public void printTasks() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, this.tasks.get(i)));
        }
    }
}
