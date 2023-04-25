package it.ispw.daniele.backpacker.dao.ItineraryDao;

import it.ispw.daniele.backpacker.dao.DaoAction;
import it.ispw.daniele.backpacker.dao.DaoTemplate;
import it.ispw.daniele.backpacker.entity.Itinerary;
import it.ispw.daniele.backpacker.utils.DatabaseUserConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ItineraryDaoFactory extends DaoTemplate {

    protected final String path_goes_to = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/goes_to.json";

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

    public void addParticipation(String username, int itineraryId) {
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


    protected abstract List<Itinerary> getItinerary(String city);

    protected abstract Boolean isParticipating(String username, int itineraryId);
}
