package it.ispw.daniele.backpacker.view.command_line_interface;


public class CLI {

    public static final String RESET = "\033[0m";  // TEXT RESET

    public static final String RED = "\033[0;31m"; // TEXT RED
    public static final String GREEN = "\u001B[32m"; // TEXT GREEN
    public static final String BOLD = "\033[0;1m"; // TEXT BOLD

    public static void main(String[] args) {

        CliLoginController cliLoginController = new CliLoginController();
        cliLoginController.init();

    }
}
