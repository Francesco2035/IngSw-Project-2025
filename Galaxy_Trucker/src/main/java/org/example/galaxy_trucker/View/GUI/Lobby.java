package org.example.galaxy_trucker.View.GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Lobby extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Titolo
        Label titleLabel = new Label("GALAXY TRUCKERS");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        // Lista giocatori
        ListView<String> gamesList = new ListView<>();
        gamesList.setPrefHeight(150);
        gamesList.setPlaceholder(new Label("No existing games"));

        // Area messaggi console/chat (opzionale)
        TextArea messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        messageArea.setPrefHeight(100);
        messageArea.setPromptText("Log della lobby...");

        // Bottone per avviare la partita (solo host)
        Button newGame = new Button("New Game");
        newGame.setStyle("-fx-font-size: 14px;");
        newGame.setOnAction(e -> {
            Stage newGameStage = new Stage();
            newGameStage.setTitle("New Game");

            // Campi di input
            TextField usernameField = new TextField();
            usernameField.setPromptText("Player Name");

            TextField gameNameField = new TextField();
            gameNameField.setPromptText("Game Name");

            ComboBox<String> levelBox = new ComboBox<>();
            levelBox.getItems().addAll("Tutorial", "Complete Game");
            levelBox.setPromptText("Select Game Mode");

            Button confirmButton = new Button("Confirm");
            Button GobackButton = new Button("Cancel");

            confirmButton.disableProperty().bind(
                    usernameField.textProperty().isEmpty()
                            .or(gameNameField.textProperty().isEmpty())
                            .or(levelBox.valueProperty().isNull())
            );

            confirmButton.setOnAction(ev -> {
                String username = usernameField.getText();
                String gameName = gameNameField.getText();

                String level = levelBox.getValue();
                int gameLv;

                if(level.equals("Tutorial"))
                    gameLv = 1;
                else gameLv = 2;

                newGameStage.close();

            });

            GobackButton.setOnAction(ev -> {
                newGameStage.close();

            });


            HBox Buttons = new HBox(50, confirmButton, GobackButton);

            Buttons.setAlignment(Pos.CENTER);
            Buttons.setPadding(new Insets(15));

            VBox formBox = new VBox(10,
                    new Label("Player Name:"), usernameField,
                    new Label("Game Name:"), gameNameField,
                    new Label("Game Mode:"), levelBox,
                    Buttons
            );
            formBox.setPadding(new Insets(15));
            formBox.setAlignment(Pos.CENTER);

            Scene newGameScene = new Scene(formBox, 300, 300);
            newGameStage.setScene(newGameScene);
            newGameStage.initOwner(primaryStage); // Blocca interazioni con la finestra principale
            newGameStage.initModality(Modality.WINDOW_MODAL);
            newGameStage.show();
        });

        // Layout centrale
        VBox centerBox = new VBox(10, titleLabel, gamesList, messageArea, newGame);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));
        centerBox.setMaxWidth(400);

        BorderPane root = new BorderPane(centerBox);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Lobby");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



