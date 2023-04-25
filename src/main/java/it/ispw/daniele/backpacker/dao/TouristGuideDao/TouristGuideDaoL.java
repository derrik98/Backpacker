package it.ispw.daniele.backpacker.dao.TouristGuideDao;

import it.ispw.daniele.backpacker.entity.TouristGuide;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
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

                try (FileReader fileReader = new FileReader(path_tourist_guide)) {

                    JSONObject o = (JSONObject) parser.parse(fileReader);
                    JSONArray arr = (JSONArray) o.get("tourist_guide");

                    if (arr.isEmpty()) {
                        return Collections.emptyList();
                    }

                    for (int index = 0; index < arr.size(); index++) {

                        JSONObject object = (JSONObject) arr.get(index);

                        if (object.get("username").equals(caller)) {
                            String username = (String) object.get("username");
                            String name = (String) object.get("name");
                            String surname = (String) object.get("surname");
                            String profilePicture = (String) object.get("profile_picture_path");
                            String email = (String) object.get("email");
                            String vatNum = (String) object.get("identification_code");

                            if (object.get("profile_picture_path") == null || object.get("profile_picture_path").equals("")) {
                                profilePicture = "tourist_guide.png";
                            }

                            l.add(new TouristGuide(username, name, surname, profilePicture, email, vatNum));

                            fileReader.close();

                            return l;
                        }
                    }
                }
            }
            return null;
        });

        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }

}
