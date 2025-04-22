package org.example.view;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimpleJavaFXApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a label
        Label label = new Label("Hello, JavaFX!");
        label.setFont(new Font(18));

        // Create a button
        Button button = new Button("Click me!");

        // Add event handler to button
        button.setOnAction(event -> {
            label.setText("Button clicked!");

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(event1 -> {
                label.setFont(Font.font("Segoe UI Emoji", 18));
                label.setText("JavaFX is working 🥳");
            });
            pause.play();
        });

        // Create a layout (StackPane is a simple layout)
        StackPane root = new StackPane();
        root.getChildren().addAll(label, button);

        // Set the scene
        Scene scene = new Scene(root, 300, 200);

        label.setTranslateY(50);

        // Set up the stage (window)
        primaryStage.setTitle("Simple JavaFX App");
        primaryStage.setScene(scene);

        // Show the window
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
