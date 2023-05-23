package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Stack;

public class TouristGuideMenuBarController {

    @FXML
    private Label labelHome = new Label();
    @FXML
    public Label labelResult = new Label();
    @FXML
    private Label labelProfile = new Label();
    @FXML
    public Label labelAddItinerary = new Label();
    @FXML
    public ImageView imageUndo;

    protected static Stack<String> stackScene = new Stack<>();

    private TouristGuideGraphicChange guideGraphicChange;

    @FXML
    public void switchToHome() {
        this.guideGraphicChange.switchToHomePage(this.labelHome.getScene());
        stackScene.push("home");
    }
    @FXML
    public void switchToResult() {
        this.guideGraphicChange.switchToResult(this.labelResult.getScene());
        stackScene.push("result");
    }
    @FXML
    public void switchToTGuideDetails() {
        this.guideGraphicChange.switchToTGuideDet(this.labelProfile.getScene());
        stackScene.push("profile");
    }

    @FXML
    public void switchToAddItinerary() throws IOException {
        this.guideGraphicChange.switchToAddItinerary(this.labelAddItinerary.getScene());
        stackScene.push("addItinerary");
    }

    @FXML
    public void logout(){
        stackScene.empty();
        SessionUser.getInstance().closeSession();
        this.guideGraphicChange.switchToLogin(this.labelHome.getScene());
    }

    public void undoScene() throws IOException {

        if (stackScene.size() > 1) {
            String from = stackScene.get(stackScene.size() - 2);
            switch (from) {
                case "home" -> this.guideGraphicChange.switchToHomePage(this.labelHome.getScene());
                case "result" -> this.guideGraphicChange.switchToResult(this.labelResult.getScene());
                case "profile" -> this.guideGraphicChange.switchToTGuideDet(this.labelProfile.getScene());
                case "addItinerary" -> this.guideGraphicChange.switchToAddItinerary(this.labelAddItinerary.getScene());
            }

            stackScene.remove(stackScene.size() - 1);

        } else {
            this.guideGraphicChange.switchToHomePage(this.imageUndo.getScene());
        }

    }

    public void init(String selected) {

        this.guideGraphicChange = TouristGuideGraphicChange.getInstance();

        if(stackScene.isEmpty()){
            stackScene.push("home");
        }

        String style = "-fx-underline: true;";

        switch (selected){
            case "home" -> this.labelHome.setStyle(style);
            case "profile" -> this.labelProfile.setStyle(style);
            case "result" -> this.labelResult.setStyle(style);
            case "addItinerary" -> this.labelAddItinerary.setStyle(style);
            default -> {
            }
        }
    }
}