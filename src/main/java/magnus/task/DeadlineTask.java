package magnus.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Objects;

/**
 * Represents a task that must be completed by a specified deadline.
 */
public class DeadlineTask extends Task {
    private static final DateTimeFormatter DEADLINE_FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/uuuu HHmm")
            .withResolverStyle(ResolverStyle.STRICT);

    private final LocalDateTime deadline;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description The description of the task.
     * @param deadline The deadline by which the task should be completed.
     */
    public DeadlineTask(String description, String deadline) {
        super(description);
        this.deadline = parseDeadline(deadline);
    }

    /**
     * Creates an incomplete deadline task with an already-parsed deadline.
     *
     * @param description The description of the task.
     * @param deadline The deadline by which the task should be completed.
     */
    public DeadlineTask(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = Objects.requireNonNull(deadline);
    }

    /**
     * Returns the deadline as a date-time value.
     *
     * @return The task's deadline.
     */
    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    /**
     * Parses a deadline using the required {@code dd/MM/yyyy HHmm} input format.
     * Strict resolution rejects impossible dates and times, such as 31 February or 2400.
     *
     * @param deadline The deadline text to parse.
     * @return The parsed deadline.
     * @throws IllegalArgumentException If the deadline is null, malformed, or invalid.
     */
    private static LocalDateTime parseDeadline(String deadline) {
        if (deadline == null) {
            throw new IllegalArgumentException("deadline cannot be null");
        }

        try {
            return LocalDateTime.parse(deadline, DEADLINE_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    "deadline must use format dd/MM/yyyy HHmm and contain a valid date and time",
                    exception);
        }
    }

    /**
     * Formats the deadline in the same canonical form accepted from user input and storage.
     *
     * @return The formatted deadline.
     */
    private String getFormattedDeadline() {
        return this.deadline.format(DEADLINE_FORMATTER);
    }

    @Override
    public String toDataString() {
        return String.format("D,%d,%s,%s",
                getStatusNumber(), encodeDataField(getDescription()), encodeDataField(getFormattedDeadline()));
    }

    /**
     * Returns a string representation containing the deadline-task marker, completion status,
     * description, and deadline.
     *
     * @return The string representation of this deadline task.
     */
    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), getFormattedDeadline());
    }
}
