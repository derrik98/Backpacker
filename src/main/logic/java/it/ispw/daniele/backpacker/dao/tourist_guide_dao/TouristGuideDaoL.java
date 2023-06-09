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

    protected static final String TOURIST_GUIDE = "tourist_guide";
    protected static final String GENERAL_USER = "general_user";

    protected List<TouristGuide> queryDatabase(String caller) throws GenericException {
        List<TouristGuide> ret = this.execute(() -> {

            JSONObject objectGuide = this.openFile(PATH_TOURIST_GUIDE);
            JSONArray arrayGuide = (JSONArray) objectGuide.get(TOURIST_GUIDE);

            JSONObject objectGeneralUser = this.openFile(PATH_GENERAL_USER);
            JSONArray arrayGeneralUser = (JSONArray) objectGeneralUser.get(GENERAL_USER);

            if (arrayGuide.isEmpty() || arrayGeneralUser.isEmpty()) {
                return Collections.emptyList();
            }

            return this.getTGuide(objectGuide, arrayGeneralUser, caller);

        });

        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }

    private List<TouristGuide> getTGuide(JSONObject objectT, JSONArray arrayGeneralUser, String caller){

        List<TouristGuide> l = new ArrayList<>();

        JSONArray arrayGuide = (JSONArray) objectT.get(TOURIST_GUIDE);

        for (int indexT = 0; indexT < arrayGuide.size(); indexT++) {

            JSONObject objectU = (JSONObject) arrayGuide.get(indexT);

            for (int indexGU = 0; indexGU < arrayGeneralUser.size(); indexGU++) {

                JSONObject objectGU = (JSONObject) arrayGeneralUser.get(indexGU);

                if (objectU.get(USERNAME).equals(caller) && objectGU.get(USERNAME).equals(caller)) {

                    String username = (String) objectU.get(USERNAME);
                    String name = (String) objectU.get(NAME);
                    String surname = (String) objectU.get(SURNAME);
                    String profilePicture = (String) objectU.get(PROFILE_PICTURE_PATH);
                    String email = (String) objectGU.get(EMAIL);
                    String vatNum = (String) objectU.get(IDENTIFICATION_CODE);

                    if (objectU.get(PROFILE_PICTURE_PATH) == null || objectU.get(PROFILE_PICTURE_PATH).equals("")) {
                        profilePicture = "tourist_guide.png";
                    }

                    l.add(new TouristGuide(username, name, surname, profilePicture, email, vatNum));

                    return l;
                }
            }
        }
        return Collections.emptyList();
    }
}
