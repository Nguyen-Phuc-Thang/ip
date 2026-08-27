package magnus.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Stores and manages the tasks in Magnus's task list.
 */
public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public List<Task> getTasks() {
        return List.copyOf(this.tasks);
    }

    public TaskList filterTaskOnDate(LocalDate date) {
        List<Task> filteredTasks = this.tasks.stream()
                .filter(task -> task instanceof DeadlineTask deadlineTask
                        && deadlineTask.getDeadline().toLocalDate().equals(date))
                .toList();
        return new TaskList(filteredTasks);
    }

    public TaskList filterTaskWithinDateRange(LocalDate startDate, LocalDate endDate) {
        List<Task> filteredTasks = this.tasks.stream()
                .filter(task -> task instanceof EventTask eventTask
                        && !eventTask.getStart().toLocalDate().isBefore(startDate)
                        && !eventTask.getEnd().toLocalDate().isAfter(endDate))
                .toList();
        return new TaskList(filteredTasks);
    }

    /**
     * Returns tasks whose descriptions contain the specified query, ignoring case.
     * The matching tasks retain their original order.
     *
     * @param query The text to search for in task descriptions.
     * @return A new task list containing the matching tasks.
     */
    public TaskList filterTasksByDescription(String query) {
        String normalizedQuery = query.toLowerCase(Locale.ROOT);
        List<Task> filteredTasks = this.tasks.stream()
                .filter(task -> task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedQuery))
                .toList();
        return new TaskList(filteredTasks);
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
     * Removes and returns the task at the specified zero-based index.
     * Tasks after the removed task are shifted one position toward the start of the list.
     *
     * @param index The zero-based index of the task to remove.
     * @return The task that was removed.
     * @throws IndexOutOfBoundsException If the index is outside the task list.
     */
    public Task removeTask(int index) {
        return this.tasks.remove(index);
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
