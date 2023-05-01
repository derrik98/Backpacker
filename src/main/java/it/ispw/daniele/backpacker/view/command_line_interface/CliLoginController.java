package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.controller.login.LoginController;
import it.ispw.daniele.backpacker.exceptions.EmptyFieldException;
import it.ispw.daniele.backpacker.utils.SessionUser;

import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RED;
import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RESET;

public class CliLoginController {

    public void init() {

        Scanner scan = new Scanner(System.in);
        String input;

        do {
            System.out.print("\033[H\033[2J");
            System.out.println("----------------------------------EXIT [quit]");
            System.out.println("---------------------------------------------");
            System.out.println("----------------LOGIN [0]--------------------");
            System.out.println("----------------SIGNUP [1]-------------------");
            System.out.println("---------------------------------------------");
            System.out.println("---------------------------------------------");

            System.out.println("Command: ");

            input = scan.next();

            switch (input) {
                case "0" -> this.login();
                case "1" -> {
                    CliSignUpController signUpController = new CliSignUpController();
                    signUpController.init(scan);
                }
                /*case "quit" -> {
                    System.out.println(RED + "CLOSE APPLICATION" + RESET);
                    //scan.close();
                    return;
                }*/
                default ->
                        System.out.println(RED + "CLOSE APPLICATION" + RESET);//System.out.println(RED + "COMMAND NOT FOUND\n" + RESET);
            }

        } while(!input.equals("quit"));

        //System.out.println(RED + "CLOSE APPLICATION" + RESET);
        scan.close();
    }

    private void login() {
        Scanner scan = new Scanner(System.in);

        System.out.print("\033[H\033[2J");

        System.out.println("Username:");
        String username = scan.next();

        System.out.println("Password:");
        String password = scan.next();

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
                    case "user" -> CliUserGraphicChange.getInstance().menuBar();
                    case "tourist_guide" -> CliTouristGuideGraphicChange.getInstance().menuBar();
                }
            }

        }catch (EmptyFieldException e) {
            System.out.println("\n" + RED + e.getMessage() + RESET + "\n");
        }

        scan.close();
    }

}
