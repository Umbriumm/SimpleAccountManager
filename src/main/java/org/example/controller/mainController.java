package org.example.controller;

import org.example.model.CryptographyException;
import org.example.model.DBManager;

import java.sql.Connection;

public class mainController {

    DBManager DBM;
    private Connection connection;

    public mainController(String masterpassword){
        DBM = new DBManager(masterpassword);

    }



    public void insertRecord(String Site, String Username, String Password, String Notes) throws CryptographyException{
        DBM.insertRecord(Site, Username, Password, Notes);
    }

    public static void editRecord(int id){
        // edit method
    }

    public static void deleteRecord(int id){
        // delete method
    }

    public Connection getConnection() {
        return DBM.getConnection();
    }

}
