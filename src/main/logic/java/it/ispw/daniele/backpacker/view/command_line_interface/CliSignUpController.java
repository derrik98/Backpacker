package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.TouristGuideBean;
import it.ispw.daniele.backpacker.bean.UserBean;
import it.ispw.daniele.backpacker.controller.login.LoginController;
import it.ispw.daniele.backpacker.exceptions.EmptyFieldException;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.Roles;
import it.ispw.daniele.backpacker.view.utils_view.InterfaceController;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;
import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.*;


public class CliSignUpController extends InterfaceController {

    private File imageFile = null;

    public void init(Scanner scanner) throws SQLException, EmptyFieldException, GenericException {

        System.out.print("\033[H\033[2J");

        LoginController lc = new LoginController();

        boolean regResult;

        String email;
        String username;
        String name;
        String surname;
        String password;
        String userType;
        String vatNumb;

        System.out.println("Email:");
        email = scanner.nextLine();

        System.out.println("Username:");
        username = scanner.nextLine();

        System.out.println("Name:");
        name = scanner.nextLine();

        System.out.println("Surname:");
        surname = scanner.nextLine();

        System.out.println("Password:");
        password = scanner.nextLine();

        System.out.println("Type of user: Generic[0] - Tourist_Guide[1]");
        userType = scanner.nextLine();

        if (userType.equals("1")) {
            userType = "TOURIST_GUIDE";
        } else {
            userType = "USER";
        }

        System.out.println("Would you like profile image? Yes[y] - No[n]");

        String fileName = "";
        String newFileName = "";

        if(scanner.nextLine().equals("y")){
            final FileChooser fc = new FileChooser();
            fc.setTitle("Select image");
            fc.setInitialDirectory(new File(System.getProperty("user.home")));
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG","*.jpg"),
                    new FileChooser.ExtensionFilter("PNG","*.png"));
        }
        else {
            fileName = "";
            newFileName = "";
        }


        if (userType.equals(Roles.USER.name())){
            UserBean ub = this.setUserBean(username, name, surname, email, password, newFileName);

            regResult = lc.createUser(ub, "cli");
        }
        else {
            System.out.println("VAT Number:");
            vatNumb = scanner.nextLine();

            TouristGuideBean tgb = this.setTouristGuideBean(username, name, surname, email, password, newFileName, vatNumb);

            regResult = lc.createTouristGuide(tgb, "cli");
        }

        if(Boolean.TRUE.equals(regResult)){

            this.setImage(this.imageFile, fileName, newFileName);
            System.out.println(GREEN + "REGISTRATION SUCCESSFULLY" + RESET);
        }
        else{
            System.out.println(RED + "UNSUCCESSFUL REGISTRATION" + RESET);
        }

        this.imageFile = null;

    }
}
