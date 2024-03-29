package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.bean.SearchBean;
import it.ispw.daniele.backpacker.exceptions.AddressNotFoundException;
import it.ispw.daniele.backpacker.exceptions.CityNotFoundException;
import it.ispw.daniele.backpacker.exceptions.EmptyFieldException;
import it.ispw.daniele.backpacker.exceptions.MonumentNotFoundException;
import it.ispw.daniele.backpacker.utils.Roles;
import it.ispw.daniele.backpacker.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SearchViewController {

    @FXML
    private Text errorText;
    @FXML
    private HBox menuBar = new HBox();
    @FXML
    private Slider sliderRange;
    @FXML
    private Label labelRange;
    @FXML
    private TextField textFieldCountry;
    @FXML
    private TextField textFieldCity;
    @FXML
    private TextField textFieldAddress;
    @FXML
    private RadioButton radioButtonRestaurant;

    @FXML
    public void enterKeyPressed(KeyEvent keyEvent) throws  IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            this.searchItinerary();
        }
    }

    public void onSliderChanged() {
        BigDecimal sliderValue = BigDecimal.valueOf(sliderRange.getValue()).setScale(1, RoundingMode.HALF_UP);
        labelRange.setText(sliderValue.setScale(1, RoundingMode.HALF_UP) + " km");
    }

    public void searchItinerary() throws IOException {

        SearchBean searchBean = new SearchBean();
        searchBean.setCountry(this.textFieldCountry.getText());
        searchBean.setCity(this.textFieldCity.getText());
        searchBean.setAddress(this.textFieldAddress.getText());
        searchBean.setRestaurant(this.radioButtonRestaurant.getText());
        searchBean.setRange(String.valueOf(BigDecimal.valueOf(sliderRange.getValue()).setScale(1, RoundingMode.HALF_UP)));

        try {
            if (textFieldCountry.getText().equals("") || textFieldCity.getText().equals("") || textFieldAddress.getText().equals("")) {
                throw new EmptyFieldException("Missing inputs");
            }

            SessionUser.getInstance().setSearchSession(searchBean);

            it.ispw.daniele.backpacker.controller.search.SearchController sc = new it.ispw.daniele.backpacker.controller.search.SearchController();
            sc.checkInput(searchBean);
            UserGraphicChange.getInstance().switchToResult(this.textFieldCountry.getScene());

        } catch (CityNotFoundException | EmptyFieldException | AddressNotFoundException | MonumentNotFoundException exception) {
            this.errorText.setText(exception.getMessage());
        }
    }

    public void init() {
        if(SessionUser.getInstance().getSession().getRole().equals(Roles.TOURIST_GUIDE.name().toLowerCase())) {
            TouristGuideGraphicChange tGuideGraphicChange = TouristGuideGraphicChange.getInstance();
            tGuideGraphicChange.menuBar(this.menuBar, "home");
        }
        else {
            UserGraphicChange ugc = UserGraphicChange.getInstance();
            ugc.menuBar(this.menuBar, "home");
        }
    }

}