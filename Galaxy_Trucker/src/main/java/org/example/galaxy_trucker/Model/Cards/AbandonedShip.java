package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Exceptions.ImpossibleBoardChangeException;
import org.example.galaxy_trucker.Exceptions.WrongNumofHumansException;
import org.example.galaxy_trucker.Model.Boards.Actions.KillCrewAction;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.Accepting;
import org.example.galaxy_trucker.Model.PlayerStates.Killing;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;

import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;



/**
 * The AbandonedShip class is a type of Card that implements specific effects
 * within a game. It extends the basic functionality provided by the Card class.
 * This card requires players to meet certain conditions in order to succeed,
 * and failure to do so can result in penalties.
 */
public class AbandonedShip extends Card{
    /**
     * Represents the minimum human crew requirement for interaction with the abandoned ship card.
     * This value indicates the number of humans necessary to proceed with specific actions
     * or events related to the abandoned ship card.
     */
    private int requirement;
    /**
     * Represents the reward points awarded to the player for successfully completing
     * the encounter or task associated with the card. The value of this variable
     * is set when the card is initialized and can be modified during the game.
     * It typically reflects the motivation for players to attempt or complete
     * the actions required by the card.
     */
    private int reward;
    /**
     * Represents the player who is currently interacting or taking actions with
     * respect to the AbandonedShip card. This variable tracks the active player
     * in the context of the card's effects and gameplay logic.
     */
    private Player currentPlayer;
    /**
     * Indicates the state of the abandoned ship, often used to track specific
     * conditions or transitions related to this card's gameplay mechanics.
     * This variable can represent whether a particular event has occurred
     * or a specific flag is set within the state of the card.
     */
    private boolean flag;
    /**
     * Represents the order in which the card effects are executed or processed.
     * This variable may be used to sequence or prioritize specific actions or events
     * within the context of the game logic.
     */
    private int order;
    /**
     * Represents the total number of human crew members on the abandoned ship.
     * This variable tracks the current amount of humans available in the ship,
     * which may be influenced by different card effects or game mechanics.
     */
    private int totHumans;
    /**
     * A list of players who failed to meet the requirements or objectives
     * associated with the current card effect or scenario.
     *
     * This field is used to keep track of the players who are considered
     * losers in the context of the game logic defined by the AbandonedShip class.
     */
    private ArrayList<Player> losers;

