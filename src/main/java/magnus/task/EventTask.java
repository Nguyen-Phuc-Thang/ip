package magnus.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a task that occurs between specified start and end times.
 */
public class EventTask extends Task {
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/uuuu HHmm")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter
            .ofPattern("MMM dd, uuuu HH:mm", Locale.ENGLISH);

    private final LocalDateTime start;
    private final LocalDateTime end;

    /**
     * Creates an incomplete event task.
     *
     * @param description The description of the event.
     * @param start The event's start time.
     * @param end The event's end time.
     */
    public EventTask(String description, String start, String end) {
        super(description);
        this.start = parseEventTime(start, "start time");
        this.end = parseEventTime(end, "end time");
    }

    /**
     * Creates an incomplete event task with already-parsed start and end times.
     *
     * @param description The description of the event.
     * @param start The event's start time.
     * @param end The event's end time.
     */
    public EventTask(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = Objects.requireNonNull(start);
        this.end = Objects.requireNonNull(end);
    }

    /**
     * Returns the event's start time as a date-time value.
     *
     * @return The event's start time.
     */
    public LocalDateTime getStart() {
        return this.start;
    }

    /**
     * Returns the event's end time as a date-time value.
     *
     * @return The event's end time.
     */
    public LocalDateTime getEnd() {
        return this.end;
    }

    /**
     * Parses an event time using the required {@code dd/MM/yyyy HHmm} input format.
     * Strict resolution rejects impossible dates and times, such as 31 February or 2400.
     *
     * @param eventTime The event time text to parse.
     * @param fieldName The name used to identify this event time in an error message.
     * @return The parsed event time.
     * @throws IllegalArgumentException If the event time is null, malformed, or invalid.
     */
    private static LocalDateTime parseEventTime(String eventTime, String fieldName) {
        if (eventTime == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }

        try {
            return LocalDateTime.parse(eventTime, STORAGE_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    fieldName + " must use format dd/MM/yyyy HHmm and contain a valid date and time",
                    exception);
        }
    }

    /**
     * Formats an event time in the canonical form accepted from user input and storage.
     *
     * @param eventTime The event time to format.
     * @return The formatted event time.
     */
    private String formatForStorage(LocalDateTime eventTime) {
        return eventTime.format(STORAGE_FORMATTER);
    }

    /**
     * Formats an event time for display with an English abbreviated month.
     *
     * @param eventTime The event time to format.
     * @return The display-formatted event time.
     */
    private String formatForDisplay(LocalDateTime eventTime) {
        return eventTime.format(DISPLAY_FORMATTER);
    }

    @Override
    public String toDataString() {
        String eventTime = formatForStorage(this.start) + "-" + formatForStorage(this.end);
        return String.format("E,%d,%s,%s",
                getStatusNumber(), encodeDataField(getDescription()), encodeDataField(eventTime));
    }

    /**
     * Returns a string representation containing the event-task marker, completion status,
     * description, start time, and end time.
     *
     * @return The string representation of this event task.
     */
    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(),
                formatForDisplay(this.start), formatForDisplay(this.end));
    }
}
