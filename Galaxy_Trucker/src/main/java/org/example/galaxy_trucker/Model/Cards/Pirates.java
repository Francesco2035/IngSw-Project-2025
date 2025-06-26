package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
//import org.example.galaxy_trucker.Model.InputHandlers.Accept;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Exceptions.ImpossibleBoardChangeException;
import org.example.galaxy_trucker.Exceptions.InvalidDefenceEceptiopn;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Exceptions.WrongNumofEnergyExeption;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;


/**
 * The Pirates class represents a card in the game, derived from the base Card class.
 * It manages the logic associated with pirate attacks, rewards, punishments, player actions,
 * and various in-game states related to the pirate card.
 */
public class Pirates extends Card{
    /**
     * Represents the minimum requirement a player must meet to achieve certain game objectives
     * or actions within the Pirates game. This variable is essential in game mechanics to determine
     * whether certain conditions or thresholds have been fulfilled.
     */
    private int requirement;
    /**
     * Represents the amount of treasure or compensation that a player can earn or receive
     * under specific conditions in the game.
     *
     * This variable is used to track the reward associated with the player's performance
     * during the game or after certain actions or events.
     */
    private int reward;
    /**
     * Represents the order of the player in the game, determining their turn sequence.
     * This variable is used to identify the specific player position in the gameplay.
     */
    private int PlayerOrder;
    /**
     * Represents the number of shots ordered or activated during a player's turn.
     * This variable tracks the order or sequence of shot-related actions
     * within the gameplay of the Pirates class.
     */
    private int ShotsOrder;
    /**
     * Represents the line of shots in the Pirates game. This field may indicate a specific row or configuration of fired shots
     * associated with the pirate mechanics during gameplay. Its value can influence interactions or specific game logic related
     * to shot placements or outcomes.
     */
    private int ShotsLine;
    /**
     * Represents the number of shots fired and successfully landed during the player's turn.
     * The variable is an instance of {@link IntegerPair}, where the first integer
     * typically denotes the number of shots fired, and the second integer represents
     * the number of successful hits.
     *
     * This field is private and primarily used internally within the {@code Pirates} class
     * to track the player's performance in combat scenarios.
     */
    private IntegerPair hit;
    /**
     * A boolean variable indicating whether the Pirates have been defeated.
     * It is used to track the state of defeat within the context of the game.
     */
    private  boolean defeated;
    /**
     * Represents the order in which actions or events occur within the game.
     * This variable is likely used to track or manage the sequential order
     * of execution or gameplay among players or events in the Pirates game.
     */
    private int order;
    /**
     * Represents the current player in the game.
     * This variable tracks which player is active or performing actions at a given time.
     * It is primarily used to manage gameplay logic and ensure the flow of the game's turn-based mechanics.
     */
    private Player currentPlayer;
    /**
     * Represents an array of lines associated with a game's state or actions.
     * It may be used for tracking or organizing positional data or sequences
     * relevant to the gameplay mechanics.
     */
    private int[] lines;
    /**
     * Represents the current power level of the Pirates, typically used to quantify
     * the power available for offensive or defensive actions during gameplay.
     *
     * This variable is modified or assessed in various methods such as `checkPower`,
     * which evaluates the power relative to certain conditions, and `DefendFromSmall`,
     * which utilizes this power level for defensive maneuvers.
     *
     * The `currentpower` value is directly influenced by gameplay mechanics, including
     * interactions with other players, card effects, and energy consumption.
     */
    private double currentpower;
    /**
     * Represents the energy consumption level of the pirates in the game.
     * This variable is used to track the amount of energy utilized during
     * various actions or events within the gameplay.
     */
    private int energyUsage;
    /**
     * Represents a collection of players who have been defeated or have lost in the game.
     * This list keeps track of the players that are no longer active or successful during gameplay.
     */
    ArrayList<Player> losers;

    @JsonProperty("punishment")
    private ArrayList<Integer> Punishment;


