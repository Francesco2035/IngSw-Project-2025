package org.example.galaxy_trucker.View.GUI;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The GuiOut class is responsible for managing and displaying various scenes in the application.
 * It serves as an intermediary between the stored scenes and the primary application stage and
 * allows transitioning between different views such as title screen, lobby, game lobby, building
 * screen, and others.
 */
public class GuiOut {

    private GuiRoot gui;
    private Scene TitleScene;
    private Scene LobbyScene;
    private Scene GameLobbyScene;
    private Scene buildingScene;
    private Scene validityScene;
    private Scene addCrewScene;
    private Scene flightScene;
    private Stage primaryStage;


    /**
     *
     */
    public GuiOut(GuiRoot root){
        gui = root;
    }


    public GuiRoot getRoot(){
        return gui;
    }

    /**
     *
     */
    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    /**
     *
     */
    public void setTitleScreen(Scene titleScene) {
        this.TitleScene=titleScene;
    }

    /**
     *
     */
    public void printTitleScreen(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Galaxy Truckers");
            primaryStage.setScene(TitleScene);
            primaryStage.show();
        });
    }


    public void setLobby(Scene lobbyScene){
        this.LobbyScene=lobbyScene;
    }

    /**
     * Displays the "Lobby" screen in the application.
     *
     * This method ensures the user interface changes occur on the JavaFX Application thread
     * by using Platform.runLater. It updates the title of the primary stage to "Lobby",
     * sets the scene to the LobbyScene, and makes the stage visible.
     */
    public void printLobby(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Lobby");
            primaryStage.setScene(LobbyScene);
            primaryStage.show();
        });
    }


    /**
     * Sets the scene for the "Game Lobby" in the application.
     *
     * @param gameLobbyScene the Scene to be set as the "Game Lobby".
     */
    public void setGameLobby(Scene gameLobbyScene){
        this.GameLobbyScene = gameLobbyScene;
    }

    /**
     * Displays the "Game Lobby" screen in the application.
     * <br>
     * This method sets the title of the primary stage to "Game Lobby",
     * assigns the GameLobbyScene to the stage, and makes it visible.
     * The execution of UI updates is ensured on the JavaFX Application thread
     * by using Platform.runLater to manage thread safety with the JavaFX framework.
     */
    public void printGameLobby(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Game Lobby");
            primaryStage.setScene(GameLobbyScene);
            primaryStage.show();
        });
    }


    /**
     * Sets the scene for the "Building Scene" in the application.
     *
     * @param buildingScene the Scene to be set as the "Building Scene".
     */
    public void setBuildingScene(Scene buildingScene){
        this.buildingScene=buildingScene;
    }



    /**
     * Displays the "Building Screen" scene in the application.
     *
     * This method ensures that JavaFX UI updates are executed on the JavaFX Application thread
     * by utilizing the `Platform.runLater` utility. It sets the title of the primary stage to
     * "Building Screen", assigns the `buildingScene` to the stage, and makes the stage visible.
     */
    public void printBuildingScreen(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Building Screen");
            primaryStage.setScene(buildingScene);
            primaryStage.show();
        });
    }


    public void setAddCrewScreen(Scene addCrewScene){
        this.addCrewScene=addCrewScene;
    }


    /**
     * Displays the "Add Crew" screen in the application.
     *
     * This method ensures that the user interface updates occur on the JavaFX Application thread
     * by using the `Platform.runLater` utility. It updates the title of the primary stage to
     * "Add Crew", sets the scene of the stage to `addCrewScene`, and makes the stage visible.
     */
    public void printAddCrewScreen(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Add Crew");
            primaryStage.setScene(addCrewScene);
            primaryStage.show();
        });
    }

    /**
     * Sets the Check Validity Screen scene for the GUI.
     *
     * @param scene the Scene to be set as the Check Validity Screen.
     */
    public void setCheckValidityScreen(Scene scene){
        this.validityScene= scene;
    }


    /**
     * Displays the "Check Validity" screen in the application.
     * This method sets the title of the primary stage to "Check Validity",
     * assigns the validityScene to the stage, and makes it visible.
     * The execution of UI changes is ensured on the JavaFX Application thread
     * by using Platform.runLater.
     */
    public void printCheckValidityScreen(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Check Validity");
            primaryStage.setScene(validityScene);
            primaryStage.show();
        });
    }



    /**
     *
     */
    public void setFlightScreen(Scene scene){
        this.flightScene= scene;
    }


    /**
     * Displays the "Flight" screen in the application.
     *
     * This method ensures that JavaFX UI updates occur on the JavaFX Application thread
     * by wrapping the updates inside a `Platform.runLater` call. It sets the title of the
     * primary stage to "Flight", assigns the `flightScene` to the stage, and makes the stage visible.
     */
    public void printFlightScreen(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Flight");
            primaryStage.setScene(flightScene);
            primaryStage.show();
        });
    }

}
