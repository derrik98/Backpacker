package it.ispw.daniele.backpacker.dao.tourist_guide_dao;

import it.ispw.daniele.backpacker.entity.TouristGuide;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TouristGuideDaoL extends TouristGuideDaoFactory {

    protected List<TouristGuide> queryDatabase(String caller) throws GenericException {
        List<TouristGuide> ret = this.execute(() -> {

            JSONObject objectT = this.openFile(PATH_TOURIST_GUIDE);
            JSONArray arrayT = (JSONArray) objectT.get("tourist_guide");

            JSONObject objectGeneralUser = this.openFile(PATH_GENERAL_USER);
            JSONArray arrayGeneralUser = (JSONArray) objectGeneralUser.get("general_user");

            if (arrayT.isEmpty() || arrayGeneralUser.isEmpty()) {
                return Collections.emptyList();
            }

            return this.getTGuide(objectT, arrayGeneralUser, caller);

        });

        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }

    private List<TouristGuide> getTGuide(JSONObject objectT, JSONArray arrayGeneralUser, String caller){

        List<TouristGuide> l = new ArrayList<>();

        JSONArray arrayT = (JSONArray) objectT.get("tourist_guide");

        for (int indexT = 0; indexT < arrayT.size(); indexT++) {

            JSONObject objectU = (JSONObject) arrayT.get(indexT);

            for (int indexGU = 0; indexGU < arrayGeneralUser.size(); indexGU++) {

                JSONObject objectGU = (JSONObject) arrayGeneralUser.get(indexGU);

                if (objectU.get(USERNAME).equals(caller) && objectGU.get(USERNAME).equals(caller)) {

                    String username = (String) objectT.get(USERNAME);
                    String name = (String) objectT.get(NAME);
                    String surname = (String) objectT.get(SURNAME);
                    String profilePicture = (String) objectT.get(PROFILE_PICTURE_PATH);
                    String email = (String) objectGU.get(EMAIL);
                    String vatNum = (String) objectT.get(IDENTIFICATION_CODE);

                    if (objectU.get(PROFILE_PICTURE_PATH) == null || objectU.get(PROFILE_PICTURE_PATH).equals("")) {
                        profilePicture = "user.png";
                    }

                    l.add(new TouristGuide(username, name, surname, profilePicture, email, vatNum));

                    return l;

                }
            }
        }

        return Collections.emptyList();
    }
}
