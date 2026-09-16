package magnus.command;

/**
 * Formats task-count summaries used in command responses.
 */
final class TaskCountMessage {
    private TaskCountMessage() {
    }

    /**
     * Formats the current number of tasks, using the correct singular or plural noun.
     *
     * @param taskCount The current number of tasks.
     * @return A user-facing task-count summary.
     */
    static String formatTotal(int taskCount) {
        String taskNoun = taskCount == 1 ? "task" : "tasks";
        return String.format("\tYou now have %d %s in the list.", taskCount, taskNoun);
    }
}
