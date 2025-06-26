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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.galaxy_trucker.ClientServer.Messages.*;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.*;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.ClientModel.States.LobbyClient;
import org.example.galaxy_trucker.View.ClientModel.States.LoginClient;
import org.example.galaxy_trucker.View.View;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.TileEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * The `GuiRoot` class is responsible for handling the graphical user interface (GUI)
 * and interaction logic for the game application. It manages the primary stage, scene
 * graph components, game state updates, and player interactions within the UI.
 *
 * The class interfaces with various game events (e.g., `ReconnectedEvent`, `TileEvent`,
 * `HandEvent`) to dynamically update the visuals and state of the game in response to
 * player actions or server events. It ensures thread-safe updates using the
 * JavaFX `Platform.runLater` method and provides interactive elements for players
 * to engage with the game.
 *
 * Key responsibilities of the `GuiRoot` class include:
 * - Setting up the JavaFX application window (`sceneSetup`).
 * - Managing reconnection processes and restoring player state (`reconnect`).
 * - Updating game boards, personal boards, or specific player-related UI elements (`updateBoard`, `updateOthersPB`).
 * - Handling tile interactions, including placement, removal, or tiling mechanics (`updateBoard`, `updateHand`, `updateUncoveredTilesSet`).
 * - Monitoring and processing various game events to reflect gameplay dynamics.
 *
 * Class fields:
 * - `guiThread`: The thread in which the GUI is executed.
 * - `primaryStage`: The primary stage for the JavaFX application.
 * - `inputQueue`: Queue for processing player inputs.
 * - `primaryRoot`, `contentRoot`, `primaryScene`: JavaFX scene components for rendering and layout structure.
 * - `playerClient`: The client handling communication with the server.
 * - `printer`: Utility responsible for logging and message display.
 * - `lobbyEvents`, `myGameName`, `myName`, `myGameLv`: Fields to manage lobby and game session information.
 * - `readyPlayers`, `amIReady`, `amIBuilding`: Fields tracking player readiness and game phases.
 * - `players`, `discardedTiles`, `discardedMap`: Structures for managing player and tile-related data.
 * - `gameBoardImg`, `myBoard`, `tilePlaceholder`, `tileImage`: UI elements for displaying game and player boards.
 * - Numerous other fields to handle UI components related to game mechanics, interactivity, animations, and events.
 *
 * Methods:
 * - `GuiRoot(LoginClient loginClient)`: Constructor initializing the GUI with the user's login client.
 * - `void setStage(Stage primaryStage)`: Sets the primary stage for the application.
 * - `void sceneSetup()`: Configures the JavaFX scene graph and initializes the application's opening view.
 * - `void reconnect(ReconnectedEvent event)`: Handles reconnection events, showing a success dialog and restoring game state.
 * - `void updateBoard(TileEvent event)`: Updates the game board UI to reflect changes based on a tile event.
 * - `void updateOthersPB(PlayerTileEvent event)`: Updates the personal boards of other players based on a player tile event.
 * - `void updateHand(HandEvent event)`: Updates the currently active tile in the player's hand.
 * - `void updateUncoveredTilesSet(UncoverdTileSetEvent event)`: Updates the UI for uncovered tiles, including managing discarded tiles and their interactions.
 *
 * The implementation ensures seamless and fluid user experiences, maintaining thread safety
 * by leveraging the JavaFX `Platform.runLater` method for any UI modifications. This class
 * provides the backbone for the visual and interactive components of the application.
 */
public class GuiRoot implements View {

    /**
     * Represents the main thread responsible for managing and updating the graphical user interface (GUI).
     * This thread is typically used to ensure that all GUI-related operations are performed
     * on the same thread to maintain thread safety and avoid concurrency issues.
     */
    private Thread guiThread;
    /**
     * A thread-safe blocking queue that holds strings to be processed as input.
     * This queue uses a First-In-First-Out (FIFO) ordering and provides methods
     * that block or timeout when retrieving or adding elements, ensuring safe
     * concurrent access in multi-threaded environments.
     */
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    /**
     * Represents the primary stage of the JavaFX application.
     * This is the main window where the application's user interface
     * is displayed. It serves as the entry point for displaying
     * scenes and managing the main application lifecycle.
     */
    private Stage primaryStage;

    /**
     * The root container for the primary user interface layout in a JavaFX application.
     * This object serves as the main container node for all other graphical components
     * and defines the structure of the application's visible scene.
     */
    private StackPane primaryRoot;
    /**
     * The root container for the main content of the user interface.
     * This variable typically acts as the primary parent node
     * for displaying content components or controls in the application layout.
     */
    private Pane contentRoot;
    /**
     * Represents the primary {@link Scene} of the application.
     * This variable is used to set and manage the main user interface elements
     * displayed within the application's primary stage.
     */
    private Scene primaryScene;


    /**
     * Represents the client responsible for handling player-related operations
     * such as managing player data or communicating with player services.
     */
    private PlayerClient playerClient;
    /**
     * A private instance variable representing the output handler for the graphical user interface.
     * The printer is responsible for handling and managing the output display within the GUI context.
     */
    private GuiOut printer;
    /**
     * A list of lobby event objects used to store events occurring within a lobby.
     * This variable holds instances of the LobbyEvent class, which represent individual events.
     */
    private ArrayList<LobbyEvent> lobbyEvents = new ArrayList<>();

    /**
     * Represents the name of the game.
     * This variable is used to store the title or identifier of the game.
     */
    private String myGameName;
    /**
     * Represents the name of an individual or entity.
     * This variable is used to store the name as a String value.
     */
    private String myName;
    /**
     * Represents the current level of the game for a player.
     * This variable tracks the player's progress within the game.
     * It is stored as an integer and typically increases as the player advances.
     */
    private int myGameLv;

    /**
     * A ListView containing Label instances that represent the players
     * who are ready. This UI component is typically used to display
     * a list of players that have indicated they are prepared for
     * an upcoming event or game.
     */
    private ListView<Label> readyPlayers;
    /**
     * Indicates whether the current entity or system is prepared or ready to proceed
     * or execute a specific operation. The readiness status is managed internally
     * and determines if further actions can be performed.
     */
    private boolean amIReady;
    /**
     * A boolean variable indicating whether the current process or operation
     * is in the process of building or constructing something.
     *
     * When set to true, it signifies that the build or construction
     * process is actively taking place. When set to false, it denotes
     * that the build process is either completed, not started, or not applicable.
     */
    private boolean amIBuilding;

    /**
     * A list that stores player labels.
     * This ArrayList holds Label objects representing players,
     * typically used to associate player-related properties or information
     * with corresponding labels in the application.
     */
    private ArrayList<Label> players;
    /**
     * Represents a collection of discarded tile identifiers.
     * This list stores the integers corresponding to tiles that have
     * been removed or discarded during gameplay or processing.
     */
    private ArrayList<Integer> discardedTiles;
    /**
     * A map that stores discarded items, associating an Integer key with a VBox value.
     * The Integer key typically represents a unique identifier, while the VBox value
     * represents a UI component or container associated with the discarded item.
     *
     * This map is likely used for managing or tracking elements that have been discarded
     * or removed in a specific context within the application.
     */
    private HashMap<Integer, VBox> discardedMap;
    /**
     * Represents the image used for displaying the game board.
     * This variable holds the graphical representation of the game board
     * and is used to render it on the screen.
     */
    private Image gameBoardImg;
    /**
     * Represents the primary game board displayed in the application's user interface.
     * This is a GridPane layout container used to organize and hold the components
     * or elements of the game board in a structured grid format.
     */
    private GridPane myBoard;
    /**
     * Represents a placeholder image used for tiles in the application.
     * This image may be displayed when the actual tile image is not available
     * or has not yet been loaded.
     */
    private Image tilePlaceholder;
    /**
     * Represents the graphical component that displays the image of a tile.
     * Used to manage and render the visual representation of a tile in the user interface.
     */
    private ImageView tileImage;
    /**
     * Represents a container or pane for tiles that are currently uncovered in
     * a game or puzzle. This variable typically holds and manages the
     * uncovered tile objects during the execution of the program.
     */
    private TilePane uncoveredTiles;
    /**
     * Represents the rotation state of a tile in degrees.
     * This variable is used to determine the orientation of a tile
     * within a graphical or game environment. The value typically
     * ranges from 0 to 359, where 0 represents no rotation.
     */
    private int tileRotation;
    /**
     * A map that associates a String identifier with a GridPane object, representing
     * the boards of other players or entities in a board game or other grid-based system.
     * The keys in the HashMap are Strings, typically used as unique identifiers for
     * different boards, while the values are GridPane objects that represent the
     * corresponding board layouts.
     */
    private HashMap<String, GridPane> othersBoards;
    /**
     * A boolean variable that indicates the validity status.
     * It is used to determine if a certain condition, state, or operation is valid or not.
     */
    private boolean checkvalidity;
    /**
     * A private instance of VBox used for structuring or arranging UI components
     * in a vertical layout. The hourglassBox variable may specifically be utilized
     * for displaying content related to an hourglass metaphor or animation within
     * the application's user interface.
     */
    private VBox hourglassBox;
    /**
     * A private member variable of type ImageView used to store an image or graphical representation.
     * The specific purpose and usage of this variable should be determined based on the larger context
     * of the application in which it is used.
     */
    private ImageView buffer1, /**
     * The variable buffer2 is used to store or manipulate data temporarily
     * within the context of the application or a specific operation.
     * It typically acts as a secondary or auxiliary buffer for processing.
     */
    buffer2;
    /**
     * Represents the primary stage or window in which the game board
     * is displayed during the application's runtime.
     * This stage manages the graphical user interface components
     * related to the game board.
     */
    private Stage gameBoardStage;

    /**
     * Represents the image to be used as the back side of a playing card.
     * This image is typically displayed when the card is face down.
     */
    private Image cardBack;
    /**
     * A HashMap that associates player identifiers (as String keys) with their corresponding rocket images (as ImageView values).
     * This map is used to manage and track the rockets assigned to each player in the game.
     */
    private HashMap<String, ImageView>playerRockets;
    /**
     * Represents a mapping between player names (as keys) and their respective positions (as values).
     * The positions typically indicate the players' ranks or placement within a game or leaderboard.
     */
    private HashMap<String, Integer> playerPositions;
    /**
     * A mapping of integer keys to IntegerPair values, representing coordinates or related data.
     * The key is an integer identifier, and the value is an IntegerPair object that holds two integers.
     */
    private HashMap<Integer, IntegerPair> coords;
    /**
     * Represents the image resource of a brown-colored alien entity.
     * This variable is used to store the visual representation of the alien,
     * typically loaded from an external source such as a file or asset bundle.
     */
    private Image brownAlien, /**
     * Represents an alien entity with unique attributes or characteristics.
     * The 'purpleAlien' variable may hold specific information or data
     * related to the alien, depending on its usage in the program.
     *
     * It is recommended to refer to the context or relevant documentation
     * for understanding the specific role or value assigned to 'purpleAlien'.
     */
    purpleAlien, /**
     * Represents a crew member in a given context, such as a team or group.
     * Typically used to denote an individual who is part of a crew
     * participating in a shared task or mission.
     * The specific attributes or role of the crew member may vary depending
     * on the implementation or application.
     */
    crewMate;
    /**
     * Represents a graphical pane within the user interface dedicated to displaying or managing rockets.
     * This pane can be used to hold visual elements, such as images, buttons, or labels, related to rockets.
     */
    private Pane rocketsPane;
    /**
     * A ListView component that displays a list of log entries as strings.
     * This can be used to show a dynamic or static set of log messages in the
     * user interface.
     */
    private ListView<String> log;
    /**
     * Represents the total attack value of an entity, which is used to determine
     * its offensive strength in computations or game mechanics.
     */
    private double totAtk;
    /**
     * Represents the total speed calculated or accumulated, expressed as an integer value.
     * This variable is typically used to store the cumulative speed in a specific context.
     */
    private int totSpeed;
    /**
     * Represents the total number of credits accumulated or assigned.
     * This variable is used to store an aggregate value typically
     * related to a system that tracks credit-based transactions
     * or metrics.
     */
    private int totCredits;
    /**
     * Represents the total energy value.
     * This variable stores the cumulative energy level as an integer.
     */
    private int totEnergy;
    /**
     * Represents the total damages calculated or incurred in an operation or process.
     * This variable stores the cumulative value of damages quantified as an integer.
     */
    private int totDamages;
    /**
     * Represents the total number of humans.
     * This variable is used to store the count of humans in a given context.
     */
    private int totHumans;
    /**
     * Stores the coordinates of a shot in a 2D space.
     * The coordinates are represented as an instance of IntegerPair,
     * where the first integer represents the x-coordinate
     * and the second integer represents the y-coordinate.
     */
    private IntegerPair shotCoords;

