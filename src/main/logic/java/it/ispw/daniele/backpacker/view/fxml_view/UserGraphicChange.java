package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.utils.Roles;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.io.FileInputStream;

public class UserGraphicChange extends GuiChangeTemplate {

    private static UserGraphicChange instance = null;

    private UserGraphicChange() {
        whoAmI = Roles.USER;
    }

    public static UserGraphicChange getInstance() {
        if (instance == null) {
            instance = new UserGraphicChange();
        }
        return instance;
    }

    @Override
    public void switchToHomePage(Scene scene) {
        this.catcher(() -> {
            FXMLLoader loader = new FXMLLoader();
            FileInputStream fileInputStream = new FileInputStream("src/main/logic/java/it/ispw/daniele/backpacker/view/fxml_view/Home-Page.fxml");
            Parent fxmlLoader = loader.load(fileInputStream);
            HomeUserController huc = loader.getController();
            scene.setRoot(fxmlLoader);
            huc.init();
        });
    }

    public void switchToUserDet(Scene scene) {
        this.catcher(() -> {
            FXMLLoader loader = new FXMLLoader();
            FileInputStream fileInputStream = new FileInputStream("src/main/logic/java/it/ispw/daniele/backpacker/view/fxml_view/User-Details-Page.fxml");
            Parent fxmlLoader = loader.load(fileInputStream);
            UserDetailsController pc = loader.getController();
            scene.setRoot(fxmlLoader);
            pc.init();
        });
    }

    public void switchToItineraryDet(ItineraryBean itineraryBean, StackPane stackPane) {
        this.catcher(() -> {
            FXMLLoader loader = new FXMLLoader();
            FileInputStream fileInputStream = new FileInputStream("src/main/logic/java/it/ispw/daniele/backpacker/view/fxml_view/ItineraryDetails-Page.fxml");
            Parent fxmlLoader = loader.load(fileInputStream);
            ItineraryDetailsController idc = loader.getController();
            idc.init(itineraryBean);

            stackPane.getChildren().add(fxmlLoader);
            stackPane.getChildren().get(0).setDisable(true);

        });
    }
}
