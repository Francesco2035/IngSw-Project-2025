package org.example.galaxy_trucker.View.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.galaxy_trucker.Controller.Messages.*;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.View.View;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GuiRoot implements View {

    private Thread guiThread;
    private HashMap<String,LobbyEvent> lobbyEventHashMap = new HashMap<>();
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
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
    public void showCard(CardEvent event) {

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
        lobbyEventHashMap.remove(event.getGameId());
        lobbyEventHashMap.put(event.getGameId(), event);
        //stampi for tutti i cazzi in lobby
        //se il player clicca un game devi
        //metti nella coda Login
        //metti nella coda playerid
        //metti nella coda gameId
        //metti nella coda livello (1/2)

    }

    @Override
    public void showLobbyGame(GameLobbyEvent event) {

    }

    @Override
    public void phaseChanged(PhaseEvent event) {

    }

    public void setGuiMain(GuiMain guiMain) {
    }

    public void goToFirstScene(Stage primaryStage) {
        Label titleLabel = new Label("GALAXY TRUCKERS");
        //se ha premuto
        String cmd = "Lobby";
        inputQueue.add(cmd);
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
                inputQueue.add(username);
                inputQueue.add(gameName);
                inputQueue.add(String.valueOf(gameLv));

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

    //public gameboard scene(scena)
}
