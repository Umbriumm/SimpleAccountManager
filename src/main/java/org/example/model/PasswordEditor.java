package org.example.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.util.List;

// Class for editing account passwords
public class PasswordEditor extends EncryptionHandler {

    public PasswordEditor(String masterPassword) throws Exception {
        super();
        init(masterPassword);
    }

    // Edit a password based on the account label
    public void editPasswordByLabel(List<AccountEntry> accounts, String label, String newPassword) throws Exception {
        boolean accountFound = false;

        for (AccountEntry account : accounts) {
            if (account.getLabel().equalsIgnoreCase(label)) {
                account.setPassword(newPassword);

                // Encrypt the new password and set it
                String encryptedPassword = Encrypt(newPassword);
                account.setEncryptedpassword(encryptedPassword);

                accountFound = true;
                System.out.println("Password updated successfully for account with label: " + label);
                break;
            }
        }

        if (!accountFound) {
            System.out.println("No account found with the label: \"" + label + "\".");
        } else {
            // Save updated accounts to the JSON file
            saveAccountsToJson(accounts);
        }
    }

    // Save the account list to a JSON file
    private void saveAccountsToJson(List<AccountEntry> accounts) throws Exception {
        Gson gsonInstance = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("Passwords.json")) {
            gsonInstance.toJson(accounts, writer);
            System.out.println("Accounts saved successfully to JSON file.");
        }
    }
}
