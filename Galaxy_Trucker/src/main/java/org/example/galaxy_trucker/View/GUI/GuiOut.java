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

    /**
     * Represents the root element of the application's graphical user interface (GUI).
     *
     * This variable holds an instance of {@code GuiRoot}, which provides the starting point
     * and main context for setting up and managing the various scenes and stages of the application's GUI.
     * The {@code GuiRoot} instance is critical for coordinating user interface components and handling
     * transitions between different scenes.
     */
    private GuiRoot gui;
    /**
     * Represents the JavaFX scene for the title screen in the application.
     *
     * This scene is used as the initial screen displayed to the user upon launching
     * the application. It can be updated using the setTitleScreen method and displayed
     * via the printTitleScreen method, ensuring proper handling of JavaFX threads.
     */
    private Scene TitleScene;
    /**
     * Represents the scene for the "Lobby" screen in the application.
     *
     * This Scene is used to display the lobby interface to the user, where
     * they can interact with the game's lobby settings. It is managed and
     * displayed by the GuiOut class, typically in the context of transitioning
     * between different application screens.
     *
     * The Scene can be assigned and updated using the {@code setLobby} method
     * of the GuiOut class and displayed using the {@code printLobby} method.
     */
    private Scene LobbyScene;
    /**
     * Represents the Scene object for the "Game Lobby" screen in the application.
     *
     * This variable is used to store and manage the JavaFX Scene instance
     * that corresponds to the "Game Lobby" interface within the application.
     * It can be set via appropriate methods and displayed on the primary stage
     * when needed.
     */
    private Scene GameLobbyScene;
    /**
     * Represents the scene for the "Building Screen" in the application's GUI.
     *
     * This Scene is primarily used to display and interact with the building-related
     * features within the application. It is managed and displayed through various
     * methods of the GuiOut class, which handles setting, updating, and rendering
     * this Scene in the primary JavaFX stage.
     */
    private Scene buildingScene;
    /**
     * Represents the scene used to display the "Check Validity" screen in the application.
     *
     * This field holds the JavaFX Scene object associated with the "Check Validity" screen.
     * It is used to configure and display the respective user interface when necessary.
     */
    private Scene validityScene;
    /**
     * The `addCrewScene` variable represents the JavaFX `Scene` associated with the "Add Crew" screen
     * in the graphical user interface of the application.
     *
     * It is utilized to configure and display the user interface components necessary
     * for the "Add Crew" functionality within the application. This scene is assigned
     * to the primary stage when the "Add Crew" screen is to be shown.
     *
     * The scene can be updated or accessed through corresponding methods in the class.
     */
    private Scene addCrewScene;
    /**
     * Represents the JavaFX Scene used for the "Flight" screen in the application.
     *
     * This variable stores the reference to the Scene displayed during the "Flight" phase
     * of the application. It is intended to be initialized and managed using methods
     * of the GuiOut class, such as setting or displaying the scene on the primary stage.
     */
    private Scene flightScene;
    /**
     * Represents the primary stage of the JavaFX application.
     *
     * This stage serves as the main window where various scenes of the application,
     * such as the title screen, lobby, game lobby, and other screens, are displayed.
     * The stage is managed using associated methods for setting and switching scenes,
     * ensuring thread-safe execution with JavaFX's application thread.
     */
    private Stage primaryStage;


    /**
     * Constructs a GuiOut instance and initializes it with the specified GuiRoot object.
     *
     * @param root the GuiRoot object that provides the root element for the application's GUI.
     */
    public GuiOut(GuiRoot root){
        gui = root;
    }


    /**
     * Retrieves the root GUI element associated with this instance.
     *
     * @return the root GUI element as a GuiRoot object.
     */
    public GuiRoot getRoot(){
        return gui;
    }

    /**
     * Sets the primary stage for the application.
     *
     * @param primaryStage the main JavaFX stage to be used by the application
     */
    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    /**
     * Sets the scene for the title screen in the application.
     *
     * @param titleScene the Scene to be set as the title screen.
     */
    public void setTitleScreen(Scene titleScene) {
        this.TitleScene=titleScene;
    }

    /**
     * Displays the title screen for the application.
     *
     * This method ensures that UI changes occur on the JavaFX Application thread
     * by utilizing the `Platform.runLater` method. It sets the title of the primary
     * stage to "Galaxy Truckers", assigns `TitleScene` to the primary stage, and makes
     * the stage visible to the user.
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
