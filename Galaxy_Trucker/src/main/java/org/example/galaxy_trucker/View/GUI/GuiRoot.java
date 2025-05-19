package org.example.galaxy_trucker.View.GUI;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.LobbyEvent;
import org.example.galaxy_trucker.View.View;
import org.example.galaxy_trucker.Controller.Messages.GameBoardEvent;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GuiRoot implements View {

    private Thread guiThread;
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private Stage primaryStage;

    private ArrayList<Event> lobbyEvents = new ArrayList<>();


    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void updateBoard(TileEvent tileEvent) {

    }

    public GuiRoot(){
        guiThread = new Thread(() -> GuiMain.launchApp(this));
        guiThread.start();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public String askInput(String message) {
        try {
            String toSend = inputQueue.take();
            //System.out.println("ask input: " + toSend);
            return toSend;
        } catch (InterruptedException e) {
            //Thread.currentThread().interrupt();
            return "";
        }
    }

    @Override
    public void updateHand(HandEvent event) {

    }

    @Override
    public void updateGameboard(GameBoardEvent event) {

    }

    @Override
    public void updateCoveredTilesSet(CoveredTileSetEvent event) {

    }

    @Override
    public void updateUncoveredTilesSet(UncoverdTileSetEvent event) {

    }

    @Override
    public void showDeck(DeckEvent event) {

    }

    @Override
    public void showCard(int id) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void connect() throws IOException {

    }

    @Override
    public void setGameboard(int lv) {

    }

    @Override
    public void showLobby(LobbyEvent event) {
        //mi arriva il lobby event ogni volta che qualcuno crea/si aggiunge ad un game

        Label titleLabel = new Label("GALAXY TRUCKERS");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        // Lista giocatori

        lobbyEvents.add(event);

        ListView<LobbyEvent> gamesList = new ListView<>();
        if(event.getLv() > 0)
            gamesList.getItems().add(event);

        gamesList.setPrefHeight(150);
        gamesList.setPlaceholder(new Label("No existing games"));

        gamesList.setCellFactory(p -> new ListCell<>() {
            @Override
            protected void updateItem(LobbyEvent newGame, boolean empty) {
                super.updateItem(newGame, empty);

                if(!empty && newGame != null) {
                    Label gameLabel = new Label("Game: " + newGame.getGameId());
                    Label playersLabel = new Label("Players: " + String.join(", ", newGame.getPlayers()));

                    Button joinButton = joinButtonMaker(newGame);

                    VBox labels = new VBox(5, gameLabel, playersLabel);
                    HBox button = new HBox(5, joinButton);
                    button.setAlignment(Pos.CENTER_RIGHT);
                    labels.setAlignment(Pos.CENTER_LEFT);
                    HBox cell = new HBox(248, labels, button);
                    setText(null);
                    setGraphic(cell);
                }
                else {
                    setText(null);
                    setGraphic(null);
                }

            }
        });





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
            newGameStage.setTitle("Create New Game");


            //Campi di input
            TextField usernameField = new TextField();
            usernameField.setPromptText("Player Name");

            TextField gameNameField = new TextField();
            gameNameField.setPromptText("Game Name");

            ComboBox<String> levelBox = new ComboBox<>();
            levelBox.getItems().addAll("Tutorial", "Complete Game");
            levelBox.setPromptText("Select Game Mode");

            Button confirmButton = new Button("Confirm");
            Button goBackButton = goBackButtonMaker(newGameStage) ;

            confirmButton.disableProperty().bind(
                    usernameField.textProperty().isEmpty()
                            .or(gameNameField.textProperty().isEmpty())
                            .or(levelBox.valueProperty().isNull())
            );

            confirmButton.setOnAction(ev -> {

                String level = levelBox.getValue();
                int gameLv;

                if(level.equals("Tutorial"))
                    gameLv = 1;
                else gameLv = 2;

                newGameStage.close();
                inputQueue.add("Login");
                inputQueue.add(usernameField.getText());
                inputQueue.add(gameNameField.getText());
                inputQueue.add(String.valueOf(gameLv));

            });



            HBox Buttons = new HBox(50, confirmButton, goBackButton);

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


        Platform.runLater(() -> {
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("Lobby");
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }



    private Button joinButtonMaker(LobbyEvent joining) {
        Button joinButton = new Button("Join");
        joinButton.setOnAction(e -> {
            Stage InsertNameStage = new Stage();
            InsertNameStage.setTitle("Insert Username");

            TextField usernameField = new TextField();
            usernameField.setPromptText("Player Name");

            Button confirmButton = new Button("Confirm");
            Button goBackButton = goBackButtonMaker(InsertNameStage);

            confirmButton.disableProperty().bind(usernameField.textProperty().isEmpty());

            HBox Buttons = new HBox(50, confirmButton, goBackButton);

            Buttons.setAlignment(Pos.CENTER);
            Buttons.setPadding(new Insets(15));

            VBox formBox = new VBox(10,
                    new Label("Insert Username:"), usernameField,
                    Buttons
            );
            formBox.setPadding(new Insets(15));
            formBox.setAlignment(Pos.CENTER);

            Scene insertNamescene = new Scene(formBox, 300, 150);
            InsertNameStage.setScene(insertNamescene);
            InsertNameStage.initOwner(primaryStage);
            InsertNameStage.initModality(Modality.WINDOW_MODAL);
            InsertNameStage.show();


            confirmButton.setOnAction(ev -> {
                InsertNameStage.close();
                inputQueue.add("Login");
                inputQueue.add(usernameField.getText());
                inputQueue.add(joining.getGameId());
                inputQueue.add(String.valueOf(joining.getLv()));
            });
        });
        return joinButton;
    }


    private Button goBackButtonMaker(Stage  stage) {
        Button GobackButton = new Button("Cancel");
        GobackButton.setOnAction(ev -> {
            stage.close();
        });
        return GobackButton;
    }




    public void setGuiMain(GuiMain guiMain) {
    }


    public void goToFirstScene(Stage primaryStage) {
        Label titleLabel = new Label("GALAXY TRUCKERS");
        titleLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");

        Button startButton = new Button("Start!");
        startButton.setStyle("-fx-font-size: 20px;");

        VBox TitleScreenBox = new VBox(10, titleLabel, startButton);
        TitleScreenBox.setAlignment(Pos.CENTER);
        TitleScreenBox.setPadding(new Insets(20));
        //TitleScreenBox.setMaxWidth(400);

        BorderPane root = new BorderPane(TitleScreenBox);
        root.setPadding(new Insets(10));

        Scene lobbyScene = new Scene(root, 600, 400);
        primaryStage.setTitle("Galaxy Truckers");
        primaryStage.setScene(lobbyScene);
        primaryStage.show();


        startButton.setOnAction(e -> {
            inputQueue.add("Lobby");
        });
    }


}
