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

import java.io.*;
import java.util.*;
import java.net.URI;
import java.awt.*;
import java.util.List;

public class QuizApp extends Application {
    // Variables, big components of the application.
    private BorderPane root;
    private VBox commonSection;
    private GridPane questionSection;
    // Variables, used to present questions.
    public List<Question> questions;
    private int currentQuestionIndex = 0;
    // Variables, used for the timer.
    private Timeline timer;
    private int timeLimit = 180;
    private Label timerLabel;
    private Properties userDatabase = new Properties();
    private final String userDataFile = "userData.properties";

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
        Button openGoogleButton = new Button("Google!");
        HBox hButtons = new HBox(30, submitButton, helpButton, previousButton, nextButton, openGoogleButton);
        // Set up timer label.
        timerLabel = new Label("Time left: " + timeLimit + " seconds");
        timerLabel.setStyle("-fx-text-fill: #addea6;");

        // Set up the common section.
        commonSection = new VBox();
        commonSection.getChildren().add(hButtons);
        commonSection.getChildren().add(timerLabel);
        commonSection.setSpacing(10);   // Space between timer & buttons
        commonSection.setPadding(new Insets(20));   // Space wrapped around the common section
        commonSection.setPrefHeight(100);
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
        openGoogleButton.setOnAction(e -> {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        URI uri = new URI("https://www.google.com");
                        desktop.browse(uri);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //authenticator
        Label userLabel = new Label("Username:");
        TextField userTextField = new TextField();
        userTextField.setPrefWidth(200);
        userTextField.setMaxWidth(200);
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        passwordField.setMaxWidth(200);
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Label messageLabel = new Label();

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.add(userLabel, 0, 0);
        gridPane.add(userTextField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 1, 2);
        gridPane.add(registerButton, 1, 3);
        gridPane.add(messageLabel, 1, 4);
        gridPane.setAlignment(Pos.CENTER);

        // Set up the scene.
        Scene scene = new Scene(root, 1132, 700);
        Scene authenticationScene = new Scene(gridPane, 500, 250);
        scene.getStylesheets().add("style.css");
        authenticationScene.getStylesheets().add("style.css");
        primaryStage.setScene(authenticationScene);
        primaryStage.setTitle("Quiz App");
        loadUserData();
        primaryStage.show();
        stopTimer();

        loginButton.setOnAction(e -> {
            String username = userTextField.getText();
            String password = passwordField.getText();
            if (userTextField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                messageLabel.setStyle("-fx-text-fill: red");
                messageLabel.setText("Please fill out both fields.");
            } else if (authenticate(username, password)) {
                primaryStage.close();
                showMainApplication(scene);
            } else {
                messageLabel.setStyle("-fx-text-fill: red");
                messageLabel.setText("Invalid credentials. Try Again.");
            }
        });

        registerButton.setOnAction(e -> {
            primaryStage.close();
            registerWindow(primaryStage);
        });
    }

    private void loadUserData() throws FileNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(userDataFile)) {
            userDatabase.load(fileInputStream);
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    //Method to show register window
    private void registerWindow(Stage stage) {
        Label newUserLabel = new Label("New Username:");
        TextField newUserTextField = new TextField();
        newUserTextField.setPrefWidth(200);
        newUserTextField.setMaxWidth(200);
        Label newPasswordLabel = new Label("New Password:");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPrefWidth(200);
        newPasswordField.setMaxWidth(200);
        Button registerButton = new Button("Register");
        Button returnButton = new Button("Return to Login");
        Label messageLabel = new Label();

        // Create layout and add UI elements for the registration window
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.add(newUserLabel, 0, 0);
        gridPane.add(newUserTextField, 1, 0);
        gridPane.add(newPasswordLabel, 0, 1);
        gridPane.add(newPasswordField, 1, 1);
        gridPane.add(registerButton, 1, 2);
        gridPane.add(returnButton, 1, 3);
        gridPane.add(messageLabel, 1, 4);
        gridPane.setAlignment(Pos.CENTER);

        registerButton.setOnAction(e -> {
            String newUsername = newUserTextField.getText().toLowerCase();
            String newPassword = newPasswordField.getText().toLowerCase();
            try {
                if (newUserTextField.getText().isEmpty() || newPasswordField.getText().isEmpty()) {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("Please fill out both fields");
                } else if (register(newUsername, newPassword)) {
                    messageLabel.setStyle("-fx-text-fill: green;");
                    messageLabel.setText("Registration successful!");
                } else {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("Username already exists. Try a different one.");
                }
            } catch (IOException ex) {
                System.out.println("Issue with file");
            }
        });

        Scene registerScene = new Scene(gridPane, 500, 250);
        registerScene.getStylesheets().add("style.css");
        Stage registerStage = new Stage();
        registerStage.setScene(registerScene);
        registerStage.setTitle("Register Window");
        registerStage.show();

        returnButton.setOnAction(e -> {
            registerStage.close();
            showLoginPage(stage);
        });
    }

    //Method to show login page after
    private void showLoginPage(Stage stage) {
        stage.show();
    }

    //Method to register new user to database
    private boolean register(String username, String password) throws IOException {
        if (userDatabase.containsKey(username)) {
            return false;
        }
        userDatabase.setProperty(username, password);
        saveUserData();
        return true;
    }

    //Method to save new user data to database file after registration
    private void saveUserData() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(userDataFile)) {
            userDatabase.store(fileOutputStream, null);
        } catch (IOException e) {
            System.out.println("Issue with the file");
        }
    }

    //Method to show main application after authentication
    private void showMainApplication(Scene scene) {
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Quiz App");
        stage.show();
        startTimer();
    }

    //Method to check if the user is in the database
    private boolean authenticate(String username, String password) {
        return userDatabase.containsKey(username) && userDatabase.get(username).equals(password);
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

        // Q6.
        QuestionGenerator.trueFalseQuestion(questions);

        // Q7.
        QuestionGenerator.trueFalseQuestion(questions);

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
