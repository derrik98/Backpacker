package it.ispw.daniele.backpacker.dao.user_dao;

import it.ispw.daniele.backpacker.entity.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class UserDaoL extends UserDaoFactory {

    protected static final String USER = "user";
    protected static final String GENERAL_USER = "general_user";

    protected List<User> queryDatabase(String caller, String operation) {
        List<User> ret = this.execute(() -> {
            List<User> l = new ArrayList<>();

            if (operation.equals(SEARCH_USER)) {

                JSONParser parser = new JSONParser();

                try (FileReader fileUser = new FileReader(path_user); FileReader fileGeneralUser = new FileReader(path_general_user)) {

                    JSONObject objectUser = (JSONObject) parser.parse(fileUser);
                    JSONArray arrayUser = (JSONArray) objectUser.get(USER);

                    JSONObject objectGeneralUser = (JSONObject) parser.parse(fileGeneralUser);
                    JSONArray arrayGeneralUser = (JSONArray) objectGeneralUser.get(GENERAL_USER);

                    if (arrayUser.isEmpty() || arrayGeneralUser.isEmpty()) {
                        return Collections.emptyList();
                    }

                    for (int indexU = 0; indexU < arrayUser.size(); indexU++) {

                        JSONObject objectU = (JSONObject) arrayUser.get(indexU);

                        for (int indexGU = 0; indexGU < arrayGeneralUser.size(); indexGU++) {

                            JSONObject objectGU = (JSONObject) arrayGeneralUser.get(indexGU);

                            if (objectU.get(USERNAME).equals(caller) && objectGU.get(USERNAME).equals(caller)) {

                                String username = (String) objectU.get(USERNAME);
                                String name = (String) objectU.get(NAME);
                                String surname = (String) objectU.get(SURNAME);
                                String profilePicture = (String) objectU.get(PROFILE_PICTURE_PATH);
                                String email = (String) objectGU.get(EMAIL);

                                if (objectU.get(PROFILE_PICTURE_PATH) == null || objectU.get(PROFILE_PICTURE_PATH).equals("")) {
                                    profilePicture = "user.png";
                                }

                                l.add(new User(username, name, surname, profilePicture, email));

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
