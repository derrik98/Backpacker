package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
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

    protected static ArrayList<String> stackScene = new ArrayList<>();

    private TouristGuideGraphicChange guideGraphicChange;

    private final String HOME = "home";
    private final String PROFILE = "profile";
    private final String RESULT = "result";
    private final String ADD_ITINERARY = "addItinerary";

    @FXML
    public void switchToHome() {
        this.guideGraphicChange.switchToHomePage(this.labelHome.getScene());
        stackScene.add(HOME);
    }
    @FXML
    public void switchToResult() {
        this.guideGraphicChange.switchToResult(this.labelResult.getScene());
        stackScene.add(RESULT);
    }
    @FXML
    public void switchToTGuideDetails() {
        this.guideGraphicChange.switchToTGuideDet(this.labelProfile.getScene());
        stackScene.add(PROFILE);
    }

    @FXML
    public void switchToAddItinerary() throws IOException {
        this.guideGraphicChange.switchToAddItinerary(this.labelAddItinerary.getScene());
        stackScene.add(ADD_ITINERARY);
    }

    @FXML
    public void logout(){
        SessionUser.getInstance().closeSession();
        this.guideGraphicChange.switchToLogin(this.labelHome.getScene());
    }

    public void undoScene() throws IOException {

        if (stackScene.size() > 1) {
            String from = stackScene.get(stackScene.size() - 2);
            switch (from) {
                case HOME -> this.guideGraphicChange.switchToHomePage(this.labelHome.getScene());
                case RESULT -> this.guideGraphicChange.switchToResult(this.labelResult.getScene());
                case PROFILE -> this.guideGraphicChange.switchToTGuideDet(this.labelProfile.getScene());
                case ADD_ITINERARY -> this.guideGraphicChange.switchToAddItinerary(this.labelAddItinerary.getScene());
                default -> this.imageUndo.setDisable(true);
            }

            stackScene.remove(stackScene.size() - 1);

        } else {
            this.guideGraphicChange.switchToHomePage(this.imageUndo.getScene());
        }

    }

    public void init(String selected) {

        this.guideGraphicChange = TouristGuideGraphicChange.getInstance();

        if(stackScene.isEmpty()){
            stackScene.add(HOME);
        }

        String style = "-fx-underline: true;";

        switch (selected){
            case HOME -> this.labelHome.setStyle(style);
            case PROFILE -> this.labelProfile.setStyle(style);
            case RESULT -> this.labelResult.setStyle(style);
            case ADD_ITINERARY -> this.labelAddItinerary.setStyle(style);
            default -> this.imageUndo.setDisable(false);
        }
    }
}