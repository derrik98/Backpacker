package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.utils.Roles;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.FileInputStream;
import java.io.IOException;

public class TouristGuideGraphicChange extends GuiChangeTemplate {

    private static TouristGuideGraphicChange instance = null;

    TouristGuideGraphicChange(){
        whoAmI = Roles.TOURIST_GUIDE;
    }

    public static TouristGuideGraphicChange getInstance(){
        if(instance == null){
            instance = new TouristGuideGraphicChange();
        }
        return instance;
    }

    public void switchToHomePage(Scene scene) {
        this.catcher(() -> {
            FXMLLoader loader = new FXMLLoader();
            FileInputStream fileInputStream = new FileInputStream("src/main/logic/java/it/ispw/daniele/backpacker/view/fxml_view/Search-Page.fxml");
            Parent fxmlLoader = loader.load(fileInputStream);
            SearchViewController huc = loader.getController();
            scene.setRoot(fxmlLoader);
            huc.init();
        });
    }

    public void switchToAddItinerary(Scene scene) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream("src/main/logic/java/it/ispw/daniele/backpacker/view/fxml_view/Add-Itinerary-Page.fxml");
        Parent fxmlLoader = loader.load(fileInputStream);
        AddItineraryViewController addItineraryViewController = loader.getController();
        scene.setRoot(fxmlLoader);
        addItineraryViewController.init();
    }

    public void switchToTGuideDet(Scene scene){
        this.catcher(() -> {
            FXMLLoader loader = new FXMLLoader();
            FileInputStream fileInputStream = new FileInputStream("src/main/logic/java/it/ispw/daniele/backpacker/view/fxml_view/TGuide-Details-Page.fxml");
            Parent fxmlLoader = loader.load(fileInputStream);
            TGuideDetailsController gdc = loader.getController();
            scene.setRoot(fxmlLoader);
            gdc.init();
        });
    }
}
