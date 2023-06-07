package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.utils.SessionUser;

import java.util.ArrayList;
import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RED;
import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RESET;

public class CliMenuUserController {

    private static final ArrayList<String> stackScene = new ArrayList<>();

    private CliUserGraphicChange ugc;

    private static final String HOME = "home";
    private static final String RESULT = "result";
    private static final String PROFILE = "profile";

    public void init(Scanner scanner) {

        this.ugc = CliUserGraphicChange.getInstance();

        if (stackScene.isEmpty()) {
            stackScene.add(HOME);
        }

        do {
            System.out.print("\033[H\033[2J");
            System.out.println("MENU'------------------------------UNDO [u]--");
            System.out.println("---------------------------------------------");
            System.out.println("----------------Search [0]-------------------");
            System.out.println("----------------Result [1]-------------------");
            System.out.println("----------------Profile [2]------------------");
            System.out.println("----------------Logout [3]-------------------");
            System.out.println("---------------------------------------------");
            System.out.println("---------------------------------------------");

            System.out.println("Command: ");

            switch (scanner.nextLine()) {
                case "0" -> {
                    ugc.switchToHome(scanner);
                    stackScene.add(HOME);
                }case "1" -> {
                    ugc.switchToResult(scanner);
                    stackScene.add(RESULT);
                }
                case "2" -> {
                    ugc.switchToUserDetails();
                    stackScene.add(PROFILE);
                }
                case "3" -> {
                    System.out.println(RED + "LOGOUT" + RESET);
                    SessionUser.getInstance().closeSession();
                    ugc.switchToLogin();
                    return;
                }
                case "u" -> {
                    this.undo(scanner);
                    //System.out.println("undo");
                    //return;
                }
                default -> System.out.println(RED + "COMMAND NOT FOUND\n" + RESET);
            }

        } while (true);
    }

    private void undo(Scanner scanner) {

        if (stackScene.size() > 1) {
            String from = stackScene.get(stackScene.size() - 2);
            switch (from) {
                case HOME -> this.ugc.switchToHome(scanner);
                case RESULT -> this.ugc.switchToResult(scanner);
                case PROFILE -> this.ugc.switchToUserDetails();
                default -> {
                    System.out.println(RED + "Error" + RESET);
                    SessionUser.getInstance().closeSession();
                    return;
                }
            }
            stackScene.remove(stackScene.size() - 1);

        } else {
            this.ugc.switchToHome(scanner);
        }
    }
}
