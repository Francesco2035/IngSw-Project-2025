package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Exceptions.*;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Boards.Actions.KillCrewAction;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;

//import org.example.galaxy_trucker.Model.InputHandlers.GiveAttack;
//import org.example.galaxy_trucker.Model.InputHandlers.GiveSpeed;
//import org.example.galaxy_trucker.Model.InputHandlers.Killing;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.Tiles.PowerCenter;
import org.example.galaxy_trucker.Model.Tiles.Storage;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.min;
import static java.util.Collections.max;

/**
 * Represents a Warzone card in the Galaxy Trucker game.
 * This card creates combat scenarios where players must compete in various challenges
 * and face punishments based on their performance.
 *
 * The warzone operates in phases:
 * 1. Challenge phase - players compete in attack power, speed, or crew count
 * 2. Punishment phase - the worst performer faces consequences like losing time, crew, cargo, or taking damage
 *
 * Requirements are checked in order: cannons (1), movement (2), humans (3)
 * Punishments are applied in order: movement (1), humans (2), cargo (3), shots (4)
 *
 * @author Galaxy Trucker Development Team
 * @version 1.0
 */

public class Warzone extends Card{

    /**
     * Array defining the order of requirement checks for challenges.
     * Values: 1 = cannons/attack, 2 = movement/speed, 3 = humans/crew
     */

    @JsonProperty("RequirementOrder")
    private int[] RequirementsType;

    /**
     * Array defining the order of punishment types to be applied.
     * Values: 1 = movement penalty, 2 = kill humans, 3 = lose cargo, 4 = take shots
     */

    @JsonProperty("PunishmentOrder")
    private int[] PunishmentType;
    /**
     * The amount of time/movement to be lost as punishment.
     */


    @JsonProperty("Punishment1")
    private int PunishmentMovement;
    /**
     * The number of crew members to be killed as punishment.
     */


    @JsonProperty("Punishment2")
    private int PunishmentHumans;
    /**
     * The amount of cargo to be lost as punishment.
     */


    @JsonProperty("Punishment3")
    private int PunishmentCargo;

    /**
     * List of shot parameters for combat punishment.
     * Pairs of values: direction and size for each shot.
     */

    @JsonProperty("Punishment4")
    private ArrayList<Integer> PunishmentShots;

    /**
     * The player currently being evaluated in the challenge phase.
     */

    private  Player currentPlayer;

    /**
     * The player with the worst performance who will face punishment.
     */

    private Player Worst;

    /**
     * The minimum value recorded during the current challenge.
     */

    private double Minimum;

    /**
     * Index tracking which player is currently being evaluated.
     */

    private int PlayerOrder;

    /**
     * Index tracking which challenge is currently active.
     */

    private int ChallengeOrder;

    /**
     * Index tracking the current shot in the punishment sequence.
     */

    private int ShotsOrder;

    /**
     * The line number for the current shot.
     */

    private int ShotsLine;

    /**
     * Coordinates of the last hit during combat.
     */

    private IntegerPair hit;

    /**
     * Array of dice roll results determining shot trajectories.
     */

    private int[] lines;

    /**
     * The current power value being evaluated.
     */

    private double currentpower;

    /**
     * The current movement value being evaluated.
     */

    private int currentmovement;

    /**
     * The amount of energy required for the current operation.
     */


    private int energyUsage;

    /**
     * Message string for player notifications.
     */

    private String message;

    /**
     * Flag indicating whether the current operation is a punishment.
     */


    boolean isaPunishment;

    /**
     * Temporary storage for punishment values during processing.
     */


    int tmpPunishment;

    /**
     * List of players who have lost the game during this warzone.
     */

    private ArrayList<Player> losers;

