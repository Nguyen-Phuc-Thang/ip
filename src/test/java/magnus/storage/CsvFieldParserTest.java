package magnus.storage;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests decoding of the comma-separated fields used by task storage.
 */
public class CsvFieldParserTest {
    @Test
    public void parseFields_unquotedFields_returnsAllFields() {
        List<String> fields = CsvFieldParser.parseFields("T,0,read book");

        assertIterableEquals(List.of("T", "0", "read book"), fields);
    }

    @Test
    public void parseFields_quotedComma_returnsDecodedField() {
        List<String> fields = CsvFieldParser.parseFields("T,0,\"buy milk, eggs\"");

        assertIterableEquals(List.of("T", "0", "buy milk, eggs"), fields);
    }

    @Test
    public void parseFields_escapedQuotationMark_returnsDecodedField() {
        List<String> fields = CsvFieldParser.parseFields("T,0,\"read \"\"Dune\"\"\"");

        assertIterableEquals(List.of("T", "0", "read \"Dune\""), fields);
    }

    @Test
    public void parseFields_unclosedQuotedField_throwsIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class, () -> CsvFieldParser.parseFields("T,0,\"read book"));
    }

    @Test
    public void parseFields_quotationMarkInsideUnquotedField_throwsIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class, () -> CsvFieldParser.parseFields("T,0,re\"ad book"));
    }

    @Test
    public void parseFields_textAfterQuotedField_throwsIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class, () -> CsvFieldParser.parseFields("T,0,\"read book\"later"));
    }
}
