package itinerary;

import it.ispw.daniele.backpacker.bean.HomeBean;
import it.ispw.daniele.backpacker.controller.search.SearchController;
import it.ispw.daniele.backpacker.exceptions.AddressNotFoundException;
import it.ispw.daniele.backpacker.exceptions.CityNotFoundException;
import it.ispw.daniele.backpacker.exceptions.MonumentNotFoundException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestItineraryController {

    @Test
    public void tesIncorrectInput() {

        SearchController sc;
        String output;
        String attended;
        String country;
        String city;
        String address;
        HomeBean hb;

        hb = new HomeBean();
        sc = new SearchController();
        output = "";
        attended = "City not found in this country";
        country = "italia";
        city = "madrid";
        address = "via del corso";
        hb.setCountry(country);
        hb.setCity(city);
        hb.setAddress(address);

        try {
            sc.checkInput(hb);
        } catch (CityNotFoundException | IOException | AddressNotFoundException | MonumentNotFoundException e) {
            output = e.getMessage();
        } finally {
            assertEquals(attended, output);
        }
    }
}