    /**
     * Indicates whether a flight has started or not.
     * The value is true if the flight has been initiated,
     * and false if the flight has not yet started.
     */
    private boolean flightStarted;
    /**
     * Represents the state or condition indicating whether an action
     * or process is in a "killing" state. This variable is used to track
     * if a certain operation or situation involves termination or cessation.
     */
    private boolean killing;
    /**
     * Indicates whether a chunk of data is currently being selected.
     * This variable is used as a flag to track the state of the selection process.
     */
    private boolean selectingChunk;
    /**
     * A horizontal box container (HBox) used to hold and arrange phase-related buttons in a GUI.
     * This variable helps in organizing buttons in a horizontal layout and provides a convenient way
     * of managing the button components during different phases of the application's workflow.
     */
    private HBox phaseButtons;
    /**
     * A list of coordinate pairs representing specific command locations.
     * Each entry in the list specifies a pair of integer values
     * that may denote x and y coordinates or other pair-based data.
     */
    private ArrayList<IntegerPair> cmdCoords;
    /**
     * Indicates whether the tiles in the application are interactive or clickable.
     * When set to true, tiles can respond to user interactions such as clicks.
     * This can be used to control interactivity or enable/disable user interaction
     * with specific UI components represented as tiles.
     */
    private boolean tilesClickable;
    /**
     * Represents a Label component used to display a prompt message in the user interface.
     * This variable holds a reference to a Label object and is generally used
     * to guide or provide information to the user in the context of a graphical interface.
     */
    private Label prompt;
    /**
     * Represents a list of tile coordinates that are excluded from certain operations
     * or processes. Each excluded tile is defined by an IntegerPair, where the pair
     * typically signifies the x and y coordinates of the tile on a grid or board.
     */
    private ArrayList<IntegerPair> excludedTiles;
    /**
     * Represents the current card displayed in the application.
     * This variable is used to store and manage the ImageView object
     * associated with the currently active or visible card.
     */
    private ImageView curCard;
    /**
     * Represents the current image of a card being displayed or utilized
     * within the application's context. This variable holds the graphical
     * representation of the card in the form of an Image object.
     */
    private Image curCardImg;
    /**
     * Indicates whether the battery icon or related UI element is clickable.
     * This variable determines if user interactions, such as clicks,
     * are enabled for the battery UI component.
     */
    private boolean batteryClickable;
    /**
     * Represents the current coordinates of the cargo in the form of an integer pair.
     * This variable is typically used to track the location of cargo in a 2D plane
     * where the first integer corresponds to the x-coordinate and the second integer
     * corresponds to the y-coordinate.
     */
    private IntegerPair curCargoCoords;
    /**
     * Represents the current index of the cargo being processed or accessed
     * within a cargo-related collection or workflow.
     * This variable is used to track the position of the current cargo item.
     */
    private int curCargoIndex;
    /**
     * Represents the current cargo image displayed in the user interface.
     * This variable holds an ImageView object that is used to visualize
     * the cargo image associated with the current state or selection.
     */
    private ImageView curCargoImg;
    /**
     * A map that associates a pair of integers, represented by the IntegerPair key,
     * with a list of goods. This structure is designed to manage and store
     * collections of goods based on defined integer-pair identifiers.
     *
     * Key:
     * - IntegerPair: Represents a pair of integers, which can be used to
     *   identify specific storage compartments.
     *
     * Value:
     * - ArrayList<Goods>: Represents the collection of goods stored in the
     *   corresponding identified compartment.
     */
    private HashMap<IntegerPair, ArrayList<Goods>> storageCompartments;
    /**
     * Represents the number of rewards that remain available.
     * This variable is used to keep track of the remaining rewards
     * that can still be distributed or claimed.
     */
    private int rewardsLeft;
    /**
     * Represents the number of planets.
     * This variable stores the count of planets and is used in astronomical calculations
     * or planetary systems modeling.
     */
    private int nPlanets;
    /**
     * Indicates whether the system is currently in the process of handling cargo.
     * The value is true if cargo is being handled, and false otherwise.
     */
    private boolean handlingCargo;
    /**
     * Indicates whether a theft incident has occurred or is being tracked.
     * This variable represents the state or presence of theft in a specific context.
     * The value is true if theft is present, false otherwise.
     */
    private boolean theft;
    /**
     * A list of rewards represented as Goods objects.
     * This collection holds the rewards associated with a specific context,
     * such as achievements or milestones in an application.
     */
    private ArrayList<Goods> rewards;
    /**
     * A list of ImageView objects representing the selected images.
     * This variable is used to store and manage a collection of images
     * that have been selected by the user or through the application's
     * functionality for further processing or display.
     */
    private ArrayList<ImageView> selectedImages;
    /**
     * Represents a client responsible for handling the login functionality
     * and managing authentication requests and responses within the application.
     */
    private LoginClient loginClient;

    /**
     * Indicates whether a connection is currently in the process of being re-established.
     *
     * This variable is used to represent the state of a connection that might
     * have been interrupted or lost and is undergoing attempts to reconnect.
     * A value of {@code true} signifies that reconnection attempts are active,
     * while {@code false} indicates no ongoing reconnection attempts.
     */
    private boolean reconnecting;


    /**
     * Constructs a GuiRoot object, initializes GUI elements, and starts the GUI thread.
     *
     * @param loginClient the LoginClient instance used for authentication and communication
     */
    public GuiRoot(LoginClient loginClient){
        printer = new GuiOut(this);
        this.loginClient = loginClient;

        brownAlien = new Image(getClass().getResourceAsStream("/GUI/Boards/addons/alien-brown.png"));
        purpleAlien = new Image(getClass().getResourceAsStream("/GUI/Boards/addons/alien-purple.png"));
        crewMate = new Image(getClass().getResourceAsStream("/GUI/Boards/addons/among-us-white.png"));

        guiThread = new Thread(() -> GuiMain.launchApp(this));
        guiThread.start();
    }


    /**
     * Sets the primary stage for the application and updates the stage in the printer.
     *
     * @param primaryStage the primary Stage object to be set and used throughout the application
     */
    public void setStage(Stage primaryStage){
        printer.setStage(primaryStage);
        this.primaryStage = primaryStage;
    }


    /**
     * Initializes and sets up the main application scene.
     *
     * This method is executed on the JavaFX application thread using `Platform.runLater`.
     * It prepares the primary scene and its associated components such as the root pane,
     * the content pane, the tile image, background media, and binds necessary properties
     * for dynamic adjustment with the primary stage's dimensions. The scene is finalized
     * by invoking the `goToFirstScene` method to transition to the initial scene.
     */
    public void sceneSetup(){
        Platform.runLater(()->{
            primaryRoot = new StackPane();
            contentRoot = new Pane();
            primaryScene = new Scene(primaryRoot, 800, 600);

            tileImage = new ImageView();
            tilePlaceholder = new Image(getClass().getResourceAsStream("/GUI/Tiles/tileBG.png"));
            tileImage.setImage(tilePlaceholder);
            tileImage.setOpacity(0.5);



            ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/GUI/background.jpg")));
            background.setPreserveRatio(false);

            background.fitHeightProperty().bind(primaryStage.heightProperty());
            background.fitWidthProperty().bind(primaryStage.widthProperty());


            primaryRoot.getChildren().addAll(background, contentRoot);
            primaryScene.setRoot(primaryRoot);

            goToFirstScene();
        });

    }


    @Override
    public void showMessage(String message) {
    }

    /**
     * Retrieves input from the inputQueue after displaying the specified message.
     *
     * @param message The message to be displayed before retrieving input.
     * @return The input retrieved from the inputQueue, or an empty string if an interruption occurs.
     */
    @Override
    public String askInput(String message) {
        try {
            String toSend = inputQueue.take();
            //System.out.println("to send: " + toSend);
            return toSend;
        } catch (InterruptedException e) {
            //Thread.currentThread().interrupt();
            return "";
        }
    }


    /**
     * Handles player reconnection by displaying a success dialog and restoring game state.
     * This method performs two key functions:
     * <ul>
     *   <li>Displays a modal confirmation dialog on successful reconnection</li>
     *   <li>Restores the player's game state from the reconnection event</li>
     * </ul>
     *
     * <p>UI Behavior:
     * <ul>
     *   <li>Creates a modal dialog titled "Reconnection"</li>
     *   <li>Displays success message with standard styling</li>
     *   <li>Provides an exit button using the shared button maker utility</li>
     *   <li>Blocks interaction with parent window until dismissed</li>
     *   <li>Ensures thread-safe UI operations via Platform.runLater()</li>
     * </ul>
     *
     * <p>State Restoration:
     * <ul>
     *   <li>Updates player name from event.getPlayerId()</li>
     *   <li>Updates game name from event.getGameId()</li>
     *   <li>Updates game level from event.getLv()</li>
     *   <li>Sets reconnecting flag to true for phase handling</li>
     *   <li>Only executes restoration if playerId is not null</li>
     * </ul>
     *
     * @param event The ReconnectedEvent containing reconnection details:
     *              <ul>
     *                <li>playerId - The reconnected player's identifier</li>
     *                <li>gameId - The game identifier being rejoined</li>
     *                <li>lv - The game level (1 or 2)</li>
     *              </ul>
     *
     * @implNote Implementation details:
     * <ul>
     *   <li>UI operations are thread-safe via Platform.runLater()</li>
     *   <li>Uses WINDOW_MODAL modality to block parent window</li>
     *   <li>Reuses goBackButtonMaker for consistent button styling</li>
     *   <li>State restoration happens outside Platform.runLater()</li>
     *   <li>Null check prevents overwriting state with empty values</li>
     * </ul>
     *
     * @see ReconnectedEvent
     * @see Platform#runLater(Runnable)
     * @see Modality#WINDOW_MODAL
     * @see #goBackButtonMaker(Stage)
     */
    @Override
    public void reconnect(ReconnectedEvent event) {

        Platform.runLater(()->{
            Stage connectStage = new Stage();
            connectStage.setTitle("Reconnection");

            Label connected = new Label("Reconnected Succesfully!");
            connected.setStyle("-fx-font-size: 15px");

            Button exit = goBackButtonMaker(connectStage);

            VBox quitBox = new VBox(3, connected, exit);
            quitBox.setAlignment(Pos.CENTER);

            Scene connectedScene = new Scene(quitBox, 330, 80);
            connectStage.setScene(connectedScene);
            connectStage.initOwner(primaryStage);
            connectStage.initModality(Modality.WINDOW_MODAL);
            connectStage.show();
        });

        if(event.getPlayerId() != null){
            myName = event.getPlayerId();
            myGameName = event.getGameId();
            myGameLv = event.getLv();
            reconnecting = true;
        }
    }



