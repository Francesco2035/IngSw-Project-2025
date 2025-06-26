package org.example.galaxy_trucker.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.galaxy_trucker.ClientServer.Messages.ScoreboardEvent;
import org.example.galaxy_trucker.Controller.Listeners.CardListner;
import org.example.galaxy_trucker.Controller.Listeners.HandListener;
import org.example.galaxy_trucker.Controller.Listeners.PhaseListener;
import org.example.galaxy_trucker.ClientServer.Messages.FinishListener;
import org.example.galaxy_trucker.ClientServer.Messages.HandEvent;
import org.example.galaxy_trucker.ClientServer.Messages.ReadyListener;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a player in the game, encapsulating their state, attributes, and actions.
 * This class handles player-specific behaviors, such as managing tiles, cards, construction,
 * and interactions with game board elements.
 */
public class Player implements Serializable {

    /**
     * An instance of HandListener used to handle and respond to events
     * related to actions or state changes in a player's hand during gameplay.
     *
     * The HandListener provides a mechanism to notify when changes occur
     * in the player's hand, such as adding, removing, or updating items.
     */
    private HandListener handListener;
    /**
     * Represents the shared game board that all players interact with during the game.
     * This variable is used to manage and coordinate game actions performed on the common board.
     * It is accessible and can be set or modified through appropriate methods within the Player class.
     */
    private GameBoard CommonBoard;
    /**
     * Represents the personal game board of the player.
     * This board holds essential information related to the player's progress,
     * resources, and actions throughout the game. It is used to track and manage
     * aspects specific to this player.
     */
    private PlayerBoard myPlayerBoard;
    /**
     * Represents the unique identifier for a Player instance.
     * This identifier is used to distinguish each player in the game.
     */
    private String ID;
    /**
     * Indicates whether the player is ready to proceed or take an action.
     */
    private boolean ready;
    /**
     * Indicates whether the player has performed their action during a specific phase of the game.
     * This variable is typically used to track if the player has completed their turn or action.
     */
    private  boolean HasActed;
    /**
     * Represents a listener for card-related events associated with the {@code Player}.
     * The {@code cardListner} allows the {@code Player} class to respond to events
     * triggered by the underlying {@code CardListner} interface, such as managing deck states
     * or handling new card introductions.
     *
     * This field is designed to handle interactions with cards and is typically set or
     * removed through specific methods in the {@code Player} class.
     */
    private CardListner cardListner;
    /**
     * Represents the tile currently held by the Player.
     * This tile is part of the game logic, often used to perform actions like placement,
     * rotation, or discarding during the game.
     */
    private Tile CurrentTile;   //the tile that Player has in his hand
    /**
     * Represents the current state of the player in the game.
     * It determines the set of actions or commands the player is allowed
     * to perform at any given point based on the game rules.
     * The state of the player can change dynamically as the game progresses.
     *
     * This variable interacts with the PlayerState abstract class, which defines
     * the allowed behavior and transitions for the player. It enables the encapsulation
     * of state-specific logic that governs actions and commands related to the player's gameplay.
     */
    private PlayerState PlayerState;
    /**
     * Represents a collection of goods that the player will handle during the game.
     * This list contains objects implementing the {@code Goods} interface, which may have different types
     * such as BLUE, GREEN, YELLOW, or RED, each providing specific values and behaviors.
     */
    private ArrayList<Goods> GoodsToHandle;
    /**
     * The current card held or interacted with by the player.
     * Represents a specific game card affecting the player state or gameplay.
     * The card can include various types, such as events, obstacles, or opportunities.
     */
    private Card CurrentCard;
    /**
     * Represents a listener for phase-related events, allowing the player to handle
     * notifications or changes during specific phases of the game.
     * The phaseListener variable is used to manage events triggered by transitions
     * between different phases, enabling custom logic to be executed in response
     * to these changes.
     */
    private PhaseListener phaseListener;
    /**
     * Represents a listener that responds to readiness events or notifications.
     * This variable is used to hold an implementation of the ReadyListener interface,
     * enabling the player to react to readiness signals during the game's workflow.
     * The associated ReadyListener implementation defines the behavior that occurs
     * when readiness is achieved or signaled.
     */
    private ReadyListener readyListener;
    /**
     * A reference to a FinishListener instance that is used to handle actions
     * or events triggered when the game concludes. This listener enables
     * custom logic to be executed at the end of the game, such as updating the
     * scoreboard, notifying players, or handling game completion workflows.
     */
    private FinishListener finishListener;

