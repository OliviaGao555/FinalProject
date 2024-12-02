package com.quizapp.quizapp;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ProgressBar {
    private Pane pane;
    private Circle[] circles;
    private OnCircleClickListener listener;

    public ProgressBar() {
        // Create the line
        Line line = new Line(20, 35, 820, 35);
        line.setStrokeWidth(2);
        // Create a Pane to hold both the line and circles
        pane = new Pane();
        pane.setPrefSize(700, 50);
        // Add the line to the pane
        pane.getChildren().add(line);
        // Create circles and position them evenly along the line
        circles = new Circle[10];
        double lineLength = 800;
        double spacing = lineLength / (circles.length - 1);

        for (int i = 0; i < circles.length; i++) {
            // Create each circle
            circles[i] = new Circle(15);
            circles[i].setCenterX(20 + i * spacing);
            circles[i].setCenterY(35);
            circles[i].setFill(Color.WHITE);
            circles[i].setStroke(Color.BLACK);
            circles[i].setStrokeWidth(1.5);
            // Add click handler for the circle
            int questionIndex = i;
            circles[i].setOnMouseClicked(event -> {
                if (listener != null) {
                    listener.onCircleClick(questionIndex);
                }
            });

            pane.getChildren().add(circles[i]);
            // Add number label above each circle
            Label numberLabel = new Label(String.valueOf(i + 1));
            numberLabel.setLayoutX(circles[i].getCenterX() - 5);
            numberLabel.setLayoutY(circles[i].getCenterY() - 35);
            pane.getChildren().add(numberLabel);
            if(i==9) {
                numberLabel.setLayoutX(circles[i].getCenterX() - 10);
            }
        }
    }

    public Pane getPane() {
        return this.pane;
    }

    // Set a listener for circles
    public void setOnCircleClickListener(OnCircleClickListener listener) {
        this.listener = listener;
    }

    // Update the color of a specific circle based on whether the answer is correct or incorrect
    public void updateCircleColor(int questionIndex, boolean isCorrect) {
        if (questionIndex >= 0 && questionIndex < circles.length) {
            Circle circle = circles[questionIndex];
            if (isCorrect) {
                circle.setFill(Color.valueOf("#addea6"));
            } else {
                circle.setFill(Color.valueOf("#dea6a6"));
            }
        }
    }

    // Interface for handling circle click events
    public interface OnCircleClickListener {
        void onCircleClick(int questionIndex);
    }
}
