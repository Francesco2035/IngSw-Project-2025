package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Controller.Listeners.RandomCardEffectListener;
import org.example.galaxy_trucker.ClientServer.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Represents a generic card in the game, serving as the base class from which specific card types inherit.
 * Provides functionality to manage the state and effects of the card during the gameplay.
 * This class is decorated with annotations to support JSON serialization and deserialization.
 *
 * The card effects, listeners, and board state interactions are encapsulated in this class, and many
 * of the features are intended to be overridden by subclasses implementing specific card behaviors.
 *
 * Subtypes include various specific card types such as Slavers, Smugglers, Pirates, SolarSystem,
 * and others, which are resolved through JSON serialization.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "Card"
)


@JsonSubTypes({
        @JsonSubTypes.Type(value = Slavers.class, name = "Slavers"),
        @JsonSubTypes.Type(value = Smugglers.class, name = "Smugglers"),
        @JsonSubTypes.Type(value = Pirates.class, name = "Pirates"),
        @JsonSubTypes.Type(value = SolarSystem.class, name = "SolarSystem"),
        @JsonSubTypes.Type(value = AbandonedShip.class, name = "AbandonedShip"),
        @JsonSubTypes.Type(value = AbandonedStation.class, name = "AbandonedStation"),
        @JsonSubTypes.Type(value = Meteorites.class, name = "Meteorites"),
        @JsonSubTypes.Type(value = OpenSpace.class, name = "OpenSpace"),
        @JsonSubTypes.Type(value = Warzone.class, name = "Warzone"),
        @JsonSubTypes.Type(value = Stardust.class, name = "Stardust"),
        @JsonSubTypes.Type(value = Epidemic.class, name = "Epidemic")
})





public class Card implements Serializable {
    /**
     * Represents a listener associated with concurrent card events within the card system.
     * This field is intended to manage and notify changes specific to concurrent card phases by utilizing the
     * associated {@link ConcurrentCardListener} interface. The assigned listener handles events
     * when the state of a card changes during a concurrent process, enabling dynamic interaction
     * with the card's lifecycle.
     *
     * It is typically used to signal changes in state by invoking the appropriate methods in an
     * implementation of {@link ConcurrentCardListener}.
     */
    public ConcurrentCardListener concurrentCardListener;
    /**
     * A HashMap that holds the mapping between unique string identifiers and their corresponding
     * {@link RandomCardEffectListener} implementations. Each entry in the map associates
     * a specific effect identifier with a listener responsible for executing the effect when triggered.
     *
     * The identifiers represent the unique names or keys associated with random card effects, and
     * the listeners define the actions to be executed when their corresponding effect is invoked.
     *
     * This field is primarily used to register, retrieve, and execute specific random card effects
     * within the context of the Card class operations.
     */
    public HashMap<String, RandomCardEffectListener> RandomCardEffectListeners = new HashMap<>();


    /**
     * A final object used as a lock for synchronizing critical sections of code within
     * the Card class to ensure thread safety.
     * The lock is typically employed to manage access to shared resources or
     * guarantee consistent state updates when multiple threads interact with a Card object.
     */
    private  final Object lock = new Object();

    /**
     * Retrieves the lock object associated with the card, used for synchronization purposes.
     *
     * @return the lock object that ensures thread safety for operations on the card.
     */
    public  Object getLock() {
        return lock;
    }

