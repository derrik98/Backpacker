package it.ispw.daniele.backpacker.controller.search;

import it.ispw.daniele.backpacker.bean.SearchBean;
import it.ispw.daniele.backpacker.exceptions.AddressNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;

public class AddressFromCity extends JSONFactory{

    private static AddressFromCity instance = null;

    public static synchronized AddressFromCity getInstance() {
        if(instance==null) {
            instance = new AddressFromCity();
        }
        return instance;
    }

    @Override
    public boolean getJSON(SearchBean searchBean) throws AddressNotFoundException, IOException {

        JSONObject json;

        json = readJsonFromUrl("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + convertString(searchBean.getAddress()) + "&types=geocode&key=" + System.getProperty("google_api") + "&language=it");
        
        if(!json.getString("status").equals("OK")) {
            throw new AddressNotFoundException("Questa via non è presente in questo città");
        }

        JSONArray a = (JSONArray) json.get("predictions");
        JSONObject o = a.getJSONObject(0);
        String s = (String) o.get("description");

        String upperCase;
        upperCase = String.valueOf(upperCase(searchBean.getCity()));

        if (s.contains(searchBean.getCity()) || s.contains(upperCase)) {
            return true;
        }
        else{
            throw new AddressNotFoundException("Address not found in this city");
        }
    }
}
