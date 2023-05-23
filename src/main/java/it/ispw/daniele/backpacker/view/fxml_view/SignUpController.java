package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.bean.TouristGuideBean;
import it.ispw.daniele.backpacker.bean.UserBean;
import it.ispw.daniele.backpacker.controller.login.LoginController;
import it.ispw.daniele.backpacker.exceptions.EmptyFieldException;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.Roles;
import it.ispw.daniele.backpacker.view.utils_view.InterfaceController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SignUpController extends InterfaceController {

    @FXML
    private Label touristGuide;
    @FXML
    private TextField textFieldImage;
    @FXML
    private Label user;
    @FXML
    private TextField textFieldEmailSignUp;
    @FXML
    private TextField textFieldNameSignUp;
    @FXML
    private TextField textFieldSurnameSignUp;
    @FXML
    private TextField textFieldPassSignUp;
    @FXML
    private TextField textFieldConfPassSignUp;
    @FXML
    private TextField textFieldVATNumber;
    @FXML
    public Text errorText;
    private File imageFile = null;

    @FXML
    public void signUp() {
        LoginController lc = new LoginController();

        boolean regResult = false;

        String email;
        String username;
        String password;
        String userType;
        String vatNumb;

        email = textFieldEmailSignUp.getText();
        username = textFieldNameSignUp.getText();
        password = textFieldPassSignUp.getText();
        if (this.user.isUnderline()) {
            userType = user.getId();
        } else {
            userType = touristGuide.getId();
        }

        String fileName;
        String newFileName;

        if (this.imageFile == null) {
            fileName = "";
            newFileName = "";
        } else {
            fileName = this.imageFile.getName();
            newFileName = username + fileName;
        }

        if (userType.equals(Roles.USER.name())) {
            String name = this.textFieldNameSignUp.getText();
            String surname = this.textFieldSurnameSignUp.getText();
            UserBean ub = this.setUserBean(username, name, surname, email, password, newFileName);

            try {
                regResult = lc.createUser(ub, "gui");
            } catch (EmptyFieldException | GenericException exception) {
                this.errorText.setText(exception.getMessage());
            }

        } else if (userType.equals(Roles.TOURIST_GUIDE.name())) {
            String name = this.textFieldNameSignUp.getText();
            String surname = this.textFieldSurnameSignUp.getText();
            vatNumb = textFieldVATNumber.getText();
            TouristGuideBean tgb = this.setTouristGuideBean(username, name, surname, email, password, newFileName, vatNumb);
            try {
                regResult = lc.createTouristGuide(tgb, "gui");
            } catch (EmptyFieldException | GenericException exception) {
                this.errorText.setText(exception.getMessage());
            }
        }

        if (Boolean.TRUE.equals(regResult)) {
            this.setImage(this.imageFile, fileName, newFileName);
            System.out.println("REGISTRATION SUCCESSFULLY");
//            if (this.imageFile != null) {
//                String path = FileManager.PROFILE;
//                System.out.println(path);
//                File file = new File(path, fileName);
//                File newFile = new File(path, newFileName);
//                try (InputStream inputStream = new FileInputStream(this.imageFile)) {
//                    Files.copy(inputStream, file.toPath());
//                } catch (Exception e) {
//                    System.out.println("Warning image");
//                }
//                if (!file.renameTo(newFile)) {
//                    System.out.println("unable to rename");
//                }
//            }
            this.errorText.setText("");
        } else {
            System.out.println("unsuccessfully registration");
        }

        this.textFieldEmailSignUp.setText("");
        this.textFieldNameSignUp.setText("");
        this.textFieldPassSignUp.setText("");
        this.textFieldConfPassSignUp.setText("");
        this.textFieldSurnameSignUp.setText("");
        this.textFieldVATNumber.setText("");
        this.imageFile = null;
        this.textFieldImage.setText("No image selected");
    }

    @FXML
    public void selectImage() {
        final FileChooser fc = new FileChooser();
        fc.setTitle("Select image");
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        this.imageFile = fc.showOpenDialog(new Stage());
        if (this.imageFile != null) this.textFieldImage.setText(this.imageFile.getName());
    }

    @FXML
    public void switchToUserSignUpPage() {
        this.textFieldVATNumber.setDisable(true);
        this.user.setUnderline(true);
        this.touristGuide.setUnderline(false);
    }

    @FXML
    public void switchToTGuideSignUpPage() {
        this.textFieldVATNumber.setDisable(false);
        this.user.setUnderline(false);
        this.touristGuide.setUnderline(true);
    }

    @FXML
    public void enterKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            this.signUp();
        }
    }
}