    /**
     * Represents the unique identifier for a card. This value is used to distinguish
     * and reference individual card instances within the Card class.
     */
    private int id;
    /**
     * Represents the level of the card in the game.
     *
     * This variable defines the difficulty or stage of a card. It is integral to
     * determining game logic, challenges, and effects tied to the card's level.
     * The value is typically set during the card's initialization and may
     * influence gameplay mechanics like power scaling or card effects.
     */
    @JsonProperty("Level")
    private int Level;
    /**
     * Represents the duration of a specific Card within the game.
     * The value of this variable typically denotes the time in seconds or other
     * relevant units that the card effect or activity will last.
     *
     * This property is managed using JSON serialization/deserialization,
     * allowing it to be included in or reconstructed from JSON data.
     *
     * The `Time` variable is likely set upon Card creation and can be
     * accessed or modified using the associated getter and setter methods:
     * - Getter: `public int getTime()`
     * - Setter: `public void setTime(int time)`
     *
     * The `Time` value plays a critical role in determining the tempo and duration
     * of gameplay mechanics related to this Card.
     */
    @JsonProperty("Time")
    private int Time;
    /**
     * Represents the current game board associated with the Card instance.
     * This field is used to define the state of the game and its components
     * within the context of the Card's operations and behavior.
     */
    private GameBoard Board;
    /**
     * Represents the default punishment value associated with the card.
     * This variable is used to determine the standard penalty to apply
     * in specific situations involving the card's effects or interactions.
     */
    private int DefaultPunishment;
    /**
     * Indicates whether the card has reached its completed or finalized state.
     * This boolean flag is used to track the lifecycle state of the card during its execution.
     * It can be set to true to represent that the card's actions or effects are finished,
     * or false to indicate that it is still in progress.
     */
    private  boolean finished;

    /**
     * Constructs a new Card object with the specified level, time, and game board.
     *
     * @param level the level of the card, which determines its difficulty or hierarchy in the game.
     * @param time the time associated with the card, potentially used for timing or duration-related mechanics.
     * @param board the game board to which the card is associated, representing the context of the game.
     */
    public Card(int level, int time, GameBoard board) {
        this.Level = level;
        this.Time = time;
        this.Board = board;
    }


    /**
     * Evaluates the current state of players in the game and determines if any players
     * should lose or abandon the game based on specific conditions such as being "doubled"
     * or having no remaining crew.
     *
     * The method interacts with the game board and player objects, resetting their states,
     * and uses the game board's logic to determine losers. It handles two main conditions:
     *
     * 1. Players who have been doubled:
     *    - Checks for players that have been lapped multiple times on the game board.
     *    - These players are forced to abandon the race with a specific message.
     *
     * 2. Players with no crew left:
     *    - Iterates through all players and checks the status of their crew.
     *    - If a player's crew count reaches zero, they are forced to abandon the race with
     *      a specific message.
     *
     * This method ensures the list of losing players is updated and processed appropriately
     * without duplicates, and all necessary notifications are handled for the game flow.
     */
    public void checkLosers(){
        ArrayList<Player> losers = new ArrayList<>();
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(new BaseState());
        }
        losers.removeAll(getBoard().checkDoubleLap());   // così non ho doppioni :3
        losers.addAll(getBoard().checkDoubleLap());
        for(Player p: losers){
            getBoard().abandonRace(p, "You have been doubled",true);
        }

        losers.clear();
        PlayerList = Board.getPlayers();
        for (Player p : PlayerList){
            if (p.getmyPlayerBoard().getNumHumans() == 0){
                losers.add(p);
            }
        }

