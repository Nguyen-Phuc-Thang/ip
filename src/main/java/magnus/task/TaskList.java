package magnus.task;

import java.util.ArrayList;
import java.util.List;

import magnus.exception.StorageException;
import magnus.storage.Storage;

/**
 * Stores and manages the tasks in Magnus's task list.
 */
public class TaskList {
    private static final String DEFAULT_DATA_FILE_PATH = "./data/magnus.txt";

    private final List<Task> tasks;
    private final Storage storage;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        this.storage = new Storage(DEFAULT_DATA_FILE_PATH);
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
    public void addTask(Task task) throws StorageException {
        this.tasks.add(task);
        saveTasks();
    }

    /**
     * Removes and returns the task at the specified zero-based index.
     * Tasks after the removed task are shifted one position toward the start of the list.
     *
     * @param index The zero-based index of the task to remove.
     * @return The task that was removed.
     * @throws IndexOutOfBoundsException If the index is outside the task list.
     */
    public Task removeTask(int index) throws StorageException {
        Task removedTask = this.tasks.remove(index);
        saveTasks();
        return removedTask;
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
    public void markTaskAsDone(int index) throws StorageException {
        this.tasks.get(index).markAsDone();
        saveTasks();
    }

    /**
     * Marks the task at the specified zero-based index as incomplete.
     *
     * @param index The zero-based index of the task to mark.
     * @throws IndexOutOfBoundsException If the index is outside the task list.
     */
    public void markTaskAsUndone(int index) throws StorageException {
        this.tasks.get(index).markAsUndone();
        saveTasks();
    }

    /**
     * Prints all tasks to standard output as a one-based numbered list.
     */
    public void printTasks() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("\t%d. %s", i + 1, this.tasks.get(i)));
        }
    }

    private void saveTasks() throws StorageException {
        this.storage.saveTasks(this.tasks);
    }
}
