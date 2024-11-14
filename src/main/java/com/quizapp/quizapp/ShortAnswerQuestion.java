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
    private Label resultLabel;
    private String help;

    public ShortAnswerQuestion(List<String> questionTexts, List<String> correctAnswers, String help) {
        this.questionTexts = new ArrayList<>(questionTexts);
        this.correctAnswers = new ArrayList<>(correctAnswers);
        this.answerInputs = new ArrayList<>();
        this.help = help;
    }

    @Override
    public Pane getQuestionPane() {
        VBox questionPane = new VBox(10);
        questionPane.setPadding(new Insets(10));
        // Add each question text and corresponding input box
        for (String questionText : questionTexts) {
            Label questionLabel = new Label(questionText);
            TextField answerInput = new TextField();
            answerInput.setPromptText("Type your answer here...");
            answerInputs.add(answerInput); // Keep track of input fields

            questionPane.getChildren().addAll(questionLabel, answerInput);
        }
        // Initialize the result label and add it to the pane
        resultLabel = new Label("");
        questionPane.getChildren().add(resultLabel);
        return questionPane;
    }

    @Override
    public boolean isAnswerCorrect() {
        for (int i = 0; i < correctAnswers.size(); i++) {
            String userAnswer = answerInputs.get(i).getText().trim();
            if (!userAnswer.equalsIgnoreCase(correctAnswers.get(i))) {
                return false; // Return false if any answer is incorrect
            }
        }
        return true;
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
}
