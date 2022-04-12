package com.example.osimulator;

import javafx.scene.paint.Color;

public class Constantes {

    public static final int SCALE_CONST_DISTANCE = 2*498669;
    public static final double ECHELLE = 200;

    public enum InfoPlanetes {
        MERCURE(0.330,2439.7 / ECHELLE,46000000, 70000000, Color.GRAY, "Mercure"),
        VENUS(4.87,6051.8 / ECHELLE,107.48e6, 108.94e6, Color.SANDYBROWN, "Venus"),
        TERRE(5.97,6371.0 / ECHELLE,147.10e6, 152.10e6, Color.BLUE, "Terre"),
        MARS(0.642,3389.5  / ECHELLE,206.7e6, 249.2e6, Color.RED, "Mars"),
        JUPITER(1898,69911.0 / ECHELLE,740.595e6, 816.363e6, Color.ORANGE, "Jupiter"),
        SATURNE(568,58232.0 / ECHELLE,1357.554e6, 1506.527e6, Color.YELLOW, "Saturne"),
        URANUS(86.8,25362.0 / ECHELLE,2732.696e6, 3001.390e6, Color.DARKCYAN, "Uranus"),
        NEPTUNE(102,24622.0 / ECHELLE, 4471.050e6, 4558.856e6, Color.DARKBLUE, "Neptune");

        public double periapsis;
        public double apoapsis;
        public final Color color;
        public final String name;
        public final double radius;
        public final double masse;

        InfoPlanetes(double masse, double radius, double periapsis, double apoapsis, Color color, String name) {
            this.periapsis = periapsis;
            this.apoapsis = apoapsis;
            this.color = color;
            this.name = name;
            this.radius = radius;
            this.masse = masse;
        }
    }
}
