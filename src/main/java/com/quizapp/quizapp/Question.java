package com.quizapp.quizapp;

import javafx.scene.layout.Pane;

public interface Question {
    //
    Pane getQuestionPane();
    // See if the user input answer is correct, record the correct answer.
    boolean isAnswerCorrect();
    // If user input answer isCorrect, use green to indicate so. Else, use red.
    void showResult(boolean isCorrect);
    // Shows the steps and answer of the question.
    void showHelp();
    // Shows the hint for the question.
    void showHint();
    // False: Show timer. True: Hide timer.
    boolean wasAnsweredCorrectly();
}
