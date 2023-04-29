package it.ispw.daniele.backpacker.dao.ItineraryDao;

import it.ispw.daniele.backpacker.dao.DaoAction;
import it.ispw.daniele.backpacker.dao.DaoTemplate;
import it.ispw.daniele.backpacker.entity.Itinerary;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.DatabaseTouristGuideConnection;
import it.ispw.daniele.backpacker.utils.DatabaseUserConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ItineraryDaoFactory extends DaoTemplate {

    protected final String path_goes_to = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/goes_to.json";
    protected final String path_itinerary = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/itinerary.json";
    protected final String path_saved_itinerary = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/saved_itinerary.json";

    protected final String ID = "id";
    protected final String LOCATION = "location";
    protected final String GUIDE_ID = "guideId";
    protected final String DATE = "date";
    protected final String TIME = "time";
    protected final String PARTICIPANTS = "participants";
    protected final String PRICE = "price";
    protected final String STEPS = "steps";
    private final String ADD_PART = "add_part";
    private final String REMOVE_PART = "remove_part";

    public void addParticipation(String username, int itineraryId)  {
        this.manageParticipation(username, itineraryId, ADD_PART);
    }

    public void removeParticipation(String username, int itineraryId) {
        this.manageParticipation(username, itineraryId, REMOVE_PART);
    }

    private void manageParticipation(String username, int id, String operation) {
        this.execute((DaoAction<Void>) () -> {
            Connection conn;
            PreparedStatement stm = null;
            String sql = null;

            //Save on Database
            try {
                conn = DatabaseUserConnection.getUserConnection();
                if (operation.equals(ADD_PART)) {
                    sql = "call backpacker.add_participation(?, ?);\r\n";
                } else if (operation.equals(REMOVE_PART)) {
                    sql = "call backpacker.remove_participation(?, ?);\r\n";
                }
                stm = conn.prepareStatement(sql);
                stm.setString(1, username);
                stm.setInt(2, id);
                stm.executeUpdate();
            } finally {
                if (stm != null)
                    stm.close();
            }
            DatabaseUserConnection.closeUserConnection(conn);


            //Save on File System
            try {
                JSONParser parser = new JSONParser();

                FileReader fileReader = new FileReader(path_goes_to);

                JSONObject o = (JSONObject) parser.parse(fileReader);
                JSONArray arr = (JSONArray) o.get("goes_to");

                if (operation.equals(ADD_PART)) {
                    Map<String, String> jsonMap;

                    jsonMap = new HashMap<>();
                    jsonMap.put("username", username);
                    jsonMap.put("itinerary_id", String.valueOf(id));
                    jsonMap.put("with_guide", String.valueOf(true));

                    JSONObject newUser = new JSONObject(jsonMap);

                    arr.add(newUser);

                } else if (operation.equals(REMOVE_PART)) {
                    for (int index = 0; index < arr.size(); index++) {

                        JSONObject object = (JSONObject) arr.get(index);

                        if (object.get("username").equals(username) && object.get("itinerary_id").equals(id)) {

                            arr.remove(object);
                        }

                    }
                }

                try (FileWriter file = new FileWriter(path_goes_to)) {
                    file.write(o.toString());
                    System.out.println("Successfully updated json object to file...!!");

                    file.close();
                    fileReader.close();

                }
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }


            return null;
        });
    }


    public boolean addItinerary(String guideId, String location, Date date, String time, int participants, int price, String steps) {
        return (this.execute(() -> {

            //Save on Database
            Connection con = DatabaseTouristGuideConnection.getTouristGuideConnection();
            String sql = "call backpacker.add_itinerary(?, ?, ?, ?, ?, ?, ?);\r\n";
            try (PreparedStatement stm = con.prepareStatement(sql)) {

                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                stm.setString(1, guideId);
                stm.setString(2, location);
                stm.setDate(3, sqlDate);
                stm.setString(4, time);
                stm.setInt(5, participants);
                stm.setInt(6, price);
                stm.setString(7, steps);
                stm.executeUpdate();

            }


            //Save on File System
            JSONParser parser = new JSONParser();
            JSONObject o;
            JSONArray arr;
            Map<String, String> jsonMap;

            try {
                o = (JSONObject) parser.parse(new FileReader(path_itinerary));
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }

            arr = (JSONArray) o.get("itinerary");


            jsonMap = new HashMap<>();
            jsonMap.put("id", String.valueOf(arr.size() + 1));  //controllare bene indice
            jsonMap.put("guide_id", guideId);
            jsonMap.put("location", location);
            jsonMap.put("date", String.valueOf(date));
            jsonMap.put("time", time);
            jsonMap.put("participants", String.valueOf(participants));
            jsonMap.put("price", String.valueOf(price));
            jsonMap.put("steps", steps);

            JSONObject newUser = new JSONObject(jsonMap);

            arr.add(newUser);

            try (FileWriter file = new FileWriter(path_itinerary)) {
                file.write(o.toString());
                System.out.println("Successfully updated json object to file...!!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return true;

        }) != null);
    }


    public void saveTour(String username, String itinerary) {
        this.execute(() -> {

            //Save on Database
            Connection con = DatabaseUserConnection.getUserConnection();
            String sql = "call backpacker.save_itinerary(?, ?);\r\n";

            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, itinerary);
                stm.executeUpdate();

            }

            //Save on File System
            JSONParser parser = new JSONParser();
            JSONObject o;
            JSONArray arr;
            Map<String, String> jsonMap;

            try {
                o = (JSONObject) parser.parse(new FileReader(path_saved_itinerary));
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }

            arr = (JSONArray) o.get("saved_itinerary");

            jsonMap = new HashMap<>();
            jsonMap.put("id", String.valueOf(arr.size() + 1));  //controllare bene indice
            jsonMap.put("username", username);
            jsonMap.put("steps", itinerary);

            JSONObject newItinerary = new JSONObject(jsonMap);

            arr.add(newItinerary);

            try (FileWriter file = new FileWriter(path_saved_itinerary)) {
                file.write(o.toString());
                System.out.println("Successfully updated json object to file...!!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return true;
        });
    }

    public void removeTour(String username, String steps) {
        this.execute(() -> {

            //Remove from Database
            Connection con = DatabaseUserConnection.getUserConnection();
            String sql = "call backpacker.remove_itinerary(?, ?);\r\n";

            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, steps);
                stm.executeUpdate();
            }

            //Remove from File System
            try {
                JSONParser parser = new JSONParser();

                FileReader fileReader = new FileReader(path_saved_itinerary);

                JSONObject o = (JSONObject) parser.parse(fileReader);
                JSONArray arr = (JSONArray) o.get("saved_itinerary");

                for (int index = 0; index < arr.size(); index++) {

                    JSONObject object = (JSONObject) arr.get(index);

                    if (object.get("username").equals(username) && object.get("steps").equals(steps)) {

                        arr.remove(object);
                    }

                }
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }

            return true;
        });
    }

    public abstract List<Itinerary> getItinerary(String city);

    public abstract Boolean isParticipating(String username, int itineraryId);

    public abstract int getItineraryId(String guideId, String location, String date, String time, int participants, int price, String steps) throws SQLException, FileNotFoundException;

    public abstract List<Itinerary> getBookedItineraries(String input);

    public abstract List<Itinerary> getSavedItinerary(String input);
}
