package org.example.galaxy_trucker.Model.Cards;
//import javafx.util.Pair;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Exceptions.ImpossibleBoardChangeException;
import org.example.galaxy_trucker.Exceptions.InvalidDefenceEceptiopn;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.Connectors.*;
import org.example.galaxy_trucker.Model.Tiles.Tile;


import java.util.ArrayList;
import java.util.HashMap;




//GESTISCI GLI ATTACCHI COME UN ARRAY LIST DI INTEGER E NON INTEGER PAIR

/**
 * The Meteorites class represents a game card that simulates meteorite attacks in the Galaxy Trucker game.
 * It manages the processes and states related to meteorite attacks and player defenses.
 * This class extends the base Card class and contains methods and data for the game's meteorite-related logic.
 */

public class   Meteorites extends Card {

    /**
     * Represents the current player in the game session for the Meteorites card.
     * This variable identifies whose turn it is to act or make decisions during the game.
     * It is updated as the game progresses and is directly associated with player actions
     * or game state modifications.
     */
    private Player currentPlayer;

    /**
     * This boolean variable indicates the status or state that determines whether
     * a particular condition or functionality is currently active within the Meteorites class.
     * It acts as a control mechanism for managing specific behaviors or logic in the game's
     * operations related to meteorites or associated gameplay mechanics.
     */
    private boolean flag;

    /**
     * Represents the order in which the player interacts with the game or is assigned roles
     * in the Meteorites class. This variable helps define the current sequence or position of
     * the player in the gameplay mechanics.
     *
     * PlayerOrder is utilized to manage or determine the player's turn or order in a series of game events
     * related to the Meteorites gameplay.
     */
    private int PlayerOrder;

    /**
     * Represents the current order of meteorites in the gameplay.
     *
     * This variable is used within the `Meteorites` class to maintain or manipulate
     * the sequence of meteorites as part of the game's mechanics. It can be accessed
     * or modified via appropriate getter and setter methods provided in the class.
     */
    private int MeteoritesOrder;

    /**
     * Represents the current line of meteorites in the game.
     * Used to track or determine the active line of meteorites
     * affecting the game state or gameplay dynamics within the Meteorites class.
     */
    private int MeteoritesLine;

    /**
     * Stores the hit positions associated with players identified by their unique string keys.
     * Each entry in the map links a player's identifier to an {@code IntegerPair} object
     * representing the coordinates of a hit.
     *
     * The keys in the HashMap are strings that uniquely identify players, while the values
     * are {@code IntegerPair} objects that denote the x and y coordinates of a hit.
     *
     * This variable likely tracks hits on players or their assets during the gameplay. It serves as a
     * central repository for managing and updating player-related hit information.
     */
    private HashMap <String,IntegerPair> hits;

    /**
     * Represents the number of successful defences performed
     * in the context of the Meteorites class.
     *
     * This variable keeps track of how many defences against meteorite impacts
     * have been successfully executed during a game or a particular event.
     *
     * It plays a key role in evaluating player performance and determining outcomes related to
     * defensive strategies in gameplay mechanics.
     */
    private  int SuccessfulDefences;

    /**
     * Represents the number of successful defenses executed during gameplay in the Meteorites class.
     *
     * This variable tracks the total count of defenses made by players to safeguard
     * against meteorite impacts or related threats. It is primarily used for record-keeping
     * and gameplay progression logic within the Meteorites class.
     *
     * The variable plays a significant role in determining various gameplay outcomes
     * and evaluating player performance.
     */
    private  int NumofDefences;

    /**
     * Represents a list of players who have lost during the game.
     * The `losers` variable is used to keep track of those players who are no longer active or have been defeated.
     */
    private ArrayList<Player> losers;

    /**
     * Represents the list of attacks associated with the Meteorites card.
     * Each integer in the list corresponds to specific attack data, where
     * values can indicate direction or type of attack based on predefined rules.
     * This variable is serialized and deserialized using JSON for proper external
     * data exchange.
     */
    @JsonProperty ("attacks")
    private ArrayList<Integer> attacks;



