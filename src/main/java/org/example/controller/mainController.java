package org.example.controller;

import org.example.model.DBManager;

public class mainController {

    static DBManager DBM = new DBManager(DBManager.getMasterPassword());

    // Exceptions will be rethrown from here and caught by the UI

    public static void insertRecord(String Label, String Username, String Password, String Notes) throws Exception {
        DBM.insertRecord(Label, Username, Password, Notes);
    }

    public static void editRecord(int id){
        // edit method
    }

    public static void deleteRecord(int id){
        // delete method
    }
}
