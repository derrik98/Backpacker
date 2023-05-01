package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.TouristGuideBean;
import it.ispw.daniele.backpacker.bean.UserBean;
import it.ispw.daniele.backpacker.controller.login.LoginController;
import it.ispw.daniele.backpacker.exceptions.EmptyFieldException;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.FileManager;
import it.ispw.daniele.backpacker.utils.Roles;
import it.ispw.daniele.backpacker.view.utils_view.InterfaceController;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.*;


public class CliSignUpController extends InterfaceController {

    private File imageFile = null;

    public void init(Scanner scan) {

        System.out.print("\033[H\033[2J");

        LoginController lc = new LoginController();

        boolean regResult = false;

        String email;
        String username;
        String name;
        String surname;
        String password;
        String userType;
        String VATNumb;
        String ProfilePicture = "";

        System.out.println("Email:");
        email = scan.nextLine();
        System.out.flush();

        System.out.println("Username:");
        username = scan.nextLine();
        System.out.flush();

        System.out.println("Name:");
        name = scan.nextLine();
        System.out.flush();

        System.out.println("Surname:");
        surname = scan.nextLine();
        System.out.flush();

        System.out.println("Password:");
        password = scan.nextLine();
        System.out.flush();

        System.out.println("Type of user: Generic[0] - Tourist_Guide[1]");
        userType = scan.nextLine();
        System.out.flush();

        switch (userType) {
            case "0" -> userType = "USER";
            case "1" -> userType = "TOURIST_GUIDE";
        }

        System.out.println("Would you like profile image? Yes[y] - No[n]");

        String fileName = "";
        String newFileName = "";

        switch (scan.nextLine()){
            case "n":
                fileName = "";
                newFileName = "";
            case "y":
                final FileChooser fc = new FileChooser();
                fc.setTitle("Select image");
                fc.setInitialDirectory(new File(System.getProperty("user.home")));
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG","*.jpg"),
                        new FileChooser.ExtensionFilter("PNG","*.png"));
                // this.imageFile = fc.showOpenDialog(new Stage());
                ////if(this.imageFile!=null){
                //    ProfilePicture = this.imageFile.getName();
                //}
                //assert this.imageFile != null;
                //fileName = this.imageFile.getName();;
                //newFileName = username+fileName;
                //C'ERANO

        }

        System.out.flush();

        if (userType.equals(Roles.USER.name())){
            UserBean ub = this.setUserBean(username, name, surname, email, password, newFileName);

            try {
                regResult = lc.createUser(ub, "cli");
            } catch (EmptyFieldException | GenericException | SQLException exception) {
                System.out.println(RED + exception.getMessage() + RESET);
            }

        }
        else if(userType.equals(Roles.TOURIST_GUIDE.name())){
            System.out.println("VAT Number:");
            VATNumb = scan.nextLine();
            System.out.flush();


            TouristGuideBean tgb = this.setTouristGuideBean(username, name, surname, email, password, newFileName, VATNumb);

            regResult = lc.createTouristGuide(tgb, "cli");
        }

        if(Boolean.TRUE.equals(regResult)){
            if(this.imageFile != null){
                String path = FileManager.PROFILE;
                System.out.println(path);
                File file = new File(path, fileName);
                File newFile = new File(path, newFileName);
                try(InputStream inputStream = new FileInputStream(this.imageFile)){
                    Files.copy(inputStream, file.toPath());
                }catch (Exception e){
                    System.out.println("Warning image");
                }
                if(!file.renameTo(newFile)){
                    System.out.println("unable to rename");
                }
            }
            System.out.println(GREEN + "REGISTRATION SUCCESSFULLY" + RESET);
        }
        else{
            System.out.println(RED + "UNSUCCESSFUL REGISTRATION" + RESET);
        }

        email = "";
        username = "";
        name = "";
        surname = "";
        password = "";
        userType = "";
        VATNumb = "";
        this.imageFile = null;
        ProfilePicture = "";

        scan.close();
    }
}
