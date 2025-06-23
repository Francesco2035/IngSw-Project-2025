package org.example.galaxy_trucker.View.GUI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.galaxy_trucker.Controller.Messages.*;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.*;
import org.example.galaxy_trucker.Controller.Messages.LobbyEvent;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.ClientModel.States.LobbyClient;
import org.example.galaxy_trucker.View.ClientModel.States.LoginClient;
import org.example.galaxy_trucker.View.View;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GuiRoot implements View {

    private Thread guiThread;
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

    private ListView<String> readyPlayers;
    private boolean amIReady;
    private boolean amIBuilding;


    private ArrayList<Integer> discardedTiles;
    private HashMap<Integer, VBox> discardedMap;
    private Image gameBoardImg;
    private GridPane myBoard;
    private Image tilePlaceholder;
    private ImageView tileImage;
    private TilePane uncoveredTiles;
    private int tileRotation;
    private HashMap<String, GridPane> othersBoards;
    private boolean checkvalidity;
    private VBox hourglassBox;
    private ImageView buffer1, buffer2;
    private Stage gameBoardStage;

    private Image cardBack;
    private HashMap<String, ImageView> playerRockets;
    private HashMap<String, Integer> playerPositions;
    private HashMap<Integer, IntegerPair> coords;
    private Image brownAlien, purpleAlien, crewMate;
    private Pane rocketsPane;
    private ListView<String> log;
    private double totAtk;
    private int totSpeed;
    private int totCredits;
    private int totEnergy;
    private int totDamages;
    private int totHumans;

    private boolean killing;
    private HBox phaseButtons;
    private ArrayList<IntegerPair> cmdCoords;
    private boolean tilesClickable;
    private Label prompt;
    private ArrayList<IntegerPair> excludedTiles;
    private ImageView curCard;
    private Image curCardImg;
    private boolean batteryClickable;
    private IntegerPair curCargoCoords;
    private int curCargoIndex;
    private ImageView curCargoImg;
    private HashMap<IntegerPair, ArrayList<Goods>> storageCompartments;
    private int rewardsLeft;
    private int nPlanets;
    private boolean handlingCargo;
    private boolean theft;
    private ArrayList<Goods> rewards;
    private ArrayList<ImageView> selectedImages;
    private LoginClient loginClient;



    public void setStage(Stage primaryStage){
        printer.setStage(primaryStage);
        this.primaryStage = primaryStage;
    }


    public GuiRoot(LoginClient loginClient){
        printer = new GuiOut(this);
        this.loginClient = loginClient;

        brownAlien = new Image(getClass().getResourceAsStream("/GUI/Boards/addons/alien-brown.png"));
        purpleAlien = new Image(getClass().getResourceAsStream("/GUI/Boards/addons/alien-purple.png"));
        crewMate = new Image(getClass().getResourceAsStream("/GUI/Boards/addons/among-us-white.png"));

        guiThread = new Thread(() -> GuiMain.launchApp(this));
        guiThread.start();
    }

    private void sceneSetup(){
        primaryRoot = new StackPane();
        contentRoot = new Pane();
        primaryScene = new Scene(primaryRoot, 800, 600);

        tileImage = new ImageView();
        tileImage.setImage(null);
        tileImage.setFitWidth(100);
        tileImage.setPreserveRatio(true);
        uncoveredTiles = new TilePane();
        amIBuilding = true;
        checkvalidity = false;
        hourglassBox = new VBox();
        buffer1 = new ImageView();
        buffer2 = new ImageView();

        coords = new HashMap<>();
        othersBoards = new HashMap<>();
        playerRockets = new HashMap<>();
        playerPositions = new HashMap<>();
        rocketsPane = new Pane();

        myBoard = new GridPane();
        playerClient = new PlayerClient();
        discardedTiles = new ArrayList<>();
        discardedMap = new HashMap<>();

        curCard =  new ImageView();
        curCard.setImage(null);
        curCardImg = null;
        killing = false;
        phaseButtons = new HBox(20);
        cmdCoords = new ArrayList<>();
        tilesClickable = false;
        excludedTiles = new ArrayList<>();
        curCargoImg = new ImageView();
        curCargoImg.setImage(null);
        storageCompartments = new HashMap<>();
        rewardsLeft = 0;
        handlingCargo = false;
        theft = false;
        rewards = null;
        batteryClickable = false;
        selectedImages = new ArrayList<>();

        readyPlayers = new ListView<>();
        log = new ListView<>();
        prompt = new Label();


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
        boolean stackTile;
        IntegerPair pair = null;

        StackPane tileStack;
        Pane crewPane = new Pane();
        ImageView tileImg = new ImageView();
        tileImg.setFitWidth(70);

        tileImage.setImage(tilePlaceholder);
        tileImage.setOpacity(0.5);

        boolean exists = false;
        for (IntegerPair p : excludedTiles) {
            if (p.getFirst() == event.getX() && p.getSecond() == event.getY()) {
                exists = true;
                pair = p;
                break;
            }
        }

        if(event.isBrownAlien() || event.isPurpleAlien() || event.getHumans() > 0 || event.getCargo() != null || event.getBatteries() > 0){
            stackTile = true;
            if (!exists)
                excludedTiles.add(new IntegerPair(event.getX(), event.getY()));
        }
        else {
            stackTile = false;
            if(exists){
                excludedTiles.remove(pair);
            }
        }


        if (event.getId() == 158) {
            tileImg.setImage(null);
        } else if (event.getId() == 157) {
            tileImg.setImage(tilePlaceholder);
            tileImg.setOpacity(0.5);
            if (amIBuilding) {
                tileImg.setOnMouseClicked(e -> {
                    inputQueue.add("InsertTile " + event.getX() + " " + event.getY() + " " + tileRotation);
                });
                tileImg.setOnMouseEntered(e -> {
                    tileImg.setOpacity(1);
                });
                tileImg.setOnMouseExited(e -> {
                    tileImg.setOpacity(0.5);
                });
            }
        } else {
            tileImg.setImage(new Image(getClass().getResourceAsStream("/GUI/Tiles/tile (" + event.getId() + ").jpg")));
            tileImg.setRotate(event.getRotation());
            tileImg.setOpacity(1);

            setColors(myName, event.getId());

            if (event.isBrownAlien() || event.isPurpleAlien() || event.getHumans() > 0) {

                tileImg.setFitWidth(70);
                ImageView crewImg = new ImageView();
                crewImg.setFitWidth(40);
                crewImg.setPreserveRatio(true);
                crewImg.setOnMouseClicked(e -> {
                    if (killing) {
                        IntegerPair coord = new IntegerPair(event.getX(), event.getY());
                        if (crewImg.getOpacity() == 1) {
                            cmdCoords.add(coord);
                            crewImg.setOpacity(0.5);
                            selectedImages.add(crewImg);
                        }
                    }
                });

                if (event.isBrownAlien()) {
                    crewImg.setImage(brownAlien);
                    crewPane.getChildren().add(crewImg);
                    excludedTiles.add(new IntegerPair(event.getX(), event.getY()));
                } else if (event.isPurpleAlien()){
                    crewImg.setImage(purpleAlien);
                    crewPane.getChildren().add(crewImg);
                    excludedTiles.add(new IntegerPair(event.getX(), event.getY()));
                } else if (event.getHumans() > 0) {

                    HBox humans = new HBox(2);

                    for (int i = 0; i < event.getHumans(); i++) {
                        ImageView crew = new ImageView(crewMate);
                        crew.setFitWidth(25);
                        crew.setPreserveRatio(true);
                        crew.setOnMouseClicked(e -> {
                            if (killing) {
                                IntegerPair coord = new IntegerPair(event.getX(), event.getY());
                                if (crew.getOpacity() == 1) {
                                    cmdCoords.add(coord);
                                    crew.setOpacity(0.5);
                                    selectedImages.add(crew);
                                }
//                                else{
//                                    cmdCoords.remove(coord);
//                                    crew.setOpacity(1);
//                                }
                            }
                        });
                        humans.getChildren().add(crew);

                    }
                    crewPane.getChildren().add(humans);
                }
            }
            else if (event.getCargo() != null) {
                excludedTiles.add(new IntegerPair(event.getX(), event.getY()));

                ImageView cargoImg = new ImageView(new Image(getClass().getResourceAsStream("/GUI/cargo/cargo.png")));
                cargoImg.setFitWidth(45);
                cargoImg.setPreserveRatio(true);
                crewPane.getChildren().setAll(cargoImg);

                cargoImg.setOnMouseClicked(e -> {
                    if(checkvalidity){
                        inputQueue.add("RemoveTile " + event.getX() + " " + event.getY());
                    }
                    else if(rewardsLeft > 0 && curCargoImg.getImage() != null && curCargoCoords == null) {
                        inputQueue.add("GetReward " + event.getX() + " " + event.getY() + " " + curCargoIndex);
                        rewardsLeft--;
                        if (rewardsLeft == 0) {
                            handleCargo();
                        }
                    } else {
                        Platform.runLater(() -> {
                            Stage cargoStage = new Stage();
                            if(event.getCargo().size() > 0)
                                cargoStage.setTitle("Select Cargo");
                            else
                                cargoStage.setTitle("Storage empty");

                            int i = 0;
                            HBox cargobox = new HBox(30);
                            Button cancelButton = goBackButtonMaker(cargoStage);
                            cancelButton.setText("Back");

                            for (Goods g : event.getCargo()) {
                                ImageView cargo = new ImageView(new Image(getClass().getResourceAsStream("/GUI/cargo/cargo" + g.getValue() + ".png")));
                                cargo.setFitWidth(50);
                                cargo.setPreserveRatio(true);
                                int finalI = i;
                                cargo.setOnMouseClicked(ev -> {
                                    if (handlingCargo) {
                                        if (curCargoCoords == null) {
                                            curCargoCoords = new IntegerPair(event.getX(), event.getY());
                                            curCargoIndex = finalI;
                                            curCargoImg.setImage(new Image(getClass().getResourceAsStream("/GUI/cargo/cargo" + event.getCargo().get(finalI).getValue() + ".png")));
                                            cargoStage.close();
                                        } else {
                                            Stage confirmStage = new Stage();
                                            confirmStage.setTitle("Switching Cargo");

                                            Label quitLabel = new Label("Do you want to switch your current cargo with this one?");
                                            quitLabel.setStyle("-fx-font-size: 15px");

                                            Button confirmButton = new Button("Switch");
                                            Button goBackButton = goBackButtonMaker(confirmStage);

                                            HBox buttons = new HBox(50, goBackButton, confirmButton);
                                            confirmButton.setOnAction(click -> {
                                                inputQueue.add("Switch " + curCargoCoords.getFirst() + " " + curCargoCoords.getSecond() + " " + curCargoIndex + " " + event.getX() + " " + event.getY() + " " + finalI);
                                                curCargoCoords = null;
                                                curCargoImg.setImage(null);
                                                confirmStage.close();
                                                cargoStage.close();
                                            });

                                            buttons.setAlignment(Pos.CENTER);
                                            buttons.setPadding(new Insets(5));

                                            VBox quitBox = new VBox(3, quitLabel, buttons);
                                            quitBox.setAlignment(Pos.CENTER);

                                            Scene newGameScene = new Scene(quitBox, 300, 80);
                                            confirmStage.setScene(newGameScene);
                                            confirmStage.initOwner(cargoStage);
                                            confirmStage.initModality(Modality.WINDOW_MODAL);

                                            confirmStage.show();
                                        }
                                    }
                                    else if(theft){
                                        inputQueue.add("Theft " + event.getX() + " " + event.getY() + " " + finalI);
                                        cargoStage.close();
                                    }
                                });
                                i++;
                                cargobox.getChildren().add(cargo);
                            }

                            VBox viewCargoBox = new VBox(5, cargobox, cancelButton);
                            viewCargoBox.setAlignment(Pos.CENTER);

                            Scene cargoScene = new Scene(viewCargoBox, 400, 200);
                            cargoStage.setScene(cargoScene);
                            cargoStage.initOwner(primaryStage);
                            cargoStage.initModality(Modality.WINDOW_MODAL);
                            cargoStage.show();
                        });

                    }
                });

            }
            else if(event.getBatteries() > 0){
                HBox batteries = new HBox(2);
                batteries.setPadding(new Insets(10));
                for (int i = 0; i < event.getBatteries(); i++) {
                    ImageView battery = new ImageView(new Image(getClass().getResourceAsStream("/GUI/battery.png")));
                    battery.setFitHeight(50);
                    tileImg.setOpacity(0.8);
                    battery.setPreserveRatio(true);
                    battery.setOnMouseClicked(e -> {
                        if(checkvalidity){
                            inputQueue.add("RemoveTile " + event.getX() + " " + event.getY());
                        }
                        else if (batteryClickable) {
                            IntegerPair coord = new IntegerPair(event.getX(), event.getY());
                            if (battery.getOpacity() == 1) {
                                cmdCoords.add(coord);
                                battery.setOpacity(0.5);
                                selectedImages.add(battery);
                            }
//                                else{
//                                    cmdCoords.remove(coord);
//                                    crew.setOpacity(1);
//                                }
                        }
                    });
                    batteries.getChildren().add(battery);

                }
                crewPane.getChildren().add(batteries);
            }
        }

        tileStack = new StackPane(tileImg, crewPane);
        tileImg.setPreserveRatio(true);

        boolean finalStackTile = stackTile;
        Platform.runLater(() -> {

            ArrayList<Node> nodes = new ArrayList<>(myBoard.getChildren());
            for (Node node : nodes) {
                if (node != null && GridPane.getRowIndex(node) == event.getX() && GridPane.getColumnIndex(node) == event.getY())
                    myBoard.getChildren().remove(node);
            }


            if (event.getX() == 3 && event.getY() == 8) {
                if (event.getId() == 158) {

                    if (buffer2.getOpacity() == 1) {
                        buffer1.setOnMouseClicked(e -> {
                            inputQueue.add("FromBuffer 0");
                        });
                        buffer1.setImage(buffer2.getImage());
                        buffer1.setOpacity(1);
                        buffer1.setOnMouseEntered(null);
                        buffer1.setOnMouseExited(null);


                        buffer2.setImage(tilePlaceholder);
                        buffer2.setOpacity(0.5);
                        buffer2.setOnMouseEntered(e -> {
                            buffer2.setOpacity(1);
                        });
                        buffer2.setOnMouseExited(e -> {
                            buffer2.setOpacity(0.5);
                        });
                        buffer2.setOnMouseClicked(e -> {
                            inputQueue.add("ToBuffer 1");
                        });

                    } else {
                        buffer1.setImage(tilePlaceholder);
                        buffer1.setOpacity(0.5);
                        buffer1.setOnMouseEntered(e -> {
                            buffer1.setOpacity(1);
                        });
                        buffer1.setOnMouseExited(e -> {
                            buffer1.setOpacity(0.5);
                        });
                        buffer1.setOnMouseClicked(e -> {
                            inputQueue.add("ToBuffer 0");
                        });
                    }
                } else {
                    buffer1.setImage(tileImg.getImage());
                    buffer1.setOpacity(1);
                    buffer1.setOnMouseEntered(null);
                    buffer1.setOnMouseExited(null);
                    buffer1.setOnMouseClicked(e -> {
                        inputQueue.add("FromBuffer 0");
                    });
                }

            } else if (event.getX() == 3 && event.getY() == 9) {
                if (event.getId() == 158) {
                    buffer2.setImage(tilePlaceholder);
                    buffer2.setOpacity(0.5);
                    buffer2.setOnMouseEntered(e -> {
                        buffer2.setOpacity(1);
                    });
                    buffer2.setOnMouseExited(e -> {
                        buffer2.setOpacity(0.5);
                    });
                    buffer2.setOnMouseClicked(e -> {
                        inputQueue.add("ToBuffer 1");
                    });
                } else {
                    buffer2.setImage(tileImg.getImage());
                    buffer2.setOpacity(1);
                    buffer2.setOnMouseEntered(null);
                    buffer2.setOnMouseExited(null);
                    buffer2.setOnMouseClicked(e -> {
                        inputQueue.add("FromBuffer 1");
                    });
                }
            } else if (!finalStackTile)
                myBoard.add(tileImg, event.getY(), event.getX());
            else
                myBoard.add(tileStack, event.getY(), event.getX());
        });

    }



    @Override
    public void updateOthersPB(PlayerTileEvent event) {
        if(!othersBoards.containsKey(event.getPlayerName()))
            othersBoards.put(event.getPlayerName(), new GridPane());

        ImageView tile = new ImageView();
        tile.setFitWidth(40);
        tile.setPreserveRatio(true);

        if(event.getId() == 157) {
            tile.setImage(tilePlaceholder);
            tile.setOpacity(0.5);
        }
        else if(event.getId() < 157){
            setColors(event.getPlayerName(), event.getId());
            tile.setImage(new Image(getClass().getResourceAsStream("/GUI/Tiles/tile ("+ event.getId() +").jpg")));
            tile.setRotate(event.getRotation());
        }

        Platform.runLater(()->{
            ArrayList<Node> nodes = new ArrayList<>(othersBoards.get(event.getPlayerName()).getChildren());
            for (Node node : nodes){
                if(node != null && GridPane.getRowIndex(node) == event.getX() && GridPane.getColumnIndex(node) == event.getY())
                    othersBoards.get(event.getPlayerName()).getChildren().remove(node);
            }

            othersBoards.get(event.getPlayerName()).add(tile, event.getY(), event.getX());
        });
    }



    @Override
    public void updateHand(HandEvent event) {
        Platform.runLater(() -> {
            tileRotation = 0;
            if (event.getId() == 158) {
                tileImage.setImage(tilePlaceholder);
                tileImage.setOpacity(0.5);
                tileImage.setRotate(tileRotation);
            } else {
                Image tile = new Image(getClass().getResourceAsStream("/GUI/Tiles/tile (" + event.getId() + ").jpg"));
                tileImage.setImage(tile);
                tileImage.setOpacity(1);
                tileImage.setRotate(tileRotation);
            }
        });

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
                discardedTiles.remove(event.getId());
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

    }

    @Override
    public void showDeck(DeckEvent event){

        Platform.runLater(() -> {
            Stage deckStage = new Stage();
            deckStage.setTitle("Spying Deck");

            HBox cards = new HBox(20);
            Button okButton = goBackButtonMaker(deckStage);
            okButton.setText("Ok");

            for(Integer i : event.getIds()){

                ImageView cardImg = new ImageView();

                if(i >= 29 && i <= 35){
                    cardImg.setImage(new Image(getClass().getResourceAsStream("/GUI/cards/card(openSpace).jpg")));
                }
                else if(i == 38 || i == 39){
                    cardImg.setImage(new Image(getClass().getResourceAsStream("/GUI/cards/card(stardust).jpg")));
                }
                else
                    cardImg.setImage(new Image(getClass().getResourceAsStream("/GUI/cards/card("+ i +").jpg")));

                cardImg.setFitWidth(150);
                cardImg.setPreserveRatio(true);
                cards.getChildren().add(cardImg);
            }

            VBox deckBox = new VBox(20, cards, okButton);
            deckBox.setAlignment(Pos.CENTER);

            deckBox.prefHeightProperty().bind(deckStage.heightProperty());
            deckBox.prefWidthProperty().bind(deckStage.widthProperty());

            Scene deckScene = new Scene(deckBox, 490, 300);
            deckStage.setScene(deckScene);
            deckStage.initOwner(primaryStage);
            deckStage.setResizable(false);
            deckStage.initModality(Modality.WINDOW_MODAL);
            deckStage.show();


        });
    }

    @Override
    public void showCard(CardEvent event){
        //abandoned station -> accept o decline -> getreward x, coords
        //planets -> chooseplaet x

        if(event.getId() >= 29 && event.getId() <= 35){
            curCardImg = new Image(getClass().getResourceAsStream("/GUI/cards/card(openSpace).jpg"));
        }
        else if(event.getId() == 38 || event.getId() == 39){
            curCardImg = new Image(getClass().getResourceAsStream("/GUI/cards/card(stardust).jpg"));
        }
        else {
            curCardImg = new Image(getClass().getResourceAsStream("/GUI/cards/card(" + event.getId() + ").jpg"));

            switch(event.getId()){
                case 21, 27:{
                    nPlanets = 4;
                    break;
                }
                case 22, 24, 25, 28:{ nPlanets = 3;
                    break;
                }
                case 23, 26:{
                    nPlanets = 2;
                    break;
                }
            }
        }
    }


    @Override
    public void disconnect() {
        //quando si accorge di esseree disconnesso ->reconnect / exit / ChangeConnection
        Platform.runLater(()->{
            Stage disconnectStage = new Stage();
            disconnectStage.setTitle("Connection lost");

            Label disconnected = new Label("Connection lost.\nWhat do you want to do?");
            disconnected.setStyle("-fx-font-size: 15px");

            Button reconnect = new Button("Reconnect");
            Button exit = new Button("Exit");
            Button change = new Button("Chenge connection");

            HBox buttons = new HBox(30, reconnect, change, exit);
            reconnect.setOnAction(click -> {
                inputQueue.add("Reconnect");
            });
            change.setOnAction(click -> {
                inputQueue.add("ChangeConnection");
            });
            exit.setOnAction(click -> {
                inputQueue.add("Exit");
            });

            buttons.setAlignment(Pos.CENTER);
            buttons.setPadding(new Insets(5));

            VBox quitBox = new VBox(3, disconnected, buttons);
            quitBox.setAlignment(Pos.CENTER);

            Scene disconnectedScene = new Scene(quitBox, 300, 80);
            disconnectStage.setScene(disconnectedScene);
            disconnectStage.initOwner(primaryStage);
            disconnectStage.initModality(Modality.WINDOW_MODAL);
            disconnectStage.show();
        });

    }

    @Override
    public void connect() throws IOException {}

    @Override
    public void setGameboard(int lv){
        Platform.runLater(()->{
            myGameLv = lv;

            if (myGameLv == 1) {
//            playerBoardImg = new Image(getClass().getResourceAsStream("/GUI/Boards/shipboard-lv1.jpg"));
                gameBoardImg = new Image(getClass().getResourceAsStream("/GUI/Boards/gameboard-lv1.png"));
                cardBack = new Image(getClass().getResourceAsStream("/GUI/cards/lv1-card.jpg"));
                Lv1GameboardSetup();
            } else {
//            playerBoardImg = new Image(getClass().getResourceAsStream("/GUI/Boards/shipboard-lv2.jpg"));
                gameBoardImg = new Image(getClass().getResourceAsStream("/GUI/Boards/gameboard-lv2.png"));
                cardBack = new Image(getClass().getResourceAsStream("/GUI/cards/lv2-card.jpg"));
                Lv2GameboardSetup();
            }

            gameBoardStage = setGameBoardStage();
        });
    }


    public void checkValidityScene(){
        boolean clickable;

        amIBuilding = false;
        checkvalidity = true;

        prompt.setText("Remove Invalid Tiles!");
        prompt.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");

        ImageView txtBackground = new ImageView(new  Image(getClass().getResourceAsStream("/GUI/all_belt.png")));
        txtBackground.setFitWidth(600);
        txtBackground.setFitHeight(100);

        StackPane textPanel = new StackPane(txtBackground, prompt);

        Button finishButton = new Button("Done");

        if(myGameLv == 2)
            finishButton.setOnAction(e -> {
            Stage ChoosePositionStage = new Stage();
            ChoosePositionStage.setTitle("Select Position");

            ComboBox<String> position = new ComboBox<>();
            position.getItems().addAll("1", "2", "3", "4");
            position.setPromptText("Position");

            Button confirmButton = new Button("Confirm");
            Button goBackButton = goBackButtonMaker(ChoosePositionStage);

            confirmButton.disableProperty().bind(
                    position.valueProperty().isNull()
            );

            confirmButton.setOnAction(ev -> {
                ChoosePositionStage.close();
                inputQueue.add("FinishBuilding "+ position.getValue());

            });


            HBox Buttons = new HBox(50, confirmButton, goBackButton);

            Buttons.setAlignment(Pos.CENTER);
            Buttons.setPadding(new Insets(15));

            VBox formBox = new VBox(10,
                    new Label("Select Yout Starting Position:"), position,
                    Buttons
            );
            formBox.setPadding(new Insets(15));
            formBox.setAlignment(Pos.CENTER);

            Scene scene = new Scene(formBox, 300, 120);
            ChoosePositionStage.setScene(scene);
            ChoosePositionStage.initOwner(primaryStage); // Blocca interazioni con la finestra principale
            ChoosePositionStage.initModality(Modality.WINDOW_MODAL);

            ChoosePositionStage.show();

        });
        else
            finishButton.setOnAction(e -> {inputQueue.add("FinishBuilding");});

        AtomicInteger x = new AtomicInteger();
        AtomicInteger y = new AtomicInteger();
        ArrayList<Node> childrenCopy = new ArrayList<>(myBoard.getChildren());

        for(Node node : childrenCopy){
            clickable = true;

            x.set(GridPane.getRowIndex(node));
            y.set(GridPane.getColumnIndex(node));

            int X = x.get();
            int Y = y.get();

            for(IntegerPair p : excludedTiles){
                if(X == p.getFirst() && Y == p.getSecond()){
                    clickable = false;
                }
            }
            if(clickable){
                ImageView tile = (ImageView) node;

                ImageView newTile =new ImageView(tile.getImage());
                tile.setFitWidth(70);
                tile.setPreserveRatio(true);

                tile.setOnMouseClicked(e->{
                    x.set(GridPane.getColumnIndex(node));
                    y.set(GridPane.getRowIndex(node));
                    inputQueue.add("RemoveTile " + X + " " + Y);
                });

                if(newTile.getImage() != null && newTile.getImage().equals(tilePlaceholder))
                    newTile.setOpacity(0.5);
            }



//            Platform.runLater(()->{
//                myBoard.getChildren().remove(node);
//                myBoard.add(newTile,  GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
//            });

        }

        Platform.runLater(()->{

            VBox othersBox = new VBox(20);
            for(String id : othersBoards.keySet()){
                othersBox.getChildren().add(othersBoards.get(id));
            }

            HBox mainBox = new HBox(new VBox(100, myBoard, textPanel), finishButton, othersBox);
            mainBox.setPadding(new Insets(150));
            mainBox.setAlignment(Pos.CENTER);
            Pane root = new Pane(mainBox);

            mainBox.prefWidthProperty().bind(primaryStage.widthProperty());
            mainBox.prefHeightProperty().bind(primaryStage.heightProperty());

            contentRoot.getChildren().setAll(root);
            printer.setCheckValidityScreen(primaryScene);
        });


    }


    public void choosePosition(){
        Platform.runLater(()->{
            prompt.setText("Your ship is valid, now choose a position!");
        });

    }


    public void AddCrewScene(){
        amIBuilding = false;
        checkvalidity = false;
        boolean clickable;

        prompt = new Label();
        prompt.setText("Populate Your Ship!");
        prompt.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");


        ImageView txtBackground = new ImageView(new  Image(getClass().getResourceAsStream("/GUI/all_belt.png")));
        txtBackground.setFitWidth(600);
        txtBackground.setFitHeight(100);

        StackPane textPanel = new StackPane(txtBackground, prompt);

        AtomicReference<String> cmdType = new AtomicReference<>(null);

        ImageView brown = new ImageView(brownAlien);
        brown.setFitWidth(60);
        brown.setPreserveRatio(true);

        brown.setOnMouseClicked(e->{
            cmdType.set("AddBrownAlien");
        });

        ImageView purple = new ImageView(purpleAlien);
        purple.setFitWidth(60);
        purple.setPreserveRatio(true);
        purple.setOnMouseClicked(e->{
            cmdType.set("AddPurpleAlien");
        });

        ImageView human = new ImageView(crewMate);
        human.setFitWidth(60);
        human.setPreserveRatio(true);
        human.setOnMouseClicked(e->{
            cmdType.set("AddCrew");
        });


        VBox crewBox;
        if(myGameLv == 2)
            crewBox = new VBox(human,purple,brown);
        else
            crewBox = new VBox(human);

        AtomicInteger x = new AtomicInteger();
        AtomicInteger y = new AtomicInteger();
        ArrayList<Node> childrenCopy = new ArrayList<>(myBoard.getChildren());

        for(Node node : childrenCopy){

            clickable = true;

            x.set(GridPane.getRowIndex(node));
            y.set(GridPane.getColumnIndex(node));

            int X = x.get();
            int Y = y.get();

            for(IntegerPair p : excludedTiles){
                if(X == p.getFirst() && Y == p.getSecond()){
                    clickable = false;
                }
            }

            if(clickable){
                ImageView tile = (ImageView) node;

                ImageView newTile =new ImageView(tile.getImage());
                tile.setFitWidth(70);
                tile.setPreserveRatio(true);

                tile.setOnMouseClicked(e->{
                    if(cmdType.get() != null){
                        x.set(GridPane.getColumnIndex(node));
                        y.set(GridPane.getRowIndex(node));
                        inputQueue.add(cmdType.get() + " " + y.get() + " " + x.get());
                    }
                });

                tile.setOnMouseEntered(null);
                tile.setOnMouseExited(null);

                if(newTile.getImage() != null && newTile.getImage().equals(tilePlaceholder))
                    newTile.setOpacity(0.5);
            }


//            Platform.runLater(()->{
//               myBoard.getChildren().remove(node);
//               myBoard.add(newTile,  GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
//            });

        }

        Platform.runLater(()->{
            HBox boardBox = new HBox(50, myBoard, crewBox);
//            boardBox.setAlignment(Pos.CENTER);
//            boardBox.setPadding(new Insets(50));

            VBox othersBox = new VBox(20);
            for(String id : othersBoards.keySet()){
                othersBox.getChildren().add(othersBoards.get(id));
            }

            HBox mainBox = new HBox(new VBox(100, boardBox, textPanel), othersBox);
            mainBox.setPadding(new Insets(150));
            mainBox.setAlignment(Pos.CENTER);
            Pane root = new Pane(mainBox);

            mainBox.prefWidthProperty().bind(primaryStage.widthProperty());
            mainBox.prefHeightProperty().bind(primaryStage.heightProperty());

            contentRoot.getChildren().setAll(root);
            printer.setAddCrewScreen(primaryScene);
        });


    }

    @Override
    public void showLobby(LobbyEvent event){
        //mi arriva il lobby event ogni volta che qualcuno crea/si aggiunge ad un game

        Label titleLabel = new Label("GALAXY TRUCKERS");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");


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



        Button newGame = new Button("New Game");
        newGame.setStyle("-fx-font-size: 14px;");

        Button reconnect = new Button("Reconnect");
        reconnect.setStyle("-fx-font-size: 14px;");

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

            ComboBox<String> playerBox = new ComboBox<>();
            playerBox.getItems().addAll("1", "2", "3", "4");
            playerBox.setPromptText("Max");

            Button confirmButton = new Button("Confirm");
            Button goBackButton = goBackButtonMaker(newGameStage);

            confirmButton.disableProperty().bind(
                    usernameField.textProperty().isEmpty()
                            .or(gameNameField.textProperty().isEmpty())
                            .or(levelBox.valueProperty().isNull())
                            .or(playerBox.valueProperty().isNull())
            );

            confirmButton.setOnAction(ev -> {

                String level = levelBox.getValue();


                if(level.equals("Tutorial")){
                    myGameLv = 1;
                }

                else {
                    myGameLv = 2;
                }
                newGameStage.close();

                myName = usernameField.getText();
                myGameName = gameNameField.getText();

                //create myname mygamename lv maxplayers
                inputQueue.add("Create");
                inputQueue.add(myName);
                inputQueue.add(myGameName);
                inputQueue.add(String.valueOf(myGameLv));
                inputQueue.add(playerBox.getValue());
            });

            HBox Buttons = new HBox(50, confirmButton, goBackButton);

            Buttons.setAlignment(Pos.CENTER);
            Buttons.setPadding(new Insets(15));

            VBox formBox = new VBox(10,
                    new Label("Player Name:"), usernameField,
                    new Label("Game Name:"), gameNameField,
                    new Label("Game Mode:"), levelBox,
                    new Label("Max Number of Players:"), playerBox,
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

        reconnect.setOnAction(e->{
            Stage reconnectStage = new Stage();
            reconnectStage.setTitle("Connection lost");

            Label txt = new Label("Insert token: ");
            txt.setStyle("-fx-font-size: 15px");

            TextField tokenField = new TextField();
            tokenField.setPromptText("abcd1234");

            Button done = new Button("Reconnect");
            Button exit = goBackButtonMaker(reconnectStage);


            done.setOnAction(click -> {
                inputQueue.add("Reconnect");
                inputQueue.add(tokenField.getText());
                reconnectStage.close();
            });

            done.disableProperty().bind(tokenField.textProperty().isEmpty());

            HBox buttons = new HBox(30, done, exit);
            buttons.setAlignment(Pos.CENTER);
            buttons.setPadding(new Insets(5));

            VBox reconBox = new VBox(3, txt, tokenField, buttons);
            reconBox.setAlignment(Pos.CENTER);

            Scene rconnectScene = new Scene(reconBox, 300, 100);
            reconnectStage.setScene(rconnectScene);
            reconnectStage.initOwner(primaryStage);
            reconnectStage.initModality(Modality.WINDOW_MODAL);
            reconnectStage.show();
        });

        VBox MainBox = new VBox(10, titleLabel, gamesList, newGame, reconnect);
        gamesList.setMaxWidth(800);
        MainBox.setAlignment(Pos.CENTER);
//        MainBox.setPadding(new Insets(20));
//        MainBox.setMaxWidth(600);

        StackPane lobbyRoot = new  StackPane(MainBox);
//        lobbyRoot.setPadding(new Insets(10));

        MainBox.prefWidthProperty().bind(primaryStage.widthProperty());
        MainBox.prefHeightProperty().bind(primaryStage.heightProperty());

        Platform.runLater(() -> {
            contentRoot.getChildren().setAll(lobbyRoot);
        });

        printer.setLobby(primaryScene);

        playerClient.showGame(printer);

    }



    @Override
    public void showLobbyGame(GameLobbyEvent event){

        Platform.runLater(()->{
            readyPlayers.getItems().clear();

            String s;
            for(int i=0; i< event.getPlayers().size(); i++){
                if(event.getPlayers().get(i).equals(myName)) {
                    s = event.getPlayers().get(i) + " (You)";
                    amIReady = event.getReady().get(i);
                }
                else s= event.getPlayers().get(i);

                if(event.getReady().get(i))
                    readyPlayers.getItems().add(s + " --> Ready!");
                else readyPlayers.getItems().add(s+ " --> Not Ready");
            }
        });

    }


    public void LobbyGameScreen() {
        Platform.runLater(() -> {
            Label GameNameLabel = new Label("Game: " + myGameName);
            GameNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");


            Button quitButton = new Button("Quit");
            Button readyButton = new Button();
            Button debugShip1 = new Button("Debug Ship 1");
            Button debugShip2 = new Button("Debug Ship 2");

            if (amIReady)
                readyButton.setText("Not Ready");
            else readyButton.setText("Ready!");

            readyButton.setOnAction(e -> {
                if (amIReady)
                    inputQueue.add("Ready false");
                else inputQueue.add("Ready true");
            });

            debugShip1.setOnAction(e -> {
                inputQueue.add("DebugShip 0");
            });

            debugShip2.setOnAction(e -> {
                inputQueue.add("DebugShip 1");
            });

            quitButton.setOnAction(e -> {
                Stage confirmStage = new Stage();
                confirmStage.setTitle("Quitting");

                Label quitLabel = new Label("Are You Sure?");
                quitLabel.setStyle("-fx-font-size: 15px");

                Button confirmButton = new Button("Pretty Sure");
                Button goBackButton = goBackButtonMaker(confirmStage);

                confirmButton.setOnAction(E -> {
                    inputQueue.add("Quit");
                });

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


            HBox Buttons;
            if (myGameLv == 2)
                Buttons = new HBox(50, quitButton, debugShip1, debugShip2, readyButton);
            else
                Buttons = new HBox(50, quitButton, readyButton);

            Buttons.setPadding(new Insets(15));
            Buttons.setAlignment(Pos.CENTER);

            StackPane stack = new StackPane(myBoard, Buttons);

            VBox mainBox = new VBox(10, GameNameLabel, readyPlayers, stack);
            readyPlayers.setMaxWidth(800);
            readyPlayers.setMaxHeight(200);
            mainBox.setAlignment(Pos.TOP_CENTER);
            mainBox.setPadding(new Insets(10));

            StackPane gameLobbyRoot = new StackPane(mainBox);

            gameLobbyRoot.setPadding(new Insets(10));
            gameLobbyRoot.setAlignment(Pos.TOP_CENTER);

            gameLobbyRoot.prefWidthProperty().bind(primaryStage.widthProperty());
            gameLobbyRoot.prefHeightProperty().bind(primaryStage.heightProperty());

            Buttons.setTranslateY(250);


            contentRoot.getChildren().setAll(gameLobbyRoot);
            printer.setGameLobby(primaryScene);
        });

        //idplayer li salvo in un arraylist
        //player->playerstate.showGame

    }


    private Stage setGameBoardStage() {
        double scaleRatio = 0.85;
        int imgX, imgY;
//            final int[] i = {0};
        if(myGameLv == 2){
            imgX = 1055;
            imgY = 639;
        }
        else{
            imgX = 985;
            imgY = 546;
        }
        Stage gbStage = new Stage();
        Platform.runLater(() -> {
            gbStage.setTitle("Game Board");
//            gbStage.initStyle(StageStyle.TRANSPARENT);

            StackPane root = new StackPane();

            ImageView board = new ImageView(gameBoardImg);
            board.setFitHeight(imgY * scaleRatio);
            board.setPreserveRatio(true);

            Rectangle background = new Rectangle(imgX * scaleRatio+ 5, imgY * scaleRatio + 5);
            if(myGameLv == 2)
                background.setFill(Color.rgb(86, 40, 110));
            else
                background.setFill(Color.rgb(6, 55, 105));

//            ImageView pedine = new ImageView(new  Image(getClass().getResourceAsStream("/GUI/among-us-white.png")));
//            pedine.setFitHeight(40);
//            pedine.setPreserveRatio(true);
//            pedine.setLayoutX((coords.get(i[0]).getFirst() - 20) * scaleRatio);
//            pedine.setLayoutY((coords.get(i[0]).getSecond() - 25) * scaleRatio);
//            pedLayer.getChildren().add(pedine);
//
//            pedine.setOnMouseClicked(e -> {
//                i[0]++;
//                pedine.setLayoutX((coords.get(i[0]).getFirst() - 20) * scaleRatio);
//                pedine.setLayoutY((coords.get(i[0]).getSecond() - 20) * scaleRatio);
//
//            });

            root.getChildren().addAll(background, board, rocketsPane);


            Scene scene = new Scene(root, imgX * scaleRatio, imgY * scaleRatio);
            gbStage.setScene(scene);
            gbStage.initModality(Modality.WINDOW_MODAL);
            gbStage.setResizable(false);

        });

        return gbStage;
    }


    @Override
    public void updateGameboard(GameBoardEvent event){
        Platform.runLater(()->{
            if(!playerPositions.containsKey(event.getPlayerID())){
                playerPositions.put(event.getPlayerID(), event.getPosition());
                rocketsPane.getChildren().add(playerRockets.get(event.getPlayerID()));
            }

            if(event.getPosition() == -1){
                playerPositions.remove(event.getPlayerID());
                rocketsPane.getChildren().remove(playerRockets.get(event.getPlayerID()));
            }
            else{
                playerRockets.get(event.getPlayerID()).setLayoutX((coords.get(event.getPosition()).getFirst() - 20) * 0.85);
                playerRockets.get(event.getPlayerID()).setLayoutY((coords.get(event.getPosition()).getSecond() - 20) * 0.85);
            }

        });

    }


    private void rewardsScreen(){

        ImageView rewardsBg = new ImageView(new Image(getClass().getResourceAsStream("/GUI/box_ship_slots_left.png")));
        rewardsBg.setFitHeight(70);
        rewardsBg.setPreserveRatio(true);
        rewardsBg.setRotate(90);
        VBox rewardsBox = new VBox(20);
        rewardsBox.setPadding(new Insets(20));

        Button discard = new Button("Discard");
        discard.setOnAction(e -> {
//            inputQueue.add("DiscardCargo " + );
        });

        int i = 0;

        for(Goods g : rewards){
            ImageView box = new ImageView(new Image(getClass().getResourceAsStream("/GUI/cargo/cargo"+ g.getValue() +".png")));
            box.setFitHeight(50);
            box.setPreserveRatio(true);
            int finalI = i;
            box.setOnMouseClicked(e ->{
                curCargoIndex = finalI;
                box.setOpacity(0.5);
            });
            rewardsBox.getChildren().add(box);
            i++;
        }

        Pane boxes  = new Pane(rewardsBox);
        StackPane stack = new StackPane(rewardsBg, boxes);

        Platform.runLater(() ->{
            phaseButtons.getChildren().setAll(stack, discard);
        });


        AtomicInteger x = new AtomicInteger();
        AtomicInteger y = new AtomicInteger();
        ArrayList<Node> childrenCopy = new ArrayList<>(myBoard.getChildren());
        boolean clickable;

        for(Node node : childrenCopy){
            clickable = true;

            x.set(GridPane.getRowIndex(node));
            y.set(GridPane.getColumnIndex(node));

            int X = x.get();
            int Y = y.get();

            for(IntegerPair p : excludedTiles){
                if(X == p.getFirst() && Y == p.getSecond()){
                    clickable = false;
                }
            }

            if(clickable){
                ImageView tile = (ImageView) node;

                tile.setOnMouseClicked(e->{
                    inputQueue.add("GetReward " +  X + " " + Y + " " + curCargoIndex);
                    rewardsLeft--;
                    if(rewardsLeft == 0)
                        handleCargo();
                });

            }

        }
    }

    @Override
    public void rewardsChanged(RewardsEvent event){
        curCargoImg.setImage(null);
        curCargoCoords = null;
        rewards = event.getRewards();
        rewardsLeft = event.getRewards().size();
        handleCargo();
    }

    public void buildingScene(){
        Label GameNameLabel = new Label("Game: " + myGameName);

        GameNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #fbcc18;");

        ImageView gameBoard = new ImageView(gameBoardImg);
        gameBoard.setPreserveRatio(true);
        gameBoard.setSmooth(true);
        gameBoard.setFitWidth(400);

        ImageView hourglass = new ImageView();
        hourglass.setImage((new Image(getClass().getResourceAsStream("/GUI/hourglass.png"))));
        hourglass.setPreserveRatio(true);
        hourglass.setSmooth(true);
        hourglass.setFitHeight(70);
        hourglass.setOnMouseClicked(event -> {
            inputQueue.add("Hourglass");
        });
        hourglassBox.getChildren().setAll(hourglass);


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


        ImageView background = new ImageView(new  Image(getClass().getResourceAsStream("/GUI/all_belt.png")));
        background.setFitWidth(200);
        background.setFitHeight(110);

        buffer1.setImage(tilePlaceholder);
        buffer1.setFitHeight(70);
        buffer1.setPreserveRatio(true);
        buffer1.setOpacity(0.5);
        buffer1.setOnMouseEntered(event -> {
            buffer1.setOpacity(1);
        });
        buffer1.setOnMouseExited(event -> {
            buffer1.setOpacity(0.5);
        });
        buffer1.setOnMouseClicked(event -> {
            inputQueue.add("ToBuffer 0");
        });

        buffer2.setImage(tilePlaceholder);
        buffer2.setFitHeight(70);
        buffer2.setPreserveRatio(true);
        buffer2.setOpacity(0.5);
        buffer2.setOnMouseEntered(event -> {
            buffer2.setOpacity(1);
        });
        buffer2.setOnMouseExited(event -> {
            buffer2.setOpacity(0.5);
        });
        buffer2.setOnMouseClicked(event -> {
            inputQueue.add("ToBuffer 1");
        });

        ImageView deck1 = new ImageView(cardBack);
        ImageView deck2 = new ImageView(cardBack);
        ImageView deck3 = new ImageView(cardBack);

        deck1.setFitHeight(100);
        deck1.setPreserveRatio(true);
        deck1.setOnMouseClicked(event -> {inputQueue.add("SeeDeck 1");});

        deck2.setFitHeight(100);
        deck2.setPreserveRatio(true);
        deck2.setOnMouseClicked(event -> {inputQueue.add("SeeDeck 2");});

        deck3.setFitHeight(100);
        deck3.setPreserveRatio(true);
        deck3.setOnMouseClicked(event -> {inputQueue.add("SeeDeck 3");});

        HBox cards = new HBox(20, deck1, deck2, deck3);


        HBox bufferBox = new HBox( 15, buffer1, buffer2);
        bufferBox.setPadding(new Insets(20));
        Pane bufferPane = new Pane(bufferBox);
        StackPane buffer = new StackPane(background, bufferPane);


        Button pickTile = new Button("Pick Tile");
        Button board = new Button("Board");
        Button discardTile = new Button("Discard");
        Button finishButton = new Button("Done!");

        pickTile.setOnAction(e -> {
            inputQueue.add("PickTile -1");
        });
        discardTile.setOnAction(e -> {
            inputQueue.add("Discard");
        });

        board.setOnAction(e -> {
           gameBoardStage.show();
        });

        if(myGameLv == 2)
            finishButton.setOnAction(e -> {
            Stage ChoosePositionStage = new Stage();
            ChoosePositionStage.setTitle("Select Position");

            ComboBox<String> position = new ComboBox<>();
            position.getItems().addAll("1", "2", "3", "4");
            position.setPromptText("Position");

            Button confirmButton = new Button("Confirm");
            Button goBackButton = goBackButtonMaker(ChoosePositionStage);

            confirmButton.disableProperty().bind(
                    position.valueProperty().isNull()
            );

            confirmButton.setOnAction(ev -> {
                ChoosePositionStage.close();
                inputQueue.add("FinishBuilding "+ position.getValue());
            });


            HBox Buttons = new HBox(50, confirmButton, goBackButton);

            Buttons.setAlignment(Pos.CENTER);
            Buttons.setPadding(new Insets(15));

            VBox formBox = new VBox(10,
                    new Label("Select Yout Starting Position:"), position,
                    Buttons
            );
            formBox.setPadding(new Insets(15));
            formBox.setAlignment(Pos.CENTER);

            Scene scene = new Scene(formBox, 300, 120);
            ChoosePositionStage.setScene(scene);
            ChoosePositionStage.initOwner(primaryStage);
            ChoosePositionStage.initModality(Modality.WINDOW_MODAL);

            ChoosePositionStage.show();

        });
        else
            finishButton.setOnAction(e -> {inputQueue.add("FinishBuilding");});


        VBox others = new VBox(20);
        for(String PlayerName : othersBoards.keySet()){
            others.getChildren().add(new HBox(5, new Label(PlayerName), othersBoards.get(PlayerName)));
        }

        HBox tileBox =  new HBox(5, counterclockwiseArrow, tileImage, clockwiseArrow);
        VBox Buttons = new VBox(15, pickTile, board, discardTile, finishButton);
        HBox buildKit;
        if(myGameLv == 2)
            buildKit = new HBox(10, tileBox, Buttons, hourglassBox, buffer);
        else
            buildKit = new HBox(10, tileBox, Buttons, buffer);


        ScrollPane uncoveredBox = new ScrollPane(uncoveredTiles);
        uncoveredBox.setFitToWidth(true);
        uncoveredBox.setPrefWidth(500);

        uncoveredBox.setOpacity(0.4);

        Platform.runLater(() ->{
            HBox mainBox;
            if(myGameLv == 2)
                mainBox = new HBox(10, uncoveredBox, new VBox(10, cards, myBoard, buildKit), others);
            else
                mainBox = new HBox(10, uncoveredBox, new VBox(10, myBoard, buildKit), others);

            mainBox.setPadding(new Insets(50));

            StackPane buildingRoot = new StackPane(mainBox);
            buildingRoot.prefWidthProperty().bind(primaryStage.widthProperty());
            buildingRoot.prefHeightProperty().bind(primaryStage.heightProperty());


            contentRoot.getChildren().setAll(buildingRoot);
        });

        printer.setBuildingScene(primaryScene);
    }


    public void flightScene() {
        prompt.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");

        ImageView txtBackground = new ImageView(new Image(getClass().getResourceAsStream("/GUI/all_belt.png")));
        txtBackground.setFitWidth(600);
        txtBackground.setFitHeight(100);

        ImageView cannons = new ImageView(new Image(getClass().getResourceAsStream("/GUI/boardInfo/cannons.png")));
        cannons.setFitHeight(50);
        cannons.setPreserveRatio(true);
        Label nCannons = new Label("x"+ totAtk);
        nCannons.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");

        ImageView credits = new ImageView(new Image(getClass().getResourceAsStream("/GUI/boardInfo/credits.png")));
        credits.setFitHeight(50);
        credits.setPreserveRatio(true);
        Label nCredits = new Label("x"+ totCredits);
        nCredits.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");

        ImageView damages = new ImageView(new Image(getClass().getResourceAsStream("/GUI/boardInfo/damages.png")));
        damages.setFitHeight(50);
        damages.setPreserveRatio(true);
        Label nDamages = new Label("x"+ totDamages);
        nDamages.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");

        ImageView energy = new ImageView(new Image(getClass().getResourceAsStream("/GUI/boardInfo/energy.png")));
        energy.setFitHeight(50);
        energy.setPreserveRatio(true);
        Label nEnergy = new Label("x"+ totEnergy);
        nEnergy.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");

        ImageView engine = new ImageView(new Image(getClass().getResourceAsStream("/GUI/boardInfo/engine.png")));
        engine.setFitHeight(50);
        engine.setPreserveRatio(true);
        Label nEngines = new Label("x"+ totSpeed);
        nEngines.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");

        ImageView humans = new ImageView(new Image(getClass().getResourceAsStream("/GUI/boardInfo/humans.png")));
        humans.setFitHeight(50);
        humans.setPreserveRatio(true);
        Label nHumans = new Label("x"+ totHumans);
        nHumans.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");


        AtomicInteger x = new AtomicInteger();
        AtomicInteger y = new AtomicInteger();
        ArrayList<Node> childrenCopy = new ArrayList<>(myBoard.getChildren());
        boolean clickable;

        for (Node node : childrenCopy) {
            clickable = true;

            x.set(GridPane.getRowIndex(node));
            y.set(GridPane.getColumnIndex(node));

            int X = x.get();
            int Y = y.get();

            for (IntegerPair i : excludedTiles) {
                if (x.get() == i.getFirst() && y.get() == i.getSecond()) {
                    clickable = false;
                }
            }

            if (clickable) {
                ImageView tile = (ImageView) node;

                tile.setOnMouseClicked(e -> {
                    if (tilesClickable) {
                        System.out.println(X + " " + Y);
                        if (tile.getOpacity() == 1) {
                            cmdCoords.add(new IntegerPair(X, Y));
                            selectedImages.add(tile);
                            tile.setOpacity(0.5);
                        }
//                    else{
//                        cmdCoords.remove(new IntegerPair(event.getX(), event.getY()));
//                        tileImage.setOpacity(0.5);
//                    }
                    }
                });
            }
        }

        Platform.runLater(() -> {
            ImageView coveredCard = new ImageView(cardBack);
            coveredCard.setFitHeight(300);
            coveredCard.setPreserveRatio(true);
            curCard.setFitHeight(300);
            curCard.setPreserveRatio(true);
            StackPane textPanel = new StackPane(txtBackground, prompt);
            HBox cardBox = new HBox(20, coveredCard, curCard);

            VBox othersBox = new VBox(20);
            for (String id : othersBoards.keySet()) {
                othersBox.getChildren().add(othersBoards.get(id));
            }

            HBox stats = new HBox(20, new HBox(3,cannons, nCannons), new HBox(3,engine, nEngines), new HBox(3,energy, nEnergy), new HBox(3,humans, nHumans), new HBox(3,damages, nDamages), new HBox(3,credits, nCredits));
            ImageView gameboard = new ImageView(gameBoardImg);
            gameboard.setFitHeight(100);
            gameboard.setPreserveRatio(true);
            gameboard.setOnMouseClicked(e -> {
                gameBoardStage.show();
            });

            log.setMaxHeight(100);
            HBox mainBox = new HBox(new VBox(25, cardBox, log, new HBox(15, phaseButtons, gameboard)), new VBox(100,stats, myBoard, textPanel), othersBox);
            mainBox.setPadding(new Insets(15));
            mainBox.prefWidthProperty().bind(primaryStage.widthProperty());
            mainBox.prefHeightProperty().bind(primaryStage.heightProperty());
            Pane root = new Pane(mainBox);

            mainBox.prefWidthProperty().bind(primaryStage.widthProperty());
            mainBox.prefHeightProperty().bind(primaryStage.heightProperty());

            contentRoot.getChildren().setAll(root);
            printer.setFlightScreen(primaryScene);
        });
    }


    @Override
    public void phaseChanged(@NotNull PhaseEvent event) {
        playerClient.setPlayerState(event.getStateClient());
        playerClient.showGame(printer);

        if(event.getStateClient() == loginClient){
            goToFirstScene();
        }

        //player.setstate(event.getpahse)
        //plary.state.showGUI
    }


    @Override
    public void exceptionOccurred(ExceptionEvent exceptionEvent){

        Platform.runLater(() -> {

            for(IntegerPair p : cmdCoords){
                ImageView tile = getTile(p.getFirst(), p.getSecond());
                if(tile != null)
                    tile.setOpacity(1);
            }

            cmdCoords.clear();

            Stage exceptionStage = new Stage();
            exceptionStage.setTitle("Exception");

            Label errorLabel = new Label(exceptionEvent.getException());
            errorLabel.setStyle("-fx-font-size: 15px");

            Button okButton = goBackButtonMaker(exceptionStage);
            okButton.setText("Ok");

            VBox errorBox = new VBox(3, errorLabel, okButton);
            errorBox.setAlignment(Pos.CENTER);

            Scene errorScene = new Scene(errorBox, 300, 80);
            exceptionStage.setScene(errorScene);
            exceptionStage.initOwner(primaryStage);
            exceptionStage.initModality(Modality.WINDOW_MODAL);
            exceptionStage.show();
        });
    }



    @Override
    public void seeBoards() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void effectCard(LogEvent event) {
        //messaggio di cosa è successo
        Platform.runLater(()->{
            String oldText = prompt.getText();
//            prompt.setText(event.message());
            log.getItems().addFirst(event.message());

//            PauseTransition pause = new PauseTransition(Duration.seconds(5));
//            pause.setOnFinished(e -> prompt.setText(oldText));
//            pause.play();
        });

    }

    @Override
    public void updatePBInfo(PBInfoEvent event) {
        totAtk = event.getPlasmaDrillsPower();
        totEnergy = event.getEnergy();
        totSpeed = event.getEnginePower();
        totHumans = event.getNumHumans();
        totDamages = event.getDamage();
        totCredits = event.getCredits();
    }

    @Override
    public void updateHourglass(HourglassEvent event){
        ImageView hourglassImg = new ImageView();


        ProgressBar progressBar = new ProgressBar(1);
        progressBar.setPrefWidth(150);
        progressBar.setPrefHeight(10);

        AtomicInteger curSec = new AtomicInteger(600);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(100), e ->{
                    curSec.getAndDecrement();
                    double progress = (double) curSec.get() / 600;
                    progressBar.setProgress(progress);

                })
        );
        timeline.setCycleCount(600);

        Platform.runLater(()->{
            if(event.getStart()){
                hourglassImg.setImage((new Image(getClass().getResourceAsStream("/GUI/super-buu-hourglass.gif"))));
                hourglassImg.setOnMouseClicked(null);
                hourglassImg.setFitWidth(150);
                hourglassImg.setFitHeight(100);
                hourglassImg.setSmooth(true);
                hourglassBox.getChildren().setAll(hourglassImg,  progressBar);
                timeline.play();

            }
            else{
                hourglassImg.setImage((new Image(getClass().getResourceAsStream("/GUI/hourglass.png"))));
                hourglassImg.setFitHeight(70);
                hourglassImg.setPreserveRatio(true);
                hourglassImg.setOnMouseClicked(e->inputQueue.add("Hourglass"));
                hourglassBox.getChildren().setAll(hourglassImg);
            }
        });

    }

    @Override
    public void seeLog(){
    }

    @Override
    public void showOutcome(FinishGameEvent event) {
        Platform.runLater(()->{
            prompt.setText(event.message());
        });
    }

    @Override
    public void reconnect(ReconnectedEvent event) {

    }

    @Override
    public void Token(TokenEvent tokenEvent){
        Platform.runLater(()->{
            Stage stage = new Stage();
            stage.setTitle("Token");

            Label txt1 = new Label("This is your token:\n");
            txt1.setStyle("-fx-font-size: 15px");
            Label txt2 = new Label(tokenEvent.getToken());
            txt2.setStyle("-fx-font-size: 19px");
            Label txt3 = new Label("\nRemember this, you will need it for reconnection!");
            txt3.setStyle("-fx-font-size: 15px");
            Button ok = new Button("Ok");
            ok.setOnAction(e->{
                stage.close();
            });

            VBox txtBox = new VBox(3, txt1, txt2, txt3, ok);
            txtBox.setAlignment(Pos.CENTER);

            Scene scene = new Scene(txtBox, 350, 150);
            stage.setScene(scene);
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        });
    }


    private @NotNull Button joinButtonMaker(LobbyEvent joining) {
        Button joinButton = new Button("Join");
        joinButton.setOnAction(e -> {
            Stage InsertNameStage = new Stage();
            InsertNameStage.setTitle("Joining game: \"" + joining.getGameId() + "\"");

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
                //Join myname mygamename
                ///TODO: sistemare la join
                inputQueue.add("Create");
                inputQueue.add(myName);
                inputQueue.add(myGameName);
                inputQueue.add(String.valueOf(joining.getLv()));
                inputQueue.add(String.valueOf(joining.getMaxPlayers()));
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
        titleLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: #fbcc18;");

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


    private void Lv1GameboardSetup(){
        int[] coordsList = {263,124,348,92,442,82,534,85,629,94,717,126,797,180,842,280,791,371,713,422,625,453,527,465,435,463,341,449,254,419,170,361,127,259,181,168};

        IntegerPair temp = new IntegerPair();

        int j = 0;
        for(int i=0; i< coordsList.length; i++){
            if(i%2 == 0)
                temp.setFirst(coordsList[i]);
            else{
                temp.setSecond(coordsList[i]);
                coords.put(j, new IntegerPair(temp.getFirst(), temp.getSecond()));
                j++;
            }
        }
    }

    private void Lv2GameboardSetup(){
        int[] coordsList = {257,153,332,121,408,106,487,96,567,98,645,107,721,126,796,157,868,207,916,282,909,374,854,442,784,486,709,516,630,533,554,539,471,539,395,530,317,508,243,478,174,426,126,349,138,259,192,194};

        IntegerPair temp = new IntegerPair();

        int j = 0;
        for(int i=0; i< coordsList.length; i++){
            if(i%2 == 0)
                temp.setFirst(coordsList[i]);
            else{
                temp.setSecond(coordsList[i]);
                coords.put(j, new IntegerPair(temp.getFirst(), temp.getSecond()));
                j++;
            }
        }
    }

    private void setColors(String pl, int id){

        Platform.runLater(()->{
            ImageView img = new ImageView();
            img.setFitHeight(40);
            img.setPreserveRatio(true);

            if(!playerRockets.containsKey(pl)) {
                if (id == 153) {
                    img.setImage(new Image(getClass().getResourceAsStream("/GUI/Boards/addons/among-us-blue.png")));
                    playerRockets.put(pl, img);
                }
                if (id == 154) {
                    img.setImage(new Image(getClass().getResourceAsStream("/GUI/Boards/addons/among-us-green.png")));
                    playerRockets.put(pl, img);
                }
                if (id == 155) {
                    img.setImage(new Image(getClass().getResourceAsStream("/GUI/Boards/addons/among-us-red.png")));
                    playerRockets.put(pl, img);
                }
                if (id == 156) {
                    img.setImage(new Image(getClass().getResourceAsStream("/GUI/Boards/addons/among-us-yellow.png")));
                    playerRockets.put(pl, img);
                }
            }

        });
        System.out.println(pl +" "+ id);
    }

    public boolean isGameStarted(){
        if(curCardImg != null || !amIBuilding)
            return true;
        else return false;
    }


    private ImageView getTile(int col, int row){
        for (Node node : myBoard.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            Integer nodeRow = GridPane.getRowIndex(node);

            for(IntegerPair i : excludedTiles) {
                if (row == i.getFirst() && col == i.getSecond())
                    return null;
            }
            if (nodeCol == col && nodeRow == row) {
                return (ImageView) node;
            }
        }
        return null;
    }



    public void baseState(){
        Button ready = new Button("Ready!");
        Button quit = new Button("Quit");

        Platform.runLater(()->{
            curCard.setImage(null);
            prompt.setText("Picking next card when everyone is ready...");
            phaseButtons.getChildren().setAll(ready,quit);

            cmdCoords.clear();
            selectedImages.clear();
            tilesClickable = false;
            killing = false;
            theft = false;


            ready.setOnAction(e -> {
                inputQueue.add("Ready True");
            });

            quit.setOnAction(e -> {
                inputQueue.add("Quit");
            });
        });
    }


    public void killing(){
        AtomicReference<String> cmd = new AtomicReference<>("Kill");
        Button kill = new Button("Kill!");

        Platform.runLater(()->{
            curCard.setImage(curCardImg);
            killing = true;
            prompt.setText("Select crew mates to kill!");
            phaseButtons.getChildren().setAll(kill);

            kill.setOnAction(e ->{
                for(IntegerPair p : cmdCoords){
                    cmd.set(cmd + " " + p.getFirst() + " " + p.getSecond());
                }
                System.out.println(cmd.get());
                inputQueue.add(cmd.get());
                for(ImageView i : selectedImages){
                    i.setOpacity(1);
                }
            });
        });

    }



    public void defend(String command, String txt){
        AtomicReference<String> cmd = new AtomicReference<>(command);
        Button defend = new Button("Defend!");
        Button doNothing = new Button("Do Nothing");

        Platform.runLater(()->{
            curCard.setImage(curCardImg);
            batteryClickable = true;
            tilesClickable = true;
            prompt.setText(txt);
            phaseButtons.getChildren().setAll(defend, doNothing);

            doNothing.setOnAction(e ->{
                inputQueue.add(cmd + " DoNothing");
            });

            defend.setOnAction(e ->{
                for(IntegerPair p : cmdCoords){
                    cmd.set(cmd + " " + p.getFirst() + " " + p.getSecond());
                }

                System.out.println(cmd.get());

                inputQueue.add(cmd.get());

//                for(IntegerPair p : cmdCoords){
//                    System.out.println(p.getFirst() + " " + p.getSecond());
//                    ImageView tile = getTile(p.getSecond(), p.getFirst());
//                    tile.setOpacity(1);
//                }
                for(ImageView i : selectedImages){
                    i.setOpacity(1);
                }
                cmdCoords.clear();
                cmd.set(command);
            });
        });

    }


    public void consumingEnergy(){
        AtomicReference<String> cmd = new AtomicReference<>();
        Button done = new Button("Done!");

        Platform.runLater(()->{
            curCard.setImage(curCardImg);
            batteryClickable = true;
            prompt.setText("Select the batteries to consume!");
            phaseButtons.getChildren().setAll(done);
            cmd.set("ConsumeEnergy");

            done.setOnAction(e ->{
                for(IntegerPair p : cmdCoords){
                    cmd.set(cmd + " " + p.getFirst() + " " + p.getSecond());
                }

                System.out.println(cmd.get());
                inputQueue.add(cmd.get());
//                for(IntegerPair p : cmdCoords){
//                    ImageView tile = getTile(p.getSecond(), p.getFirst());
//                    tile.setOpacity(1);
//                }
                cmdCoords.clear();
                cmd.set("ConsumeEnergy");
                for(ImageView i : selectedImages){
                    i.setOpacity(1);
                }
            });
        });
    }



    public void giveTiles(String command, String txt){
        AtomicReference<String> cmd = new AtomicReference<>(command);
        Button done = new Button("Done!");
        Button doNothing = new Button("Do Nothing");

        Platform.runLater(()->{
            curCard.setImage(curCardImg);
            tilesClickable = true;
            prompt.setText(txt);
            phaseButtons.getChildren().setAll(done, doNothing);

            done.setOnAction(e ->{
                for(IntegerPair p : cmdCoords){
                    cmd.set(cmd + " " + p.getFirst() + " " + p.getSecond());
                }

                System.out.println(cmd.get());

                inputQueue.add(cmd.get());

//                for(IntegerPair p : cmdCoords){
//                    ImageView tile = getTile(p.getSecond(), p.getFirst());
//                    tile.setOpacity(1);
//                }
                for(ImageView i : selectedImages){
                    i.setOpacity(1);
                }
                cmdCoords.clear();
                cmd.set(command);
            });

            doNothing.setOnAction(e->{
                inputQueue.add(command + " DoNothing");
                for(ImageView i : selectedImages){
                    i.setOpacity(1);
                }
                cmdCoords.clear();
            });
        });
    }



    public void choosingPlanet(){
        Button choose = new Button("Select");
        Button doNothing = new Button("Do Nothing");

        ComboBox<String> planets = new ComboBox<>();
        planets.setPromptText("Planets");
        choose.disableProperty().bind(
                planets.valueProperty().isNull()
        );

        for(int i = 0; i <nPlanets; i++)
            planets.getItems().add("Planet "+ (i+1));

        Platform.runLater(()->{
            curCard.setImage(curCardImg);
            prompt.setText("Choose a planet to explore!");
            phaseButtons.getChildren().setAll(planets, choose, doNothing);

            doNothing.setOnAction(e ->{
                inputQueue.add("ChoosePlanet -1");
            });

            choose.setOnAction(e ->{
                switch(planets.getValue()){
                    case "Planet 1":{
                        inputQueue.add("ChoosePlanet 0");
                        break;
                    }
                    case "Planet 2":{
                        inputQueue.add("ChoosePlanet 1");
                        break;
                    }
                    case "Planet 3":{
                        inputQueue.add("ChoosePlanet 2");
                        break;
                    }
                    case "Planet 4":{
                        inputQueue.add("ChoosePlanet 3");
                        break;
                    }
                }
            });
        });

    }


    public void acceptState(){
        AtomicReference<String> cmd = new AtomicReference<>("ChoosePlanet");
        Button accept = new Button("Accept");
        Button decline = new Button("Decline");

        Platform.runLater(()->{
            curCard.setImage(curCardImg);
            prompt.setText("Do you want to visit it?");
            phaseButtons.getChildren().setAll(accept, decline);

            accept.setOnAction(e ->{
                inputQueue.add("Accept");
            });

            decline.setOnAction(e ->{
                inputQueue.add("Decline");
            });

        });

    }


    public void handleCargo() {
        Platform.runLater(() -> {
            curCard.setImage(curCardImg);
            handlingCargo = true;
            curCargoIndex = -1;
            curCargoCoords = null;
            curCargoImg.setFitHeight(50);
            curCargoImg.setPreserveRatio(true);
            Button finish = new Button("Finish");
            Button discard = new Button("Discard");
            Button unselect = new Button("Unselect");

            prompt.setText("Move your cargo as you like!");

            StackPane cargo = new StackPane();
            ImageView slot = new ImageView(new Image(getClass().getResourceAsStream("/GUI/cargoBg.png")));
            slot.setFitHeight(70);
            slot.setPreserveRatio(true);

            HBox rewardsBox = new HBox(20);
            rewardsBox.setPadding(new Insets(20));


            if (rewardsLeft > 0) {
                int i = 0;

                for (Goods g : rewards) {
                    ImageView box = new ImageView(new Image(getClass().getResourceAsStream("/GUI/cargo/cargo" + g.getValue() + ".png")));
                    box.setFitHeight(50);
                    box.setPreserveRatio(true);
                    int finalI = i;
                    box.setOnMouseClicked(e -> {
                        curCargoIndex = finalI;
                        curCargoCoords = null;
                        curCargoImg.setImage(box.getImage());
                    });
                    rewardsBox.getChildren().add(box);
                    i++;
                }

                AtomicInteger x = new AtomicInteger();
                AtomicInteger y = new AtomicInteger();
                ArrayList<Node> childrenCopy = new ArrayList<>(myBoard.getChildren());
                boolean clickable;

                for (Node node : childrenCopy) {
                    clickable = true;

                    x.set(GridPane.getRowIndex(node));
                    y.set(GridPane.getColumnIndex(node));

                    int X = x.get();
                    int Y = y.get();

                    for (IntegerPair p : excludedTiles) {
                        if (X == p.getFirst() && Y == p.getSecond()) {
                            clickable = false;
                        }
                    }

                    if (clickable) {
                        ImageView tile = (ImageView) node;

                        tile.setOnMouseClicked(e -> {
                            inputQueue.add("GetReward " + X + " " + Y + " " + curCargoIndex);
                            rewardsLeft--;
                            if (rewardsLeft == 0)
                                handleCargo();
                        });

                    }
                }
            }

            cargo.getChildren().setAll(slot, curCargoImg);
            phaseButtons.getChildren().setAll(new VBox(10, new HBox (10, cargo, unselect, discard, finish), rewardsBox));


            finish.setOnAction(e -> {
                inputQueue.add("FinishCargo");
                handlingCargo = false;
            });

            discard.setOnAction(e -> {
                if(curCargoCoords != null){
                    inputQueue.add("DiscardCargo " + curCargoCoords.getFirst() + " " + curCargoCoords.getSecond() + " " + curCargoIndex);
                    curCargoCoords = null;
                    curCargoImg.setImage(null);
                }
            });

            unselect.setOnAction(e -> {
                curCargoCoords = null;
                curCargoImg.setImage(null);
            });
        });
    }

    public void handleTheft(){
        Platform.runLater(() -> {
            curCard.setImage(curCardImg);
            theft = true;
            prompt.setText("Select cargo to give");
        });
    }

    public void waiting(){
        Platform.runLater(() -> {
            prompt.setText("Waiting for your turn...");
            curCard.setImage(curCardImg);
            phaseButtons.getChildren().clear();
            tilesClickable = false;
            killing = false;
            theft = false;
        });
    }


}