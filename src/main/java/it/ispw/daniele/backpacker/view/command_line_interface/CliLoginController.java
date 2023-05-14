package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.controller.login.LoginController;
import it.ispw.daniele.backpacker.exceptions.EmptyFieldException;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.SessionUser;

import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RED;
import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RESET;

public class CliLoginController {

    public void init() {

        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print("\033[H\033[2J");
            System.out.println("----------------------------------EXIT [2]---");
            System.out.println("---------------------------------------------");
            System.out.println("----------------LOGIN [0]--------------------");
            System.out.println("----------------SIGNUP [1]-------------------");
            System.out.println("---------------------------------------------");
            System.out.println("---------------------------------------------");

            System.out.println("Command: ");

            switch (scanner.nextLine()) {
                case "0" -> {
                    try {
                        this.login(scanner);
                    } catch (Exception e) {
                        System.out.println("\n" + RED + e.getMessage() + RESET + "\n");
                    }
                }
                case "1" -> {
                    try {
                        CliUserGraphicChange.getInstance().switchToSignUp(scanner);
                    } catch (Exception exception) {
                        System.out.println(RED + exception.getMessage() + RESET);
                    }
                }
                case "2" -> {
                    System.out.println(RED + "CLOSE APPLICATION" + RESET);
                    scanner.close();
                    return;
                }
                default -> System.out.println(RED + "COMMAND NOT FOUND\n" + RESET);

            }

        } while (true);

    }

    private void login(Scanner scanner) throws EmptyFieldException {

        System.out.print("\033[H\033[2J");

        System.out.println("Username:");
        String username = scanner.nextLine();

        System.out.println("Password:");
        String password = scanner.nextLine();

        GeneralUserBean gub = new GeneralUserBean();
        gub.setUsername(username);
        gub.setPassword(password);

        LoginController controller = new LoginController();
        GeneralUserBean gu;
        try {
            gu = controller.login(gub, "cli");

            if (gu == null) {
                System.out.println(RED + "LOGIN FAILED" + RESET);
            } else {
                String role = gu.getRole();

                //SET SESSION GENERAL USER
                SessionUser su = SessionUser.getInstance();
                su.setSession(gu);

                switch (role) {
                    case "user" -> CliUserGraphicChange.getInstance().menuBar(scanner);
                    case "tourist_guide" -> CliTouristGuideGraphicChange.getInstance().menuBar(scanner);
                }
            }
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }

    }
}