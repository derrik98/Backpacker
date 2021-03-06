package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GenericUserDAO {
	private static String USER = "root";
    private static String PASS = "";
    private static String DB_URL = "jdbc:mysql://localhost:3306/userdb?useSSL=false";
    private static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    
    
    public static List<GenericUser> retrieiveByUsername(String username) throws Exception {
        // STEP 1: dichiarazioni
        Statement stmt = null;
        Connection conn = null;
        List<GenericUser> listOfUser = new ArrayList<GenericUser>();
        
        try {
            // STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);

            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: creazione ed esecuzione della query
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            
            ResultSet rs = Queries.selectUserByName(stmt, username);
            //ResultSet rs = Queries.selectUserIds(stmt);

            if (!rs.first()){ // rs empty
            	return listOfUser;
            	//Exception e = new Exception("No User Found matching with name: "+ username);
            	//throw e;
            }
            
            // riposizionamento del cursore
            rs.first();
            do{
                // lettura delle colonne "by name"
                
                //int id = rs.getInt("id");
                String user = rs.getString("name");
                String surname = rs.getString("surname");
                String password = rs.getString("password");
                String email = rs.getString("email");
                System.out.println(user  + email);
                
                GenericUser a = new GenericUser(user, surname, password, email);
                
                listOfUser.add(a);

            }while(rs.next());
            
            // STEP 5.1: Clean-up dell'ambiente
            rs.close();
        } finally {
            // STEP 5.2: Clean-up dell'ambiente
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return listOfUser;
    }
    
    
    
    public static boolean addUser(GenericUser user) throws Exception {
        // STEP 1: dichiarazioni
        Statement stmt = null;
        Connection conn = null;
        
        try {
            // STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);

            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4.1: creazione ed esecuzione della query
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = Queries.selectUserIds(stmt);
            while (rs.next()) {
                //lettura delle colonne "by name"
                String userId = rs.getString("email");
                System.out.println("Found UserId: "+ userId);
                System.out.println(user.getEmail());
                if (userId.equals(user.getEmail())){
                	//DuplicatedRecordException e = new DuplicatedRecordException("Duplicated Instance ID. Id "+ userId + " was already assigned");
                	//throw e;
                	System.out.println("user gia esistente");
                	return false;
                }
            }
            
            rs.close();
            stmt.close();

            // STEP 4.2: creazione ed esecuzione della query
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            int result = Queries.addUser(stmt, user);
            
            // STEP 5.1: Clean-up dell'ambiente
            rs.close();
        } finally {
            // STEP 5.2: Clean-up dell'ambiente        	
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
        }
        System.out.println("utente aggiunto");
        return true;
    }
    
    
    public static void removeUserById(GenericUser user) throws Exception {
        // STEP 1: dichiarazioni
        Statement stmt = null;
        Connection conn = null;
        
        try {
            // STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);

            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4.1: creazione ed esecuzione della query
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            int result = Queries.deleteUser(stmt, user);
            
        } finally {
            // STEP 5.2: Clean-up dell'ambiente        	
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
        }
        
	}
    
}
