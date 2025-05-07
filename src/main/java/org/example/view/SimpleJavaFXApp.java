package org.example.view;

import org.example.controller.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.CryptographyException;
import javafx.stage.Modality;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SimpleJavaFXApp extends Application {
    String passss = "s";
    mainController controller;
    ObservableList<Item> data = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        // ----------------- Scene 1: Password Entry -----------------
        PasswordField mp = new PasswordField();
        Label eymp = new Label("Enter Your Master Password");
        Button enter = new Button("Enter");

        GridPane sc1 = new GridPane();
        sc1.add(eymp, 1, 0);
        sc1.add(mp, 1, 1);
        sc1.add(enter, 1, 2);
        sc1.setAlignment(Pos.CENTER);
        sc1.setHgap(10);
        sc1.setVgap(10);
        Scene scene1 = new Scene(sc1, 500, 400);

        // ----------------- TableView (Shared) -----------------
        TableView<Item> table = new TableView<>();
        TableColumn<Item, String> col1 = new TableColumn<>("Site");
        col1.setCellValueFactory(new PropertyValueFactory<>("site"));
        TableColumn<Item, String> col2 = new TableColumn<>("Username");
        col2.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<Item, String> col3 = new TableColumn<>("Password");
        col3.setCellValueFactory(new PropertyValueFactory<>("password"));
        TableColumn<Item, String> col4 = new TableColumn<>("Notes");
        col4.setCellValueFactory(new PropertyValueFactory<>("notes"));

        table.getColumns().addAll(col1, col2, col3, col4);
        table.setItems(data);
        table.setPlaceholder(new Label("TABLE"));

        // ----------------- Scene 2: Dashboard Table -----------------
        Button addBtn = new Button("ADD");
        Button delBtn = new Button("Delete");
        Button editBtn = new Button("EDIT");
        Button exitBtn = new Button("EXIT");

        ToolBar toolbar = new ToolBar(addBtn, delBtn, editBtn, new Separator(), exitBtn);
        VBox layout2 = new VBox(toolbar, table);
        layout2.setSpacing(10);
        Scene scene2 = new Scene(layout2, 700, 500);

        // ----------------- Button Actions -----------------
        enter.setOnAction(e -> {
            controller = new mainController(mp.getText());
            stage.setScene(scene2);
            stage.setTitle("Dashboard");
        });

        exitBtn.setOnAction(e -> {
            stage.setScene(scene1);
            stage.setTitle("Code Program");
        });

        addBtn.setOnAction(e -> openAddWindow(stage));
        editBtn.setOnAction(e -> {
            Item selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                openEditWindow(stage, selected);
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a row to edit").showAndWait();
            }
        });

        delBtn.setOnAction(e -> {
            Item selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                openDeleteConfirmation(stage, selected);
            } else {
                new Alert(Alert.AlertType.WARNING, "Select an item to delete").showAndWait();
            }
        });

        // ----------------- Launch -----------------
        stage.setTitle("Code Program");
        stage.setScene(scene1);
        stage.show();
    }

    private void openAddWindow(Stage owner) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);
        popup.setTitle("Add Account");

        TextField sl = new TextField();
        TextField use = new TextField();
        TextField ps = new TextField();
        TextField no = new TextField();

        Button save = new Button("Save");
        Button back = new Button("Back");
        HBox btnBox = new HBox(10, save, back);
        btnBox.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.add(new Label("Site"), 0, 0);
        grid.add(sl, 1, 0);
        grid.add(new Label("Username"), 0, 1);
        grid.add(use, 1, 1);
        grid.add(new Label("Password"), 0, 2);
        grid.add(ps, 1, 2);
        grid.add(new Label("Notes"), 0, 3);
        grid.add(no, 1, 3);
        grid.add(btnBox, 1, 4);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        save.setOnAction(e -> {
            Item newItem = new Item(sl.getText(), use.getText(), ps.getText(), no.getText());
//            ((SimpleJavaFXApp) Application.getUserAgentStylesheet()).data.add(newItem);    Working to solve this part
            data.add(newItem); // just use it directly
            try {
                controller.insertRecord(sl.getText(), use.getText(), ps.getText(), no.getText());
            } catch (CryptographyException ignored) {}
            popup.close();
        });

        back.setOnAction(e -> popup.close());

        popup.setScene(new Scene(grid, 400, 300));
        popup.showAndWait();
    }

    private void openEditWindow(Stage owner, Item item) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);
        popup.setTitle("Edit Account");

        TextField sl = new TextField(item.getSite());
        TextField use = new TextField(item.getUsername());
        TextField ps = new TextField(item.getPassword());
        TextField no = new TextField(item.getNotes());

        Button save = new Button("Save");
        Button back = new Button("Back");
        HBox btnBox = new HBox(10, save, back);
        btnBox.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.add(new Label("Site"), 0, 0);
        grid.add(sl, 1, 0);
        grid.add(new Label("Username"), 0, 1);
        grid.add(use, 1, 1);
        grid.add(new Label("Password"), 0, 2);
        grid.add(ps, 1, 2);
        grid.add(new Label("Notes"), 0, 3);
        grid.add(no, 1, 3);
        grid.add(btnBox, 1, 4);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        save.setOnAction(e -> {
            item.setSite(sl.getText());
            item.setUsername(use.getText());
            item.setPassword(ps.getText());
            item.setNotes(no.getText());
            popup.close();
        });

        back.setOnAction(e -> popup.close());

        popup.setScene(new Scene(grid, 400, 300));
        popup.showAndWait();
    }

    private void openDeleteConfirmation(Stage owner, Item item) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);
        popup.setTitle("Confirm Delete");

        Label msg = new Label("Are you sure you want to delete this entry?");
        Button yes = new Button("Yes");
        Button no = new Button("No");

        yes.setOnAction(e -> {
            data.remove(item);
            popup.close();
        });

        no.setOnAction(e -> popup.close());

        HBox btnBox = new HBox(20, yes, no);
        btnBox.setAlignment(Pos.CENTER);
        VBox layout = new VBox(20, msg, btnBox);
        layout.setAlignment(Pos.CENTER);

        popup.setScene(new Scene(layout, 350, 150));
        popup.showAndWait();
    }

    // ----------------- Helper Class -----------------
    public static class Item {
        private final StringProperty site;
        private final StringProperty username;
        private final StringProperty password;
        private final StringProperty notes;

        public Item(String site, String username, String password, String notes) {
            this.site = new SimpleStringProperty(site);
            this.username = new SimpleStringProperty(username);
            this.password = new SimpleStringProperty(password);
            this.notes = new SimpleStringProperty(notes);
        }

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

    public static void main(String[] args) {
        launch();
    }
}
