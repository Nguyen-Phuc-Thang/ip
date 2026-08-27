package magnus.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the task collection operations provided by {@link TaskList}.
 */
public class TaskListTest {
    @Test
    public void constructor_noTasks_createsEmptyTaskList() {
        TaskList tasks = new TaskList();

        assertEquals(0, tasks.getLength());
    }

    @Test
    public void constructor_listOfTasks_createsIndependentTaskListWithSameTasks() {
        Task task1 = new ToDoTask("read book");
        Task task2 = new ToDoTask("return book");
        List<Task> originalTasks = new ArrayList<>(List.of(task1, task2));

        TaskList tasks = new TaskList(originalTasks);
        originalTasks.clear();

        assertEquals(2, tasks.getLength());
        assertSame(task1, tasks.getTask(0));
        assertSame(task2, tasks.getTask(1));
    }

    @Test
    public void getTasks_existingTasks_returnsImmutableSnapshot() {
        Task task1 = new ToDoTask("read book");
        Task task2 = new ToDoTask("return book");
        TaskList tasks = new TaskList(List.of(task1));

        List<Task> snapshot = tasks.getTasks();
        tasks.addTask(task2);

        assertEquals(1, snapshot.size());
        assertSame(task1, snapshot.get(0));
        assertThrows(UnsupportedOperationException.class, () -> snapshot.add(task2));
    }

    @Test
    public void filterTaskOnDate_mixedTasks_returnsOnlyDeadlinesOnSpecifiedDate() {
        DeadlineTask matchingTask = new DeadlineTask(
                "submit report", LocalDateTime.of(2026, 8, 27, 18, 0));
        DeadlineTask differentDateTask = new DeadlineTask(
                "pay bill", LocalDateTime.of(2026, 8, 28, 18, 0));
        ToDoTask taskWithoutDate = new ToDoTask("read book");
        TaskList tasks = new TaskList(List.of(matchingTask, differentDateTask, taskWithoutDate));

        TaskList filteredTasks = tasks.filterTaskOnDate(LocalDate.of(2026, 8, 27));

        assertIterableEquals(List.of(matchingTask), filteredTasks.getTasks());
        assertEquals(3, tasks.getLength());
    }

    @Test
    public void filterTaskWithinDateRange_mixedTasks_returnsOnlyEventsWithinInclusiveRange() {
        EventTask boundaryTask = new EventTask(
                "course", LocalDateTime.of(2026, 8, 10, 9, 0),
                LocalDateTime.of(2026, 8, 20, 17, 0));
        EventTask insideRangeTask = new EventTask(
                "conference", LocalDateTime.of(2026, 8, 12, 9, 0),
                LocalDateTime.of(2026, 8, 18, 17, 0));
        EventTask startsBeforeRangeTask = new EventTask(
                "camp", LocalDateTime.of(2026, 8, 9, 9, 0),
                LocalDateTime.of(2026, 8, 18, 17, 0));
        EventTask endsAfterRangeTask = new EventTask(
                "workshop", LocalDateTime.of(2026, 8, 12, 9, 0),
                LocalDateTime.of(2026, 8, 21, 17, 0));
        DeadlineTask nonEventTask = new DeadlineTask(
                "submit report", LocalDateTime.of(2026, 8, 15, 18, 0));
        TaskList tasks = new TaskList(List.of(
                boundaryTask, insideRangeTask, startsBeforeRangeTask, endsAfterRangeTask, nonEventTask));

        TaskList filteredTasks = tasks.filterTaskWithinDateRange(
                LocalDate.of(2026, 8, 10), LocalDate.of(2026, 8, 20));

        assertIterableEquals(List.of(boundaryTask, insideRangeTask), filteredTasks.getTasks());
        assertEquals(5, tasks.getLength());
    }

    @Test
    public void addTask_newTask_addsTaskAtEnd() {
        Task task1 = new ToDoTask("read book");
        Task task2 = new ToDoTask("return book");
        TaskList tasks = new TaskList(List.of(task1));

        tasks.addTask(task2);

        assertEquals(2, tasks.getLength());
        assertSame(task2, tasks.getTask(1));
    }

