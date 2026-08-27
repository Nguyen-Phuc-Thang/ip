package magnus.command;

/**
 * Identifies the commands supported by Magnus and their user-facing keywords.
 */
public enum CommandType {
    BYE("bye", false),
    LIST("list", false),
    LIST_DEADLINE("list_deadline", false),
    LIST_EVENT("list_event", false),
    FIND("find", false),
    MARK("mark", true),
    UNMARK("unmark", true),
    TODO("todo", true),
    DEADLINE("deadline", true),
    EVENT("event", true),
    DELETE("delete", true);

    private final String keyword;
    private final boolean changesTaskList;

    /**
     * Creates a command type with the keyword entered by users.
     *
     * @param keyword The command's user-facing keyword.
     */
    CommandType(String keyword, boolean changesTaskList) {
        this.keyword = keyword;
        this.changesTaskList = changesTaskList;
    }

    public boolean changesTaskList() {
        return this.changesTaskList;
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
            if (type.keyword.equalsIgnoreCase(keyword)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown command: " + keyword);
    }
}
