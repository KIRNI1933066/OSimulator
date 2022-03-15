package com.example.osimulator;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Planete extends Circle {

    private double trueAnomaly = 0;
    private final double ecc;
    private final double sma;

    private Vecteur2 position;
    private Vecteur2 speed;

    public Planete(int xPos, int yPos, int radius, Color color, double ecc, double sma) {
        super(xPos, yPos, radius, color);
        position = new Vecteur2(xPos, yPos);
        super.centerXProperty().bind(position.XProperty());
        super.centerYProperty().bind(position.YProperty());
        speed = new Vecteur2();

        this.ecc = ecc;
        this.sma = sma;
    }

    private double distance() {
        return (sma * (1 - ecc * ecc)) / (1 + (ecc * Math.cos(trueAnomaly)));
    }

    public void updatePosition(Vecteur2 sunPosition) {
        if (trueAnomaly < (Math.PI*2))
            trueAnomaly += 0.05;
        else
            trueAnomaly = 0;

        double scaledDistance = distance() / Constantes.SCALE_CONST_DISTANCE;

        double eccentricAnomaly = 2 * Math.atan(Math.tan(trueAnomaly / 2) /
                (Math.sqrt((1 + ecc) / (1 - ecc))));

        position.setX(sunPosition.getX() + (scaledDistance * Math.cos(eccentricAnomaly)));
        position.setY(sunPosition.getY() - (scaledDistance * Math.sin(eccentricAnomaly)));
    }

    /*public double getGM() {
        return GM;
    }*/
}
