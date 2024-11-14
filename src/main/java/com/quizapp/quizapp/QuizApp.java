package com.quizapp.quizapp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;

public class QuizApp extends Application {
    //Variables, big components of the application.
    private BorderPane root;
    private VBox commonSection;
    private GridPane questionSection;
    //Variables, used to present questions.
    public List<Question> questions;
    private Label questionCounterLabel;
    private int currentQuestionIndex = 0;
    //Variables, used for the timer.
    private Timeline timer;
    private int timeLimit = 180;
    private Label timerLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Set up the root.
        root = new BorderPane();
        // Set up the common section with buttons
        commonSection = new VBox();
        Button submitButton = new Button("Submit");
        Button helpButton = new Button("Help");
        Button previousButton = new Button("Previous Question");
        Button nextButton = new Button("Next Question");
        HBox hButtons = new HBox(20, submitButton, helpButton, previousButton, nextButton);
        questionCounterLabel = new Label();
        commonSection.getChildren().addAll(hButtons, questionCounterLabel);
        commonSection.setSpacing(10);
        commonSection.setPadding(new Insets(20));

        // Initialize timer label and add it to the common section
        timerLabel = new Label("Time left: " + timeLimit + " seconds");
        timerLabel.setStyle("-fx-text-fill: #addea6;");
        commonSection.getChildren().add(timerLabel);

        commonSection.setPrefHeight(100);

        root.setBottom(commonSection);

        // Set up the question section
        questionSection = new GridPane();
        questionSection.setPadding(new Insets(10));
        root.setCenter(questionSection);

        // Initialize questions
        initializeQuestions();

        // Display the first question
        displayQuestion(currentQuestionIndex);

        // Set up button actions
        submitButton.setOnAction(e -> {
            Question currentQuestion = questions.get(currentQuestionIndex);
            boolean isCorrect = currentQuestion.isAnswerCorrect();
            currentQuestion.showResult(isCorrect); // Show the result in the question section
        });

        nextButton.setOnAction(e -> {
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                displayQuestion(currentQuestionIndex);
            }
        });

        previousButton.setOnAction(e -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayQuestion(currentQuestionIndex);
            }
        });

        helpButton.setOnAction(e -> {
            Question currentQuestion = questions.get(currentQuestionIndex);
            currentQuestion.showHelp(); // Display the hint in the resultLabel
        });

        Scene scene = new Scene(root, 1132, 700);
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

        questions.add(new ShortAnswerQuestion(
                List.of("What is the term for the maximum displacement of an object from its equilibrium position in simple harmonic motion?",
                        "What is the term for the number of oscillations per unit time",
                        "What is the term for the time to complete a cycle"),
                List.of("Amplitude",
                        "Frequency",
                        "Period"),
                "Hint:\n" +
                        "- The maximum displacement from equilibrium is often denoted by the letter 'A'.\n" +
                        "- The number of oscillations per second is measured in Hertz (Hz).\n" +
                        "- The time to complete one full cycle of motion is the inverse of frequency."
        ));

        // Shuffle the list to randomize the question order
        Collections.shuffle(questions);
    }

    private void displayQuestion(int index) {
        questionSection.getChildren().clear();
        Question currentQuestion = questions.get(index);
        Label questionNumber = new Label();
        int totalQuestions = questions.size();
        questionNumber.setText(String.format("Question %d / %d", currentQuestionIndex + 1, totalQuestions));
        questionNumber.setStyle("-fx-font-weight: bold");
        questionSection.add(questionNumber, 0, 0);
        questionSection.add(currentQuestion.getQuestionPane(), 0, 1);

        if (!currentQuestion.wasAnsweredCorrectly()) {
            startTimer();
        } else {
            stopTimer();
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        timerLabel.setText("");
    }

    // Method to start or reset the timer for each question
    private void startTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }

        int[] timeLeft = {timeLimit};  // Reset the time left to the limit

        timerLabel.setText("Time left: " + timeLeft[0] + " seconds");
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeLeft[0]--;
            timerLabel.setText("Time left: " + timeLeft[0] + " seconds");

            if (timeLeft[0] <= 0) {
                timer.stop();
                showHintOnTimeout();  // Show hint if the time runs out
            }
        }));
        timer.setCycleCount(timeLimit);
        timer.playFromStart();
    }

    // Method to display the hint when the timer runs out
    private void showHintOnTimeout() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        currentQuestion.showHelp();
    }
}