    /**
     * Sends a log event to all players indicating the start of a Combat Zone.
     * This method notifies all players that a warzone card has been activated.
     */

    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Combat Zone",-1,-1,-1,-1));
        }
    }




    /**
     * Constructs a new Warzone card with specified parameters.
     *
     * @param level The difficulty level of the card
     * @param time The time cost associated with this card
     * @param board The game board this card operates on
     * @param RequirementOrder Array defining the order of challenges (1=attack, 2=speed, 3=crew)
     * @param PunishmentOrder Array defining the order of punishments (1=time, 2=crew, 3=cargo, 4=shots)
     * @param Punishment1 Amount of time/movement to lose
     * @param Punishment2 Number of crew members to kill
     * @param Punishment3 Amount of cargo to lose
     * @param Punishment4 List of shot parameters (direction and size pairs)
     */

    public Warzone(int level, int time, GameBoard board, int RequirementOrder[], int PunishmentOrder[], int Punishment1, int Punishment2, int Punishment3, ArrayList<Integer> Punishment4) {
        super(level, time, board);
        RequirementsType=RequirementOrder;
        PunishmentType=PunishmentOrder;
        PunishmentMovement=Punishment1;
        PunishmentHumans=Punishment2;
        PunishmentCargo=Punishment3;
        this.PunishmentShots=Punishment4;

        this.PlayerOrder = 0;
        this.ChallengeOrder = 0;
        this.currentPlayer = null;
        this.Worst = null;
        this.Minimum = 10000000;
        this.ShotsOrder = 0;
        this.ShotsLine = 0;
//        this.lines = new int[PunishmentShots.size()/2];
//        for(int i=0;i< PunishmentShots.size()/2;i++){
//            lines[i] = this.getBoard().getPlayers().getFirst().RollDice()-1;
//        }
        this.hit = new IntegerPair(0,0);
        this.currentpower=0;
        this.currentmovement=0;


    }


    /**
     * Activates the main effect of the Warzone card.
     * Initializes the challenge sequence and sets up players for competition.
     * If only one player remains, the card finishes immediately.
     *
     * @throws InterruptedException if the thread is interrupted during execution
     */

    @Override
    public void CardEffect() throws InterruptedException {
        this.Minimum = 100000;
        if(losers==null){
            losers = new ArrayList<>();
        }

        this.lines = new int[PunishmentShots.size()/2];
        for(int i=0;i< PunishmentShots.size()/2;i++){
            lines[i] = this.getBoard().getPlayers().getFirst().RollDice()-1;
        }
        if (this.hit==null) {
            this.hit = new IntegerPair(0,0);

        }
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(new Waiting());
        }

        this.isaPunishment=false;
        if(PlayerList.size() ==1){
            PlayerList.getFirst().setState(new ReadCardState());
            Thread.sleep(3000);
            this.finishCard();
            return;
        }

        this.updateStates();
    }

    /**
     * Updates the state machine managing the warzone progression.
     * Handles the transition between challenge phases and punishment phases.
     * Manages player turns and determines when punishments should be applied.
     *
     * @throws InterruptedException if the thread is interrupted during execution
     */


    @Override
    public void updateStates() throws InterruptedException {
        if(ChallengeOrder<3) {
            this.isaPunishment=false;
            message ="";
            System.out.println("challenge number: " + this.ChallengeOrder);
            GameBoard Board = this.getBoard();
            ArrayList<Player> PlayerList = Board.getPlayers();

            System.out.println("\n player order of the challenge: "+PlayerOrder+"\n");
            if (this.PlayerOrder < PlayerList.size()) {
                if (currentPlayer != null) {
                    currentPlayer.setState(new Waiting());
                }
                currentPlayer = PlayerList.get(this.PlayerOrder);
                PlayerBoard CurrentPlanche = currentPlayer.getmyPlayerBoard();
                if (RequirementsType[ChallengeOrder] == 1) {
                    System.out.println("checking attack of: " + currentPlayer.GetID());
                    this.currentPlayer.setState(new GiveAttack());
                    this.sendRandomEffect(currentPlayer.GetID(),new LogEvent(message,-1,-1,-1,-1));
                    //this.currentPlayer.setInputHandler(new GiveAttack(this));

                }
                else if (RequirementsType[ChallengeOrder] == 2) {
                    System.out.println("checking speed of: " + currentPlayer.GetID());
                    this.currentPlayer.setState(new GiveSpeed());
                    this.sendRandomEffect(currentPlayer.GetID(),new LogEvent(message,-1,-1,-1,-1));
                    //this.currentPlayer.setInputHandler(new GiveSpeed(this));
                    System.out.println("speed order "+PlayerOrder);
                }
                else { //problema qui:)
                    System.out.println("checking people");
                    this.checkPeople();
                    return;
                }


                this.PlayerOrder++;
            } else {
                System.out.println("the worst is: " + Worst.GetID());
                this.PlayerOrder = 0;
                System.out.println("player order : "+this.PlayerOrder);

                /// movement
                if (this.PunishmentType[ChallengeOrder] == 1) {
                    this.Minimum = 100000;
                    this.ChallengeOrder++;

                    this.loseTime();
                    return; // serve perché lose time ri attiva update states al completamento quinid devo fare finta di averlo finito prima di chiamarlo e poi terminarlo appena è finitra la chiamat
                }

                /// killing
                else if (this.PunishmentType[ChallengeOrder] == 2) {
                    this.Minimum = 100000;
                    if(this.currentPlayer.getmyPlayerBoard().getNumHumans()<this.PunishmentHumans){ // dovrebbe bastare a evitare il caso in cui uno è forzato ad uccidere più umani di quanti de abbia
                        losers.add(currentPlayer);
                        this.ChallengeOrder++;
                        this.updateStates();
                        return;
                    }

                    System.out.println(Worst.GetID() + " has to kill" + this.PunishmentHumans);
                    this.sendRandomEffect(Worst.GetID(),new LogEvent(Worst.GetID()+" is the worst and has to kill "+this.PunishmentHumans,-1,-1,-1,-1));
                    this.setDefaultPunishment(this.PunishmentHumans);
                    this.Worst.setState(new Killing());
                    //this.currentPlayer.setInputHandler(new Killing(this));
                }

                /// lose cargo
                else if (this.PunishmentType[ChallengeOrder] == 3) {
                    this.Minimum = 100000;
                    this.sendRandomEffect(Worst.GetID(),new LogEvent(Worst.GetID()+" is the worst and loses "+this.PunishmentCargo+" cargo",-1,-1,-1,-1));

                    //  chiamo il metodo di fottitura eterna :)





                    System.out.println("hai perso :3");
                    PlayerBoard CurrentPlanche = Worst.getmyPlayerBoard();
                    HashMap<Integer, ArrayList<IntegerPair>> cargoH = CurrentPlanche.getStoredGoods();
                    this.tmpPunishment = PunishmentCargo;
                    if (!cargoH.isEmpty()) {

                        this.setDefaultPunishment(tmpPunishment);
                        this.Worst.setState(new HandleTheft());

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
                        this.setDefaultPunishment(energyUsage);
                        Worst.setState(new ConsumingEnergy());
                    }


                }

                /// shots
                else {
                    this.Minimum = 1000000;
                    this.ChallengeOrder++;
//                    this.sendRandomEffect(Worst.GetID(),new LogEvent(Worst.GetID()+" is the worst and has to kill "+this.PunishmentHumans,-1,-1,-1,-1));
//
//                    this.sendRandomEffect(Worst.GetID(),new LogEvent(Worst.GetID()+" is the worst and he is going to get shot at ^_^",-1,-1,-1,-1));
                    Thread.sleep(1000);
                    this.continueCard();
                    return; // stessa cosa di lose time dato che ci torno automaticamente incremento qui che è meglio
                }
                this.Minimum = 1000000;
                this.ChallengeOrder++;
            }
        }
        else{
            System.out.println("all challenges completed");
            this.finishCard();
        }
    }


    /**
     * Finalizes the warzone card execution.
     * Resets all player states to base state and handles any players who lost during the warzone.
     * Marks the card as finished.
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
     * Processes a player's attack power submission for the challenge.
     * Evaluates the power value and energy consumption, then determines if this is the worst performance.
     *
     * @param power The attack power value provided by the player
     * @param numofDouble The number of energy cells that need to be consumed
     * @throws InterruptedException if the thread is interrupted during execution
     */


    @Override
    public void checkPower(double power, int numofDouble) throws InterruptedException {
//            double movement= currentPlayer.getMyPlance().getPower(coordinates);
        System.out.println("Player : "+ currentPlayer.GetID()+ " power "+ power+ " numDouble "+numofDouble);
        this.currentpower = power;
        this.energyUsage=numofDouble;
        if(numofDouble==0){
            this.checkStrength();
        }
        else {

            this.currentPlayer.setState(new ConsumingEnergy());
        }

    }



    /**
     * Processes a player's movement/speed submission for the challenge.
     * Evaluates the movement value and energy consumption, then determines if this is the worst performance.
     *
     * @param movement The movement/speed value provided by the player
     * @param numofDouble The number of energy cells that need to be consumed
     * @throws InterruptedException if the thread is interrupted during execution
     */

    @Override
    public void checkMovement(int movement, int numofDouble) throws InterruptedException {
        System.out.println("start speed order "+PlayerOrder);
//        double movement= currentPlayer.getMyPlance().getEnginePower(coordinates);
        this.currentmovement=movement;
        this.energyUsage=numofDouble;
        if(numofDouble==0){
            this.checkSpeed();
        }
        else {

            this.currentPlayer.setState(new ConsumingEnergy());
        }


    }


    /**
     * Processes energy consumption for challenge requirements or punishments.
     * Validates the energy cell coordinates and consumes energy from the specified locations.
     *
     * @param coordinates List of coordinates where energy should be consumed
     * @throws InterruptedException if the thread is interrupted during execution
     */

    @Override
    public void consumeEnergy(ArrayList<IntegerPair> coordinates) throws InterruptedException {
        if(isaPunishment){
            this.consumeEnergy2(coordinates);
            return;
        }

        if(coordinates==null) {
            System.out.println("coordinates is null");
            if (RequirementsType[ChallengeOrder] == 1) {
                this.currentPlayer.setState(new GiveAttack());
                System.out.println("going back to checking atttack of " + currentPlayer.GetID());
            } else {
                this.currentPlayer.setState(new GiveSpeed());
                System.out.println("going back to checking speed of " + currentPlayer.GetID());
            }
        }



        if(coordinates.size()!=this.energyUsage){

            if(RequirementsType[ChallengeOrder]==1){


                this.currentPlayer.setState(new GiveAttack());
            System.out.println("going back to checking attack of "+currentPlayer.GetID());
            }
            else {
                this.currentPlayer.setState(new GiveSpeed());
            System.out.println("going back to checking speed of "+currentPlayer.GetID());
            }
            throw new WrongNumofEnergyExeption("wrong number of enrgy cells");
            ///  devo fare si che in caso di errore torni alla give attack
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
                if(RequirementsType[ChallengeOrder]==1){
                    this.currentPlayer.setState(new GiveAttack());
                    System.out.println("going back to checking attack of "+currentPlayer.GetID());
                }
                else {
                    this.currentPlayer.setState(new GiveSpeed());
                    System.out.println("going back to checking speed of "+currentPlayer.GetID());
                }
                throw new WrongNumofEnergyExeption("no energy to consume");
            }
        }

        if(RequirementsType[ChallengeOrder]==1) {
            this.checkStrength();
        }
        else{
            this.checkSpeed();
        }
    }

    /**
     * Processes energy consumption specifically for punishment scenarios.
     * This is a separate method for handling energy consumption during cargo loss punishment.
     *
     * @param coordinates List of coordinates where energy should be consumed as punishment
     * @throws InterruptedException if the thread is interrupted during execution
     */


    public void consumeEnergy2(ArrayList<IntegerPair> coordinates) throws InterruptedException {

        if(coordinates.size()!=this.energyUsage){
            System.out.println("----->"+this.energyUsage);
            throw new WrongNumofEnergyExeption("wrong number of energy cells to steal");
        }
        PlayerBoard CurrentPlanche = Worst.getmyPlayerBoard();
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
     * Evaluates and records the current player's attack strength.
     * Compares the strength against the current minimum and updates the worst performer if necessary.
     * Sends appropriate notifications to all players about the results.
     *
     * @throws InterruptedException if the thread is interrupted during execution
     */

    public void checkStrength() throws InterruptedException {
        System.out.println("checking strength of "+currentPlayer.GetID()+ " strength is "+this.currentpower);
        if(this.currentpower<Minimum){
            this.Worst=currentPlayer;
            this.Minimum=this.currentpower;
            System.out.println(currentPlayer.GetID()+" is the worst, the minimum is now "+this.currentpower);

            ArrayList<Player> PlayerListMsg = this.getBoard().getPlayers();
            for(Player p : PlayerListMsg){
                if(p.GetID()== currentPlayer.GetID()){
                    this.sendRandomEffect(p.GetID(),new LogEvent("You are now the worst with "+this.Minimum+" plasma drill power",-1,-1,-1,-1));
                }
                else {
                    this.sendRandomEffect(p.GetID(), new LogEvent(currentPlayer.GetID() + " is now the worst with " + this.Minimum + " plasma drill power", -1, -1, -1, -1));
                }
            }

        }
        this.currentPlayer.setState(new Waiting());
        message= message+currentPlayer.GetID()+"has chosen strength "+this.currentpower +"\n";
        this.updateStates();
    }

    /**
     * Evaluates and records the current player's movement speed.
     * Compares the speed against the current minimum and updates the worst performer if necessary.
     * Sends appropriate notifications to all players about the results.
     *
     * @throws InterruptedException if the thread is interrupted during execution
     */

    public void checkSpeed() throws InterruptedException {
        System.out.println("checking speed of "+currentPlayer.GetID()+" speed is: "+this.currentmovement);
        if(this.currentmovement<Minimum){
            this.Worst=currentPlayer;
            this.Minimum=this.currentmovement;

            ArrayList<Player> PlayerListMsg = this.getBoard().getPlayers();
            for(Player p : PlayerListMsg){
                if(p.GetID()== currentPlayer.GetID()){
                    this.sendRandomEffect(p.GetID(),new LogEvent("You are now the worst with "+this.Minimum+" engine power",-1,-1,-1,-1));
                }
                else {
                    this.sendRandomEffect(p.GetID(), new LogEvent(currentPlayer.GetID() + " is now the worst with " + this.Minimum + " engine power", -1, -1, -1, -1));
                }
            }

        }
        this.currentPlayer.setState(new Waiting());
        message= message+currentPlayer.GetID()+"has chosen strength "+this.currentpower +"\n";
        System.out.println("end  speed order "+PlayerOrder);
        this.updateStates();
    }

    /**
     * Evaluates all players' crew count automatically.
     * Determines which player has the fewest crew members and marks them as the worst performer.
     * This challenge doesn't require player input as crew count is automatically calculated.
     *
     * @throws InterruptedException if the thread is interrupted during execution
     */

    public void checkPeople() throws InterruptedException {
        int Order=0;
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        PlayerBoard CurrentPlanche;
        int Len= PlayerList.size(); // quanti player ho
        int PlayerPower;


        for(int i=0; i<PlayerList.size(); i++){
            CurrentPlanche=PlayerList.get(i).getmyPlayerBoard(); // get the current active planche




            Tile TileBoard[][]=CurrentPlanche.getPlayerBoard();
            int totHumans = CurrentPlanche.getNumHumans();



            if(totHumans<Minimum){
                Worst=PlayerList.get(i);
                Minimum=totHumans;
            }
        }

        ArrayList<Player> PlayerListMsg = this.getBoard().getPlayers();
        for(Player p : PlayerListMsg){
            if(p.GetID()== Worst.GetID()){
                this.sendRandomEffect(p.GetID(),new LogEvent("You were the worst with "+this.Minimum+" people",-1,-1,-1,-1));
            }
            else{
            this.sendRandomEffect(p.GetID(),new LogEvent(currentPlayer.GetID()+" was the worst with "+this.Minimum+" people",-1,-1,-1,-1));
            }
        }

        this.PlayerOrder=PlayerList.size();
        this.updateStates();
    }


    /**
     * Applies the time/movement loss punishment to the worst performing player.
     * Moves the player backward on the game track by the specified punishment amount.
     *
     * @throws InterruptedException if the thread is interrupted during execution
     */

    public void loseTime() throws InterruptedException {
        this.sendRandomEffect(Worst.GetID(),new LogEvent(Worst.GetID()+" is the worst and loses "+this.PunishmentMovement+" time",-1,-1,-1,-1));
        Thread.sleep(1000);
        this.getBoard().movePlayer(Worst.GetID(),-this.PunishmentMovement);
        System.out.println(this.Worst.GetID()+" loses the time");
        this.PlayerOrder=0;
        this.updateStates();

        return;
    }

    /**
     * Legacy method for cargo loss punishment (currently unused).
     * This method was likely replaced by the more comprehensive loseCargo method.
     */

    @Override
    public void killHumans(ArrayList<IntegerPair> coordinates) throws InterruptedException {

        System.out.println("killing " + PunishmentHumans+" humans of "+Worst.GetID());
        /// worst e non current


        if (coordinates.size() != this.PunishmentHumans) {
            //devo dirgli che ha scelto il num sbagliato di persone da shottare
            throw new WrongNumofHumansException("wrong number of humans");
        }

        ///  fai l try catch e opera sulla copia :)
        PlayerBoard curr= this.Worst.getmyPlayerBoard();
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
            throw new ImpossibleBoardChangeException("there was an error in killing humans");

        }


        Worst.setState(new Waiting());
        this.updateStates();
    }

    /**
     * Continues the warzone card execution, specifically handling the shot punishment phase.
     * Processes each shot in the punishment sequence, determining hit locations and damage.
     * Handles both small shots (defendable) and large shots (automatic destruction).
     *
     * @throws InterruptedException if the thread is interrupted during execution
     */

    @Override
    public void continueCard() throws InterruptedException {
        /// cose per stampare al client
        String dimensione= new String();
        String direction = new String();
        String Colpito = new String("missed you");
        String location = new String("");




        int Movement;
        boolean shotsFlag= false;
        while (this.ShotsOrder < PunishmentShots.size() && shotsFlag == false) {

            System.out.println("attack number: "+this.ShotsOrder/2 +" on: "+Worst.GetID());

            PlayerBoard CurrentPlanche = Worst.getmyPlayerBoard(); //prendo plancia
            int[][] MeteoritesValidPlanche = CurrentPlanche.getValidPlayerBoard();//prende matrice validita

            if(PunishmentShots.get(ShotsOrder+1)==0) {
                dimensione = new String("small");

            }
            else {
                dimensione = new String("large");
            }
            if (PunishmentShots.get(ShotsOrder) == 0) { ///sinistra
                direction = new String("the left on line "+lines[ShotsOrder/2]);
                Movement = 0;
                while (Movement < 10  && lines[ShotsOrder/2]<10 && shotsFlag == false) {

                    if (MeteoritesValidPlanche[lines[ShotsOrder / 2]][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                        Colpito= new String("hit you");
                        location = new String("at "+lines[ShotsOrder/2]+" "+Movement);
                        shotsFlag = true;
                        hit.setValue( lines[ShotsOrder / 2],Movement);
                        if(PunishmentShots.get(ShotsOrder+1) == 1){//colpo grande nulla da fare
                            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                            CurrentPlanche.handleAttack(hit.getFirst(), hit.getSecond());
                            if (CurrentPlanche.getBroken()){
                                System.out.println("rottura nave");
                                sendRandomEffect(Worst.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.Worst.setState(new HandleDestruction());
                                this.ShotsOrder+=2;
                                return;

                            }
                            else{
                                this.ShotsOrder+=2;
                                sendRandomEffect(Worst.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.continueCard();
                                return;
                            }


                        }
                        else {//colpo piccolo
                            Worst.setState(new DefendingFromSmall());

                        }
                    }


                    Movement++;
                }
            }
            else if (PunishmentShots.get(ShotsOrder) == 1) {///sopra
                direction = new String("above on column "+lines[ShotsOrder/2]);
                Movement = 0;
                while (Movement < 10 && lines[ShotsOrder/2]<10 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[Movement][lines[ShotsOrder / 2]] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                        Colpito= new String("hit you");
                        location = new String("at "+Movement+" "+lines[ShotsOrder/2]);
                        shotsFlag = true;
                        hit.setValue(Movement, lines[ShotsOrder / 2]);
                        if(PunishmentShots.get(ShotsOrder+1) == 1){//colpo grande nulla da fare

                            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                            CurrentPlanche.handleAttack(hit.getFirst(), hit.getSecond());
                            if (CurrentPlanche.getBroken()){

                                System.out.println("rottura nave");
                                sendRandomEffect(Worst.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.Worst.setState(new HandleDestruction());
                                this.ShotsOrder+=2;
                                return;

                            }
                            else{
                                this.ShotsOrder+=2;
                                sendRandomEffect(Worst.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.continueCard();
                                return;
                            }

                        }
                        else {//colpo piccolo
                            Colpito= new String("hit you");
                            Worst.setState(new DefendingFromSmall());
                        }

                    }

                    Movement++;
                }
            }
            else if (PunishmentShots.get(ShotsOrder) == 2) {// destra
                direction = new String("the right on line "+lines[ShotsOrder/2]);
                Movement = 9;
                while (Movement >= 0   && lines[ShotsOrder/2]<10&& shotsFlag == false) {
                    if (MeteoritesValidPlanche[lines[ShotsOrder / 2]][Movement] > 0) {
                        Colpito= new String("hit you");
                        location = new String("at "+lines[ShotsOrder/2]+" "+Movement);

                        shotsFlag = true;
                        hit.setValue( lines[ShotsOrder/2],Movement);
                        if(PunishmentShots.get(ShotsOrder+1) == 1){//colpo grande nulla da fare

                            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                            CurrentPlanche.handleAttack(hit.getFirst(), hit.getSecond());
                            if (CurrentPlanche.getBroken()){
                                System.out.println("rottura nave");
                                sendRandomEffect(Worst.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.Worst.setState(new HandleDestruction());
                                this.ShotsOrder+=2;
                                return;

                            }
                            else{
                                System.out.println("non si è rotto nulla");
                                this.ShotsOrder+=2;
                                sendRandomEffect(Worst.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.continueCard();
                                return;
                            }

                        }
                        else {//colpo piccolo
                            Colpito= new String("hit you");
                            Worst.setState(new DefendingFromSmall());
                        }

                    }
                    Movement--;
                }

            }
            else { //sotto
                direction = new String("below on column "+lines[ShotsOrder/2]);
                Movement = 9;
                while (Movement >= 0  && lines[ShotsOrder/2]<10 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[Movement][lines[ShotsOrder / 2]] > 0) {
                        Colpito= new String("hit you");
                        location = new String("at "+Movement+" "+lines[ShotsOrder/2]);
                        shotsFlag = true;
                        hit.setValue(Movement, lines[ShotsOrder / 2]);
                        if(PunishmentShots.get(ShotsOrder+1) == 1){//colpo grande nulla da fare
                            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                            CurrentPlanche.handleAttack(hit.getFirst(), hit.getSecond());
                            if (CurrentPlanche.getBroken()){
                                System.out.println("rottura nave");
                                sendRandomEffect(Worst.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.Worst.setState(new HandleDestruction());
                                this.ShotsOrder+=2;
                                return;

                            }
                            else{
                                this.ShotsOrder+=2;
                                sendRandomEffect(Worst.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.continueCard();
                                return;
                            }

                        }
                        else {//colpo piccolo
                            Colpito= new String("hit you");
                            Worst.setState(new DefendingFromSmall());
                        }
                    }


                    Movement--;
                }

            }


            sendRandomEffect(Worst.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,hit.getFirst(),hit.getSecond(),PunishmentShots.get(ShotsOrder),3));
            if(shotsFlag == false){
                this.ShotsOrder += 2;
            }
        }
        if(this.ShotsOrder >=PunishmentShots.size() ){
            this.ShotsOrder = 0;
            Worst.setState(new Waiting());
            this.updateStates();
        }
    }


    /**
     * Defends against a small shot attack during the punishment phase.
     * The player can choose to use energy to activate a shield or take damage.
     * If energy is provided, validates that the shield defends the correct side and consumes energy.
     * If no energy is provided, the hit location is destroyed.
     *
     * @param energy The coordinates of the energy cell to use for defense, or null to take damage
     * @param player The player defending against the attack
     * @throws InterruptedException if the thread is interrupted during execution
     * @throws InvalidDefenceEceptiopn if the shield doesn't defend the correct side
     * @throws ImpossibleBoardChangeException if there's no energy available at the specified location
     */

    @Override
    public void DefendFromSmall(IntegerPair energy, Player player) throws InterruptedException {
        PlayerBoard currentBoard =this.Worst.getmyPlayerBoard();
        Tile[][] tiles =currentBoard.getPlayerBoard();
        if (energy!=null){
            if ((currentBoard.getShield()[PunishmentShots.get(ShotsOrder)]==0)){
                throw new InvalidDefenceEceptiopn("this shield defends the wrong side"+" the side was: "+getPunishmentShots().get(ShotsOrder));
            }
            else {
                try {
                    currentBoard.performAction(tiles[energy.getFirst()][energy.getSecond()].getComponent(),new UseEnergyAction(currentBoard), new ConsumingEnergy());
                }
                catch (Exception e){
                    throw new ImpossibleBoardChangeException("There was no energy to use here");
                }
                this.sendRandomEffect(Worst.GetID(),new LogEvent("you defended your ship in " +hit.getFirst()+" "+hit.getSecond(),hit.getFirst(),hit.getSecond(),PunishmentShots.get(ShotsOrder),3));
                System.out.println(Worst.GetID()+" Defended From Small");
                player.setState(new Waiting());
            }
        }
        else {
            currentBoard.destroy(hit.getFirst(), hit.getSecond());
            currentBoard.handleAttack(hit.getFirst(), hit.getSecond());
            this.sendRandomEffect(Worst.GetID(),new LogEvent("your ship got destroyed in " +hit.getFirst()+" "+hit.getSecond(),hit.getFirst(),hit.getSecond(),PunishmentShots.get(ShotsOrder),3));
            if (currentBoard.getBroken()){
                System.out.println("rottura nave");
                this.Worst.setState(new HandleDestruction());
                this.sendRandomEffect(Worst.GetID(),new LogEvent("your ship got broken into parts, select a chunk to keep",-1,-1,-1,-1));
                return;

            }
            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
        }
        this.ShotsOrder+=2;
        this.continueCard();

    }


    /**
     * Continues the warzone card execution after handling destruction or other interruptions.
     * This method resumes the shot punishment sequence where it left off.
     *
     * @throws InterruptedException if the thread is interrupted during execution
     */

    @Override
    public void keepGoing() throws InterruptedException {
        continueCard();
    }

    /**
     * Handles the cargo loss punishment by forcing the player to give up their most valuable goods.
     * The player must select cargo from storage compartments in order of highest value first.
     * If no cargo remains, energy is consumed instead as an alternative punishment.
     *
     * @param coord The coordinates of the storage compartment containing the cargo to lose
     * @param index The index of the specific cargo item within the storage compartment
     * @throws InterruptedException if the thread is interrupted during execution
     * @throws InvalidInput if the coordinates don't contain a storage or the index is invalid
     */

    @Override
    public void loseCargo(IntegerPair coord,int index) throws InterruptedException {
        PlayerBoard CurrentPlanche =Worst.getmyPlayerBoard();
        ArrayList<Storage> storages=CurrentPlanche.getStorages();
        Tile tiles[][]=CurrentPlanche.getPlayerBoard();


        HashMap<Integer,ArrayList<IntegerPair>> cargoH= CurrentPlanche.getStoredGoods();

        int max;
        max= max(cargoH.keySet());

        if(cargoH.isEmpty()){
            energyUsage=min(tmpPunishment,CurrentPlanche.getEnergy());
            System.out.println("energy usage (lose Cargo) :" +energyUsage);
            this.setDefaultPunishment(energyUsage);
            this.isaPunishment=true;
            Worst.setState(new ConsumingEnergy()); // potrebbe non fare l'update?
            this.setDefaultPunishment(energyUsage);
            return;
        }

        if(CurrentPlanche.getTile(coord.getFirst(),coord.getSecond())==null ){
            throw new InvalidInput("there is nothing here ");
        }
        else {
            if (storages.contains(CurrentPlanche.getTile(coord.getFirst(),coord.getSecond()).getComponent())){
                int i=storages.indexOf(CurrentPlanche.getTile(coord.getFirst(),coord.getSecond()).getComponent()); //per prendere l'iesimo elemento devo prima prenderne l'indice da storgaes fando indexof elemet e poi get i, non mi basta usare il primo perche il primo è component mentre preso dalla get lo considero come storage
                Storage currStorage=storages.get(i);
                if (index>=currStorage.getGoods().size() || index<0){
                    throw new InvalidInput("there is nothing at this position in the storage");
                }
                if(currStorage.getValue(index)==max){//iterator.next da il primo elemento non chiederti perché
                    CurrentPlanche.performAction(tiles[coord.getFirst()][coord.getSecond()].getComponent(), new GetGoodAction(index,CurrentPlanche,coord.getFirst(),coord.getSecond()),new HandleTheft());
                    this.tmpPunishment--;
                    this.setDefaultPunishment(tmpPunishment);



                }
                else {
                    throw new InvalidInput("this isnt the most valuable good you own");
                }

            }
            else {
                throw new InvalidInput("this isn't a storage ");
            }
        }
        if(tmpPunishment==0){
            System.out.println("finished stealing");
            Worst.setState(new Waiting());
            this.updateStates();
            return;
        }
        if(cargoH.isEmpty()){
            energyUsage=min(tmpPunishment,CurrentPlanche.getEnergy());
            this.setDefaultPunishment(energyUsage);
            this.isaPunishment=true;
            Worst.setState(new ConsumingEnergy()); // potrebbe non fare l'update?
            this.setDefaultPunishment(energyUsage);
            return;
        }
        else {
            Worst.setState(new HandleTheft());
        }

    }




    /**
     * Default constructor required for JSON deserialization.
     * Initializes a Warzone object with default values.
     */

    public Warzone() {}

    /**
     * Gets the array defining the order of requirement checks for challenges.
     *
     * @return Array where 1=cannons/attack, 2=movement/speed, 3=humans/crew
     */

    public int[] getRequirementsType() {
        return RequirementsType;
    }

    /**
     * Sets the requirements type for challenges.
     * This method creates a single-element array from the provided value.
     *
     * @param requirementsType The requirement type (1=attack, 2=speed, 3=crew)
     */

    public void setRequirementsType(int requirementsType) {RequirementsType = new int[]{requirementsType};}

    /**
     * Gets the array defining the order of punishment types to be applied.
     *
     * @return Array where 1=movement penalty, 2=kill humans, 3=lose cargo, 4=take shots
     */

    public int[] getPunishmentType() {
        return PunishmentType;
    }

    /**
     * Sets the punishment type order.
     * This method creates a single-element array from the provided value.
     *
     * @param punishmentType The punishment type (1=time, 2=crew, 3=cargo, 4=shots)
     */

    public void setPunishmentType(int punishmentType) {
        PunishmentType = new int[]{punishmentType};
    }

    /**
     * Gets the amount of time/movement to be lost as punishment.
     *
     * @return The movement penalty value
     */

    public int getPunishmentMovement() {
        return PunishmentMovement;
    }

    /**
     * Sets the movement penalty for time-based punishment.
     *
     * @param punishmentMovement The amount of time/movement to lose
     */

    public void setPunishmentMovement(int punishmentMovement) {
        PunishmentMovement = punishmentMovement;
    }

    /**
     * Gets the number of crew members to be killed as punishment.
     *
     * @return The number of humans to kill
     */

    public int getPunishmentHumans() {
        return PunishmentHumans;
    }

    /**
     * Sets the crew killing penalty.
     *
     * @param punishmentHumans The number of crew members to kill
     */


    public void setPunishmentHumans(int punishmentHumans) {
        PunishmentHumans = punishmentHumans;
    }

    /**
     * Gets the amount of cargo to be lost as punishment.
     *
     * @return The cargo loss penalty value
     */


    public int getPunishmentCargo() {
        return PunishmentCargo;
    }

    /**
     * Sets the cargo loss penalty.
     *
     * @param punishmentCargo The amount of cargo to lose
     */

    public void setPunishmentCargo(int punishmentCargo) {
        PunishmentCargo = punishmentCargo;
    }

    /**
     * Gets the list of shot parameters for combat punishment.
     * Each pair of values represents direction and size for each shot.
     *
     * @return ArrayList containing shot parameters (direction, size pairs)
     */

    public ArrayList<Integer> getPunishmentShots() {
        return PunishmentShots;
    }

    /**
     * Sets the shot punishment parameters.
     *
     * @param punishmentShots ArrayList of shot parameters where each pair represents (direction, size)
     */
    public void setPunishmentShots(ArrayList<Integer> punishmentShots) {
        PunishmentShots = punishmentShots;
    }
}