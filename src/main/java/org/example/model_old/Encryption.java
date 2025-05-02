package org.example.model_old;
import java.util.List;

public interface Encryption {
    void saveToJson(List<AccountEntry> accountEntries, String masterPassword) throws Exception;


}