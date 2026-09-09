package magnus.storage;

import java.util.ArrayList;
import java.util.List;

import magnus.task.DeadlineTask;
import magnus.task.EventTask;
import magnus.task.Task;
import magnus.task.ToDoTask;

/**
 * Converts serialized task records into task objects.
 */
public class TaskDataParser {
    /**
     * Creates a parser for serialized task records.
     */
    public TaskDataParser() {
    }

    /**
     * Parses one serialized task record.
     *
     * @param line The serialized task record to parse.
     * @return The task represented by the record.
     * @throws IllegalArgumentException If the record is malformed or contains invalid task data.
     */
    public Task parseTask(String line) {
        List<String> taskDataFields = parseDataFields(line);
        if (taskDataFields.size() < 2) {
            throw new IllegalArgumentException("missing task type or status");
        }

        String taskType = taskDataFields.get(0);
        boolean isDone = isDoneStatus(taskDataFields.get(1));

        Task task = switch (taskType) {
            case "T" -> {
                requireFieldCount(taskDataFields, 3, "to-do");
                yield new ToDoTask(requireNonBlank(taskDataFields.get(2), "description"));
            }
            case "D" -> {
                requireFieldCount(taskDataFields, 4, "deadline");
                yield new DeadlineTask(
                        requireNonBlank(taskDataFields.get(2), "description"),
                        requireNonBlank(taskDataFields.get(3), "deadline"));
            }
            case "E" -> {
                requireFieldCount(taskDataFields, 4, "event");
                String[] eventTimes = parseEventTime(
                        requireNonBlank(taskDataFields.get(3), "event time"));
                assert eventTimes.length == 2
                        : "An event-time field must produce a start time and an end time";
                yield new EventTask(
                        requireNonBlank(taskDataFields.get(2), "description"),
                        eventTimes[0], eventTimes[1]);
            }
            default -> throw new IllegalArgumentException("unknown task type '" + taskType + "'");
        };

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Splits a comma-separated task record while decoding quoted fields and escaped quotes.
     *
     * @param line The serialized task record.
     * @return The decoded fields in their original order.
     * @throws IllegalArgumentException If quotation marks are malformed.
     */
    private List<String> parseDataFields(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean isQuoted = false;
        boolean hasClosedQuote = false;

        for (int i = 0; i < line.length(); i++) {
            char currentCharacter = line.charAt(i);

            if (isQuoted) {
                if (currentCharacter == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        currentField.append('"');
                        i++;
                    } else {
                        isQuoted = false;
                        hasClosedQuote = true;
                    }
                } else {
                    currentField.append(currentCharacter);
                }
            } else if (hasClosedQuote) {
                if (currentCharacter != ',') {
                    throw new IllegalArgumentException("unexpected text after a quoted field");
                }
                fields.add(currentField.toString());
                currentField.setLength(0);
                hasClosedQuote = false;
            } else if (currentCharacter == ',') {
                fields.add(currentField.toString());
                currentField.setLength(0);
            } else if (currentCharacter == '"') {
                if (!currentField.isEmpty()) {
                    throw new IllegalArgumentException("unexpected quote in an unquoted field");
                }
                isQuoted = true;
            } else {
                currentField.append(currentCharacter);
            }
        }

        if (isQuoted) {
            throw new IllegalArgumentException("unclosed quoted field");
        }

        fields.add(currentField.toString());
        return fields;
    }

    /**
     * Returns whether a stored numeric completion status represents a completed task.
     *
     * @param status The stored status, which must be {@code "0"} or {@code "1"}.
     * @return {@code true} for a completed task; {@code false} for an incomplete task.
     * @throws IllegalArgumentException If the status is not supported.
     */
    private boolean isDoneStatus(String status) {
        if (status.equals("1")) {
            return true;
        }
        if (status.equals("0")) {
            return false;
        }
        throw new IllegalArgumentException("invalid task status '" + status + "'");
    }

    /**
     * Verifies that a serialized record contains the expected number of fields.
     *
     * @param fields The parsed fields to validate.
     * @param expectedCount The required number of fields.
     * @param taskType The task type used in an error message.
     * @throws IllegalArgumentException If the number of fields is incorrect.
     */
    private void requireFieldCount(List<String> fields, int expectedCount, String taskType) {
        if (fields.size() != expectedCount) {
            throw new IllegalArgumentException(
                    taskType + " task should have " + expectedCount + " fields, but has " + fields.size());
        }
    }

    /**
     * Verifies that a required serialized field contains non-whitespace text.
     *
     * @param field The field to validate.
     * @param fieldName The field name used in an error message.
     * @return The validated field.
     * @throws IllegalArgumentException If the field is blank.
     */
    private String requireNonBlank(String field, String fieldName) {
        if (field.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return field;
    }

    /**
     * Separates a stored event-time field into its start and end values.
     * Backslashes escape delimiter characters within either value.
     *
     * @param eventTimeData The serialized event-time field.
     * @return A two-element array containing the start and end values.
     * @throws IllegalArgumentException If the field is malformed or either value is blank.
     */
    private String[] parseEventTime(String eventTimeData) {
        StringBuilder currentPart = new StringBuilder();
        String start = null;
        boolean isEscaped = false;

        for (int i = 0; i < eventTimeData.length(); i++) {
            char currentCharacter = eventTimeData.charAt(i);
            if (isEscaped) {
                currentPart.append(currentCharacter);
                isEscaped = false;
            } else if (currentCharacter == '\\') {
                isEscaped = true;
            } else if (currentCharacter == '-' && start == null) {
                start = currentPart.toString();
                currentPart.setLength(0);
            } else {
                currentPart.append(currentCharacter);
            }
        }

        if (isEscaped) {
            throw new IllegalArgumentException("event time ends with an incomplete escape sequence");
        }

        String end = currentPart.toString();
        if (start == null || start.isBlank() || end.isBlank()) {
            throw new IllegalArgumentException("event time must contain a non-blank start and end");
        }
        return new String[] {start, end};
    }
}