        for(Player p: losers){
            getBoard().abandonRace(p, "No crew left",true);
        }

    }


    /**
     * Sets the {@link ConcurrentCardListener} to handle concurrent card-related events.
     * This listener will be notified when the concurrent phase state changes.
     *
     * @param listener the {@link ConcurrentCardListener} instance to be used for handling
     *                 concurrent card state events. It processes events related to
     *                 the concurrent phase of a card.
     */
    public void setConcurrentCardListener(ConcurrentCardListener listener){
        this.concurrentCardListener = listener;
        this.concurrentCardListener.onConcurrentCard(false);

    }

    /**
     * Removes the concurrent card listener associated with this card.
     * This method sets the internal reference to the concurrent card listener to null,
     * effectively detaching any previously set listener.
     */
    public void removeConcurrentCardListener(){
        this.concurrentCardListener = null;
    }

    /**
     * Executes the effect associated with the card. This method manages game-specific logic and
     * state transitions tied to the current card's effect. It may trigger interactions with the game board,
     * players, or other aspects of the game state.
     *
     * This method performs operations that require thread safety as it potentially interacts with shared resources.
     * Proper synchronization mechanisms, such as utilizing the card's internal lock, may be applied to prevent race conditions.
     *
     * The effect might involve:
     * - Triggering random events by invoking registered listeners.
     * - Modifying the game board or player states.
     * - Executing energy consumption or movement validation.
     * - Handling card progression by coordinating with other game objects or methods.
     *
     * Potential blocking operations or wait mechanisms might occur during method execution, ensuring smooth
     * coordination between game threads. The method is designed to accommodate interruption handling using checked exception.
     *
     * @throws InterruptedException if the current thread is interrupted while performing operations.
     */
    public void CardEffect() throws InterruptedException {}

    /**
     * Retrieves the current value of the Time property.
     *
     * @return the current value of the Time property as an integer.
     */
    public int getTime() {
        return this.Time;
    }


    /**
     * Retrieves the game board associated with this card.
     *
     * @return the {@code GameBoard} instance representing the game context
     *         associated with this card.
     */
    public GameBoard getBoard() {
        return this.Board;
    }

    /**
     * Updates the internal states of the card, handling game-related logic and ensuring proper synchronization
     * for thread-safe operations. This method may interact with various game components such as players,
     * the game board, or registered listeners to update relevant states in real-time.
     *
     * The logic may involve:
     * - Managing card progression by modifying game state variables.
     * - Coordinating with game resources to handle transitions or trigger specific actions.
     * - Synchronizing shared resources using the lock object to prevent race conditions.
     * - Handling ongoing effects or mechanisms tied to the card's behavior within the game.
     *
     * It is designed to execute in concurrent or multithreaded contexts, ensuring safety and consistency
     * during operations. The method can accommodate interruptions gracefully.
     *
     * @throws InterruptedException if the thread executing this method is interrupted.
     */
    public void updateStates() throws InterruptedException {}

    /**
     * Marks the current card as finished.
     *
     * This method is used to update the internal state of the card to indicate
     * that it has been completed. It may trigger necessary state transitions
     * or notify other components in the game logic that the card's activity
     * has concluded.
     *
     * The method is thread-safe and can interact with synchronized operations
     * to maintain consistency in a multithreaded environment.
     */
    public void finishCard() {}

    /**
     * Continues the execution or progression of the card based on the provided decision.
     * This method processes a boolean input to determine whether the associated
     * card action should proceed or not.
     *
     * @param accepted a boolean parameter indicating the user's choice or action:
     *                 - true: proceed with the card's associated effect or mechanism.
     *                 - false: do not proceed, potentially canceling or halting the card action.
     */
    public void continueCard(boolean accepted) {}

    /**
     * Analyzes the given power value and the number of doubles affecting the game
     * state to apply necessary logic or conditions.
     *
     * @param power the current power level to be checked, which may influence
     *              game mechanics or player states.
     * @param numofDouble the number of times a particular condition, such as doubling,
     *                    has occurred, potentially impacting the game logic or results.
     * @throws InterruptedException if the operation is interrupted during execution,
     *                              typically due to thread management or synchronization.
     */
    public void checkPower(double power, int numofDouble) throws InterruptedException {}

    /**
     * Validates or processes the movement of a card or entity within the game,
     * considering the energy consumption and potential game-specific restrictions
     * or rules. This method checks the provided power level and validates whether
     * the movement is feasible based on the current game state.
     *
     * @param power the power level required for the movement. This parameter is checked
     *              against the available resources or thresholds to ensure the movement
     *              can occur according to game mechanics.
     * @param numofDouble an indicator affecting movement validation, possibly representing
     *                    the number of instances the movement was attempted or other game-specific
     *                    modifiers influencing the movement logic.
     * @throws InterruptedException if the thread executing this method is interrupted
     *                              during processing, particularly while handling synchronized
     *                              operations or awaiting resources.
     */
    public void checkMovement(int power, int numofDouble) throws InterruptedException {}

    /**
     * Continues the card's operation based on the provided coordinates and acceptance status.
     * This method is typically used to determine the next phase or step of the card's effect
     * by using the given parameters.
     *
     * @param coordinates a list of {@link IntegerPair} representing the coordinates relevant
     *                    to the current card's effect or progression.
     * @param accepted    a boolean indicating whether the current action or card phase
     *                    has been accepted or approved.
     */
    public void continueCard(ArrayList<IntegerPair> coordinates, boolean accepted) {}

    /**
     * Executes defensive actions against large-scale threats in the game environment.
     * This method processes the provided cannon coordinates, energy storage location,
     * and the associated player to apply defensive measures. It may involve
     * modifying game states, triggering effects, or interacting with other components
     * of the game world for a synchronized defense mechanism.
     *
     * @param CannonCoord the coordinates of the cannon, represented as an {@link IntegerPair},
     *                    used for targeting or positioning during the defense.
     * @param EnergyStorage the coordinates of the energy storage, represented as an {@link IntegerPair},
     *                      that may be used or affected during the defensive actions.
     * @param player the {@link Player} executing or involved in the defensive operation.
     * @throws InterruptedException if the thread executing this method is interrupted during
     *                              operation, especially in concurrent or synchronized contexts.
     */
    public void DefendFromLarge(IntegerPair CannonCoord, IntegerPair EnergyStorage, Player player) throws InterruptedException {}

    /**
     * Defends against a minor or small-scale attack by consuming energy and interacting with player states.
     * This method implements defensive mechanics tailored to the game logic, utilizing
     * the specified energy and player to mitigate the effect of small threats.
     *
     * @param energy the {@code IntegerPair} representing the energy source used for defense.
     *               This is consumed or utilized in the defense operation.
     * @param player the {@code Player} object that is actively engaged in the defense process.
     *               The player's state may be altered as a result of defensive actions.
     * @throws InterruptedException if the thread executing this method is interrupted while handling
     *                              synchronization or game-related operations.
     */
    public void DefendFromSmall(IntegerPair energy, Player player) throws InterruptedException {}

    /**
     * Continues the card's operation based on the provided coordinates.
     *
     * @param coordinates a list of {@link IntegerPair} representing coordinates
     *                    relevant to the card's current progression or effect.
     */
    public void continueCard(ArrayList<IntegerPair> coordinates) {}

    /**
     * Executes the next phase or step for the card logic, handling game-related operations
     * and ensuring proper synchronization for thread-safe execution in a concurrent environment.
     *
     * This method progresses the card's effect or state without requiring additional parameters.
     * Depending on the implementation, it may involve triggering effects, updating game
     * states, interacting with players or the game board, and managing ongoing actions.
     *
     * The execution of this method may involve blocking operations or waiting mechanisms
     * to coordinate actions in a multithreaded context. Thread safety is enforced through
     * synchronization using the card's associated lock.
     *
     * Designed to handle interruptions, allowing graceful termination or recovery when
     * the executing thread is interrupted.
     *
     * @throws InterruptedException if the thread executing this method is interrupted
     *                              during execution.
     */
    public void continueCard() throws InterruptedException {}

    /**
     * Executes an operation to terminate humans at specified coordinates.
     * The method processes coordinates provided in the form of integer pairs and may throw
     * an InterruptedException during its execution.
     *
     * @param coordinates A list of IntegerPair objects representing the coordinates where
     *                     actions will take place.
     * @throws InterruptedException if the operation is interrupted during execution.
     */
    public void killHumans(ArrayList<IntegerPair> coordinates) throws InterruptedException {}

    /**
     * Selects a planet based on the provided planet identifier.
     *
     * @param planet the identifier of the planet to be selected, typically represented as an integer
     */
    public void choosePlanet(int planet){}

    /**
     * Executes a process or task that continues to operate under certain conditions.
     * This method can be interrupted, and care must be taken when handling the InterruptedException.
     *
     * @throws InterruptedException if the thread executing the method is interrupted.
     */
    public void keepGoing() throws InterruptedException {}

    /**
     * Consumes energy based on the given coordinates. This method processes
     * a list of integer pairs representing coordinates and performs operations
     * related to energy consumption.
     *
     * @param coordinates a list of IntegerPair objects representing the coordinates
     *                    to process.
     * @throws InterruptedException if the thread executing the method is interrupted.
     */
    public void consumeEnergy(ArrayList<IntegerPair> coordinates) throws InterruptedException {}

    /**
     * Removes cargo from the specified position based on the given pair of integer values.
     *
     * @param pair A pair of integers typically representing specific coordinates or identifiers.
     * @param position The position index indicating where the cargo should be removed.
     * @throws InterruptedException If the current thread is interrupted while performing the operation.
     */
    public void loseCargo(IntegerPair pair, int position) throws InterruptedException {}

    /**
     * Activates a card for use in the system.
     * This method enables the card functionality, making it ready
     * for transactions or other permitted actions.
     * Ensure that the card has been initialized and is valid
     * before calling this method.
     */
    public void ActivateCard(){}

    /**
     * Retrieves the default punishment value.
     *
     * @return the default punishment as an integer
     */
    public int getDefaultPunishment(){
        int r =this.DefaultPunishment;
        return r;
    }

    /**
     * Sets the default punishment value.
     *
     * @param p the punishment value to set as default
     */
    public void setDefaultPunishment(int p){DefaultPunishment = p;}

    /**
     * Retrieves the instance of ConcurrentCardListener.
     *
     * @return the ConcurrentCardListener instance associated with the current context
     */
    public ConcurrentCardListener getConcurrentCardListener() {
        return concurrentCardListener;
    }

    /**
     * Checks whether the process or operation is complete.
     *
     * @return true if the process or operation has finished, false otherwise.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Sends a type log entry to the logging system.
     *
     * This method is responsible for logging information about the
     * type events or operations. The specific implementation of logging
     * depends on the underlying logic of the application.
     *
     * It does not accept any arguments and does not return any value.
     *
     * Note: Ensure that the logging system is properly configured
     * before calling this method to avoid issues with log transmission or storage.
     */
    public void sendTypeLog(){}

    /**
     * Sets the finished status of an object.
     *
     * @param finished a boolean value indicating whether the object is finished (true) or not finished (false)
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Associates a RandomCardEffectListener with a specific card ID.
     * This method allows setting a listener for a given card to handle
     * its random effect behavior.
     *
     * @param id the unique identifier of the card for which the listener is being set
     * @param randomCardEffectListener the listener instance to be associated with the card
     */
    public void setRandomCardEffectListeners(String id, RandomCardEffectListener randomCardEffectListener) {
        getRandomCardEffectListeners().put(id, randomCardEffectListener);
    }


    /**
     * Sends a random card effect event to the specified player.
     * If the player exists in the random card effect listeners map, the effect will be triggered for that player.
     * If the listener for the player is null, a message will be logged indicating a potential issue.
     *
     * @param playerid The ID of the player to whom the random card effect should be sent.
     * @param randomCardEffectEvent The event representing the random card effect to be sent.
     */
    public void sendRandomEffect(String playerid, LogEvent randomCardEffectEvent) {
        //System.out.println("invio a "+playerid+" "+randomCardEffectEvent.message());
        if(getRandomCardEffectListeners().get(playerid) != null) {
            getRandomCardEffectListeners().get(playerid).Effect(randomCardEffectEvent);
        }
        else{
            System.out.println("the value of the card listener was Null if its not a test this is an issue ");
        }
    }

    /**
     * Retrieves a map of random card effect listeners.
     *
     * @return a HashMap where the keys are strings representing the names or identifiers of random card effects,
     * and the values are RandomCardEffectListener instances associated with those effects.
     */
    public HashMap<String, RandomCardEffectListener> getRandomCardEffectListeners() {
        return RandomCardEffectListeners;
    }


    /**
     * Default constructor for the Card class.
     * Initializes a new instance of the Card object with no parameters.
     */
    public Card() {}

    /**
     * Retrieves the unique identifier.
     *
     * @return the unique identifier as an integer
     */
    public int getId() {return id;}

    /**
     * Sets the ID value for this object.
     *
     * @param id the integer value to set as the ID
     */
    public void setId(int id) {this.id = id;}

    /**
     * Retrieves the current level value.
     *
     * @return the integer value representing the current level.
     */
    public int getLevel() {return Level;}

    /**
     * Sets the level of the object.
     *
     * @param level the level to set
     */
    public void setLevel(int level) {Level = level;}

    /**
     * Sets the time value.
     *
     * @param time the time value to set
     */
    public void setTime(int time) {Time = time;}

    /**
     * Sets the game board to the specified board instance.
     *
     * @param board the GameBoard object to be set as the current game board
     */
    public void setBoard(GameBoard board) {Board = board;}
}