package com.quizapp.quizapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.*;

public class QuizApp extends Application {

    private BorderPane root;
    private VBox commonSection;
    private Pane questionSection;
    public List<Question> questions;
    private int currentQuestionIndex = 0;
    private Label questionCounterLabel;
    private int[] preserveStateArray = new int[10000];
    public static void main(String[] args) {
        launch(args);
    }
    private Label alreadyAnsweredLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new BorderPane();
        // Set up the common section with buttons
        commonSection = new VBox(10);
        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("custom-button");
        Button helpButton = new Button("Help");
        helpButton.getStyleClass().add("custom-button");
        Button previousButton = new Button("Previous Question");
        previousButton.getStyleClass().add("custom-button");
        Button nextButton = new Button("Next Question");
        nextButton.getStyleClass().add("custom-button");
        HBox hButtons = new HBox(20, submitButton, helpButton, previousButton, nextButton);
        hButtons.setPadding(new Insets(10));
        questionCounterLabel = new Label();
        alreadyAnsweredLabel = new Label("");

        commonSection.getChildren().addAll(hButtons, questionCounterLabel, alreadyAnsweredLabel);
        commonSection.setPrefHeight(100);
        root.setBottom(commonSection);

        // Set up the question section
        questionSection = new Pane();
        questionSection.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(3))));
        questionSection.setPadding(new Insets(10));
        root.setCenter(questionSection);

        // Initialize questions
        initializeQuestions();
        updateQuestionCounter();

        // Display the first question
        displayQuestion(currentQuestionIndex);

        // Set up button actions
        submitButton.setOnAction(e -> {
            Question currentQuestion = questions.get(currentQuestionIndex);
            boolean isCorrect = currentQuestion.isAnswerCorrect();
            currentQuestion.showResult(isCorrect); // Show the result in the question section
            if (isCorrect) {
                preserveStateArray[currentQuestionIndex] = 1;
            } else {
                preserveStateArray[currentQuestionIndex] = 0;
            }
        });

        nextButton.setOnAction(e -> {
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                displayQuestion(currentQuestionIndex);
                updateQuestionCounter();
            }
        });

        previousButton.setOnAction(e -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayQuestion(currentQuestionIndex);
                updateQuestionCounter();
            }
        });

        helpButton.setOnAction(e -> {
            Question currentQuestion = questions.get(currentQuestionIndex);
            currentQuestion.showHelp(); // Display the hint in the resultLabel
        });

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Quiz App");
        primaryStage.show();
    }

    // Method to initialize a list of questions
    private void initializeQuestions() {
        questions = new ArrayList<>();

        // Q1
        QuestionGenerator.windQuestion(questions);

        // Q2
        QuestionGenerator.windQuestion(questions);

        // example to show how to add short answer question
        questions.add(new ShortAnswerQuestion(
                List.of("What is the capital of France?", "Largest planet?", "H2O is the chemical formula for?"),
                List.of("Paris", "Jupiter", "Water"),
                "Some hint"
        ));

        // Shuffle the list to randomize the question order
        Collections.shuffle(questions);
    }

    private void displayQuestion(int index) {
        if (preserveStateArray[index] == 1) {
            questionSection.getChildren().clear();
            questionSection.getChildren().add(questions.get(index).getQuestionPane());
            alreadyAnsweredLabel.setText("You have already answered this question correctly!");
            alreadyAnsweredLabel.setStyle("-fx-text-fill: green; -fx-font-size: 20; -fx-font-weight: bold;");
            alreadyAnsweredLabel.setVisible(true);
        } else {
            questionSection.getChildren().clear();
            questionSection.getChildren().add(questions.get(index).getQuestionPane());
            alreadyAnsweredLabel.setVisible(false);
        }
    }

    private void updateQuestionCounter() {
        int totalQuestions = questions.size();
        int currentQuestionNumber = currentQuestionIndex + 1;
        questionCounterLabel.setText("Question " + currentQuestionNumber + " / " + totalQuestions);
    }
}
