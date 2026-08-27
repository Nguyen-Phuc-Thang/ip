package magnus.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import magnus.exception.CommandSyntaxException;
import magnus.task.DeadlineTask;
import magnus.task.TaskList;
import magnus.task.ToDoTask;

/**
 * Tests validation and output for {@link FindCommand}.
 */
public class FindCommandTest {
    @Test
    public void execute_matchingQuery_printsMatchingTasksInOriginalOrder() throws CommandSyntaxException {
        ToDoTask firstMatch = new ToDoTask("read book");
        firstMatch.markAsDone();
        DeadlineTask secondMatch = new DeadlineTask(
                "return book", LocalDateTime.of(2026, 6, 6, 18, 0));
        secondMatch.markAsDone();
        TaskList tasks = new TaskList(List.of(
                firstMatch, new ToDoTask("submit report"), secondMatch));
        FindCommand command = new FindCommand(tasks);
        String expectedOutput = String.join(System.lineSeparator(),
                "\tHere are the results that match \"book\":",
                "",
                "\t1. [T][X] read book",
                "\t2. [D][X] return book (by: Jun 06, 2026 18:00)") + System.lineSeparator();

        String output = captureOutput(() -> command.execute(new String[] { "book" }));

        assertEquals(expectedOutput, output);
    }

    @Test
    public void execute_noQuery_throwsCommandSyntaxException() {
        FindCommand command = new FindCommand(new TaskList());

        CommandSyntaxException exception = assertThrows(
                CommandSyntaxException.class, () -> command.execute(new String[0]));

        assertEquals("\tInvalid syntax! Please give me a search query.\n\tUsage: find <query>",
                exception.getMessage());
    }

    @Test
    public void execute_blankQuery_throwsCommandSyntaxException() {
        FindCommand command = new FindCommand(new TaskList());

        assertThrows(CommandSyntaxException.class, () -> command.execute(new String[] { "   " }));
    }

    @Test
    public void execute_multipleArgumentFields_throwsCommandSyntaxException() {
        FindCommand command = new FindCommand(new TaskList());

        assertThrows(CommandSyntaxException.class,
                () -> command.execute(new String[] { "book", "tomorrow" }));
    }

    /**
     * Captures output produced by a command action.
     *
     * @param action The action whose output should be captured.
     * @return The captured standard output.
     * @throws CommandSyntaxException If the action rejects its arguments.
     */
    private String captureOutput(CommandAction action) throws CommandSyntaxException {
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        try (PrintStream testOutput = new PrintStream(capturedOutput, true, StandardCharsets.UTF_8)) {
            System.setOut(testOutput);
            action.run();
        } finally {
            System.setOut(originalOutput);
        }

        return capturedOutput.toString(StandardCharsets.UTF_8);
    }

    /**
     * Represents a command action that may reject invalid syntax.
     */
    @FunctionalInterface
    private interface CommandAction {
        void run() throws CommandSyntaxException;
    }
}
