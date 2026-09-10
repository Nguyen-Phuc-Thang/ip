package magnus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import magnus.exception.MagnusException;

/**
 * Tests the command-response interface exposed by {@link Magnus}.
 */
public class MagnusTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void getResponse_validCommand_returnsCommandMessage() throws MagnusException {
        Magnus magnus = createMagnus();

        String response = magnus.getResponse("todo read book");

        assertEquals("\tI've added this To-Do task:\n\n\t[T][ ] read book", response);
    }

    @Test
    public void getResponse_invalidCommand_returnsErrorMessage() throws MagnusException {
        Magnus magnus = createMagnus();

        String response = magnus.getResponse("unknown");

        assertEquals("\tSorry, I don't know what you mean by 'unknown'", response);
        assertFalse(magnus.isExitRequested());
    }

    @Test
    public void getResponse_byeCommand_returnsMessageAndRequestsExit() throws MagnusException {
        Magnus magnus = createMagnus();

        String response = magnus.getResponse("bye");

        assertEquals("\tGoodbye. See you soon!", response);
        assertTrue(magnus.isExitRequested());
    }

    @Test
    public void getResponse_validUpdateFlow_updatesTaskAndPersistsResult() throws MagnusException {
        Magnus magnus = createMagnus();
        magnus.getResponse("todo read book");

        String prompt = magnus.getResponse("update 1");
        String updateResponse = magnus.getResponse("read two books");
        Magnus reloadedMagnus = createMagnus();

        assertEquals("\tPlease enter the updated To-Do task in this format:\n"
                + "\t<new task name>", prompt);
        assertEquals("\tI've updated this task:\n\n\t[T][ ] read two books", updateResponse);
        assertEquals("\tHere's your task list:\n\n\t1. [T][ ] read two books",
                reloadedMagnus.getResponse("list"));
    }

    @Test
    public void getResponse_invalidUpdateInput_failsThenProcessesNextCommandNormally() throws MagnusException {
        Magnus magnus = createMagnus();
        magnus.getResponse("deadline submit report /by 02/09/2026 1500");
        magnus.getResponse("update 1");

        String failureResponse = magnus.getResponse("submit report tomorrow");
        String listResponse = magnus.getResponse("list");

        assertEquals("\tUpdate failed.", failureResponse);
        assertEquals("\tHere's your task list:\n\n"
                + "\t1. [D][ ] submit report (by: Sep 02, 2026 15:00)", listResponse);
    }

    /**
     * Creates a Magnus instance with an isolated task data file.
     *
     * @return A Magnus instance for the current test.
     * @throws MagnusException If the test data file cannot be loaded.
     */
    private Magnus createMagnus() throws MagnusException {
        return new Magnus(this.temporaryDirectory.resolve("magnus.txt"));
    }
}
