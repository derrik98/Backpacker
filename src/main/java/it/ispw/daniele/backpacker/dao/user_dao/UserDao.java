package it.ispw.daniele.backpacker.dao.user_dao;

import it.ispw.daniele.backpacker.entity.User;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.DatabaseUserConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserDao extends UserDaoFactory {

    protected List<User> queryDatabase(String caller) throws GenericException {
        List<User> ret = this.execute(() -> {
            List<User> l = new ArrayList<>();
            Connection conn = DatabaseUserConnection.getUserConnection();
            PreparedStatement stm;
            try {
                String sql;

                sql = "call backpacker.search_user(?);\r\n";
                stm = conn.prepareStatement(sql);
                stm.setString(1, caller);

                try (ResultSet rs = stm.executeQuery()) {

                    if (!rs.first()) // rs not empty
                        return Collections.emptyList();

                    do {
                        String username = rs.getString(USERNAME);
                        String name = rs.getString(NAME);
                        String surname = rs.getString(SURNAME);
                        String profilePicture = rs.getString(PROFILE_PICTURE_PATH);
                        String email = rs.getString(EMAIL);

                        if (profilePicture == null || profilePicture.equals("")) {
                            profilePicture = "user.png";
                        }

                        l.add(new User(username, name, surname, profilePicture, email));

                    } while (rs.next());
                    return l;
                }
            } finally {
                DatabaseUserConnection.closeUserConnection(conn);
            }
        });

        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }

}
