package com.quizapp.quizapp;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ShortAnswerQuestion implements Question {
    private List<String> questionTexts;
    private List<String> correctAnswers;
    private List<TextField> answerInputs; // Input fields for each question
    private List<String> userCorrectAnswers; // To store correct answers entered by the user
    private Label resultLabel;
    private String help;
    private boolean lastAnswerWasCorrect = false;

    public ShortAnswerQuestion(List<String> questionTexts, List<String> correctAnswers, String help) {
        this.questionTexts = new ArrayList<>(questionTexts);
        this.correctAnswers = new ArrayList<>(correctAnswers);
        this.answerInputs = new ArrayList<>();
        this.userCorrectAnswers = new ArrayList<>(correctAnswers.size()); // Initialize with the correct size, filled with nulls
        for (int i = 0; i < correctAnswers.size(); i++) {
            userCorrectAnswers.add(null); // Initialize with nulls
        }
        this.help = help;
    }

    @Override
    public Pane getQuestionPane() {
        VBox questionPane = new VBox(10);
        questionPane.setPadding(new Insets(10));
        answerInputs.clear(); // Clear previous inputs to avoid duplication
        boolean allPreviouslyCorrect = true;

        // Add each question text and corresponding input box
        for (int i = 0; i < questionTexts.size(); i++) {
            String questionText = questionTexts.get(i);
            Label questionLabel = new Label(questionText);
            TextField answerInput = new TextField();
            answerInput.setPromptText("Type your answer here...");

            // Restore correct answer if exists
            if (userCorrectAnswers.get(i) != null) {
                answerInput.setText(userCorrectAnswers.get(i));
            } else {
                allPreviouslyCorrect = false; // If any answer is not saved, it means not all were correct previously
            }

            answerInputs.add(answerInput); // Keep track of input fields

            questionPane.getChildren().addAll(questionLabel, answerInput);
        }

        // Initialize the result label and add it to the pane
        resultLabel = new Label("");
        questionPane.getChildren().add(resultLabel);

        // If all answers were previously correct, show the result as correct
        if (allPreviouslyCorrect && !userCorrectAnswers.contains(null)) { // Check if all answers were correct
            showResult(true);
        }

        return questionPane;
    }

    @Override
    public boolean isAnswerCorrect() {
        boolean allCorrect = true;
        for (int i = 0; i < correctAnswers.size(); i++) {
            String userAnswer = answerInputs.get(i).getText().trim();
            if (!userAnswer.equalsIgnoreCase(correctAnswers.get(i))) {
                allCorrect = false; // Mark as incorrect if any answer doesn't match
                userCorrectAnswers.set(i, null); // Reset the answer if it's incorrect
            } else {
                // Save correct answer
                userCorrectAnswers.set(i, userAnswer);
            }
        }
        this.lastAnswerWasCorrect = allCorrect;
        return allCorrect;
    }

    @Override
    public void showResult(boolean isCorrect) {
        if (isCorrect) {
            resultLabel.setText("All answers are correct!");
            resultLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
            for (int i = 0; i < answerInputs.size(); i++) {
                answerInputs.get(i).getStyleClass().add("custom-right-text-field");
            }
        } else {
            resultLabel.setText("One or more answers are incorrect. Try again!");
            resultLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
            for (int i = 0; i < answerInputs.size(); i++) {
                answerInputs.get(i).getStyleClass().add("custom-wrong-text-field");
            }
        }
    }

    @Override
    public void showHelp() {
        resultLabel.setText(help);
        resultLabel.setStyle("-fx-text-fill: grey; -fx-font-style: italic;");
    }

    @Override
    public boolean wasAnsweredCorrectly() {
        return this.lastAnswerWasCorrect;
    }
}
