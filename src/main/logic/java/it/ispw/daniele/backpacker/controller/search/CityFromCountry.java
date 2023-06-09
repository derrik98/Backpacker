package it.ispw.daniele.backpacker.controller.search;

import it.ispw.daniele.backpacker.bean.SearchBean;
import it.ispw.daniele.backpacker.exceptions.CityNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class CityFromCountry extends JSONFactory{

    private static CityFromCountry instance = null;

    public static CityFromCountry getInstance() {
        if(instance==null) {
            instance = new CityFromCountry();
        }
        return instance;
    }

    @Override
    public boolean getJSON(SearchBean searchBean) throws CityNotFoundException, IOException {
        JSONObject json;

        json = readJsonFromUrl("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
                + searchBean.getCity() + "&types=(cities)&language=it&key=" + System.getProperty("google_api"));

        JSONArray ja = (JSONArray) json.get("predictions");
        JSONObject o = (JSONObject) ja.getJSONObject(0).get("structured_formatting");
        String s = (String) o.get("secondary_text");

        String upperCase;
        upperCase = String.valueOf(upperCase(searchBean.getCountry()));

        if (s.contains(searchBean.getCountry()) || s.contains(upperCase)) {
            return true;
        }
        else{
            throw new CityNotFoundException("City not found in this country");
        }
    }
}
