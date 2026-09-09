package magnus.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import magnus.exception.CommandSyntaxException;
import magnus.exception.MagnusException;
import magnus.exception.TaskNotFoundException;

/**
 * Tests task-number validation shared by task-specific commands.
 */
public class TaskIndexParserTest {
    @Test
    public void parseTaskIndex_validTaskNumber_returnsZeroBasedIndex() throws MagnusException {
        int taskIndex = TaskIndexParser.parseTaskIndex(new String[] { "2" }, "mark", 3);

        assertEquals(1, taskIndex);
    }

    @Test
    public void parseTaskIndex_missingTaskNumber_throwsCommandSyntaxException() {
        CommandSyntaxException exception = assertThrows(
                CommandSyntaxException.class, () -> TaskIndexParser.parseTaskIndex(new String[0], "mark", 3));

        assertEquals("\tInvalid syntax! Please tell me the task number.\n"
                + "\tUsage: mark <task number>", exception.getMessage());
    }

    @Test
    public void parseTaskIndex_extraArgument_throwsCommandSyntaxException() {
        CommandSyntaxException exception = assertThrows(
                CommandSyntaxException.class, () -> TaskIndexParser.parseTaskIndex(
                        new String[] { "1", "2" }, "unmark", 3));

        assertEquals("\tInvalid syntax! The unmark command accepts only one argument.\n"
                + "\tUsage: unmark <task number>", exception.getMessage());
    }

    @Test
    public void parseTaskIndex_nonNumericTaskNumber_throwsCommandSyntaxException() {
        CommandSyntaxException exception = assertThrows(
                CommandSyntaxException.class, () -> TaskIndexParser.parseTaskIndex(
                        new String[] { "first" }, "delete", 3));

        assertEquals("\t'first' is not a valid task number.\n"
                + "\tUsage: delete <task number>", exception.getMessage());
    }

    @Test
    public void parseTaskIndex_taskNumberBelowRange_throwsTaskNotFoundException() {
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class, () -> TaskIndexParser.parseTaskIndex(
                        new String[] { "0" }, "mark", 3));

        assertEquals("\tSorry, I can't find this task number", exception.getMessage());
    }

    @Test
    public void parseTaskIndex_taskNumberAboveRange_throwsTaskNotFoundException() {
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class, () -> TaskIndexParser.parseTaskIndex(
                        new String[] { "4" }, "mark", 3));

        assertEquals("\tSorry, I can't find this task number", exception.getMessage());
    }
}
