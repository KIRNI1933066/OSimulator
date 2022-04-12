package com.example.osimulator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.NumberFormat;

public class InfoPlanete extends Group {

    private Text distanceSoleil;

    public InfoPlanete(Planete planete) {
        BorderPane bP = new BorderPane();
        bP.setPrefSize(300, 600);
        bP.setMinSize(50,100);
        bP.setMaxSize(300, 550);
        bP.getStyleClass().add("fond");
        VBox vBox = new VBox();
        try {
            Image image = new Image("file:src/main/images/" + planete.name + ".png");
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(150);
            vBox.getChildren().add(imageView);

        } catch (Exception exception)
        {
            System.out.println("Image introuvable! Pour la planete : " + planete.name);
        }

        vBox.setPrefSize(200, 600);

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(0);

        Text nomPlanete = new Text(planete.name);
        nomPlanete.getStyleClass().addAll("textInfo", "textInfoGras");
        Text rayonPlanete = new Text("Rayon : " + nf.format(planete.getRadiusPlanete() * Constantes.ECHELLE) + " km");
        rayonPlanete.getStyleClass().add("textInfo");
        Text massePlanete = new Text("Masse : \n" + nf.format(planete.masse) + " x 10^24 kg");
        massePlanete.getStyleClass().add("textInfo");
        distanceSoleil = new Text();
        distanceSoleil.getStyleClass().add("textInfo");
        Text tempsPourOrbitPlanete = new Text("Temps pour une année :\n" + 365 + " jours");
        tempsPourOrbitPlanete.getStyleClass().add("textInfo");
        vBox.getChildren().addAll(nomPlanete, rayonPlanete, massePlanete, distanceSoleil, tempsPourOrbitPlanete);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(10);

        bP.setCenter(vBox);
        bP.setMouseTransparent(true);
        super.getChildren().add(bP);
    }

    public void setDistanceSoleil(Double distance)
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(0);
        distanceSoleil.setText("Distance du soleil :\n" + nf.format(distance * Main.ECHELLE) + " km");
    }
}
