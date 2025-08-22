package org.example.view;

import javafx.geometry.HPos;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
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
        Button wipe = new Button("Forgot your password?");

        GridPane sc1 = new GridPane();
        sc1.add(eymp, 1, 0);
        sc1.add(mp, 1, 1);
        sc1.add(enter, 1, 2);
        sc1.add(wipe, 1, 3);
        sc1.setAlignment(Pos.CENTER);
        GridPane.setHalignment(enter, HPos.CENTER);
        GridPane.setHalignment(wipe, HPos.CENTER);
        sc1.setHgap(10);
        sc1.setVgap(10);
        Scene scene1 = new Scene(sc1, 500, 400);

        // ----------------- TableView (Shared) -----------------
        TableView<Item> table = new TableView<>();
        TableColumn<Item, String> col1 = new TableColumn<>("Site");
        col1.setCellValueFactory(new PropertyValueFactory<>("site"));
        TableColumn<Item, String> col2 = new TableColumn<>("Username");
        col2.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<Item, String> col3 = new TableColumn<>("Notes");
        col3.setCellValueFactory(new PropertyValueFactory<>("notes"));

        table.getColumns().addAll(col1, col2, col3);
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
        clearButton.setOnAction(e -> {

            searchField.clear();
            dataL.clear();
            dataL.addAll(controller.loadAllItems());

        });

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        ToolBar toolbar = new ToolBar(addBtn, editBtn, detailsBtn, delBtn, new Separator(), SearchPane, searchField, searchButton, new Separator(), spacer, exitBtn);

        VBox layout2 = new VBox(toolbar, table);
        layout2.setSpacing(10);
        VBox.setVgrow(table, Priority.ALWAYS);
        Scene scene2 = new Scene(layout2, 700, 500);
        // ------------------ CSS Applying -----------------------
        scene1.getStylesheets().add((new File("src/main/java/org/example/view/style.css")).toURI().toString());
        scene2.getStylesheets().add((new File("src/main/java/org/example/view/style.css")).toURI().toString());

        // ----------------- Button Actions -----------------
        enter.setOnAction(e -> {
            try {
                controller = new mainController(mp.getText());

                controller.Validate();
//                if (controller.Validate() != true){    // 117 should already do the trick
//
//                }

            } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | SQLException |
                     NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | CryptographyException |
                     InvalidKeyException ex) {

                Alert alert = new Alert(Alert.AlertType.WARNING, "An error occured while trying to decrypt");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add((new File("src/main/java/org/example/view/style.css")).toURI().toString());
                alert.showAndWait();
                return;


            }
            dataL.clear();
            dataL.addAll(controller.loadAllItems());
            Sort.sortItemsBySite(dataL);

            stage.setScene(scene2);
            stage.setTitle("Dashboard");
        });

        wipe.setOnAction(e -> {
            try {
                controller = new mainController(mp.getText());
            } catch (CryptographyException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Confirmation");
            inputDialog.setHeaderText("Are you sure you want to delete all data?");
            inputDialog.setContentText("Type 'yes' to confirm:");

            String result = inputDialog.showAndWait().get();

            if (result != null && result.equalsIgnoreCase("yes")) {
                // Proceed with reset
                System.out.println("Data will be deleted.");
                try {
                    controller.selfDestruct();
                } catch (IOException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                // Handle cancellation or incorrect input
                System.out.println("Action canceled or invalid input.");
            }
        });

        exitBtn.setOnAction(e -> {
            stage.setScene(scene1);
            stage.setTitle("Code Program");
        });

        addBtn.setOnAction(e -> openAddWindow(stage));
        editBtn.setOnAction(e -> {
            Item SelectedItem = table.getSelectionModel().getSelectedItem();
            if (SelectedItem != null) {
                try {
                    openEditWindow(stage, SelectedItem);
                } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                         NoSuchAlgorithmException | BadPaddingException | InvalidKeyException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a row to edit");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add((new File("src/main/java/org/example/view/style.css")).toURI().toString());
                alert.showAndWait();
            }
        });

        delBtn.setOnAction(e -> {
            Item SelectedItem = table.getSelectionModel().getSelectedItem();
            if (SelectedItem != null) {
                openDeleteConfirmation(stage, SelectedItem);
            } else {
                Alert alert1 = new Alert(Alert.AlertType.WARNING, "Select an item to delete");
                DialogPane dialogPane1 = alert1.getDialogPane();
                dialogPane1.getStylesheets().add((new File("src/main/java/org/example/view/style.css")).toURI().toString());
                alert1.showAndWait();
            }
        });
        detailsBtn.setOnAction(e -> {
            Item selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    showDetailsPopup(selected);
                } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                         NoSuchAlgorithmException | BadPaddingException | InvalidKeyException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                Alert alert2 = new Alert(Alert.AlertType.WARNING, "Please select a row to view details");
                DialogPane dialogPane2 = alert2.getDialogPane();
                dialogPane2.getStylesheets().add((new File("src/main/java/org/example/view/style.css")).toURI().toString());
                alert2.showAndWait();

            }
        });
        // -------------------Search --------------------
        searchButton.setOnAction(e -> {
            dataL.clear();
            dataL.addAll(controller.searchResult(searchField.getText()));
            Sort.sortItemsBySite(dataL);
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
        PasswordField maskedPs = new PasswordField();
        TextField unmaskedPs = new TextField();
        TextField no = new TextField();

        maskedPs.setPrefWidth(200);
        unmaskedPs.setPrefWidth(200);
        unmaskedPs.setVisible(false);
        unmaskedPs.setManaged(false);

        // Sync both fields
        maskedPs.textProperty().bindBidirectional(unmaskedPs.textProperty());

        ToggleButton toggle = new ToggleButton("Show️");
        toggle.selectedProperty().addListener((obs, oldVal, isSelected) -> {
            maskedPs.setVisible(!isSelected);
            maskedPs.setManaged(!isSelected);
            unmaskedPs.setVisible(isSelected);
            unmaskedPs.setManaged(isSelected);
        });

        HBox passwordBox = new HBox(5, maskedPs, unmaskedPs, toggle);

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
        grid.add(passwordBox, 1, 2);
        grid.add(new Label("Notes"), 0, 3);
        grid.add(no, 1, 3);
        grid.add(btnBox, 1, 4);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        save.setOnAction(e -> {
            if (sl.getText().trim().isEmpty() || use.getText().trim().isEmpty() || maskedPs.getText().trim().isEmpty()) {
                Alert alert3 = new Alert(Alert.AlertType.WARNING, "Site, Username, and Password are required.");
                DialogPane dialogPane3 = alert3.getDialogPane();
                dialogPane3.getStylesheets().add((new File("src/main/java/org/example/view/style.css")).toURI().toString());
                alert3.showAndWait();

                return;
            }

            try {
                controller.insertRecord(sl.getText(), use.getText(), maskedPs.getText(), no.getText());
            } catch (CryptographyException ignored) {
            }
            popup.close();

            dataL.clear();
            dataL.addAll(controller.loadAllItems());
            Sort.sortItemsBySite(dataL);
        });

        back.setOnAction(e -> popup.close());

        //popup.setScene(new Scene(grid, 450, 300));
        Scene scene22 = new Scene(grid, 450, 300);
        scene22.getStylesheets().add((new File("src/main/java/org/example/view/style.css")).toURI().toString());

        popup.setScene(scene22);
        popup.showAndWait();


    }

    private void openEditWindow(Stage owner, Item SelectedItem) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);
        popup.setTitle("Edit Account");

        TextField sl = new TextField(SelectedItem.getSite());
        TextField use = new TextField(SelectedItem.getUsername());
        TextField no = new TextField(SelectedItem.getNotes());

        // Masked + Unmasked password fields
        PasswordField maskedPs = new PasswordField();
        TextField unmaskedPs = new TextField();
        maskedPs.setPrefWidth(200);
        unmaskedPs.setPrefWidth(200);
        unmaskedPs.setVisible(false);
        unmaskedPs.setManaged(false);

        // Bind both fields
        maskedPs.textProperty().bindBidirectional(unmaskedPs.textProperty());

        // Set initial password text
        maskedPs.setText(controller.retrieve(SelectedItem.getPassword()));


        ToggleButton toggle = new ToggleButton("Show️");
        toggle.selectedProperty().addListener((obs, oldVal, isSelected) -> {
            maskedPs.setVisible(!isSelected);
            maskedPs.setManaged(!isSelected);
            unmaskedPs.setVisible(isSelected);
            unmaskedPs.setManaged(isSelected);
        });

        HBox passwordBox = new HBox(5, maskedPs, unmaskedPs, toggle);

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
        grid.add(passwordBox, 1, 2);
        grid.add(new Label("Notes"), 0, 3);
        grid.add(no, 1, 3);
        grid.add(btnBox, 1, 4);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        save.setOnAction(e -> {
            if (sl.getText().trim().isEmpty() || use.getText().trim().isEmpty() || maskedPs.getText().trim().isEmpty()) {
                Alert alert4 = new Alert(Alert.AlertType.WARNING, "Site, Username, and Password are required.");
                DialogPane dialogPane4 = alert4.getDialogPane();
                dialogPane4.getStylesheets().add((new File("src/main/java/org/example/view/style.css")).toURI().toString());
                alert4.showAndWait();
                return;
            }

            try {
                controller.updateRecord(
                        SelectedItem.getID(),
                        sl.getText(),
                        use.getText(),
                        maskedPs.getText(),
                        no.getText()
                );
            } catch (CryptographyException ex) {
                throw new RuntimeException(ex);
            }

            popup.close();
            dataL.clear();
            dataL.addAll(controller.loadAllItems());
            Sort.sortItemsBySite(dataL);
        });

        back.setOnAction(e -> popup.close());

        //popup.setScene(new Scene(grid, 450, 300));
        Scene scene22 = new Scene(grid, 450, 300);
        scene22.getStylesheets().add((new File("src/main/java/org/example/view/style.css")).toURI().toString());

        popup.setScene(scene22);
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
            Sort.sortItemsBySite(dataL);
        });

        no.setOnAction(e -> popup.close());

        HBox btnBox = new HBox(20, yes, no);
        btnBox.setAlignment(Pos.CENTER);
        VBox layout = new VBox(20, msg, btnBox);
        layout.setAlignment(Pos.CENTER);

        //popup.setScene(new Scene(layout, 350, 150));

        Scene scene22 = new Scene(layout, 350, 150);
        scene22.getStylesheets().add((new File("src/main/java/org/example/view/style.css")).toURI().toString());

        popup.setScene(scene22);
        popup.showAndWait();
    }

    private void showDetailsPopup(Item item) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Stage detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.setTitle("Details");

        // Left column
        Label userLabel = new Label("Username");
        TextField userField = new TextField(item.getUsername());
        userField.setEditable(false);
        userField.setPrefWidth(200);

        Label passLabel = new Label("Password");
        PasswordField maskedField = new PasswordField();
        TextField unmaskedField = new TextField();
        unmaskedField.setEditable(false);
        unmaskedField.setPrefWidth(200);
        maskedField.textProperty().bindBidirectional(unmaskedField.textProperty()); // bind masked to unmasked content-wise
        maskedField.setText(controller.retrieve(item.getPassword()));
        maskedField.setEditable(false);
        maskedField.setPrefWidth(200);

        ToggleButton maskToggle = new ToggleButton("Show️"); // visibility toggle


        HBox passwordDisplay = new HBox(0);
        passwordDisplay.getChildren().addAll(maskedField, unmaskedField);
        passwordDisplay.getChildren().add(maskToggle);  // ensuring that this gets added last, sometimes it overlaps
        passwordDisplay.setSpacing(5);

        unmaskedField.setVisible(false);
        unmaskedField.setManaged(false);  // setManaged(false) means that the layout is not going to include this component at the moment

        maskToggle.selectedProperty().addListener((observable, oldvalue, isSelected) -> {
            if (isSelected) {
                maskedField.setVisible(false);
                maskedField.setManaged(false);
                unmaskedField.setVisible(true);
                unmaskedField.setManaged(true);
            } else {
                maskedField.setVisible(true);
                maskedField.setManaged(true);
                unmaskedField.setVisible(false);
                unmaskedField.setManaged(false);
            }
        });

        VBox leftBox = new VBox(15, userLabel, userField, passLabel, passwordDisplay);
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
        Scene scene = new Scene(layout, 550, 350);
        scene.getStylesheets().add((new File("src/main/java/org/example/view/style.css")).toURI().toString());
        detailStage.setScene(scene);
        detailStage.showAndWait();


    }


    // ----------------- Helper Class -----------------


    public static void main(String[] args) {
        launch();
    }
}
