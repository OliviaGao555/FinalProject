package com.quizapp.quizapp;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MultipleChoiceQuestion implements Question {

    private String questionText;
    private String[] options;

    public MultipleChoiceQuestion(String questionText, String[] options) {
        this.questionText = questionText;
        this.options = options;
    }

    @Override
    public Pane getQuestionPane() {
        VBox questionPane = new VBox(10);
        questionPane.setPadding((new Insets(10)));

        Label questionLabel = new Label(this.questionText);
        questionPane.getChildren().add(questionLabel);

        // create radio buttons for each option
        ToggleGroup optionsGroup = new ToggleGroup();
        for(String option: this.options) {
            RadioButton optionButton = new RadioButton(option);
            optionButton.setToggleGroup(optionsGroup);
            questionPane.getChildren().add(optionButton);
        }
        return questionPane;
    }
}
