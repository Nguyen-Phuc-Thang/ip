package magnus.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests the state changes, display format, and storage format of {@link Task}.
 */
public class TaskTest {

    @Test
    public void constructor_validDescription_createsIncompleteTask() {
        Task task = new Task("read book");

        assertEquals("read book", task.getDescription());
        assertEquals("[ ] read book", task.toString());
    }

    @Test
    public void markAsDone_incompleteTask_taskIsCompleted() {
        Task task = new Task("read book");

        task.markAsDone();

        assertTrue(task.isDone());
        assertEquals("[X] read book", task.toString());
        assertEquals("T,1,read book", task.toDataString());
    }

    @Test
    public void markAsUndone_completedTask_taskIsIncomplete() {
        Task task = new Task("read book");
        task.markAsDone();

        task.markAsUndone();

        assertFalse(task.isDone());
        assertEquals("[ ] read book", task.toString());
        assertEquals("T,0,read book", task.toDataString());
    }

    @Test
    public void toDataString_simpleDescription_returnsUnquotedData() {
        Task task = new Task("read book");

        assertEquals("T,0,read book", task.toDataString());
    }

    @Test
    public void toDataString_descriptionWithComma_quotesDescription() {
        Task task = new Task("buy milk, eggs");

        assertEquals("T,0,\"buy milk, eggs\"", task.toDataString());
    }

    @Test
    public void toDataString_descriptionWithQuotes_escapesAndQuotesDescription() {
        Task task = new Task("read \"Dune\"");

        assertEquals("T,0,\"read \"\"Dune\"\"\"", task.toDataString());
    }

    @Test
    public void toDataString_descriptionWithLineFeed_throwsIllegalArgumentException() {
        Task task = new Task("first line\nsecond line");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, task::toDataString);
        assertEquals("Task fields cannot contain line breaks", exception.getMessage());
    }

    @Test
    public void toDataString_descriptionWithCarriageReturn_throwsIllegalArgumentException() {
        Task task = new Task("first line\rsecond line");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, task::toDataString);
        assertEquals("Task fields cannot contain line breaks", exception.getMessage());
    }

    @Test
    public void toString_incompleteTask_returnsIncompleteStatusAndDescription() {
        Task task = new Task("read book");

        assertEquals("[ ] read book", task.toString());
    }

    @Test
    public void toString_completedTask_returnsCompletedStatusAndDescription() {
        Task task = new Task("read book");
        task.markAsDone();

        assertEquals("[X] read book", task.toString());
    }
}
