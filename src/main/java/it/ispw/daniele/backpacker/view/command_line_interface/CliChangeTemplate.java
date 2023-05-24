package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.exceptions.*;
import it.ispw.daniele.backpacker.utils.Roles;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RED;
import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RESET;

public abstract class CliChangeTemplate {

    protected final Logger logger = Logger.getLogger("GraphicChange");

    protected Roles whoAmI;

    protected static Stack<String> stackScene = new Stack<>();

    public void catcher(CliAction cliGuiAction) {
        try {
            cliGuiAction.action();
        } catch (IOException | AddressNotFoundException | CityNotFoundException | SQLException | GenericException |
                 EmptyFieldException | MonumentNotFoundException | NoSuchAlgorithmException ioException) {
            logger.log(Level.WARNING, ioException.toString(), ioException.getCause());
        }
    }

    public void switchToLogin() {
        this.catcher(() -> {
            stackScene.push("login");
            CliLoginController clc = new CliLoginController();
            clc.init();
        });
    }

    public void switchToResult(Scanner scanner) {
        this.catcher(() -> {
            stackScene.push("result");
            CliResultController crc = new CliResultController();
            crc.init(scanner);
        });
    }

    public void switchToSignUp(Scanner scanner) {
        this.catcher(() -> {
            stackScene.push("signUp");
            CliSignUpController signUpController = new CliSignUpController();
            signUpController.init(scanner);
        });
    }

    public void menuBar(Scanner scanner) {
        this.catcher(() -> {

            stackScene.push("home");

            switch (whoAmI) {
                case USER -> {
                    CliMenuUserController cliMenuUserController = new CliMenuUserController();
                    cliMenuUserController.init(scanner);
                }
                case TOURIST_GUIDE -> {
                    CliMenuGuideController cliMenuGuideController = new CliMenuGuideController();
                    cliMenuGuideController.init(scanner);
                }
                default -> System.out.println(RED + "ERROR\n" + RESET);
            }
        });
    }

    protected abstract void switchToHome(Scanner scanner) throws IOException;

    protected abstract void undo(Scanner scanner);
}
