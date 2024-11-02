package com.quizapp.quizapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class QuizApp extends Application {

    private BorderPane root;
    private VBox commonSection;
    private Pane questionSection;
    public List<Question> questions;
    private int currentQuestionIndex = 0;
    private Label questionCounterLabel;


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

        questionCounterLabel = new Label();

        commonSection.getChildren().addAll(hButtons, questionCounterLabel);
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

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Quiz App");
        primaryStage.show();
    }

    // Method to initialize a list of questions
    private void initializeQuestions() {
        questions = new ArrayList<>();

        //Question 1
        ArrayList<String> multi = multiChoiWind();
        String answer = multi.get(0);
        String randomAnswer1 = multi.get(1);
        String randomAnswer2 = multi.get(2);
        String randomAnswer3 = multi.get(3);
        String questionText1 = multi.get(4);
        String[] options = {answer, randomAnswer1, randomAnswer2, randomAnswer3};
        questions.add(new MultipleChoiceQuestion(questionText1, options, answer));

        // Shuffle the list to randomize the question order
        Collections.shuffle(questions);
    }

    private void displayQuestion(int index) {
        questionSection.getChildren().clear();
        questionSection.getChildren().add(questions.get(index).getQuestionPane());
    }

    private void updateQuestionCounter() {
        int totalQuestions = questions.size();
        int currentQuestionNumber = currentQuestionIndex + 1;
        questionCounterLabel.setText("Question " + currentQuestionNumber + " / " + totalQuestions);
    }

    public ArrayList<String> multiChoiWind() {
        double randomW = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(1.00,10.00)).replace(",","."));
        double randomS = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(0.10,5.00)).replace(",","."));
        double wave = randomW;
        double speed = randomS;
        String questionText = String.format("Wind gusts create ripples on the ocean " +
                "that have a wavelength of %,.2f cm and propagate at %,.2f m/s. What is their frequency?", wave, speed);

        double correctAnswer = speed / (wave / 100);
        double randomAnswer1 = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(1.00,500.00)).replace(",","."));
        double randomAnswer2 = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(1.00,500.00)).replace(",","."));
        double randomAnswer3 = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(1.00,500.00)).replace(",","."));

        String answer = String.format("%,.2f", correctAnswer);
        ArrayList<String> ret = new ArrayList<>();
        ret.add(answer);
        ret.add(Double.toString(randomAnswer1));
        ret.add(Double.toString(randomAnswer2));
        ret.add(Double.toString(randomAnswer3));
        ret.add(questionText);
        return ret;
    }
}
