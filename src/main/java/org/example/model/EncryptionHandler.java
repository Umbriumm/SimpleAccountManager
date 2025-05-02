package org.example.model;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

class EncryptionHandler {
    protected transient SecretKey key;
    protected transient Cipher encryptionCipher;    // Must be transient because Gson cannot access it
    protected transient String EncodedIV;

    public EncryptionHandler() {
    }

    // Pass a MasterPassword to the handler to initialize
    public void init(String password) throws Exception {
        // Derive a 256-bit key from the password using SHA-256 hash
        key = new SecretKeySpec(sha256(password), "AES");
    }

    // Derive an encryptionKey from the MasterPassword
    private byte[] sha256(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(password.getBytes(StandardCharsets.UTF_8));
    }

    // Encrypt the entries
    public String Encrypt(String decryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] messageInBytes = decryptedData.getBytes(StandardCharsets.UTF_8);
        encryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        encryptionCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        EncodedIV = encode(iv); // Base64-encode the IV
        byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
        return EncodedIV + ":" +encode(encryptedBytes); // Return the Base64-encoded encrypted message
    }

    // Decrypt the message
    public String Decrypt(String encryptedData) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {

        String[] parts = encryptedData.split(":");
        String EncodedIV = parts[0];
        String cipherText = parts[1];
        byte[] initializationVectorinBytes = decode(parts[0]);
        byte[] cipherTextinBytes = decode(parts[1]); // Decode the Base64-encoded encrypted message

        IvParameterSpec ivspec = new IvParameterSpec(initializationVectorinBytes);
        Cipher decryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decryptionCipher.init(Cipher.DECRYPT_MODE, key, ivspec);
        // Decrypt the message
        byte[] decryptedBytes = decryptionCipher.doFinal(cipherTextinBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8); // Return the decrypted message as a string
    }
    // Encoding and Decoding methods to transform data into a printable form
    public String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    public static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }
}
