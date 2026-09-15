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

        assertEquals("\tOpening move complete - I've added this To-Do task:\n\n\t[T][ ] read book", response);
    }

    @Test
    public void getResponse_invalidCommand_returnsErrorMessage() throws MagnusException {
        Magnus magnus = createMagnus();

        String response = magnus.getResponse("unknown");

        assertEquals("\tThat move is not in my playbook - I don't recognize the command 'unknown'.", response);
        assertFalse(magnus.isExitRequested());
    }

    @Test
    public void getResponseResult_validCommand_marksResponseAsSuccessful() throws MagnusException {
        Magnus magnus = createMagnus();

        MagnusResponse response = magnus.getResponseResult("todo read book");

        assertEquals("\tOpening move complete - I've added this To-Do task:\n\n\t[T][ ] read book",
                response.message());
        assertFalse(response.isError());
    }

    @Test
    public void getResponseResult_invalidCommand_marksResponseAsError() throws MagnusException {
        Magnus magnus = createMagnus();

        MagnusResponse response = magnus.getResponseResult("unknown");

        assertEquals("\tThat move is not in my playbook - I don't recognize the command 'unknown'.",
                response.message());
        assertTrue(response.isError());
    }

    @Test
    public void getResponse_byeCommand_returnsMessageAndRequestsExit() throws MagnusException {
        Magnus magnus = createMagnus();

        String response = magnus.getResponse("bye");

        assertEquals("\tThe board is set aside for now. Goodbye, and see you next game!", response);
        assertTrue(magnus.isExitRequested());
    }

    @Test
    public void getResponse_validUpdateFlow_updatesTaskAndPersistsResult() throws MagnusException {
        Magnus magnus = createMagnus();
        magnus.getResponse("todo read book");

        String prompt = magnus.getResponse("update 1");
        String updateResponse = magnus.getResponse("read two books");
        Magnus reloadedMagnus = createMagnus();

        assertEquals("\tYour move - enter the updated To-Do task in this format:\n"
                + "\t<new task name>", prompt);
        assertEquals("\tPosition updated - I've updated this task:\n\n\t[T][ ] read two books",
                updateResponse);
        assertEquals("\tHere's the current position - your full task list:\n\n"
                        + "\t1. [T][ ] read two books",
                reloadedMagnus.getResponse("list"));
    }

    @Test
    public void getResponse_invalidUpdateInput_failsThenProcessesNextCommandNormally() throws MagnusException {
        Magnus magnus = createMagnus();
        magnus.getResponse("deadline submit report /by 02/09/2026 1500");
        magnus.getResponse("update 1");

        String failureResponse = magnus.getResponse("submit report tomorrow");
        String listResponse = magnus.getResponse("list");

        assertEquals("\tThat move does not match the required format - the task was not updated.",
                failureResponse);
        assertEquals("\tHere's the current position - your full task list:\n\n"
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