    /**
     * A flag indicating whether the debug call has been made in the current execution context.
     * This variable is primarily used to track if a debug operation has occurred,
     * helping to ensure that certain debug-related functionality is executed only once.
     */
    private boolean debugCalled = false;

    /**
     * Retrieves the finish listener associated with this instance.
     *
     * @return the FinishListener instance that is currently set
     */
    public FinishListener getFinishListener() {
        return finishListener;
    }

    /**
     * Sets the finish listener that will be triggered when the associated event or process is completed.

     * @param finishListener the listener to handle finish events
     */
    public void setFinishListener(FinishListener finishListener) {
        this.finishListener = finishListener;
    }

    /**
     * Retrieves the ready listener associated with this instance.
     *
     * @return the ReadyListener instance currently set.
     */
    public ReadyListener getReadyListener() {
        return readyListener;
    }

    /**
     * Sets the listener that gets notified when the component is ready.
     *
     * @param readyListener the ReadyListener to be notified when the component is ready
     */
    public void setReadyListener(ReadyListener readyListener) {
        this.readyListener = readyListener;
    }

    /**
     * Retrieves the common game board instance.
     *
     * @return the shared GameBoard instance used across the application
     */
    public GameBoard getCommonBoard() {
        return CommonBoard;
    }


    /**
     * Sets the PhaseListener for the current instance.
     *
     * @param phaseListener the PhaseListener to be set
     */
    public void setPhaseListener(PhaseListener phaseListener) {
        this.phaseListener = phaseListener;
    }


    /**
     * Retrieves the current tile.
     *
     * @return the current Tile object representing the position or state.
     */
    public Tile getCurrentTile() {
        return CurrentTile;
    }

    /**
     * Sets the current tile to the specified tile. If there is already a tile
     * set, an IllegalStateException is thrown to indicate that the operation
     * cannot be performed.
     *
     * @param currentTile the tile to be set as the current tile. Must be null to avoid exception.
     * @throws IllegalStateException if a tile is already set.
     */
    public void setCurrentTile(Tile currentTile) {
        if (currentTile != null) {
            throw new IllegalStateException("Your hand is full!");
        }
        CurrentTile = currentTile;
    }




    /**
     * Default constructor for the Player class.
     * Initializes the player state and associated properties to default values.
     * The player is created in a non-ready state, without having acted, and with no current tile, player state, or card assigned.
     * A new list is instantiated for the player's goods to handle.
     */
    public Player()  {
        ready = false;
        HasActed = false;
        CurrentTile = null;
        PlayerState= null;
        GoodsToHandle = new ArrayList<>();
        CurrentCard = null;
    }




//
//    public void consumeEnergyFrom(IntegerPair coordinates) {
//        myPlayerBoard.getEnergyTiles().stream()
//                                 .filter(pair -> pair.equals(coordinates))
//                                 .findFirst()
//                                 .ifPresent(pair -> myPlayerBoard.getTile(pair.getFirst(), pair.getSecond()).getComponent().setAbility());//riduce di 1 le batterie a x, y se non sono già a zero
//    }
//





//    public void fireCannon(){}
//    public void startEngine(){}


    /**
     * rolls 2 dice
     *
     * @return the dice result (int between 2-12)
     */
    public int RollDice() {
        Random r = new Random();
        int d1 = r.nextInt(6) + 1;
        int d2 = r.nextInt(6) + 1;
        return d1+d2;
    }


    /**
     * Retrieves the current state of the player.
     *
     * @return the current PlayerState object representing the player's current*/
    public PlayerState getPlayerState() {
        return PlayerState;
    }


