package it.ispw.daniele.backpacker.dao.ItineraryDao;

import it.ispw.daniele.backpacker.entity.Itinerary;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItineraryDaoL extends ItineraryDaoFactory {

    private final String path_itinerary = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/itinerary.json";

    @Override
    protected List<Itinerary> getItinerary(String city) {
        List<Itinerary> ret = this.execute(() -> {

            List<Itinerary> itineraryList = null;

            JSONParser parser = new JSONParser();

            try (FileReader fileReader = new FileReader(path_itinerary)) {

                JSONObject o = (JSONObject) parser.parse(fileReader);
                JSONArray arr = (JSONArray) o.get("itinerary");
                if (arr.isEmpty()) {
                    return Collections.emptyList();
                }

                for (int index = 0; index < arr.size(); index++) {

                    JSONObject object = (JSONObject) arr.get(index);

                    if (object.get("location").equals(city)) {

                        int id = (int) object.get(ID);
                        String guideId = (String) object.get(GUIDE_ID);
                        String location = (String) object.get(LOCATION);
                        String date = (String) object.get(DATE);
                        String time = (String) object.get(TIME);
                        int participants = (int) object.get(PARTICIPANTS);
                        int price = (int) object.get(PRICE);
                        String steps = (String) object.get(STEPS);

                        Itinerary itinerary = new Itinerary(id, guideId, location, date, time, participants, price, steps);

                        itineraryList.add(itinerary);

                        fileReader.close();

                        return itineraryList;
                    }
                }

            }
            return null;
        });
        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }

    @Override
    protected Boolean isParticipating(String username, int itineraryId) {
        Boolean ret = (Boolean) this.execute(() -> {
//            Connection conn = DatabaseUserConnection.getUserConnection();
//            String sql = "call backpacker.is_participating(?, ?);\r\n";

            JSONParser parser = new JSONParser();

            try (FileReader fileReader = new FileReader(path_goes_to)) {

                JSONObject o = (JSONObject) parser.parse(fileReader);
                JSONArray arr = (JSONArray) o.get("goes_to");

                if (arr.isEmpty()) {
                    return Collections.emptyList();
                }

                for (int index = 0; index < arr.size(); index++) {

                    JSONObject object = (JSONObject) arr.get(index);

                    if (object.get("username").equals(username) && object.get("itinerary_id").equals(itineraryId)) {

                        fileReader.close();

                        return true;
                    }
                }

            }

            return false;
        });
        return Objects.requireNonNullElse(ret, false);
    }


}