    /**
     * Sends a log event of type "Meteor swarn" to all players in the game.
     *
     * This method retrieves all players from the game board and iterates through
     * the list of players. For each player, a random effect is sent using the
     * player's ID and a new {@code LogEvent} object initialized with the
     * parameters specific to a "Meteor swarn" event.
     *
     * The log event is created with the following parameters:
     * - Event name: "Meteor swarn"
     * - Default values for other parameters: -1
     *
     * This method ensures that all players are informed of the event in their
     * game environment.
     */
    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Meteor swarn",-1,-1,-1,-1));
        }
    }


    /**
     * Constructs a {@code Meteorites} object to represent a meteorite-related challenge in the game.
     *
     * @param level the difficulty level of the meteorite encounter
     * @param time the time allocated or associated with the meteorite event
     * @param board the game board on which the meteorite challenge will occur
     * @param attacks a list of integers representing specific meteorite attack patterns or configurations
     */
    public Meteorites(int level, int time, GameBoard board, ArrayList<Integer> attacks) {
        super(level, 0, board);

        this.attacks = attacks;
        this.currentPlayer = new Player();
        this.flag = false;
        this.PlayerOrder = 0;
        this.MeteoritesOrder = 0;
        this.MeteoritesLine = 0;
        this.hits = new HashMap<>();



    }

    /**
     * Executes the card effect logic for the "Meteorites" card in the game. This method is called during
     * the gameplay whenever the effect of this card needs to be applied.
     *
     * The method performs the following actions:
     * - Initializes internal state and data structures like the list of losers and hit mappings.
     * - Iterates through all players on the game board and updates their states to "Waiting."
     * - Updates the hit count for each player using their unique ID.
     * - Determines the line of meteorite attack based on player dice rolls.
     * - Executes the meteorite attack sequence if there are pending attacks in the order.
     * - Logs relevant information about the meteorite attack and updates the game states.
     * - Ends the card effect if all meteorite attacks have been handled.
     */
    @Override
    public void CardEffect() {
        losers = new ArrayList<>();
        hits = new HashMap<>();
        for (Player p: this.getBoard().getPlayers()) {
            p.setState(new Waiting());
            hits.put(p.GetID(),new IntegerPair(0,0));
        }

        System.out.println("SIZE OF ATTACK:"+attacks.size()/2);

        GameBoard MeteoritesBoard = super.getBoard();
        ArrayList<Player> MeteoritesPlayerList = MeteoritesBoard.getPlayers();
        this.SuccessfulDefences=0;
        this.NumofDefences=super.getBoard().getPlayers().size();

        if (this.MeteoritesOrder< this.attacks.size()) { //scorre i meteoriti e attacca i player 1 a 1

            this.MeteoritesLine = MeteoritesPlayerList.get(0).RollDice()-1; // tira numero
//            if(this.attacks.get(this.MeteoritesOrder)==0){
//                this.MeteoritesLine=8;
//            }
            System.out.println("attacco da "+attacks.get(MeteoritesOrder) +" alla riga:"+this.MeteoritesLine);
            System.out.println("attacco numero: "+(this.MeteoritesOrder/2));
            this.updateStates();
        }
        else {
//            this.pog=true;
            this.finishCard();

        }
    }


    /**
     * Increments the count of successful defences and updates the internal game states.
     *
     * This method is typically invoked to signify that a defensive action for
     * meteorites has been performed successfully during the game. It increments
     * the {@code SuccessfulDefences} field to reflect the total number of successful
     * defenses and proceeds to update the relevant internal state by invoking the
     * {@code updateStates()} method.
     */
    @Override
    public void keepGoing(){
        this.SuccessfulDefences++;
        updateStates();
    }

    /**
     * Updates the states of players and processes the meteorite attacks in the game.
     * This method contains logic for determining whether players are in a waiting state,
     * handling the effects of meteorite attacks on players’ boards, and setting states
     * based on the outcome of such attacks. The behavior and state transitions for
     * players are dependent on the conditions and results of defense actions.
     *
     * The method operates as follows:
     * - Checks whether all defenses are completed successfully.
     *   If true, resets player order and progresses to the next meteorite attack phase,
     *   updating players' states and applying card effects as needed.
     * - Proceeds with meteorite attacks for the current player based on specified directions
     *   (left, above, right, below) and sizes (small, large). The position of the meteorite and
     *   its interaction with the player's board are evaluated to determine the outcome of the attack.
     * - Updates the hit locations and damage indicators on the player’s board if a meteorite
     *   successfully impacts, and transitions the player to the appropriate defending state
     *   based on the size of the meteorite.
     * - Handles cases where meteorites bounce off the player’s board without causing damage.
     *
     * This method executes in a synchronized context for portions where delay
     * (e.g., thread sleep) is implemented to ensure proper turn-based gameplay handling.
     *
     * Key actions determined by meteorite attacks include:
     * - Evaluating the type and trajectory of meteorites.
     * - Analyzing the impact on players' boards.
     * - Transitioning players' states to Waiting, DefendingFromSmall,
     *   or DefendingFromLarge based on the outcome.
     */
    @Override
    public void updateStates(){
        int Movement;
        boolean MeteoritesFlag=false;
        boolean DamageFlag=false;
        System.out.println("Successfuldefences ="+SuccessfulDefences +" NumofDefences= " +NumofDefences);
        if(this.SuccessfulDefences==NumofDefences) {
       // if (PlayerOrder>=this.getBoard().getPlayers().size()){
            try{
                for (Player p: this.getBoard().getPlayers()) {
                    p.setState(new Waiting());
                }
                synchronized (getLock()){
                    Thread.sleep(2500);
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            PlayerOrder=0;
            MeteoritesOrder+=2;
            this.CardEffect();
        }
        else if (PlayerOrder<this.getBoard().getPlayers().size()){
            //if (currentPlayer != null) {currentPlayer.setState(new Waiting());}

            this.currentPlayer = this.getBoard().getPlayers().get(PlayerOrder);

            PlayerBoard CurrentPlanche = currentPlayer.getmyPlayerBoard(); //prendo plancia
            int[][] MeteoritesValidPlanche = CurrentPlanche.getValidPlayerBoard();//prende matrice validita
            System.out.println("attack number: "+this.MeteoritesOrder/2 +" on: "+currentPlayer.GetID());

            /// cose per stampare al client
            String dimensione= new String();
            String direction = new String();
            String Colpito = new String("missed you");
            String location = new String("");
            int TypeOfHit= 0;

            if(attacks.get(MeteoritesOrder+1)==0) {
                dimensione = new String("small");
                TypeOfHit =0;

            }
            else {
                dimensione = new String("large");
                TypeOfHit =1;
            }

            if (attacks.get(MeteoritesOrder) == 0) { //sinistra
                System.out.println("SINISTRA");
                direction = new String("the left on line "+MeteoritesLine);
                Movement = 0;
                while (Movement < 10 && MeteoritesLine<10  && MeteoritesFlag == false) {
                    if (MeteoritesValidPlanche[MeteoritesLine][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                        location= new String("at "+MeteoritesLine+ " "+Movement);
                        Tile tiles[][] = CurrentPlanche.getPlayerBoard();
                        System.out.println("touch in:"+MeteoritesLine+" "+Movement);
                        if (attacks.get(MeteoritesOrder + 1) == 0 && tiles[MeteoritesLine][Movement].getConnectors().get(0) == NONE.INSTANCE) {
                            MeteoritesFlag = true;
                            System.out.println("lisciato");
                            Colpito =new String("bounced off");

                        }
                        else if (attacks.get(MeteoritesOrder + 1) == 0) {
                            MeteoritesFlag = true;
                            DamageFlag=true;
                            hits.get(currentPlayer.GetID()).setValue(MeteoritesLine,Movement);
                            System.out.println("Meteorites hit in: " + MeteoritesLine + " " + Movement);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromSmall());

                        }
                        else {
                            MeteoritesFlag = true;
                            DamageFlag = true;
                            hits.get(currentPlayer.GetID()).setValue(MeteoritesLine, Movement);
                            System.out.println("Meteorites hit in: " + MeteoritesLine + " " + Movement);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromLarge());
                        }
                    }

                    Movement++;
                }
            }
            else if (attacks.get(MeteoritesOrder) == 1) {//sopra
                System.out.println("SOPRA");
                direction = new String("above on column "+MeteoritesLine);
                Movement = 0;
                while (Movement < 10 && MeteoritesLine<10 && MeteoritesFlag == false) {
                    if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                        location= new String("at "+Movement+ " "+MeteoritesLine);
                        Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                        if (attacks.get(MeteoritesOrder + 1) == 0 && tiles[Movement][MeteoritesLine].getConnectors().get(1).equals(NONE.INSTANCE)) {
                            MeteoritesFlag = true;

                            System.out.println("lisciato");
                            Colpito =new String("bounced off");

                        }
                        else if (attacks.get(MeteoritesOrder + 1) == 0) {
                            MeteoritesFlag = true;
                            DamageFlag=true;
                            hits.get(currentPlayer.GetID()).setValue( Movement, MeteoritesLine);
                            System.out.println("Meteorites hit in: " + Movement + " " + MeteoritesLine);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromSmall());

                        }
                        else {
                            MeteoritesFlag = true;
                            DamageFlag = true;
                            hits.get(currentPlayer.GetID()).setValue(Movement, MeteoritesLine);
                            System.out.println("Meteorites hit in: " + Movement + " " + MeteoritesLine);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromLarge());
                        }
                    }

                    Movement++;
                }
            }
            else if (attacks.get(MeteoritesOrder) == 2) {// destra
                System.out.println("DESTRA");
                direction = new String("the right on line "+MeteoritesLine);
                Movement = 9;
                while (Movement >= 0  && MeteoritesLine<10 && MeteoritesFlag == false) {
                    if (MeteoritesValidPlanche[MeteoritesLine][Movement] > 0) {
                        location= new String("at "+MeteoritesLine+ " "+Movement);
                        Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                        if (attacks.get(MeteoritesOrder + 1) == 0 && tiles[MeteoritesLine][Movement].getConnectors().get(2).equals(NONE.INSTANCE)) {
                            MeteoritesFlag = true;
                            System.out.println("lisciato");
                            Colpito =new String("bounced off");

                        }
                        else if (attacks.get(MeteoritesOrder + 1) == 0) {
                            MeteoritesFlag = true;
                            DamageFlag=true;
                            hits.get(currentPlayer.GetID()).setValue(MeteoritesLine,Movement);
                            System.out.println("Meteorites hit in: " + MeteoritesLine+ " " + Movement);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromSmall());

                        }
                        else {
                            MeteoritesFlag = true;
                            DamageFlag = true;
                            hits.get(currentPlayer.GetID()).setValue(MeteoritesLine, Movement);
                            System.out.println("Meteorites hit in: " + MeteoritesLine + " " + Movement);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromLarge());
                        }
                    }
                    Movement--;
                }

            }
            else { //sotto
                System.out.println("SOTTO");
                direction = new String("below on column "+MeteoritesLine);
                Movement = 9;
                while (Movement >= 0 && MeteoritesLine<10  && MeteoritesFlag == false) {
                    if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {
                        location= new String("at "+Movement+ " "+MeteoritesLine);
                        Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                        if (attacks.get(MeteoritesOrder + 1) == 0 && tiles[Movement][MeteoritesLine].getConnectors().get(3).equals(NONE.INSTANCE)) {
                            MeteoritesFlag = true;
                            System.out.println("lisciato");
                            Colpito =new String("bounced off");

                        }
//
                        else if (attacks.get(MeteoritesOrder + 1) == 0) {
                            MeteoritesFlag = true;
                            DamageFlag = true;
                            hits.get(currentPlayer.GetID()).setValue( Movement, MeteoritesLine);
                            System.out.println("Meteorites hit in: " + Movement + " " + MeteoritesLine);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromSmall());

                        }

                        else {// caso meteoriti grandi
                            MeteoritesFlag = true;
                            DamageFlag = true;
                            hits.get(currentPlayer.GetID()).setValue(Movement, MeteoritesLine);
                            System.out.println("Meteorites hit in: " + Movement + " " + MeteoritesLine);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromLarge());
                        }
                    }

                    Movement--;
                }

            }

            System.out.println("a "+dimensione+" meteorite came from "+direction+" and it "+Colpito+" "+location);
            this.sendRandomEffect(currentPlayer.GetID(),new LogEvent("a "+dimensione+" meteorite came from "+direction+" and it "+Colpito+" "+location,hits.get(currentPlayer.GetID()).getFirst(),hits.get(currentPlayer.GetID()).getSecond(),attacks.get(MeteoritesOrder),TypeOfHit));

            if (!DamageFlag){this.SuccessfulDefences++;}
            this.PlayerOrder++;
            if (PlayerOrder<this.getBoard().getPlayers().size()) {
                this.updateStates();
            }
            else if(this.SuccessfulDefences==NumofDefences){
                this.updateStates();
            }
        }
    }


    /**
     * Executes a defense action against a small meteorite attack for a specific player.
     * This method processes the meteorite impact based on the player's energy,
     * shield availability, and board state. It updates the game state accordingly,
     * including the player's status after a successful or failed defense action.
     *
     * The method handles various scenarios within the game:
     * - Determines if energy is available to perform the defense.
     * - Checks whether the shield configuration is valid for defending against the attack.
     * - Applies energy consumption for defense, or executes destruction and logging in case of failure.
     * - Sends log events for specific outcomes, such as the destruction or defense success.
     * - Updates the player state and increments successful defense counts when applicable.
     *
     * This method should be invoked during a game's meteorite attack sequence when a small meteorite
     * impacts a player's game board.
     *
     * @param energy    The coordinates representing the energy source position on the player's board
     *                  that is used for the defense action, or null if no energy is available.
     * @param player    The player object representing the targeted player who is defending the attack.
     * @throws InterruptedException    If the method is interrupted during its execution, particularly
     *                                 when handling delays or thread interruptions.
     */
    @Override
    public void DefendFromSmall(IntegerPair energy, Player player) throws InterruptedException {
        System.out.println(player.GetID()+ "is defending from small");
        PlayerBoard currentBoard =player.getmyPlayerBoard();
        Tile[][] tiles =currentBoard.getPlayerBoard();
        if (energy!=null){

            /// possibile bug con gli scudi??
            if ((currentBoard.getShield()[attacks.get(MeteoritesOrder)]==0)){
                throw new InvalidDefenceEceptiopn("this shield defends the wrong side"+" the side was: "+attacks.get(MeteoritesOrder));
            }
            else {
                try {
                    currentBoard.performAction(tiles[energy.getFirst()][energy.getSecond()].getComponent(),new UseEnergyAction(currentBoard), new ConsumingEnergy());

                }
            catch (Exception e){
                throw new ImpossibleBoardChangeException("There was no energy to use here");
            }
                this.sendRandomEffect(player.GetID(),new LogEvent("you defended your ship in " +hits.get(player.GetID()).getFirst()+" "+hits.get(player.GetID()).getSecond(),hits.get(currentPlayer.GetID()).getFirst(),hits.get(currentPlayer.GetID()).getSecond(),attacks.get(MeteoritesOrder),0));
                System.out.println(player.GetID()+" Defended From Small");
                player.setState(new Waiting());
            }
        }
        else {
            currentBoard.destroy(hits.get(player.GetID()).getFirst(), hits.get(player.GetID()).getSecond());
            currentBoard.handleAttack(hits.get(player.GetID()).getFirst(), hits.get(player.GetID()).getSecond());
            this.sendRandomEffect(player.GetID(),new LogEvent("your ship got destroyed in " +hits.get(player.GetID()).getFirst()+" "+hits.get(player.GetID()).getSecond(),hits.get(currentPlayer.GetID()).getFirst(),hits.get(currentPlayer.GetID()).getSecond(),attacks.get(MeteoritesOrder),0));
            player.setState(new Waiting());

            if (currentBoard.getBroken()){

                System.out.println("\nrottura nave\n");

                System.out.println("destroyed: "+hits.get(player.GetID()).getFirst()+" "+hits.get(player.GetID()).getSecond());
                this.sendRandomEffect(player.GetID(),new LogEvent("your ship got broken into parts, select a chunk to keep",-1,-1,-1,-1));
                player.setState(new HandleDestruction());
                System.out.println("Stato del player "+ player.getPlayerState().getClass().getName());
                return;

            }
            System.out.println("Stato del player "+ player.getPlayerState().getClass().getName());
            System.out.println("destroyed: "+hits.get(player.GetID()).getFirst()+" "+hits.get(player.GetID()).getSecond());
        }
        this.SuccessfulDefences++;
        this.updateStates();
    }

    /**
     * Attempts to defend against a large meteorite attack using a specified cannon and optional energy storage.
     * The method verifies the cannon's position, determines the correct type of energy usage, and validates
     * if the defense action is feasible. If successful, it updates the player's state and logs the event.
     * If the defense fails or coordinates are invalid, appropriate exceptions are thrown, and the player's
     * ship might suffer damage or be destroyed.
     *
     * @param CannonCoord The coordinates of the selected cannon that will be used for defense. It must align
     *                    with the meteorite's trajectory.
     * @param EnergyStorage The coordinates of the energy storage component, required if the cannon needs to
     *                      consume energy for the defense action.
     * @param player The player initiating the defense. This includes access to the player's board and current state.
     */
    @Override
    public void DefendFromLarge(IntegerPair CannonCoord,IntegerPair EnergyStorage, Player player){
        PlayerBoard currentBoard =player.getmyPlayerBoard();
        Tile[][] tiles =currentBoard.getPlayerBoard();
        if(CannonCoord !=null) {
            if (attacks.get(MeteoritesOrder) == 0 || attacks.get(MeteoritesOrder) == 2) { // sinistra o destra
                if(!((( CannonCoord.getFirst() == hits.get(player.GetID()).getFirst() ||CannonCoord.getFirst() == hits.get(player.GetID()).getFirst()+1 || CannonCoord.getFirst() == hits.get(player.GetID()).getFirst()-1 ) && currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getConnectors().get(attacks.get(MeteoritesOrder))==(CANNON.INSTANCE)))){
                    throw new InvalidDefenceEceptiopn("this cannon isn't aiming at the meteorite");
                }
                else if (currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getComponent().getType() == 2) {
                    if(EnergyStorage==null){
                        throw new InvalidDefenceEceptiopn("you have to consume energy to use this cannon");
                    }
                    try {
                        currentBoard.performAction(tiles[EnergyStorage.getFirst()][EnergyStorage.getSecond()].getComponent(), new UseEnergyAction(currentBoard), new DefendingFromLarge());

                    }
                    catch (Exception e){ // potrei splittare la catch in no energia e coord sbagliata
                        throw new ImpossibleBoardChangeException("There was no energy to use");}
                }
                this.sendRandomEffect(player.GetID(),new LogEvent("you defended your ship in " +hits.get(player.GetID()).getFirst()+" "+hits.get(player.GetID()).getSecond(),hits.get(currentPlayer.GetID()).getFirst(),hits.get(currentPlayer.GetID()).getSecond(),attacks.get(MeteoritesOrder),1));
                System.out.println(player.GetID()+" Defended From Large");
                player.setState(new Waiting());
            }
            else if (attacks.get(MeteoritesOrder) == 1 || attacks.get(MeteoritesOrder) == 3){//sopra o sotto
                 if(!((CannonCoord.getSecond() == hits.get(player.GetID()).getSecond() && currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getConnectors().get(attacks.get(MeteoritesOrder))==(CANNON.INSTANCE))) ){
                     throw new InvalidDefenceEceptiopn("this cannon isn't aiming at the meteorite");
                 }
                 else if (currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getComponent().getType() == 2) {
                     if(EnergyStorage==null){
                         throw new InvalidDefenceEceptiopn("you have to consume energy to use this cannon");
                     }
                     try {
                         currentBoard.performAction(tiles[EnergyStorage.getFirst()][EnergyStorage.getSecond()].getComponent(),
                                 new UseEnergyAction(currentBoard), new DefendingFromLarge());


                     }
                 catch (Exception e){ // potrei splittare la catch in no energia e coord sbagliata
                     throw new ImpossibleBoardChangeException("There was no energy to use the coordinates are " +EnergyStorage.getFirst() + " " + EnergyStorage.getSecond()+" the type of componet is "+tiles[EnergyStorage.getFirst()][EnergyStorage.getSecond()].getComponent().getClass());}
                 }
                this.sendRandomEffect(player.GetID(),new LogEvent("you defended your ship in " +hits.get(player.GetID()).getFirst()+" "+hits.get(player.GetID()).getSecond(),hits.get(currentPlayer.GetID()).getFirst(),hits.get(currentPlayer.GetID()).getSecond(),attacks.get(MeteoritesOrder),1));
                System.out.println(player.GetID()+" Defended From Large");
                player.setState(new Waiting());
            }
        }
        else  {
            currentBoard.destroy(hits.get(player.GetID()).getFirst(), hits.get(player.GetID()).getSecond());
            currentBoard.handleAttack(hits.get(player.GetID()).getFirst(), hits.get(player.GetID()).getSecond());
            System.out.println("destryoyed: "+hits.get(player.GetID()).getFirst()+" "+hits.get(player.GetID()).getSecond()+" of:"+player.GetID());

            this.sendRandomEffect(player.GetID(),new LogEvent("your ship got destroyed in " +hits.get(player.GetID()).getFirst()+" "+hits.get(player.GetID()).getSecond(),hits.get(currentPlayer.GetID()).getFirst(),hits.get(currentPlayer.GetID()).getSecond(),attacks.get(MeteoritesOrder),1));
            player.setState(new Waiting());

            if (currentBoard.getBroken()){
                System.out.println("\nrottura nave\n");

                System.out.println(" rottura in "+hits.get(player.GetID()).getFirst()+" "+hits.get(player.GetID()).getSecond());

                player.setState(new HandleDestruction());
                this.sendRandomEffect(player.GetID(),new LogEvent("your ship got broken into parts, select a chunk to keep",-1,-1,-1,-1));

                System.out.println("Stato del player "+ player.getPlayerState().getClass().getName());
                return;

            }
        }
        this.SuccessfulDefences++;
        this.updateStates();
    }

    /**
     * Completes the operations associated with the "Meteorites" card and transitions the
     * game to the next phase. This method is called when the card's effect sequence has been finished.
     *
     * The method performs the following actions:
     *
     * 1. Retrieves the attached {@code ConcurrentCardListener} instance and notifies it
     *    that the concurrent phase associated with this card has ended by invoking
     *    {@code onConcurrentCard(false)}.
     *
     * 2. Invokes the {@code checkLosers()} method to evaluate the current state of the players
     *    and validate if any players need to be removed from the game based on specific conditions.
     *
     * 3. Outputs a message to provide debugging or logging feedback for developers, indicating
     *    that the card's processing has concluded.
     *
     * 4. Updates the internal state of the card by marking it as finished using {@code setFinished(true)}.
     */
    @Override
    public void finishCard() {
        ConcurrentCardListener concurrentCardListener = this.getConcurrentCardListener();
        concurrentCardListener.onConcurrentCard(false);

        checkLosers();
        System.out.println("card finished\n");
        this.setFinished(true);
    }

    /**
     * Sets the order in which meteorite attacks are executed.
     *
     * @param meteoritesOrder the order of the meteorite attacks, represented as an integer.
     */
    public void setMeteoritesOrder(int meteoritesOrder) {
        MeteoritesOrder = meteoritesOrder;
    }

    /**
     * Sets the order of the player for the gameplay.
     *
     * @param playerOrder the order of the player, represented as an integer.
     */
    public void setPlayerOrder(int playerOrder) {
        PlayerOrder = playerOrder;
    }

    /**
     * Retrieves the hitting coordinates of a meteorite impact from the player's board.
     *
     * This method fetches the hit location corresponding to the unique identifier
     * of the specified player. The retrieved hit location is represented as an
     * {@code IntegerPair}.
     *
     * @param p The player for whom the meteorite impact location is being retrieved.
     *          This is represented as a {@code Player} object.
     * @return An {@code IntegerPair} object representing the coordinates of the player's
     *         hit location. Returns {@code null} if no hit is recorded for the given player.
     */
    public IntegerPair getHit(Player p) {
        return hits.get(p.GetID());
    }

    /**
     * Updates the hit coordinates for a specific player on the game board.
     *
     * This method sets the X and Y coordinates where a meteorite hit has occurred
     * for the specified player. The player's unique ID is used to identify and
     * update their hit data within the internal tracking structure.
     *
     * @param x the X-coordinate of the meteorite impact
     * @param y the Y-coordinate of the meteorite impact
     * @param p the player for whom the hit is being recorded
     */
    public void setHit(int x, int y, Player p) {
        this.hits.get(p.GetID()).setValue(x,y);
    }

    /**
     * Sets the current player in the game.
     *
     * @param currentPlayer the Player object representing the current player
     *                      to be set for the meteorite-related game state.
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Retrieves the order in which meteorite attacks are executed during the game.
     *
     * @return the integer representing the meteorites order.
     */
    public int getMeteoritesOrder() {
        return MeteoritesOrder;
    }

    /**
     * Sets the ConcurrentCardListener for handling concurrent card events.
     *
     * @param listener the ConcurrentCardListener instance to be set
     */
    @Override
    public void setConcurrentCardListener(ConcurrentCardListener listener){
        concurrentCardListener = listener;

        this.getConcurrentCardListener().onConcurrentCard(true);
    }

    /**
     * Retrieves the collection of hits stored by the system.
     *
     * @return a HashMap where the key is a String representing the identifier,
     * and the value is an IntegerPair instance containing associated numerical data.
     */
    public HashMap<String, IntegerPair> getHits() {
        return hits;
    }

    /**
     * Sets the hits data with the provided map.
     *
     * @param hits A HashMap where the keys are Strings representing the item identifiers,
     *             and the values are IntegerPair objects representing associated data.
     */
    public void setHits(HashMap<String, IntegerPair> hits) {
        this.hits = hits;
    }


    //json required
    /**
     * Default constructor for the Meteorites class.
     * This constructor initializes an instance of the Meteorites class.
     * No parameters are required to create an instance using this constructor.
     *
     * Note: The presence of this constructor indicates the use of JSON
     * functionalities associated with the Meteorites class.
     */
    public Meteorites() {}
    /**
     * Retrieves the list of attacks.
     *
     * @return an ArrayList of Integer representing the attacks.
     */
    public ArrayList<Integer> getAttacks() {return attacks;}
    /**
     * Sets the list of attacks for this object.
     *
     * @param attacks the list of attack identifiers to be set, represented as an ArrayList of integers
     */
    @JsonCreator
    public void setAttacks(ArrayList<Integer> attacks) {this.attacks = attacks;}
}