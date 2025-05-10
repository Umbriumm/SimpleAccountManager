package org.example.model;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface EncryptionMethods {
    void init(String password) throws CryptographyException, NoSuchAlgorithmException;
    String Encrypt(String decryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException;
    String Decrypt(String encryptedData) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException;
    String encode(byte[] data);
}
