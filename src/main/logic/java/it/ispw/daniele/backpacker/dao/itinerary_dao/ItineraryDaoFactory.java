package it.ispw.daniele.backpacker.dao.itinerary_dao;

import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.dao.DaoAction;
import it.ispw.daniele.backpacker.dao.DaoTemplate;
import it.ispw.daniele.backpacker.entity.Itinerary;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.DatabaseTouristGuideConnection;
import it.ispw.daniele.backpacker.utils.DatabaseUserConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

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

    public void addParticipation(String username, int itineraryId) throws GenericException {
        this.manageParticipation(username, itineraryId, ADD_PART);
    }

    public void removeParticipation(String username, int itineraryId) throws GenericException {
        this.manageParticipation(username, itineraryId, REMOVE_PART);
    }

    private void manageParticipation(String username, int id, String operation) throws GenericException {
        this.execute((DaoAction<Void>) () -> {

            Connection conn = DatabaseUserConnection.getUserConnection();
            PreparedStatement stm;
            String sql;

            //Save on Database
            if (operation.equals(ADD_PART)) {
                sql = "call backpacker.add_participation(?, ?);\r\n";
            } else {
                sql = "call backpacker.remove_participation(?, ?);\r\n";
            }

            stm = conn.prepareStatement(sql);
            stm.setString(1, username);
            stm.setInt(2, id);
            stm.executeUpdate();

            DatabaseUserConnection.closeUserConnection(conn);


            //Save on File System
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

            return null;
        });
    }

    public boolean addItinerary(ItineraryBean itineraryBean, Date date) throws GenericException {
        return (this.execute(() -> {

            //Save on Database
            Connection conn = DatabaseTouristGuideConnection.getTouristGuideConnection();
            String sql = "call backpacker.add_itinerary(?, ?, ?, ?, ?, ?, ?);\r\n";

            try (PreparedStatement stm = conn.prepareStatement(sql)) {

                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                stm.setString(1, itineraryBean.getGuideId());
                stm.setString(2, itineraryBean.getLocation());
                stm.setDate(3, sqlDate);
                stm.setString(4, itineraryBean.getTime());
                stm.setInt(5, itineraryBean.getParticipants());
                stm.setInt(6, itineraryBean.getPrice());
                stm.setString(7, itineraryBean.getSteps());
                stm.executeUpdate();

            } finally {
                DatabaseTouristGuideConnection.closeTouristGuideConnection(conn);
            }

            //Save on File System
            JSONObject o;
            JSONArray arr;
            Map<String, String> jsonMap;

            o = this.openFile(PATH_ITINERARY);

            arr = (JSONArray) o.get("itinerary");

            jsonMap = new HashMap<>();
            jsonMap.put(ID, String.valueOf(arr.size() + 1));
            jsonMap.put(GUIDE_ID, itineraryBean.getGuideId());
            jsonMap.put(LOCATION, itineraryBean.getLocation());
            jsonMap.put(DATE, String.valueOf(date));
            jsonMap.put(TIME, itineraryBean.getTime());
            jsonMap.put(PARTICIPANTS, String.valueOf(itineraryBean.getParticipants()));
            jsonMap.put(PRICE, String.valueOf(itineraryBean.getPrice()));
            jsonMap.put(STEPS, itineraryBean.getSteps());

            JSONObject newUser = new JSONObject(jsonMap);

            arr.add(newUser);

            this.writeOnFile(PATH_ITINERARY, o);


            return true;

        }) != null);
    }


    public void saveItinerary(int id, String username, String itinerary) throws GenericException {
        this.execute(() -> {

            //Save on Database
            Connection conn = DatabaseUserConnection.getUserConnection();
            String sql = "call backpacker.save_itinerary(?, ?, ?);\r\n";

            try (PreparedStatement stm = conn.prepareStatement(sql)) {
                stm.setInt(1, id);
                stm.setString(2, username);
                stm.setString(3, itinerary);
                stm.executeUpdate();

            } finally {
                DatabaseUserConnection.closeUserConnection(conn);
            }

            //Save on File System
            JSONObject o;
            JSONArray arr;
            Map<String, String> jsonMap;

            o = this.openFile(PATH_SAVED_ITINERARY);
            arr = (JSONArray) o.get("saved_itinerary");

            jsonMap = new HashMap<>();
            jsonMap.put(ID, String.valueOf(id));
            jsonMap.put(USERNAME, username);
            jsonMap.put(STEPS, itinerary);

            JSONObject newItinerary = new JSONObject(jsonMap);

            arr.add(newItinerary);

            this.writeOnFile(PATH_SAVED_ITINERARY, o);

            return true;
        });
    }

    public void removeItinerary(int id, String username, String steps) throws GenericException {
        this.execute(() -> {

            //Remove from Database
            Connection conn = DatabaseUserConnection.getUserConnection();
            String sql = "call backpacker.remove_itinerary(?, ?, ?);\r\n";

            try (PreparedStatement stm = conn.prepareStatement(sql)) {
                stm.setInt(1, id);
                stm.setString(2, username);
                stm.setString(3, steps);
                stm.executeUpdate();
            } finally {
                DatabaseUserConnection.closeUserConnection(conn);
            }

            //Remove from File System
            JSONObject o = this.openFile(PATH_SAVED_ITINERARY);
            JSONArray arr = (JSONArray) o.get("saved_itinerary");

            for (int index = 0; index < arr.size(); index++) {

                JSONObject object = (JSONObject) arr.get(index);

                if (object.get(ID).equals(String.valueOf(id)) && object.get(USERNAME).equals(username) && object.get(STEPS.replace("\\/", "/")).equals(steps)) {

                    arr.remove(object);

                    this.writeOnFile(PATH_SAVED_ITINERARY, o);

                }
            }
            return true;
        });
    }

    public abstract List<Itinerary> getItinerary(String city) throws GenericException;

    public abstract Boolean isParticipating(String username, int itineraryId) throws GenericException;

    public abstract int getItineraryId(ItineraryBean itineraryBean) throws SQLException, FileNotFoundException, GenericException, ClassNotFoundException;

    public abstract List<Itinerary> getBookedItineraries(String input) throws GenericException;

    public abstract List<Itinerary> getSavedItinerary(String input) throws GenericException;
}
