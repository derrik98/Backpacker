package it.ispw.daniele.backpacker.dao.UserDao;

import it.ispw.daniele.backpacker.dao.DaoTemplate;
import it.ispw.daniele.backpacker.entity.Itinerary;
import it.ispw.daniele.backpacker.entity.User;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UserDaoL extends UserDaoFactory {

//    private static final String SEARCH_USER = "search_user";
//    private final String path_user = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/user.json";
//    private final String path_general_user = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/general_user.json";

//    public Boolean createUser(String username, String name, String surname, String email, String password, String profilePicture) {
//        return this.execute(() -> {
//
//            JSONParser parser = new JSONParser();
//            JSONObject o;
//            JSONArray arr;
//            Map<String, String> jsonMap;
//
//            o = (JSONObject) parser.parse(new FileReader(path_user));
//
//            arr = (JSONArray) o.get("user");
//
//            jsonMap = new HashMap<>();
//            jsonMap.put("username", username);
//            //jsonMap.put("email", email);
//           // jsonMap.put("password", password);
//            jsonMap.put("name", name);
//            jsonMap.put("surname", surname);
//            jsonMap.put("profile_picture_path", profilePicture);
//
//            JSONObject newUser = new JSONObject(jsonMap);
//
//            arr.add(newUser);
//
//            try (FileWriter file = new FileWriter(path_user)) {
//                file.write(o.toString());
//                System.out.println("Successfully updated json object to file...!!");
//            }
//
//            o = (JSONObject) parser.parse(new FileReader(path_general_user));
//
//            arr = (JSONArray) o.get("general_user");
//
//            jsonMap = new HashMap<>();
//            jsonMap.put("username", username);
//            jsonMap.put("email", email);
//            jsonMap.put("password", password);
//            jsonMap.put("role", "user");
//
//            JSONObject newUser1 = new JSONObject(jsonMap);
//
//            arr.add(newUser1);
//
//            try (FileWriter file = new FileWriter(path_general_user)) {
//                file.write(o.toString());
//                System.out.println("Successfully updated json object to file...!!");
//            }
//            return true;
//        }) != null;
//    }

//    public List<User> getSearchUser(String caller) {
//        return this.queryDatabase(caller, SEARCH_USER);
//    }


    protected List<User> queryDatabase(String caller, String operation) {
        List<User> ret = this.execute(() -> {
            List<User> l = new ArrayList<>();

            if (operation.equals(SEARCH_USER)) {

                JSONParser parser = new JSONParser();
                //String path = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/user.json";

                try (FileReader fileUser = new FileReader(path_user); FileReader fileGeneralUser = new FileReader(path_general_user)) {

                    JSONObject objectUser = (JSONObject) parser.parse(fileUser);
                    JSONArray arrayUser = (JSONArray) objectUser.get("user");

                    JSONObject objectGeneralUser = (JSONObject) parser.parse(fileGeneralUser);
                    JSONArray arrayGeneralUser = (JSONArray) objectGeneralUser.get("general_user");

                    if (arrayUser.isEmpty() || arrayGeneralUser.isEmpty()) {
                        return Collections.emptyList();
                    }

                    for (int indexU = 0; indexU < arrayUser.size(); indexU++) {

                        JSONObject objectU = (JSONObject) arrayUser.get(indexU);

                        for (int indexGU = 0; indexGU < arrayGeneralUser.size(); indexGU++) {

                            JSONObject objectGU = (JSONObject) arrayGeneralUser.get(indexGU);

                            if (objectU.get("username").equals(caller) && objectGU.get("username").equals(caller)) {

                                String username = (String) objectU.get("username");
                                String name = (String) objectU.get("name");
                                String surname = (String) objectU.get("surname");
                                String profilePicture = (String) objectU.get("profile_picture_path");
                                String email = (String) objectGU.get("email");

                                if (objectU.get("profile_picture_path") == null || objectU.get("profile_picture_path").equals("")) {
                                    profilePicture = "user.png";
                                }

                                l.add(new User(username, name, surname, profilePicture, email));

                                fileUser.close();
                                fileGeneralUser.close();

                                return l;

                            }
                        }
                    }
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });

        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }
}
