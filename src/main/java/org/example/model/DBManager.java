package org.example.model;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// General DB operations

public class DBManager implements AutoCloseable {

    private Connection conn;
    private static String MasterPassword;
    EncryptionHandler enc = new EncryptionHandler();

    private static final String URL = "jdbc:sqlite:data.db";

    // The constructor initializes the DB if it did not exist
    public DBManager(String MasterPassword) throws CryptographyException, NoSuchAlgorithmException {
        this.MasterPassword = MasterPassword;

        String init = "CREATE TABLE IF NOT EXISTS MT(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "SITE TEXT, " +
                "USER TEXT, " +
                "PASS TEXT, " +
                "NOTES TEXT)";

        enc.init(MasterPassword);
        try{
            conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(init);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertRecord(String SITE, String USER, String PASS, String NOTES) throws CryptographyException {

       String Insert ="INSERT INTO MT(SITE,USER,PASS,NOTES) VALUES(?,?,?,?)";

        try {

            PreparedStatement pstmt = conn.prepareStatement(Insert);
            pstmt.setString(1, SITE);
            pstmt.setString(2, USER);
            pstmt.setString(3, enc.Encrypt(PASS));
            pstmt.setString(4, NOTES);

            pstmt.executeUpdate();


        } catch (SQLException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {

            System.out.println("SQL or Encryption Error");
            e.printStackTrace();
            throw new CryptographyException(e);

        }

    }

    public void deleteRecord(int id) throws  SQLException{
        String sql= "DELETE FROM MT WHERE ID = ?";
        try(PreparedStatement ptsmt = conn.prepareStatement(sql)){
            ptsmt.setInt(1, id);
            ptsmt.executeUpdate();
        } catch (SQLException e){
            System.out.println("Failed to delete record with ID" + id);
            e.printStackTrace();
        }

    }


    public void updateRecord(int ID, String SITE, String USER, String PASS, String NOTES) throws CryptographyException{
        String sql= "UPDATE MT SET SITE = ?, USER = ?, PASS = ?, NOTES = ? WHERE ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, SITE);
            pstmt.setString(2, USER);
            pstmt.setString(3, enc.Encrypt(PASS));
            pstmt.setString(4, NOTES);
            pstmt.setInt(5, ID);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Failed to update record with ID " + ID);
            e.printStackTrace();
            throw new CryptographyException(e);
        }

    }

    public List<Item> getAllItems(){

        String sql = "SELECT * FROM MT";
        List<Item> data = new ArrayList<>();
        try (
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                String site = rs.getString("SITE");
                String user = rs.getString("USER");
                String pass = (rs.getString("PASS")); // or however you handle it
                String notes = rs.getString("NOTES");
                data.add(new Item(id, site, user, pass, notes));
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        return data;
    }

    // This function is required to use try-with-resources
    // (closes resources inside a try block after it's executed)
    // it tells the JVM how to close this certain resource
    // and it is a part of the autoClosable interface implemented above
    @Override
    public void close() throws Exception {
        if(conn != null && !conn.isClosed()){
            conn.close();
        }
    }

    public String getMasterPassword() {
        return MasterPassword;
    }

    public Connection getConnection() {
        return conn;
    }

    public List<Item> Search(String searchTerm){
        List<Item> query = new ArrayList<>();
        String sql = "SELECT * FROM MT WHERE SITE LIKE ? OR USER LIKE ? OR NOTES LIKE ?";
        try (
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1,"%" + searchTerm + "%");
            pstmt.setString(2,"%" + searchTerm + "%");
            pstmt.setString(3,"%" + searchTerm + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int ID = rs.getInt("Id");
                    String site = rs.getString("SITE");
                    String user = rs.getString("USER");
                    String pass = rs.getString("PASS");
                    String note = rs.getString("NOTES");

                    query.add(new Item(ID, site, user, pass, note));
                }
                return query;
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null; //if the user is not found or if the databses has an erorr
    }
}

