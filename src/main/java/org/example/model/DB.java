package org.example.model;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public interface DB {


    void insertRecord(String SITE, String USER, String PASS, String NOTES) throws CryptographyException;
    void deleteRecord(int id) throws SQLException;
    void updateRecord(int ID, String SITE, String USER, String PASS, String NOTES) throws CryptographyException;
    List<Item> getAllItems();
    void close() throws Exception;
    String retrieve(String s) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
}
