package org.example.model;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

// General DB operations

public class DBManager implements AutoCloseable {

    private Connection conn;
    private static String MasterPassword;
    private static final String URL = "jdbc:sqlite:data.db";
    public static String init = "CREATE TABLE IF NOT EXISTS MT(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "SITE TEXT, " +
            "USER TEXT, " +
            "PASS TEXT, " +
            "NOTES TEXT)";

    private String Insert ="INSERT INTO MT(SITE,USER,PASS,NOTES) VALUES(?,?,?,?)";

    // The constructor initializes the DB if it did not exist
    public DBManager(String MasterPassword) {
        this.MasterPassword = MasterPassword;
        try{
            Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(init);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertRecord(String SITE, String USER, String PASS, String NOTES) throws CryptographyException {


        try {

            Connection conn = DriverManager.getConnection(URL);

            EncryptionHandler enc = new EncryptionHandler();
            enc.init(MasterPassword);

            PreparedStatement pstmt = conn.prepareStatement(Insert);
            pstmt.setString(1, enc.Encrypt(SITE));
            pstmt.setString(2, USER);
            pstmt.setString(3, PASS);
            pstmt.setString(4, NOTES);

            pstmt.executeUpdate();


        } catch (SQLException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException | CryptographyException | InvalidAlgorithmParameterException e) {

            System.out.println("SQL or Encryption Error");
            e.printStackTrace();
            throw new CryptographyException(e);

        }

    }

    public void deleteRecord(){}


    public void editRecord(){}


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
}
