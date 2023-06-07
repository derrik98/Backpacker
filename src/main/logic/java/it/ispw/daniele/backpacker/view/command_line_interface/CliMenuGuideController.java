package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.utils.SessionUser;

import java.util.ArrayList;
import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RED;
import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RESET;

public class CliMenuGuideController {

    private static final ArrayList<String> stackScene = new ArrayList<>();

    private CliTouristGuideGraphicChange ggc;

    private static final String HOME = "home";
    private static final String RESULT = "result";
    private static final String ADD_ITINERARY = "addItinerary";
    private static final String PROFILE = "profile";

    public void init(Scanner scanner) {

        ggc = CliTouristGuideGraphicChange.getInstance();

        if (stackScene.isEmpty()) {
            stackScene.add(HOME);
        }

        do {
            System.out.print("\033[H\033[2J");
            System.out.println("MENU'------------------------------UNDO [u]--");
            System.out.println("---------------------------------------------");
            System.out.println("----------------Search [0]-------------------");
            System.out.println("----------------Result [1]-------------------");
            System.out.println("----------------Add Itinerary [2]------------");
            System.out.println("----------------Profile [3]------------------");
            System.out.println("----------------Logout [4]-------------------");
            System.out.println("---------------------------------------------");
            System.out.println("---------------------------------------------");

            System.out.println("Command: ");

            switch (scanner.nextLine()) {
                case "0" -> {
                    ggc.switchToHome(scanner);
                    stackScene.add(HOME);
                }
                case "1" -> {
                    ggc.switchToResult(scanner);
                    stackScene.add(RESULT);
                }
                case "2" -> {
                    ggc.switchToAddItinerary();
                    stackScene.add(ADD_ITINERARY);
                }
                case "3" -> {
                    ggc.switchToGuideDetails();
                    stackScene.add(PROFILE);
                }
                case "4" -> {
                    System.out.println(RED + "LOGOUT" + RESET);
                    SessionUser.getInstance().closeSession();
                    return;
                }
                case "u" -> {
                    this.undo(scanner);
                    //return;
                }
                default -> System.out.println(RED + "Command not found\n" + RESET);
            }

        } while (true);
    }

    public void undo(Scanner scanner) {

        if (stackScene.size() > 1) {
            String from = stackScene.get(stackScene.size() - 2);
            switch (from) {
                case HOME -> this.ggc.switchToHome(scanner);
                case RESULT -> this.ggc.switchToResult(scanner);
                case ADD_ITINERARY -> this.ggc.switchToAddItinerary();
                case PROFILE -> this.ggc.switchToGuideDetails();
                default -> {
                    System.out.println(RED + "Error" + RESET);
                    SessionUser.getInstance().closeSession();
                    return;
                }
            }
            stackScene.remove(stackScene.size() - 1);

        } else {
            this.ggc.switchToHome(scanner);
        }
    }
}
