package it.ispw.daniele.backpacker.dao.tourist_guide_dao;

import it.ispw.daniele.backpacker.dao.DaoTemplate;
import it.ispw.daniele.backpacker.entity.TouristGuide;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.DatabaseLoginConnection;
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

public  abstract class TouristGuideDaoFactory extends DaoTemplate {

    protected static final String PATH_TOURIST_GUIDE = System.getProperty("path_tourist_guide");
    protected static final String PATH_GENERAL_USER = System.getProperty("path_general_user");

    protected static final String USERNAME = "username";
    protected static final String NAME = "name";
    protected static final String SURNAME = "surname";
    protected static final String PROFILE_PICTURE_PATH = "profile_picture_path";
    protected static final String IDENTIFICATION_CODE = "identification_code";
    protected static final String EMAIL = "email";
    protected static final String PASSWORD = "password";

    protected static final String TOURIST_GUIDE = "tourist_guide";
    protected static final String ROLE = "role";

    public Boolean createTouristGuide(String username, String name, String surname,
                                      String email, String password, String profilePicture, String identificationCode) throws GenericException {
        return (this.execute(() -> {

            //Save on Database
            Connection conn = DatabaseLoginConnection.getLoginConnection();

            if(conn == null){
                return false;
            }

            String sql = "call backpacker.add_tourist_guide(?, ?, ?, ?, ?, ?, ?);\r\n";
            try (PreparedStatement stm = conn.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, name);
                stm.setString(3, surname);
                stm.setString(4, email);
                stm.setString(5, password);
                stm.setString(6, profilePicture);
                stm.setString(7, identificationCode);
                stm.executeUpdate();
            }finally {
                DatabaseLoginConnection.closeLoginConnection(conn);
            }


            //Save on File System
            JSONParser parser = new JSONParser();
            JSONObject o;
            JSONArray arr;
            Map<String, String> jsonMap;

            try {
                o = (JSONObject) parser.parse(new FileReader(PATH_TOURIST_GUIDE));
            } catch (IOException | ParseException e) {
                throw new GenericException(e.getMessage());
            }

            arr = (JSONArray) o.get(TOURIST_GUIDE);

            jsonMap = new HashMap<>();
            jsonMap.put(USERNAME, username);
            jsonMap.put(NAME, name);
            jsonMap.put(SURNAME, surname);
            jsonMap.put(PROFILE_PICTURE_PATH, profilePicture);
            jsonMap.put(IDENTIFICATION_CODE, identificationCode);

            JSONObject newUser = new JSONObject(jsonMap);

            arr.add(newUser);

            try (FileWriter file = new FileWriter(PATH_TOURIST_GUIDE)) {
                file.write(o.toString());
            } catch (IOException e) {
                throw new GenericException(e.getMessage());
            }

            try {
                o = (JSONObject) parser.parse(new FileReader(PATH_GENERAL_USER));
            } catch (IOException | ParseException e) {
                throw new GenericException(e.getMessage());
            }

            arr = (JSONArray) o.get("general_user");

            jsonMap = new HashMap<>();
            jsonMap.put(USERNAME, username);
            jsonMap.put(EMAIL, email);
            jsonMap.put(PASSWORD, password);
            jsonMap.put(ROLE, TOURIST_GUIDE);

            JSONObject newUser1 = new JSONObject(jsonMap);

            arr.add(newUser1);

            try (FileWriter file = new FileWriter(PATH_GENERAL_USER)) {
                file.write(o.toString());
            } catch (IOException e) {
                throw new GenericException(e.getMessage());
            }

            return true;
        }) != null);
    }

    public List<TouristGuide> getSearchUser(String caller) throws GenericException {
        return this.queryDatabase(caller);
    }

    protected abstract List<TouristGuide> queryDatabase(String caller) throws GenericException;
}
