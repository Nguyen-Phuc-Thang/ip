package magnus.storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import magnus.exception.StorageException;
import magnus.parser.TaskDataParser;
import magnus.task.Task;

public class Storage {
    private final Path filePath;
    private final TaskDataParser taskDataParser;

    public Storage(Path filePath) {
        this.filePath = Objects.requireNonNull(filePath);
        this.taskDataParser = new TaskDataParser();
    }

    public List<Task> loadTasks() throws StorageException {
        List<Task> tasks = new ArrayList<>();

        if (Files.notExists(this.filePath)) {
            return tasks;
        }

        try (BufferedReader reader = Files.newBufferedReader(this.filePath, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }

                try {
                    tasks.add(this.taskDataParser.parseTask(line));
                } catch (IllegalArgumentException exception) {
                    throw new StorageException(
                            "\tThe data file is corrupted at line " + lineNumber
                                    + ": " + exception.getMessage(),
                            exception);
                }
            }
            return tasks;
        } catch (IOException exception) {
            throw new StorageException(
                    "\tSorry, I could not read the data file: " + this.filePath, exception);
        }
    }

    public void saveTasks(List<Task> tasks) throws StorageException {
        String fileContents;
        try {
            fileContents = tasks.stream()
                    .map(this::serializeTask)
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (RuntimeException exception) {
            throw new StorageException("\tSorry, I could not prepare the tasks for saving.", exception);
        }

        Path temporaryFile = null;
        try {
            Path parentDirectory = this.filePath.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            Path temporaryDirectory = parentDirectory == null ? Path.of(".") : parentDirectory;
            temporaryFile = Files.createTempFile(temporaryDirectory, "magnus-", ".tmp");
            Files.writeString(temporaryFile, fileContents, StandardCharsets.UTF_8);
            replaceDataFile(temporaryFile);
        } catch (IOException exception) {
            throw new StorageException(
                    "\tSorry, I could not save your tasks to: " + this.filePath, exception);
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException ignored) {
                    // The original save result is more important than temporary-file cleanup.
                }
            }
        }
    }

    private String serializeTask(Task task) {
        String taskData = Objects.requireNonNull(task).toDataString();
        this.taskDataParser.parseTask(taskData);
        return taskData;
    }

    private void replaceDataFile(Path temporaryFile) throws IOException {
        try {
            Files.move(temporaryFile, this.filePath,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(temporaryFile, this.filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
