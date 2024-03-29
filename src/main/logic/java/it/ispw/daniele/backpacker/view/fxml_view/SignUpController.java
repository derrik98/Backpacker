package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.bean.TouristGuideBean;
import it.ispw.daniele.backpacker.bean.UserBean;
import it.ispw.daniele.backpacker.controller.login.LoginController;
import it.ispw.daniele.backpacker.exceptions.EmptyFieldException;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.Roles;
import it.ispw.daniele.backpacker.view.utils_view.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SignUpController extends Controller {

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
        if (user.isUnderline()) {
            userType = "USER";
        } else {
            userType = "TOURIST_GUIDE";
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

        String name = this.textFieldNameSignUp.getText();
        String surname = this.textFieldSurnameSignUp.getText();

        if (userType.equals(Roles.USER.name())) {
            UserBean ub = this.setUserBean(username, name, surname, email, password, newFileName);


            try {
                regResult = lc.createUser(ub, "gui");
            } catch (EmptyFieldException | GenericException exception) {
                this.errorText.setText(exception.getMessage());
            }

        } else {
            vatNumb = textFieldVATNumber.getText();
            TouristGuideBean tgb = this.setTouristGuideBean(username, name, surname, email, password, newFileName, vatNumb);
            try {
                regResult = lc.createTouristGuide(tgb, "gui");
            } catch (EmptyFieldException | GenericException exception) {
                this.errorText.setText(exception.getMessage());
            }
        }

        this.errorText.setText("");

        if (Boolean.TRUE.equals(regResult)) {

            try {
                this.setImage(this.imageFile, fileName, newFileName);
                this.errorText.setText("User successfully registered");
            } catch (GenericException e) {
                this.errorText.setText(e.getMessage());
            }
        } else {
            this.errorText.setText("Unable to sign up");
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
        user.setUnderline(true);
        touristGuide.setUnderline(false);
    }

    @FXML
    public void switchToTGuideSignUpPage() {
        this.textFieldVATNumber.setDisable(false);
        user.setUnderline(false);
        touristGuide.setUnderline(true);
    }

    @FXML
    public void enterKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            this.signUp();
        }
    }
}
