module org.example.galaxy_trucker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.compiler;
    requires com.fasterxml.jackson.databind;
    requires org.jetbrains.annotations;
    requires java.desktop;

    opens org.example.galaxy_trucker to javafx.fxml, com.fasterxml.jackson.databind;
    exports org.example.galaxy_trucker.Exceptions;
    exports org.example.galaxy_trucker.Model.Tiles;
    opens org.example.galaxy_trucker.Model.Tiles to com.fasterxml.jackson.databind, javafx.fxml;
    exports org.example.galaxy_trucker.Model.Cards;
    opens org.example.galaxy_trucker.Model.Cards to com.fasterxml.jackson.databind, javafx.fxml;
    exports org.example.galaxy_trucker.Model.Boards;
    opens org.example.galaxy_trucker.Model.Boards to com.fasterxml.jackson.databind, javafx.fxml;
    exports org.example.galaxy_trucker.Model;
    opens org.example.galaxy_trucker.Model to com.fasterxml.jackson.databind, javafx.fxml;
    exports org.example.galaxy_trucker.Model.InputHandlers;
    opens org.example.galaxy_trucker.Model.InputHandlers to com.fasterxml.jackson.databind, javafx.fxml;
    exports org.example.galaxy_trucker.Model.Goods;
    opens org.example.galaxy_trucker.Model.Goods to com.fasterxml.jackson.databind, javafx.fxml;
}