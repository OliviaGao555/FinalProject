package com.quizapp.quizapp;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class MultipleChoiceQuestion implements Question {

    private String questionText;
    private String[] options;
    private String correctAnswer;
    private ToggleGroup optionsGroup;
    private Label resultLabel; // Label to show the result
    private String help;
    private String savedSelectionText = null; // Add this field to your class
    private boolean lastAnswerWasCorrect = false;

    public MultipleChoiceQuestion(String questionText, String[] options, String correctAnswer, String help) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.help = help;
    }

    @Override
    public Pane getQuestionPane() {
        VBox questionPane = new VBox(10);
        questionPane.setPadding((new Insets(10)));

        Label questionLabel = new Label(this.questionText);
        questionPane.getChildren().add(questionLabel);

        // create radio buttons for each option
        optionsGroup = new ToggleGroup();
        for (String option : this.options) {
            RadioButton optionButton = new RadioButton(option);
            optionButton.setToggleGroup(optionsGroup);
            optionButton.getStyleClass().add("custom-radio");
            questionPane.getChildren().add(optionButton);
        }

        // Initialize the result label and add it to the pane
        resultLabel = new Label("");
        questionPane.getChildren().add(resultLabel);

        // Restore previous select if applicable
        this.restoreSelectionIfCorrect();

        return questionPane;
    }

    public boolean isAnswerCorrect() {
        RadioButton selectedOption = (RadioButton) optionsGroup.getSelectedToggle();
        if (selectedOption != null) {
            // Save the selected option's text instead of the RadioButton reference
            savedSelectionText = selectedOption.getText(); // Assume savedSelectionText is a String field
            lastAnswerWasCorrect = savedSelectionText.equals(correctAnswer);
            return lastAnswerWasCorrect;
        }
        return false;
    }

    public void showResult(boolean isCorrect) {
        RadioButton selectedOption = (RadioButton) optionsGroup.getSelectedToggle();
        if (isCorrect) {
            selectedOption.getStyleClass().add("custom-right-radio");
        } else {
            selectedOption.getStyleClass().add("custom-wrong-radio");
        }
    }

    @Override
    // Show hint in the resultLabel
    public void showHelp() {
        resultLabel.setText(help);
        resultLabel.setStyle("-fx-text-fill: green; -fx-font-style: italic;");
    }

    // Add a method to restore the saved selection
    private void restoreSelectionIfCorrect() {
        if (savedSelectionText != null && lastAnswerWasCorrect) {
            optionsGroup.getToggles().forEach(toggle -> {
                RadioButton rb = (RadioButton) toggle;
                if (rb.getText().equals(savedSelectionText)) {
                    rb.setSelected(true);
                    // Optionally, apply the correct styling if needed
                    showResult(true);
                }
            });
        }
    }

    @Override
    public boolean wasAnsweredCorrectly() {
        return this.lastAnswerWasCorrect;
    }
}
