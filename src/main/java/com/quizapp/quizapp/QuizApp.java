package com.quizapp.quizapp;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class QuizApp extends Application {

    private BorderPane root;
    private VBox commonSection;
    private Pane questionSection;
    public List<Question> questions;
    private int currentQuestionIndex = 0;
    private Label questionCounterLabel;

    private Timeline timer;
    private int timeLimit = 5; // Set a time limit for each question (in seconds)
    private Label timerLabel; // Label to display the countdown

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
        Button previousButton = new Button("Previous Question");
        Button nextButton = new Button("Next Question");
        HBox hButtons = new HBox(20, submitButton, helpButton, previousButton, nextButton);
        hButtons.setPadding(new Insets(10));
        questionCounterLabel = new Label();
        commonSection.getChildren().addAll(hButtons, questionCounterLabel);

        // Initialize timer label and add it to the common section
        timerLabel = new Label("Time left: " + timeLimit + " seconds");
        timerLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16;");
        commonSection.getChildren().add(timerLabel);

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
        questionSection.getChildren().add(currentQuestion.getQuestionPane());

        if (!currentQuestion.wasAnsweredCorrectly()) {
            startTimer();
        } else {
            stopTimer();
        }
    }

    private void updateQuestionCounter() {
        int totalQuestions = questions.size();
        int currentQuestionNumber = currentQuestionIndex + 1;
        questionCounterLabel.setText("Question " + currentQuestionNumber + " / " + totalQuestions);
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
