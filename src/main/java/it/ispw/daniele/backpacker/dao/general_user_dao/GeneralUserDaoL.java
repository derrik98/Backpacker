package it.ispw.daniele.backpacker.dao.general_user_dao;

import it.ispw.daniele.backpacker.entity.GeneralUser;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class GeneralUserDaoL extends GeneralUserDaoFactory {

    private static final String PATH_GENERAL_USER = System.getProperty("path_general_user");

    public GeneralUser findUser(String username, String password) {
        return this.execute(() -> {

            GeneralUser u;
            JSONParser parser = new JSONParser();

            try (FileReader fileReader = new FileReader(PATH_GENERAL_USER)) {

                JSONObject o = (JSONObject) parser.parse(fileReader);
                JSONArray arr = (JSONArray) o.get(GENERAL_USER);
                if (arr.isEmpty()) {
                    return null;
                }

                for (int index = 0; index < arr.size(); index++) {

                    JSONObject object = (JSONObject) arr.get(index);
                    if (object.get(USERNAME).equals(username) && object.get(PASSWORD).equals(password)) {
                        String role = (String) object.get(ROLE);
                        String usernameLoaded = (String) object.get(USERNAME);

                        if (usernameLoaded.equals(username)) {
                            u = new GeneralUser(usernameLoaded, "", role);
                            return u;
                        }
                    }
                }

            } catch (IOException | ParseException e) {
                throw new GenericException("Error");
            }

            return null;
        });
    }

}
