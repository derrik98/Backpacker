package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.utils.SessionUser;

import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RED;
import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RESET;

public class CliMenuUserController {

    public void init(Scanner scanner) {


        CliUserGraphicChange ugc = CliUserGraphicChange.getInstance();

        do {
            System.out.print("\033[H\033[2J");
            System.out.println("MENU'------------------------------UNDO [u]--");
            System.out.println("---------------------------------------------");
            System.out.println("----------------Search [0]-------------------");
            System.out.println("----------------Profile [1]------------------");
            System.out.println("----------------Logout [2]-------------------");
            System.out.println("---------------------------------------------");
            System.out.println("---------------------------------------------");

            System.out.println("Command: ");

            switch (scanner.nextLine()) {
                case "0" -> ugc.switchToHome(scanner);
                case "1" -> ugc.switchToUserDetails();
                case "2" -> {
                    System.out.println(RED + "LOGOUT" + RESET);
                    SessionUser.getInstance().closeSession();
                    return;
                }
                case "u" -> {
                    System.out.println("undo");
                    return;
                }
                default -> System.out.println(RED + "COMMAND NOT FOUND\n" + RESET);
            }

        } while (true);
    }
}