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
        for(String option: this.options) {
            RadioButton optionButton = new RadioButton(option);
            optionButton.setToggleGroup(optionsGroup);
            optionButton.getStyleClass().add("custom-radio");
            questionPane.getChildren().add(optionButton);
        }

        // Initialize the result label and add it to the pane
        resultLabel = new Label("");
        questionPane.getChildren().add(resultLabel);

        return questionPane;
    }

    public boolean isAnswerCorrect() {
        RadioButton selectedOption = (RadioButton) optionsGroup.getSelectedToggle();
        if (selectedOption != null) {
            String selectedAnswer = selectedOption.getText();
            return selectedAnswer.equals(correctAnswer);
        }
        return false;
    }

    public void showResult(boolean isCorrect) {
        if (isCorrect) {
            RadioButton selectedOption = (RadioButton) optionsGroup.getSelectedToggle();
            selectedOption.getStyleClass().add("custom-rightRadio");
        } else {
            RadioButton selectedOption = (RadioButton) optionsGroup.getSelectedToggle();
            selectedOption.getStyleClass().add("custom-wrongRadio");
        }
    }

    @Override
    // Show hint in the resultLabel
    public void showHelp() {
        resultLabel.setText(help);
        resultLabel.setStyle("-fx-text-fill: green; -fx-font-style: italic;");
    }
}
