package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Exceptions.WrongNumofEnergyExeption;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates.*;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;

/**
 * OpenSpace is a specific type of card in the game, which involves gameplay mechanics
 * such as player movement, energy consumption, and determining player states.
 * This class is responsible for managing the interactions of players with the OpenSpace.
 */
public class OpenSpace extends Card{
    /**
     * Represents the current active player in the game. This variable keeps track
     * of whose turn it is during gameplay and can be used to manage game logic
     * related to the active player.
     */
    private Player currentPlayer;
    /**
     * Represents the order of this OpenSpace instance in the sequence of gameplay or actions.
     * This variable is used to determine the play order or progression within the game logic.
     */
    private int order;
    /**
     * Represents the current movement value of the player in the game.
     * This variable is used to track the number of movements the player
     * can or has performed during their turn in the OpenSpace class.
     * It is influenced by game mechanics, such as engine power and
     * certain card effects.
     */
    private int currentmovement;
    /**
     * Represents the amount of energy used during a player's movement or action
     * in the OpenSpace game context. Tracks energy consumption based on gameplay
     * mechanics and player decisions.
     */
    private int energyUsage;
    /**
     * Represents a list of players who have lost in the game.
     * Used to maintain and manage the players who are no longer active participants.
     */
    private ArrayList<Player> losers;


