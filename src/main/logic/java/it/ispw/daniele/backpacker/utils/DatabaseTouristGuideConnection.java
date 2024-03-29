package it.ispw.daniele.backpacker.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTouristGuideConnection {

    private static Connection connection = null;
    private static final String TOURIST_GUIDE_PSW = System.getProperty("tourist_guide_password");
    private static final String DB_URL = "jdbc:mysql://localhost/backpacker?allowPublicKeyRetrieval=true&useSSL=false";

    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

    private DatabaseTouristGuideConnection() {
    }

    public static Connection getTouristGuideConnection() throws SQLException, ClassNotFoundException {

        if(connection == null) {
            Class.forName(DRIVER_CLASS_NAME);
            connection = DriverManager.getConnection(DB_URL, "tourist_guide", TOURIST_GUIDE_PSW);
        }
        return connection;

    }

    public static void closeTouristGuideConnection(Connection conn) throws SQLException{
        conn.close();
        connection = null;
    }

}
