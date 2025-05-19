    module org.example.galaxy_trucker {
        requires javafx.controls;
        requires javafx.fxml;
        requires org.controlsfx.controls;
        requires java.compiler;
        requires com.fasterxml.jackson.databind;
        requires org.jetbrains.annotations;
        requires java.desktop;
        requires jdk.jdi;
        requires java.rmi;
        requires com.google.gson;
        requires org.fusesource.jansi;
        requires jdk.sctp;
        requires org.jline;
        requires jdk.compiler;


            // Jackson
        opens org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents to com.fasterxml.jackson.databind;
        opens org.example.galaxy_trucker.Controller.Messages to com.fasterxml.jackson.databind;
        opens org.example.galaxy_trucker.Controller.Messages.TileSets to com.fasterxml.jackson.databind;

        opens org.example.galaxy_trucker to javafx.fxml, com.fasterxml.jackson.databind;

        exports org.example.galaxy_trucker.Exceptions;
        exports org.example.galaxy_trucker.Model.Tiles;
        opens org.example.galaxy_trucker.Model.Tiles to com.fasterxml.jackson.databind, javafx.fxml;
        exports org.example.galaxy_trucker.Model.Cards;
        opens org.example.galaxy_trucker.Model.Cards to com.fasterxml.jackson.databind, javafx.fxml;
        exports org.example.galaxy_trucker.Model.Boards;
        opens org.example.galaxy_trucker.Model.Boards to com.fasterxml.jackson.databind, javafx.fxml;
        exports org.example.galaxy_trucker.Model;
        opens org.example.galaxy_trucker.Model to com.google.gson, com.fasterxml.jackson.databind, javafx.fxml;
        exports org.example.galaxy_trucker.ClientServer to java.rmi;
        exports org.example.galaxy_trucker.Model.Goods;
        opens org.example.galaxy_trucker.Model.Goods to com.fasterxml.jackson.databind, javafx.fxml;
        exports org.example.galaxy_trucker;
        opens org.example.galaxy_trucker.Model.Connectors to com.fasterxml.jackson.databind;
        exports org.example.galaxy_trucker.Model.Connectors to com.fasterxml.jackson.databind, javafx.fxml;
        exports org.example.galaxy_trucker.Model.Boards.Actions;
        opens org.example.galaxy_trucker.Model.Boards.Actions to com.fasterxml.jackson.databind, javafx.fxml;
        exports org.example.galaxy_trucker.Model.PlayerStates;
        exports org.example.galaxy_trucker.ClientServer.RMI to java.rmi;
        opens org.example.galaxy_trucker.Commands to com.google.gson, com.fasterxml.jackson.databind;
        exports org.example.galaxy_trucker.View.GUI;
        exports org.example.galaxy_trucker.View.TUI to java.rmi;
        exports org.example.galaxy_trucker.View to java.rmi;
    }
