package com.quizapp.quizapp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;

public class QuizApp extends Application {
    // Variables, big components of the application.
    private BorderPane root;
    private VBox commonSection;
    private GridPane questionSection;
    private HBox progressSection;

    // Variables, used to present questions.
    public List<Question> questions;
    private int currentQuestionIndex = 0;
    // Variables, used for the timer.
    private Timeline timer;
    private int timeLimit = 180;
    private Label timerLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create the buttons.
        Button submitButton = new Button("Submit");
        Button helpButton = new Button("Help");
        Button previousButton = new Button("Previous Question");
        Button nextButton = new Button("Next Question");
        HBox hButtons = new HBox(30, submitButton, helpButton, previousButton, nextButton);
        // Set up timer label.
        timerLabel = new Label("Time left: " + timeLimit + " seconds");
        timerLabel.setStyle("-fx-text-fill: #addea6;");

        // Set up the common section.
        commonSection = new VBox();
        commonSection.getChildren().add(hButtons);
        commonSection.getChildren().add(timerLabel);
        ProgressBar bar = new ProgressBar();
        commonSection.getChildren().add(bar.getPane());
        commonSection.setSpacing(10);   // Space between timer & buttons
        commonSection.setPadding(new Insets(20));   // Space wrapped around the common section
        commonSection.setPrefHeight(200);
        // Set up the question section.
        questionSection = new GridPane();
        questionSection.setPadding(new Insets(20));   // Space wrapped around the question section


        // Set up the root.
        root = new BorderPane();
        root.setCenter(questionSection);
        root.setBottom(commonSection);


        // Initialize questions and display the first question.
        initializeQuestions();
        displayQuestion(currentQuestionIndex);

        // Set up button actions.
        submitButton.setOnAction(e -> {
            Question currentQuestion = questions.get(currentQuestionIndex);
            boolean isCorrect = currentQuestion.isAnswerCorrect();
            currentQuestion.showResult(isCorrect);
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
            currentQuestion.showHelp();
        });

        //authenticator

        Label userLabel = new Label("Username:");
        TextField userTextField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Label messageLabel = new Label();

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.add(userLabel, 0, 0);
        gridPane.add(userTextField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 1, 2);
        gridPane.add(messageLabel, 1, 3);
        gridPane.setAlignment(Pos.CENTER);

        // Set up the scene.
        Scene scene = new Scene(root, 1132, 700);
        Scene authenticationScene = new Scene(gridPane, 500, 250);
        scene.getStylesheets().add("style.css");
        authenticationScene.getStylesheets().add("style.css");
        primaryStage.setScene(authenticationScene);
        primaryStage.setTitle("Quiz App");
        primaryStage.show();

        loginButton.setOnAction(e -> {
            String username = userTextField.getText();
            String password = passwordField.getText();
            if (authenticate(username, password)) {
                primaryStage.close();
                showMainApplication(scene);
            } else {
                messageLabel.setText("Invalid credentials. Try Again.");
                messageLabel.setStyle("-fx-text-fill: red");
            }
        });
    }

    private void showMainApplication(Scene scene) {
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Quiz App");
        stage.show();
    }

    private boolean authenticate(String username, String password) {
        return "1".equals(username) && "1".equals(password);
    }

    // Method to initialize a list of questions
    private void initializeQuestions() {
        // Create an ArrayList of questions.
        questions = new ArrayList<>();

        // Q1.
        QuestionGenerator.windQuestion(questions);

        // Q2.
        QuestionGenerator.windQuestion(questions);

        // Q3.
        QuestionGenerator.termsQuestion(questions);

        // Q4.
        QuestionGenerator.slideQuestion(questions);

        // Q5.
        QuestionGenerator.slideQuestion(questions);

        // Shuffle the list to randomize the question order.
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
        currentQuestion.showHint();
    }
}
