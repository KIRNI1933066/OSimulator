package com.example.osimulator;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;

public class Vecteur2 {


    private DoubleProperty x;
    private DoubleProperty y;

    public Vecteur2() {
        this.setX(0);
        this.setY(0);
    }

    public Vecteur2(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    public Vecteur2(Vecteur2 vector2) {
        this.setX(vector2.getX());
        this.setY(vector2.getY());
    }

    public final void setX(double var1) {
        this.XProperty().set(var1);
    }

    public final double getX() {
        return this.x == null ? 0 : this.x.get();
    }

    public final DoubleProperty XProperty() {
        if (this.x == null) {
            this.x = new DoublePropertyBase(0) {
                public Object getBean() {
                    return Vecteur2.this;
                }

                public String getName() {
                    return "X";
                }
            };
        }

        return this.x;
    }

    public final void setY(double var1) {
        this.YProperty().set(var1);
    }

    public final double getY() {
        return this.y == null ? 0 : this.y.get();
    }

    public final DoubleProperty YProperty() {
        if (this.y == null) {
            this.y = new DoublePropertyBase(0) {
                public Object getBean() {
                    return Vecteur2.this;
                }

                public String getName() {
                    return "Y";
                }
            };
        }

        return this.y;
    }

    public final void setYProperty(double var1) {
        this.YProperty().set(var1);
    }


    public static double distanceTo(Vecteur2 vectorA, Vecteur2 vectorB) {
        double x = vectorA.getX() - vectorB.getX();
        double y = vectorA.getY() - vectorB.getY();
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public void add(double x, double y) {
        this.setX(getX() + x);
        this.setY(getY() + y);
    }

    public void add(Vecteur2 other) {
        this.setX(getX() + other.getX());
        this.setY(getY() + other.getY());
    }

    public void substract(Vecteur2 other) {
        this.setX(getX() - other.getX());
        this.setY(getY() - other.getY());
    }

    public void multiScalaire(double nombre) {
        this.setX(getX() * nombre);
        this.setY(getY() * nombre);
    }

    public boolean equals(Vecteur2 other) {
        return (this.getX() == other.getX() && this.getY() == other.getY());
    }

    @Override
    public String toString() {
        return "X: " + x.get() + " Y: " + y.get();
    }
}