    /**
     * Constructor for the Pirates class. Initializes the pirates card with specific parameters
     * such as level, time, game board, reward, requirement, and punishment values.
     *
     * @param level        The level or difficulty associated with the Pirates card.
     * @param time         The time allocated for the card execution.
     * @param board        The GameBoard to which this card belongs.
     * @param Reward       The reward to be given if the conditions for this card are met.
     * @param Requirement  The requirement needed to successfully pass this card.
     * @param Punsihment   A list of punishments to be applied if the card's conditions are not met.
     */
    public Pirates(int level, int time, GameBoard board, int Reward, int Requirement, ArrayList<Integer> Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.reward = Reward;
        this.Punishment = Punsihment;
        this.ShotsOrder = 0;
        this.ShotsLine = 0;

        this.defeated = false;
        this.currentPlayer = new Player();
//        this.lines = new int[Punsihment.size()/2];
//        for(int i=0;i< Punishment.size()/2;i++){
//            lines[i] = this.getBoard().getPlayers().getFirst().RollDice()-1;
//        }
        this.hit = new IntegerPair(0,0);
        this.currentpower = 0;
        this.energyUsage = 0;
        this.order = 0;


    }

    /**
     * Sends a type log to all players in the game by iterating through the list of players,
     * retrieved from the game board, and applying a random effect encapsulated in a LogEvent object.
     *
     * The LogEvent applied has the type "Pirates" with placeholder parameters -1 indicating default or undefined values.
     * This method utilizes the sendRandomEffect process for each player's unique ID.
     *
     * The associated game board is accessed using the {@code getBoard()} method, and the list of players
     * is retrieved using {@code getPlayers()}. Each player's effects are processed in the current game state.
     */
    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Pirates",-1,-1,-1,-1));
        }
    }


 /**
  * Executes the effects of the current card in the game. This method applies
  * specific penalties, updates player states, and prepares the game board for
  * the next stage based on the card's defined logic.
  *
  * When this method is invoked:
  * 1. Prepares a list of penalties and initializes relevant data structures.
  * 2. Generates random lines based on dice rolls to define punishment areas.
  * 3. Sets up the initial hit point pair with default zero values.
  * 4. Updates the state of all players to "Waiting" to signal the transition
  *    to a waiting phase.
  * 5. Triggers a state update on the card, ensuring all internal statuses reflect
  *    the changes made.
  *
  * The method leverages the game's board and player list to execute its logic.
  * It assumes valid initialization of essential class fields and valid player
  * and card configurations.
  */
 @Override
    public void CardEffect(){
     losers = new ArrayList<>();
     this.lines = new int[this.Punishment.size()/2];
     for(int i=0;i< Punishment.size()/2;i++){
         lines[i] = this.getBoard().getPlayers().getFirst().RollDice()-1;
     }

        this.hit =new IntegerPair(0,0);
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(new Waiting());
        }
        this.updateStates();
    }

    /**
     * Updates the state of the game, transitioning to the next player or completing the card's actions.
     * This method manages player turn transitions and updates the states associated with the current player.
     *
     * Logic includes:
     * - Fetching the game board and the list of players.
     * - If the current player order is within the player list size and the current player is not defeated:
     *   - Updates the previous player's state to "Waiting".
     *   - Sets the current player based on the order and updates the current player's state to "GiveAttack".
     *   - Resets the current power to 0.
     *   - Increments the player order to move to the next player.
     * - If all players have finished their turns or no further actions are possible, it calls the `finishCard` method.
     *
     * Prints debugging information about the current player transition and state updates.
     */
    @Override
    public void updateStates(){
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        System.out.println("player number "+this.order);
        if(this.order<PlayerList.size() && !this.defeated){
            if (currentPlayer != null) {currentPlayer.setState(new Waiting());}

            currentPlayer = PlayerList.get(this.order);
            System.out.println("current is:"+currentPlayer.GetID());
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
     * Checks and updates the power level and energy usage for a player. The method evaluates
     * the current state based on the provided number of doubles (`numofDouble`) and triggers
     * actions such as state transitions or strength checks.
     *
     * @param power       The current power level associated with the player's state.
     * @param numofDouble The number of "double" occurrences affecting the energy usage;
     *                    if it is zero, the `checkStrength` method is called, otherwise
     *                    the player's state is updated to `ConsumingEnergy`.
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
     * Consumes the energy necessary to perform a specific action in the game. This method ensures
     * that the energy requirements are met before proceeding and handles invalid inputs or incorrect
     * energy usage scenarios by updating the player's state and throwing relevant exceptions.
     *
     * @param coordinates A list of integer pairs representing the coordinates of the energy points
     *                    to be consumed. Each pair specifies the row and column positions on the
     *                    player's board.
     * @throws IllegalArgumentException If the provided coordinates list is null.
     * @throws WrongNumofEnergyExeption If the number of provided coordinates does not match the
     *                                  energy usage requirement, or if invalid energy points
     *                                  are specified.
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
        for(IntegerPair i:coordinates){
            try {
                CurrentPlanche.performAction(tiles[i.getFirst()][i.getSecond()].getComponent(),
                        new UseEnergyAction(CurrentPlanche), new ConsumingEnergy());
            }
            catch (InvalidInput e){
                currentPlayer.setState(new GiveAttack());
                throw new WrongNumofEnergyExeption("no energy here:"+ i.getFirst()+ " "+i.getSecond());
            }
        }
        this.checkStrength();

    }

    /**
     * Evaluates the strength of the current player against the defined requirement and determines the outcome.
     * Based on the evaluation, the method transitions the current player's state and updates the game flow.
     *
     * Behavior:
     * - Prints the current player's ID, their current power, and the required strength.
     * - If the current player's power exceeds the required strength:
     *   - Declares the player as the winner and sets the defeated flag to true.
     *   - Updates the current player's state to "Accepting".
     * - If the current player's power is less than the required strength:
     *   - Declares the player as the loser.
     *   - Calls the `continueCard` method to prompt further game actions based on this outcome.
     * - If the current player's power matches the required strength:
     *   - Declares the result as a tie.
     *   - Updates the current player's state to "Waiting".
     *   - Calls the `updateStates` method to progress the game to the next stage.
     *
     * This method assumes that the current player's ID, power levels, and state-changing logic are correctly
     * initialized and accessible.
     */
    public void checkStrength(){

        System.out.println("strength of "+currentPlayer.GetID()+" is: "+this.currentpower+" required: "+this.requirement);
        if(this.currentpower>this.getRequirement()){
            System.out.println(currentPlayer.GetID()+" won");
            this.defeated=true;
            this.currentPlayer.setState(new Accepting());
            //this.currentPlayer.setInputHandler(new Accept(this));
        }
        else if (this.currentpower<this.getRequirement()){
            System.out.println(currentPlayer.GetID()+" lost");
            this.continueCard();
        }
        else {
            System.out.println(currentPlayer.GetID()+" was even");
            this.currentPlayer.setState(new Waiting());
            this.updateStates();
        }


    }


    /**
     * Executes the "continue card" action, resolving attacks and handling player board states based on the
     * punishment sequence and game rules. This method iterates through a series of attacks, determining the
     * direction, size, and validity of each shot, and applies their effects on the current player's board.
     *
     * Key functionality includes:
     * - Determining the attack direction, size (small or large), and impact location based on the punishment sequence.
     * - Resolving consequences of successful attacks, such as destroying cells on the player's board, handling ship
     *   destruction, and triggering related player states.
     * - Managing defense mechanisms depending on the attack size and the current board state.
     * - Sending log events to track what happened during the attack sequence.
     * - Handling transitions between attack phases and resetting the attack sequence when all punishments are resolved.
     *
     * The method interacts with several components such as:
     * - The player's board to analyze the validity of impacts and apply damage.
     * - The punishment list to retrieve attack specifications.
     * - Current player states to update based on attack outcomes (e.g., ship destruction, defense states).
     */
    @Override
    public void continueCard() {
        String dimensione= new String();
        String direction = new String();
        String Colpito = new String("missed you");
        String location = new String("");




        int Movement;
        boolean shotsFlag= false;
            while (this.ShotsOrder < Punishment.size() && shotsFlag == false) {

                System.out.println("attack number: "+this.ShotsOrder/2 +" on: "+currentPlayer.GetID());

            PlayerBoard CurrentPlanche = currentPlayer.getmyPlayerBoard(); //prendo plancia
            int[][] MeteoritesValidPlanche = CurrentPlanche.getValidPlayerBoard();//prende matrice validita

                if(Punishment.get(ShotsOrder+1)==0) {
                    dimensione = new String("small");

                }
                else {
                    dimensione = new String("large");
                }

                location = "";
            if (Punishment.get(ShotsOrder) == 0) { ///sinistra
                direction = new String("the left on line "+lines[ShotsOrder/2]);
                Movement = 0;
                while (Movement < 10  && lines[ShotsOrder/2]<10 && shotsFlag == false) {

                    if (MeteoritesValidPlanche[lines[ShotsOrder / 2]][Movement] > 0) {
                        Colpito= new String("hit you");
                        location = new String("at "+lines[ShotsOrder/2]+" "+Movement);
                            shotsFlag = true;
                            hit.setValue(Movement, lines[ShotsOrder / 2]);
                        if(Punishment.get(ShotsOrder+1) == 1){//colpo grande nulla da fare
                            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                            CurrentPlanche.handleAttack(hit.getFirst(), hit.getSecond());
                            if (CurrentPlanche.getBroken()){
                                System.out.println("rottura nave");
                                sendRandomEffect(currentPlayer.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.sendRandomEffect(currentPlayer.GetID(),new LogEvent("your ship got broken into parts, select a chunk to keep",-1,-1,-1,-1));
                                this.currentPlayer.setState(new HandleDestruction());
                                return;

                            }
                            else{
                                this.ShotsOrder+=2;
                                sendRandomEffect(currentPlayer.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.continueCard();
                                return;
                            }


                        }
                        else {
                            currentPlayer.setState(new DefendingFromSmall());

                        }
                        }


                    Movement++;
                }
            }
            else if (Punishment.get(ShotsOrder) == 1) {///sopra
                direction = new String("above on column "+lines[ShotsOrder/2]);
                Movement = 0;
                while (Movement < 10 && lines[ShotsOrder/2]<10 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[Movement][lines[ShotsOrder / 2]] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                        Colpito= new String("hit you");
                        location = new String("at "+Movement+" "+lines[ShotsOrder/2]);
                            shotsFlag = true;
                            hit.setValue(Movement, lines[ShotsOrder / 2]);
                        if(Punishment.get(ShotsOrder+1) == 1){//colpo grande nulla da fare

                            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                            CurrentPlanche.handleAttack(hit.getFirst(), hit.getSecond());
                            if (CurrentPlanche.getBroken()){
                                System.out.println("rottura nave");
                                sendRandomEffect(currentPlayer.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.sendRandomEffect(currentPlayer.GetID(),new LogEvent("your ship got broken into parts, select a chunk to keep",-1,-1,-1,-1));

                                this.currentPlayer.setState(new HandleDestruction());
                                return;

                            }
                            else{
                                this.ShotsOrder+=2;
                                sendRandomEffect(currentPlayer.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.continueCard();
                                return;
                            }

                        }
                        else {//colpo piccolo

                            currentPlayer.setState(new DefendingFromSmall());
                        }

                    }

                    Movement++;
                }
            }
            else if (Punishment.get(ShotsOrder) == 2) {// destra
                direction = new String("the right on line "+lines[ShotsOrder/2]);
                Movement = 9;
                while (Movement >= 0   && lines[ShotsOrder/2]<10&& shotsFlag == false) {
                    if (MeteoritesValidPlanche[lines[ShotsOrder / 2]][Movement] > 0) {
                        Colpito= new String("hit you");
                        location = new String("at "+lines[ShotsOrder/2]+" "+Movement);

                            shotsFlag = true;
                            hit.setValue(Movement, lines[ShotsOrder/2]);
                        if(Punishment.get(ShotsOrder+1) == 1){//colpo grande nulla da fare

                            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                            CurrentPlanche.handleAttack(hit.getFirst(), hit.getSecond());
                            if (CurrentPlanche.getBroken()){
                                System.out.println("rottura nave");
                                sendRandomEffect(currentPlayer.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.sendRandomEffect(currentPlayer.GetID(),new LogEvent("your ship got broken into parts, select a chunk to keep",-1,-1,-1,-1));
                                this.currentPlayer.setState(new HandleDestruction());
                                return;

                            }
                            else{
                                System.out.println("non si è rotto nulla");
                                this.ShotsOrder+=2;
                                sendRandomEffect(currentPlayer.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.continueCard();
                                return;
                            }

                        }
                        else {//colpo piccolo

                            currentPlayer.setState(new DefendingFromSmall());
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
                        if(Punishment.get(ShotsOrder+1) == 1){//colpo grande nulla da fare
                            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                            CurrentPlanche.handleAttack(hit.getFirst(), hit.getSecond());
                            if (CurrentPlanche.getBroken()){
                                System.out.println("rottura nave");
                                sendRandomEffect(currentPlayer.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.sendRandomEffect(currentPlayer.GetID(),new LogEvent("your ship got broken into parts, select a chunk to keep",-1,-1,-1,-1));
                                this.currentPlayer.setState(new HandleDestruction());
                                return;

                            }
                            else{
                                this.ShotsOrder+=2;
                                sendRandomEffect(currentPlayer.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,-1,-1,-1,-1));
                                this.continueCard();
                                return;
                            }

                        }
                        else {//colpo piccolo

                            currentPlayer.setState(new DefendingFromSmall());
                        }
                        }


                    Movement--;
                }

            }

            sendRandomEffect(currentPlayer.GetID(),new LogEvent("a "+dimensione+" shot came from "+direction+" and it "+Colpito+" "+location,hit.getFirst(),hit.getSecond(),Punishment.get(ShotsOrder),3));
                if(shotsFlag == false){
                    this.ShotsOrder += 2;
                }
        }
        if(this.ShotsOrder >=Punishment.size() ){
            this.ShotsOrder = 0;
            this.updateStates();
        }
    }

    /**
     * Handles the decision-making process for the Pirates card, determining the next actions
     * based on whether the current player accepts or refuses to loot the pirates.
     * This method updates the game state, sends appropriate messages to players,
     * adjusts rewards, and progresses the gameplay accordingly.
     *
     * @param accepted A boolean value indicating whether the current player has
     *                 accepted (true) or refused (false) to loot the pirates.
     */
    @Override
    public void continueCard(boolean accepted){
        if(accepted){

            ArrayList<Player> PlayerListMsg = this.getBoard().getPlayers();
            for(Player p : PlayerListMsg){
                if(p.GetID()== currentPlayer.GetID()){
                    this.sendRandomEffect(p.GetID(),new LogEvent("You have accepted to loot the pirates",-1,-1,-1,-1));
                }
                else {
                    this.sendRandomEffect(p.GetID(), new LogEvent(currentPlayer.GetID() + " has accepted to loot the pirates", -1, -1, -1, -1));
                }
            }

            currentPlayer.getmyPlayerBoard().setCredits(this.reward);
            this.getBoard().movePlayer(this.currentPlayer.GetID(), -this.getTime());
        }
        else{
            ArrayList<Player> PlayerListMsg = this.getBoard().getPlayers();
            for(Player p : PlayerListMsg){
                if(p.GetID()== currentPlayer.GetID()){
                    this.sendRandomEffect(p.GetID(),new LogEvent("You have refused to loot the pirates",-1,-1,-1,-1));
                }
                this.sendRandomEffect(p.GetID(),new LogEvent(currentPlayer.GetID()+" has refused to loot the pirates",-1,-1,-1,-1));
            }

        }



        this.finishCard();
    }

    /**
     * Defends the player's ship from a small attack. This method evaluates the provided energy
     * coordinates and player's board state to execute the defensive action. If the energy is
     * null or invalid, it applies destruction processes and checks for further consequences,
     * such as ship breaking or transitioning game states.
     *
     * @param energy A pair representing the coordinates (row and column) of the energy point
     *               being used for defense. Null value indicates no energy is available to defend.
     * @param player The current player performing the defense action, whose state and effects
     *               will be updated based on the outcome.
     *
     * @throws InterruptedException If there are delays or interruptions during the execution
     *                              of certain operations, such as state updates or destruction processes.
     */
    @Override
    public void DefendFromSmall(IntegerPair energy, Player player) throws InterruptedException {
        PlayerBoard currentBoard =this.currentPlayer.getmyPlayerBoard();
        Tile[][] tiles =currentBoard.getPlayerBoard();
        if (energy!=null){
            if ((currentBoard.getShield()[Punishment.get(ShotsOrder)]==0)){
                throw new InvalidDefenceEceptiopn("this shield defends the wrong side"+" the side was: "+getPunisment().get(ShotsOrder));
            }
            else {
                try {
                    currentBoard.performAction(tiles[energy.getFirst()][energy.getSecond()].getComponent(),new UseEnergyAction(currentBoard), new ConsumingEnergy());
                }
                catch (Exception e){
                    throw new ImpossibleBoardChangeException("There was no energy to use here");
                }
                this.sendRandomEffect(player.GetID(),new LogEvent("you defended your ship in " +hit.getFirst()+" "+hit.getSecond(),hit.getFirst(),hit.getSecond(),Punishment.get(ShotsOrder),3));
                System.out.println(player.GetID()+" Defended From Small");
                player.setState(new Waiting());
            }
        }
        else {
            currentBoard.destroy(hit.getFirst(), hit.getSecond());
            currentBoard.handleAttack(hit.getFirst(), hit.getSecond());
            this.sendRandomEffect(player.GetID(),new LogEvent("your ship got destroyed in " +hit.getFirst()+" "+hit.getSecond(),hit.getFirst(),hit.getSecond(),Punishment.get(ShotsOrder),3));
            player.setState(new Waiting());
            Thread.sleep(1000);
            if (currentBoard.getBroken()){
                System.out.println("rottura nave");
                this.currentPlayer.setState(new HandleDestruction());
                this.sendRandomEffect(player.GetID(),new LogEvent("your ship got broken into parts, select a chunk to keep",-1,-1,-1,-1));

                return;

            }
            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
        }
        this.ShotsOrder+=2;
        this.continueCard();
    }

    /**
     * Marks the current card as finished and evaluates the state of the game to determine
     * any losing players. This method ensures that all necessary end-of-card logic is executed,
     * transitioning the game to its next phase or concluding the current card's effects.
     *
     * Behavior:
     * - Invokes the `checkLosers` method to evaluate which players, if any, should lose based
     *   on the current game state. This may include players who fail to meet specific criteria
     *   such as insufficient crew or being doubled on the board.
     * - Calls the `setFinished` method with a value of `true` to signify that the current card's
     *   actions have been completed.
     *
     * Preconditions:
     * - The card and game board should be properly initialized and in a valid state for processing.
     * - The list of players and their statuses must be accessible through the game board object.
     *
     * Postconditions:
     * - The finished status of the current card is set to true.
     * - Any losing players are identified and processed according to the game's specific rules.
     */
    @Override
    public void finishCard() {
        checkLosers();
        this.setFinished(true);
    }


    /**
     * Executes the continuation of a specific process or task.
     * This method is responsible for calling the {@code continueCard} method
     * to proceed further in the defined operation or workflow.
     */
    public void keepGoing(){

        continueCard();
    }

    /**
     * Retrieves the player who is currently taking their turn in the game.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Retrieves the order of shots.
     *
     * @return the order of shots as an integer
     */
    public int getShotsOrder() {
        return ShotsOrder;
    }


    //json required

    /**
     * Constructs a new Pirates object.
     * This class represents a group of pirates or functionality associated with pirates.
     * It may be used to model pirate-related behavior, attributes, or activities.
     */
    public Pirates(){}
    /**
     * Retrieves the value of the requirement.
     *
     * @return the requirement as an integer
     */
    public int getRequirement() {return requirement;}
    /**
     * Sets the requirement value.
     *
     * @param requirement the integer value to set as the requirement
     */
    public void setRequirement(int requirement) {this.requirement = requirement;}
    /**
     * Retrieves the reward value.
     *
     * @return the reward value as an integer
     */
    public int getReward() {return reward;}
    /**
     * Sets the reward value for the current object.
     *
     * @param reward the reward value to be set, expressed as an integer
     */
    public void setReward(int reward) {this.reward = reward;}
    /**
     * Retrieves the list of punishments.
     *
     * @return an ArrayList of integers representing punishments.
     */
    public ArrayList<Integer> getPunisment() {return Punishment;}
    /**
     * Sets the punishment values.
     *
     * @param punisment an ArrayList of integers representing the punishment values to be set
     */
    public void setPunisment(ArrayList<Integer> punisment) {Punishment = punisment;}
}
