package org.example.galaxy_trucker.View.GUI;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
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

    private StackPane primaryRoot;
    private Pane contentRoot;
    private Scene primaryScene;


    private PlayerClient playerClient;
    private GuiOut printer;
    private ArrayList<LobbyEvent> lobbyEvents = new ArrayList<>();

    private String myGameName;
    private String myName;
    private int myGameLv;
    private boolean amIReady;
    private boolean amIBuilding;

    private ArrayList<Integer> discardedTiles;
    private HashMap<Integer, VBox> discardedMap;
    private Image playerBoardImg;
    private Image gameBoardImg;
    private GridPane myBoard;
    private Image tilePlaceholder;
    private ImageView tileImage;
    private TilePane uncoveredTiles;
    private int tileRotation;

    public void setStage(Stage primaryStage) {
        printer.setStage(primaryStage);
        this.primaryStage = primaryStage;
    }



    public GuiRoot(){
        tileImage = new ImageView();
        tileImage.setImage(null);
        tileImage.setFitWidth(100);
        tileImage.setPreserveRatio(true);
        uncoveredTiles = new TilePane();
        amIBuilding = true;

        myBoard = new GridPane();
        printer = new GuiOut(this);
        playerClient = new PlayerClient();
        discardedTiles = new ArrayList<>();
        discardedMap = new HashMap<>();

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
    public void updateBoard(TileEvent event) {

        ImageView tileImg = new ImageView();

        if(event.getId() != 158){
            if(event.getId() == 157) {
                tileImg.setImage(tilePlaceholder);
                tileImg.setOpacity(0.5);
                if(amIBuilding){
                    tileImg.setOnMouseClicked(e -> {
                        inputQueue.add("InsertTile "+ event.getY() +" "+ event.getX() +" "+ tileRotation);
                        tileImage.setImage(tilePlaceholder);
                        tileImage.setOpacity(0.5);
                    });
                    tileImg.setOnMouseEntered(e -> {
                        tileImg.setOpacity(1);

                    });
                    tileImg.setOnMouseExited(e -> {
                        tileImg.setOpacity(0.5);
                    });
                }

            }
            else{
                tileImg.setImage(new Image(getClass().getResourceAsStream("/GUI/Tiles/tile ("+ event.getId() +").jpg")));
                tileImg.setOpacity(1);
                tileImg.setRotate(event.getRotation());
            }

            tileImg.setFitHeight(50);
            tileImg.setPreserveRatio(true);

            Platform.runLater(()->{
                myBoard.add(tileImg, event.getX(), event.getY());
            });

        }


    }


    @Override
    public void updateHand(HandEvent event){
        tileRotation = 0;
        tileImage.setRotate(0);
        System.out.println("CACCAPUPU " + event.getId());

        if(event.getId() == 158) {
            tileImage.setImage(tilePlaceholder);
            tileImage.setOpacity(0.5);
        }
        else{
            Image tile = new Image(getClass().getResourceAsStream("/GUI/Tiles/tile ("+ event.getId() +").jpg"));
            tileImage.setImage(tile);
            tileImage.setOpacity(1);
        }

//        Platform.runLater(()->{
//            playerClient.showGame(printer);
//        });

    }

    @Override
    public void updateGameboard(GameBoardEvent event) {

    }

    @Override
    public void updateCoveredTilesSet(CoveredTileSetEvent event) {
    }

    @Override
    public void updateUncoveredTilesSet(UncoverdTileSetEvent event){

        if(discardedMap.containsKey(event.getId())) {
            Platform.runLater(()->{
                uncoveredTiles.getChildren().remove(discardedMap.get(event.getId()));
                discardedMap.remove(event.getId());
            });

        }
        else{
            Image tile = new Image(getClass().getResourceAsStream("/GUI/Tiles/tile ("+ event.getId() +").jpg"));
            ImageView image = new ImageView(tile);
            image.setFitWidth(100);
            image.setPreserveRatio(true);
            image.setSmooth(true);

            VBox imageBox = new VBox(image);
            imageBox.setPadding(new Insets(10));

            discardedMap.put(event.getId(), imageBox);

            Platform.runLater(()->{
                discardedTiles.add(event.getId());
                uncoveredTiles.getChildren().add(imageBox);
            });


            image.setOnMouseClicked(mouseEvent -> {
                inputQueue.add("PickTile "+ discardedTiles.indexOf(event.getId()));
            });
        }


//        Platform.runLater(()->{
//            playerClient.showGame(printer);
//        });

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
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");

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


                if(level.equals("Tutorial")){
                    myGameLv = 1;
//                    lv1PlayerboardBuilder();
                }

                else {
                    myGameLv = 2;
//                    lv2PlayerboardBuilder();
                }
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

        BorderPane lobbyRoot = new  BorderPane(MainBox);
        lobbyRoot.setPadding(new Insets(10));

        lobbyRoot.prefHeightProperty().bind(primaryStage.widthProperty());
        lobbyRoot.prefWidthProperty().bind(primaryStage.widthProperty());


        Platform.runLater(() -> {
            contentRoot.getChildren().setAll(lobbyRoot);
        });

        printer.setLobby(primaryScene);

        playerClient.showGame(printer);

    }



    @Override
    public void showLobbyGame(GameLobbyEvent event){

        Label GameNameLabel = new Label("Game: " + myGameName);
        GameNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");

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


        if(myGameLv == 1){
            playerBoardImg = new Image(getClass().getResourceAsStream("/GUI/Boards/shipboard-lv1.jpg"));
            gameBoardImg = new Image(getClass().getResourceAsStream("/GUI/Boards/gameboard-lv1.png"));
        }
        else{
            playerBoardImg = new Image(getClass().getResourceAsStream("/GUI/Boards/shipboard-lv2.jpg"));
            gameBoardImg = new Image(getClass().getResourceAsStream("/GUI/Boards/gameboard-lv2.png"));
        }

        ImageView playerBoard = new ImageView(playerBoardImg);
        playerBoard.setPreserveRatio(true);
        playerBoard.setSmooth(true);
        playerBoard.setFitWidth(400);
        playerBoard.maxWidth(300);


        VBox mainBox = new VBox(10, GameNameLabel, players);
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

        VBox imageBox = new VBox(playerBoard, Buttons);

        StackPane gameLobbyRoot = new StackPane(mainBox, imageBox);

        gameLobbyRoot.setPadding(new Insets(10));
        gameLobbyRoot.setAlignment(Pos.TOP_CENTER);

        gameLobbyRoot.prefWidthProperty().bind(primaryStage.widthProperty());
        gameLobbyRoot.prefHeightProperty().bind(primaryStage.heightProperty());

        playerBoard.setTranslateY(200);
        Buttons.setTranslateY(250);

        Platform.runLater(() ->{
            contentRoot.getChildren().setAll(gameLobbyRoot);
        });

        Platform.runLater(() ->{
            printer.setGameLobby(primaryScene);
            playerClient.showGame(printer);
        });


        //idplayer li salvo in un arraylist
        //player->playerstate.showGame
    }

    @Override
    public void rewardsChanged(RewardsEvent event) {

    }

    public void buildingScene(){
        Label GameNameLabel = new Label("Game: " + myGameName);
        GameNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");

        ImageView playerBoard = new ImageView(playerBoardImg);
        playerBoard.setPreserveRatio(true);
        playerBoard.setSmooth(true);
        playerBoard.setFitWidth(500);

        ImageView gameBoard = new ImageView(gameBoardImg);
        gameBoard.setPreserveRatio(true);
        gameBoard.setSmooth(true);
        gameBoard.setFitWidth(400);


        ImageView clockwiseArrow = new ImageView(new Image(getClass().getResourceAsStream("/GUI/rotate arrow clockwise.png")));
        clockwiseArrow.setPreserveRatio(true);
        clockwiseArrow.setFitHeight(100);
        clockwiseArrow.setFitWidth(50);
        clockwiseArrow.setSmooth(false);
        clockwiseArrow.setOnMouseClicked(event -> {
            tileRotation += 90;
            if(tileRotation == 360)
                tileRotation = 0;
            tileImage.setRotate(tileRotation);
        });


        ImageView counterclockwiseArrow = new ImageView(new Image(getClass().getResourceAsStream("/GUI/rotate arrow counterclockwise.png")));
        counterclockwiseArrow.setPreserveRatio(true);
        counterclockwiseArrow.setFitHeight(100);
        counterclockwiseArrow.setFitWidth(50);
        counterclockwiseArrow.setSmooth(false);
        counterclockwiseArrow.setOnMouseClicked(event -> {
            tileRotation -= 90;
            if(tileRotation == -90)
                tileRotation = 270;
            tileImage.setRotate(tileRotation);
        });



//        if(tileImage.getImage() == null) {
//            tileImage.setImage(tilePlaceholder);
//            tileImage.setOpacity(0.5);
//        }

        Button pickTile = new Button("Pick Tile");
        Button insertTile = new Button("Insert Tile");
        Button discardTile = new Button("Discard");

        pickTile.setOnAction(e -> {
            inputQueue.add("PickTile -1");
        });
        discardTile.setOnAction(e -> {
            inputQueue.add("Discard");
        });


        HBox tileBox =  new HBox(5, counterclockwiseArrow, tileImage, clockwiseArrow);
        VBox Buttons = new VBox(15, tileBox, pickTile, insertTile, discardTile);
        HBox buildKit = new HBox(20, myBoard, Buttons);
//        buildBox.prefWidthProperty().bind(primaryStage.widthProperty());
//        buildBox.prefHeightProperty().bind(primaryStage.heightProperty());


        VBox boardsBox = new VBox(10, GameNameLabel, buildKit);
        boardsBox.setMaxWidth(800);
        boardsBox.setAlignment(Pos.TOP_CENTER);
        boardsBox.prefWidthProperty().bind(primaryStage.widthProperty());
        boardsBox.prefHeightProperty().bind(primaryStage.heightProperty());
        boardsBox.setPadding(new Insets(100));


        ScrollPane uncoveredBox = new ScrollPane(uncoveredTiles);
        uncoveredBox.setFitToWidth(true);
        uncoveredBox.setPrefWidth(365);

        uncoveredBox.setOpacity(0.4);

        HBox mainBox = new HBox(10, uncoveredBox, boardsBox);
        mainBox.setPadding(new Insets(50));

        StackPane buildingRoot = new StackPane(mainBox);
        buildingRoot.prefWidthProperty().bind(primaryStage.widthProperty());
        buildingRoot.prefHeightProperty().bind(primaryStage.heightProperty());

        Platform.runLater(() ->{
            contentRoot.getChildren().setAll(buildingRoot);
        });

        printer.setBuildingScene(primaryScene);


    }

    @Override
    public void phaseChanged(@NotNull PhaseEvent event) {
        playerClient.setPlayerState(event.getStateClient());
        playerClient.showGame(printer);
        //player.setstate(event.getpahse)
        //plary.state.showGUI
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
        sceneSetup();

        Label titleLabel = new Label("GALAXY TRUCKERS");
        titleLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");

        Button startButton = new Button("Start!");
        startButton.setStyle("-fx-font-size: 20px;");

        VBox TitleScreenBox = new VBox(10, titleLabel, startButton);
        TitleScreenBox.setAlignment(Pos.CENTER);
        TitleScreenBox.setPadding(new Insets(20));
        //TitleScreenBox.setMaxWidth(400);
        StackPane titleRoot = new StackPane(TitleScreenBox);
        TitleScreenBox.prefWidthProperty().bind(primaryStage.widthProperty());
        TitleScreenBox.prefHeightProperty().bind(primaryStage.heightProperty());

        contentRoot.getChildren().setAll(titleRoot);


        printer.setTitleScreen(primaryScene);
        printer.printTitleScreen();

        startButton.setOnAction(e -> {
            inputQueue.add("Lobby");
            playerClient.setPlayerState(new LobbyClient());
        });
    }

    private void sceneSetup(){
        primaryRoot = new StackPane();
        contentRoot = new Pane();
        primaryScene = new Scene(primaryRoot, 800, 600);

        //tile setup
        tilePlaceholder = new Image(getClass().getResourceAsStream("/GUI/Tiles/Space void.jpg"));
        tileImage.setImage(tilePlaceholder);
        tileImage.setOpacity(0.5);

        //background setup
        Media media = new Media(getClass().getResource("/GUI/magenta-nebula-moewalls-com.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(true);

        MediaView background = new MediaView(mediaPlayer);
        background.setPreserveRatio(false);

        background.fitHeightProperty().bind(primaryStage.heightProperty());
        background.fitWidthProperty().bind(primaryStage.widthProperty());


        primaryRoot.getChildren().addAll(background, contentRoot);
        primaryScene.setRoot(primaryRoot);
    }

    private void lv2PlayerboardBuilder(){

        for(int i=0; i< 9; i++)
            for(int j=0; j< 10; j++) {
                ImageView emptyCell = new ImageView(tilePlaceholder);
                emptyCell.setPreserveRatio(true);
                emptyCell.setFitWidth(50);
                int finalJ = j;
                int finalI = i;
                if ((i == 4 && (j == 5 || j == 7)) || (i == 5 && (j >= 4 && j <= 8)) || ((i == 6 || i == 7) && j>=3) || (i == 8 && (j>=3 && j != 6))){
                    Platform.runLater(()->{
                        myBoard.add(emptyCell, finalJ, finalI);
                    });
                }
                emptyCell.setOpacity(0.5);
                emptyCell.setOnMouseClicked(e -> {
                    inputQueue.add("InsertTile "+ finalJ +" "+ finalI +" "+ tileRotation);
                });
                emptyCell.setOnMouseEntered(e -> {
                    emptyCell.setOpacity(1);
                });
                emptyCell.setOnMouseExited(e -> {
                    emptyCell.setOpacity(0.5);
                });
            }
    }


    private void lv1PlayerboardBuilder(){
        for(int i=0; i< 9; i++)
            for(int j=0; j< 10; j++) {
                ImageView emptyCell = new ImageView(tilePlaceholder);
                emptyCell.setPreserveRatio(true);
                emptyCell.setFitWidth(50);
                int finalJ = j;
                int finalI = i;
                if ((i == 4 && (j == 6)) || (i == 5 && (j >= 5 && j <= 7)) || ((i == 6 || i == 7) && (j>=4 && j<= 8)) || (i == 8 && (j>=4 && j != 6 && j!=9))){

                    Platform.runLater(()->{
                        myBoard.add(emptyCell, finalJ, finalI);
                    });
                }
                emptyCell.setOpacity(0.5);
                emptyCell.setOnMouseClicked(e -> {
                    inputQueue.add("InsertTile "+ finalJ +" "+ finalI +" "+ tileRotation);
                });
                emptyCell.setOnMouseEntered(e -> {
                    emptyCell.setOpacity(1);
                });
                emptyCell.setOnMouseExited(e -> {
                    emptyCell.setOpacity(0.5);
                });
            }
    }

    //printplayerboard(){..}

}
