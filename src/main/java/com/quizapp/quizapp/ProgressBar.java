package com.quizapp.quizapp;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ProgressBar {
    private Pane pane;
    private int[] answerState;
    private Circle[] circles;
    private OnCircleClickListener listener;

    public ProgressBar() {
        answerState = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Create the line
        Line line = new Line(50, 50, 850, 50);  // Line from x=50 to x=850
        line.setStrokeWidth(5);

        // Create a Pane to hold both the line and circles
        pane = new Pane();
        pane.setPrefSize(900, 100);

        // Add the line to the pane
        pane.getChildren().add(line);

        // Create circles and position them evenly along the line
        circles = new Circle[10];
        double lineLength = 800;  // Distance between start and end of the line
        double spacing = lineLength / (circles.length - 1);  // Spacing between circles

        for (int i = 0; i < circles.length; i++) {
            // Create each circle
            circles[i] = new Circle(20);  // Radius of 20 for each circle
            circles[i].setCenterX(50 + i * spacing);  // X position spread along the line
            circles[i].setCenterY(50);  // Align circles vertically at y=50
            circles[i].setFill(Color.WHITE);
            circles[i].setStroke(Color.BLACK);
            circles[i].setStrokeWidth(3);

            // Add click handler for the circle
            int questionIndex = i;  // Capture the index for the event handler
            circles[i].setOnMouseClicked(event -> {
                if (listener != null) {
                    listener.onCircleClick(questionIndex);
                }
            });

            pane.getChildren().add(circles[i]);

            // Add number label above each circle
            Label numberLabel = new Label(String.valueOf(i + 1));
            numberLabel.setLayoutX(circles[i].getCenterX() - 5); // Center the label horizontally
            numberLabel.setLayoutY(circles[i].getCenterY() - 40); // Position the label above the circle
            pane.getChildren().add(numberLabel);
        }
    }

    public Pane getPane() {
        return this.pane;
    }

    // Set a listener for circle clicks
    public void setOnCircleClickListener(OnCircleClickListener listener) {
        this.listener = listener;
    }

    // Update the color of a specific circle based on whether the answer is correct or incorrect
    public void updateCircleColor(int questionIndex, boolean isCorrect) {
        if (questionIndex >= 0 && questionIndex < circles.length) {
            Circle circle = circles[questionIndex];
            if (isCorrect) {
                circle.setFill(Color.GREEN);
            } else {
                circle.setFill(Color.RED);
            }
        }
    }

    // Interface for handling circle click events
    public interface OnCircleClickListener {
        void onCircleClick(int questionIndex);
    }
}