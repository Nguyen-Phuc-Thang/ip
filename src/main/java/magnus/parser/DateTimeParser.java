package magnus.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Parses dates and date-times using Magnus's strict user-input formats.
 */
public class DateTimeParser {
    private static final int DATE_RANGE_ARGUMENT_COUNT = 2;
    private static final int START_DATE_INDEX = 0;
    private static final int END_DATE_INDEX = 1;

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
        return LocalDate.parse(date, DateTimeFormats.DATE_FORMATTER);
    }

    /**
     * Parses exactly two whitespace-separated dates in {@code dd/MM/yyyy} format.
     *
     * @param dateRange The start and end dates to parse.
     * @return The parsed date range.
     * @throws IllegalArgumentException If the input does not contain exactly two dates.
     * @throws DateTimeParseException If either date is malformed or invalid.
     */
    public DateRange parseDateRange(String dateRange) {
        String[] dateArguments = dateRange.strip().split("\\s+");
        if (dateArguments.length != DATE_RANGE_ARGUMENT_COUNT) {
            throw new IllegalArgumentException("date range must contain exactly two dates");
        }
        assert dateArguments.length == 2
                : "A validated date range must contain exactly two dates";

        LocalDate startDate = parseDate(dateArguments[START_DATE_INDEX]);
        LocalDate endDate = parseDate(dateArguments[END_DATE_INDEX]);
        return new DateRange(startDate, endDate);
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
            return LocalDateTime.parse(dateTime, DateTimeFormats.DATE_TIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    fieldName + " must use format dd/MM/yyyy HHmm and contain a valid date and time",
                    exception);
        }
    }
}