    /**
     * Sends a log event of type "Open space" to all players currently on the game board.
     *
     * This method retrieves all the players participating in the game from the
     * associated game board. For each player, it sends a randomly generated effect
     * represented by a {@code LogEvent} with predefined parameters:
     * event name as "Open space" and all numerical values as -1.
     */
    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Open space",-1,-1,-1,-1));
        }
    }


    /**
     * Constructs an OpenSpace object representing a space with no specific attributes or effects.
     * The OpenSpace is initialized with the specified level and GameBoard, and sets up
     * the default values for its state and associated player.
     *
     * @param level the level of the OpenSpace, indicating its position or importance in the game
     * @param board the GameBoard instance to which this OpenSpace is associated
     */
    public OpenSpace(int level, GameBoard board){

        super(level, 0 ,board);
        this.order = 0;
        this.currentPlayer = new Player();
        this.energyUsage = 0;
        this.currentmovement = 0;
    }

    /**
     * Executes the effect specified for the card associated with this space.
     * This method primarily sets each player's state to 'Waiting' and updates
     * their statuses accordingly. It also initializes an internal list to
     * track players who lose the game based on the effect implementation.
     *
     * The method retrieves the game board and the current list of players,
     * iterating through them to adjust their state. After modifying the
     * player states, it triggers an update to reflect these changes across
     * the game system.
     */
    @Override
    public void CardEffect(){
        losers = new ArrayList<>();
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(new Waiting());
        }
        this.updateStates();
    }

    /**
     * Updates the current state of the OpenSpace card and its related players.
     *
     * The updateStates method determines the current player's turn, updates player states,
     * and manages the internal order of player turns. It interacts with the associated GameBoard
     * and the list of players to transition the state of the current player. When the order
     * of processing reaches the end of the player list, the card's actions are finalized.
     *
     * Functional behavior includes:
     * - Retrieving the GameBoard and its player list.
     * - If the order is within the bounds of player list size, transitions the previous player
     *   to a Waiting state and sets the current player to a GiveSpeed state.
     * - Increments the turn order.
     * - When all players have been processed, completes the card's action using finishCard.
     */
    @Override
    public void updateStates(){
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        currentmovement=0;
        if(this.order<PlayerList.size()){
            if (currentPlayer != null) {currentPlayer.setState(new Waiting());}
            currentPlayer = PlayerList.get(this.order);
            PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();

                this.currentPlayer.setState(new GiveSpeed());
                //this.currentPlayer.setInputHandler(new GiveSpeed(this));

            this.order++;
        }
        else{
            this.finishCard();
        }
    }

    /**
     * Ends the current card's effect and updates the game state accordingly.
     *
     * The method performs the following actions:
     * - Retrieves the current game board and the list of players in the game.
     * - Resets each player's state to a default or "base" state.
     * - For each player in the losers list, triggers an abandonment of the game or race
     *   with a specified reason and sets them to inactive for the current round.
     * - Checks for and processes any remaining losers that require handling.
     * - Sets the status of the current card to finished, indicating that its effect
     *   has been fully processed.
     */
    @Override
    public void finishCard() {
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(new BaseState());
        }

        for(Player p: losers){
            getBoard().abandonRace(p, "You don't have engine power",true);
        }

        checkLosers();


        this.setFinished(true);
    }

    /**
     * Verifies and processes the movement of the current player based on provided engine power
     * and energy usage values. This method determines the player's movement outcome, updates
     * relevant attributes such as energy usage and movement, and transitions the player's state
     * accordingly.
     *
     * @param enginePower the power value determining the player's movement capability
     * @param numofDouble the energy usage or penalties affecting the player's movement
     */
    @Override
    public void checkMovement(int enginePower, int numofDouble) {
            System.out.println("checkMovement of "+currentPlayer.GetID()+" "+enginePower+" "+numofDouble);
            this.currentmovement=enginePower;
            this.energyUsage=numofDouble;
            this.setDefaultPunishment(this.energyUsage);
            if(this.energyUsage==0){
                this.moveplayer();
            }
            else{

                this.currentPlayer.setState(new ConsumingEnergy());
            }

    }

    /**
     * Consumes energy based on the provided coordinates, performing actions associated
     * with the tiles at the specified locations. If the number of coordinates is not
     * equal to the required energy usage or invalid input is detected, exceptions are thrown
     * and the current player's state is updated to handle the situation.
     *
     * @param coordinates a list of IntegerPair objects representing the positions of energy
     *                    cells to be consumed. Each pair contains the row and column indexes
     *                    of the target tiles.
     * @throws IllegalArgumentException if the coordinates list is null.
     * @throws WrongNumofEnergyExeption if the number of energy cells provided does not match
     *                                  the required energy usage or if an invalid input is found.
     */
    @Override
    public void consumeEnergy(ArrayList<IntegerPair> coordinates) {
        if (coordinates==null){
            throw new IllegalArgumentException("you must give coordinates to consumeEnergy");
        }
        if(coordinates.size()!=this.energyUsage){
            currentPlayer.setState(new GiveSpeed());
            throw new WrongNumofEnergyExeption("wrong number of enrgy cells");
        }
        PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();
        Tile[][] tiles = CurrentPlanche.getPlayerBoard();
        /// opero sulla copia
        for(IntegerPair i:coordinates){
            try {
                CurrentPlanche.performAction(tiles[i.getFirst()][i.getSecond()].getComponent(),
                        new UseEnergyAction(CurrentPlanche), new ConsumingEnergy());
            }
            catch (InvalidInput e){
                currentPlayer.setState(new GiveSpeed());
                throw new WrongNumofEnergyExeption("there was no energy to use in:"+i.getFirst()+" "+i.getSecond());
            }
        }
        this.moveplayer();
    }

    /**
     * Moves the current player based on the value of {@code currentmovement}.
     *
     * If {@code currentmovement} equals zero, the current player is added to the losers' list,
     * their state is set to {@code Waiting}, and the game states are updated.
     *
     * Otherwise, the player is moved on the game board by invoking the {@code movePlayer}
     * method with the player's ID and {@code currentmovement} value.
     * After the movement, the player's state is set to {@code Waiting}, and the game states are updated accordingly.
     */
    public void moveplayer(){
        if(currentmovement==0){
            losers.add(this.currentPlayer);
            this.currentPlayer.setState(new Waiting());
            this.updateStates();
        }
        else {
            getBoard().movePlayer(currentPlayer.GetID(),currentmovement);
            this.currentPlayer.setState(new Waiting());
        this.updateStates();
        }
    }


    /**
     * Retrieves the current player associated with the OpenSpace object.
     *
     * @return the player currently interacting with or assigned to the OpenSpace instance
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Constructs an OpenSpace object, representing a game space with no specific
     * attributes or initial effects.
     *
     * This default constructor initializes an OpenSpace instance, setting up
     * the default internal state of the space while requiring further configuration
     * or association during gameplay. Primarily used for situations where no specific
     * parameters are required during the object's creation.
     *
     * Note: This constructor is marked as requiring JSON serialization/deserialization
     * compatibility and is intended for use in systems where JSON support for OpenSpace
     * instances is utilized or necessary.
     */
    //json required
    public OpenSpace() {}
}

