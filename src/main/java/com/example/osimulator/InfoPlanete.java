package com.example.osimulator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InfoPlanete extends Group {

    public InfoPlanete() {
        BorderPane bP = new BorderPane();
        bP.setPrefSize(300, 300);
        bP.setStyle("-fx-background-color: #999999;");
        Circle circle = new Circle(50, Color.WHITE);
        Text nomPlanete = new Text("Nom : ");
        nomPlanete.setFont(new Font(25));
        Text rayonPlanete = new Text("Rayon : ");
        rayonPlanete.setFont(new Font(25));
        Text distanceSoleil = new Text("Distance du soleile : ");
        distanceSoleil.setFont(new Font(25));
        VBox vBox = new VBox(circle, nomPlanete, rayonPlanete, distanceSoleil);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(10);

        bP.setCenter(vBox);
        super.getChildren().add(bP);
    }

    public InfoPlanete(Planete planete) {
        BorderPane bP = new BorderPane();
        bP.setPrefSize(300, 300);
        bP.setStyle("-fx-background-color: #999999;");
        Circle circle = new Circle(50, planete.getCouleur());
        Text nomPlanete = new Text("Nom : " + planete.name);
        nomPlanete.setFont(new Font(25));
        Text rayonPlanete = new Text("Rayon : " + planete.getRadiusPlanete());
        rayonPlanete.setFont(new Font(25));
        Text distanceSoleil = new Text("Distance du soleile : ");
        distanceSoleil.setFont(new Font(25));
        VBox vBox = new VBox(circle, nomPlanete, rayonPlanete, distanceSoleil);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(10);

        bP.setCenter(vBox);
        super.getChildren().add(bP);
    }

    public InfoPlanete(double posX, double posY, Planete planete) {
        super.setTranslateX(posX);
        super.setTranslateY(posY);

        BorderPane bP = new BorderPane();
        bP.setPrefSize(300, 300);
        bP.setStyle("-fx-background-color: #999999;");
        Circle circle = new Circle(50, planete.getCouleur());
        Text nomPlanete = new Text("Nom : " + planete.name);
        nomPlanete.setFont(new Font(25));
        Text rayonPlanete = new Text("Rayon : " + planete.getRadiusPlanete());
        rayonPlanete.setFont(new Font(25));
        Text distanceSoleil = new Text("Distance du soleile : ");
        distanceSoleil.setFont(new Font(25));
        VBox vBox = new VBox(circle, nomPlanete, rayonPlanete, distanceSoleil);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(10);

        bP.setCenter(vBox);
        super.getChildren().add(bP);
    }
}
