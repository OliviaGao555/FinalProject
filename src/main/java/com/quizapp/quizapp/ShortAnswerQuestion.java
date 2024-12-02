package com.quizapp.quizapp;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShortAnswerQuestion implements Question {
    // Variables.
    private List<String> questionTexts;
    private List<String> correctAnswers;
    private List<TextField> answerInputs;
    private List<String> userCorrectAnswers;
    private Label resultLabel;
    private Label helpLabel;
    private String help;
    private String hint;
    private boolean lastAnswerWasCorrect = false;
    private double helpHeight;
    // Variables, used for audio.
    private File soundFile = new File("ding.mp3");
    private Media media = new Media(soundFile.toURI().toString());
    MediaPlayer player = new MediaPlayer(media);

    // Constructor.
    public ShortAnswerQuestion(List<String> questionTexts, List<String> correctAnswers, String help, String hint, double helpHeight) {
        this.questionTexts = new ArrayList<>(questionTexts);
        this.correctAnswers = new ArrayList<>(correctAnswers);
        this.answerInputs = new ArrayList<>();
        this.userCorrectAnswers = new ArrayList<>(correctAnswers.size());   // Initialize with the correct size
        for (int i = 0; i < correctAnswers.size(); i++) {
            userCorrectAnswers.add(null);   // Initialize with null
        }
        this.help = help;
        this.hint = hint;
        this.helpHeight = helpHeight;
    }

    @Override
    public VBox getQuestionPane() {
        VBox questionV = new VBox(10);
        questionV.setPadding(new Insets(10));
        answerInputs.clear();   // Clear previous inputs to avoid duplication
        boolean allPreviouslyCorrect = true;

        // Add each question text and corresponding input box
        for (int i = 0; i < questionTexts.size(); i++) {
            String questionText = questionTexts.get(i);
            Label questionLabel = new Label(questionText);
            questionLabel.setMaxWidth(865);
            questionLabel.setWrapText(true);
            TextField answerInput = new TextField();
            answerInput.setMaxWidth(865);
            answerInput.setPromptText("Type your answer here...");

            // Restore correct answer if exists
            if (userCorrectAnswers.get(i) != null) {
                answerInput.setText(userCorrectAnswers.get(i));
            } else {
                allPreviouslyCorrect = false; // If any answer is not saved, it means not all were correct previously
            }

            answerInputs.add(answerInput); // Keep track of input fields

            questionV.getChildren().addAll(questionLabel, answerInput);
        }

        // Initialize the result label and add it to the pane
        resultLabel = new Label("");
        questionV.getChildren().add(resultLabel);
        helpLabel = new Label("");
        helpLabel.setPadding(new Insets(10));
        helpLabel.setMaxWidth(845);
        ScrollPane scrollPane = new ScrollPane(helpLabel);
        scrollPane.setMaxHeight(helpHeight);
        scrollPane.setMinHeight(helpHeight);
        scrollPane.setMaxWidth(865);
        questionV.getChildren().add(scrollPane);

        // If all answers were previously correct, show the result as correct
        if (allPreviouslyCorrect && !userCorrectAnswers.contains(null)) { // Check if all answers were correct
            showResult(true);
        }

        return questionV;
    }

    // See: Question Interface.
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

    // See: Question Interface.
    @Override
    public void showResult(boolean isCorrect) {
        if (isCorrect) {
            resultLabel.setText("All answers are correct!");
            resultLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
            for (int i = 0; i < answerInputs.size(); i++) {
                answerInputs.get(i).getStyleClass().remove("custom-wrong-text-field");
                answerInputs.get(i).getStyleClass().add("custom-right-text-field");
            }
            player.play();
        } else {
            resultLabel.setText("One or more answers are incorrect. Try again!");
            resultLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
            for (int i = 0; i < answerInputs.size(); i++) {
                answerInputs.get(i).getStyleClass().remove("custom-right-text-field");
                answerInputs.get(i).getStyleClass().add("custom-wrong-text-field");
            }
        }
    }

    // See: Question Interface.
    @Override
    public void showHelp() {
        helpLabel.setText(help);
        helpLabel.setWrapText(true);
        helpLabel.setStyle("-fx-text-fill: green; -fx-font-style: italic;");
    }

    // See: Question Interface.
    @Override
    public void showHint() {
        if (!helpLabel.getText().equals(help)) {
            helpLabel.setText(hint);
            helpLabel.setWrapText(true);
            helpLabel.setStyle("-fx-text-fill: green; -fx-font-style: italic;");
        }
    }

    // See: Question Interface.
    @Override
    public boolean wasAnsweredCorrectly() {
        return this.lastAnswerWasCorrect;
    }
}
