package magnus.task;

/**
 * Represents a task that occurs between specified start and end times.
 */
public class EventTask extends Task {
    private String start;
    private String end;

    /**
     * Creates an incomplete event task.
     *
     * @param description The description of the event.
     * @param start The event's start time.
     * @param end The event's end time.
     */
    public EventTask(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns a string representation containing the event-task marker, completion status,
     * description, start time, and end time.
     *
     * @return The string representation of this event task.
     */
    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), this.start, this.end);
    }
}
