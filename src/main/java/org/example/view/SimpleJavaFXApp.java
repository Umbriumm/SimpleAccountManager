package org.example.view;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import org.example.model.Item;
import org.example.controller.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.model.CryptographyException;
import javafx.stage.Modality;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class SimpleJavaFXApp extends Application {
    mainController controller;
    ObservableList<Item> dataL = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
       //controller.
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
        table.setItems(dataL);
        table.setPlaceholder(new Label("TABLE"));

        // ----------------- Scene 2: Dashboard Table -----------------
        Button addBtn = new Button("ADD");
        Button delBtn = new Button("Delete");
        Button editBtn = new Button("EDIT");
        Button detailsBtn = new Button("DETAILS");
        Button exitBtn = new Button("EXIT");
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(300);
        Button searchButton = new Button("Search");


        Button clearButton = new Button("✕");
        clearButton.setStyle("-fx-background-radius: 5em;");
        clearButton.setStyle("-fx-background-color: transparent; -fx-padding: 0 5 0 0;");
        clearButton.setVisible(false);

        // StackPane to overlay the button on the TextField
        StackPane SearchPane = new StackPane();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().add(clearButton);
        SearchPane.getChildren().addAll(searchField, hBox);

        // Make the button appear only when there's text
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            clearButton.setVisible(!newVal.isEmpty());
        });

        // Clear the text when the button is clicked
        clearButton.setOnAction(e ->{

                searchField.clear();
                dataL.clear();
                dataL.addAll(controller.loadAllItems());

        });

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        ToolBar toolbar = new ToolBar(addBtn, delBtn, editBtn, detailsBtn,new Separator(),SearchPane ,searchField,searchButton, new Separator(),spacer, exitBtn);


        VBox layout2 = new VBox(toolbar, table);
        layout2.setSpacing(10);
        Scene scene2 = new Scene(layout2, 700, 500);

        // ----------------- Button Actions -----------------
        enter.setOnAction(e -> {
            try {
                controller = new mainController(mp.getText());
            } catch (CryptographyException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
            dataL.clear();
            dataL.addAll(controller.loadAllItems());

            stage.setScene(scene2);
            stage.setTitle("Dashboard");
        });

        exitBtn.setOnAction(e -> {
            stage.setScene(scene1);
            stage.setTitle("Code Program");
        });

        addBtn.setOnAction(e -> openAddWindow(stage));
        editBtn.setOnAction(e -> {
            Item SelectedItem = table.getSelectionModel().getSelectedItem();
            if (SelectedItem != null) {
                openEditWindow(stage, SelectedItem);
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a row to edit").showAndWait();
            }
        });

        delBtn.setOnAction(e -> {
            Item SelectedItem = table.getSelectionModel().getSelectedItem();
            if (SelectedItem != null) {
                openDeleteConfirmation(stage, SelectedItem);
            } else {
                new Alert(Alert.AlertType.WARNING, "Select an item to delete").showAndWait();
            }
        });
        detailsBtn.setOnAction(e -> {
            Item selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showDetailsPopup(selected);
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a row to view details").showAndWait();
            }
        });
        // -------------------Search --------------------
        searchButton.setOnAction(e -> {

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

            if (sl.getText().trim().isEmpty() || use.getText().trim().isEmpty() || ps.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Site, Username, and Password are required.").showAndWait();
                return; // Stop if fields are empty
            }

            try {
                controller.insertRecord(sl.getText(), use.getText(), ps.getText(), no.getText());
            } catch (CryptographyException ignored) {}
            popup.close();

            dataL.clear();
            dataL.addAll(controller.loadAllItems());
        });

        back.setOnAction(e -> popup.close());

        popup.setScene(new Scene(grid, 400, 300));
        popup.showAndWait();
    }

    private void openEditWindow(Stage owner, Item SelectedItem) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);
        popup.setTitle("Edit Account");

        TextField sl = new TextField(SelectedItem.getSite());
        TextField use = new TextField(SelectedItem.getUsername());
        TextField ps = new TextField(SelectedItem.getPassword());
        TextField no = new TextField(SelectedItem.getNotes());

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

            if (sl.getText().trim().isEmpty() || use.getText().trim().isEmpty() || ps.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Site, Username, and Password are required.").showAndWait();
                return; // Stop if fields are empty
            }

            try {
                controller.updateRecord(SelectedItem.getID(), sl.getText(), use.getText(), ps.getText(), no.getText());
            } catch (CryptographyException ex) {
                throw new RuntimeException(ex);
            }

            popup.close();

            dataL.clear();
            dataL.addAll(controller.loadAllItems());
        });

        back.setOnAction(e -> popup.close());

        popup.setScene(new Scene(grid, 400, 300));
        popup.showAndWait();
    }

    private void openDeleteConfirmation(Stage owner, Item SelectedItem) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);
        popup.setTitle("Confirm Delete");

        Label msg = new Label("Are you sure you want to delete this entry?");
        Button yes = new Button("Yes");
        Button no = new Button("No");

        yes.setOnAction(e -> {
            try {
                controller.deleteRecord(SelectedItem.getID());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            popup.close();

            dataL.clear();
            dataL.addAll(controller.loadAllItems());
        });

        no.setOnAction(e -> popup.close());

        HBox btnBox = new HBox(20, yes, no);
        btnBox.setAlignment(Pos.CENTER);
        VBox layout = new VBox(20, msg, btnBox);
        layout.setAlignment(Pos.CENTER);

        popup.setScene(new Scene(layout, 350, 150));
        popup.showAndWait();
    }

    private void showDetailsPopup(Item item) {
        Stage detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.setTitle("Details");

        // Left column
        Label userLabel = new Label("Username");
        TextField userField = new TextField(item.getUsername());
        userField.setEditable(false);
        userField.setPrefWidth(200);

        Label passLabel = new Label("Password");
        PasswordField passField = new PasswordField();
        passField.setText(item.getPassword());
        passField.setEditable(false);
        passField.setPrefWidth(200);

        VBox leftBox = new VBox(15, userLabel, userField, passLabel, passField);
        leftBox.setAlignment(Pos.TOP_LEFT);

        // Right column
        Label siteLabel = new Label("Site");
        TextField siteField = new TextField(item.getSite());
        siteField.setEditable(false);
        siteField.setPrefWidth(200);

        Label noteLabel = new Label("Note");
        TextField noteField = new TextField(item.getNotes());
        noteField.setEditable(false);
        noteField.setPrefWidth(200);

        VBox rightBox = new VBox(15, siteLabel, siteField, noteLabel, noteField);
        rightBox.setAlignment(Pos.TOP_LEFT);

        HBox mainContent = new HBox(30, leftBox, rightBox);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.CENTER);

        Button closeBtn = new Button("Close");
        HBox btnBox = new HBox(closeBtn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(10));

        closeBtn.setOnAction(e -> detailStage.close());

        VBox layout = new VBox(20, mainContent, btnBox);
        layout.setPadding(new Insets(10));
        Scene scene = new Scene(layout, 500, 300);
        detailStage.setScene(scene);
        detailStage.showAndWait();
    }

    // ----------------- Helper Class -----------------


    public static void main(String[] args) {
        launch();
    }
}
