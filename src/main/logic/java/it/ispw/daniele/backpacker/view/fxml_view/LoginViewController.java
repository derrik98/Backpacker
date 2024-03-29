package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.controller.login.LoginController;
import it.ispw.daniele.backpacker.exceptions.EmptyFieldException;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.IOException;

public class LoginViewController {

    @FXML
    public Label labelLogin;
    @FXML
    public Label labelSignUp;
    @FXML
    public VBox dynamicZone;
    @FXML
    public Text errorText;
    @FXML
    public Button buttonLogin;
    @FXML
    private TextField textFieldUsername = new TextField();
    @FXML
    private TextField textFieldPassword = new TextField();

    @FXML
    public void loginButtonAction() {

        GeneralUserBean gub = new GeneralUserBean();
        gub.setUsername(this.textFieldUsername.getText());
        gub.setPassword(this.textFieldPassword.getText());

        LoginController controller = new LoginController();
        GeneralUserBean gu;
        try{
            gu = controller.login(gub, "gui");

            if(gu != null){
                String role = gu.getRole();

                //SET SESSION GENERAL USER
                SessionUser su = SessionUser.getInstance();
                su.setSession(gu);

                switch (role) {
                    case "user" -> UserGraphicChange.getInstance().switchToHomePage(this.textFieldUsername.getScene());
                    case "tourist_guide" -> TouristGuideGraphicChange.getInstance().switchToHomePage(this.textFieldUsername.getScene());
                    default -> this.errorText.setText("Unable to login");
                }
            }
        }catch (EmptyFieldException | GenericException exception){
            this.errorText.setText(exception.getMessage());
        }
    }

    public void switchToSignUp() throws IOException {
        SignUpController suc = new SignUpController();
        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream("src/main/logic/java/it/ispw/daniele/backpacker/view/fxml_view/SignUp-Page.fxml");
        Parent fxmlLoader = loader.load(fileInputStream);
        loader.setController(suc);

        dynamicZone.getChildren().remove(0, dynamicZone.getChildren().size());
        dynamicZone.getChildren().add(fxmlLoader);
        this.labelSignUp.setUnderline(true);
        this.labelLogin.setUnderline(false);
    }

    public void switchToLogin() throws IOException {
        LoginViewController lvc = new LoginViewController();
        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream("src/main/logic/java/it/ispw/daniele/backpacker/view/fxml_view/LoginView-Page.fxml");
        Parent fxmlLoader = loader.load(fileInputStream);
        loader.setController(lvc);
        this.labelLogin.getScene().setRoot(fxmlLoader);
        this.labelSignUp.setUnderline(false);
        this.labelLogin.setUnderline(true);

    }

    @FXML
    public void enterKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            this.loginButtonAction();
        }
    }

    public void init() {
        this.textFieldUsername.setText("");
        this.textFieldPassword.setText("");
    }
}