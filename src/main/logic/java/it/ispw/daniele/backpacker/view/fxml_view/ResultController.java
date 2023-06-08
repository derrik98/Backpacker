package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.bean.SearchBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.booktour.BookTourController;
import it.ispw.daniele.backpacker.controller.search.SearchController;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.exceptions.MonumentNotFoundException;
import it.ispw.daniele.backpacker.utils.Controller;
import it.ispw.daniele.backpacker.utils.Roles;
import it.ispw.daniele.backpacker.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class ResultController  extends Controller {
    @FXML
    public Text suggestedItinerary = new Text();
    @FXML
    public VBox vBoxResultGuide = new VBox();
    @FXML
    public Text selfItinerary = new Text();
    @FXML
    public ImageView guideImage = new ImageView();
    @FXML
    public StackPane stackPaneResult;
    @FXML
    public HBox hBoxInput = new HBox();
    @FXML
    public ImageView guideOff;
    @FXML
    public VBox vBoxDynamic = new VBox();
    @FXML
    private Label countrySearch = new Label();
    @FXML
    private Label citySearch = new Label();
    @FXML
    private Label addressSearch = new Label();
    @FXML
    private Label isRestaurant = new Label();
    @FXML
    private Label radiusSearch = new Label();
    @FXML
    private VBox vBoxResult = new VBox();
    @FXML
    private HBox menuBar = new HBox();

    private static final String HOME = "home";
    private static final String RESULT = "result";

    public void init() throws GenericException {

        if(SessionUser.getInstance().getSession().getRole().equals(Roles.TOURIST_GUIDE.name().toLowerCase())) {
            TouristGuideGraphicChange i = TouristGuideGraphicChange.getInstance();
            i.menuBar(this.menuBar, RESULT);
        }
        else {
            UserGraphicChange ugc = UserGraphicChange.getInstance();
            ugc.menuBar(this.menuBar, RESULT);
        }

        SearchBean searchBean = SessionUser.getInstance().getSearchSession();

        if (searchBean != null) {
            this.countrySearch.setText(searchBean.getCountry());
            this.citySearch.setText(searchBean.getCity());
            this.addressSearch.setText(searchBean.getAddress());
            this.isRestaurant.setText(searchBean.isRestaurant());
            this.radiusSearch.setText(searchBean.getRange());
        }
        else {
            this.countrySearch.setText("");
            this.citySearch.setText("");
            this.addressSearch.setText("");
            this.isRestaurant.setText("");
            this.radiusSearch.setText("");
            hBoxInput.setVisible(false);
            selfItinerary.setVisible(false);
            guideOff.setVisible(false);

            hBoxInput.getChildren().removeAll();
            Hyperlink link = new Hyperlink("Start from the Home-Page");
            link.setOnMouseClicked(mouseEvent -> {
                if(SessionUser.getInstance().getSession().getRole().equals(Roles.TOURIST_GUIDE.name().toLowerCase())) {
                    TouristGuideGraphicChange ggc = TouristGuideGraphicChange.getInstance();
                    ggc.switchToHomePage(this.menuBar.getScene());
                    ggc.menuBar(this.menuBar, HOME);
                }
                else {
                    UserGraphicChange ugc = UserGraphicChange.getInstance();
                    ugc.switchToHomePage(this.menuBar.getScene());
                    ugc.menuBar(this.menuBar, HOME);
                }
            });

            vBoxDynamic.getChildren().add(0, link);
            return;
        }

        BookTourController btc = new BookTourController("gui");
        List<ItineraryBean> sItinerary;
        sItinerary = btc.getItinerary(citySearch.getText(), "city");

        if(sItinerary == null){
            suggestedItinerary.setText("Suggested Itinerary: EMPTY");
        }
        else{
            suggestedItinerary.setText("Suggested Itinerary");
            guideImage.setImage(new Image("guideOn.png"));
            guideImage.setFitHeight(50);
            guideImage.setFitHeight(50);

            Accordion accordionSuggested = this.createTable(sItinerary, "suggested", RESULT, this.stackPaneResult);
            vBoxResultGuide.getChildren().add(accordionSuggested);
        }

        SearchController sc = new SearchController();
        List<ItineraryBean> iti;
        try {
            iti = sc.createItinerary(searchBean);
        } catch (GenericException | MonumentNotFoundException e) {
            throw new GenericException("ERROR");
        }

        if(iti == null){
            selfItinerary.setText("Self Itinerary: EMPTY");
        }
        else {
            selfItinerary.setText("Self Itinerary");

            Accordion accordionSelf = this.createTable(iti, "self", RESULT, null);
            vBoxResult.getChildren().add(accordionSelf);
        }
    }

}