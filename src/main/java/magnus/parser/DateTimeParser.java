package magnus.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DateTimeParser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/uuuu HHmm")
            .withResolverStyle(ResolverStyle.STRICT);

    public LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    public LocalDate[] parseDateRange(String dateRange) {
        String[] dateArguments = dateRange.strip().split("\\s+");
        if (dateArguments.length != 2) {
            throw new IllegalArgumentException("date range must contain exactly two dates");
        }

        return new LocalDate[] {
            parseDate(dateArguments[0]),
            parseDate(dateArguments[1])
        };
    }

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
