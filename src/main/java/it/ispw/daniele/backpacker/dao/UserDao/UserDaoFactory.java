package it.ispw.daniele.backpacker.dao.UserDao;

import it.ispw.daniele.backpacker.dao.DaoTemplate;
import it.ispw.daniele.backpacker.entity.User;
import it.ispw.daniele.backpacker.utils.DatabaseLoginConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UserDaoFactory extends DaoTemplate {

    protected static final String SEARCH_USER = "search_user";
    private final String path_user = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/user.json";
    private final String path_general_user = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/general_user.json";

    public Boolean createUser(String username, String name, String surname,
                              String email, String password, String profilePicture) {
        return (this.execute(() -> {

            //Save on Database
            Connection con = DatabaseLoginConnection.getLoginConnection();

            String sql = "call backpacker.add_user(?, ?, ?, ?, ?, ?);\r\n";
            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, name);
                stm.setString(3, surname);
                stm.setString(4, email);
                stm.setString(5, password);
                stm.setString(6, profilePicture);
                stm.executeUpdate();
            }

            //Save on File System
            JSONParser parser = new JSONParser();
            JSONObject o;
            JSONArray arr;
            Map<String, String> jsonMap;

            o = (JSONObject) parser.parse(new FileReader(path_user));

            arr = (JSONArray) o.get("user");

            jsonMap = new HashMap<>();
            jsonMap.put("username", username);
            //jsonMap.put("email", email);
            //jsonMap.put("password", password);
            jsonMap.put("name", name);
            jsonMap.put("surname", surname);
            jsonMap.put("profile_picture_path", profilePicture);

            JSONObject newUser = new JSONObject(jsonMap);

            arr.add(newUser);

            try (FileWriter file = new FileWriter(path_user)) {
                file.write(o.toString());
                System.out.println("Successfully updated json object to file...!!");
            }

            o = (JSONObject) parser.parse(new FileReader(path_general_user));

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
            }

            return true;
        }) != null);
    }

    public List<User> getSearchUser(String caller){
        return this.queryDatabase(caller, SEARCH_USER);
    }

    protected abstract List<User> queryDatabase(String caller, String operation);


}
