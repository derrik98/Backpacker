package itinerary.addItinerary;

import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.controller.add_itinerary.AddItineraryController;
import it.ispw.daniele.backpacker.exceptions.DateException;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAddItineraryController {

    @Test
    public void tesMissingInput() { //test for AddItineraryController class

        String output;
        String attended;
        String guideId;
        String location;
        String date;
        String time;
        int participants;
        int price;
        String steps;

        ItineraryBean ib;

        ib = new ItineraryBean();

        AddItineraryController aic = new AddItineraryController("gui");

        output = "";
        attended = "Data missing";

        guideId = "";
        location = "Roma";
        date = "2023-07-20";
        time = "9:00";
        participants = 30;
        price = 25;
        steps = "Colosseo-Pantheon-Piazza Navona";
        ib.setGuideId(guideId);
        ib.setLocation(location);
        ib.setDate(date);
        ib.setTime(time);
        ib.setParticipants(participants);
        ib.setPrice(price);
        ib.setSteps(steps);

        try {
            aic.addItinerary(ib);
        } catch (DateException | GenericException e) {
            output = e.getMessage();
        } finally {
            assertEquals(attended, output);
        }
    }

}
