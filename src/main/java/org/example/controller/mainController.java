package org.example.controller;

import org.example.model.CryptographyException;
import org.example.model.DBManager;
import org.example.model.Item;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class mainController {

    DBManager DBM;
    private Connection connection;

    public mainController(String masterpassword) throws CryptographyException, NoSuchAlgorithmException {
        DBM = new DBManager(masterpassword);

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

    public Connection getConnection() {
        return DBM.getConnection();
    }

}
