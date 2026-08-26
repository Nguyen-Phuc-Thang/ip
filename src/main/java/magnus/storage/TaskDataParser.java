package magnus.storage;

import java.util.ArrayList;
import java.util.List;

import magnus.task.DeadlineTask;
import magnus.task.EventTask;
import magnus.task.Task;
import magnus.task.ToDoTask;

public class TaskDataParser {
    public Task parseTask(String line) {
        List<String> taskData = parseDataFields(line);
        if (taskData.size() < 2) {
            throw new IllegalArgumentException("missing task type or status");
        }

        String taskType = taskData.get(0);
        boolean isDone = parseStatus(taskData.get(1));

        Task task = switch (taskType) {
            case "T" -> {
                requireFieldCount(taskData, 3, "to-do");
                yield new ToDoTask(requireNonBlank(taskData.get(2), "description"));
            }
            case "D" -> {
                requireFieldCount(taskData, 4, "deadline");
                yield new DeadlineTask(
                        requireNonBlank(taskData.get(2), "description"),
                        requireNonBlank(taskData.get(3), "deadline"));
            }
            case "E" -> {
                requireFieldCount(taskData, 4, "event");
                String[] eventTime = parseEventTime(
                        requireNonBlank(taskData.get(3), "event time"));
                yield new EventTask(
                        requireNonBlank(taskData.get(2), "description"),
                        eventTime[0], eventTime[1]);
            }
            default -> throw new IllegalArgumentException("unknown task type '" + taskType + "'");
        };

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

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

    private boolean parseStatus(String status) {
        if (status.equals("1")) {
            return true;
        }
        if (status.equals("0")) {
            return false;
        }
        throw new IllegalArgumentException("invalid task status '" + status + "'");
    }

    private void requireFieldCount(List<String> fields, int expectedCount, String taskType) {
        if (fields.size() != expectedCount) {
            throw new IllegalArgumentException(
                    taskType + " task should have " + expectedCount + " fields, but has " + fields.size());
        }
    }

    private String requireNonBlank(String field, String fieldName) {
        if (field.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return field;
    }

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