    /**
     * Updates the current player state and triggers appropriate actions based on the new state.
     * If a phase listener is set, it notifies the listener about the phase change.
     * The state also determines whether the player is required to act.
     *
     * @param state The new {@link PlayerState}
     */
    public void setState(PlayerState state) {
        this.PlayerState = state;

        System.out.println("Listener null?");
        if (phaseListener != null) {
            System.out.println("Phase listener not null "+ state.toClientState().getClass());
            phaseListener.PhaseChanged(state.toClientState());
        }
        state.shouldAct(this);

    }

    /**
     * Sets the player's specific board configuration.
     *
     * @param myPlance the PlayerBoard to be set for the player
     */
    public void setMyPlance(PlayerBoard myPlance) {
        this.myPlayerBoard = myPlance;
    }

    /**
     * Executes the action associated with the player's current card.
     * This method activates the card assigned to the player, performing
     * the behavior defined within the card's implementation.
     *
     * The method depends on the `CurrentCard` being initialized and non-null.
     * If the current card is not set or initialized, this method might
     * result in a runtime exception or undesired behavior.
     */
    public void  execute() {
        this.CurrentCard.ActivateCard();
    } // possibilmente inutile

    /**
     * Starts the timer for the player by invoking the hourglass functionality
     * through the CommonBoard. This method is typically called during phases where
     * time tracking is required for the player's actions.
     *
     * @throws RuntimeException if the hourglass operation fails or if the player
     *         has not prepared their ship as required.
     */
    public void StartTimer() throws RuntimeException {
        CommonBoard.callHourglass(this);
    }



    /**
     * Picks a new tile based on the provided index. If no tile is currently picked,
     * either a new random tile or a tile at the specified index is retrieved
     * depending on the input value.
     *
     * @param index the index of the tile to be picked. If index is -1,
     *              a random tile is picked. If the index is a non-negative
     *              number, a tile at the specified index is picked.
     *              If the index is invalid, an exception is thrown.
     * @throws IllegalStateException if a tile is already picked and
     *                               the method is called again.
     * @throws InvalidInput          if the index is other than -1 or non-negative.
     */
    public void PickNewTile(int index)  {
        if (index == -1){
            if (CurrentTile != null) {
                throw new IllegalStateException("You can't pick a Tile, you have already one!");
            }
            CurrentTile = CommonBoard.getTilesSets().getNewTile();
            System.out.println("Id Tile: " +CurrentTile.getId());
            if (handListener != null) {
                handListener.handChanged(new HandEvent(CurrentTile.getId(), CurrentTile.getConnectors()));
            }
        }
        else if (index >= 0 ){
            if (CurrentTile != null) {
                throw new IllegalStateException("You can't pick a Tile, you have already one!");
            }
            try{
                CurrentTile = CommonBoard.getTilesSets().getNewTile(index);
                System.out.println("Id Tile: " +CurrentTile.getId()+ " "+ GetID());
                if (handListener != null) {
                    handListener.handChanged(new HandEvent(CurrentTile.getId(), CurrentTile.getConnectors()));
                }
            }catch(RuntimeException e){
                System.out.println(e);
                CurrentTile = null;
            }
        }
        else {
            throw new InvalidInput("Where are you picking?");
        }
    }


    /**
     * discards the current tile and places it back in the uncovered tiles list
     */
    public void DiscardTile() throws RemoteException, JsonProcessingException {
        if (CurrentTile == null) {
            throw new IllegalStateException("You can't discard a Tile, you don't have one!");
        }
        if (CurrentTile.getChosen()){
            throw new IllegalStateException("You can't discard this Tile!");
        }

        CommonBoard.getTilesSets().AddUncoveredTile(CurrentTile);
        CurrentTile = null;
        handListener.handChanged(new HandEvent(158, null));
    }

    /**
     * places the current tile in the buffer
     */
    public void PlaceInBuffer()  {
        myPlayerBoard.insertBuffer(CurrentTile);
        CurrentTile = null;
        handListener.handChanged(new HandEvent(158, null));
    }

