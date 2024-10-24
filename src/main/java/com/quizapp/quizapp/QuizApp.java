package com.quizapp.quizapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizApp extends Application {

    private BorderPane root;
    private VBox commonSection;
    private Pane questionSection;
    private List<Question> questions;
    private int currentQuestionIndex = 0;

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
        commonSection.getChildren().addAll(hButtons);
        root.setBottom(commonSection);

        // Set up the question section
        questionSection = new Pane();
        questionSection.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(3)
        )));
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

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Quiz App");
        primaryStage.show();
    }

    // Method to initialize a list of questions
    private void initializeQuestions() {
        questions = new ArrayList<>();

        // Create multiple questions
        String questionText1 = "What is the capital of France?";
        String[] options1 = {"Berlin", "Madrid", "Paris", "Rome"};
        String correctAnswer1 = "Paris";
        questions.add(new MultipleChoiceQuestion(questionText1, options1, correctAnswer1));

        String questionText2 = "Which planet is known as the Red Planet?";
        String[] options2 = {"Earth", "Mars", "Jupiter", "Saturn"};
        String correctAnswer2 = "Mars";
        questions.add(new MultipleChoiceQuestion(questionText2, options2, correctAnswer2));

        // Shuffle the list to randomize the question order
        Collections.shuffle(questions);
    }

    private void displayQuestion(int index) {
        questionSection.getChildren().clear();
        questionSection.getChildren().add(questions.get(index).getQuestionPane());
    }
}
