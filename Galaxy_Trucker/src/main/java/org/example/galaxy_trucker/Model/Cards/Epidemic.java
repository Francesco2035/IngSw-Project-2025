package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.KillCrewAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;

/**
 * The Epidemic class extends Card and represents a specific card in the game that triggers
 * an epidemic event. This involves infecting housing units and possibly impacting
 * players through the spread of infection.
 */
public class Epidemic extends Card {
    /**
     * Represents a collection of housing units that are currently infected.
     * Maintains details of all the housing units impacted during the course
     * of the epidemic. This variable is utilized to track the spread of the
     * infection within the game environment.
     */
    private ArrayList<HousingUnit> infected;

    /**
     * Represents the player currently taking their turn in the game.
     * This variable holds a reference to the Player object
     * whose actions, decisions, and moves are being tracked
     * and executed during their active turn.
     */
    private Player currentPlayer;
    /**
     * Represents a list of players who have lost or been eliminated in the game.
     * This list is maintained to track all players that are no longer actively participating
     * due to specific game rules or conditions.
     */
    private ArrayList<Player> losers;


    /**
     * Sends a log event of type "Epidemic" to all players currently participating in the game.
     *
     * This method retrieves the list of players from the game board associated with this card
     * and iterates through each player, sending a predefined "Epidemic" log event with random effects.
     *
     * The log event is created with the following parameters:
     * - Event type: "Epidemic"
     * - Predefined placeholder values for the remaining event attributes, all initialized as -1.
     */
    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Epidemic",-1,-1,-1,-1));
        }
    }


    /**
     * Constructs an Epidemic card with specified level, time, and a reference to the game board.
     *
     * @param level the level of the epidemic, influencing its severity or effects
     * @param time the duration or timing parameter for the epidemic
     * @param board the game board associated with the current game session
     */
    public Epidemic(int level, int time, GameBoard board) {
        super(level, time, board);

    }

    /**
     * Executes the effect of the epidemic card, spreading infection across players and performing
     * specific actions on their boards. The method iterates over all players, identifies infected
     * housing units on their respective player boards, and executes corresponding actions to simulate
     * the epidemic, including "killing" crew members in affected tiles.
     *
     * Steps:
     * 1. Initializes or clears necessary data structures such as the list of infected housing units.
     * 2. Sets all players to a `Waiting` state and temporarily pauses execution for five seconds.
     * 3. Iterates through all players in the game:
     *      a. Identifies infected housing units on their respective player boards using the
     *         `findInfected` method.
     *      b. Outputs the coordinates of the infected tiles to the console for debugging purposes.
     *      c. Puts the current player into a `WaitingEpidemic` state.
     *      d. Executes the `KillCrewAction` on infected tiles to simulate the epidemic spreading
     *         within their player board.
     * 4. Invokes the `finishCard` method, marking the completion of the card effect.
     *
     * Exceptions:
     * - InterruptedException: Thrown if the thread sleep operation is interrupted.
     *
     * Preconditions:
     * - The `GameBoard` and `Player` objects associated with the game must be properly initialized.
     * - The `findInfected` method must function correctly to identify infected housing units.
     *
     * Postconditions:
     * - Affected players will have their crew members in infected tiles impacted as per the game rules.
     * - The method finishes by updating the state of the game relative to the epidemic card effects.
     */
    public void CardEffect() throws InterruptedException {
        losers = new ArrayList<>();
        if(infected==null) {
            infected = new ArrayList<>();
        }

        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(new Waiting());
        }
        Thread.sleep(5000);
        PlayerBoard CurrentPlanche;
        int Len= PlayerList.size(); // quanti player ho
        ArrayList<HousingUnit> HousingCoords= new ArrayList<>();
        infected = new ArrayList<>();

        Tile[][] tiles;
        for(int i=0;i<Len;i++){
            this.infected.clear();
            this.currentPlayer = PlayerList.get(i);
            this.currentPlayer.setState(new Waiting());
            CurrentPlanche=this.currentPlayer.getmyPlayerBoard();
            tiles = CurrentPlanche.getPlayerBoard();
            HousingCoords.clear();
            HousingCoords.addAll(CurrentPlanche.getHousingUnits());


            ArrayList<IntegerPair> visited = new ArrayList<>();
            System.out.println("starting infected");
            for (int j = 0; j < HousingCoords.size(); j++) {
                findInfected(HousingCoords.get(j),CurrentPlanche);
                //findPaths(HousingCoords.get(j).getFirst(), HousingCoords.get(j).getSecond(), visited, valid ,playerTiles);

            }
            System.out.println("size of infected is: "+infected.size());
            for(int k=0;k<infected.size();k++){
                System.out.println(infected.get(k).getX()+" "+infected.get(k).getY());
            }
            currentPlayer.setState(new WaitingEpidemic());
            for(int k=0;k<infected.size();k++){

                System.out.println("killing in:"+infected.get(k).getX()+" "+infected.get(k).getY());

                System.out.println((currentPlayer.getPlayerState().getClass()));
                CurrentPlanche.performAction(tiles[this.infected.get(k).getX()][this.infected.get(k).getY()].getComponent(),new KillCrewAction(CurrentPlanche), new WaitingEpidemic());

            }


        }
        this.finishCard();
    }

    /**
     * Completes the processing of the current card and updates its state to indicate it is finished.
     *
     * This method performs the following actions:
     * 1. Calls the `checkLosers` method to evaluate the current state of players in the game and
     *    identify any players who should lose or abandon the game based on predefined conditions
     *    such as being "doubled" or having no remaining crew members.
     * 2. Sets the finished status of the card to true using the `setFinished` method, marking the
     *    card as processed.
     *
     * This method ensures that game state validations occur and the card transitions to a finished state
     * appropriately.
     */
    @Override
    public void finishCard() {
        checkLosers();
        this.setFinished(true);
    }

    /**
     * Identifies and processes infected housing units on the board based on their proximity to populated houses.
     * An infected housing unit is defined as one that is adjacent to a populated house, where a populated house
     * contains brown aliens, purple aliens, or human occupants.
     *
     * The method evaluates the surrounding tiles of the specified housing unit and checks if any nearby tiles
     * meet the infection criteria based on their coordinates and validity status. If a match is found, the
     * housing unit is flagged as infected and added to the list of infected units.
     *
     * @param house the housing unit to check for potential infection based on adjacency to already populated units
     * @param playerBoard the player board containing tiles, validity information, and housing unit data
     */
    public void findInfected(HousingUnit house,PlayerBoard playerBoard) {

        int x = house.getX();
        int y = house.getY();
        Tile [][] tiles=playerBoard.getPlayerBoard();
        int[][]valid=playerBoard.getValidPlayerBoard();
        PlayerBoard currBoard= this.currentPlayer.getmyPlayerBoard();
        ArrayList<IntegerPair> PopulatedHousecoords = new ArrayList<>();
        for(HousingUnit H:currBoard.getHousingUnits() ){
            if(H.isBrownAlien()||H.isPurpleAlien()||H.getNumHumans()>0) {
                PopulatedHousecoords.add(new IntegerPair(H.getX(), H.getY()));
            }
        }



        System.out.println("looking at: "+x+" "+y);
        int w;
        int z;
        IntegerPair temp;
        if(house.getNumHumans()>0||house.isBrownAlien()||house.isPurpleAlien()){
            w=x;
            z=y-1; //left
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if (PopulatedHousecoords.contains(new IntegerPair(w,z)) ) {
                    if (!this.infected.contains(house)) {
                        System.out.println("added from left");
                        this.infected.add(house);
                    }
                }
            }
            w=x-1;
            z=y;//up
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if (PopulatedHousecoords.contains(new IntegerPair(w,z)) ) {
                    if (!this.infected.contains(house)) {
                        System.out.println("added from left");
                        this.infected.add(house);
                    }
                }
            }
            w=x;
            z=y+1; //right
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if (PopulatedHousecoords.contains(new IntegerPair(w,z)) ) {
                    if (!this.infected.contains(house)) {
                        System.out.println("added from left");
                        this.infected.add(house);
                    }
                }
            }
            w=x+1;
            z=y; //down
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if (PopulatedHousecoords.contains(new IntegerPair(w,z)) ) {
                    if (!this.infected.contains(house)) {
                        System.out.println("added from left");
                        this.infected.add(house);
                    }
                }
            }

        }
        System.out.println("end of infected");

    }


    /**
     * Default constructor for the Epidemic class.
     *
     * This constructor initializes an Epidemic card object without any specified level,
     * time, or game board parameters. It creates a new instance of the card with default
     * state values and no direct association with a particular game session.
     *
     * Use this constructor to create a generic Epidemic card when specific details do
     * not need to be immediately assigned.
     */
    public Epidemic(){}

}