    /**
     * takes a tile from the buffer
     * @param index of the tile to pick
     */
    public void SelectFromBuffer(int index) {
        if (CurrentTile != null) {
            throw new IllegalStateException("You can't select a Tile, you have already one!");
        }
        CurrentTile = myPlayerBoard.getTileFromBuffer(index);
        handListener.handChanged(new HandEvent(CurrentTile.getId(), CurrentTile.getConnectors()));
      }


//    /**
//     * sets the current tile on the shipboard
//     * this action is definitive: the tile cannot be moved or rotated after it is settled
//     * @param coords where the tile will be placed
//     */
//    public void PlaceTile(IntegerPair coords) {
//
//        this.myPlayerBoard.insertTile(CurrentTile, coords.getFirst(), coords.getSecond(), true);
//        CurrentTile = null;
//    }

    /**
     * Updates the player's current card to the specified new card.
     * If a card listener is set, it triggers a notification with the new card's details.
     *
     * @param NewCard The new Card to be assigned to the player.
     */
    public void setCard(Card NewCard){
        CurrentCard = NewCard;

        if (cardListner!=null){
            cardListner.newCard(new CardEvent(NewCard.getId()));
        }

    }


    /**
     * once a player is done building his ship (or the time is up), this method sets his starting position on the common board
     */
    public void EndConstruction() throws IllegalStateException{
        if(true)
            CommonBoard.SetStartingPosition(this);
        else throw new IllegalStateException("Called a lv 1 command in a lv 2 game!");
    }


    /**
     * Finalizes the player's construction phase and sets their starting position on the common game board.
     * This method ensures that the game state and level are appropriate for this action.
     *
     * @param index The index of the starting position to assign to the player.
     * @throws IllegalStateException if the game is not at level 2, as this action is only permitted in level 2 games.
     * @throws IllegalArgumentException if the specified index is out of bounds or the position is already taken.
     */
    public void EndConstruction(int index) throws IllegalStateException, IllegalArgumentException{
        if(getCommonBoard().getLevel() ==2)
            CommonBoard.SetStartingPosition(this, index);
        else throw new IllegalStateException("Called a lv 2 command in a lv 1 game!");
    }

    /**
     * Calculates the result for a player based on their completion status in the game.
     * If the race is finished, additional bonuses are added to the result.
     *
     * @param finished A boolean indicating whether the race has been completed by the player.
     *                 True if the race is finished, false otherwise.
     * @return The total result of the player, including bonuses if applicable.
     */
    public int CalculateResult(boolean finished){
        int result = 0;
        result =  getmyPlayerBoard().finishRace(finished);
        if(finished){
            result= result + this.getCommonBoard().arrivalBonus(this);

            result = result + this.getCommonBoard().beautyBonus(this);
        }
        return result;
    }


    /**
     * Completes the race for a player and determines the end-game state.
     * If the result is greater than 0, the player is considered successful; otherwise, unsuccessful.
     * Invokes the finishListener's `onEndGame` method to handle the end of the game.
     *
     * @param result an integer representing the player's performance or score in the race
     * @param message a string containing a descriptive message for the end-of-game status
     * @return the final result of the race as an integer
     */
    public int finishRace(int result, String message){
//        int result = 0;
//        result =  getmyPlayerBoard().finishRace(finished);
//        if(finished){
//            result= result + this.getCommonBoard().arrivalBonus(this);
//
//            result = result + this.getCommonBoard().beautyBonus(this);
//        }

        if (result > 0){
            finishListener.onEndGame(true, GetID(), message,null);
        }
        else{
            finishListener.onEndGame(false, GetID(), message,null);
        }
        return result;
    }


    public int finishRace(ScoreboardEvent scoreboardEvent, int result, String message){

        if (result > 0){
            finishListener.onEndGame(true, GetID(), message,scoreboardEvent);
        }
        else{
            finishListener.onEndGame(false, GetID(), message, scoreboardEvent);
        }
        return result;
    }



    /**
     * Updates the player's ready status and triggers the associated ready listener.
     * This method sets the player's "ready" state to the provided value and notifies
     * the ready listener to handle readiness-related actions.
     *
     * @param ready a boolean indicating the player's readiness status.
     *              True if the player is ready, false otherwise.
     */
    public void SetReady(boolean ready){
        this.ready = ready;
        this.readyListener.onReady();
    }


