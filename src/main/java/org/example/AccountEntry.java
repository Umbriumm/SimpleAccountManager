package org.example;

import javax.crypto.EncryptedPrivateKeyInfo;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class AccountEntry extends EncryptionHandler implements Encryption {
    private  String service;
    private  String label;
    private String Username;
    private transient String Password;
    private String Encryptedpassword;
    private transient String enCodedIV;
    private transient String MasterPassword;

    // Transient : field should NOT be serialized (tansformation of object states / values to a byte stream)

    public AccountEntry(){
        super();

    }
    public  AccountEntry(String label, String service, String Username, String Password,String MasterPassword) {

        this.label = label;
        this.service = service;
        this.Username = Username;
        this.Password = Password;
        this.MasterPassword=MasterPassword;
    }

    public  AccountEntry(String label, String service, String Username, String Password) {
        this.label = label;
        this.service = service;
        this.Username = Username;
        this.Password = Password;
    }

    public void setPassword(String password) {

        Password = password;
    }


    public String getLabel() {
        return label;
    }



    public void setEncryptedpassword(String encryptedpassword) {
        Encryptedpassword = encryptedpassword;
    }


    @Override
    public void saveToJson(List<AccountEntry> AccountEntries,String Masterpassword) throws Exception {
        init(Masterpassword);
        Encryptedpassword = Encrypt(this.Password);
        enCodedIV=Encryptedpassword.split(":")[0];

        Gson gsonInstance = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("Passwords.json")) {
            gsonInstance.toJson(AccountEntries, writer);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static List<AccountEntry> readFromJson() {
        Gson gsonInstance = new Gson();
        try (FileReader reader = new FileReader("Passwords.json")) {
            Type listType=new TypeToken<List<AccountEntry>>() {}.getType();
            return gsonInstance.fromJson(reader, listType);
        } catch (IOException ex) {
            System.out.println("Read failed returning empty array list ");
            return new  ArrayList<>();
        }
    }

    public void print() {
        System.out.println("Label: " + label);
        System.out.println("Service: " + service);
        System.out.println("Username: " + Username);
        System.out.println("Password: " + Password);
    }
    public static boolean  loadByLabel(String label){
        List<AccountEntry> accountEntries=readFromJson();
        for (AccountEntry account : accountEntries) {
            if (account.getLabel().equalsIgnoreCase(label)){
                return true;
            }
        }

        return false;
    }

    public AccountEntry loadByLabel(String label, String masterPassword) throws Exception {
        List<AccountEntry> accountEntries = readFromJson();
        init(masterPassword);

        for (AccountEntry account : accountEntries) {
            if (account.getLabel().equalsIgnoreCase(label)) {
            try {
                String decryptedPassword = Decrypt(account.Encryptedpassword);
                account.setPassword(decryptedPassword);
                System.out.println("Account found and decrypted:");
                account.print();
                return account;
            } catch (Exception e) {
                throw new Exception("Decryption Failed! - Incorrect Master Password for Account with Label: " + label);
            }
            }
        }
        return null;
    }

}
