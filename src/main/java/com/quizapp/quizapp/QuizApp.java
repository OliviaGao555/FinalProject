package com.quizapp.quizapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class QuizApp extends Application {

    private BorderPane root;
    private VBox commonSection;
    private Pane questionSection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new BorderPane();

        // Set up the common section with buttons
        commonSection = new VBox(10);
        Button submitButton = new Button("Submit");
        Button helpButton = new Button("Help");
        Button lastButton = new Button("Last Question");
        Button nextButton = new Button("Next Question");
        HBox hButtons = new HBox(20, submitButton, helpButton, lastButton, nextButton);
        commonSection.getChildren().addAll(hButtons);
        root.setBottom(commonSection);

        questionSection = new Pane();
//        questionSection.setBorder(new Border(new BorderStroke(
//                Color.BLACK,
//                BorderStrokeStyle.SOLID,
//                CornerRadii.EMPTY,
//                new BorderWidths(3)
//        )));
        questionSection.setPadding(new Insets(10));

        root.setCenter(questionSection);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Quiz App");
        primaryStage.show();
    }
}