    /**
     * Updates the player's action state.
     *
     * @param hasActed A boolean indicating whether the player has performed an action.
     *                 True if the player has acted, false otherwise.
     */
    public void SetHasActed(boolean hasActed){
        this.HasActed = hasActed;
    }


    /**
     * Sets the ID of the player.
     *
     * @param id the unique identifier to be assigned to the player
     */
    public void setId(String id){this.ID = id;}


    /**
     * Sets the common game board for the player and initializes the player's individual board
     * based on the level of the provided common board.
     *
     * @param CommonBoard the GameBoard object representing the common game board to be set for the player
     */
    public void setBoards(GameBoard CommonBoard) {
        this.CommonBoard = CommonBoard;
        myPlayerBoard = new PlayerBoard(CommonBoard.getLevel());
    }



    /**
     * Retrieves the unique identifier (ID) of the player.
     *
     * @return the player's ID as a String
     */
    public String GetID() {return this.ID;}

    /**
     * Retrieves the player's readiness status.
     *
     * @return true if the player is ready, false otherwise.
     */
    public boolean GetReady() {return this.ready;}

    /**
     * Retrieves the player's action status indicating whether the player has performed an action.
     *
     * @return true if the player has acted, false otherwise
     */
    public boolean GetHasActed() {
        return HasActed;
    }

    /**
     * Retrieves the player's individual board configuration.
     *
     * @return the PlayerBoard object representing the player's specific board setup
     */
    public PlayerBoard getmyPlayerBoard() {return myPlayerBoard;}

    /**
     * Retrieves the player's current card.
     *
     * @return the current Card object assigned to the player, or null if no card is assigned.
     */
    public Card getCurrentCard() {
        return CurrentCard;
    }

    /**
     * Sets the hand listener for the player. This listener is responsible for responding to
     * changes or updates related to hand events. The specified listener will handle actions
     * triggered by hand events associated with the player.
     *
     * @param handListener the HandListener instance to be assigned to the player. This listener
     *                     will be notified of hand-related changes or events.
     */
    public void setHandListener(HandListener handListener) {
        this.handListener = handListener;
    }

    /**
     * Removes the hand listener associated with the player.
     * This method sets the player's hand listener reference to null,
     * effectively detaching any previously set hand listener.
     */
    public void removeHandListener(){
        this.handListener = null;
    }

    /**
     * Sets the card listener for the player. The card listener is responsible
     * for handling events related to card operations, such as new card
     * notifications or deck updates.
     *
     * @param cardListner the CardListner implementation to be set for the player.
     *                     This listener will handle card-related events.
     */
    public void setCardListner(CardListner cardListner) {
        this.cardListner = cardListner;
    }

    /**
     * Removes the card listener associated with the player.
     *
     * This method sets the `cardListner` field to `null`, effectively disabling any
     * notifications or functionality tied to the card listener. It is typically used
     * to clean up resources or reset the card listener when it is no longer needed.
     */
    public void removeCardListener(){
        this.cardListner = null;
    }

    /**
     * Removes the ready listener associated with the player.
     * This method sets the `readyListener` field to null, effectively
     * detaching any previously set listener for handling readiness-related actions.
     */
    public void removeReadyListener(){
        this.readyListener = null;
    }

    /**
     * Removes the finish listener associated with the player.
     * This method sets the `finishListener` field to null,
     * effectively disabling any finish-related event callbacks.
     */
    public void removeFinishListener(){
        this.finishListener = null;
    }

    /**
     * Checks whether debug mode has been called.
     *
     * @return true if debug mode was invoked, false otherwise.
     */
    public boolean isDebugCalled() {
        return debugCalled;
    }

    /**
     * Sets the debugCalled flag to the specified value.
     *
     * @param debugCalled the value to set for the debugCalled flag
     */
    public void setDebugCalled(boolean debugCalled) {
        this.debugCalled = debugCalled;
    }
}
