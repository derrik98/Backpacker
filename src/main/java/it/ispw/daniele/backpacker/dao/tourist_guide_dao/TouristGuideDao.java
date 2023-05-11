package it.ispw.daniele.backpacker.dao.tourist_guide_dao;

import it.ispw.daniele.backpacker.entity.TouristGuide;
import it.ispw.daniele.backpacker.utils.DatabaseTouristGuideConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TouristGuideDao extends TouristGuideDaoFactory {

    protected List<TouristGuide> queryDatabase(String caller) {
        List<TouristGuide> ret = this.execute(() -> {
            List<TouristGuide> l = new ArrayList<>();
            Connection conn = DatabaseTouristGuideConnection.getTouristGuideConnection();
            PreparedStatement stm;

            try {
                String sql;

                sql = "call backpacker.search_t_guide(?);\r\n";
                stm = conn.prepareStatement(sql);
                stm.setString(1, caller);

                try (ResultSet rs = stm.executeQuery()) {
                    if (!rs.first()) // rs not empty
                        return Collections.emptyList();

                    do {
                        String username = rs.getString("username");
                        String name = rs.getString("name");
                        String surname = rs.getString("surname");
                        String profilePicture = rs.getString("profile_picture_path");
                        String email = rs.getString("email");
                        String vatNum = rs.getString("identification_code");

                        if (profilePicture == null || profilePicture.equals("")) {
                            profilePicture = "tourist_guide.png";
                        }

                        l.add(new TouristGuide(username, name, surname, profilePicture, email, vatNum));
                    } while (rs.next());

                    return l;
                }
            } finally {
                DatabaseTouristGuideConnection.closeTouristGuideConnection(conn);
            }
        });

        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }
}
