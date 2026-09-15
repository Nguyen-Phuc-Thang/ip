package magnus.task;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Tests validation of event scheduling invariants.
 */
public class EventTaskTest {
    @Test
    public void constructor_equalStartAndEnd_throwsIllegalArgumentException() {
        LocalDateTime eventTime = LocalDateTime.of(2026, 9, 2, 15, 0);

        assertThrows(IllegalArgumentException.class, () ->
                new EventTask("meeting", eventTime, eventTime));
    }

    @Test
    public void constructor_startAfterEnd_throwsIllegalArgumentException() {
        LocalDateTime start = LocalDateTime.of(2026, 9, 2, 16, 0);
        LocalDateTime end = LocalDateTime.of(2026, 9, 2, 15, 0);

        assertThrows(IllegalArgumentException.class, () ->
                new EventTask("meeting", start, end));
    }
}
