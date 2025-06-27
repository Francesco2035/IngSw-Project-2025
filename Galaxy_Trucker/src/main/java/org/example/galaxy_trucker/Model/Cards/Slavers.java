package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Exceptions.ImpossibleBoardChangeException;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Exceptions.WrongNumofEnergyExeption;
import org.example.galaxy_trucker.Exceptions.WrongNumofHumansException;
import org.example.galaxy_trucker.Model.Boards.Actions.KillCrewAction;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;

/**
 * Represents the "Slavers" card in the game. This card introduces a specific challenge for the players,
 * requiring them to meet certain requirements or face penalties. The card involves various interactions
 * with players, including applying effects, determining winners and losers, consuming energy, and dealing
 * with penalties.
 *
 * The card has a reward, a requirement to meet, and potential punishments for failure. Players take turns
 * attempting to meet the requirement, with their actions influencing the outcome of the card.
 *
 * Inherits from the {@code Card} class.
 */
public class Slavers extends Card{
    /**
     * Represents the minimum requirement or threshold that must be met or achieved
     * within the rules or mechanics of the game as defined by the Slavers card.
     * This variable is used to determine whether certain conditions or actions are valid
     * or triggered based on its value during gameplay.
     */
    private int requirement;
    /**
     * Represents the reward value associated with the Slavers card.
     * This variable determines the amount of reward a player receives
     * upon successfully completing the associated card effect.
     */
    private int reward;
    /**
     * Represents the punishment value associated with the "Slavers" card.
     * This variable defines the consequence imposed on the users
     * when specific conditions or actions are not met during gameplay.
     */
    private int Punishment;
    /**
     * Represents the ranking or sequence of the card's operations or events
     * within the `Slavers` class. This variable determines the specific order
     * in which certain actions or evaluations are processed. It is integral
     * to the functionality of determining the sequence of operations tied to
     * this card in the game's logic.
     */
    private int order;
    /**
     * Represents the current player associated with the Slavers card.
     * This variable keeps track of the player actively affected by or interacting with the card's functionalities.
     */
    private Player currentPlayer;
    /**
     * Indicates whether the Slavers have been defeated in the current state of the game.
     *
     * This variable is used to track the status of the Slavers during gameplay.
     * It is updated as part of the game mechanics to reflect the outcome of interactions
     * with the Slavers.
     */
    private boolean defeated;
    /**
     * Represents the current power level of the card or entity within the game.
     * This variable is used to track and evaluate the power level associated
     * with the card's state, which can influence its effects, energy usage,
     * and interactions with game elements. Changes in this value reflect the
     * dynamic state of the card during gameplay.
     */
    private double currentpower;
    /**
     * Represents the amount of energy consumed or required by the current instance
     * of the Slavers class during various operations or effects.
     * This variable is used to track and manage energy-related actions within the
     * game's mechanics, such as applying card effects or performing specific actions.
     */
    private  int energyUsage;
    /**
     * Represents a list of players who have been defeated or have lost in the game.
     * This variable is used to track and manage the players who are no longer active participants
     * due to specific game conditions or outcomes such as losing a battle or failing to meet certain objectives.
     */
    ArrayList<Player> losers;



    /**
     * Constructs a new Slavers object with the specified parameters.
     *
     * @param level        the level associated with the Slavers card
     * @param time         the time duration for which the Slavers card is active
     * @param board        the game board instance the card is interacting with
     * @param Reward       the reward granted upon satisfying the conditions
     * @param Requirement  the requirement needed to complete this card
     * @param Punsihment   the punishment or penalty for failing to meet the requirements
     */
    public Slavers(int level, int time, GameBoard board, int Reward, int Requirement, int Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.reward = Reward;
        this.Punishment = Punsihment;
        this.defeated = false;
        this.currentPlayer=new Player();
        this.order=0;
        this.currentpower=0;
        this.energyUsage=0;

    }

