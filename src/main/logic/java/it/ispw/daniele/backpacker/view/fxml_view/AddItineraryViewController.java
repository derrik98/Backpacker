package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.controller.add_itinerary.AddItineraryController;
import it.ispw.daniele.backpacker.exceptions.DateException;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.SessionUser;
import it.ispw.daniele.backpacker.view.utils_view.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class AddItineraryViewController extends Controller {

    @FXML
    public TextField textFieldLanguages;
    @FXML
    public TextField textFieldPrice;
    @FXML
    public TextField textFieldParticipants;
    @FXML
    public ListView<Object> listView = new ListView<>();
    @FXML
    private Text errorText;
    @FXML
    private HBox menuBar = new HBox();
    @FXML
    public TextField textFieldTime;
    @FXML
    public DatePicker fieldDate;
    @FXML
    public TextField textFieldCity;

    private GeneralUserBean guideBean;
    private AddItineraryController controller;
    private String steps = "";

    @FXML
    public void share() {

        for (int i = 0; i < listView.getItems().size() - 1; i++) {
            TextField t = (TextField) listView.getItems().get(i);
            if (!t.getText().equals("") && t.getText() != null) {
                this.steps = this.steps.concat(t.getText() + "-");
            }
        }

        String location = this.textFieldCity.getText();
        String date = "";
        String time = this.textFieldTime.getText();
        String participants = this.textFieldParticipants.getText();
        String price = this.textFieldPrice.getText();

        if (this.fieldDate.getValue() != null) {
            date = this.fieldDate.getValue().toString();
        }

        if (this.guideBean.getUsername().equals("") || location.equals("") || date.equals("") || time.equals("") || participants.equals("") || price.equals("") || this.steps.equals("")) {
            this.errorText.setText("Data missing");
        } else {

            try {
                ItineraryBean itineraryBean = this.setItineraryBean(this.guideBean.getUsername(), location, date, time, Integer.parseInt(participants), Integer.parseInt(price), this.steps);
                this.controller.addItinerary(itineraryBean);
                itineraryBean.setItineraryId(controller.getItineraryId(itineraryBean));

                this.errorText.setText("Itinerary added successfully");
            } catch (DateException | GenericException | SQLException | FileNotFoundException | ClassNotFoundException | NumberFormatException e) {
                this.errorText.setText(e.getMessage());
            }

        }
    }

    public void init() {

        TouristGuideGraphicChange guideGraphicChange = TouristGuideGraphicChange.getInstance();
        guideGraphicChange.menuBar(this.menuBar, "addItinerary");

        fieldDate.setStyle("-fx-font-size: 20");

        this.controller = new AddItineraryController("gui");

        for (int i = 1; i < 10; i++) {
            TextField textField = new TextField();
            textField.setStyle("-fx-font-size: 20");
            textField.setId(String.valueOf(i));
            textField.setPromptText("step -> " + i);

            listView.getItems().add(textField);
        }

        this.guideBean = SessionUser.getInstance().getSession();

    }
}
