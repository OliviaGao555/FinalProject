package com.quizapp.quizapp;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class MultipleChoiceQuestion implements Question {
    // Variables.
    private String questionText;
    private String[] options;
    private String correctAnswer;
    private ToggleGroup optionsGroup;
    private Label resultLabel;
    private Label helpLabel;
    private String help;
    private String hint;
    private String savedSelectionText = null;
    private boolean lastAnswerWasCorrect = false;
    // Variables, used for audio.
    private File soundFile = new File("ding.mp3");
    private Media media = new Media(soundFile.toURI().toString());
    MediaPlayer player = new MediaPlayer(media);

    // Constructor.
    public MultipleChoiceQuestion(String questionText, String[] options, String correctAnswer, String help, String hint) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.help = help;
        this.hint = hint;
    }

    @Override
    public Pane getQuestionPane() {
        VBox questionPane = new VBox(10);
        questionPane.setPadding((new Insets(10)));

        Label questionLabel = new Label(this.questionText);
        questionLabel.setMaxWidth(865);
        questionLabel.setWrapText(true);
        questionPane.getChildren().add(questionLabel);

        // create radio buttons for each option
        optionsGroup = new ToggleGroup();
        for (String option : this.options) {
            RadioButton optionButton = new RadioButton(option);
            optionButton.setToggleGroup(optionsGroup);
            optionButton.getStyleClass().add("custom-radio");
            questionPane.getChildren().add(optionButton);
        }

        // Initialize the help label and add it to the pane
        resultLabel = new Label("");
        questionPane.getChildren().add(resultLabel);
        helpLabel = new Label("");
        helpLabel.setPadding(new Insets(10));
        helpLabel.setMaxWidth(845);
        ScrollPane scrollPane = new ScrollPane(helpLabel);
        scrollPane.setMaxHeight(200);
        scrollPane.setMinHeight(200);
        scrollPane.setMaxWidth(865);
        questionPane.getChildren().add(scrollPane);

        // Restore previous select if applicable
        this.restoreSelectionIfCorrect();

        return questionPane;
    }

    // See: Question Interface.
    public boolean isAnswerCorrect() {
        RadioButton selectedOption = (RadioButton) optionsGroup.getSelectedToggle();
        if (selectedOption != null) {
            savedSelectionText = selectedOption.getText();   // For easier comparison.
            lastAnswerWasCorrect = savedSelectionText.equals(correctAnswer);
            return lastAnswerWasCorrect;
        }
        return false;
    }

    // See: Question Interface.
    public void showResult(boolean isCorrect) {
        RadioButton selectedOption = (RadioButton) optionsGroup.getSelectedToggle();
        if (isCorrect) {
            resultLabel.setText("Your answer is correct!");
            resultLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
            selectedOption.getStyleClass().add("custom-right-radio");
            player.play();
        } else {
            resultLabel.setText("Your answer is incorrect. Try again!");
            resultLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
            selectedOption.getStyleClass().add("custom-wrong-radio");
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

    /**
     * Restores a selection if the question was already answered correctly by user.
     */
    private void restoreSelectionIfCorrect() {
        if (savedSelectionText != null && lastAnswerWasCorrect) {
            optionsGroup.getToggles().forEach(toggle -> {
                RadioButton rb = (RadioButton) toggle;
                if (rb.getText().equals(savedSelectionText)) {
                    rb.setSelected(true);
                    showResult(true);
                }
            });
        }
    }

    // See: Question Interface.
    @Override
    public boolean wasAnsweredCorrectly() {
        return this.lastAnswerWasCorrect;
    }
}
