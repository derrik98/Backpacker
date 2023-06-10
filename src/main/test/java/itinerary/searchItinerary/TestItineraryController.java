package itinerary.searchItinerary;

import it.ispw.daniele.backpacker.bean.SearchBean;
import it.ispw.daniele.backpacker.controller.search.CityFromCountry;
import it.ispw.daniele.backpacker.exceptions.CityNotFoundException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestItineraryController {

    @Test
    public void tesIncorrectInput() { //test for SearchController class

        CityFromCountry cfc;
        String output;
        String attended;
        String country;
        String city;
        String address;
        SearchBean sb;

        sb = new SearchBean();
        cfc = new CityFromCountry();
        output = "";
        attended = "City not found in this country";
        country = "italia";
        city = "madrid";
        address = "via del corso";
        sb.setCountry(country);
        sb.setCity(city);
        sb.setAddress(address);

        try {
            cfc.getJSON(sb);
        } catch (IOException | CityNotFoundException e) {
            output = e.getMessage();
        } finally {
            assertEquals(attended, output);
        }
    }
}
