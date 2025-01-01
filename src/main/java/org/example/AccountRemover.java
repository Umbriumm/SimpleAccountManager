package org.example;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
public class AccountRemover extends AccountEntry implements Encryption  {



    public void removeByLabel(List<AccountEntry> AccountEntries,String label, String masterPassword) throws Exception {
        // Read all account entries from JSON
        List<AccountEntry> accountEntries = readFromJson();
        init(masterPassword);

        // Iterator to safely remove accounts while iterating
        Iterator<AccountEntry> iterator = accountEntries.iterator();
        int removedCount = 0;

        while (iterator.hasNext()) {
            AccountEntry account = iterator.next();
            if (account.getLabel().equalsIgnoreCase(label)) {
                iterator.remove(); // Remove matching account
                removedCount++;
            }
        }

        // Save updated list back to JSON
        if (removedCount > 0) {
            Gson gsonInstance = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter("Passwords.json")) {
                gsonInstance.toJson(accountEntries, writer);
            } catch (IOException ex) {
                throw new RuntimeException("Error while saving updated accounts to JSON", ex);
            }
            System.out.println(removedCount + " account(s) with label \"" + label + "\" have been removed.");
        } else {
            System.out.println("No accounts found with the label: \"" + label + "\".");
        }
    }
}






