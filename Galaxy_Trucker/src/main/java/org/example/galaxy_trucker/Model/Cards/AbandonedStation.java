package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
//import org.example.galaxy_trucker.Model.InputHandlers.Accept;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.Accepting;
import org.example.galaxy_trucker.Model.PlayerStates.HandleCargo;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;


import java.util.ArrayList;

/**
 * Represents an Abandoned Station card in the Galaxy Trucker game.
 *
 * <p>This card presents players with the opportunity to board an abandoned space station
 * and collect valuable goods.</p>
 *
 * <p>The card follows a turn-based approach where players are checked in order
 * to see if they meet the crew requirement. The first player who meets the
 * requirement can choose to accept or decline the opportunity.</p>
 *
 * <p>Card mechanics:
 * <ul>
 * <li>Players need a minimum number of crew members (including aliens) to qualify</li>
 * <li>If accepted, the player receives a collection of goods as reward</li>
 * <li>The player moves backward in time as a cost for the opportunity</li>
 * <li>Players enter a cargo handling state to manage the received goods</li>
 * </ul></p>
 *
 * <p>Note: In case of disconnection, the player automatically refuses the offer.</p>
 *
 * @author Pietro
 * @version 1.0
 * @since 1.0
 */
public class AbandonedStation extends Card{
    /**
     * The minimum number of crew members required to board the abandoned station.
     * This includes both human crew and alien crew members (brown and purple aliens).
     */
    private int requirement;

    /**
     * The collection of goods awarded to the player who successfully boards the station.
     * These goods are added to the player's inventory upon acceptance.
     */
    @JsonProperty("rewardGoods")
    private ArrayList<Goods> rewardGoods;

    /**
     * The player currently being evaluated or interacting with the card.
     * This tracks which player is making the decision to accept or decline.
     */
    private Player currentPlayer;

    /**
     * Flag indicating whether a player has been found who meets the requirements.
     * Set to true when a qualified player is found, stopping further searches.
     */
    private boolean flag;

    /**
     * The current order/index in the player list being evaluated.
     * Used to iterate through players in turn order.
     */
    private int order;

    /**
     * The total number of human crew members the current player has.
     * Used for tracking crew count during evaluation.
     */
    int totHumans;

    /**
     * List of players who will be eliminated due to various conditions.
     * Maintained for consistency with other card types, though typically unused in this card.
     */
    private ArrayList<Player> losers;


    /**
     * Sends type-specific log information to all players.
     * Notifies all players that an Abandoned Station card has been encountered.
     *
     */
    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Abandoned station",-1,-1,-1,-1));
        }
    }

    /**
     * Constructs a new AbandonedStation card with specified parameters.
     *
     * @param requirement the minimum number of crew members needed to qualify
     * @param reward the list of goods awarded for boarding the station
     * @param level the difficulty level of the card
     * @param time the time cost associated with this card
     * @param board the game board this card is associated with
     */
    public AbandonedStation(int requirement, ArrayList<Goods> reward, int level, int time, GameBoard board) {
        super(level, time, board);
        this.requirement = requirement;
        this.rewardGoods = reward;
        this.order = 0;
        this.totHumans = 0;
        this.flag = false;
        this.currentPlayer = null;

    }


//    @Override
//    public  void  ActivateCard() {
//        currentPlayer.getInputHandler().action();
//    }

    /**
     * Executes the main effect of the Abandoned Station card.
     *
     * <p>Initializes the losers list, puts all players in waiting state,
     * and begins the evaluation process after a brief delay to allow
     * for proper state synchronization.</p>
     *
     * @throws InterruptedException if the thread is interrupted during the sleep period
     */
    @Override
    public void CardEffect() throws InterruptedException {

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
     * Updates player states and evaluates players in turn order.
     *
     * <p>Iterates through players to find the first one who meets the crew requirement.
     * The requirement includes human crew plus brown and purple aliens if present.
     * When a qualified player is found, they are put in Accepting state.</p>
     *
     * <p>If no qualified player is found after checking all players, the card finishes automatically.</p>
     *
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


            int NumHumans= CurrentPlanche.getNumHumans();
            if(CurrentPlanche.isBrownAlien()){
                NumHumans++;
            }
            if (CurrentPlanche.isPurpleAlien()){
                NumHumans++;
            }
            if(NumHumans>=this.requirement){
                System.out.println(currentPlayer.GetID()+" has enough required housing");
                this.flag = true;
                currentPlayer.setState(new Accepting());
                //currentPlayer.setInputHandler(new Accept(this));
                currentPlayer.setCard(this

                );

            }

            this.order++;
        }
    }

    /**
     * Finishes the card execution and performs cleanup.
     *
     * <p>Calls the checkLosers method to handle any player elimination
     * and marks the card as finished.</p>
     */
    @Override
    public void finishCard() {
        checkLosers();
        System.out.println("card finished!");
        this.setFinished(true);
    }

    /**
     * Continues the card execution based on player's acceptance decision.
     *
     * <p>If the player accepts:
     * <ul>
     * <li>Sends notification to all players about the acceptance</li>
     * <li>Changes the current player's state to HandleCargo</li>
     * <li>Awards the reward goods to the player's inventory</li>
     * <li>Moves the player backward in time as the cost</li>
     * </ul></p>
     *
     * <p>If the player refuses:
     * <ul>
     * <li>Returns the player to Waiting state</li>
     * <li>Resets the flag and continues searching for the next qualified player</li>
     * </ul></p>
     *
     * @param accepted true if the player accepts to board the station, false otherwise
     */
    @Override
    public void continueCard(boolean accepted) {
        if(accepted) {
            ArrayList<Player> PlayerListMsg = this.getBoard().getPlayers();
            for(Player p : PlayerListMsg){
                if(p.GetID()== currentPlayer.GetID()){
                    this.sendRandomEffect(p.GetID(),new LogEvent("You have accepted to board the station",-1,-1,-1,-1));
                }
                else {
                    this.sendRandomEffect(p.GetID(), new LogEvent(currentPlayer.GetID() + " has accepted to board the station", -1, -1, -1, -1));
                }
            }

            currentPlayer.setState(new HandleCargo());
            currentPlayer.getmyPlayerBoard().setRewards(rewardGoods);
            this.getBoard().movePlayer(this.currentPlayer.GetID(), -this.getTime());

        }
        else{
            currentPlayer.setState(new Waiting());
            this.flag = false;
            this.updateStates();
        }
    }

    /**
     * Allows the card to continue its execution and finish.
     *
     * <p>This method is typically called after cargo handling is complete
     * to finalize the card's effects and clean up.</p>
     *
     */
    public void keepGoing(){
        this.finishCard();
    }

    /**
     * Empty constructor required for JSON serialization.
     */
    public AbandonedStation() {}


    /**
     * Retrieves the crew requirement necessary to qualify for boarding the abandoned station.
     *
     * @return an integer representing the minimum number of crew members required
     */
    public int getRequirement() {return requirement;}

    /**
     * Sets the crew requirement to qualify for boarding the abandoned station.
     *
     * @param requirement the minimum number of crew members required
     */
    public void setRequirement(int requirement) {this.requirement = requirement;}


    /**
     * Gets the list of goods awarded for boarding the abandoned station.
     *
     * @return ArrayList of Goods representing the reward items
     */
    public ArrayList<Goods> getReward() {return rewardGoods;}

    /**
     * Sets the list of goods awarded for boarding the abandoned station.
     *
     * @param reward ArrayList of Goods representing the reward items to set
     */
    public void setReward(ArrayList<Goods> reward) {this.rewardGoods = reward;}
}
