package com.example.osimulator;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

import static com.example.osimulator.Main.*;

public class Planete extends Sphere {

    private final Vecteur2 mousepos = new Vecteur2();
    private Vecteur2 position, speed;
    public String name;
    private Orbit orbit;
    private double periapsis, apoapsis;
    private PolyLine3D orbitPath;
    private double radius;
    private Color couleur;
    private InfoPlanete infoPlanete;

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
        this.radius = radius;
        this.couleur = color;
        orbit = new Orbit(5000);

        infoPlanete = new InfoPlanete(this);
        BorderPane bp = (BorderPane)infoPlanete.getChildren().get(0);
        super.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown())
            {
                if (Main.pivot.xProperty().isBound())
                {
                    Main.pivot.xProperty().unbind();
                }
                if (Main.pivot.yProperty().isBound())
                {
                    Main.pivot.yProperty().unbind();
                }
                Main.pivot.xProperty().bind(this.translateXProperty());
                Main.pivot.yProperty().bind(this.translateYProperty());
                zoom.setX(0);
                zoom.setY(0);
            }
        });
        super.setOnMouseEntered(mouseEvent -> {
            infoPlanete.setTranslateX(mouseEvent.getSceneX() + 5);
            infoPlanete.setTranslateY(mouseEvent.getSceneY() - bp.getPrefHeight());
            if (!principal.getChildren().contains(infoPlanete))
                principal.getChildren().add(infoPlanete);
        });
        super.setOnMouseExited(mouseEvent -> {
            principal.getChildren().remove(infoPlanete);
        });
    }

    public void updateOrbitPath(Vecteur2 sunPosition) {
        if (orbitPath != null) {
            Main.racine.getChildren().remove(orbitPath);
        }
        Path orbitRealPath = orbit.getPathOrbit(sunPosition, periapsis/ ECHELLE, apoapsis/ ECHELLE);


        List<Point3D> listPoints3D = new ArrayList<>();
        for (int i = 1; i < orbitRealPath.getElements().size(); i++) {
            LineTo lineTo = (LineTo) orbitRealPath.getElements().get(i);
            listPoints3D.add(new Point3D(lineTo.getX(), lineTo.getY(), 0));
        }

        BorderPane bp = (BorderPane)infoPlanete.getChildren().get(0);
        orbitPath = new PolyLine3D(listPoints3D, 15, Color.ORANGE, PolyLine3D.LineType.TRIANGLE);

        orbitPath.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown())
            {
                if (Main.pivot.xProperty().isBound())
                {
                    Main.pivot.xProperty().unbind();
                }
                if (Main.pivot.yProperty().isBound())
                {
                    Main.pivot.yProperty().unbind();
                }
                Main.pivot.xProperty().bind(this.translateXProperty());
                Main.pivot.yProperty().bind(this.translateYProperty());
                zoom.setX(0);
                zoom.setY(0);
            }
        });
        orbitPath.setOnMouseEntered(mouseEvent -> {
            infoPlanete.setTranslateX(mouseEvent.getSceneX() + 5);
            infoPlanete.setTranslateY(mouseEvent.getSceneY() - bp.getPrefHeight());
            if (!principal.getChildren().contains(infoPlanete))
                principal.getChildren().add(infoPlanete);
        });
        orbitPath.setOnMouseExited(mouseEvent -> {
            principal.getChildren().remove(infoPlanete);
        });

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

    public double getRadiusPlanete() {
        return radius;
    }

    public Color getCouleur() {
        return couleur;
    }
}
