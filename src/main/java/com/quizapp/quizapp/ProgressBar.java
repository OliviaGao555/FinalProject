package com.quizapp.quizapp;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ProgressBar {
    private Pane pane;
    private int[] answerState;
    private Circle[] circles;

    public ProgressBar() {
        answerState = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Create the line
        Line line = new Line(50, 50, 850, 50);  // Line from x=50 to x=750
        line.setStrokeWidth(5);

        // Create a Pane to hold both the line and circles
        pane = new Pane();
        pane.setPrefSize(900, 100);

        // Add the line to the pane
        pane.getChildren().add(line);

        // Create circles and position them evenly along the line
        circles = new Circle[10];
        double lineLength = 800;  // Distance between start and end of the line (750 - 50)
        double spacing = lineLength / (circles.length - 1);  // Spacing between circles

        for (int i = 0; i < circles.length; i++) {
            circles[i] = new Circle(20);  // Radius of 5 for each circle
            circles[i].setCenterX(50 + i * spacing);  // X position spread along the line
            circles[i].setCenterY(50);  // Align circles vertically at y=50
            circles[i].setFill(Color.WHITE);
            circles[i].setStroke(Color.BLACK);
            circles[i].setStrokeWidth(3);
            pane.getChildren().add(circles[i]);
        }
    }

    public Pane getPane() {
        return this.pane;
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
}
