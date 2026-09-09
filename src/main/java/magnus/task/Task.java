package magnus.task;

/**
 * Represents a task in Magnus's task list.
 * A newly created task is incomplete by default.
 */
public class Task {
    private static final String FIELD_DELIMITER = ",";
    private static final String QUOTATION_MARK = "\"";
    private static final String ESCAPED_QUOTATION_MARK = "\"\"";
    private static final int INCOMPLETE_STATUS = 0;
    private static final int COMPLETE_STATUS = 1;

    private final String description;
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

    /**
     * Serializes this task into the format used by persistent storage.
     *
     * @return The serialized task data.
     */
    public String toDataString() {
        return String.format("%s,%d,%s",
                getTaskType().getStorageCode(),
                getCompletionStatusCode(), encodeDataField(this.description));
    }

    /**
     * Returns the specific type of this task.
     *
     * @return This task's type.
     */
    protected TaskType getTaskType() {
        return TaskType.TODO;
    }

    /**
     * Returns the numeric completion status used by persistent storage.
     *
     * @return {@code 1} if the task is complete; {@code 0} otherwise.
     */
    protected int getCompletionStatusCode() {
        return this.isDone ? COMPLETE_STATUS : INCOMPLETE_STATUS;
    }

    /**
     * Escapes a task field for safe storage in the comma-separated data format.
     *
     * @param field The field to encode.
     * @return The encoded field, quoted when it contains commas or quotation marks.
     * @throws IllegalArgumentException If the field contains a line break.
     */
    protected String encodeDataField(String field) {
        if (field.contains("\n") || field.contains("\r")) {
            throw new IllegalArgumentException("Task fields cannot contain line breaks");
        }
        boolean requiresQuotationMarks = field.contains(FIELD_DELIMITER)
                || field.contains(QUOTATION_MARK);
        if (!requiresQuotationMarks) {
            return field;
        }

        String escapedField = field.replace(QUOTATION_MARK, ESCAPED_QUOTATION_MARK);
        return QUOTATION_MARK + escapedField + QUOTATION_MARK;
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
