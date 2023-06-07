package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class MenuBarController {
    @FXML
    public AnchorPane apMenuBar = new AnchorPane();
    @FXML
    private Label labelHome = new Label();
    @FXML
    private Label labelResult = new Label();
    @FXML
    private Label labelProfile = new Label();
    @FXML
    public ImageView imageUndo;

    private static final String HOME = "home";
    private static final String RESULT = "result";
    private static final String PROFILE = "profile";

    private static final ArrayList<String> stackScene = new ArrayList<>();

    private UserGraphicChange ugc;

    @FXML
    public void switchToHome() {
        this.ugc.switchToHomePage(this.labelHome.getScene());
        stackScene.add(HOME);
    }

    @FXML
    public void switchToResult() {
        this.ugc.switchToResult(this.labelResult.getScene());
        stackScene.add(RESULT);
    }

    @FXML
    public void switchToUserDet(){
        this.ugc.switchToUserDet(this.labelProfile.getScene());
        stackScene.add(PROFILE);
    }

    @FXML
    public void logout(){
        SessionUser.getInstance().closeSession();
        this.ugc.switchToLogin(this.labelHome.getScene());
    }


    @FXML
    public void undoScene() {

        if (stackScene.size() > 1) {
            String from = stackScene.get(stackScene.size() - 2);
            switch (from) {
                case HOME -> this.ugc.switchToHomePage(this.labelHome.getScene());
                case RESULT -> this.ugc.switchToResult(this.labelResult.getScene());
                case PROFILE -> this.ugc.switchToUserDet(this.labelProfile.getScene());
                default -> this.imageUndo.setDisable(true);
            }
            stackScene.remove(stackScene.size() - 1);

        } else {
            this.ugc.switchToHomePage(this.imageUndo.getScene());
        }

    }

    public void init(String selected) {
        this.ugc = UserGraphicChange.getInstance();

        if(stackScene.isEmpty()){
            stackScene.add(HOME);
        }

        String style = "-fx-underline: true;";

        switch (selected){
            case HOME -> this.labelHome.setStyle(style);
            case RESULT -> this.labelResult.setStyle(style);
            case PROFILE -> this.labelProfile.setStyle(style);
            default -> this.imageUndo.setDisable(false);
        }
    }
}
