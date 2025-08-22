package org.example.model;

import javafx.beans.property.StringProperty;

public interface ItemsGettersSetters {
    int getID();
    String getSite();
    void setSite(String value);
    StringProperty siteProperty();
    String getUsername();
    void setUsername(String value);
    StringProperty usernameProperty();
    String getPassword();
    void setPassword(String value);
    StringProperty passwordProperty();
    String getNotes();
    void setNotes(String value);
    StringProperty notesProperty();
}
