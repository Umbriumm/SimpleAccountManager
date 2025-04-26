package org.example.model;
import java.util.List;

public interface Encryption {
    void saveToJson(List<AccountEntry> accountEntries, String masterPassword) throws Exception;


}