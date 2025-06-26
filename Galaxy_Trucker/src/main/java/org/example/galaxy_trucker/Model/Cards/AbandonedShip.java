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
 * Represents an Abandoned Ship card in the Galaxy Trucker game.
 *
 * <p>This card presents players with the opportunity to board an abandoned ship
 * in exchange for sacrificing crew members. Players must have sufficient crew
 * to meet the requirement and choose which crew members to sacrifice.</p>
 *
 * <p>The card follows a turn-based approach where players are checked in order
 * to see if they meet the crew requirement. The first player who meets the
 * requirement can choose to accept or decline the opportunity.</p>
 *
 * <p>Card mechanics:
 * <ul>
 * <li>Players need a minimum number of crew members (including aliens) to qualify</li>
 * <li>If accepted, the player must sacrifice the required number of crew members</li>
 * <li>In return, the player receives credits and moves backward in time</li>
 * <li>Players with no remaining crew are eliminated from the race</li>
 * </ul></p>
 *
 * <p>Note: In case of disconnection, the player automatically refuses the offer.</p>
 *
 * @author Pietro
 * @version 1.0
 * @since 1.0
 */

public class AbandonedShip extends Card{
    /**
     * The minimum number of crew members required to board the abandoned ship.
     * This includes both human crew and alien crew members.
     */
    private int requirement;

    /**
     * The credit reward given to the player who successfully boards the ship.
     * This compensation is provided after sacrificing the required crew.
     */
    private int reward;

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
     * Used for tracking crew count before the sacrifice decision.
     */
    private int totHumans;

    /**
     * List of players who will be eliminated due to having no crew left.
     * Populated when players sacrifice their last crew members.
     */
    private ArrayList<Player> losers;


    /**
     * Sends type-specific log information to all players.
     * Notifies all players that an Abandoned Ship card has been encountered.
     */
    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Abandoned Ship",-1,-1,-1,-1));
        }
    }

    /**
     * Constructs a new AbandonedShip card with specified parameters.
     *
     * @param requirement the minimum number of crew members needed to qualify
     * @param reward the credit reward for boarding the ship
     * @param level the difficulty level of the card
     * @param time the time cost associated with this card
     * @param board the game board this card is associated with
     */
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
     * Executes the main effect of the Abandoned Ship card.
     *
     * <p>Sets the default punishment to the crew requirement, initializes the losers list,
     * puts all players in waiting state, and begins the evaluation process after a delay.</p>
     *
     * @throws InterruptedException if the thread is interrupted during the sleep period
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
     * Updates player states and evaluates players in turn order.
     *
     * <p>Iterates through players to find the first one who meets the crew requirement.
     * The requirement includes human crew plus brown and purple aliens if present.
     * When a qualified player is found, they are put in Accepting state.</p>
     *
     * <p>If no qualified player is found, the card finishes automatically.</p>
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
    @Override
    public void continueCard(boolean accepted) {


        if(accepted){

            ArrayList<Player> PlayerList = this.getBoard().getPlayers();
            for(Player p : PlayerList){
                if(p.GetID()== currentPlayer.GetID()){
                    this.sendRandomEffect(p.GetID(),new LogEvent("You have accepted to board the ship",-1,-1,-1,-1));
                }
                this.sendRandomEffect(p.GetID(),new LogEvent(currentPlayer.GetID()+" has accepted to board the ship",-1,-1,-1,-1));
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
//    @Override
//    public  void  ActivateCard() {
//
//        System.out.println("ActivateCard");
//        currentPlayer.getInputHandler().action();
//    }

    @Override
    public void finishCard() {
        System.out.println("card finished");
        checkLosers();

        this.setFinished(true);

    }

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

    public int getTotHumans() {
        return totHumans;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Empty constructor required for JSON serialization.
     */
    public AbandonedShip() {}
    public int getRequirement() {return requirement;}
    public void setRequirement(int requirement) {this.requirement = requirement;}
    public int getReward() {return reward;}
    public void setReward(int reward) {this.reward = reward;}
}
