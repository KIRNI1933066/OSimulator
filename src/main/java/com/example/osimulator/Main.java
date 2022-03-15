package com.example.osimulator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Main extends Application {

    private static double trueAnomaly = 0;
    private static final double ECC_EARTH = 0.167;
    private static final double SMA_EARTH = 149.60e6;
    private static final Vecteur2 POS_SUN = new Vecteur2(600, 500);
    private static final int SCALE_CONST = 498669; // 1 : 498669  pixel : km

    @Override
    public void start(Stage stage) {

        Planete mars = new Planete(0, 0, 8, Color.RED, 0.093, 227.956e6);
        Planete earth = new Planete(0, 0, 10, Color.BLUE, 0.167, 149.60e6);
        Circle soleilGraph = new Circle(POS_SUN.getX(), POS_SUN.getY(), 20);
        soleilGraph.setFill(Color.YELLOW);

        Group racine = new Group(earth, mars, soleilGraph);

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                earth.updatePosition(POS_SUN);
                mars.updatePosition(POS_SUN);
                int i = 0;
                if (trueAnomaly >= 360)
                    i = 1;
                if (i == 0) {
                    Line line = new Line(earth.getCenterX(), earth.getCenterY(), earth.getCenterX(), earth.getCenterY());
                    line.setStroke(Color.BLUE);
                    Line line2 = new Line(mars.getCenterX(), mars.getCenterY(), mars.getCenterX(), mars.getCenterY());
                    line2.setStroke(Color.RED);
                    racine.getChildren().addAll(line, line2);
                }
            }
        }.start();

        Scene scene = new Scene(racine, 1200, 1000);
        stage.setTitle("ORBIT SIMULATION");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
