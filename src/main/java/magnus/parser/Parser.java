package magnus.parser;

/**
 * Parses raw user input into tokens that can be interpreted as a command and
 * its arguments.
 */
public class Parser {

    /**
     * Splits user input on whitespace. Consecutive whitespace characters are treated
     * as one separator, and leading or trailing whitespace is ignored.
     *
     * @param userInput the user input to split
     * @return the tokens in the input, or an empty array if the input is blank
     */
    public String[] parse(String userInput) {
        if (userInput.isBlank()) {
            return new String[0];
        }

        return userInput.trim().split("\\s+");
    }
}
