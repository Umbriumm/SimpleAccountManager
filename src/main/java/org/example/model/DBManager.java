package org.example.model;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

// General DB operations

public class DBManager {
    private static final String URL = "jdbc:sqlite:data.db";
    public static String init = "CREATE TABLE IF NOT EXISTS MT(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "SERVICE TEXT, " +
            "USER TEXT, " +
            "PASS TEXT, " +
            "NOTES TEXT)";

    private static String Insert ="INSERT INTO MT(SERVICE,USER,PASS,NOTES) VALUES(?,?,?,?)";

    // The constructor initializes the DB if it did not exist
    public DBManager() {
        try{
            Connection initDB = DriverManager.getConnection(URL);
            Statement stmt = initDB.createStatement();
            stmt.executeUpdate(init);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void Insert(String SERVICE, String USER, String PASS, String NOTES) throws InvalidInputException {

        if ((USER.isBlank() || USER == null || PASS.isBlank() || PASS == null )){
            throw new InvalidInputException();
        }

        try {

            Connection conn = DriverManager.getConnection(URL);

            EncryptionHandler enc = new EncryptionHandler();

            PreparedStatement pstmt = conn.prepareStatement(Insert);
            pstmt.setString(1, enc.Encrypt(SERVICE));
            pstmt.setString(2, USER);
            pstmt.setString(3, PASS);
            pstmt.setString(4, NOTES);

            pstmt.executeUpdate();


        } catch (SQLException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

    }

}
