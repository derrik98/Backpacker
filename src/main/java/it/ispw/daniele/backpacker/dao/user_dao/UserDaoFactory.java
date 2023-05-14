package it.ispw.daniele.backpacker.dao.user_dao;

import it.ispw.daniele.backpacker.dao.DaoTemplate;
import it.ispw.daniele.backpacker.entity.User;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UserDaoFactory extends DaoTemplate {
    protected static final String PATH_USER = System.getProperty("path_user");
    protected static final String PATH_GENERAL_USER = System.getProperty("path_general_user");

    protected static final String USERNAME = "username";
    protected static final String NAME = "name";
    protected static final String SURNAME = "surname";
    protected static final String PROFILE_PICTURE_PATH = "profile_picture_path";
    protected static final String EMAIL = "email";
    protected static final String PASSWORD = "password";

    protected static final String USER = "user";
    protected static final String GENERAL_USER = "general_user";
    protected static final String ROLE = "role";

    public Boolean createUser(String username, String name, String surname, String email, String password, String profilePicture) throws GenericException {
        return (this.execute(() -> {

            //Save on Database
            Connection conn = DatabaseLoginConnection.getLoginConnection();

            if (conn == null) {
                return false;
            }

            String sql = "call backpacker.add_user(?, ?, ?, ?, ?, ?);\r\n";

            try (PreparedStatement stm = conn.prepareStatement(sql)){
                stm.setString(1, username);
                stm.setString(2, name);
                stm.setString(3, surname);
                stm.setString(4, email);
                stm.setString(5, password);
                stm.setString(6, profilePicture);
                stm.executeUpdate();
            }
            finally {
                DatabaseLoginConnection.closeLoginConnection(conn);
            }

            //Save on File System
            JSONParser parser = new JSONParser();
            JSONObject o;
            JSONArray arr;
            Map<String, String> jsonMap;

            try {
                o = (JSONObject) parser.parse(new FileReader(PATH_USER));
            } catch (IOException | ParseException e) {
                throw new GenericException(e.getMessage());
            }

            arr = (JSONArray) o.get(USER);

            jsonMap = new HashMap<>();
            jsonMap.put(USERNAME, username);
            jsonMap.put(NAME, name);
            jsonMap.put(SURNAME, surname);
            jsonMap.put(PROFILE_PICTURE_PATH, profilePicture);

            JSONObject newUser = new JSONObject(jsonMap);

            arr.add(newUser);


            try (FileWriter file = new FileWriter(PATH_USER)) {
                file.write(o.toString());

            } catch (IOException e) {
                throw new GenericException(e.getMessage());
            }

            try {
                o = (JSONObject) parser.parse(new FileReader(PATH_GENERAL_USER));
            } catch (IOException | ParseException e) {
                throw new GenericException(e.getMessage());
            }

            arr = (JSONArray) o.get(GENERAL_USER);

            jsonMap = new HashMap<>();
            jsonMap.put(USERNAME, username);
            jsonMap.put(EMAIL, email);
            jsonMap.put(PASSWORD, password);
            jsonMap.put(ROLE, USER);

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

    public List<User> getSearchUser(String caller) throws GenericException {
        return this.queryDatabase(caller);
    }

    protected abstract List<User> queryDatabase(String caller) throws GenericException;

}
