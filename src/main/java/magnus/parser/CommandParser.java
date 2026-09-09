package magnus.parser;

/**
 * Parses raw user input into a command and its arguments.
 * Task fields are separated by the {@code /by}, {@code /from}, and {@code /to}
 * keywords, while spaces within a field are preserved.
 */
public class CommandParser {
    private static final String WHITESPACE_REGEX = "\\s+";
    private static final String FIELD_DELIMITER_REGEX =
            "(?:^|\\s+)/(?:by|from|to)(?:\\s+|$)";
    private static final int COMMAND_AND_ARGUMENTS_SPLIT_LIMIT = 2;
    private static final int COMMAND_ONLY_PART_COUNT = 1;
    private static final int PRESERVE_ALL_FIELDS = -1;

    /**
     * Creates a parser for command-line input.
     */
    public CommandParser() {
    }

    /**
     * Splits user input into a command followed by its argument fields. The first
     * whitespace-separated word is the command, and {@code /by}, {@code /from},
     * and {@code /to} delimit any subsequent fields.
     *
     * @param userInput The user input to split.
     * @return The tokens in the input, or an empty array if the input is blank.
     */
    public String[] parse(String userInput) {
        if (userInput.isBlank()) {
            return new String[0];
        }

        String[] commandAndArguments = userInput.strip()
                .split(WHITESPACE_REGEX, COMMAND_AND_ARGUMENTS_SPLIT_LIMIT);
        if (commandAndArguments.length == COMMAND_ONLY_PART_COUNT) {
            return commandAndArguments;
        }

        String[] arguments = commandAndArguments[1]
                .split(FIELD_DELIMITER_REGEX, PRESERVE_ALL_FIELDS);
        String[] parsedTokens = new String[arguments.length + 1];
        parsedTokens[0] = commandAndArguments[0];
        System.arraycopy(arguments, 0, parsedTokens, 1, arguments.length);
        return parsedTokens;
    }
}
