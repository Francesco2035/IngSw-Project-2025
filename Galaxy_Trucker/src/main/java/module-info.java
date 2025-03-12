module org.example.galaxy_trucker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.compiler;
    requires com.fasterxml.jackson.databind;

    opens org.example.galaxy_trucker to javafx.fxml;
    exports org.example.galaxy_trucker;
}