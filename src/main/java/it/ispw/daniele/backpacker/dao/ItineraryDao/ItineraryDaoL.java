package it.ispw.daniele.backpacker.dao.ItineraryDao;

import it.ispw.daniele.backpacker.entity.Itinerary;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItineraryDaoL extends ItineraryDaoFactory {


    @Override
    public List<Itinerary> getItinerary(String city) {
        List<Itinerary> ret = this.execute(() -> {

            List<Itinerary> itineraryList = new ArrayList<>();

            JSONParser parser = new JSONParser();

            try (FileReader fileReader = new FileReader(path_itinerary)) {

                JSONObject o = (JSONObject) parser.parse(fileReader);
                JSONArray arr = (JSONArray) o.get("itinerary");
                if (arr.isEmpty()) {
                    return Collections.emptyList();
                }

                for (int index = 0; index < arr.size(); index++) {

                    JSONObject object = (JSONObject) arr.get(index);

                    if (String.valueOf(object.get("location")).equals(city)) {

                        System.out.println(object.get(ID));

                        String id = String.valueOf(object.get(ID));
                        String guideId = String.valueOf(object.get(GUIDE_ID));
                        String location = String.valueOf(object.get(LOCATION));
                        String date = String.valueOf(object.get(DATE));
                        String time = String.valueOf(object.get(TIME));
                        String participants = String.valueOf(object.get(PARTICIPANTS));
                        String price = String.valueOf(object.get(PRICE));
                        String steps = String.valueOf(object.get(STEPS));


                        Itinerary itinerary = new Itinerary(Integer.parseInt(id), guideId, location, date, time, Integer.parseInt(participants),
                                Integer.parseInt(price), steps);

                        itineraryList.add(itinerary);

                        fileReader.close();

                        return itineraryList;
                    }
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }

    @Override
    public Boolean isParticipating(String username, int itineraryId) {
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

            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }

            return false;
        });
        return Objects.requireNonNullElse(ret, false);
    }

    @Override
    public int getItineraryId(String guideId, String location, String date, String time, int participants, int price, String steps) {

        JSONParser parser = new JSONParser();

        try (FileReader fileReader = new FileReader(path_itinerary)) {

            JSONObject o = (JSONObject) parser.parse(fileReader);
            JSONArray arr = (JSONArray) o.get("itinerary");
            if (arr.isEmpty()) {
                return 0;
            }

            for (int index = 0; index < arr.size(); index++) {

                JSONObject object = (JSONObject) arr.get(index);

                if (object.get("guide_id").equals(guideId) && object.get("location").equals(location) && object.get("date").equals(date) &&
                        object.get("time").equals(time) && object.get("participants").equals(String.valueOf(participants))
                        && object.get("price").equals(String.valueOf(price)) && object.get("steps").equals(steps)) {

                    int id = (int) object.get(ID);

                    fileReader.close();

                    return id;
                }

            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    @Override
    public List<Itinerary> getBookedItineraries(String input) {
        List<Itinerary> ret = this.execute(() -> {

            List<Itinerary> itineraryList = null;

            JSONParser parser = new JSONParser();

            try (FileReader fileItinerary = new FileReader(path_itinerary); FileReader fileGoesTo = new FileReader(path_goes_to)) {

                JSONObject objectGoesTo = (JSONObject) parser.parse(fileGoesTo);
                JSONArray arrayGoesTo = (JSONArray) objectGoesTo.get("goes_to");

                JSONObject objectItinerary = (JSONObject) parser.parse(fileItinerary);
                JSONArray arrayItinerary = (JSONArray) objectItinerary.get("itinerary");

                if (arrayGoesTo.isEmpty() || arrayItinerary.isEmpty()) {
                    return Collections.emptyList();
                }

                for (int indexG = 0; indexG < arrayGoesTo.size(); indexG++) {

                    JSONObject objectG = (JSONObject) arrayGoesTo.get(indexG);

                    for (int indexI = 0; indexI < arrayItinerary.size(); indexI++) {

                        JSONObject objectI = (JSONObject) arrayItinerary.get(indexI);

                        if (objectG.get("itinerary_id").equals(objectI.get("id")) && objectG.get("username").equals(input)) {

                            int id = (int) objectG.get(ID);
                            String guideId = (String) objectI.get(GUIDE_ID);
                            String location = (String) objectI.get(LOCATION);
                            String date = (String) objectI.get(DATE);
                            String time = (String) objectI.get(TIME);
                            int participants = (int) objectI.get(PARTICIPANTS);
                            int price = (int) objectI.get(PRICE);
                            String steps = (String) objectI.get(STEPS);

                            Itinerary itinerary = new Itinerary(id, guideId, location, date, time, participants, price, steps);

                            itineraryList.add(itinerary);

                        }
                    }
                }
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }

            return itineraryList;

        });
        return Objects.requireNonNullElse(ret, Collections.emptyList());

    }

    @Override
    public List<Itinerary> getSavedItinerary(String input) {
        List<Itinerary> ret = this.execute(() -> {

            List<Itinerary> itineraryList = null;

            JSONParser parser = new JSONParser();

            try(FileReader fileReader = new FileReader(path_saved_itinerary)) {

                JSONObject o = (JSONObject) parser.parse(fileReader);
                JSONArray arr = (JSONArray) o.get("saved_itinerary");

                if (arr.isEmpty()) {
                    return Collections.emptyList();
                }

                for (int index = 0; index < arr.size(); index++) {

                    JSONObject object = (JSONObject) arr.get(index);

                    if (object.get("username").equals(input)) {

                        int id = (int) object.get(ID);
                        String steps = (String) object.get(STEPS);

                        Itinerary itinerary = new Itinerary(id, "", "", "", "", 0, 0, steps);

                        itineraryList.add(itinerary);

                    }
                }

                fileReader.close();
                return itineraryList;
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }

        });

        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }

}