    /**
     * Sends a "Type Log" event to all players currently in the game.
     * This method retrieves the list of players from the game board and iterates over them,
     * sending a randomized "Abandoned Ship"*/
    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Abandoned Ship",-1,-1,-1,-1));
        }
    }


    /**
     * Constructs an AbandonedShip card with specified requirements, rewards, level, time, and game board.
     *
     * @param requirement the requirement needed to complete the abandoned ship scenario
     * @param reward the reward given upon completing*/
    public AbandonedShip(int requirement, int reward, int level, int time, GameBoard board) {
        super(level, time, board);
        this.requirement = requirement;
        this.reward = reward;
        this.currentPlayer = new Player();
        this.flag = false;
        this.order = 0;
        totHumans=0;
    }

    /**
     * Executes the effect of the "Abandoned Ship" card. This method is responsible for:
     *
     * 1. Setting the default punishment value for the card using the {@code setDefaultPunishment} method,
     *    initialized with the card's requirement value.
     * 2. Initializing a list to track players who fail to meet the card's conditions.
     * 3. Transitioning all players in the game to the "Waiting" state.
     * 4. Introducing a pause in execution for a specified duration using {@code Thread.sleep}.
     * 5. Updating the game states after the delay by invoking {@code updateStates}.
     *
     * @throws InterruptedException if the thread's sleep is interrupted during execution.
     */
    @Override
    public void CardEffect() throws InterruptedException {
        this.setDefaultPunishment(this.requirement);
        losers = new ArrayList<>();
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(new Waiting());
        }
        Thread.sleep(3000);
        this.updateStates();
    }
    /**
     * Updates the state of the game based on the current state of the "Abandoned Ship" card.
     *
     * This method iterates over the list of players in the game to determine the next steps
     * for the card's progression. It performs the following operations:
     *
     * 1. Retrieves the game board and the list of players currently in the game.
     * 2. Iterates through the players based on the card's internal order, stopping
     *    either when the order exceeds the player list size or a specific condition is met.
     * 3. Changes the state of the current player to "Waiting" and moves to the next player.
     * 4. Retrieves the current player's board status, checks the number of humans present,
     *    and determines if the player satisfies the requirement for passing the "Abandoned Ship" card.
     * 5. If a player meets or exceeds the required number of humans, sets the player's state to "Accepting"
     *    and assigns the card to the player, marking the card as accepted.
     * 6. If all players have been processed without triggering the acceptance condition,
     *    marks the card as finished by calling the {@code finishCard} method.
     */
    @Override
    public void updateStates(){
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        while(this.order<=PlayerList.size()&& !this.flag) {
            if(order==PlayerList.size()){
                this.finishCard();
                break;
            }
            if (currentPlayer != null) {currentPlayer.setState(new Waiting());}
            currentPlayer = PlayerList.get(this.order);
            PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();
            System.out.println("Checking: "+currentPlayer.GetID());

            int NumHumans= CurrentPlanche.getNumHumans();
            if(CurrentPlanche.isBrownAlien()){
                NumHumans++;
            }
            if (CurrentPlanche.isPurpleAlien()){
                NumHumans++;
            }
            if(NumHumans>=requirement){ //TODO: capire se maggiore o maggiore uguale
                this.totHumans=CurrentPlanche.getNumHumans();
                System.out.println(currentPlayer.GetID()+" has enough required housing");
                this.flag = true;
                currentPlayer.setState(new Accepting());
                //currentPlayer.setInputHandler(new AcceptKilling(this));
                currentPlayer.setCard(this);
            }

            this.order++;
        }

    }

    /**
     * Handles the continuation of the "Abandoned Ship" card based on whether the card has been accepted or not.
     * If accepted, applies a random effect to all players and transitions the current player to the "Killing" state.
     * If not accepted, transitions the current player to the "Waiting" state and updates the states of the game.
     *
     * @param accepted a boolean value indicating whether the card has been accepted (true) or refused (false).
     */
    @Override
    public void continueCard(boolean accepted) {


        if(accepted){

            ArrayList<Player> PlayerList = this.getBoard().getPlayers();
            for(Player p : PlayerList) {
                if (p.GetID() == currentPlayer.GetID()) {
                    this.sendRandomEffect(p.GetID(), new LogEvent("You have accepted to board the ship", -1, -1, -1, -1));
                }
                else{
                     this.sendRandomEffect(p.GetID(), new LogEvent(currentPlayer.GetID() + " has accepted to board the ship", -1, -1, -1, -1));
                }
            }
            System.out.println(currentPlayer.GetID()+" has accepted");
            this.currentPlayer.setState(new Killing());
        }
        else{
            System.out.println(currentPlayer.GetID()+" has refused");
            currentPlayer.setState(new Waiting());
            this.flag = false;
            this.updateStates();
        }
    }

    /**
     * Marks the current "AbandonedShip" card as finished by performing the following steps:
     *
     * 1. Prints a message indicating the card is finished.
     * 2. Invokes the {@code checkLosers()} method to determine and handle any players
     *    who lost based on the game's criteria.
     * 3. Sets the card's finished state to true by calling {@code setFinished(true)}.
     */
    @Override
    public void finishCard() {
        System.out.println("card finished");
        checkLosers();

        this.setFinished(true);

    }

    /**
     * Executes the logic to "kill" humans located at specified coordinates on the player's board.
     * This method validates the input, applies the action, and updates the game state accordingly.
     * If the number of coordinates provided does not match the required number or
     * an error occurs during the operation, an exception will be thrown.
     *
     * @param coordinates the list of {@code IntegerPair} objects representing the positions
     *                    of the humans to be "killed" on the player's board.
     *                    Each {@code IntegerPair} contains the row and column indices of a target tile.
     * @throws WrongNumofHumansException if the number of coordinates supplied is incorrect.
     * @throws ImpossibleBoardChangeException if an error occurs while attempting to modify the board state.
     */
    @Override
    public void killHumans (ArrayList<IntegerPair> coordinates) {
        if(coordinates!=null) {
            if (coordinates.size() != this.requirement) {
                //devo dirgli che ha scelto il num sbagliato di persone da shottare
                this.currentPlayer.setState(new Accepting());
                System.out.println( "numofHumans given: " +coordinates.size());
                throw new WrongNumofHumansException("wrong number of humans");
            }

            ///  fai l try catch e opera sulla copia :)
            PlayerBoard curr= currentPlayer.getmyPlayerBoard();
            Tile tiles[][]=curr.getPlayerBoard();
            try{
            for (IntegerPair coordinate : coordinates) {
                System.out.println("killing humans in "+coordinate.getFirst()+" "+coordinate.getSecond());

                curr.performAction(tiles[coordinate.getFirst()][coordinate.getSecond()].getComponent(),new KillCrewAction(curr), new Killing());
            }
            }
            catch (Exception e){
                //devo rimanere allo stato di dare gli umani ezzz
                System.out.println("non ce sta più nessuno qui");
                this.currentPlayer.setState(new Accepting());
                throw new ImpossibleBoardChangeException("there was an error in killing humans");

            }
            //lo vedi?
            currentPlayer.getmyPlayerBoard().setCredits(this.reward);
            this.getBoard().movePlayer(this.currentPlayer.GetID(), -this.getTime());


            if(currentPlayer.getmyPlayerBoard().getNumHumans()==0){
                losers.add(currentPlayer);
            }
            this.finishCard();
        }
//        else{
//            currentPlayer.setState(new Waiting());
//            this.flag = false;
//            this.updateSates();
//        }
    }

    /**
     * Retrieves the total number of humans currently present on the abandoned ship.
     *
     * @return the total number of humans as an integer
     */
    public int getTotHumans() {
        return totHumans;
    }

    /**
     * Retrieves the current player associated with the game or card context.
     *
     * @return the current player object.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    /**
     * Default constructor for the AbandonedShip class.
     * This constructor initializes an instance of the AbandonedShip card
     * without specifying any initial parameters.
     * It serves as a no-arguments constructor that can be used to instantiate
     * the object with default values.
     */
    public AbandonedShip() {}
    /**
     * Retrieves the requirement value associated with the AbandonedShip card.
     *
     * @return the requirement needed to complete the abandoned ship scenario
     */
    public int getRequirement() {return requirement;}
    /**
     * Sets the requirement value for the AbandonedShip scenario.
     *
     * @param requirement the integer value representing the requirement to complete the scenario
     */
    public void setRequirement(int requirement) {this.requirement = requirement;}
    /**
     * Retrieves the reward associated with the AbandonedShip card.
     *
     * @return the reward value as an integer
     */
    public int getReward() {return reward;}
    /**
     * Sets the reward value for the AbandonedShip card.
     *
     * @param reward the reward value to be assigned
     */
    public void setReward(int reward) {this.reward = reward;}
}
