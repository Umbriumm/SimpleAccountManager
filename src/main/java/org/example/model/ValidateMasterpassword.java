package org.example.model;

import com.sun.net.httpserver.Headers;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class ValidateMasterpassword {

    private static final String header = "TEST_HEADER_VALUE";

    public boolean isValid(DBManager dbm, String MP) throws SQLException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, CryptographyException, InvalidKeyException {

        try {
            EncryptionHandler enc = new EncryptionHandler();
            enc.init(MP);

            String encryptedHeader = dbm.getHeader();

            if (encryptedHeader == null) {
                String newHeader = enc.Encrypt(header);
                dbm.insertHeader(newHeader);
                return true; // This is for first time setup
            }

            return (enc.Decrypt(encryptedHeader) == header);

        } catch (InvalidKeyException e) {
            return false;  // Key is invalid, cannot decrypt
        }

    }

}
