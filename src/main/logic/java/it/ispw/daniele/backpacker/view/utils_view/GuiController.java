package it.ispw.daniele.backpacker.view.utils_view;

import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.controller.book_tour.BookTourController;
import it.ispw.daniele.backpacker.controller.book_tour.SaveItinerary;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.FileManager;
import it.ispw.daniele.backpacker.utils.SessionUser;
import it.ispw.daniele.backpacker.view.fxml_view.UserGraphicChange;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Accordion;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuiController extends Controller {

    private static final String STYLE = "Arial";

    protected Accordion createGuiTable(List<ItineraryBean> itineraryBeanList, String type, String from, StackPane stackPane) {

        Accordion accordion = new Accordion();

        for (ItineraryBean itineraryBean : itineraryBeanList) {

            String[] steps = itineraryBean.getSteps().split("/");
            ArrayList<String> als = new ArrayList<>();

            for (int i = 0; i < steps.length; i++) {
                als.add(i, steps[i]);
            }

            TitledPane titledPane = new TitledPane();

            titledPane.setAlignment(Pos.CENTER);

            HBox contentPane = new HBox();
            contentPane.setAlignment(Pos.CENTER);

            contentPane.setPadding(new Insets(0, 10, 0, 35));

            contentPane.minWidthProperty().bind(titledPane.widthProperty());

            this.setLabelItinerary(als, contentPane);

            Label output = new Label();
            this.setLabelOutput(output, contentPane);

            if (type.equals("suggested") && !from.equals("profile")) {

                //Setting of buy image
                this.setIvBuy(titledPane, itineraryBean, stackPane, contentPane);
            }

            //Setting of delete image
            if (from.equals("profile")) {
                this.setIvDelete(titledPane, itineraryBean, accordion, contentPane, output, type);
            }

            //Setting of save image
            else {
                this.setIvSave(titledPane, itineraryBean, contentPane, output);
            }

            this.goToMap(als, titledPane);

            titledPane.setGraphic(contentPane);
            accordion.getPanes().add(titledPane);
        }
        return accordion;
    }

    private void setLabelItinerary(ArrayList<String> arrayList, HBox hBox) {

        for (int indexMonument = 0; indexMonument < arrayList.size(); indexMonument++) {

            Label label = new Label(" " + arrayList.get(indexMonument) + " ");

            if (indexMonument != 0) {

                Label space = new Label(" - ");
                space.setFont(new Font(STYLE, 14));
                space.setPrefWidth(Control.USE_COMPUTED_SIZE);

                hBox.getChildren().add(space);

                label.setCursor(Cursor.HAND);

            }

            label.setFont(new Font(STYLE, 14));
            label.setPrefWidth(Control.USE_COMPUTED_SIZE);
            hBox.getChildren().add(label);
        }
    }

    private void setLabelOutput(Label output, HBox hBox) {

        HBox region = new HBox();
        region.setMinWidth(15);
        region.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(region, Priority.ALWAYS);

        output.setFont(new Font(STYLE, 14));
        output.setPrefWidth(Control.USE_COMPUTED_SIZE);
        hBox.getChildren().addAll(region, output);
    }

    private void setIvBuy(TitledPane titledPane, ItineraryBean itineraryBean, StackPane stackPane, HBox contentPane) {

        HBox region1 = new HBox();
        region1.setMinWidth(15);
        region1.setMaxWidth(Double.MAX_VALUE);

        ImageView ivBuy;
        ivBuy = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/buy.png")).toExternalForm()));
        ivBuy.setFitWidth(40);
        ivBuy.setFitHeight(40);
        ivBuy.setCursor(Cursor.HAND);

        ivBuy.setOnMouseClicked(mouseEvent -> {

            titledPane.setExpanded(false);

            UserGraphicChange ugc = UserGraphicChange.getInstance();
            ugc.switchToItineraryDet(itineraryBean, stackPane);

        });

        contentPane.getChildren().addAll(region1, ivBuy);
    }

    private void setIvDelete(TitledPane titledPane, ItineraryBean itineraryBean, Accordion accordion, HBox hBox, Label output, String type) {

        HBox region2 = new HBox();
        region2.setMinWidth(15);
        region2.setMaxWidth(Double.MAX_VALUE);

        ImageView ivDelete;
        ivDelete = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/bin.png")).toExternalForm()));

        ivDelete.setOnMouseClicked(mouseEvent -> {

            titledPane.setExpanded(false);
            try {

                this.deleteItinerary(itineraryBean, type);

                accordion.getPanes().remove(titledPane);
                output.setText("Deleted");
            } catch (Exception e) {
                output.setText("Error");
            }
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> output.setText(""));
            pause.play();
        });

        ivDelete.setCursor(Cursor.HAND);
        ivDelete.setFitWidth(35);
        ivDelete.setFitHeight(35);

        hBox.getChildren().addAll(region2, ivDelete);
    }

    private void setIvSave(TitledPane titledPane, ItineraryBean itineraryBean, HBox hBox, Label output) {

        HBox region2 = new HBox();
        region2.setMinWidth(15);
        region2.setMaxWidth(Double.MAX_VALUE);

        ImageView ivSave;
        ivSave = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/save.png")).toExternalForm()));

        ivSave.setOnMouseClicked(mouseEvent -> {
            titledPane.setExpanded(false);
            try {
                this.saveItinerary(itineraryBean);

                output.setText("Saved");

            } catch (Exception e) {
                output.setText("Error");
            }
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> output.setText(""));
            pause.play();
        });

        ivSave.setCursor(Cursor.HAND);
        ivSave.setFitWidth(35);
        ivSave.setFitHeight(35);

        hBox.getChildren().addAll(region2, ivSave);
    }

    private void goToMap(ArrayList<String> als, TitledPane titledPane) {

        WebView webView = new WebView();
        webView.setMinHeight(550);

        String s = als.get(0).replace(" - ", "/");
        StringBuilder url = new StringBuilder("https://google.it/maps/dir/" + s);

        for (String element : als) {

            url.append("/").append(element);
        }

        webView.getEngine().load(url.toString());

        VBox v = new VBox(webView);
        titledPane.setContent(v);

    }

    private void saveItinerary(ItineraryBean itineraryBean) throws GenericException {

        SaveItinerary st = new SaveItinerary("gui");
        st.saveItinerary(SessionUser.getInstance().getSession(), itineraryBean);

    }

    private void deleteItinerary(ItineraryBean itineraryBean, String type) throws GenericException {

        if (type.equals("suggested")) {
            BookTourController btc = new BookTourController("gui");
            btc.removeParticipation(SessionUser.getInstance().getSession(), itineraryBean);
        } else {
            SaveItinerary st = new SaveItinerary("gui");
            st.removeItinerary(SessionUser.getInstance().getSession(), itineraryBean);

        }
    }

    protected void setImage(String user, ImageView profilePicture) {
        File file = new File(FileManager.PROFILE + File.separator + user);
        Image image = new Image(file.toURI().toString());
        profilePicture.setImage(image);
        profilePicture.setFitHeight(150);
        profilePicture.setFitWidth(150);
    }

}
