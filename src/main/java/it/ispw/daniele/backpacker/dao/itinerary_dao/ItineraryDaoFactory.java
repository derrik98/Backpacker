package it.ispw.daniele.backpacker.dao.itinerary_dao;

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

    protected static final String PATH_GOES_TO = System.getProperty("path_goes_to");
    protected static final String PATH_ITINERARY = System.getProperty("path_itinerary");
    protected static final String PATH_SAVED_ITINERARY = System.getProperty("path_saved_itinerary");
    protected static final String ID = "id";
    protected static final String ITINERARY_ID = "itinerary_id";

    protected static final String USERNAME = "username";
    protected static final String LOCATION = "location";
    protected static final String GUIDE_ID = "guideId";
    protected static final String WITH_GUIDE = "with_guide";
    protected static final String DATE = "date";
    protected static final String TIME = "time";
    protected static final String PARTICIPANTS = "participants";
    protected static final String PRICE = "price";
    protected static final String STEPS = "steps";
    private static final String ADD_PART = "add_part";
    private static final String REMOVE_PART = "remove_part";

    public void addParticipation(String username, int itineraryId) {
        this.manageParticipation(username, itineraryId, ADD_PART);
    }

    public void removeParticipation(String username, int itineraryId) {
        this.manageParticipation(username, itineraryId, REMOVE_PART);
    }

    private void manageParticipation(String username, int id, String operation) {
        this.execute((DaoAction<Void>) () -> {

            Connection conn = DatabaseUserConnection.getUserConnection();
            PreparedStatement stm;
            String sql = null;

            //Save on Database
            try {
                if (operation.equals(ADD_PART)) {
                    sql = "call backpacker.add_participation(?, ?);\r\n";
                } else if (operation.equals(REMOVE_PART)) {
                    sql = "call backpacker.remove_participation(?, ?);\r\n";
                }
                stm = conn.prepareStatement(sql);
                stm.setString(1, username);
                stm.setInt(2, id);
                stm.executeUpdate();
            } catch (SQLException e) {
                throw new GenericException(e.getMessage());
            } finally {
                DatabaseUserConnection.closeUserConnection(conn);
            }

            //Save on File System
            try {

                //JSONParser parser = new JSONParser();
                //FileReader fileReader = new FileReader(PATH_GOES_TO);

                //JSONObject o = (JSONObject) parser.parse(fileReader);
                JSONObject o = this.openFile(PATH_GOES_TO);
                JSONArray arr = (JSONArray) o.get("goes_to");

                if (operation.equals(ADD_PART)) {
                    Map<String, String> jsonMap;

                    jsonMap = new HashMap<>();
                    jsonMap.put(USERNAME, username);
                    jsonMap.put(ITINERARY_ID, String.valueOf(id));
                    jsonMap.put(WITH_GUIDE, String.valueOf(true));

                    JSONObject newUser = new JSONObject(jsonMap);

                    arr.add(newUser);

                } else if (operation.equals(REMOVE_PART)) {
                    for (int index = 0; index < arr.size(); index++) {

                        JSONObject object = (JSONObject) arr.get(index);

                        if (object.get(USERNAME).equals(username) && object.get(ITINERARY_ID).equals(id)) {

                            arr.remove(object);
                        }
                    }
                }

                this.writeOnFile(PATH_GOES_TO, o);

            } catch (IOException e) {
                throw new GenericException(e.getMessage());
            }

            return null;
        });
    }

    public boolean addItinerary(String guideId, String location, Date date, String time, int participants, int price, String steps) {
        return (this.execute(() -> {

            //Save on Database
            Connection conn = DatabaseTouristGuideConnection.getTouristGuideConnection();
            String sql = "call backpacker.add_itinerary(?, ?, ?, ?, ?, ?, ?);\r\n";
            try (PreparedStatement stm = conn.prepareStatement(sql)) {

                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                stm.setString(1, guideId);
                stm.setString(2, location);
                stm.setDate(3, sqlDate);
                stm.setString(4, time);
                stm.setInt(5, participants);
                stm.setInt(6, price);
                stm.setString(7, steps);
                stm.executeUpdate();

            } finally {
                DatabaseTouristGuideConnection.closeTouristGuideConnection(conn);
            }


            //Save on File System
            //JSONParser parser = new JSONParser();
            JSONObject o;
            JSONArray arr;
            Map<String, String> jsonMap;

//            try {
//                o = (JSONObject) parser.parse(new FileReader(PATH_ITINERARY));
//            } catch (IOException | ParseException e) {
//                throw new GenericException(e.getMessage());
//            }

            o = this.openFile(PATH_ITINERARY);

            arr = (JSONArray) o.get("itinerary");


            jsonMap = new HashMap<>();
            jsonMap.put(ID, String.valueOf(arr.size() + 1));  //controllare bene indice
            jsonMap.put(GUIDE_ID, guideId);
            jsonMap.put(LOCATION, location);
            jsonMap.put(DATE, String.valueOf(date));
            jsonMap.put(TIME, time);
            jsonMap.put(PARTICIPANTS, String.valueOf(participants));
            jsonMap.put(PRICE, String.valueOf(price));
            jsonMap.put(STEPS, steps);

            JSONObject newUser = new JSONObject(jsonMap);

            arr.add(newUser);

            try {
                this.writeOnFile(PATH_ITINERARY, o);
            } catch (IOException e) {
                throw new GenericException(e.getMessage());
            }

            return true;

        }) != null);
    }


    public void saveTour(String username, String itinerary) {
        this.execute(() -> {

            //Save on Database
            Connection conn = DatabaseUserConnection.getUserConnection();
            String sql = "call backpacker.save_itinerary(?, ?);\r\n";

            try (PreparedStatement stm = conn.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, itinerary);
                stm.executeUpdate();

            } finally {
                DatabaseUserConnection.closeUserConnection(conn);
            }

            //Save on File System
            //JSONParser parser = new JSONParser();
            JSONObject o;
            JSONArray arr;
            Map<String, String> jsonMap;

//            try {
//                o = (JSONObject) parser.parse(new FileReader(PATH_SAVED_ITINERARY));
//            } catch (IOException | ParseException e) {
//                throw new GenericException(e.getMessage());
//            }

            o = this.openFile(PATH_SAVED_ITINERARY);
            arr = (JSONArray) o.get("saved_itinerary");

            jsonMap = new HashMap<>();
            jsonMap.put(ID, String.valueOf(arr.size() + 1));  //controllare bene indice
            jsonMap.put(USERNAME, username);
            jsonMap.put(STEPS, itinerary);

            JSONObject newItinerary = new JSONObject(jsonMap);

            arr.add(newItinerary);

            try {
                this.writeOnFile(PATH_SAVED_ITINERARY, o);
            } catch (IOException e) {
                throw new GenericException(e.getMessage());
            }

            return true;
        });
    }

    public void removeTour(String username, String steps) {
        this.execute(() -> {

            //Remove from Database
            Connection conn = DatabaseUserConnection.getUserConnection();
            String sql = "call backpacker.remove_itinerary(?, ?);\r\n";

            try (PreparedStatement stm = conn.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, steps);
                stm.executeUpdate();
            } finally {
                DatabaseUserConnection.closeUserConnection(conn);
            }

            //Remove from File System
            try {
                //JSONParser parser = new JSONParser();

                //FileReader fileReader = new FileReader(PATH_SAVED_ITINERARY);

                //JSONObject o = (JSONObject) parser.parse(fileReader);

                JSONObject o = openFile(PATH_SAVED_ITINERARY);
                JSONArray arr = (JSONArray) o.get("saved_itinerary");

                for (int index = 0; index < arr.size(); index++) {

                    JSONObject object = (JSONObject) arr.get(index);

                    if (object.get(USERNAME).equals(username) && object.get(STEPS).equals(steps)) {

                        arr.remove(object);

                        this.writeOnFile(PATH_SAVED_ITINERARY, o);

                    }
                }
            } catch (IOException e) {
                throw new GenericException(e.getMessage());
            }

            return true;
        });
    }

    private void writeOnFile(String path, JSONObject object) throws IOException, GenericException {

        try (FileWriter file = new FileWriter(path);) {
            file.write(object.toString());
        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }
    }

    private JSONObject openFile(String path) throws GenericException {

        JSONParser parser = new JSONParser();
        JSONObject o;

        try {
            o = (JSONObject) parser.parse(new FileReader(path));
        } catch (IOException | ParseException e) {
            throw new GenericException(e.getMessage());
        }
        return o;
    }

    public abstract List<Itinerary> getItinerary(String city);

    public abstract Boolean isParticipating(String username, int itineraryId);

    public abstract int getItineraryId(String guideId, String location, String date, String time, int participants, int price, String steps) throws SQLException, FileNotFoundException, GenericException, ClassNotFoundException;

    public abstract List<Itinerary> getBookedItineraries(String input);

    public abstract List<Itinerary> getSavedItinerary(String input);
}
