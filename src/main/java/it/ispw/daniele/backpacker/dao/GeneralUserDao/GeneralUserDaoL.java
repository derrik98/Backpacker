package it.ispw.daniele.backpacker.dao.GeneralUserDao;

import it.ispw.daniele.backpacker.entity.GeneralUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class GeneralUserDaoL extends GeneralUserDaoFactory {

    private final String path_general_user = "C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/general_user.json";

    public GeneralUser findUser(String username, String password) {
        return this.execute(() -> {

            GeneralUser u;
            JSONParser parser = new JSONParser();

            try (FileReader fileReader = new FileReader(path_general_user)) {

                JSONObject o = (JSONObject) parser.parse(fileReader);
                JSONArray arr = (JSONArray) o.get("general_user");
                if (arr.isEmpty()) {
                    return null;
                }

                for (int index = 0; index < arr.size(); index++) {

                    JSONObject object = (JSONObject) arr.get(index);
                    if (object.get("username").equals(username) && object.get("password").equals(password)) {
                        String role = (String) object.get("role");
                        String usernameLoaded = (String) object.get("username");

                        if (usernameLoaded.equals(username)) {
                            u = new GeneralUser(usernameLoaded, "", role);
                            return u;

                        }
                    }
                }

            }

            return null;
        });
    }

}
