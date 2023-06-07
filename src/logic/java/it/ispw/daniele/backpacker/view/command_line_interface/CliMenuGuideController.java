package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.utils.SessionUser;

import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RED;
import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RESET;

public class CliMenuGuideController {

    public void init(Scanner scanner) {

        CliTouristGuideGraphicChange touristGuideGraphicChange = CliTouristGuideGraphicChange.getInstance();

        do {
            System.out.print("\033[H\033[2J");
            System.out.println("MENU'------------------------------UNDO [u]--");
            System.out.println("---------------------------------------------");
            System.out.println("----------------Home [0]---------------------");
            System.out.println("----------------Add Itinerary [1]------------");
            System.out.println("----------------Profile [2]------------------");
            System.out.println("----------------Logout [3]-------------------");
            System.out.println("---------------------------------------------");
            System.out.println("---------------------------------------------");

            System.out.println("Command: ");

            switch (scanner.nextLine()) {
                case "0" -> touristGuideGraphicChange.switchToHome(scanner);
                case "1" -> touristGuideGraphicChange.switchToAddItinerary();
                case "2" -> touristGuideGraphicChange.switchToGuideDetails();
                case "3" -> {
                    System.out.println(RED + "LOGOUT" + RESET);
                    SessionUser.getInstance().closeSession();
                    return;
                }
                case "u" -> {

                    return;
                }
                default -> System.out.println(RED + "Command not found\n" + RESET);
            }

        } while (true);
    }
}
