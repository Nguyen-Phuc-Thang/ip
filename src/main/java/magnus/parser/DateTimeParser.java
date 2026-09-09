package magnus.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Parses dates and date-times using Magnus's strict user-input formats.
 */
public class DateTimeParser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/uuuu HHmm")
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Creates a parser for Magnus's supported date and date-time formats.
     */
    public DateTimeParser() {
    }

    /**
     * Parses a date in {@code dd/MM/yyyy} format.
     *
     * @param date The date text to parse.
     * @return The parsed date.
     * @throws DateTimeParseException If the text is malformed or does not represent a valid date.
     */
    public LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    /**
     * Parses exactly two whitespace-separated dates in {@code dd/MM/yyyy} format.
     *
     * @param dateRange The start and end dates to parse.
     * @return A two-element array containing the parsed start and end dates.
     * @throws IllegalArgumentException If the input does not contain exactly two dates.
     * @throws DateTimeParseException If either date is malformed or invalid.
     */
    public LocalDate[] parseDateRange(String dateRange) {
        String[] dateArguments = dateRange.strip().split("\\s+");
        if (dateArguments.length != 2) {
            throw new IllegalArgumentException("date range must contain exactly two dates");
        }
        assert dateArguments.length == 2
                : "A validated date range must contain exactly two dates";

        return new LocalDate[] {
            parseDate(dateArguments[0]),
            parseDate(dateArguments[1])
        };
    }

    /**
     * Parses a date and time in {@code dd/MM/yyyy HHmm} format.
     *
     * @param dateTime The date-time text to parse.
     * @param fieldName The field name used in an error message.
     * @return The parsed date and time.
     * @throws IllegalArgumentException If the input is {@code null}, malformed, or invalid.
     */
    public LocalDateTime parseDateTime(String dateTime, String fieldName) {
        if (dateTime == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }

        try {
            return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    fieldName + " must use format dd/MM/yyyy HHmm and contain a valid date and time",
                    exception);
        }
    }
}
