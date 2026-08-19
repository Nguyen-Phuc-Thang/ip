package magnus.parser;

/**
 * Parses raw user input into a command and its arguments.
 * Task fields are separated by the {@code /by}, {@code /from}, and {@code /to}
 * keywords, while spaces within a field are preserved.
 */
public class Parser {

    /**
     * Splits user input into a command followed by its argument fields. The first
     * whitespace-separated word is the command, and {@code /by}, {@code /from},
     * and {@code /to} delimit any subsequent fields.
     *
     * @param userInput the user input to split
     * @return the tokens in the input, or an empty array if the input is blank
     */
    public String[] parse(String userInput) {
        if (userInput.isBlank()) {
            return new String[0];
        }

        String[] commandAndArguments = userInput.strip().split("\\s+", 2);
        if (commandAndArguments.length == 1) {
            return commandAndArguments;
        }

        String[] arguments = commandAndArguments[1]
                .split("\\s+/(?:by|from|to)\\s+");
        String[] parsedInput = new String[arguments.length + 1];
        parsedInput[0] = commandAndArguments[0];
        System.arraycopy(arguments, 0, parsedInput, 1, arguments.length);
        return parsedInput;
    }
}
