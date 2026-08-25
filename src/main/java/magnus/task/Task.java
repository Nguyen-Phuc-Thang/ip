package magnus.task;

/**
 * Represents a task in Magnus's task list.
 * A newly created task is incomplete by default.
 */
public class Task {
    private String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the specified description.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return The task's description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    public String toDataString() {
        return String.format("T,%d,%s", getStatusNumber(), encodeDataField(this.description));
    }

    protected int getStatusNumber() {
        return this.isDone ? 1 : 0;
    }

    protected String encodeDataField(String field) {
        if (field.contains("\n") || field.contains("\r")) {
            throw new IllegalArgumentException("Task fields cannot contain line breaks");
        }
        if (!field.contains(",") && !field.contains("\"")) {
            return field;
        }

        return "\"" + field.replace("\"", "\"\"") + "\"";
    }

    /**
     * Returns the icon representing this task's completion status.
     *
     * @return {@code "X"} if the task is complete; an empty string otherwise.
     */
    private String getStatusIcon() {
        return this.isDone ? "X" : " ";
    }

    /**
     * Returns a string representation containing the task's completion
     * indicator and description.
     *
     * @return The string representation of this task.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), this.description);
    }

}
