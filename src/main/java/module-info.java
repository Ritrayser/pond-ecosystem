module com.pond.ecosystem {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.pond to javafx.fxml;
    exports com.pond;
    exports com.pond.model;
    exports com.pond.view;
}
