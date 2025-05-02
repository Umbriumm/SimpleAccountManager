package org.example.model;

// Custom exception when the user or password fields are missing
// Will probably be useful for the view controller

public class InvalidInputException extends Exception{
    public InvalidInputException(){
        super("Username/Email and Password cannot be empty!");
    }
}
