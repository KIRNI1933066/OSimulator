module com.example.osimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.osimulator to javafx.fxml;
    exports com.example.osimulator;
}