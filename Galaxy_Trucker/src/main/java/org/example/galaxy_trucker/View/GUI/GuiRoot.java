package org.example.galaxy_trucker.View.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.galaxy_trucker.Controller.Messages.*;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Controller.Messages.LobbyEvent;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.ClientModel.States.LobbyClient;
import org.example.galaxy_trucker.View.View;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GuiRoot implements View {

    private Thread guiThread;
    private HashMap<String,LobbyEvent> lobbyEventHashMap = new HashMap<>();
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private Stage primaryStage;

    private PlayerClient playerClient;
    private GuiOut printer;
    private ArrayList<LobbyEvent> lobbyEvents = new ArrayList<>();

    private String myGameName;
    private String myName;
    private int myGameLv;
    private boolean amIReady;

    public void setStage(Stage primaryStage) {
        printer.setStage(primaryStage);
        this.primaryStage = primaryStage;
    }

    @Override
    public void updateBoard(TileEvent tileEvent) {

    }

    public GuiRoot(){
        printer = new GuiOut();
        playerClient = new PlayerClient();

        guiThread = new Thread(() -> GuiMain.launchApp(this));
        guiThread.start();


    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public String askInput(String message) {
        String toSend = inputQueue.poll();
        //System.out.println("ask input: " + toSend);
        if (toSend != null) {
            return toSend;

        }
        else {
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
        //mi arriva il lobby event ogni volta che qualcuno crea/si aggiunge ad un game

        Label titleLabel = new Label("GALAXY TRUCKERS");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        // Lista giocatori

        if(event.getLv() > 0)
            lobbyEvents.add(event);

        ListView<LobbyEvent> gamesList = new ListView<>();
        gamesList.setPrefHeight(150);
        gamesList.setPlaceholder(new Label("No existing games"));

        for(LobbyEvent e : lobbyEvents)
            gamesList.getItems().add(e);

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



        // Bottone per avviare la partita
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
            Button goBackButton = goBackButtonMaker(newGameStage);

            confirmButton.disableProperty().bind(
                    usernameField.textProperty().isEmpty()
                            .or(gameNameField.textProperty().isEmpty())
                            .or(levelBox.valueProperty().isNull())
            );

            confirmButton.setOnAction(ev -> {

                String level = levelBox.getValue();


                if(level.equals("Tutorial"))
                    myGameLv = 1;
                else myGameLv = 2;

                newGameStage.close();

                myName = usernameField.getText();
                myGameName = gameNameField.getText();

                inputQueue.add("Login");
                inputQueue.add(myName);
                inputQueue.add(myGameName);
                inputQueue.add(String.valueOf(myGameLv));

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
        VBox MainBox = new VBox(10, titleLabel, gamesList, newGame);
        MainBox.setAlignment(Pos.CENTER);
        MainBox.setPadding(new Insets(20));
        MainBox.setMaxWidth(600);

        BorderPane root = new BorderPane(MainBox);
        root.setPadding(new Insets(10));

        printer.setLobby( new Scene(root, 800, 600));

        playerClient.showGame(printer);

    }



    @Override
    public void showLobbyGame(GameLobbyEvent event){


        Label GameNameLabel = new Label("Game: " + myGameName);
        GameNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        ListView<String> players = new ListView<>();
        players.setPrefHeight(100);

        String s;
        for(int i=0; i< event.getPlayers().size(); i++){
            if(event.getPlayers().get(i).equals(myName)) {
                s = event.getPlayers().get(i) + " (You)";
                amIReady = event.getReady().get(i);
            }
            else s= event.getPlayers().get(i);

            if(event.getReady().get(i))
                players.getItems().add(s + " -> Ready!");
            else players.getItems().add(s+ " -> Not Ready");
        }


        Image playerBoardImg;
        if(myGameLv == 1)
            playerBoardImg = new Image(getClass().getResourceAsStream("/GUI/cardboard-lv1.jpg"));
        else
            playerBoardImg = new Image(getClass().getResourceAsStream("/GUI/cardboard-lv2.jpg"));

        ImageView playerBoard = new ImageView(playerBoardImg);
        playerBoard.setPreserveRatio(true);
        playerBoard.setSmooth(true);
        playerBoard.setFitWidth(400);
        playerBoard.maxWidth(300);

//        VBox imageBox = new VBox(playerBoard);
//        imageBox.setAlignment(Pos.CENTER);
//        imageBox.setPrefSize(40, 30); // iniziale
//        imageBox.setMaxSize(40, 30); // permette l'espansione
//

//        playerBoard.fitWidthProperty().bind(imageBox.widthProperty());
//        playerBoard.fitHeightProperty().bind(imageBox.heightProperty());




        VBox mainBox = new VBox(10, GameNameLabel, players);
//        VBox.setVgrow(imageBox, Priority.ALWAYS);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setPadding(new Insets(10));

        Button quitButton = new Button("Quit");
        Button readyButton = new Button();

        if(amIReady)
            readyButton.setText("Not Ready");
        else readyButton.setText("Ready!");

        readyButton.setOnAction(e -> {
            if(amIReady)
                inputQueue.add("Ready false");
            else inputQueue.add("Ready true");
        });


        quitButton.setOnAction(e -> {
            Stage confirmStage = new Stage();
            confirmStage.setTitle("Quitting");

            Label quitLabel = new Label("Are You Sure?");
            quitLabel.setStyle("-fx-font-size: 15px");

            Button confirmButton = new Button("Pretty Sure");
            Button goBackButton = goBackButtonMaker(confirmStage);

            HBox buttons = new HBox(50, goBackButton, confirmButton);
            buttons.setAlignment(Pos.CENTER);
            buttons.setPadding(new Insets(5));

            VBox quitBox = new VBox(3, quitLabel, buttons);
            quitBox.setAlignment(Pos.CENTER);

            Scene newGameScene = new Scene(quitBox, 250, 80);
            confirmStage.setScene(newGameScene);
            confirmStage.initOwner(primaryStage); // Blocca interazioni con la finestra principale
            confirmStage.initModality(Modality.WINDOW_MODAL);

            confirmStage.show();

        });


        HBox Buttons = new HBox(50, quitButton, readyButton);
        Buttons.setPadding(new Insets(15));
        Buttons.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(mainBox, playerBoard, Buttons);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);

        playerBoard.setTranslateY(200);
        Buttons.setTranslateY(250);

        Scene scene = new Scene(root, 800, 600);


        printer.setGameLobby(scene);
        playerClient.showGame(printer);

        //idplayer li salvo in un arraylist
        //player->playerstate.showGame
    }

    @Override
    public void rewardsChanged(RewardsEvent event) {

    }

    @Override
    public void phaseChanged(@NotNull PhaseEvent event) {
        playerClient.setPlayerState(event.getStateClient());
        playerClient.showGame(printer);
        //player.setstate(event.getpahse)
        //plary.state.showGUI
    }

    @Override
    public void exceptionOccurred(ExceptionEvent exceptionEvent) {

    }


    private @NotNull Button joinButtonMaker(LobbyEvent joining) {
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
                myName = usernameField.getText();
                myGameName = joining.getGameId();
                myGameLv = joining.getLv();

                InsertNameStage.close();
                inputQueue.add("Login");
                inputQueue.add(myName);
                inputQueue.add(myGameName);
                inputQueue.add(String.valueOf(myGameLv));
            });
        });
        return joinButton;
    }


    private @NotNull Button goBackButtonMaker(Stage  stage) {
        Button GobackButton = new Button("Cancel");
        GobackButton.setOnAction(ev -> {
            stage.close();
        });
        return GobackButton;
    }




    public void goToFirstScene() {


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

        printer.setTitleScreen(new Scene(root, 600, 400));
        printer.printTitleScreen();

        startButton.setOnAction(e -> {
            inputQueue.add("Lobby");
            playerClient.setPlayerState(new LobbyClient());
        });
    }

    //printplayerboard(){..}

}
