package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.bean.UserBean;
import it.ispw.daniele.backpacker.controller.book_tour.BookTourController;
import it.ispw.daniele.backpacker.controller.book_tour.SaveItinerary;
import it.ispw.daniele.backpacker.dao.user_dao.UserDao;
import it.ispw.daniele.backpacker.entity.User;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.SessionUser;
import it.ispw.daniele.backpacker.view.utils_view.GuiController;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Objects;

public class UserDetailsController extends GuiController {

    @FXML
    public ImageView profilePicture;
    @FXML
    public Text textBookedItineraries;
    @FXML
    public VBox vBoxBooked;
    @FXML
    public VBox vBoxSaved;
    @FXML
    public Text textSavedItineraries;
    @FXML
    private Text errorText;
    @FXML
    private Text textSettings;
    @FXML
    private Label username;
    @FXML
    private Label name = new Label();
    @FXML
    private Label surname;
    @FXML
    private Label email;
    @FXML
    private HBox menuBar = new HBox();

    private static final String PROFILE = "profile";

    public void showInfoSettings() {
        textSettings.setVisible(true);
    }

    public void notShowInfoSettings() {
        textSettings.setVisible(false);
    }

    private UserBean getSearchUser(String caller) {
        UserDao ud = new UserDao();
        List<User> l = null;
        try {
            l = ud.getSearchUser(caller);
        } catch (GenericException e) {
            this.errorText.setText(e.getMessage());
        }
        return this.convert(Objects.requireNonNull(l).get(0));
    }

    public void init() {

        UserGraphicChange ugc = UserGraphicChange.getInstance();
        ugc.menuBar(this.menuBar, PROFILE);

        UserBean users = this.getSearchUser(SessionUser.getInstance().getSession().getUsername());

        this.username.setText(users.getUsername());
        this.name.setText(users.getName());
        this.email.setText(users.getEmail());
        this.surname.setText(users.getSurname());

        this.setImage(users.getProfilePicture(), this.profilePicture);

        List<ItineraryBean> booked = null;
        try {
            booked = this.convert(new BookTourController("gui").getItinerary(users.getUsername(), "user"));
        } catch (GenericException e) {
            this.errorText.setText(e.getMessage());
        }

        List<ItineraryBean> saved = null;
        try {
            saved = this.convert(new SaveItinerary("gui").getItinerary(users.getUsername()));
        } catch (GenericException e) {
            this.errorText.setText(e.getMessage());
        }

        if(booked == null || booked.isEmpty()){
            this.textBookedItineraries.setText(this.textBookedItineraries.getText() + ": EMPTY");
        }
        else {
            Accordion accordionSuggested = this.createGuiTable(booked, "suggested", PROFILE, null);
            vBoxBooked.getChildren().addAll(accordionSuggested);
        }

        if(saved == null || saved.isEmpty()){
            this.textSavedItineraries.setText(this.textSavedItineraries.getText() + ": EMPTY");
        }
        else {
            Accordion accordionSelf = this.createGuiTable(saved, "self", PROFILE, null);
            vBoxSaved.getChildren().addAll(accordionSelf);
        }
    }

}
