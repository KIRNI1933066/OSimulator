package com.example.osimulator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.SerializablePermission;

public class Main extends Application {

    private double ancreX, ancreY;
    private double angleAncreX = 0;
    private double angleAncreY = 0;
    private final DoubleProperty ANGLE_X = new SimpleDoubleProperty(0);
    private final DoubleProperty ANGLE_Y = new SimpleDoubleProperty(0);
    public static Translate pivot;
    public static Translate zoom;

    public static final double ECHELLE = 400000;
    public static final Vecteur2 POS_SOLEIL = new Vecteur2(0, 0);
    private static final int LARGEUR_SCENE = 1000;
    private static final int HAUTEUR_SCENE = 1000;
    private static double temps = 0;
    //0.Mercure 1.Venus 2.Terre 3.Mars 4.Jupiter 5.Saturn 6.Uranus 7.Neptune
    private static final double[] TEMPS_PLANETES = {0,0,0,0,0,0,0,0};
    private static final double[] FACTEURS_VITESSE = {1.6075,1.176,1,0.8085,0.4389,0.3254,0.2287,0.1823};
    private static final double V_BASE_TERRE = 0.0001;
    public static Group racine = new Group();
    public static Group principal = new Group();
    @Override
    public void start(Stage stage) throws FileNotFoundException {

        Image vide = new Image(new FileInputStream("Blank.jpg"));
        Image etoiles = new Image(new FileInputStream("stars.jpg"));
        Sphere soleil = new Sphere(10);
        //Box ciel = new Box(50000, 50000, 50000);
        //ciel.setCullFace(CullFace.NONE);
        PhongMaterial matCiel = new PhongMaterial();
        matCiel.setDiffuseMap(etoiles);
        //ciel.setMaterial(matCiel);
        PhongMaterial matSoleil = new PhongMaterial();
        matSoleil.setDiffuseColor(Color.ORANGE);
        matSoleil.setSelfIlluminationMap(vide);
        soleil.setMaterial(matSoleil);
        racine.getChildren().addAll(soleil);

        Slider sliderVitesseTemps = new Slider(0.05,500,5);


        Planete[] planetes = new Planete[8];
        Constantes.InfoPlanetes[] infoPlanetes = Constantes.InfoPlanetes.values();
        for (int i = 0; i < planetes.length; i++) {
            planetes[i] = new Planete(infoPlanetes[i].radius, infoPlanetes[i].color, infoPlanetes[i].periapsis, infoPlanetes[i].apoapsis, infoPlanetes[i].name, infoPlanetes[i].masse);
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

        final PointLight pointLight = new PointLight();
        pointLight.setColor(Color.ORANGE);
        racine.getChildren().add(pointLight);
        racine.getChildren().add(new AmbientLight());

        SubScene scene3D = new SubScene(racine, LARGEUR_SCENE,HAUTEUR_SCENE,true, SceneAntialiasing.BALANCED);
        scene3D.setFill(Color.BLACK);
        scene3D.setCamera(camera);

        principal.getChildren().addAll(scene3D, sliderVitesseTemps);

        Scene scene2D = new Scene(principal, LARGEUR_SCENE, HAUTEUR_SCENE);
        scene2D.getStylesheets().add("file:src/main/java/com/example/osimulator/css/infoplanete.css");
        mouseControl(stage, scene2D, camera);
        //initMouseControl(racine, scene2D, stage, camera);

        stage.setScene(scene2D);
        stage.setFullScreen(true);
        stage.widthProperty().addListener((observable -> {
            scene3D.setWidth(stage.getWidth());
        }));
        stage.heightProperty().addListener((observable -> {
            scene3D.setHeight(stage.getHeight());
        }));
        stage.show();
    }

    private void mouseControl(Stage stage, Scene scene, Camera camera)
    {
        pivot = new Translate(0, 0, 0);
        zoom = new Translate(0, 0, -3000);
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Z_AXIS);

        camera.getTransforms().addAll(
                pivot,
                rotateY,
                rotateX,
                zoom
        );

        Vecteur2 basePos = new Vecteur2();
        Vecteur2 basePivot = new Vecteur2(pivot.getX(), pivot.getY());
        Vecteur2 baseRotate = new Vecteur2();
        scene.setOnMousePressed((mouseEvent -> {
            basePos.setX(mouseEvent.getSceneX());
            basePos.setY(mouseEvent.getSceneY());

            if (mouseEvent.isPrimaryButtonDown())
            {
                basePivot.setX(zoom.getX());
                basePivot.setY(zoom.getY());
            }
            if (mouseEvent.isSecondaryButtonDown())
            {
                baseRotate.setX(rotateY.angleProperty().get());
                baseRotate.setY(rotateX.angleProperty().get());
            }
        }));
        scene.setOnMouseDragged((mouseEvent ->
        {
            if (mouseEvent.isPrimaryButtonDown())
            {
                if (pivot.xProperty().isBound())
                {
                    pivot.xProperty().unbind();
                    pivot.xProperty().set(0);
                }
                if (pivot.yProperty().isBound()) {
                    pivot.yProperty().unbind();
                    pivot.yProperty().set(0);
                }

                double moveX = basePivot.getX() + (basePos.getX() - mouseEvent.getSceneX());
                double moveY = basePivot.getY() + (basePos.getY() - mouseEvent.getSceneY());

                zoom.setX(moveX);
                zoom.setY(moveY);
            }
            if (mouseEvent.isSecondaryButtonDown())
            {
                double rotateByX = baseRotate.getX() + (basePos.getX() - mouseEvent.getSceneX());
                double rotateByY = baseRotate.getY() + (basePos.getY() - mouseEvent.getSceneY());
                rotateY.angleProperty().set(rotateByX);
                rotateX.angleProperty().set(rotateByY);
            }
        }));

        stage.addEventHandler(ScrollEvent.SCROLL, mouseEvent -> {
            zoom.setZ(zoom.getZ() + mouseEvent.getDeltaY()*3);
        });
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
            camera.translateXProperty().set(ev.getSceneX());
            camera.translateYProperty().set(ev.getDeltaY());
            camera.translateZProperty().set(camera.getTranslateZ() + delta);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}