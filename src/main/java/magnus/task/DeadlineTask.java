package magnus.task;

/**
 * Represents a task that must be completed by a specified deadline.
 */
public class DeadlineTask extends Task {
    private String deadline;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description The description of the task.
     * @param deadline The deadline by which the task should be completed.
     */
    public DeadlineTask(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * Returns a string representation containing the deadline-task marker, completion status,
     * description, and deadline.
     *
     * @return The string representation of this deadline task.
     */
    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.deadline);
    }
}
