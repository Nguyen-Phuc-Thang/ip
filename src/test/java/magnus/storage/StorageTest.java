package magnus.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import magnus.exception.StorageException;
import magnus.task.DeadlineTask;
import magnus.task.EventTask;
import magnus.task.Task;
import magnus.task.ToDoTask;

/**
 * Tests loading and saving tasks through persistent storage.
 */
public class StorageTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void loadTasks_missingDataFile_returnsEmptyList() throws StorageException {
        Storage storage = new Storage(this.temporaryDirectory.resolve("missing.txt"));

        List<Task> tasks = storage.loadTasks();

        assertTrue(tasks.isEmpty());
    }

    @Test
    public void saveTasks_multipleTaskTypes_loadsEquivalentTasks() throws StorageException {
        Path dataFile = this.temporaryDirectory.resolve("data").resolve("magnus.txt");
        Storage storage = new Storage(dataFile);
        ToDoTask todo = new ToDoTask("buy milk, eggs");
        DeadlineTask deadline = new DeadlineTask(
                "submit report", LocalDateTime.of(2026, 9, 2, 15, 0));
        EventTask event = new EventTask(
                "meeting", LocalDateTime.of(2026, 9, 2, 16, 0),
                LocalDateTime.of(2026, 9, 2, 17, 0));
        deadline.markAsDone();
        List<Task> tasks = List.of(todo, deadline, event);

        storage.saveTasks(tasks);
        List<Task> loadedTasks = storage.loadTasks();

        assertEquals(tasks.stream().map(Task::toDataString).toList(),
                loadedTasks.stream().map(Task::toDataString).toList());
    }

    @Test
    public void loadTasks_malformedRecord_reportsLineNumber() throws IOException {
        Path dataFile = this.temporaryDirectory.resolve("magnus.txt");
        Files.writeString(dataFile, "T,0,read book\ninvalid", StandardCharsets.UTF_8);
        Storage storage = new Storage(dataFile);

        StorageException exception = assertThrows(
                StorageException.class, storage::loadTasks);

        assertEquals("\tThe data file is corrupted at line 2: missing task type or status",
                exception.getMessage());
    }
}
