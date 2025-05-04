package org.example.view;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimpleJavaFXApp extends Application {
    String passss = "s";

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
        TableColumn<Item, String> col1 = new TableColumn<>("Service / Label");
        col1.setCellValueFactory(new PropertyValueFactory<>("label"));
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

        // ----------------- Scene 3: Edit Account -----------------
        TextField sl = new TextField();
        TextField use = new TextField();
        TextField ps = new TextField();
        TextField no = new TextField();
        no.setEditable(false);
        Button save = new Button("Save");
        Button back = new Button("Back");
        HBox editBtnBox = new HBox(10, save, back); // Aligned next to each other

        GridPane s3 = new GridPane();
        s3.add(new Label("Service / Label"), 0, 0);
        s3.add(sl, 1, 0);
        s3.add(new Label("Username"), 0, 1);
        s3.add(use, 1, 1);
        s3.add(new Label("Password"), 0, 2);
        s3.add(ps, 1, 2);
        s3.add(new Label("Notes"), 0, 3);
        s3.add(no, 1, 3);
        s3.add(save, 1, 4);
        s3.add(back, 2, 4);
        s3.setAlignment(Pos.CENTER);
        s3.setHgap(10);
        s3.setVgap(10);
        Scene scene3 = new Scene(s3, 600, 400);
        s3.add(editBtnBox, 1, 4);


        // ----------------- Scene 4: Add Account -----------------
        TextField slAdd = new TextField();
        TextField useAdd = new TextField();
        TextField psAdd = new TextField();
        TextField noAdd = new TextField();
        Button saveAdd = new Button("Save");
        Button backAdd = new Button("Back");
        HBox addBtnBox = new HBox(10, saveAdd, backAdd); // Aligned next to each other


        GridPane s4 = new GridPane();
        s4.add(new Label("Service / Label"), 0, 0);
        s4.add(slAdd, 1, 0);
        s4.add(new Label("Username"), 0, 1);
        s4.add(useAdd, 1, 1);
        s4.add(new Label("Password"), 0, 2);
        s4.add(psAdd, 1, 2);
        s4.add(new Label("Notes"), 0, 3);
        s4.add(noAdd, 1, 3);
        s4.add(saveAdd, 1, 4);
        s4.add(backAdd, 2, 4);
        s4.setAlignment(Pos.CENTER);
        s4.setHgap(10);
        s4.setVgap(10);
        Scene scene4 = new Scene(s4, 600, 400);
        s4.add(addBtnBox, 1, 4);


        // ----------------- Scene 5: Delete Confirmation -----------------
        Label delLabel = new Label("Are you sure you want to delete the selected item?");
        Button yes = new Button("Yes");
        Button noBtn = new Button("No"); // renamed to avoid variable conflict

        HBox deleteBtns = new HBox(20, yes, noBtn);
        deleteBtns.setAlignment(Pos.CENTER);

        VBox s5 = new VBox(20, delLabel, deleteBtns);
        s5.setAlignment(Pos.CENTER);
        Scene scene5 = new Scene(s5, 400, 200);


        // ----------------- Button Actions -----------------
        enter.setOnAction(e -> {
            if (mp.getText().equals(passss)) {
                stage.setScene(scene2);
                stage.setTitle("Dashboard");
            } else {
                new Alert(Alert.AlertType.ERROR, "Wrong Password!").showAndWait();
            }
        });

        exitBtn.setOnAction(e -> {
            stage.setScene(scene1);
            stage.setTitle("Code Program");
        });

        addBtn.setOnAction(e -> {
            slAdd.clear();
            useAdd.clear();
            psAdd.clear();
            noAdd.clear();
            stage.setScene(scene4);
            stage.setTitle("Add Account");
        });

        saveAdd.setOnAction(e -> {
            data.add(new Item(slAdd.getText(), useAdd.getText(), psAdd.getText(), noAdd.getText()));
            stage.setScene(scene2);
        });

        backAdd.setOnAction(e -> stage.setScene(scene2));

        editBtn.setOnAction(e -> {
            Item selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                sl.setText(selected.getLabel());
                use.setText(selected.getUsername());
                ps.setText(selected.getPassword());
                no.setText(selected.getNotes());
                stage.setScene(scene3);
                stage.setTitle("Edit Account");
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a row to edit").showAndWait();
            }
        });

        save.setOnAction(e -> {
            Item selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setLabel(sl.getText());
                selected.setUsername(use.getText());
                selected.setPassword(ps.getText());
                selected.setNotes(no.getText());
                table.refresh();
                stage.setScene(scene2);
            }
        });

        back.setOnAction(e -> stage.setScene(scene2));

        delBtn.setOnAction(e -> {
            Item selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                stage.setScene(scene5);
                stage.setTitle("Confirm Delete");
            } else {
                new Alert(Alert.AlertType.WARNING, "Select an item to delete").showAndWait();
            }
        });

        yes.setOnAction(e -> {
            Item selected = table.getSelectionModel().getSelectedItem();
            data.remove(selected);
            stage.setScene(scene2);
        });

        noBtn.setOnAction(e -> stage.setScene(scene2));

        // ----------------- Launch -----------------
        stage.setTitle("Code Program");
        stage.setScene(scene1);
        stage.show();
    }

    // ----------------- Helper Class -----------------
    public static class Item {
        private String label, username, password, notes;

        public Item(String label, String username, String password, String notes) {
            this.label = label;
            this.username = username;
            this.password = password;
            this.notes = notes;
        }

        public String getLabel() { return label; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getNotes() { return notes; }

        public void setLabel(String label) { this.label = label; }
        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static void main(String[] args) {
        launch();
    }
}
