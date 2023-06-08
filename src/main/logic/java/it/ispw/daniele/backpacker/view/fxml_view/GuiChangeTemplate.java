package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.exceptions.MonumentNotFoundException;
import it.ispw.daniele.backpacker.utils.Roles;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GuiChangeTemplate {

    protected final Logger logger = Logger.getLogger("GraphicChange");

    protected Roles whoAmI;

    public void catcher(GuiAction guiAction) {
        try {
            guiAction.action();
        } catch (IOException | MonumentNotFoundException | GenericException ioException) {
            logger.log(Level.WARNING, ioException.toString(), ioException.getCause());
        }
    }

    @FXML
    public void switchToLogin(Scene scene) {
        this.catcher(() -> {
            FXMLLoader loader = new FXMLLoader();
            FileInputStream fileInputStream = new FileInputStream("src/main/logic/java/it/ispw/daniele/backpacker/view/fxml_view/LoginView-Page.fxml");
            Parent fxmlLoader = loader.load(fileInputStream);
            LoginViewController lvc = loader.getController();
            scene.setRoot(fxmlLoader);
            lvc.init();
        });
    }

    public void switchToResult(Scene scene) {
        this.catcher(() -> {
            FXMLLoader loader = new FXMLLoader();
            FileInputStream fileInputStream = new FileInputStream("src/main/logic/java/it/ispw/daniele/backpacker/view/fxml_view/Result-Page.fxml");
            Parent fxmlLoader = loader.load(fileInputStream);
            ResultController rc = loader.getController();
            scene.setRoot(fxmlLoader);
            rc.init();
        });
    }

    public void menuBar(HBox pos, String sel) {
        this.catcher(() -> {

            FXMLLoader loader;

            if(whoAmI.equals(Roles.USER)){
                loader = new FXMLLoader();
                FileInputStream fileInputStream = new FileInputStream("src/main/logic/java/it/ispw/daniele/backpacker/view/fxml_view/MenuBar.fxml");
                Parent fxmlLoader = loader.load(fileInputStream);
                MenuBarController mbc = loader.getController();
                pos.getChildren().add(fxmlLoader);
                mbc.init(sel);
            }
            else {
                loader = new FXMLLoader();
                FileInputStream fileInputStream = new FileInputStream("src/main/logic/java/it/ispw/daniele/backpacker/view/fxml_view/TouristGuideMenuBar.fxml");
                Parent fxmlLoader = loader.load(fileInputStream);
                TouristGuideMenuBarController gbc = loader.getController();
                pos.getChildren().add(fxmlLoader);
                gbc.init(sel);
            }
        });
    }

    public abstract void switchToHomePage(Scene scene) throws IOException;

}

