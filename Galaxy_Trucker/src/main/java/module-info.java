module org.example.galaxy_trucker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.example.galaxy_trucker to javafx.fxml;
    exports org.example.galaxy_trucker;
}