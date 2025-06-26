


package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Exceptions.WrongNumofEnergyExeption;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.Tiles.PowerCenter;
import org.example.galaxy_trucker.Model.Tiles.Storage;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.min;
import static java.util.Collections.max;

/**
 * The Smugglers class represents a specific type of card in the game. It extends the Card class
 * and implements game mechanics involving smugglers, including reward distribution, punishment handling,
 * and state transitions.
 *
 * The card interacts with the players on the game board to check their power, enforce penalties,
 * and reward successful interactions. The card maintains states for the players and requires specific amounts
 * of energy to deal with its effects. It also has mechanisms for energy consumption and handling cargo actions.
 */
public class Smugglers extends Card{
    /**
     * Represents the minimum requirement or threshold that needs to be met
     * within the game objectives. This value is used to determine if specific
     * conditions associated with the Smugglers class or gameplay interactions
     * have been satisfied. It may directly influence the gameplay logic and
     * player progression.
     */
    private int requirement;
    /**
     * Represents the list of goods rewarded to the player(s) as part of the game mechanics.
     *
     * The `rewardGoods` variable holds an array list of `Goods` objects, where each object
     * corresponds to specific goods (e.g., BLUE, GREEN, YELLOW, RED) that can be earned
     * during gameplay. These goods provide value to the player(s) and are determined by
     * the specific game rules.
     *
     * This field is serialized and deserialized for JSON purposes using the
     * `@JsonProperty` annotation, ensuring smooth integration with external systems.
     */
    @JsonProperty("rewardGoods")
    private ArrayList<Goods> rewardGoods;
    /**
     * Represents the punishment value associated with a game card or event in the Smugglers class.
     * This variable typically defines the penalty or negative consequence imposed on a player
     * when certain conditions or rules are met within the gameplay.
     */
    private int Punishment;
    /**
     * Represents a temporary punishment value used within the Smugglers class.
     * This variable is likely used to store a punishment value to be applied or processed
     * during certain game actions or states, potentially as a short-term or intermediate state
     * for penalty handling in the gameplay logic.
     */
    private int tmpPunishment;
    /**
     * Represents the player's order or turn within the game.
     * This variable determines the sequence in which players interact or take actions.
     * Managed within the Smugglers class to handle gameplay dynamics and player progression.
     */
    private int order;
    /**
     * Represents the player currently taking their turn in the game.
     *
     * This variable is responsible for storing the current active player
     * during the gameplay, allowing the game logic to track whose turn it is.
     * It is updated as the game progresses to reflect the active player's state.
     */
    private Player currentPlayer;
    /**
     * Represents the state indicating whether the Smugglers have been defeated.
     * This variable is used to track the outcome of interactions or actions
     * involving the Smugglers.
     */
    private boolean defeated;
    /**
     * Represents the current power level of the Smugglers.
     * This variable keeps track of the energy or resources associated with
     * the ongoing operations during gameplay. It is updated and utilized
     * within various game mechanics such as checking power levels, consuming
     * energy, and determining gameplay outcomes.
     */
    private double currentpower;
    /**
     * Represents the energy usage level required by the smugglers during gameplay.
     * This value is used to track or measure the amount of energy consumed during specific actions or events.
     * It plays a critical role in determining the efficiency and operational capacity of the smugglers.
     */
    private int energyUsage;
    /**
     * Indicates whether a penalty or punishment is currently applicable in the context of the game.
     *
     * This variable is used to track the state of punishment when certain conditions are met during
     * gameplay. It helps manage game logic by determining if specific punitive actions need to
     * occur for the current player or scenario.
     */
    private boolean isaPunishment;

    /**
     * Represents a collection of players who have lost in the current game context.
     * This list is used to track players who have been defeated or eliminated during gameplay.
     */
    ArrayList<Player> losers;


    /**
     * Constructs a Smugglers card with the specified level, time, game board, reward, requirement, and punishment criteria.
     *
     * @param level the difficulty level of the Smugglers card
     * @param time the time constraint for the Smugglers card
     * @param board the current game board associated with the Smugglers card
     * @param Reward the list of goods offered as a reward for defeating the smugglers
     * @param Requirement the minimum strength or power required to defeat the smugglers
     * @param Punsihment the penalty incurred if the smugglers are not defeated
     */
    Smugglers(int level, int time, GameBoard board, ArrayList<Goods> Reward, int Requirement, int Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.rewardGoods= Reward;
        this.Punishment = Punsihment;
        this.defeated=false;
        this.currentPlayer=new Player();
        this.order=0;
        this.tmpPunishment=0;
        this.energyUsage=0;
        this.isaPunishment=false;
    }

