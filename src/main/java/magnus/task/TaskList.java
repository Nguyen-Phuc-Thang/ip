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
     * Returns the number of tasks in this task list.
     *
     * @return The number of stored tasks.
     */
    public int getLength() {
        return this.tasks.size();
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
     * Returns the task at the specified zero-based index.
     *
     * @param index The zero-based index of the task to return.
     * @return The task at the specified index.
     * @throws IndexOutOfBoundsException If the index is outside the task list.
     */
    public Task getTask(int index) {
        return this.tasks.get(index);
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
            System.out.println(String.format("\t%d. %s", i + 1, this.tasks.get(i)));
        }
    }
}
