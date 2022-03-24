package com.example.osimulator;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.function.Function;

public class Orbit {

    private final int ORBIT_RESOLUTION;

    public Orbit(int resolution) {
        ORBIT_RESOLUTION = resolution;
    }

    public Path getPathOrbit(Vecteur2 sun, double periapsis, double apoapsis) {
        double semiMajorLength = (periapsis + apoapsis) / 2;
        double linearEccentricity = semiMajorLength - periapsis;
        double semiMinorLength = Math.sqrt(Math.pow(semiMajorLength, 2) - Math.pow(linearEccentricity, 2));
        double ellipseCenterX = sun.getX() - linearEccentricity;
        double ellipseCenterY = sun.getY();

        Path pathOrbit = new Path();
        pathOrbit.getElements().add(new MoveTo(semiMajorLength + ellipseCenterX, ellipseCenterY));
        for (int i = 0; i < ORBIT_RESOLUTION; i++) {
            double angle = (i / (ORBIT_RESOLUTION - 1f)) * Math.PI * 2;
            double px = Math.cos(angle) * semiMajorLength + ellipseCenterX;
            double py = Math.sin(angle) * semiMinorLength + ellipseCenterY;

            LineTo lineTo = new LineTo(px, py);
            pathOrbit.getElements().add(lineTo);
        }

        return pathOrbit;
    }

    public Vecteur2 findOrbitPoint(Vecteur2 sun, double periapsis, double apoapsis, double t) {
        double semiMajorLength = (periapsis + apoapsis) / 2;
        double linearEccentricity = semiMajorLength - periapsis;
        double eccentricity = linearEccentricity / semiMajorLength;
        double semiMinorLength = Math.sqrt(Math.pow(semiMajorLength, 2) - Math.pow(linearEccentricity, 2));
        double ellipseCenterX = sun.getX() - linearEccentricity;
        double ellipseCenterY = sun.getY();

        double meanAnomaly = t * Math.PI * 2;
        double eccentricAnomaly = solve((x) -> (keplerEquation(x, meanAnomaly, eccentricity)), meanAnomaly, 100);

        double pointX = Math.cos(eccentricAnomaly) * semiMajorLength + ellipseCenterX;
        double pointY = Math.sin(eccentricAnomaly) * semiMinorLength + ellipseCenterY;

        return new Vecteur2(pointX, pointY);
    }

    private double keplerEquation(double E, double M, double e) {
        return M - E + e * Math.sin(E);
    }

    private double solve(Function<Double, Double> function, double initialGuess, int maxIterations) {
        double h = 0.0001;
        double acceptableError = 0.00000001;
        double guess = initialGuess;

        for (int i = 0; i < maxIterations; i++) {
            double y = function.apply(guess);

            if(Math.abs(y) < acceptableError)
                break;

            double slope = (function.apply(guess + h) - y) / h;
            double step = y / slope;
            guess -= step;
        }

        return guess;
    }
}
