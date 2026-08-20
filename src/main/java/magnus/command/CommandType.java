package magnus.command;

/**
 * Identifies the commands supported by Magnus and their user-facing keywords.
 */
public enum CommandType {
    BYE("bye"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    DELETE("delete");

    private final String keyword;

    /**
     * Creates a command type with the keyword entered by users.
     *
     * @param keyword The command's user-facing keyword.
     */
    CommandType(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Returns the command type represented by the supplied keyword.
     *
     * @param keyword The command keyword entered by the user.
     * @return The matching command type.
     * @throws IllegalArgumentException If the keyword does not identify a supported command.
     */
    public static CommandType fromKeyword(String keyword) {
        for (CommandType type : values()) {
            if (type.keyword.equals(keyword)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown command: " + keyword);
    }
}