    @Test
    public void removeTask_validIndex_returnsRemovedTaskAndShiftsRemainingTasks() {
        Task task1 = new ToDoTask("read book");
        Task task2 = new ToDoTask("return book");
        Task task3 = new ToDoTask("buy book");
        TaskList tasks = new TaskList(List.of(task1, task2, task3));

        Task removedTask = tasks.removeTask(1);

        assertSame(task2, removedTask);
        assertEquals(2, tasks.getLength());
        assertSame(task1, tasks.getTask(0));
        assertSame(task3, tasks.getTask(1));
    }

    @Test
    public void removeTask_indexOutsideTaskList_throwsIndexOutOfBoundsException() {
        TaskList tasks = new TaskList();

        assertThrows(IndexOutOfBoundsException.class, () -> tasks.removeTask(0));
    }

    @Test
    public void getTask_validIndex_returnsTaskAtIndex() {
        Task task1 = new ToDoTask("read book");
        Task task2 = new ToDoTask("return book");
        TaskList tasks = new TaskList(List.of(task1, task2));

        Task result = tasks.getTask(1);

        assertSame(task2, result);
    }

    @Test
    public void getTask_indexOutsideTaskList_throwsIndexOutOfBoundsException() {
        TaskList tasks = new TaskList(List.of(new ToDoTask("read book")));

        assertThrows(IndexOutOfBoundsException.class, () -> tasks.getTask(1));
    }

    @Test
    public void markTaskAsDone_validIndex_marksOnlySelectedTaskAsDone() {
        Task task1 = new ToDoTask("read book");
        Task task2 = new ToDoTask("return book");
        TaskList tasks = new TaskList(List.of(task1, task2));

        tasks.markTaskAsDone(1);

        assertEquals("[T][ ] read book", task1.toString());
        assertEquals("[T][X] return book", task2.toString());
    }

    @Test
    public void markTaskAsDone_indexOutsideTaskList_throwsIndexOutOfBoundsException() {
        TaskList tasks = new TaskList();

        assertThrows(IndexOutOfBoundsException.class, () -> tasks.markTaskAsDone(0));
    }

    @Test
    public void markTaskAsUndone_validIndex_marksOnlySelectedTaskAsUndone() {
        Task task1 = new ToDoTask("read book");
        Task task2 = new ToDoTask("return book");
        task1.markAsDone();
        task2.markAsDone();
        TaskList tasks = new TaskList(List.of(task1, task2));

        tasks.markTaskAsUndone(1);

        assertEquals("[T][X] read book", task1.toString());
        assertEquals("[T][ ] return book", task2.toString());
    }

    @Test
    public void markTaskAsUndone_indexOutsideTaskList_throwsIndexOutOfBoundsException() {
        TaskList tasks = new TaskList();

        assertThrows(IndexOutOfBoundsException.class, () -> tasks.markTaskAsUndone(0));
    }

    @Test
    public void printTasks_multipleTasks_printsOneBasedNumberedList() {
        Task task1 = new ToDoTask("read book");
        Task task2 = new ToDoTask("return book");
        task2.markAsDone();
        TaskList tasks = new TaskList(List.of(task1, task2));
        String expectedOutput = String.join(System.lineSeparator(),
                "\t1. [T][ ] read book",
                "\t2. [T][X] return book") + System.lineSeparator();

        String output = capturePrintedTasks(tasks);

        assertEquals(expectedOutput, output);
    }

    /**
     * Captures the standard output produced when the supplied task list is printed.
     *
     * @param tasks The task list to print.
     * @return The captured output.
     */
    private String capturePrintedTasks(TaskList tasks) {
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        try (PrintStream testOutput = new PrintStream(capturedOutput, true, StandardCharsets.UTF_8)) {
            System.setOut(testOutput);
            tasks.printTasks();
        } finally {
            System.setOut(originalOutput);
        }

        return capturedOutput.toString(StandardCharsets.UTF_8);
    }
}
