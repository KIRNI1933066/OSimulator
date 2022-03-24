package com.example.osimulator;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.osimulator.Main.ECHELLE;

public class Planete extends Sphere {

    private Vecteur2 position, speed;
    public String name;
    private Orbit orbit;
    private double periapsis, apoapsis;
    private PolyLine3D orbitPath;

    private boolean drawPath = true;

    public Planete (double radius, Color color, double periapsis, double apoapsis, String name) {
        super(radius);
        PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseColor(color);
        super.setMaterial(mat);
        position = new Vecteur2(0, 0);
        super.setTranslateX(position.getX());
        super.setTranslateY(position.getY());
        super.translateXProperty().bind(position.XProperty());
        super.translateYProperty().bind(position.YProperty());
        speed = new Vecteur2();

        this.name = name;
        this.apoapsis = apoapsis;
        this.periapsis = periapsis;
        orbit = new Orbit(5000);
    }

    public void updateOrbitPath(Vecteur2 sunPosition) {
        if (orbitPath != null) {
            Main.racine.getChildren().remove(orbitPath);
        }
        Path orbitRealPath = orbit.getPathOrbit(sunPosition, periapsis/ ECHELLE, apoapsis/ ECHELLE);


        List<Point3D> lisPoint3D = new ArrayList<>();
        for (int i = 1; i < orbitRealPath.getElements().size(); i++) {
            LineTo lineTo = (LineTo) orbitRealPath.getElements().get(i);
            lisPoint3D.add(new Point3D(lineTo.getX(), lineTo.getY(), 0));
        }

        orbitPath = new PolyLine3D(lisPoint3D, 15, Color.ORANGE);

        Main.racine.getChildren().addAll(orbitPath);
        drawPath = false;
    }

    public void updatePosition(Vecteur2 sunPosition, double t) {
        Vecteur2 newPosition = orbit.findOrbitPoint(sunPosition, periapsis/ ECHELLE, apoapsis/ ECHELLE, t);
        position.setX(newPosition.getX());
        position.setY(newPosition.getY());

        if (drawPath) {
            updateOrbitPath(sunPosition);
        }
    }
}
