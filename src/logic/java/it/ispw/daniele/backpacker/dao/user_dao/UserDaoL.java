package it.ispw.daniele.backpacker.dao.user_dao;

import it.ispw.daniele.backpacker.entity.User;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserDaoL extends UserDaoFactory {

    protected static final String USER = "user";
    protected static final String GENERAL_USER = "general_user";

    protected List<User> queryDatabase(String caller) throws GenericException {
        List<User> ret = this.execute(() -> {

            JSONObject objectUser = this.openFile(PATH_USER);
            JSONArray arrayUser = (JSONArray) objectUser.get(USER);

            JSONObject objectGeneralUser = this.openFile(PATH_GENERAL_USER);
            JSONArray arrayGeneralUser = (JSONArray) objectGeneralUser.get(GENERAL_USER);

            if (arrayUser.isEmpty() || arrayGeneralUser.isEmpty()) {
                return Collections.emptyList();
            }

            return this.getUser(objectUser, arrayGeneralUser, caller);

        });

        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }

    private List<User> getUser(JSONObject objectUser, JSONArray arrayGeneralUser, String caller) {

        List<User> l = new ArrayList<>();

        JSONArray arrayUser = (JSONArray) objectUser.get(USER);

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

        return Collections.emptyList();
    }
}