    /**
     * Sends a log event of type "Slavers" to all players currently present on the game board.
     *
     * This method retrieves the list of players from the game board and iterates through them.
     * For each player, it sends a random effect tied to a log event. Each log event uses the type
     * "Slavers" and default placeholder parameters (-1, -1, -1, -1) for additional information.
     *
     * The random effect for each player is sent by invoking the {@code sendRandomEffect} method,
     * which provides the targeted log event.
     */
    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Slavers",-1,-1,-1,-1));
        }
    }

    /**
     * Executes the effect of the card for the Slavers scenario.
     *
     * This method sets the default punishment for the card, initializes a list of losers,
     * and updates the states of all players to a "Waiting" state. It also triggers a state
     * update to apply the changes to the game environment.
     *
     * The method interacts with the game board to retrieve the current list of players
     * and modifies their states as part of the card's effect.
     */
    @Override
    public void CardEffect(){
        this.setDefaultPunishment(this.Punishment);
        losers = new ArrayList<>();
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(new Waiting());
        }
        this.updateStates();
    }


    /**
     * Updates the state for the current turn in the game logic. This method is responsible
     * for handling the transfer of control to the next player and altering the relevant
     * game states accordingly. If all players have completed their turns or a certain
     * condition is met, it concludes the card phase.
     *
     * The method performs the following steps:
     * 1. Retrieves the game board and the list of players.
     * 2. Checks if the current player's turn order is within the valid range and
     *    ensures the player is not defeated.
     * 3. Updates the state of the previous player to "Waiting".
     * 4. Assigns the next player in order as the current player and resets relevant
     *    attributes like attack power.
     * 5. Sets the state for the newly selected player to "GiveAttack".
     * 6. Increments the turn order to track progress through the player list.
     * 7. If the turn order exceeds the player list size or game conditions indicate
     *    the end, the card phase finishes by calling `finishCard`.
     */
    @Override
    public void updateStates(){
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        if(this.order<PlayerList.size() && !this.defeated){
            if (currentPlayer != null) {currentPlayer.setState(new Waiting());}

            currentPlayer = PlayerList.get(this.order);
            this.currentpower=0;
            PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();

            this.currentPlayer.setState(new GiveAttack());
            //this.currentPlayer.setInputHandler(new Accept(this));

            this.order++;
            System.out.println(this.order);
        }
        else{
            System.out.println("finita");
            this.finishCard();
        }
    }

    /**
     * Checks and updates the power and energy usage of the current player.
     * Depending on the number of doubles (`numofDouble`), it either checks
     * the player's strength directly or updates the player's state to consume energy.
     *
     * @param power        the power value to be checked and assigned to the current player
     * @param numofDouble  the number indicating whether to check strength (if 0)
     *                     or to update the player's state for consuming energy
     */
    @Override
    public void checkPower(double power, int numofDouble) {
        this.energyUsage = numofDouble;
        this.currentpower = power;
        if(numofDouble==0){
            this.checkStrength();
        }
        else {
            this.currentpower = power;
            this.currentPlayer.setState(new ConsumingEnergy());

//
        }
    }

    /**
     * Consumes energy from the game board based on the provided coordinates.
     *
     * This method verifies the provided coordinates before attempting to consume energy.
     * If the number of coordinates does not match the required energy usage for the card,
     * the player's state is updated, and an exception is thrown.
     * For each coordinate, the method attempts to perform an energy-consuming action,
     * handling invalid inputs gracefully by throwing an exception with a message indicating the issue.
     *
     * @param coordinates an ArrayList of IntegerPair objects representing the coordinates
     *                    from which energy will be consumed. Each IntegerPair contains
     *                    the row and column indexes of a specific tile.
     *
     * @throws IllegalArgumentException if the provided coordinates are null.
     * @throws WrongNumofEnergyExeption if the number of coordinates does not match the required energy usage
     *                                  or if one of the specified tiles does not contain energy to consume.
     */
    @Override
    public void consumeEnergy(ArrayList<IntegerPair> coordinates) {
        if (coordinates==null){
            throw new IllegalArgumentException("you must give coordinates to consumeEnergy");
        }
        if(coordinates.size()!=this.energyUsage){
            currentPlayer.setState(new GiveAttack());
            throw new WrongNumofEnergyExeption("wrong number of energy cells");
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
                currentPlayer.setState(new GiveAttack());

                throw new WrongNumofEnergyExeption("no energy cell in: "+i.getFirst()+" "+i.getSecond());
            }
        }
        this.checkStrength();

    }

    /**
     * Evaluates the current player's strength against the required threshold and updates their state
     * accordingly. The method determines whether the current player has exceeded the required strength,
     * fallen short, or equaled the requirement, and then transitions the player to the appropriate state.
     *
     * Behavior:
     * - If the current strength is greater than the requirement, the player is considered victorious.
     *   The player's state is set to "Accepting," and the player is marked as having defeated the challenge.
     * - If the current strength is less than the requirement, the player is considered to have lost.
     *   If the player's associated board has fewer humans than the specified punishment threshold, the player
     *   is added to a list of losers, and global states are updated. Otherwise, the player's state is set
     *   to "Killing."
     * - If the current strength equals the requirement, the player is considered even, and their state is
     *   set to "Waiting," with a subsequent update of global states.
     *
     * The method relies on several class-level fields and player-related methods to determine the current
     * player's state and make necessary updates to the game context.
     */
    public void checkStrength(){
            System.out.println("Checking strengthof: "+currentPlayer.GetID());
            System.out.println("strength of "+currentPlayer.GetID()+" is: "+this.currentpower+" required: "+this.requirement);
        if(this.currentpower>this.getRequirement()){
            System.out.println(currentPlayer.GetID()+" won");
            this.currentPlayer.setState(new Accepting());
            //this.currentPlayer.setInputHandler(new Accept(this));
            this.defeated=true;
            System.out.println("defeated");
        }
        else if(this.currentpower<this.getRequirement()){
            System.out.println(currentPlayer.GetID()+" lost");
            if(this.currentPlayer.getmyPlayerBoard().getNumHumans()<this.Punishment){
                losers.add(currentPlayer);
                this.updateStates();
                return;
            }


            this.currentPlayer.setState(new Killing());
            //this.currentPlayer.setInputHandler(new Killing(this));
        }
        else {
            System.out.println(currentPlayer.GetID()+" was even");
            this.currentPlayer.setState(new Waiting());
            this.updateStates();
        }

    }


    /**
     * Executes the action of "killing humans" for the provided set of board coordinates.
     * This method validates the number of coordinates against the expected punishment value,
     * performs the required action on each specified coordinate, and updates the game state.
     * If the number of coordinates provided does not match the expected punishment, or if an
     * invalid or impossible action is encountered during execution, the appropriate exceptions
     * are thrown.
     *
     * @param coordinates a list of {@code IntegerPair} objects representing the coordinates
     *                    where the "kill humans" action will be executed. Each pair denotes
     *                    a specific tile location on the player's game board.
     * @throws WrongNumofHumansException if the number of coordinates does not match the expected punishment.
     * @throws ImpossibleBoardChangeException if an invalid or impossible action is attempted (e.g., no humans left to kill).
     */
    @Override
    public void killHumans(ArrayList<IntegerPair> coordinates){

        if (coordinates.size() != this.Punishment) {
            throw new WrongNumofHumansException("wrong number of humans");
        }

        PlayerBoard curr= currentPlayer.getmyPlayerBoard();
        Tile tiles[][]=curr.getPlayerBoard();
        try{
            for (IntegerPair coordinate : coordinates) {
                System.out.println("killing humans in "+coordinate.getFirst()+" "+coordinate.getSecond());

                curr.performAction(tiles[coordinate.getFirst()][coordinate.getSecond()].getComponent(),new KillCrewAction(curr), new Killing());
            }
        }
        catch (Exception e){
            System.out.println("non ce sta più nessuno qui");
            throw new ImpossibleBoardChangeException("no more humans to kill");

        }
        this.updateStates();
    }


    /**
     * Executes the continuation logic for the Slavers card based on the player's decision.
     *
     * If the player accepts, the method grants the specified reward to the current player,
     * sends corresponding log events to all players on the board, and adjusts the player's position
     * on the board. If the player refuses, appropriate log events are sent to indicate the decision.
     * At the end of either scenario, the card is finalized by calling {@code finishCard()}.
     *
     * @param accepted a boolean value indicating the player's decision.
     *                 If {@code true}, the player has accepted the conditions of the card;
     *                 otherwise, {@code false}.
     */
    @Override
    public void continueCard(boolean accepted){
        if(accepted){


            ArrayList<Player> PlayerListMsg = this.getBoard().getPlayers();
            for(Player p : PlayerListMsg){
                if(p.GetID()== currentPlayer.GetID()){
                    this.sendRandomEffect(p.GetID(),new LogEvent("You have accepted to loot the slaves",-1,-1,-1,-1));
                }
                this.sendRandomEffect(p.GetID(),new LogEvent(currentPlayer.GetID()+" has accepted to loot the slavers",-1,-1,-1,-1));
            }


            currentPlayer.getmyPlayerBoard().setCredits(this.reward);

            this.getBoard().movePlayer(this.currentPlayer.GetID(), -this.getTime());
        }
        else{
            ArrayList<Player> PlayerListMsg = this.getBoard().getPlayers();
            for(Player p : PlayerListMsg){
                if(p.GetID()== currentPlayer.GetID()){
                    this.sendRandomEffect(p.GetID(),new LogEvent("You have refused to loot the slavers",-1,-1,-1,-1));
                }
                else {
                    this.sendRandomEffect(p.GetID(), new LogEvent(currentPlayer.GetID() + " has refused to loot the slavers", -1, -1, -1, -1));
                }
            }
        }

        this.finishCard();
    }


    /**
     * Completes the execution of the "Slavers" card and finalizes its effects.
     *
     * This method performs the following actions:
     * 1. Updates the state of all players on the game board to the "BaseState".
     * 2. Removes players from the current game as "losers" if they meet specific conditions.
     *    Specifically, this is done by invoking the {@code abandonRace} method on the game board
     *    for each player in the list of defeated players.
     * 3. Calls the {@code checkLosers()} method to process any additional updates or validations
     *    related to defeated players.
     * 4. Logs the finalization of the card by printing a message to the console.
     * 5. Marks the card as finished by setting its "finished" state to {@code true}.
     */
    @Override
    public void finishCard() {
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(new BaseState());
        }
        for(Player p: losers){
            getBoard().abandonRace(p, "No crew left",true);
        }
        checkLosers();
        System.out.println("card finished");
        this.setFinished(true);
    }

    /**
     * Retrieves the current player in the game.
     *
     * @return the {@code Player} object representing the current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //json required

    /**
     * Default constructor for the Slavers class.
     *
     * This constructor initializes an instance of the Slavers class without any
     * parameters. It sets up the default state of the Slavers card, enabling it
     * to be used in the game logic.
     */
    public Slavers(){}
    /**
     * Retrieves the default punishment value for the Slavers card.
     *
     * @return the integer value representing the default punishment associated with this card.
     */
    public int getDefaultPunishment() {return Punishment;}
    /**
     * Sets the punishment value for the Slavers card.
     *
     * @param punishment the punishment or penalty value to be applied when the requirements
     *                   of the card are not met.
     */
    public void setPunishment(int punishment) {Punishment = punishment;}
    /**
     * Retrieves the reward associated with the Slavers card.
     *
     * @return the reward value as an integer.
     */
    public int getReward() {return reward;}
    /**
     * Sets the reward for the current Slavers card.
     *
     * @param reward the reward value to be assigned, representing the benefit
     *               granted upon satisfying the conditions of the card.
     */
    public void setReward(int reward) {this.reward = reward;}
    /**
     * Retrieves the requirement value for the Slavers card.
     *
     * @return the integer representing the requirement needed to complete this card.
     */
    public int getRequirement() {return requirement;}
    /**
     * Sets the requirement needed to complete this card.
     *
     * @param requirement an integer specifying the required threshold or condition
     *                    that must be met to fulfill the card's objective.
     */
    public void setRequirement(int requirement) {this.requirement = requirement;}


}
