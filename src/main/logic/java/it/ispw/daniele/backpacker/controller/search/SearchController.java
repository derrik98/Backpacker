package it.ispw.daniele.backpacker.controller.search;

import it.ispw.daniele.backpacker.bean.SearchBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.entity.Itinerary;
import it.ispw.daniele.backpacker.exceptions.AddressNotFoundException;
import it.ispw.daniele.backpacker.exceptions.CityNotFoundException;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.exceptions.MonumentNotFoundException;
import it.ispw.daniele.backpacker.utils.Controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SearchController extends Controller {

    public void checkInput(SearchBean bean) throws CityNotFoundException, AddressNotFoundException, MonumentNotFoundException, IOException {

        JSONFactory checkCityCountry = new CityFromCountry();
        checkCityCountry.getJSON(bean);

        JSONFactory checkAddressCity = new AddressFromCity();
        checkAddressCity.getJSON(bean);

        MonumentFromAddress monuments = new MonumentFromAddress();
        monuments.getJSON(bean);


//        if (checkCityCountry.getJSON(bean)) {
//            JSONFactory checkAddressCity = new AddressFromCity();
//            if (checkAddressCity.getJSON(bean)) {
//                MonumentFromAddress monuments = new MonumentFromAddress();
//                monuments.getJSON(bean);
//            }
//        }
    }

    public List<ItineraryBean> createItinerary(SearchBean searchBean) throws GenericException, MonumentNotFoundException, IOException {

        MonumentFromAddress monuments = new MonumentFromAddress();
        List<String> result = monuments.getMonuments(searchBean);

        //monuments.getJSON(searchBean);

        ArrayList<Itinerary> it = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            Random rand;
            try {
                rand = SecureRandom.getInstanceStrong();
            } catch (NoSuchAlgorithmException e) {
                throw new GenericException("Search error");
            }

            StringBuilder vector = new StringBuilder();

            for (int j = 0; j <= 5; j++) {

                int index = rand.nextInt((result.size() - 1) + 1);

                if (!vector.toString().contains(result.get(index)) && j != 0) {
                    vector.append("/").append(result.get(index));
                } else {
                    vector.append(result.get(index));
                }
            }

            Itinerary itinerary;

            itinerary = new Itinerary("", "", "", "", 0, 0, vector.toString());
            itinerary.setId(i);

            it.add(itinerary);
        }
        return this.convert(it);
    }

}