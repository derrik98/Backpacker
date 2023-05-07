package it.ispw.daniele.backpacker.dao.general_user_dao;

import it.ispw.daniele.backpacker.entity.GeneralUser;
import it.ispw.daniele.backpacker.utils.DatabaseLoginConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GeneralUserDao extends GeneralUserDaoFactory {

    public GeneralUser findUser(String username, String password) {
        return this.execute(() -> {
            Connection conn;
            GeneralUser generalUser = null;
            conn = DatabaseLoginConnection.getLoginConnection();

            String sql = "call backpacker.login(?, ?);\r\n";
            try (PreparedStatement stm = conn.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, password);
                try (ResultSet rs = stm.executeQuery()) {

                    if (!rs.first()) // rs not empty
                        return null;

                    boolean moreThanOne = rs.first() && rs.next();
                    assert !moreThanOne;
                    rs.first();

                    String role = rs.getString("role");
                    String usernameLoaded = rs.getString("username");

                    if (usernameLoaded.equals(username)) {
                        generalUser = new GeneralUser(usernameLoaded, "", role);

                    }
                }
            }

            DatabaseLoginConnection.closeLoginConnection(conn);
            return generalUser;
        });
    }
}
