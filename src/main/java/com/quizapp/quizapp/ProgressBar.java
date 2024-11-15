package com.quizapp.quizapp;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ProgressBar {
    private Pane pane;
    private int[] answerState;
    private Circle[] circles;

    public ProgressBar() {
        answerState = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Create the line
        Line line = new Line(50, 50, 950, 50);  // Line from x=50 to x=950
        line.setStrokeWidth(5);

        // Create a Pane to hold both the line and circles
        pane = new Pane();
        pane.setPrefSize(1000, 100);

        // Add the line to the pane
        pane.getChildren().add(line);

        // Create circles and position them evenly along the line
        circles = new Circle[10];
        double lineLength = 900;  // Distance between start and end of the line (950 - 50)
        double spacing = lineLength / (circles.length - 1);  // Spacing between circles

        for (int i = 0; i < circles.length; i++) {
            circles[i] = new Circle(20);  // Radius of 5 for each circle
            circles[i].setCenterX(50 + i * spacing);  // X position spread along the line
            circles[i].setCenterY(50);  // Align circles vertically at y=50

            pane.getChildren().add(circles[i]);
        }
    }

    public Pane getPane() {
        return this.pane;
    }


}
