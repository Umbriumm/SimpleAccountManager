package org.example.model;

public class CryptographyException extends Exception{
    public CryptographyException(Throwable x){
        super("Cryptography Exception", x);
    }
}
