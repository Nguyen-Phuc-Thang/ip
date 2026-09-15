package magnus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests the success and error response factories.
 */
public class MagnusResponseTest {
    @Test
    public void success_message_returnsNonErrorResponse() {
        MagnusResponse response = MagnusResponse.success("Task added");

        assertEquals("Task added", response.message());
        assertFalse(response.isError());
    }

    @Test
    public void error_message_returnsErrorResponse() {
        MagnusResponse response = MagnusResponse.error("Invalid command");

        assertEquals("Invalid command", response.message());
        assertTrue(response.isError());
    }
}
