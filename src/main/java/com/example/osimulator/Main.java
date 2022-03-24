package com.example.osimulator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main extends Application {

    private double ancreX, ancreY;
    private double angleAncreX = 0;
    private double angleAncreY = 0;
    private final DoubleProperty ANGLE_X = new SimpleDoubleProperty(0);
    private final DoubleProperty ANGLE_Y = new SimpleDoubleProperty(0);

    public static final double ECHELLE = 400000;
    private static final Vecteur2 POS_SOLEIL = new Vecteur2(0, 0);
    private static final int LARGEUR_SCENE = 1000;
    private static final int HAUTEUR_SCENE = 1000;
    private static double temps = 0;
    //0.Mercure 1.Venus 2.Terre 3.Mars 4.Jupiter 5.Saturn 6.Uranus 7.Neptune
    private static final double[] TEMPS_PLANETES = {0,0,0,0,0,0,0,0};
    private static final double[] FACTEURS_VITESSE = {1.6075,1.176,1,0.8085,0.4389,0.3254,0.2287,0.1823};
    private static final double V_BASE_TERRE = 0.0001;
    public static Group racine = new Group();

    @Override
    public void start(Stage stage) throws FileNotFoundException {

        Image vide = new Image(new FileInputStream("Blank.jpg"));
        Sphere soleil = new Sphere(10);
        PhongMaterial matSoleil = new PhongMaterial();
        matSoleil.setDiffuseColor(Color.ORANGE);
        matSoleil.setSelfIlluminationMap(vide);
        soleil.setMaterial(matSoleil);
        racine.getChildren().add(soleil);

        Slider sliderVitesseTemps = new Slider(0.05,500,5);
        sliderVitesseTemps.setTranslateX(-1000);
        sliderVitesseTemps.setTranslateY(-1000);
        sliderVitesseTemps.setScaleX(3);
        Group secondaire = new Group(sliderVitesseTemps, racine);


        Planete[] planetes = new Planete[8];
        Constantes.InfoPlanetes[] infoPlanetes = Constantes.InfoPlanetes.values();
        for (int i = 0; i < planetes.length; i++) {
            planetes[i] = new Planete(infoPlanetes[i].radius, infoPlanetes[i].color, infoPlanetes[i].periapsis, infoPlanetes[i].apoapsis, infoPlanetes[i].name);
            racine.getChildren().add(planetes[i]);
        }

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                double vitesseBase = V_BASE_TERRE * sliderVitesseTemps.valueProperty().get();
                temps = temps + sliderVitesseTemps.valueProperty().get();
                for (int i = 0; i < TEMPS_PLANETES.length; i++) {
                    TEMPS_PLANETES[i] = TEMPS_PLANETES[i] + vitesseBase * FACTEURS_VITESSE[i];
                    if (TEMPS_PLANETES[i] > 1)
                        TEMPS_PLANETES[i] = 0;
                }
                if (temps > 1)
                    temps = 0;
                for (Planete planet : planetes) {
                    switch (planet.name) {
                        case "Mercure" -> planet.updatePosition(POS_SOLEIL, TEMPS_PLANETES[0]);
                        case "Venus" -> planet.updatePosition(POS_SOLEIL, TEMPS_PLANETES[1]);
                        case "Terre" -> planet.updatePosition(POS_SOLEIL, TEMPS_PLANETES[2]);
                        case "Mars" -> planet.updatePosition(POS_SOLEIL, TEMPS_PLANETES[3]);
                        case "Jupiter" -> planet.updatePosition(POS_SOLEIL, TEMPS_PLANETES[4]);
                        case "Saturne" -> planet.updatePosition(POS_SOLEIL, TEMPS_PLANETES[5]);
                        case "Uranus" -> planet.updatePosition(POS_SOLEIL, TEMPS_PLANETES[6]);
                        case "Neptune" -> planet.updatePosition(POS_SOLEIL, TEMPS_PLANETES[7]);
                    }
                }
            }
        }.start();

        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(100000);
        camera.translateZProperty().set(-3000);

        final PointLight pointLight = new PointLight();
        pointLight.setColor(Color.ORANGE);
        racine.getChildren().add(pointLight);
        racine.getChildren().add(new AmbientLight());

        Scene scene = new Scene(secondaire, LARGEUR_SCENE,HAUTEUR_SCENE,true);
        initMouseControl(racine, scene, stage, camera);
        scene.setFill(Color.BLACK);
        scene.setCamera(camera);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private void initMouseControl(Group group, Scene scene, Stage stage, Camera camera) {
        Rotate rotationX;
        Rotate rotationY;
        group.getTransforms().addAll(
                rotationX = new Rotate(0, Rotate.X_AXIS),
                rotationY = new Rotate(0, Rotate.Y_AXIS)
        );
        rotationX.angleProperty().bind(ANGLE_X);
        rotationY.angleProperty().bind(ANGLE_Y);

        scene.setOnMousePressed(ev -> {
            ancreX = ev.getSceneX();
            ancreY = ev.getSceneY();
            angleAncreX = ANGLE_X.get();
            angleAncreY = ANGLE_Y.get();
        });

        scene.setOnMouseDragged(ev -> {
            ANGLE_X.set(angleAncreX - (ancreY - ev.getSceneY()));
            ANGLE_Y.set(angleAncreY + (ancreX - ev.getScreenX()));
        });

        stage.addEventHandler(ScrollEvent.SCROLL, ev -> {
            double delta = ev.getDeltaY();
            camera.translateZProperty().set(camera.getTranslateZ() + delta);
        });

        /*EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

            @Override
            public void handle(ScrollEvent event) {

                double delta = 1.2;

                double scale = canvas.getScale(); // currently we only use Y, same value is used for X
                double oldScale = scale;

                if (event.getDeltaY() < 0)
                    scale /= delta;
                else
                    scale *= delta;

                scale = clamp( scale, MIN_SCALE, MAX_SCALE);

                double f = (scale / oldScale)-1;

                double dx = (event.getSceneX() - (canvas.getBoundsInParent().getWidth()/2 + canvas.getBoundsInParent().getMinX()));
                double dy = (event.getSceneY() - (canvas.getBoundsInParent().getHeight()/2 + canvas.getBoundsInParent().getMinY()));

                canvas.setScale( scale);

                // note: pivot value must be untransformed, i. e. without scaling
                canvas.setPivot(f*dx, f*dy);

                event.consume();

            }

        };*/
    }



    public static void main(String[] args) {
        launch();
    }
}