    /**
     * Sends a log event of type "Smugglers" to all players currently present in the game.
     *
     * This method retrieves the current list of players from the game board associated
     * with this card and then iterates over each player. For each player, it sends a
     * random effect defined by a {@code LogEvent} object containing details about the
     * "Smugglers" event.
     *
     * The {@code LogEvent} created here uses default parameter values of -1 for all
     * numeric fields except the event type, which is set to "Smugglers".
     */
    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Smugglers",-1,-1,-1,-1));
        }
    }

    /**
     * Executes the effect of the Smugglers card within the game context.
     *
     * This method initializes a list to keep track of players who lose in the associated round.
     * It retrieves the game board and the list of players currently participating in the game.
     * Each player's state is updated to a Waiting state, representing early steps in resolving the card effect.
     * The method then invokes the updateStates() method to apply additional state transitions or updates
     * required after this stage of processing.
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
     * Updates the state of the Smugglers card and its associated player actions in the game.
     *
     * This method transitions the game's state by iterating over the players on the game board.
     * It checks if the Smugglers card is still in an active phase by verifying the current player order
     * and whether the card's condition of being defeated has been met.
     *
     * If the card is in an active state:
     * - Sets the previously active player (if any) to a "Waiting" state.
     * - Selects the next player from the list of players on the game board.
     * - Initializes the player's board and sets their state to "GiveAttack".
     * - Increments the order to move to the next player.
     *
     * If the card has progressed through all players or has been defeated:
     * - Calls the finishCard() method to finalize the Smugglers card's effect.
     */
    @Override
    public void updateStates(){
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        if(this.order<PlayerList.size() && !this.defeated){
            if (currentPlayer != null) {currentPlayer.setState(new Waiting());}

            currentPlayer = PlayerList.get(this.order);
            PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();
            this.currentpower=0;
            this.currentPlayer.setState(new GiveAttack());
            //this.currentPlayer.setInputHandler(new Accept(this));

            this.order++;
        }
        else{
            this.finishCard();
        }
    }

    /**
     * Validates the power level and updates the state of the current player based on energy usage.
     *
     * @param power the current power value to be checked
     * @param numofDouble the number of power-doubling instances or values affecting the energy usage
     */
    @Override
    public void checkPower(double power, int numofDouble) {
        this.currentpower = power;
        this.energyUsage = numofDouble;
        if(numofDouble==0){
            this.checkStrength();
        }
        else {

            this.currentPlayer.setState(new ConsumingEnergy());
//
        }

    }


    /**
     * Consumes the energy from specific coordinates on the player's board,
     * updating their game state based on the available energy and current conditions.
     *
     * This method validates the given coordinates, ensures the number of energy cells
     * matches the expected usage, and executes energy consumption actions. If the conditions
     * of the operation are not met or invalid inputs are encountered, the player's state
     * is adjusted accordingly, and relevant exceptions are thrown.
     *
     * @param coordinates the list of {@code IntegerPair} objects representing
     *                    the coordinates where energy consumption is intended.
     *                    Each {@code IntegerPair} specifies a (row, column) position on the board.
     *                    Must not be {@code null} and must provide the correct count
     *                    of energy cells.
     * @throws IllegalArgumentException if the {@code coordinates} parameter is {@code null}.
     * @throws WrongNumofEnergyExeption if the number of provided energy cells does not match
     *                                  the required energy usage, or if the operation
     *                                  encounters invalid energy inputs at the specified tiles.
     */
    @Override
    public void consumeEnergy(ArrayList<IntegerPair> coordinates) {
        if (coordinates==null){
            throw new IllegalArgumentException("you must give coordinates to consumeEnergy");
        }
        if(isaPunishment){
            this.consumeEnergy2(coordinates);
            return;
        }
        if(coordinates.size()!=this.energyUsage){
            currentPlayer.setState(new GiveAttack());
            throw new WrongNumofEnergyExeption("wrong number of energy cells");
        }
        PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();
        Tile[][] tiles = CurrentPlanche.getPlayerBoard();
        for(IntegerPair i:coordinates){
            try {
                CurrentPlanche.performAction(tiles[i.getFirst()][i.getSecond()].getComponent(),
                        new UseEnergyAction(CurrentPlanche), new ConsumingEnergy());
            }
            catch (InvalidInput e){
                currentPlayer.setState(new GiveAttack());
                throw new WrongNumofEnergyExeption("wrong number of energy cells");
            }
        }
        this.checkStrength();

    }



    /**
     * Consumes energy from specified coordinates on the current player's game board.
     *
     * This method validates the number of specified coordinates against the required energy usage.
     * If the validation fails, it throws a {@code WrongNumofEnergyExeption}. For each coordinate,
     * it attempts to perform an energy-related action. If the action cannot be performed
     * due to invalid input or no energy being present at the specified location, another
     * {@code WrongNumofEnergyExeption} is thrown. Successful execution updates
     * the game state and disables the punishment flag.
     *
     * @param coordinates a list of {@code IntegerPair} objects representing the coordinates
     *                    on the game board from where energy will be consumed. The size of
     *                    the list must match the predefined energy usage requirement.
     * @throws WrongNumofEnergyExeption if the number of coordinates provided does
     *                                  not match the required energy usage or
     *                                  if no energy is present at any specified coordinate.
     */
    public void consumeEnergy2(ArrayList<IntegerPair> coordinates) {

        if(coordinates.size()!=this.energyUsage){
            System.out.println("----->"+this.energyUsage);
            throw new WrongNumofEnergyExeption("wrong number of energy cells to steal you had to give "+this.energyUsage+" energy cells, you gave "+coordinates.size());
        }
        PlayerBoard CurrentPlanche = currentPlayer.getmyPlayerBoard();
        Tile[][] tiles = CurrentPlanche.getPlayerBoard();
        // opero sulla copia
        for(IntegerPair i:coordinates){
            try {
                CurrentPlanche.performAction(tiles[i.getFirst()][i.getSecond()].getComponent(),
                        new UseEnergyAction(CurrentPlanche), new ConsumingEnergy());
            }
            catch (InvalidInput e){

                throw new WrongNumofEnergyExeption("no energy here to consume");
            }
        }
        this.isaPunishment=false;
        this.updateStates();


    }



    /**
     * Evaluates the strengths and resources of the current player against the smugglers' requirements
     * to determine the outcome of the encounter. This method governs the transition of the player's
     * state based on the outcome and handles subsequent actions, such as penalties or rewards.
     *
     * The method performs the following logic:
     * - If the current player's power exceeds the smugglers' requirement:
     *   - Sets the player's state to {@code Accepting}.
     *   - Marks the smugglers as defeated and prints a notification.
     * - If the current player's power is less than the smugglers' requirement:
     *   - Handles potential penalties depending on the player's resources:
     *     - If goods are stored on the player's board, adjusts punishment and transitions to the
     *       theft-handling state.
     *     - If no goods are found, calculates energy penalties based on the available energy, updates
     *       the punishment, and transitions to a state where energy is consumed.
     * - If the current player's power matches the smugglers' requirement:
     *   - Sets the player's state to {@code Waiting} and updates the game state.
     *
     * Responsibilities:
     * - Compare the player's current power with the smugglers' requirement.
     * - Update the player's state based on the outcome.
     * - Handle associated penalties or rewards, including energy consumption and theft handling.
     */
    public void checkStrength(){


        if(this.currentpower>this.getRequirement()){


            this.currentPlayer.setState(new Accepting());
            //this.currentPlayer.setInputHandler(new Accept(this));
            this.defeated=true;
            System.out.println("defeated");

        }
        else if(this.currentpower<this.getRequirement()) {

            System.out.println("hai perso :3");
            PlayerBoard CurrentPlanche = currentPlayer.getmyPlayerBoard();
            HashMap<Integer, ArrayList<IntegerPair>> cargoH = CurrentPlanche.getStoredGoods();
            this.tmpPunishment = Punishment;
            if (!cargoH.isEmpty()) {

                this.setDefaultPunishment(tmpPunishment);
                this.currentPlayer.setState(new HandleTheft());

             }
            else {
                System.out.println("no goods found");
                this.isaPunishment = true;
                int totenergy=0;
                for(PowerCenter i: CurrentPlanche.getPowerCenters() ){
                    totenergy+=i.getType();
                    System.out.println( "tot energy:" +totenergy);
                }


                energyUsage = min(tmpPunishment, CurrentPlanche.getEnergy());
                System.out.println("energy usage (Check Strength):" +energyUsage);
                this.setDefaultPunishment(this.energyUsage);
                int test = this.getDefaultPunishment();
                if (test ==2 ){System.out.println("sgr");}
                currentPlayer.setState(new ConsumingEnergy());
            }

        }
        else {
            this.currentPlayer.setState(new Waiting());
            this.updateStates();
        }

    }



    /**
     * Continues the Smugglers card interaction based on whether the current
     * player accepts or refuses to loot the smugglers.
     *
     * If the player accepts, the player is transitioned to a new state
     * (HandleCargo), rewards are assigned, and all players are notified
     * about the player's decision.
     * If the player refuses, the card is finalized, and all players are
     * notified about the refusal.
     *
     * @param accepted a boolean indicating whether the current player has
     *                 accepted to loot the smugglers (true) or refused (false)
     */
    @Override
    public void continueCard(boolean accepted){
        if(accepted){

            ArrayList<Player> PlayerListMsg = this.getBoard().getPlayers();
            for(Player p : PlayerListMsg){
                if(p.GetID()== currentPlayer.GetID()){
                    this.sendRandomEffect(p.GetID(),new LogEvent("You have accepted to loot the smugglers",-1,-1,-1,-1));
                }
                else {
                    this.sendRandomEffect(p.GetID(), new LogEvent(currentPlayer.GetID() + " has accepted to loot the smugglers", -1, -1, -1, -1));
                }
            }

            this.getBoard().movePlayer(this.currentPlayer.GetID(), this.getTime());
            currentPlayer.setState(new HandleCargo());
            currentPlayer.getmyPlayerBoard().setRewards(rewardGoods);
        }
        else{

            ArrayList<Player> PlayerListMsg = this.getBoard().getPlayers();
            for(Player p : PlayerListMsg){
                if(p.GetID()== currentPlayer.GetID()){
                    this.sendRandomEffect(p.GetID(),new LogEvent("You have refused to loot the smugglers",-1,-1,-1,-1));
                }
                this.sendRandomEffect(p.GetID(),new LogEvent(currentPlayer.GetID()+" has refused to loot the smugglers",-1,-1,-1,-1));
            }


            this.finishCard();
        }
    }


    /**
     * Executes the process of losing a cargo item for the current player. The method targets
     * a specific coordinate and an index within a storage to attempt stealing the most valuable good.
     * If there are no goods left to steal, energy is used instead. The method also handles transitions
     * between different player states during the process.
     *
     * @param coord the coordinates of the tile containing the storage to target.
     * @param index the index of the good to be stolen within the targeted storage.
     * @throws InvalidInput if the targeted tile does not contain a storage, the specified index
     *         is out of bounds, or the selected good is not the most valuable one.
     */
    @Override
    public void loseCargo(IntegerPair coord, int index) {

        PlayerBoard CurrentPlanche = currentPlayer.getmyPlayerBoard();
        ArrayList<Storage> storages = CurrentPlanche.getStorages();
        Tile tiles[][] = CurrentPlanche.getPlayerBoard();


        HashMap<Integer, ArrayList<IntegerPair>> cargoH = CurrentPlanche.getStoredGoods();

        int max;
        max = max(cargoH.keySet());
        System.out.println("\npre furto devo rubare " + tmpPunishment + "\n");

        if (cargoH.isEmpty()) {
            System.out.println("ho rubato tutto, passo alle energie");
            energyUsage = min(tmpPunishment, CurrentPlanche.getEnergy());
            System.out.println("energy usage (lose Cargo) :" + energyUsage);
            this.setDefaultPunishment(energyUsage);
            this.isaPunishment = true;
            currentPlayer.setState(new ConsumingEnergy()); // potrebbe non fare l'update?
            this.setDefaultPunishment(energyUsage);
            return;
        }

        if (CurrentPlanche.getTile(coord.getFirst(), coord.getSecond()) == null) {
            throw new InvalidInput("there is nothing here ");
        } else {
            if (storages.contains(CurrentPlanche.getTile(coord.getFirst(), coord.getSecond()).getComponent())) {
                int i = storages.indexOf(CurrentPlanche.getTile(coord.getFirst(), coord.getSecond()).getComponent()); //per prendere l'iesimo elemento devo prima prenderne l'indice da storgaes fando indexof elemet e poi get i, non mi basta usare il primo perche il primo è component mentre preso dalla get lo considero come storage
                Storage currStorage = storages.get(i);
                if (index >= currStorage.getGoods().size() || index < 0) {
                    throw new InvalidInput("there is nothing at this position in the storage");
                }
                if (currStorage.getValue(index) == max) {//iterator.next da il primo elemento non chiederti perché
                    CurrentPlanche.performAction(tiles[coord.getFirst()][coord.getSecond()].getComponent(), new GetGoodAction(index, CurrentPlanche, coord.getFirst(), coord.getSecond()), new HandleTheft());
                    this.tmpPunishment--;
                    this.setDefaultPunishment(tmpPunishment);

                } else {
                    throw new InvalidInput("this isnt the most valuable good you own");
                }

            } else {
                throw new InvalidInput("this isn't a storage ");
            }
        }
        System.out.println("\npost furto devo rubare " + tmpPunishment + "\n");

        if (tmpPunishment == 0) {
            System.out.println("finished stealing");
            this.updateStates();
            return;
        }
        if (cargoH.isEmpty()) {
            System.out.println("ho rubato tutto, passo alle energie");
            energyUsage = min(tmpPunishment, CurrentPlanche.getEnergy());
            this.setDefaultPunishment(energyUsage);
            this.isaPunishment = true;
            currentPlayer.setState(new ConsumingEnergy()); // potrebbe non fare l'update?
            this.setDefaultPunishment(energyUsage);
            return;
        } else {
            currentPlayer.setState(new HandleTheft());
        }


    }


    /**
     * Finalizes the Smugglers card's effect in the game by marking it as completed.
     *
     * This method performs the following tasks:
     * 1. Calls the `checkLosers` method to identify and process players who were unable to meet the card's conditions.
     * 2. Logs a message indicating that the card's effect has been concluded.
     * 3. Marks the card as finished by setting its `finished` status to `true`.
     *
     * The method ensures that all necessary end-of-card actions are executed and the game's state is updated to reflect
     * the completion of the Smugglers card.
     */
    @Override
    public void finishCard() {
        checkLosers();
        System.out.println("card finished");
        this.setFinished(true);
    }

    /**
     * Retrieves the current player involved in the game/session.
     *
     * @return the current player in the game as a {@code Player} object.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Continues the current Smugglers card phase in the game by finalizing its execution.
     *
     * This method completes the sequence of actions or state transitions associated with the
     * Smugglers card. It invokes the {@code finishCard()} method, which processes final
     * outcomes such as evaluating the players' performance, checking losers, and setting
     * the card's state to finished. This ensures the game can progress to subsequent phases.
     */
    @Override
    public void keepGoing(){
        this.finishCard();
    }



    //json required

    /**
     * Represents the Smugglers class which may be used to handle operations
     * or data related to smuggling activities. This default constructor
     * initializes an instance of the Smugglers class.
     */
    public Smugglers(){}
    /**
     * Retrieves the value of the requirement.
     *
     * @return the requirement value as an integer
     */
    public int getRequirement() {return requirement;}
    /**
     * Sets the requirement value.
     *
     * @param requirement an integer value representing the requirement to be set
     */
    public void setRequirement(int requirement) {this.requirement = requirement;}
    /**
     * Retrieves the list of reward goods.
     *
     * @return an ArrayList containing the reward goods.
     */
    public ArrayList<Goods> getReward() {return rewardGoods;}
    /**
     * Sets the reward for the current instance.
     *
     * @param reward the list of goods to be set as the reward
     */
    public void setReward(ArrayList<Goods> reward) {this.rewardGoods = reward;}
    /**
     * Sets the punishment value.
     *
     * @param punishment the punishment value to be set
     */
    public void setPunishment(int punishment) {Punishment = punishment;}
}
