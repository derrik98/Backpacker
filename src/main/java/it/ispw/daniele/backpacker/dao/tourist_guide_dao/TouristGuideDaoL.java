package it.ispw.daniele.backpacker.dao.tourist_guide_dao;

import it.ispw.daniele.backpacker.entity.TouristGuide;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TouristGuideDaoL extends TouristGuideDaoFactory {

    protected List<TouristGuide> queryDatabase(String caller, String operation) {
        List<TouristGuide> ret = this.execute(() -> {
            List<TouristGuide> l = new ArrayList<>();

            if (operation.equals(SEARCH_T_GUIDE)) {

                JSONParser parser = new JSONParser();

                try (FileReader fileT = new FileReader(path_tourist_guide); FileReader fileGeneralUser = new FileReader(path_general_user)) {

                    JSONObject objectT = (JSONObject) parser.parse(fileT);
                    JSONArray arrayT = (JSONArray) objectT.get("tourist_guide");

                    JSONObject objectGeneralUser = (JSONObject) parser.parse(fileGeneralUser);
                    JSONArray arrayGeneralUser = (JSONArray) objectGeneralUser.get("general_user");

                    if (arrayT.isEmpty() || arrayGeneralUser.isEmpty()) {
                        return Collections.emptyList();
                    }

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

                                fileT.close();
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