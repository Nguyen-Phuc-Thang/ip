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
import magnus.task.Task;

/**
 * Loads and saves Magnus tasks in a UTF-8 data file.
 */
public class Storage {
    private final Path filePath;
    private final TaskDataParser taskDataParser;

    /**
     * Creates a storage service that uses the specified data file.
     *
     * @param filePath The path of the task data file.
     * @throws NullPointerException If {@code filePath} is {@code null}.
     */
    public Storage(Path filePath) {
        this.filePath = Objects.requireNonNull(filePath);
        this.taskDataParser = new TaskDataParser();
    }

    /**
     * Loads every non-blank task record from the data file.
     *
     * @return The loaded tasks, or an empty list if the data file does not exist.
     * @throws StorageException If the file cannot be read or contains a malformed task record.
     */
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

    /**
     * Saves all tasks by atomically replacing the existing data file when supported.
     *
     * @param tasks The tasks to save.
     * @throws StorageException If the tasks cannot be serialized or the data file cannot be written.
     */
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

    /**
     * Serializes and validates one task before it is written.
     *
     * @param task The task to serialize.
     * @return The validated serialized task record.
     * @throws NullPointerException If {@code task} is {@code null}.
     * @throws IllegalArgumentException If the serialized record cannot be parsed.
     */
    private String serializeTask(Task task) {
        String taskData = Objects.requireNonNull(task).toDataString();
        assert taskData != null && !taskData.isBlank()
                : "A task must serialize to a non-blank storage record";
        this.taskDataParser.parseTask(taskData);
        return taskData;
    }

    /**
     * Replaces the data file with a temporary file, using an atomic move when available.
     *
     * @param temporaryFile The temporary file containing the complete task data.
     * @throws IOException If the temporary file cannot replace the data file.
     */
    private void replaceDataFile(Path temporaryFile) throws IOException {
        try {
            Files.move(temporaryFile, this.filePath,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(temporaryFile, this.filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