    /**
     * Updates the game board UI to reflect a new tile placement or modification received via a {@link TileEvent}.
     *
     * <p>This method handles a wide range of tile update scenarios, including:
     * <ul>
     *   <li>Placing a new tile image on the board based on the tile ID and rotation.</li>
     *   <li>Rendering alien crew members, human crew, cargo, and batteries over tiles.</li>
     *   <li>Managing interactivity for specific tile types during different game phases (e.g., building, cargo handling, theft, selection).</li>
     *   <li>Updating the internal list of {@code excludedTiles} depending on the tile content.</li>
     *   <li>Handling special buffer tile cases at fixed coordinates (3,8) and (3,9).</li>
     * </ul>
     * If the tile includes interactive elements (e.g., crew or cargo), appropriate mouse event handlers are attached
     * to support gameplay mechanics like removal, cargo selection, theft, or special commands.
     *
     * <p>Tile updates are applied on the JavaFX Application Thread using {@code Platform.runLater()} to safely
     * manipulate UI components.
     *
     * @param event the {@link TileEvent} carrying all necessary data for rendering and interactivity, including tile ID,
     *              coordinates, rotation, and contents such as aliens, crew, cargo, or batteries
     */
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
                    else if(selectingChunk){
                        inputQueue.add("SelectChunk "+event.getX() + " "+ event.getY());
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
                            else if(selectingChunk){
                                inputQueue.add("SelectChunk "+event.getX() + " "+ event.getY());
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
                VBox cargoBox = new VBox(cargoImg);
                cargoBox.setPadding(new Insets(10));
                cargoImg.setFitWidth(45);
                cargoImg.setPreserveRatio(true);
                crewPane.getChildren().setAll(cargoBox);


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
                    }
                    else if(selectingChunk){
                        inputQueue.add("SelectChunk "+event.getX() + " "+ event.getY());
                    }
                    else {
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
                    ImageView battery = new ImageView(new Image(getClass().getResourceAsStream("/GUI/buildingPhase/battery.png")));
                    battery.setFitHeight(50);
                    tileImg.setOpacity(0.7);
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
                        else if(selectingChunk){
                            inputQueue.add("SelectChunk "+event.getX() + " "+ event.getY());
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



    /**
     * Updates the personal board (PB) UI of another player based on a {@link PlayerTileEvent}.
     *
     * <p>This method handles the display of another player's board state by rendering tile images
     * along with any associated game elements such as aliens, human crew, cargo, or batteries.
     * If the player's board has not been displayed before, a new {@code GridPane} is created for them.
     *
     * <p>UI components are updated on the JavaFX Application Thread using {@link Platform#runLater(Runnable)}.
     *
     * <p>Displayed elements may include:
     * <ul>
     *     <li><b>Aliens</b>: Brown or purple alien images layered on the tile.</li>
     *     <li><b>Humans</b>: A row of small crew images.</li>
     *     <li><b>Cargo</b>: A clickable cargo icon that opens a dialog showing cargo contents.</li>
     *     <li><b>Batteries</b>: Battery icons displayed with reduced tile opacity.</li>
     * </ul>
     *
     * @param event the {@link PlayerTileEvent} containing the player name, tile position, tile ID, and any additional elements (aliens, crew, cargo, batteries)
     */
    @Override
    public void updateOthersPB(PlayerTileEvent event) {
        Platform.runLater(() -> {
            StackPane tileStack;
            Pane crewPane = new Pane();

            if (!othersBoards.containsKey(event.getPlayerName())) {
                othersBoards.put(event.getPlayerName(), new GridPane());
                Label name = new Label(event.getPlayerName());
                name.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #fbcc18;");
                players.add(name);
//                others.getChildren().add(new VBox(name, othersBoards.get(event.getPlayerName())));
            }
            ImageView tileImg = new ImageView();
            tileImg.setFitWidth(40);
            tileImg.setPreserveRatio(true);

            if (event.getId() == 157) {
                tileImg.setImage(tilePlaceholder);
                tileImg.setOpacity(0.5);
            } else if (event.getId() < 157) {
                setColors(event.getPlayerName(), event.getId());
                tileImg.setImage(new Image(getClass().getResourceAsStream("/GUI/Tiles/tile (" + event.getId() + ").jpg")));
                tileImg.setRotate(event.getRotation());

                if (event.isBrownAlien() || event.isPurpleAlien() || event.getHumans() > 0) {
                    ImageView crewImg = new ImageView();
                    crewImg.setFitWidth(25);
                    crewImg.setPreserveRatio(true);

                    if (event.isBrownAlien()) {
                        crewImg.setImage(brownAlien);
                        crewPane.getChildren().add(crewImg);
                    } else if (event.isPurpleAlien()) {
                        crewImg.setImage(purpleAlien);
                        crewPane.getChildren().add(crewImg);
                    } else if (event.getHumans() > 0) {

                        HBox humans = new HBox(2);

                        for (int i = 0; i < event.getHumans(); i++) {
                            ImageView crew = new ImageView(crewMate);
                            crew.setFitWidth(18);
                            crew.setPreserveRatio(true);
                            humans.getChildren().add(crew);

                        }
                        crewPane.getChildren().add(humans);
                    }
                } else if (event.getBatteries() > 0) {
                    HBox batteries = new HBox(2);
//                    batteries.setPadding(new Insets(10));
                    for (int i = 0; i < event.getBatteries(); i++) {
                        ImageView battery = new ImageView(new Image(getClass().getResourceAsStream("/GUI/buildingPhase/battery.png")));
                        battery.setFitHeight(30);
                        tileImg.setOpacity(0.7);
                        battery.setPreserveRatio(true);
                        batteries.getChildren().add(battery);
                    }
                    crewPane.getChildren().add(batteries);
                } else if (event.getCargo() != null) {
                    ImageView cargoImg = new ImageView(new Image(getClass().getResourceAsStream("/GUI/cargo/cargo.png")));
                    VBox cargoBox = new VBox(cargoImg);
                    cargoBox.setPadding(new Insets(10));
                    cargoImg.setFitWidth(20);
                    cargoImg.setPreserveRatio(true);
                    crewPane.getChildren().setAll(cargoBox);

                    cargoImg.setOnMouseClicked(e -> {
                        Stage cargoStage = new Stage();
                        if (event.getCargo().size() > 0)
                            cargoStage.setTitle(event.getPlayerName() + "'s cargo'");
                        else
                            cargoStage.setTitle("Storage empty");

                        HBox cargobox = new HBox(30);
                        Button cancelButton = goBackButtonMaker(cargoStage);
                        cancelButton.setText("Back");

                        for (Goods g : event.getCargo()) {
                            ImageView cargo = new ImageView(new Image(getClass().getResourceAsStream("/GUI/cargo/cargo" + g.getValue() + ".png")));
                            cargo.setFitWidth(50);
                            cargo.setPreserveRatio(true);
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
            }

            tileStack = new StackPane(tileImg, crewPane);
            tileImg.setPreserveRatio(true);


            ArrayList<Node> nodes = new ArrayList<>(othersBoards.get(event.getPlayerName()).getChildren());
            for (Node node : nodes) {
                if (node != null && GridPane.getRowIndex(node) == event.getX() && GridPane.getColumnIndex(node) == event.getY())
                    othersBoards.get(event.getPlayerName()).getChildren().remove(node);
            }
            othersBoards.get(event.getPlayerName()).add(tileStack, event.getY(), event.getX());
        });
    }



    /**
     * Updates the currently held tile in the player's hand based on the {@link HandEvent}.
     *
     * <p>This method sets the tile image to either a placeholder (when the tile ID is 158,
     * indicating an empty or removed tile) or to the corresponding tile image from resources.
     * It also resets the rotation to 0 and ensures the visual opacity is appropriate:
     * <ul>
     *   <li>0.5 opacity for placeholder tiles (ID 158)</li>
     *   <li>Full opacity (1.0) for active/valid tiles</li>
     * </ul>
     *
     * <p>UI updates are executed on the JavaFX Application Thread using {@link Platform#runLater(Runnable)}.
     *
     * @param event the {@link HandEvent} containing the tile ID for the new tile to be displayed in the player's hand
     */
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


    /**
     * Updates the UI to reflect the current state of the set of uncovered (discarded or available) tiles.
     *
     * <p>If the tile with the given ID already exists in the discarded map, it will be removed from both
     * the `uncoveredTiles` UI container and the internal tracking structures (`discardedMap`, `discardedTiles`).
     *
     * <p>If the tile is not yet in the discarded map, it will be created as an {@link ImageView}, styled,
     * wrapped in a {@link VBox}, and added to both the internal tracking structures and the UI container.
     * A mouse click on the tile will queue a "PickTile" command with the index of that tile.
     *
     * <p>All UI updates are safely wrapped inside {@link Platform#runLater(Runnable)} to ensure execution
     * on the JavaFX Application Thread.
     *
     * @param event the {@link UncoverdTileSetEvent} containing the ID of the tile to update
     */
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


    /**
     * Displays a modal window showing a set of cards from the deck, typically used when spying the deck.
     *
     * <p>The method creates a new {@link Stage} and populates it with card images based on the
     * IDs received in the {@link DeckEvent}. Specific ID ranges are handled with custom images:
     * <ul>
     *   <li>IDs between 29 and 35 are displayed as "open space" cards.</li>
     *   <li>IDs 38 and 39 are displayed as "stardust" cards.</li>
     *   <li>Other IDs are mapped to corresponding image files using their numeric value.</li>
     * </ul>
     *
     * <p>A button labeled "Ok" is included to close the window. All card images are displayed in a horizontal
     * layout and are scaled to a consistent width while maintaining aspect ratio.
     *
     * <p>All UI updates are wrapped in {@link Platform#runLater(Runnable)} to ensure execution on the
     * JavaFX Application Thread.
     *
     * @param event the {@link DeckEvent} containing a list of card IDs to be shown
     */
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


    /**
     * Displays the appropriate card image based on the given CardEvent and sets up
     * the number of planets to display for planet choice cards.
     *
     * <p>The method handles different types of cards:
     * <ul>
     *   <li>Open Space cards (IDs 29-35) - displays a generic open space image</li>
     *   <li>Stardust cards (IDs 38-39) - displays a stardust image</li>
     *   <li>Other cards - displays the specific card image corresponding to the event ID</li>
     * </ul>
     *
     * <p>For planet choice cards (IDs 21-28), this method also sets the number of planets
     * that will be available for selection:
     * <ul>
     *   <li>4 planets for cards 21 and 27</li>
     *   <li>3 planets for cards 22, 24, 25, and 28</li>
     *   <li>2 planets for cards 23 and 26</li>
     * </ul>
     *
     * @param event The CardEvent containing the card ID to display. The card image
     *              is determined based on this ID.
     *
     * @see CardEvent
     */
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


    /**
     * Handles disconnection by showing a modal dialog with options to reconnect or exit.
     * This method must be called on the JavaFX Application Thread, as it creates UI elements.
     *
     * <p>When invoked, this method:
     * <ul>
     *   <li>Creates a modal dialog titled "Connection lost"</li>
     *   <li>Displays a message and two buttons: "Reconnect" and "Exit"</li>
     *   <li>Adds the corresponding action ("Reconnect" or "Exit") to the inputQueue when a button is clicked</li>
     *   <li>Blocks interaction with the parent window (primaryStage) until resolved</li>
     * </ul>
     *
     * <p>The dialog is created and shown using {@code Platform.runLater()} to ensure thread safety
     * with the JavaFX Application Thread. The method follows the WINDOW_MODAL modality pattern,
     * meaning it only blocks the owning window (primaryStage) while allowing interaction with
     * other application windows.
     *
     * @implNote The actual reconnection or exit logic should be handled by whatever processes
     *           the messages added to the inputQueue. The dialog itself only queues these actions.
     *
     * @see Platform#runLater(Runnable)
     * @see Modality#WINDOW_MODAL
     */
    @Override
    public void disconnect() {
        Platform.runLater(()->{
            Stage disconnectStage = new Stage();
            disconnectStage.setTitle("Connection lost");

            Label disconnected = new Label("Connection lost.\nWhat do you want to do?");
            disconnected.setStyle("-fx-font-size: 15px");

            Button reconnect = new Button("Reconnect");
            Button exit = new Button("Exit");

            HBox buttons = new HBox(30, reconnect, exit);
            reconnect.setOnAction(click -> {
                inputQueue.add("Reconnect");
                System.out.println("Reconnect");
            });

            exit.setOnAction(click -> {
                inputQueue.add("Exit");
            });

            buttons.setAlignment(Pos.CENTER);
            buttons.setPadding(new Insets(5));

            VBox quitBox = new VBox(3, disconnected, buttons);
            quitBox.setAlignment(Pos.CENTER);

            Scene disconnectedScene = new Scene(quitBox, 330, 80);
            disconnectStage.setScene(disconnectedScene);
            disconnectStage.initOwner(primaryStage);
            disconnectStage.initModality(Modality.WINDOW_MODAL);
            disconnectStage.show();
        });

    }

    @Override
    public void connect() throws IOException {}

    /**
     * Initializes and displays the game board based on the specified level.
     * This method must be called on the JavaFX Application Thread as it updates UI elements.
     *
     * <p>The method performs the following actions:
     * <ul>
     *   <li>Sets the current game level (myGameLv) to the specified level</li>
     *   <li>Loads the appropriate game assets based on the level:
     *     <ul>
     *       <li>For level 1: loads level 1 game board image, card back image, and calls Lv1GameboardSetup()</li>
     *       <li>For level 2: loads level 2 game board image, card back image, and calls Lv2GameboardSetup()</li>
     *     </ul>
     *   </li>
     *   <li>Creates and displays the game board stage by calling setGameBoardStage()</li>
     * </ul>
     *
     * <p>All UI operations are wrapped in {@code Platform.runLater()} to ensure thread safety
     * with the JavaFX Application Thread.
     *
     * @param lv The game level to set up (1 or 2). Any other value will default to level 2 behavior.
     *
     * @implNote The actual game board setup is delegated to level-specific methods:
     *           {@code Lv1GameboardSetup()} or {@code Lv2GameboardSetup()}.
     *
     * @see Platform#runLater(Runnable)
     */
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


    /**
     * Creates and displays a scene for validating and removing invalid tiles from the game board.
     * This interactive scene allows players to:
     * <ul>
     *   <li>Identify and remove invalid tiles from their board</li>
     *   <li>View other players' boards</li>
     *   <li>Complete the building phase with level-specific options</li>
     * </ul>
     *
     * <p>Key features:
     * <ul>
     *   <li>Displays a prominent "Remove Invalid Tiles!" prompt with golden text</li>
     *   <li>Makes valid tiles clickable for removal (sends "RemoveTile X Y" to inputQueue)</li>
     *   <li>Excludes tiles marked in excludedTiles from being interactive</li>
     *   <li>Provides different completion behavior based on game level:
     *     <ul>
     *       <li>Level 1: Simple "FinishBuilding" command</li>
     *       <li>Level 2: Opens a position selection dialog (1-4) before sending "FinishBuilding X"</li>
     *     </ul>
     *   </li>
     *   <li>Displays other players' boards for reference</li>
     *   <li>Includes a game board preview that can be clicked to show the full game board</li>
     * </ul>
     *
     * <p>The scene layout includes:
     * <ul>
     *   <li>Left: Current player's board with interactive tiles</li>
     *   <li>Center: Action buttons and game board preview</li>
     *   <li>Right: Other players' boards</li>
     * </ul>
     *
     * @implNote This method:
     * <ul>
     *   <li>Uses Platform.runLater() for thread-safe UI updates</li>
     *   <li>Maintains game state through inputQueue commands</li>
     *   <li>Binds layout properties to primaryStage dimensions</li>
     *   <li>Sets checkValidity flag to true and amIBuilding to false</li>
     * </ul>
     *
     * @see GridPane
     * @see Platform#runLater(Runnable)
     */
    public void checkValidityScene(){
        boolean clickable;

        amIBuilding = false;
        checkvalidity = true;

        ImageView txtBackground = new ImageView(new  Image(getClass().getResourceAsStream("/GUI/all_belt.png")));
        txtBackground.setFitWidth(600);
        txtBackground.setFitHeight(100);


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
            prompt.setText("Remove Invalid Tiles!");
            prompt.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");
            StackPane textPanel = new StackPane(txtBackground, prompt);
            VBox othersBox = new VBox(20);
            int i = 0;
            for (String id : othersBoards.keySet()) {
                othersBox.getChildren().add(new VBox(5, players.get(i), othersBoards.get(id)));
                i++;
            }

            ImageView gameboard = new ImageView(gameBoardImg);
            gameboard.setFitHeight(100);
            gameboard.setPreserveRatio(true);
            gameboard.setOnMouseClicked(e -> {
                gameBoardStage.show();
            });

            HBox mainBox = new HBox(20, new VBox(100, myBoard, textPanel), new VBox(20, finishButton, gameboard), othersBox);
            mainBox.setPadding(new Insets(150));
            mainBox.setAlignment(Pos.CENTER);
            Pane root = new Pane(mainBox);

            mainBox.prefWidthProperty().bind(primaryStage.widthProperty());
            mainBox.prefHeightProperty().bind(primaryStage.heightProperty());

            contentRoot.getChildren().setAll(root);
            printer.setCheckValidityScreen(primaryScene);
        });
    }



    /**
     * Updates the user interface to prompt the user to choose a position
     * for their valid ship. This method is executed on the JavaFX
     * Application Thread using Platform.runLater to ensure thread safety
     * when updating the UI.
     */
    public void choosePosition(){
        Platform.runLater(()->{
            prompt.setText("Your ship is valid, now choose a position!");
        });
    }


    /**
     * Creates and displays the crew placement scene where players can add crew members to their ship.
     * This interactive scene allows players to:
     * <ul>
     *   <li>Select different crew types (human, purple alien, brown alien - level 2 only)</li>
     *   <li>Place selected crew members on valid tiles of their board</li>
     *   <li>View other players' boards for reference</li>
     * </ul>
     *
     * <p>Key features:
     * <ul>
     *   <li>Displays a prominent "Populate Your Ship!" prompt with golden text</li>
     *   <li>Provides crew selection panel with clickable crew member images</li>
     *   <li>Makes valid board tiles clickable for placement (sends commands to inputQueue)</li>
     *   <li>Excludes tiles marked in excludedTiles from being interactive</li>
     *   <li>Shows different crew options based on game level:
     *     <ul>
     *       <li>Level 1: Only human crew members available</li>
     *       <li>Level 2: Human, purple alien, and brown alien crew members available</li>
     *     </ul>
     *   </li>
     *   <li>Displays other players' boards in a separate panel</li>
     * </ul>
     *
     * <p>The scene layout includes:
     * <ul>
     *   <li>Left: Current player's board with interactive tiles</li>
     *   <li>Center: Crew selection panel (right of player's board)</li>
     *   <li>Right: Other players' boards</li>
     *   <li>Bottom: Action prompt with decorative background</li>
     * </ul>
     *
     * <p>State management:
     * <ul>
     *   <li>Sets amIBuilding flag to false</li>
     *   <li>Sets checkvalidity flag to false</li>
     *   <li>Uses AtomicReference to track currently selected crew type</li>
     * </ul>
     *
     * @implNote This method:
     * <ul>
     *   <li>Uses Platform.runLater() for thread-safe UI updates</li>
     *   <li>Maintains game state through inputQueue commands</li>
     *   <li>Binds layout properties to primaryStage dimensions</li>
     *   <li>Command format: "[AddCrew|AddPurpleAlien|AddBrownAlien] Y X"</li>
     * </ul>
     *
     * @see GridPane
     * @see Platform#runLater(Runnable)
     * @see AtomicReference
     */
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
            int i = 0;
            for (String id : othersBoards.keySet()) {
                othersBox.getChildren().add(new VBox(5, players.get(i), othersBoards.get(id)));
                i++;
            }

            HBox mainBox = new HBox(20, new VBox(100, boardBox, textPanel), othersBox);
            mainBox.setPadding(new Insets(150));
            mainBox.setAlignment(Pos.CENTER);
            Pane root = new Pane(mainBox);

            mainBox.prefWidthProperty().bind(primaryStage.widthProperty());
            mainBox.prefHeightProperty().bind(primaryStage.heightProperty());

            contentRoot.getChildren().setAll(root);
            printer.setAddCrewScreen(primaryScene);
        });


    }


    /**
     * Displays the game lobby interface where players can create or join games.
     * This method handles both the initial lobby display and dynamic updates when lobby events occur.
     *
     * <p>The lobby provides:
     * <ul>
     *   <li>A list of available games with their players</li>
     *   <li>Options to create new games or reconnect to existing ones</li>
     *   <li>Dynamic updates when players join/create games</li>
     * </ul>
     *
     * <p>Key components:
     * <ul>
     *   <li><b>Game List</b>:
     *     <ul>
     *       <li>Displays all active games with their IDs and player lists</li>
     *       <li>Each entry has a "Join" button</li>
     *       <li>Updates automatically when receiving LobbyEvents</li>
     *     </ul>
     *   </li>
     *   <li><b>New Game Button</b>:
     *     <ul>
     *       <li>Opens a form to create a new game</li>
     *       <li>Collects: player name, game name, game mode (Tutorial/Complete), max players</li>
     *       <li>Validates inputs before sending creation request</li>
     *     </ul>
     *   </li>
     *   <li><b>Reconnect Button</b>:
     *     <ul>
     *       <li>Allows reconnection using a session token</li>
     *       <li>Validates token before sending reconnect request</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <p>Behavior details:
     * <ul>
     *   <li>Processes incoming LobbyEvents to add/remove games from the list</li>
     *   <li>Maintains lobbyEvents collection to track current game states</li>
     *   <li>Uses modal dialogs for game creation and reconnection</li>
     *   <li>Enforces input validation (e.g., name length limits)</li>
     *   <li>Routes all actions through the inputQueue</li>
     * </ul>
     *
     * @param event The LobbyEvent triggering the update (new game created or player joined)
     *
     * @implNote This method:
     * <ul>
     *   <li>Uses Platform.runLater() for thread-safe UI updates</li>
     *   <li>Maintains game state through inputQueue commands</li>
     *   <li>Binds layout properties to primaryStage dimensions</li>
     *   <li>Command formats:
     *     <ul>
     *       <li>Create: "Create", name, gameName, level, maxPlayers</li>
     *       <li>Reconnect: "Reconnect", token</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * @see LobbyEvent
     * @see Platform#runLater(Runnable)
     */
    @Override
    public void showLobby(LobbyEvent event){
        int index = -1;
        //mi arriva il lobby event ogni volta che qualcuno crea/si aggiunge ad un game

        Label titleLabel = new Label("GALAXY TRUCKERS");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");

        for(LobbyEvent e : lobbyEvents){
            if(e.getGameId().equals(event.getGameId())) {
                index = lobbyEvents.indexOf(e);
//                lobbyEvents.remove(e);
            }
        }

        if(index >= 0)
            lobbyEvents.remove(index);


        if(event.getLv() > 0){
            lobbyEvents.add(event);
        }
        else
            lobbyEvents.remove(event);


        ListView<LobbyEvent> gamesList = new ListView<>();
        gamesList.setPrefHeight(150);
        gamesList.setPlaceholder(new Label("No existing games"));

        gamesList.getItems().setAll(lobbyEvents);

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

                if(myName.length() > 20){
                    Stage exceptionStage = new Stage();
                    exceptionStage.setTitle("Exception");

                    Label errorLabel = new Label("Name too long (max: 20 characters)");
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
                }else{
                    inputQueue.add("Create");
                    inputQueue.add(myName);
                    inputQueue.add(myGameName);
                    inputQueue.add(String.valueOf(myGameLv));
                    inputQueue.add(playerBox.getValue());
                }
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



    /**
     * Displays the lobby game interface by updating the list of players and their readiness status.
     * Updates the visual representation of player names and readiness using JavaFX components.
     *
     * @param event the GameLobbyEvent containing the list of players, their readiness status,
     *              and other data related to the game lobby.
     */
    @Override
    public void showLobbyGame(GameLobbyEvent event){

        Platform.runLater(()->{
            readyPlayers.getItems().clear();

            String s;
            for(int i=0; i< event.getPlayers().size(); i++){
                if(event.getPlayers().get(i).equals(myName)) {
                    s = event.getPlayers().get(i) + " (You)";
                    if(event.getReady().get(i)){
                        amIReady = true;
                    }
                    else {
                        amIReady = false;
                    }
                }
                else s= event.getPlayers().get(i);

                Label name = new Label(s);


                if(event.getReady().get(i))
                    name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill:  #3cc917;");
                else name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill:  #000000;");

                readyPlayers.getItems().add(name);
            }
        });

    }



    /**
     * Creates and displays the in-game lobby screen where players can prepare before the game starts.
     * This screen shows player readiness status and provides game management options.
     *
     * <p>The screen includes:
     * <ul>
     *   <li>Game name display (styled with golden text)</li>
     *   <li>List of players and their readiness status (via readyPlayers ListView)</li>
     *   <li>Interactive controls:
     *     <ul>
     *       <li>Ready/Not Ready toggle button</li>
     *       <li>Quit game button (with confirmation dialog)</li>
     *       <li>Debug ship buttons (only in level 2)</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <p>Key functionality:
     * <ul>
     *   <li><b>Ready Button</b>:
     *     <ul>
     *       <li>Toggles between "Ready!" and "Not Ready" states</li>
     *       <li>Sends "Ready" command to inputQueue when clicked</li>
     *       <li>Updates based on amIReady flag</li>
     *     </ul>
     *   </li>
     *   <li><b>Quit Button</b>:
     *     <ul>
     *       <li>Opens a confirmation dialog before quitting</li>
     *       <li>Sends "Quit" command to inputQueue when confirmed</li>
     *     </ul>
     *   </li>
     *   <li><b>Debug Buttons</b> (Level 2 only):
     *     <ul>
     *       <li>"Debug Ship 1" sends "DebugShip 0" command</li>
     *       <li>"Debug Ship 2" sends "DebugShip 1" command</li>
     *       <li>Buttons disable after use</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <p>Layout details:
     * <ul>
     *   <li>Responsive design bound to primaryStage dimensions</li>
     *   <li>Game name at top center</li>
     *   <li>Player list below game name</li>
     *   <li>Control buttons centered at bottom</li>
     * </ul>
     *
     * @implNote This method:
     * <ul>
     *   <li>Must run on JavaFX Application Thread (uses Platform.runLater())</li>
     *   <li>Shows different buttons based on game level (myGameLv)</li>
     *   <li>Uses modal dialogs for critical actions (quit confirmation)</li>
     *   <li>Maintains UI state through amIReady flag</li>
     * </ul>
     *
     * @see Platform#runLater(Runnable)
     * @see Modality#WINDOW_MODAL
     */
    public void LobbyGameScreen() {
        Platform.runLater(() -> {
            Label GameNameLabel = new Label("Game: " + myGameName);
            GameNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill:  #fbcc18;");


            Button quitButton = new Button("Quit");
            Button readyButton = new Button("Ready!");
            Button debugShip1 = new Button("Debug Ship 1");
            Button debugShip2 = new Button("Debug Ship 2");


            readyButton.setOnAction(e -> {
                if(amIReady){
                    inputQueue.add("NotReady");
                    readyButton.setText("Ready!");
                }

                else{
                    inputQueue.add("Ready");
                    readyButton.setText("Not Ready");
                }
            });

            debugShip1.setOnAction(e -> {
                inputQueue.add("DebugShip 0");
                debugShip1.setDisable(true);
                debugShip2.setDisable(true);
            });

            debugShip2.setOnAction(e -> {
                inputQueue.add("DebugShip 1");
                debugShip1.setDisable(true);
                debugShip2.setDisable(true);
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
                    confirmStage.close();
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



    /**
     * Configures and initializes a new Stage for displaying the game board.
     * Depending on the game level, the stage is customized with specific dimensions,
     * background colors, and other visual elements such as an ImageView for the game board
     * and a StackPane as its root layout.
     *
     * @return a Stage instance that represents the game board stage, fully configured
     *         and initialized but not yet displayed on the screen.
     */
    private Stage setGameBoardStage() {
        double scaleRatio = 0.85;
        int imgX, imgY;
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

            StackPane root = new StackPane();

            ImageView board = new ImageView(gameBoardImg);
            board.setFitHeight(imgY * scaleRatio);
            board.setPreserveRatio(true);

            Rectangle background = new Rectangle(imgX * scaleRatio+ 5, imgY * scaleRatio + 5);
            if(myGameLv == 2)
                background.setFill(Color.rgb(86, 40, 110));
            else
                background.setFill(Color.rgb(6, 55, 105));


            root.getChildren().addAll(background, board, rocketsPane);


            Scene scene = new Scene(root, imgX * scaleRatio, imgY * scaleRatio);
            gbStage.setScene(scene);
            gbStage.initModality(Modality.WINDOW_MODAL);
            gbStage.setResizable(false);

        });

        return gbStage;
    }



    /**
     * Updates the visual representation of player positions on the game board.
     * This method handles both adding/removing players and updating their positions
     * based on incoming GameBoardEvents.
     *
     * <p>Behavior details:
     * <ul>
     *   <li>For new players (not in playerPositions):
     *     <ul>
     *       <li>Adds their position to playerPositions map</li>
     *       <li>Adds their rocket visualization to rocketsPane</li>
     *     </ul>
     *   </li>
     *   <li>When position is -1 (player leaving):
     *     <ul>
     *       <li>Removes player from playerPositions map</li>
     *       <li>Removes their rocket from rocketsPane</li>
     *     </ul>
     *   </li>
     *   <li>For position updates:
     *     <ul>
     *       <li>Calculates screen coordinates using pre-defined coords list</li>
     *       <li>Applies 85% scaling factor to match game board scaling</li>
     *       <li>Adjusts position by -20 pixels to center the rocket image</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <p>Thread safety:
     * <ul>
     *   <li>All UI operations are wrapped in Platform.runLater()</li>
     *   <li>Safely handles concurrent modifications to playerPositions</li>
     * </ul>
     *
     * @param event The GameBoardEvent containing:
     *              <ul>
     *                <li>playerID - Unique identifier for the player</li>
     *                <li>position - New board position (-1 indicates removal)</li>
     *              </ul>
     *
     * @implNote This method:
     * <ul>
     *   <li>Relies on playerRockets containing pre-configured ImageViews for all players</li>
     *   <li>Depends on coords list containing valid coordinate mappings</li>
     *   <li>Assumes consistent 85% scaling factor matches setGameBoardStage()</li>
     *   <li>Maintains synchronization between playerPositions map and rocketsPane</li>
     * </ul>
     *
     * @see GameBoardEvent
     * @see Platform#runLater(Runnable)
     * @see #setGameBoardStage()
     */
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


    /**
     * Handles changes in rewards when a RewardsEvent occurs.
     * Updates the current cargo image, coordinates, rewards list,
     * and remaining rewards count, and initiates cargo handling.
     *
     * @param event the RewardsEvent containing details about the updated rewards
     */
    @Override
    public void rewardsChanged(RewardsEvent event){
        curCargoImg.setImage(null);
        curCargoCoords = null;
        rewards = event.getRewards();
        rewardsLeft = event.getRewards().size();
        handleCargo();
    }



    /**
     * Creates and displays the ship building scene where players construct their spaceships.
     * This interactive scene provides all necessary tools for tile management and ship construction.
     *
     * <p>The scene includes:
     * <ul>
     *   <li><b>Game Information</b>:
     *     <ul>
     *       <li>Game name display (styled with golden text)</li>
     *       <li>Preview of the game board</li>
     *     </ul>
     *   </li>
     *   <li><b>Tile Management</b>:
     *     <ul>
     *       <li>Current tile display with rotation controls (90° clockwise/counter-clockwise)</li>
     *       <li>"Pick Tile" and "Discard" buttons</li>
     *       <li>Two tile buffers with hover effects</li>
     *       <li>Hourglass button (level 2 only) for special actions</li>
     *     </ul>
     *   </li>
     *   <li><b>Player Boards</b>:
     *     <ul>
     *       <li>Current player's board (interactive)</li>
     *       <li>Other players' boards (view-only)</li>
     *     </ul>
     *   </li>
     *   <li><b>Card Decks</b> (level 2 only):
     *     <ul>
     *       <li>Three face-down decks that can be inspected</li>
     *     </ul>
     *   </li>
     *   <li><b>Completion Controls</b>:
     *     <ul>
     *       <li>Level 1: Simple "Done!" button</li>
     *       <li>Level 2: Position selection dialog (1-4) before completion</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <p>Interactive Features:
     * <ul>
     *   <li><b>Tile Rotation</b>:
     *     <ul>
     *       <li>Clockwise and counter-clockwise rotation buttons</li>
     *       <li>Maintains rotation state in tileRotation variable</li>
     *     </ul>
     *   </li>
     *   <li><b>Tile Buffers</b>:
     *     <ul>
     *       <li>Visual hover effects (opacity changes)</li>
     *       <li>Sends "ToBuffer 0" or "ToBuffer 1" commands when clicked</li>
     *     </ul>
     *   </li>
     *   <li><b>Action Buttons</b>:
     *     <ul>
     *       <li>All buttons send commands to inputQueue</li>
     *       <li>Includes validation for required selections (level 2 position)</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <p>Layout Organization:
     * <ul>
     *   <li>Left: Uncovered tiles scroll pane (40% opacity)</li>
     *   <li>Center: Main building area with player board and controls</li>
     *   <li>Right: Other players' boards</li>
     *   <li>Responsive design bound to primaryStage dimensions</li>
     * </ul>
     *
     * @implNote This method:
     * <ul>
     *   <li>Creates complex UI layouts with multiple nested containers</li>
     *   <li>Uses Platform.runLater() for thread-safe UI updates</li>
     *   <li>Maintains game state through inputQueue commands</li>
     *   <li>Shows different components based on game level (myGameLv)</li>
     *   <li>Uses modal dialogs for critical actions (position selection)</li>
     * </ul>
     *
     * @see Platform#runLater(Runnable)
     * @see Modality#WINDOW_MODAL
     */
    public void buildingScene(){
        Label GameNameLabel = new Label("Game: " + myGameName);

        GameNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #fbcc18;");

        ImageView gameBoard = new ImageView(gameBoardImg);
        gameBoard.setPreserveRatio(true);
        gameBoard.setSmooth(true);
        gameBoard.setFitWidth(400);

        ImageView hourglass = new ImageView();
        hourglass.setImage((new Image(getClass().getResourceAsStream("/GUI/buildingPhase/hourglass.png"))));
        hourglass.setPreserveRatio(true);
        hourglass.setSmooth(true);
        hourglass.setFitHeight(70);
        hourglass.setOnMouseClicked(event -> {
            inputQueue.add("Hourglass");
        });
        hourglassBox.getChildren().setAll(hourglass);


        ImageView clockwiseArrow = new ImageView(new Image(getClass().getResourceAsStream("/GUI/buildingPhase/rotate arrow clockwise.png")));
        clockwiseArrow.setPreserveRatio(true);
        clockwiseArrow.setFitHeight(70);
        clockwiseArrow.setFitWidth(50);
        clockwiseArrow.setSmooth(false);
        clockwiseArrow.setOnMouseClicked(event -> {
            tileRotation += 90;
            if(tileRotation == 360)
                tileRotation = 0;
            tileImage.setRotate(tileRotation);
        });


        ImageView counterclockwiseArrow = new ImageView(new Image(getClass().getResourceAsStream("/GUI/buildingPhase/rotate arrow counterclockwise.png")));
        counterclockwiseArrow.setPreserveRatio(true);
        counterclockwiseArrow.setFitHeight(70);
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
        bufferBox.setPadding(new Insets(35));
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
        int i = 0;
        for (String id : othersBoards.keySet()) {
            others.getChildren().add(new VBox(5, players.get(i), othersBoards.get(id)));
            i++;
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
                mainBox = new HBox( uncoveredBox, new VBox(10, cards, myBoard, buildKit), others);
            else
                mainBox = new HBox( uncoveredBox, new VBox(10, myBoard, buildKit), others);

            mainBox.setPadding(new Insets(50));

            StackPane buildingRoot = new StackPane(mainBox);
            buildingRoot.prefWidthProperty().bind(primaryStage.widthProperty());
            buildingRoot.prefHeightProperty().bind(primaryStage.heightProperty());


            contentRoot.getChildren().setAll(buildingRoot);
        });

        printer.setBuildingScene(primaryScene);
    }



    /**
     * Creates and displays the flight phase scene showing ship status and interactive elements.
     * This scene provides real-time information about the player's ship and allows interaction
     * during the flight phase of the game.
     *
     * <p>The scene includes:
     * <ul>
     *   <li><b>Ship Statistics Panel</b> displaying:
     *     <ul>
     *       <li>Cannons (Attack power)</li>
     *       <li>Engines (Speed)</li>
     *       <li>Energy</li>
     *       <li>Human crew</li>
     *       <li>Damage tokens</li>
     *       <li>Credits</li>
     *     </ul>
     *   </li>
     *   <li><b>Card Display Area</b> showing:
     *     <ul>
     *       <li>Current card (face-up)</li>
     *       <li>Deck (face-down)</li>
     *     </ul>
     *   </li>
     *   <li><b>Interactive Elements</b>:
     *     <ul>
     *       <li>Clickable tiles on player's board (when enabled)</li>
     *       <li>Game board preview button</li>
     *       <li>Phase-specific action buttons (via phaseButtons)</li>
     *     </ul>
     *   </li>
     *   <li><b>Information Panels</b>:
     *     <ul>
     *       <li>Game log/messages</li>
     *       <li>Prompt area with decorative background</li>
     *     </ul>
     *   </li>
     *   <li><b>Other Players' Boards</b> for reference</li>
     * </ul>
     *
     * <p>Interactive Features:
     * <ul>
     *   <li><b>Tile Selection</b>:
     *     <ul>
     *       <li>Only active when tilesClickable is true</li>
     *       <li>Tracks selected tiles in cmdCoords list</li>
     *       <li>Visual feedback through opacity changes</li>
     *       <li>Excludes tiles marked in excludedTiles</li>
     *     </ul>
     *   </li>
     *   <li><b>Game Board Preview</b>:
     *     <ul>
     *       <li>Shows the main game board in a modal window</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <p>Layout Organization:
     * <ul>
     *   <li>Left: Card display and game log</li>
     *   <li>Center: Statistics and player board</li>
     *   <li>Right: Other players' boards</li>
     *   <li>Responsive design bound to primaryStage dimensions</li>
     * </ul>
     *
     * @implNote This method:
     * <ul>
     *   <li>Uses Platform.runLater() for thread-safe UI updates</li>
     *   <li>Maintains state through various atomic variables</li>
     *   <li>Relies on properly initialized image resources</li>
     *   <li>Coordinates with printer for scene management</li>
     * </ul>
     *
     * @see Platform#runLater(Runnable)
     * @see IntegerPair
     */
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
            int i = 0;

            VBox othersBox = new VBox(20);
            for (String id : othersBoards.keySet()) {
                othersBox.getChildren().add(new VBox(5, players.get(i), othersBoards.get(id)));
                i++;
            }

            HBox stats = new HBox(20, new HBox(3,cannons, nCannons), new HBox(3,engine, nEngines), new HBox(3,energy, nEnergy), new HBox(3,humans, nHumans), new HBox(3,damages, nDamages), new HBox(3,credits, nCredits));
            ImageView gameboard = new ImageView(gameBoardImg);
            gameboard.setFitHeight(100);
            gameboard.setPreserveRatio(true);
            gameboard.setOnMouseClicked(e -> {
                gameBoardStage.show();
            });

            log.setMaxHeight(100);
            HBox mainBox = new HBox(new VBox(25, cardBox, log, new VBox(15, phaseButtons, gameboard)), new VBox(100,stats, myBoard, textPanel), othersBox);
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



    /**
     * Handles game phase transitions by updating the player's state and refreshing the UI.
     * This callback method is invoked whenever the game progresses to a new phase.
     *
     * <p>Key responsibilities:
     * <ul>
     *   <li>Updates the player's state with the new phase information</li>
     *   <li>Triggers a UI refresh to display the appropriate screen for the new phase</li>
     *   <li>Handles special cases for login phase and flight phase reconnection</li>
     * </ul>
     *
     * <p>Behavior details:
     * <ul>
     *   <li>Delegates state update to {@code playerClient.setPlayerState()}</li>
     *   <li>Requests UI refresh through {@code playerClient.showGame()} with the printer</li>
     *   <li>Prints debug information about the new state class</li>
     *   <li>For login phase transitions, calls {@code goToFirstScene()} to reset the UI</li>
     *   <li>When reconnecting during flight phase, resets flags and shows flight scene</li>
     * </ul>
     *
     * @param event The phase change event containing:
     *              <ul>
     *                <li>The new client state ({@code stateClient})</li>
     *                <li>Phase transition metadata</li>
     *              </ul>
     * @throws NullPointerException if event is null (enforced by {@code @NotNull} annotation)
     *
     * @implNote Important implementation details:
     * <ul>
     *   <li>Uses {@code reconnecting} and {@code flightStarted} flags to manage reconnection state</li>
     *   <li>Contains debug output that could be replaced with proper logging</li>
     *   <li>Includes commented pseudocode suggesting potential future enhancements</li>
     *   <li>State comparison uses reference equality ({@code ==}) for {@code loginClient}</li>
     * </ul>
     *
     * @see PhaseEvent
     * @see #goToFirstScene()
     * @see #flightScene()
     */
    @Override
    public void phaseChanged(@NotNull PhaseEvent event) {
        playerClient.setPlayerState(event.getStateClient());
        playerClient.showGame(printer);

        System.out.println(event.getStateClient().getClass());
        if(event.getStateClient() == loginClient){
            goToFirstScene();
        }
        if(reconnecting && flightStarted){
            reconnecting = false;
            flightScene();
        }

        //player.setstate(event.getpahse)
        //plary.state.showGUI
    }




    /**
     * Handles exceptions by displaying an error dialog and resetting any pending tile selections.
     * This method provides user-friendly error reporting and cleans up the UI state when exceptions occur.
     *
     * <p>Key functionality:
     * <ul>
     *   <li>Resets visual state of any selected tiles (sets opacity back to 1)</li>
     *   <li>Clears the command coordinates collection</li>
     *   <li>Displays a modal error dialog with the exception message</li>
     *   <li>Ensures all UI operations are performed on the JavaFX Application Thread</li>
     * </ul>
     *
     * <p>UI Behavior:
     * <ul>
     *   <li>Creates a modal dialog titled "Exception"</li>
     *   <li>Displays the exception message in a centered label</li>
     *   <li>Provides an "Ok" button to dismiss the dialog</li>
     *   <li>Blocks interaction with the parent window until dismissed</li>
     * </ul>
     *
     * @param exceptionEvent The exception event containing:
     *                       <ul>
     *                         <li>The exception that occurred</li>
     *                       </ul>
     *
     * @implNote Implementation details:
     * <ul>
     *   <li>Uses {@code Platform.runLater()} for thread-safe UI operations</li>
     *   <li>Maintains clean state by clearing {@code cmdCoords} collection</li>
     *   <li>Reuses the {@code goBackButtonMaker} utility for consistent button styling</li>
     *   <li>Dialog modality ensures user attention for error conditions</li>
     *   <li>Tile reset functionality prevents UI inconsistency after errors</li>
     * </ul>
     *
     * @see ExceptionEvent
     * @see Platform#runLater(Runnable)
     * @see Modality#WINDOW_MODAL
     * @see #goBackButtonMaker(Stage)
     */
    @Override
    public void exceptionOccurred(ExceptionEvent exceptionEvent){

        Platform.runLater(() -> {

            for(IntegerPair p : cmdCoords){
                ImageView tile = getTile(p.getFirst(), p.getSecond());
                if(tile != null)
                    tile.setOpacity(1);
            }

            cmdCoords.clear();

            ImageView alert = new ImageView(new Image(getClass().getResourceAsStream("/GUI/alert.png")));
            alert.setFitHeight(45);
            alert.setPreserveRatio(true);
            Stage exceptionStage = new Stage();
            exceptionStage.setTitle("Exception");

            Label errorLabel = new Label(exceptionEvent.getException());
            errorLabel.setStyle("-fx-font-size: 15px");

            Button okButton = goBackButtonMaker(exceptionStage);
            okButton.setText("Ok");

            VBox errorBox = new VBox(3, new HBox(10, alert, errorLabel), okButton);
            errorBox.setAlignment(Pos.CENTER);

            Scene errorScene = new Scene(errorBox, 400, 80);
            exceptionStage.setScene(errorScene);
            exceptionStage.initOwner(primaryStage);
            exceptionStage.initModality(Modality.WINDOW_MODAL);
            exceptionStage.show();
        });
    }



    @Override
    public void seeBoards() {}

    @Override
    public void refresh() {}



    /**
     * Handles card effect events by updating the game log and managing visual effects.
     * This method processes game events that trigger visual changes on the board,
     * particularly shot animations and flight phase transitions.
     *
     * <p>Key functionality:
     * <ul>
     *   <li>Adds event messages to the game log (displayed in reverse chronological order)</li>
     *   <li>Manages transition to flight phase when flight starts</li>
     *   <li>Handles shot animations with directional rendering</li>
     *   <li>Cleans up previous shot effects before applying new ones</li>
     * </ul>
     *
     * <p>Visual Effects Handling:
     * <ul>
     *   <li>Shot animations are positioned and rotated based on direction:
     *     <ul>
     *       <li>0 (left): Rotated 90° from right edge</li>
     *       <li>1 (up): Rotated 180° from bottom edge</li>
     *       <li>2 (right): Rotated 270° from left edge</li>
     *       <li>3 (down): Standard orientation from top edge</li>
     *     </ul>
     *   </li>
     *   <li>Different shot types (small/large) use different image sizes</li>
     *   <li>Previous shots are automatically removed before new ones are placed</li>
     *
     * </ul>
     *
     * @param event The LogEvent containing:
     *              <ul>
     *                <li>Message to display in log</li>
     *                <li>Optional coordinates for shot effects (x, y)</li>
     *                <li>Shot type (0-3 indicating size and style)</li>
     *                <li>Direction (0-3 indicating cardinal direction)</li>
     *              </ul>
     *
     * @implNote Implementation details:
     * <ul>
     *   <li>Uses Platform.runLater() for thread-safe UI operations</li>
     *   <li>Maintains flight state through flightStarted flag</li>
     *   <li>Stores shot coordinates in shotCoords for cleanup</li>
     *   <li>Images are loaded from /GUI/shots/ directory</li>
     *   <li>Contains commented code for potential future enhancements</li>
     * </ul>
     *
     * @see LogEvent
     * @see Platform#runLater(Runnable)
     * @see #flightScene()
     */
    @Override
    public void effectCard(LogEvent event){
        //messaggio di cosa è successo
        Platform.runLater(()->{
//            prompt.setText(event.message());
            log.getItems().addFirst(event.message());
            if (event.message().equals("Flight started")){
                flightScene();
                flightStarted = true;
            }
//            PauseTransition pause = new PauseTransition(Duration.seconds(5));
//            pause.setOnFinished(e -> prompt.setText(oldText));
//            pause.play();
            //0 sx - 1 su - 2 dx - 3 sotto
            //tipo 0 m piccolo - 1 m grande - 2 s piccolo - 3 s grande


            if(shotCoords != null){
                ArrayList<Node> nodes = new ArrayList<>(myBoard.getChildren());
                for (Node node : nodes) {
                    if (node != null && GridPane.getRowIndex(node) == shotCoords.getSecond() && GridPane.getColumnIndex(node) == shotCoords.getFirst())
                        myBoard.getChildren().remove(node);
                }

                shotCoords = null;
            }

            if(event.getX() >= 0 && event.getY() >= 0){
                ImageView shot = new ImageView(new Image(getClass().getResourceAsStream("/GUI/shots/shot_" + event.getType() + ".png")));
                shot.setFitHeight(50);
                shot.setPreserveRatio(true);

                if(event.getType() == 0 ||  event.getType() == 2)
                    shot.setFitHeight(40);

                switch(event.getDirection()){
                    case 0:{
                        shot.setRotate(90);
                        shotCoords = new IntegerPair(2, event.getX());
                        break;
                    }
                    case 1:{
                        shot.setRotate(180);
                        shotCoords = new IntegerPair(event.getY(), 3);
                        break;
                    }
                    case 2:{
                        shot.setRotate(270);
                        shotCoords = new IntegerPair(10, event.getX());
                        break;
                    }
                    case 3:{
                        shot.setRotate(0);
                        shotCoords = new IntegerPair(event.getY(), 9);
                        break;
                    }
                }
                myBoard.add(shot, shotCoords.getFirst(), shotCoords.getSecond());
            }

        });
    }




    /**
     * Updates player board statistics with the latest game state information in a thread-safe manner.
     * This method synchronizes player board metrics between the game engine and JavaFX UI,
     * ensuring all updates occur on the JavaFX Application Thread.
     *
     * <p>Maintains and updates the following player statistics:
     * <ul>
     *   <li><b>Attack Power</b> (Plasma drills: {@code totAtk})</li>
     *   <li><b>Energy Reserves</b> ({@code totEnergy})</li>
     *   <li><b>Movement Capability</b> (Engine power: {@code totSpeed})</li>
     *   <li><b>Crew Status</b> (Human count: {@code totHumans})</li>
     *   <li><b>Ship Integrity</b> (Damage taken: {@code totDamages})</li>
     *   <li><b>Financial Resources</b> (Credits: {@code totCredits})</li>
     * </ul>
     *
     * @param event The PBInfoEvent containing:
     *              <ul>
     *                <li>{@code plasmaDrillsPower} - Current attack strength</li>
     *                <li>{@code energy} - Available energy units</li>
     *                <li>{@code enginePower} - Movement capacity</li>
     *                <li>{@code numHumans} - Surviving crew members</li>
     *                <li>{@code damage} - Accumulated damage points</li>
     *                <li>{@code credits} - Available currency</li>
     *              </ul>
     *
     * @implNote Important implementation details:
     * <ul>
     *   <li>All updates are wrapped in {@code Platform.runLater()} for thread safety</li>
     *   <li>Only stores values - UI components should observe these variables</li>
     *   <li>Variables use atomic or volatile references if accessed across threads</li>
     *   <li>No input validation - assumes valid values from game engine</li>
     *   <li>Updates occur asynchronously on JavaFX thread</li>
     * </ul>
     *
     * @see PBInfoEvent
     * @see Platform#runLater(Runnable)
     * @see #totAtk
     * @see #totEnergy
     * @see #totSpeed
     * @see #totHumans
     * @see #totDamages
     * @see #totCredits
     */
    @Override
    public void updatePBInfo(PBInfoEvent event) {
        Platform.runLater(()->{
            totAtk = event.getPlasmaDrillsPower();
            totEnergy = event.getEnergy();
            totSpeed = event.getEnginePower();
            totHumans = event.getNumHumans();
            totDamages = event.getDamage();
            totCredits = event.getCredits();
        });
    }





    /**
     * Updates the hourglass display based on the start or stop status of the event and manages the countdown progress.
     *
     * @param event the HourglassEvent object that contains metadata about the hourglass operation, including whether it
     *              should start or stop.
     */
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
                hourglassImg.setImage((new Image(getClass().getResourceAsStream("/GUI/buildingPhase/super-buu-hourglass.gif"))));
                hourglassImg.setOnMouseClicked(null);
                hourglassImg.setFitWidth(150);
                hourglassImg.setFitHeight(100);
                hourglassImg.setSmooth(true);
                hourglassBox.getChildren().setAll(hourglassImg,  progressBar);
                timeline.play();

            }
            else{
                hourglassImg.setImage((new Image(getClass().getResourceAsStream("/GUI/buildingPhase/hourglass.png"))));
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


    /**
     * Displays the game outcome message to the player at the end of a game session.
     * This method updates the UI to show the final result of the game, whether it's
     * a win, loss, or other conclusion state.

     * <p>Behavior Details:
     * <ul>
     *   <li>Updates the prompt text with the game outcome message</li>
     *   <li>Ensures thread-safe UI updates via {@code Platform.runLater()}</li>
     *   <li>Uses the existing prompt component for consistent message display</li>
     * </ul>

     * @param event The FinishGameEvent containing:
     *              <ul>
     *                <li>The outcome message to display</li>
     *                <li>Potentially additional game result details</li>
     *              </ul>

     * @implNote Implementation details:
     * <ul>
     *   <li>Performs a simple text update without additional formatting</li>
     *   <li>Relies on the existing prompt component's styling</li>
     *   <li>Does not clear previous messages automatically</li>
     *   <li>Assumes the message is properly formatted for display</li>
     * </ul>

     * @see FinishGameEvent
     * @see Platform#runLater(Runnable)
     */
    @Override
    public void showOutcome(FinishGameEvent event){
        Platform.runLater(()->{
            prompt.setText(event.message());
        });
    }


    /**
     * Displays the scores on the scoreboard based on the scores provided in the event.
     *
     * @param event The ScoreboardEvent object that contains the scores to display.
     *              The scores should be provided as a map of player names to their
     *              corresponding scores.
     */
    @Override
    public void showScore(ScoreboardEvent event) {
        int i = 1;
        VBox scoreboard = new VBox(25);

        LinkedHashMap<String, Integer> sortedScores = event.getScores().entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new
                ));

        for (String s : sortedScores.keySet()) {
            ImageView pos = new ImageView(new Image(getClass().getResourceAsStream("/GUI/ordinalTokens/" + i + ".png")));
            pos.setFitHeight(70);
            pos.setPreserveRatio(true);
            Label name = new Label(s + "                " + sortedScores.get(s));
            name.setStyle("-fx-font-size: 27px; -fx-font-weight: bold; -fx-text-fill: #fbcc18;");

            scoreboard.getChildren().add(new HBox(15, pos, name));
            i++;
        }

        scoreboard.setAlignment(Pos.CENTER);
        scoreboard.prefWidthProperty().bind(primaryStage.widthProperty());
        scoreboard.prefHeightProperty().bind(primaryStage.heightProperty());

        Platform.runLater(() -> {
            contentRoot.getChildren().setAll(scoreboard);
            primaryStage.setTitle("Final Scoreboard");
            primaryStage.setScene(primaryScene);
            primaryStage.show();
        });
    }

    @Override
    public void background() {

    }




    /**
     * Displays a modal dialog containing the player's unique connection token.
     * This dialog provides the player with their session token for potential reconnection
     * and explains its importance.
     *
     * <p>Dialog Features:
     * <ul>
     *   <li>Title: "Token"</li>
     *   <li>Non-editable, non-focusable text field displaying the token</li>
     *   <li>Instructional text about token importance</li>
     *   <li>OK button to dismiss the dialog</li>
     *   <li>Window-modal to parent stage</li>
     * </ul>
     *
     * <p>Visual Design:
     * <ul>
     *   <li>Clean, transparent text field for token display</li>
     *   <li>Consistent 15px font for instructional text</li>
     *   <li>19px font for the token itself</li>
     *   <li>Centered alignment of all elements</li>
     *   <li>Fixed 350x150 pixel window size</li>
     * </ul>
     *
     * @param tokenEvent The TokenEvent containing:
     *                   <ul>
     *                     <li>token - The unique connection token string</li>
     *                   </ul>
     *
     * @implNote Implementation details:
     * <ul>
     *   <li>Uses Platform.runLater() for thread-safe UI operations</li>
     *   <li>Text field is disabled to prevent accidental modification</li>
     *   <li>Contains commented-out alternative label implementation</li>
     *   <li>Token is displayed exactly as received from the event</li>
     * </ul>
     *
     * @see TokenEvent
     * @see Platform#runLater(Runnable)
     * @see Modality#WINDOW_MODAL
     */
    @Override
    public void Token(TokenEvent tokenEvent){
        Platform.runLater(()->{
            Stage stage = new Stage();
            stage.setTitle("Token");

            ImageView alert = new ImageView(new Image(getClass().getResourceAsStream("/GUI/alert.png")));
            alert.setFitHeight(45);
            alert.setPreserveRatio(true);

            Label txt1 = new Label("This is your token:\n");
            txt1.setStyle("-fx-font-size: 15px");

            TextField txt2 = new TextField(tokenEvent.getToken());
            txt2.setEditable(false);
            txt2.setFocusTraversable(false);
            txt2.setStyle("-fx-background-color: transparent; -fx-border-width: 0;-fx-font-size: 19px");
            txt1.setAlignment(Pos.CENTER);


            Label txt3 = new Label("\nRemember this, you will need it for reconnection!");
            txt3.setStyle("-fx-font-size: 15px");
            Button ok = new Button("Ok");
            ok.setOnAction(e->{
                stage.close();
            });

            VBox txtBox = new VBox(3,new HBox(10, alert, txt1), txt2, txt3, ok);
            txtBox.setAlignment(Pos.CENTER);

            Scene scene = new Scene(txtBox, 400, 200);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        });
    }



    /**
     * Creates and configures a "Join" button for lobby game entries with full join workflow.
     * The button triggers a multi-step joining process when clicked:
     * <ol>
     *   <li>Displays a username input dialog</li>
     *   <li>Validates the username length</li>
     *   <li>Initiates the game join process</li>
     * </ol>
     *
     * <p>Button Behavior:
     * <ul>
     *   <li>Text: "Join"</li>
     *   <li>On click: Opens username input dialog</li>
     *   <li>Dialog title shows target game ID</li>
     * </ul>
     *
     * <p>Username Dialog Features:
     * <ul>
     *   <li>Text field with "Player Name" prompt</li>
     *   <li>Confirm button (disabled when empty)</li>
     *   <li>Back button for cancellation</li>
     *   <li>Input validation (20 character max)</li>
     *   <li>Modal to parent window</li>
     * </ul>
     *
     * <p>Join Process:
     * <ul>
     *   <li>Stores player name, game name, and level</li>
     *   <li>Validates name length before proceeding</li>
     *   <li>Sends join command to inputQueue</li>
     *   <li>Includes game level and max players from lobby</li>
     * </ul>
     *
     * @param joining The LobbyEvent containing game details:
     *                <ul>
     *                  <li>gameId - Game identifier to join</li>
     *                  <li>lv - Game level (1 or 2)</li>
     *                  <li>maxPlayers - Maximum player count</li>
     *                </ul>
     * @return Configured Button instance with join functionality
     * @throws NullPointerException if joining parameter is null (enforced by @NotNull)
     *
     * @implNote Implementation details:
     * <ul>
     *   <li>Uses WINDOW_MODAL for all dialogs</li>
     *   <li>Reuses goBackButtonMaker for consistent cancel buttons</li>
     *   <li>Maintains local state (myName, myGameName, myGameLv)</li>
     *   <li>Command format: "Create", name, gameName, level, maxPlayers</li>
     * </ul>
     *
     * @see LobbyEvent
     * @see #goBackButtonMaker(Stage)
     * @see Modality#WINDOW_MODAL
     */
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

                if(myName.length() > 20){
                    Stage exceptionStage = new Stage();
                    exceptionStage.setTitle("Exception");

                    Label errorLabel = new Label("Name too long (max: 20 characters)");
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
                }
                else{
                    InsertNameStage.close();
                    inputQueue.add("Create");
                    inputQueue.add(myName);
                    inputQueue.add(myGameName);
                    inputQueue.add(String.valueOf(joining.getLv()));
                    inputQueue.add(String.valueOf(joining.getMaxPlayers()));
                }
            });
        });
        return joinButton;
    }





    /**
     * Creates a standardized "Cancel" button for closing modal dialogs.
     *
     * <p>The button is pre-configured with:
     * <ul>
     *   <li>Text: "Cancel"</li>
     *   <li>Action: Closes the provided stage when clicked</li>
     *   <li>Consistent behavior across all dialogs</li>
     * </ul>
     *
     * @param stage The Stage window that this button should close
     * @return A configured Button instance with cancel functionality
     * @throws NullPointerException if stage parameter is null (enforced by @NotNull)
     *
     * @implNote This utility method:
     * <ul>
     *   <li>Promotes UI consistency for cancel operations</li>
     *   <li>Simplifies dialog window creation</li>
     *   <li>Follows standard JavaFX button patterns</li>
     * </ul>
     */
    private @NotNull Button goBackButtonMaker(Stage  stage) {
        Button GobackButton = new Button("Cancel");
        GobackButton.setOnAction(ev -> {
            stage.close();
        });
        return GobackButton;
    }




    /**
     * Resets the game state and displays the initial title screen.
     * This method performs a complete application reset and prepares the UI for a new game session.
     *
     * <p>Key Responsibilities:
     * <ul>
     *   <li><b>State Reset</b>:
     *     <ul>
     *       <li>Clears all game state variables and collections</li>
     *       <li>Reinitializes UI components to default states</li>
     *       <li>Resets all game flags (e.g., amIBuilding, killing, etc.)</li>
     *     </ul>
     *   </li>
     *   <li><b>UI Initialization</b>:
     *     <ul>
     *       <li>Creates title screen with game logo</li>
     *       <li>Sets up the "Start!" button with lobby navigation</li>
     *       <li>Configures responsive layout binding</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <p>Visual Elements:
     * <ul>
     *   <li>Large "GALAXY TRUCKERS" title with gold styling</li>
     *   <li>Centered "Start!" button with large font</li>
     *   <li>Responsive layout that adapts to window size</li>
     * </ul>
     *
     * @implNote Implementation Details:
     * <ul>
     *   <li>All operations are wrapped in Platform.runLater() for thread safety</li>
     *   <li>Completely resets over 25 game state variables</li>
     *   <li>Uses a StackPane as the root container for the title screen</li>
     *   <li>The "Start!" button triggers a transition to the lobby state</li>
     *   <li>Includes debug output for button press verification</li>
     * </ul>
     *
     * @see Platform#runLater(Runnable)
     */
    public void goToFirstScene() {

        Platform.runLater(() -> {
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

            players = new ArrayList<>();
            myBoard = new GridPane();
            playerClient = new PlayerClient();
            discardedTiles = new ArrayList<>();
            discardedMap = new HashMap<>();

            curCard = new ImageView();
            curCard.setImage(null);
            curCardImg = null;
            killing = false;
            selectingChunk = false;
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
            shotCoords = null;
            reconnecting = false;

            readyPlayers = new ListView<>();
            readyPlayers.setMaxHeight(110);
            log = new ListView<>();
            prompt = new Label();

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
        });
    }


    /**
     * Initializes the coordinate mapping for Level 1 game board positions.
     * This method sets up the spatial relationships between grid positions
     * and their corresponding pixel coordinates on the visual game board.
     *
     * <p>Coordinate Mapping Details:
     * <ul>
     *   <li>Processes a fixed array of alternating x,y coordinates</li>
     *   <li>Creates 18 position mappings (indexed 0-17)</li>
     *   <li>Stores mappings in the {@code coords} HashMap</li>
     * </ul>
     *
     * <p>Coordinate Array Structure:
     * <ul>
     *   <li>36-element array (18 x/y pairs)</li>
     *   <li>Alternating x and y values</li>
     *   <li>Precise pixel positions for visual elements</li>
     * </ul>
     *
     * @implNote Implementation Details:
     * <ul>
     *   <li>Uses temporary {@code IntegerPair} for coordinate processing</li>
     *   <li>Even indices (0,2,4...) become x coordinates</li>
     *   <li>Odd indices (1,3,5...) become y coordinates</li>
     *   <li>Creates immutable coordinate entries in the map</li>
     *   <li>Hard-coded values match specific Level 1 board layout</li>
     * </ul>
     *
     * @see IntegerPair
     * @see #coords
     */
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


    /**
     * Sets up the Level 2 gameboard by populating a map of coordinate pairs.
     *
     * This method initializes a predefined list of integers representing x and
     * y coordinate values. It parses this list to create pairs of coordinates
     * that are stored in the `coords` map. Each pair of integers (x, y) from
     * the list is stored as a new `IntegerPair` object, which is then added
     * to the map with an integer key.
     *
     * The method uses an index-based loop to iterate over the coordinate list.
     * Odd indices represent y-values, while even indices represent x-values
     * in the `coordsList` array.
     *
     * At each iteration:
     * - The x-coordinate value is temporarily stored in a helper object.
     * - Once both x and y values are available, a new `IntegerPair` is created
     *   and added to the `coords` map with an incrementing key.
     */
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




    /**
     * Assigns a specific color and image to a player based on the input identifier
     * and stores the association. If the player does not already have a rocket
     * assigned, an {@code ImageView} with a designated image is created and added
     * to the playerRockets map.
     *
     * @param pl The name or identifier of the player.
     * @param id The unique identifier used to determine the color and image to assign.
     */
    private void setColors(String pl, int id){
            ImageView img = new ImageView();
            img.setFitHeight(40);
            img.setPreserveRatio(true);

            if(!playerRockets.containsKey(pl)){
                if (id == 153) {
                    img.setImage(new Image(getClass().getResourceAsStream("/GUI/Boards/shuttles/shuttle_blue.png")));
                    playerRockets.put(pl, img);
                }
                if (id == 154) {
                    img.setImage(new Image(getClass().getResourceAsStream("/GUI/Boards/shuttles/shuttle_green.png")));
                    playerRockets.put(pl, img);
                }
                if (id == 155) {
                    img.setImage(new Image(getClass().getResourceAsStream("/GUI/Boards/shuttles/shuttle_red.png")));
                    playerRockets.put(pl, img);
                }
                if (id == 156) {
                    img.setImage(new Image(getClass().getResourceAsStream("/GUI/Boards/shuttles/shuttle_yellow.png")));
                    playerRockets.put(pl, img);
                }
            }
    }




    /**
     * Determines if the game has started based on the current card image
     * and the building state.
     *
     * @return true if the game has started (indicated by a non-null current card image
     *         or if not in*/
    public boolean isGameStarted(){
        if(curCardImg != null || !amIBuilding)
            return true;
        else return false;
    }



    /**
     * Returns the {@link ImageView} tile located at the specified column and row
     * within {@code myBoard}, unless the position is listed in {@code excludedTiles}.
     *
     * The method iterates through all child nodes of {@code myBoard} and checks their
     * grid position using {@link GridPane#getColumnIndex(Node)} and {@link GridPane#getRowIndex(Node)}.
     * If a tile's position matches the given {@code col} and {@code row}, and it is not part of the
     * {@code excludedTiles} list, it is returned as an {@code ImageView}.
     *
     * @param col the column index of the target tile
     * @param row the row index of the target tile
     * @return the {@code ImageView} at the specified grid position, or {@code null}
     *         if the position is excluded or no matching node is found
     */
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



    /**
     * Initializes the base state of the application interface and game settings.
     *
     * This method sets up the primary user interface components and resets game-related attributes to prepare for the next phase of the game.
     * Buttons for "Ready" and "Quit" are added, event listeners are assigned, and specific game state variables are reinitialized.
     *
     * Functionality includes:
     * - Clearing selected images and command coordinates.
     * - Setting user interface elements to a non-selectable state.
     * - Displaying a prompt message to indicate the next steps.
     * - Assigning actions to the "Ready" and "Quit" buttons to send corresponding commands to an input queue.
     *
     * This method*/
    public void baseState(){
        Button ready = new Button("Ready!");
        Button quit = new Button("Quit");

        Platform.runLater(()->{
//            for(Label name : players){
//                System.out.println("PORCODIO");
//                name.setText(name.getText() + "(not ready)");
//            }

            curCard.setImage(null);
            prompt.setText("Picking next card when everyone is ready...");
            phaseButtons.getChildren().setAll(new VBox(10, readyPlayers, new HBox(10, ready,quit)));

            cmdCoords.clear();
            selectedImages.clear();
            tilesClickable = false;
            killing = false;
            theft = false;
            selectingChunk = false;


            ready.setOnAction(e -> {
                inputQueue.add("Ready");
//                ready.setDisable(true);
            });

            quit.setOnAction(e -> {
                inputQueue.add("Quit");
            });
            primaryStage.show();
        });
    }




    /**
     * Handles the "killing" phase of the game, allowing the user to select crew members to mark for elimination.
     * This method updates the game's UI, setting up the appropriate prompts and buttons,
     */
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
            primaryStage.show();
        });

    }



    /**
     * Manages the defend phase of the game by adding buttons for actions such as "Defend!"
     * and "Do Nothing". Updates the user interface and handles user input accordingly.
     *
     * @param command The initial command to be used or modified during the defend phase logic.
     * @param txt The prompt text to be displayed to the user during the defend phase.
     */
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
            primaryStage.show();
        });

    }



    /**
     * Handles the action of consuming energy in the current game state. This method sets up the user interface
     */
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
            primaryStage.show();
        });
    }




    /**
     * Configures the interface and handles tile selection logic based on the provided parameters.
     * Allows the user to interact with the visual components to create a command using tile coordinates.
     *
     * @param command The initial command string provided as input, used as a base for further updates.
     * @param txt The text prompt displayed to the user*/
    public void giveTiles(String command, String txt, boolean chunk){
        AtomicReference<String> cmd = new AtomicReference<>(command);
        Button done = new Button("Done!");
        Button doNothing = new Button("Do Nothing");

        Platform.runLater(()->{
            curCard.setImage(curCardImg);
            tilesClickable = true;
            selectingChunk = chunk;
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
            primaryStage.show();
        });
    }




    /**
     * Displays a UI that allows the user to select a planet to explore
     * or choose to do nothing. This method initializes a ComboBox
     * containing a list of planets and updates the UI elements accordingly.
     * It also defines actions for the "Select" and "Do Nothing" buttons.
     *
     * The "Select" button is disabled unless the user selects a planet from the ComboBox.
     * When the "Select" button is clicked, the method maps the selected planet
     * to a corresponding numeric index and adds the choice to the input queue.
     * Clicking the "Do Nothing" button adds a default negative choice to the input queue.
     *
     * This method dynamically modifies the UI components using a JavaFX `Platform.runLater`
     * block to ensure the UI update is performed on the JavaFX Application Thread.
     */
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
            primaryStage.show();
        });
    }




    /**
     * Handles the "accept or decline" state by updating the user interface and setting actions
     * for the provided buttons. This method modifies the current UI elements to display the state
     * where the user is prompted to either accept or decline a choice, and their input is added to a queue.
     *
     * The method updates:
     * - The current card image displayed on the UI.
     * - The prompt text shown to the user.
     * - The buttons displayed, as well as their respective actions.
     *
     * The "Accept" button enqueues an "Accept" command into the input queue when clicked.
     * The "Decline" button enqueues a "Decline" command into the input queue when clicked.
     *
     * This method ensures that the UI updates and associated event-handling are executed on
     * the JavaFX Application Thread.
     */
    public void acceptState(){
        AtomicReference<String> cmd = new AtomicReference<>("ChoosePlanet");
        Button accept = new Button("Accept");
        Button decline = new Button("Decline");

        Platform.runLater(()->{
            curCard.setImage(curCardImg);
            prompt.setText("Accept or decline?");
            phaseButtons.getChildren().setAll(accept, decline);

            accept.setOnAction(e ->{
                inputQueue.add("Accept");
            });

            decline.setOnAction(e ->{
                inputQueue.add("Decline");
            });
            primaryStage.show();
        });

    }




    /**
     * Manages the cargo-handling phase within the application. This method sets the appropriate UI
     * components and logic for allowing users to move, select, discard, or finish handling their cargo.
     * The cargo component interacts with the game board and updates the state of rewards and input queues
     * based on user actions.
     *
     * The method accomplishes the following actions:
     * - Updates the current card image and resets cargo-related state variables.
     * - Configures interactive buttons for completing, discarding, or unselecting cargo.
     * - Sets up the stack pane containing the cargo slot and its draggable image representation.
     * - Dynamically creates clickable reward boxes based on available rewards, allowing users to
     *   select which reward they wish to place and where.
     * - Updates grid tiles to handle user interactions for placing selected rewards while enforcing
     *   certain restrictions for excluded tiles.
     * - Manages the appearance of phase-specific buttons and dynamically modifiable UI components.
     *
     * This method uses the JavaFX `Platform.runLater` to ensure updates to the UI components are made
     * within the JavaFX Application Thread.
     *
     * Preconditions:
     * - `curCard`, `curCardImg`, and other relevant UI components must be properly initialized.
     * - `rewards`, `excludedTiles`, `myBoard`, and other related game state variables must contain
     *   valid, up-to-date data.
     *
     * Postconditions:
     * - Cargo handling UI and logic will be active, enabling interaction with rewards and grid tiles.
     * - The state of `rewardsLeft`, input queues, and cargo indices will be updated based on user actions.
     */
    public void handleCargo(){
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
            ImageView slot = new ImageView(new Image(getClass().getResourceAsStream("/GUI/cargo/cargoBg.png")));
            slot.setFitHeight(70);
            slot.setPreserveRatio(true);

            HBox rewardsBox = new HBox(20);
            rewardsBox.setPadding(new Insets(20));


            if (rewardsLeft > 0){
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
                        if (X == p.getFirst() && Y == p.getSecond()){
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


            finish.setOnAction(e ->{
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
            primaryStage.show();
        });
    }



    /**
     * Handles the theft event within the application.
     * This method updates the UI components on the JavaFX application thread to reflect
     * the changes resulting from a theft scenario.
     *
     * Specifically:
     * - Sets the current card image using the provided `curCardImg`.
     * - Updates the internal theft status to `true`.
     * - Displays a prompt message instructing the user to select cargo to give.
     * - Ensures the primaryStage is visible to the user.
     *
     * This method ensures that all UI updates are executed correctly on the JavaFX thread.
     */
    public void handleTheft(){
        Platform.runLater(() -> {
            curCard.setImage(curCardImg);
            theft = true;
            prompt.setText("Select cargo to give");
            primaryStage.show();
        });
    }




    /**
     * Updates the user interface to indicate a waiting state for the user's turn.
     * This method performs the UI updates on the JavaFX Application Thread using
     * the Platform.runLater() mechanism. During this state, interactions are
     * disabled, and specific states are reset.
     *
     * The actions performed in this method include:
     * - Setting the prompt text to indicate the waiting status.
     * - Updating the current card image.
     * - Clearing any buttons related to the game phase.
     * - Disabling tile interactions and specific gameplay actions, such as killing,
     *   theft, and chunk selection.
     * - Ensuring the primary stage is displayed to the user.
     */
    public void waiting(){
        Platform.runLater(() -> {
            prompt.setText("Waiting for your turn...");
            curCard.setImage(curCardImg);
            phaseButtons.getChildren().clear();
            tilesClickable = false;
            killing = false;
            theft = false;
            selectingChunk = false;
            primaryStage.show();
        });
    }
}