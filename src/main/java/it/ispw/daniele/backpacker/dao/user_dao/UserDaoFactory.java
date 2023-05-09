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

    protected static final String SEARCH_USER = "search_user";
    protected static final String path_user = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/user.json";
    protected static final String path_general_user = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/general_user.json";

    protected static final String USERNAME = "username";
    protected static final String NAME = "name";
    protected static final String SURNAME = "surname";
    protected static final String PROFILE_PICTURE_PATH = "profile_picture_path";
    protected static final String EMAIL = "email";
    protected static final String PASSWORD = "password";
    protected static final String ROLE = "user";

    public Boolean createUser(String username, String name, String surname, String email, String password, String profilePicture) throws SQLException, GenericException {
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
                o = (JSONObject) parser.parse(new FileReader(path_user));
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }

            arr = (JSONArray) o.get("user");

            jsonMap = new HashMap<>();
            jsonMap.put(USERNAME, username);
            jsonMap.put(NAME, name);
            jsonMap.put(SURNAME, surname);
            jsonMap.put(PROFILE_PICTURE_PATH, profilePicture);

            JSONObject newUser = new JSONObject(jsonMap);

            arr.add(newUser);


            try (FileWriter file = new FileWriter(path_user)) {
                file.write(o.toString());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                o = (JSONObject) parser.parse(new FileReader(path_general_user));
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }

            arr = (JSONArray) o.get("general_user");

            jsonMap = new HashMap<>();
            jsonMap.put(USERNAME, username);
            jsonMap.put(EMAIL, email);
            jsonMap.put(PASSWORD, password);
            jsonMap.put(ROLE, "user");

            JSONObject newUser1 = new JSONObject(jsonMap);

            arr.add(newUser1);

            try (FileWriter file = new FileWriter(path_general_user)) {
                file.write(o.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return true;
        }) != null);
    }

    public List<User> getSearchUser(String caller) {
        return this.queryDatabase(caller, SEARCH_USER);
    }

    protected abstract List<User> queryDatabase(String caller, String operation);

}
