package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.booktour.BookTourController;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ItineraryDetailsController {
    @FXML
    public Button subscribeButton;
    @FXML
    public ImageView closePageImage;
    @FXML
    public AnchorPane apDetails;
    @FXML
    public Text itineraryId = new Text();
    @FXML
    public Text guideId = new Text();
    @FXML
    public Text location = new Text();
    @FXML
    public Text date = new Text();
    @FXML
    public Text time = new Text();
    @FXML
    public Text participants = new Text();
    @FXML
    public Text price = new Text();
    @FXML
    public Text steps;

    private final BookTourController controller = new BookTourController("gui");
    private final GeneralUserBean sessionUser = SessionUser.getInstance().getSession();
    @FXML
    private Text errorText;
    private ItineraryBean ib = new ItineraryBean();

    @FXML
    public void closePage() {
        StackPane sp = (StackPane) apDetails.getParent();
        sp.getChildren().remove(1);
        sp.getChildren().get(0).setDisable(false);
    }

    @FXML
    public void subscribe() {

        boolean isPart;
        try {
            isPart = controller.isParticipating(this.sessionUser, ib);
            if (isPart) {
                controller.removeParticipation(this.sessionUser, ib);
                this.subscribeButton.setText("Subscribe");

            } else {
                controller.addParticipation(this.sessionUser, ib);
                this.subscribeButton.setText("Remove");

            }
        } catch (GenericException e) {
            this.errorText.setText("Error to subscribe");
        }

    }

    public void init(ItineraryBean itineraryBean) {

        this.itineraryId.setText(String.valueOf(itineraryBean.getItineraryId()));
        this.guideId.setText(itineraryBean.getGuideId());
        this.location = new Text(itineraryBean.getLocation());
        this.date.setText(itineraryBean.getDate());
        this.time.setText(itineraryBean.getTime());
        this.participants.setText(String.valueOf(itineraryBean.getParticipants()));
        this.price.setText(String.valueOf(itineraryBean.getPrice()));
        this.steps.setText(itineraryBean.getSteps().replace("/", " - "));
        this.errorText.setText("");

        this.ib = itineraryBean;
    }
}
