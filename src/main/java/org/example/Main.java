package org.example;
import org.example.model.DBManager;
import org.example.model.InvalidInputException;
import org.example.model_old.AccountEntry;
import org.example.model_old.AccountRemover;
import org.example.model_old.PasswordEditor;

import java.util.*;

// This is the testing grounds for now
// !!!! THIS IS EXPERIMENTAL !!!! //

public class Main {
    public static void main(String[] args) {

//        DBManager db = new DBManager();
//        try {
//            db.insertRecord("","i","p","");
//        } catch (InvalidInputException e) {
//            System.out.println("s");
//        }



        Scanner scanner = new Scanner(System.in);


        System.out.println("Enter Master Password for Encryption/Decryption:");
        String masterPassword = scanner.nextLine();

        while (true) {
            System.out.print("Choose an option: ");
            System.out.println("\n1. Add a new account");
            System.out.println("2. Retrieve account by label");
            System.out.println("3. Edit the password by label");
            System.out.println("4. Remove account by label");
            System.out.println("5. Exit");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.println("\nEnter account details:");

                System.out.print("Enter Label: ");
                System.out.println("Make sure every LABEL is unique per entry! ");
                String label = scanner.nextLine();

                System.out.print("Enter Service Name: ");
                String service = scanner.nextLine();

                System.out.print("Enter Username: ");
                String username = scanner.nextLine();

                System.out.print("Enter Password: ");
                String password = scanner.nextLine();

                System.out.print("Feel free to add any notes to this entry, if not, leave it empty");
                String Notes = scanner.nextLine();

               try (DBManager DBM = new DBManager(masterPassword)) {
                   DBM.insertRecord(service, username, password, Notes);
               } catch (Exception e) {
                   throw new RuntimeException(e);
               }

            } else if (choice.equals("2")) {

                System.out.print("\nEnter the label of the account to retrieve: ");
                String accountLabel = scanner.nextLine();

                try {

                    AccountEntry account = new AccountEntry();
                    account.loadByLabel(accountLabel, masterPassword);
                    if (account != null) {
                        System.out.println("-----------------------------------------");
                    } else {
                        System.out.println("No account found with the label: " + accountLabel);
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }



            } else if (choice.equals("3")) {

                // Handle password editing
                System.out.print("\nEnter the label of the account to edit the password: ");
                String accountLabel = scanner.nextLine();

                System.out.print("Enter the new password: ");
                String newPassword = scanner.nextLine();

                try {
                    AccountEntry accountEntry=new AccountEntry();
                    List<AccountEntry> accounts = accountEntry.readFromJson();
                    PasswordEditor passwordEditor = new PasswordEditor(masterPassword);
                    passwordEditor.editPasswordByLabel(accounts, accountLabel, newPassword);
                } catch (Exception e) {
                    System.out.println("Failed to edit the password: " + e.getMessage());
                    e.printStackTrace();
                }

            }else if (choice.equals("4")) {

                System.out.print("\nEnter the label of the account(s) to remove: ");
                String labelToRemove = scanner.nextLine();

                try {
                    AccountEntry accountEntry=new AccountEntry();
                    AccountRemover accountRemover=new AccountRemover();
                    List<AccountEntry> accounts = accountEntry.readFromJson();
                    accountRemover.removeByLabel(accounts, labelToRemove, masterPassword);
                } catch (Exception e) {
                    System.out.println("Error while removing the account: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            else if (choice.equals("5")) {
                System.out.println("🙏");
                System.exit(0);
            } else {
                System.out.println("????????????????????????????????");
            }
        }


    }
}