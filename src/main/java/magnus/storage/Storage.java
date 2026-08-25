package magnus.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import magnus.exception.StorageException;
import magnus.task.Task;

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
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
