package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.bean.TouristGuideBean;
import it.ispw.daniele.backpacker.booktour.BookTourController;
import it.ispw.daniele.backpacker.booktour.SaveTour;
import it.ispw.daniele.backpacker.dao.tourist_guide_dao.TouristGuideDao;
import it.ispw.daniele.backpacker.entity.TouristGuide;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.Controller;
import it.ispw.daniele.backpacker.utils.FileManager;
import it.ispw.daniele.backpacker.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TGuideDetailsController extends Controller {
    @FXML
    public HBox menuBar;
    @FXML
    public Label username;
    @FXML
    public Label name;
    @FXML
    public Label surname;
    @FXML
    public Label email;
    @FXML
    public Label vat;
    @FXML
    public ImageView imageSettings;
    @FXML
    public Text textSettings;
    @FXML
    public ImageView profilePicture;
    @FXML
    public Text textBookedItineraries;
    @FXML
    public VBox vBoxBooked;
    @FXML
    public Text textSavedItineraries;
    @FXML
    public VBox vBoxSaved;

    private static final String PROFILE = "profile";

    public void switchToSettings() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/it/ispw/daniele/backpacker/Edit-User-Details-Page.fxml");
        Parent fxmlLoader = loader.load(fileInputStream);
        Scene scene = this.imageSettings.getScene();
        scene.setRoot(fxmlLoader);
        //stackScene.push(fxmlLoader);
    }

    public void showInfoSettings() {
        textSettings.setVisible(true);
    }

    public void notShowInfoSettings() {
        textSettings.setVisible(false);
    }

    private TouristGuideBean getSearchUser(String caller) {
        TouristGuideDao ud = new TouristGuideDao();
        List<TouristGuide> l = null;
        try {
            l = ud.getSearchUser(caller);
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
        return this.convert(Objects.requireNonNull(l).get(0));
    }

    public void init() {

        TouristGuideGraphicChange tgc = new TouristGuideGraphicChange();
        tgc.menuBar(this.menuBar, PROFILE);

        TouristGuideBean tUsers = this.getSearchUser(SessionUser.getInstance().getSession().getUsername());

        this.username.setText(tUsers.getUsername());
        this.name.setText(tUsers.getName());
        this.email.setText(tUsers.getEmail());
        this.surname.setText(tUsers.getSurname());
        this.vat.setText(tUsers.getIdentificationCode());

        this.setImage(tUsers.getProfilePicture(), this.profilePicture);

        List<ItineraryBean> booked = null;
        try {
            booked = new BookTourController("gui").getItinerary(tUsers.getUsername(), "user");
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }

        List<ItineraryBean> saved = null;
        try {
            saved = new SaveTour("gui").getItinerary(tUsers.getUsername());
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }

        if(booked == null){
            textBookedItineraries.setText(textBookedItineraries.getText() + ": EMPTY");
        }
        else {
            Accordion accordionSuggested = this.createTable(booked, "suggested", PROFILE, null);
            vBoxBooked.getChildren().addAll(accordionSuggested);
        }
        if(saved == null){
            textSavedItineraries.setText(textSavedItineraries.getText() + ": EMPTY");
        }
        else {
            Accordion accordionSelf = this.createTable(saved, "self", PROFILE, null);
            vBoxSaved.getChildren().addAll(accordionSelf);
        }

    }

}
