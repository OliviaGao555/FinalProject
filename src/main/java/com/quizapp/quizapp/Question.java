package com.quizapp.quizapp;

import javafx.scene.layout.Pane;

public interface Question {
    Pane getQuestionPane();
    public boolean isAnswerCorrect();
    public void showResult(boolean isCorrect);
    public void showHint();
}
