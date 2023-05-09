package it.ispw.daniele.backpacker.dao.itinerary_dao;

import it.ispw.daniele.backpacker.entity.Itinerary;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItineraryDaoL extends ItineraryDaoFactory {

    protected static final String ITINERARY = "itinerary";

    @Override
    public List<Itinerary> getItinerary(String city) {
        List<Itinerary> ret = this.execute(() -> {

            List<Itinerary> itineraryList = new ArrayList<>();

            JSONParser parser = new JSONParser();

            try (FileReader fileReader = new FileReader(PATH_ITINERARY)) {

                JSONObject o = (JSONObject) parser.parse(fileReader);
                JSONArray arr = (JSONArray) o.get(ITINERARY);
                if (arr.isEmpty()) {
                    return Collections.emptyList();
                }

                for (int index = 0; index < arr.size(); index++) {

                    JSONObject object = (JSONObject) arr.get(index);

                    if (String.valueOf(object.get(LOCATION)).equals(city)) {

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

                        return itineraryList;
                    }
                }

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

            JSONParser parser = new JSONParser();

            try (FileReader fileReader = new FileReader(PATH_GOES_TO)) {

                JSONObject o = (JSONObject) parser.parse(fileReader);
                JSONArray arr = (JSONArray) o.get("goes_to");

                if (arr.isEmpty()) {
                    return Collections.emptyList();
                }

                for (int index = 0; index < arr.size(); index++) {

                    JSONObject object = (JSONObject) arr.get(index);

                    if (object.get(USERNAME).equals(username) && object.get(ITINERARY_ID).equals(itineraryId)) {

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

        try (FileReader fileReader = new FileReader(PATH_ITINERARY)) {

            JSONObject o = (JSONObject) parser.parse(fileReader);
            JSONArray arr = (JSONArray) o.get(ITINERARY);
            if (arr.isEmpty()) {
                return 0;
            }

            for (int index = 0; index < arr.size(); index++) {

                JSONObject object = (JSONObject) arr.get(index);

                if (object.get(GUIDE_ID).equals(guideId) && object.get(LOCATION).equals(location) && object.get(DATE).equals(date) &&
                        object.get(TIME).equals(time) && object.get(PARTICIPANTS).equals(String.valueOf(participants))
                        && object.get(PRICE).equals(String.valueOf(price)) && object.get(STEPS).equals(steps)) {

                    int id = (int) object.get(ID);

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

            try (FileReader fileItinerary = new FileReader(PATH_ITINERARY); FileReader fileGoesTo = new FileReader(PATH_GOES_TO)) {

                JSONObject objectGoesTo = (JSONObject) parser.parse(fileGoesTo);
                JSONArray arrayGoesTo = (JSONArray) objectGoesTo.get("goes_to");

                JSONObject objectItinerary = (JSONObject) parser.parse(fileItinerary);
                JSONArray arrayItinerary = (JSONArray) objectItinerary.get(ITINERARY);

                if (arrayGoesTo.isEmpty() || arrayItinerary.isEmpty()) {
                    return Collections.emptyList();
                }

                for (int indexG = 0; indexG < arrayGoesTo.size(); indexG++) {

                    JSONObject objectG = (JSONObject) arrayGoesTo.get(indexG);

                    for (int indexI = 0; indexI < arrayItinerary.size(); indexI++) {

                        JSONObject objectI = (JSONObject) arrayItinerary.get(indexI);

                        if (objectG.get(ITINERARY_ID).equals(objectI.get(ID)) && objectG.get(USERNAME).equals(input)) {

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

            List<Itinerary> itineraryList = new ArrayList<>();

            JSONParser parser = new JSONParser();

            try(FileReader fileReader = new FileReader(PATH_SAVED_ITINERARY)) {

                JSONObject o = (JSONObject) parser.parse(fileReader);
                JSONArray arr = (JSONArray) o.get("saved_itinerary");

                if (arr.isEmpty()) {
                    return Collections.emptyList();
                }

                for (int index = 0; index < arr.size(); index++) {

                    JSONObject object = (JSONObject) arr.get(index);

                    if (object.get(USERNAME).equals(input)) {

                        String id = (String) object.get(ID);
                        String steps = (String) object.get(STEPS);

                        Itinerary itinerary = new Itinerary(Integer.parseInt(id), "", "", "", "", 0, 0, steps);

                        itineraryList.add(itinerary);

                    }
                }

                return itineraryList;
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }

        });

        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }

}
