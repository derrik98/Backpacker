package it.ispw.daniele.backpacker.dao.UserDao;

import it.ispw.daniele.backpacker.dao.DaoTemplate;
import it.ispw.daniele.backpacker.entity.User;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.exceptions.LoginFailException;
import it.ispw.daniele.backpacker.utils.DatabaseLoginConnection;
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
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UserDaoFactory extends DaoTemplate {

    protected static final String SEARCH_USER = "search_user";
    protected final String path_user = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/user.json";
    protected final String path_general_user = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/general_user.json";

    public Boolean createUser(String username, String name, String surname, String email, String password, String profilePicture) {
        return (this.execute(() -> {

            //Save on Database
            Connection conn = DatabaseLoginConnection.getLoginConnection();

            if (conn == null) {
                return false;
            }

            String sql = "call backpacker.add_user(?, ?, ?, ?, ?, ?);\r\n";
            //
            // PreparedStatement stm = con.prepareStatement(sql);

            try (PreparedStatement stm = conn.prepareStatement(sql)){
                stm.setString(1, username);
                stm.setString(2, name);
                stm.setString(3, surname);
                stm.setString(4, email);
                stm.setString(5, password);
                stm.setString(6, profilePicture);
                stm.executeUpdate();
            }finally {
                DatabaseLoginConnection.closeLoginConnection(conn);
            }

            /*finally {
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException e1) {
                        System.out.println("error");
                    }
                }
                try {
                    con.close();
                } catch (SQLException e) {
                    System.out.println("error");
                }
            }*/

            //Save on File System
            JSONParser parser = new JSONParser();
            JSONObject o;
            JSONArray arr;
            Map<String, String> jsonMap;

            try {
                o = (JSONObject) parser.parse(new FileReader(path_user));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            arr = (JSONArray) o.get("user");

            jsonMap = new HashMap<>();
            jsonMap.put("username", username);
            jsonMap.put("name", name);
            jsonMap.put("surname", surname);
            jsonMap.put("profile_picture_path", profilePicture);

            JSONObject newUser = new JSONObject(jsonMap);

            arr.add(newUser);


            try (FileWriter file = new FileWriter(path_user)) {
                file.write(o.toString());
                System.out.println("Successfully updated json object to file...!!");
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
            jsonMap.put("username", username);
            jsonMap.put("email", email);
            jsonMap.put("password", password);
            jsonMap.put("role", "user");

            JSONObject newUser1 = new JSONObject(jsonMap);

            arr.add(newUser1);

            try (FileWriter file = new FileWriter(path_general_user)) {
                file.write(o.toString());
                System.out.println("Successfully updated json object to file...!!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return true;
        }) != null);
    }

    public List<User> getSearchUser(String caller){
        return this.queryDatabase(caller, SEARCH_USER);
    }

    protected abstract List<User> queryDatabase(String caller, String operation);

}
