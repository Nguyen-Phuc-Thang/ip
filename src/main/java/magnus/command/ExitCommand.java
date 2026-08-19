package magnus.command;

public class ExitCommand implements Command{
    private static String EXIT_TEXT = "\tGoodbye. See you soon!";
    @Override
    public void execute(String[] args) {
        System.out.println(EXIT_TEXT);
    }
}
