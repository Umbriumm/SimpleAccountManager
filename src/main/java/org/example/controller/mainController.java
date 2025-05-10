package org.example.controller;

import org.example.model.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class mainController {

    EncryptionHandler enc = new EncryptionHandler();
    DBManager DBM;
    ValidateMasterpassword VMP = new ValidateMasterpassword();
    private Connection connection;

    public mainController(String masterpassword) throws CryptographyException, NoSuchAlgorithmException {
        DBM = new DBManager(masterpassword);
        enc.init(masterpassword);
    }



    public void insertRecord(String Site, String Username, String Password, String Notes) throws CryptographyException{
        DBM.insertRecord(Site, Username, Password, Notes);
    }

    public void updateRecord(int id, String Site, String Username, String Password, String Notes) throws CryptographyException {
        DBM.updateRecord(id, Site, Username, Password, Notes);
    }

    public void deleteRecord(int id) throws SQLException {
        DBM.deleteRecord(id);
    }

    public List<Item> loadAllItems(){
        return DBM.getAllItems();
    }

    public List<Item> searchResult(String searchTerm){
        return DBM.Search(searchTerm);
    }

    public String retrieve(String s) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return (enc.Decrypt(s));
    }

    public Connection getConnection() {
        return DBM.getConnection();
    }

    public boolean Validate() throws InvalidAlgorithmParameterException, CryptographyException, SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return VMP.isValid(DBM, DBM.getMasterPassword());
    }

    public boolean selfDestruct() throws IOException, SQLException {
       return DBM.selfDestruct();
    }

}
