public class Magnus {
    public static void main(String[] args) {
        String banner = """
                ███╗   ███╗ █████╗  ██████╗ ███╗   ██╗██╗   ██╗███████╗
                ████╗ ████║██╔══██╗██╔════╝ ████╗  ██║██║   ██║██╔════╝
                ██╔████╔██║███████║██║  ███╗██╔██╗ ██║██║   ██║███████╗
                ██║╚██╔╝██║██╔══██║██║   ██║██║╚██╗██║██║   ██║╚════██║
                ██║ ╚═╝ ██║██║  ██║╚██████╔╝██║ ╚████║╚██████╔╝███████║
                ╚═╝     ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝ ╚══════╝
                """;

        String divider = "____________________________________________________________";

        String greeting = """
                Hello! I'm Magnus.
                How can I help you today?
                """;
        
        String exit = "Goodbye. See you soon!";

        System.out.println(divider);
        System.out.print(banner);
        System.out.print(greeting);
        System.out.println(divider);
        System.out.println(exit);
        System.out.println(divider);

    }
}
