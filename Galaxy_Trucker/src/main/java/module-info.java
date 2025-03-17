module org.example.galaxy_trucker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.compiler;
    requires com.fasterxml.jackson.databind;
    requires org.jetbrains.annotations;

    opens org.example.galaxy_trucker to javafx.fxml, com.fasterxml.jackson.databind;
    exports org.example.galaxy_trucker;
}