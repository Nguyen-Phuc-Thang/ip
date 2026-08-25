package magnus.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import magnus.exception.StorageException;
import magnus.task.DeadlineTask;
import magnus.task.EventTask;
import magnus.task.Task;
import magnus.task.ToDoTask;

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    public List<Task> loadTasks() throws StorageException {
        List<Task> tasks = new ArrayList<>();

        if (!Files.exists(this.filePath)) {
            return tasks;
        }

        try {
            for (String line : Files.readAllLines(this.filePath, StandardCharsets.UTF_8)) {
                if (!line.isBlank()) {
                    tasks.add(parseTask(line));
                }
            }
            return tasks;
        } catch (IOException | IllegalArgumentException exception) {
            throw new StorageException("\tSorry, I could not load your saved tasks.", exception);
        }
    }

    private Task parseTask(String line) {
        String[] taskData = line.split(",", 4);
        if (taskData.length < 3) {
            throw new IllegalArgumentException("Invalid task data: " + line);
        }

        Task task = switch (taskData[0]) {
            case "T" -> new ToDoTask(taskData[2]);
            case "D" -> {
                if (taskData.length < 4) {
                    throw new IllegalArgumentException("Invalid deadline data: " + line);
                }
                yield new DeadlineTask(taskData[2], taskData[3]);
            }
            case "E" -> {
                if (taskData.length < 4) {
                    throw new IllegalArgumentException("Invalid event data: " + line);
                }
                String[] eventTime = taskData[3].split("-", 2);
                if (eventTime.length < 2) {
                    throw new IllegalArgumentException("Invalid event time: " + line);
                }
                yield new EventTask(taskData[2], eventTime[0], eventTime[1]);
            }
            default -> throw new IllegalArgumentException("Unknown task type: " + taskData[0]);
        };

        if (taskData[1].equals("1")) {
            task.markAsDone();
        } else if (!taskData[1].equals("0")) {
            throw new IllegalArgumentException("Invalid task status: " + taskData[1]);
        }

        return task;
    }

    public void saveTasks(List<Task> tasks) throws StorageException {
        String fileContents = tasks.stream()
                .map(Task::toDataString)
                .collect(Collectors.joining(System.lineSeparator()));

        try {
            Path parentDirectory = this.filePath.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }
            Files.writeString(this.filePath, fileContents, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new StorageException("\tSorry, I could not save your tasks.", exception);
        }
    }
}
