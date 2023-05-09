package it.ispw.daniele.backpacker.dao.user_dao;

import it.ispw.daniele.backpacker.entity.User;
import it.ispw.daniele.backpacker.utils.DatabaseUserConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserDao extends UserDaoFactory {

    protected List<User> queryDatabase(String caller, String operation) {
        List <User> ret = this.execute(() -> {
            List<User> l = new ArrayList<>();
            Connection conn = DatabaseUserConnection.getUserConnection();
            PreparedStatement stm = null;
            try {
                String sql;
                if (operation.equals(SEARCH_USER)) {

                    sql = "call backpacker.search_user(?);\r\n";
                    stm = conn.prepareStatement(sql);
                    stm.setString(1, caller);
                } else {

                    return Collections.emptyList();
                }
                try (ResultSet rs = stm.executeQuery()) {

                    if (!rs.first()) // rs not empty
                        return Collections.emptyList();

                    do{
                        String username = rs.getString("username");
                        String name = rs.getString("name");
                        String surname = rs.getString("surname");
                        String profilePicture = rs.getString("profile_picture_path");
                        String email = rs.getString("email");

                        if(profilePicture == null || profilePicture.equals("")) {
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
