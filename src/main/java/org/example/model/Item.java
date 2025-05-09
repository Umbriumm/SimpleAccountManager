package org.example.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {
    private int ID;
    private final StringProperty site;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty notes;

    public Item(int id, String site, String username, String password, String notes) {
        this.ID = id;
        this.site = new SimpleStringProperty(site);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.notes = new SimpleStringProperty(notes);
    }

    public Item(String site, String username, String password, String notes) {
        this.ID = -1;
        this.site = new SimpleStringProperty(site);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.notes = new SimpleStringProperty(notes);
    }

    public int getID() { return ID; }

    public String getSite() { return site.get(); }
    public void setSite(String value) { site.set(value); }
    public StringProperty siteProperty() { return site; }

    public String getUsername() { return username.get(); }
    public void setUsername(String value) { username.set(value); }
    public StringProperty usernameProperty() { return username; }

    public String getPassword() { return password.get(); }
    public void setPassword(String value) { password.set(value); }
    public StringProperty passwordProperty() { return password; }

    public String getNotes() { return notes.get(); }
    public void setNotes(String value) { notes.set(value); }
    public StringProperty notesProperty() { return notes; }
}