package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.HomeBean;
import it.ispw.daniele.backpacker.exceptions.*;
import it.ispw.daniele.backpacker.utils.Roles;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RED;
import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RESET;

public abstract class CliGuiChangeTemplate {

    protected final Logger logger = Logger.getLogger("GraphicChange");

    protected Roles whoAmI;

    public void catcher(CliGuiAction cGuiAction){
        try {
            cGuiAction.action();
        }catch (IOException | AddressNotFoundException | CityNotFoundException | MonumentNotFoundException |
                NoSuchAlgorithmException | GenericException ioException){
            logger.log(Level.WARNING, ioException.toString(), ioException.getCause());
        }
    }

    public void switchToLogin(){
        this.catcher(() -> {
            CliLoginController clc = new CliLoginController();
            clc.init();
        });
    }

    public  void switchToResult(HomeBean homeBean, Scanner scanner){
        this.catcher(() -> {
            CliResultController crc = new CliResultController();
            crc.init(homeBean, scanner);
        });
    }

    public void switchToSignUp(Scanner scanner) throws SQLException, EmptyFieldException, GenericException {
        CliSignUpController signUpController = new CliSignUpController();
        signUpController.init(scanner);
    }

    public void menuBar (Scanner scanner){
        this.catcher(() -> {

            switch (whoAmI){
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
}
