package it.ispw.daniele.backpacker.dao.TouristGuideDao;

import it.ispw.daniele.backpacker.dao.DaoTemplate;
import it.ispw.daniele.backpacker.entity.TouristGuide;
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

public  abstract class TouristGuideDaoFactory extends DaoTemplate {

    protected static final String SEARCH_T_GUIDE = "search_t_guide";

    protected final String path_tourist_guide = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/tourist_guide.json";
    private final String path_general_user = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/general_user.json";


    public Boolean createTouristGuide(String username, String name, String surname,
                                      String email, String password, String profilePicture, String identificationCode) {
        return (this.execute(() -> {

            //Save on Database
            Connection con = DatabaseLoginConnection.getLoginConnection();

            if(con == null){
                return false;
            }

            String sql = "call backpacker.add_tourist_guide(?, ?, ?, ?, ?, ?, ?);\r\n";
            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, name);
                stm.setString(3, surname);
                stm.setString(4, email);
                stm.setString(5, password);
                stm.setString(6, profilePicture);
                stm.setString(7, identificationCode);
                stm.executeUpdate();
            }


            //Save on File System
            JSONParser parser = new JSONParser();
            JSONObject o;
            JSONArray arr;
            Map<String, String> jsonMap;

            o = (JSONObject) parser.parse(new FileReader(path_tourist_guide));

            arr = (JSONArray) o.get("user");

            jsonMap = new HashMap<>();
            jsonMap.put("username", username);
            jsonMap.put("name", name);
            jsonMap.put("surname", surname);
            jsonMap.put("profile_picture_path", profilePicture);
            jsonMap.put("identification_code", identificationCode);

            JSONObject newUser = new JSONObject(jsonMap);

            //arr.put(newUser);
            arr.add(newUser);

            try (FileWriter file = new FileWriter(path_tourist_guide)) {
                file.write(o.toString());
                System.out.println("Successfully updated json object to file...!!");
            }

            o = (JSONObject) parser.parse(new FileReader(path_general_user));

            arr = (JSONArray) o.get("general_user");

            jsonMap = new HashMap<>();
            jsonMap.put("username", username);
            jsonMap.put("email", email);
            jsonMap.put("password", password);
            jsonMap.put("role", "tourist_guide");

            JSONObject newUser1 = new JSONObject(jsonMap);

            arr.add(newUser1);

            try (FileWriter file = new FileWriter(path_general_user)) {
                file.write(o.toString());
                System.out.println("Successfully updated json object to file...!!");
            }

            return true;
        }) != null);
    }

    public List<TouristGuide> getSearchUser(String caller){
        return this.queryDatabase(caller, SEARCH_T_GUIDE);
    }

    protected abstract List<TouristGuide> queryDatabase(String caller, String operation);
}
