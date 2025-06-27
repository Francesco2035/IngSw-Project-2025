package org.example.galaxy_trucker.Model.Cards;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Exceptions.WrongPlanetExeption;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.ChoosingPlanet;
import org.example.galaxy_trucker.Model.PlayerStates.HandleCargo;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;

import java.util.ArrayList;

/**
 * Represents a SolarSystem card in the game, which extends the attributes and behaviors of a basic Card.
 * The SolarSystem card involves players interacting with planets, progressing through states, and handling
 * game logic associated with planet selection and resource management.
 *
 * The class manages game-state transitions, player interactions, and the effects of the card on the game board.
 * It provides methods to update player states, handle decision-making for choosing planets, and determine the
 * end conditions for the card.
 *
 * Key responsibilities of the class include:
 * - Managing players' progress and their interactions with planets.
 * - Allowing players to choose planets or skip the selection phase.
 * - Updating player states based on their actions and the state of the SolarSystem card.
 * - Finalizing the card and determining losers when all actions are completed.
 */
public class SolarSystem extends Card {
    /**
     * Represents a collection of planets within the solar system.
     * Each element in the list is an instance of the {@link Planet} class,
     * which defines the characteristics and status of individual planets.
     * The primary purpose of this variable is to encapsulate the list of planets
     * that are part of the solar system, providing essential data
     * for gameplay mechanics and interactions.
     *
     * This variable is annotated with {@code @JsonProperty("Planets")}
     * to facilitate JSON serialization and deserialization when exchanging data.
     *
     * The {@link Planet} objects in the list can contain information
     * such as the goods available on each planet and its occupancy status.
     */
    @JsonProperty ("Planets")
    private ArrayList<Planet> planets;
    /**
     * Represents the current player in the solar system game context.
     * It holds the reference to the player currently taking their turn
     * or performing actions in the game.
     */
    private  Player currentPlayer;
    /**
     * Represents the current turn order within the solar system game.
     * This variable tracks and manages the progression of player turns.
     */
    private int order;
    /**
     * Represents the completion state within the SolarSystem context.
     * This variable is used to track progress or determine whether a certain
     * action or set of actions has been completed.
     */
    private int done;
    /**
     * Represents a message or description that can be associated with the SolarSystem instance.
     * This variable may store textual information about the current state or context within the system.
     */
    private  String message;

    /**
     * Represents a list of players who have lost in the SolarSystem game context.
     * This list stores instances of the Player class who are determined to be in a losing state
     * during the game lifecycle. The specific criteria for being added to this list
     * depend on the game's rules and logic.
     */
    ArrayList<Player> losers;

    /**
     * Sends a type log event to all players in the game.
     *
     * This method iterates through all the players retrieved from the game board
     * and invokes the {@code sendRandomEffect()} method for each player. Each
     * invocation sends a {@code LogEvent} instance that is constructed with preset
     * values.
     *
     * The event is sent using the player's unique identifier obtained through
     * {@code Player.GetID()}.
     */
    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Planets",-1,-1,-1,-1));
        }
    }



    /**
     * Constructs a new SolarSystem instance with the specified parameters.
     *
     * @param lv the level of the game
     * @param time the time limit of the game
     * @param board the game board instance associated with the SolarSystem
     * @param planets the list of planets within the SolarSystem
     */
    public SolarSystem(int lv, int time, GameBoard board, ArrayList<Planet> planets) {
        super(lv, time,board);
        this.planets = planets;
        this.order = 0;
        this.currentPlayer = new Player();
        this.done = 0;
    }

    /**
     * Executes the effect of the card for the Solar System game. This method modifies
     * the state of the players and updates the game states according to card logic.
     *
     * Specifically, it initializes the `losers` list and retrieves all players from the game board.
     * Each player's state is set to "Waiting". Additionally, the method updates the relevant
     * states in the game and clears the message field.
     *
     * Debugging information, such as the size of the player list and player IDs,
     * is printed to the console during execution.
     */
    @Override
    public void CardEffect(){
        losers = new ArrayList<>();
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(new Waiting());
        }
        this.message=" ";
        this.updateStates();
    }


    /**
     * Updates the current state of the game based on the progress of the game round.
     * This method ensures that the players in the game move through their respective states
     * and that the game logic is followed sequentially.
     *
     * The method checks if the current game round is finished. If the round is not finished,
     * it processes the next player in the order. It sets their state to "ChoosingPlanet"
     * and increments the order for the next turn. If all players have been processed, it
     * triggers cargo handling for the planets occupied by players and resets player states
     * to "Waiting" for the next phase.
     *
     * Key functions handled:
     * - Updates the current player's state to "ChoosingPlanet".
     * - Checks the list of players and advances through their turns based on the game order.
     * - Handles cargo for planets occupied by players.
     * - Communicates game updates with the ConcurrentCardListener.
     *
     * Preconditions:
     * - The game is not in the finished state (verified by this.isFinished()).
     *
     * Postconditions:
     * - Updates player states and logs their actions.
     * - Manages planet occupation and assigns rewards based on cargo handling.
     */
    @Override
    public void updateStates(){
        if (!this.isFinished()){
            GameBoard Board=this.getBoard();
            ArrayList<Player> PlayerList = Board.getPlayers();
            if(this.order<PlayerList.size()){
                if (currentPlayer != null) {currentPlayer.setState(new Waiting());}
                currentPlayer = PlayerList.get(this.order);
                PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();


//                if(!message.equals(" ")) {
//                    this.sendRandomEffect(currentPlayer.GetID(), new LogEvent(message,-1,-1,-1,-1));
//                }
                this.currentPlayer.setState(new ChoosingPlanet());

                System.out.println(this.currentPlayer.GetID() + " : "+ this.currentPlayer.getPlayerState());
                //this.currentPlayer.setInputHandler(new ChoosingPlanet(this));

                this.order++;
            }
            else{
                System.out.println("Players now have to handle the cargo :)");
                for(Player p : PlayerList){
                    p.setState(new Waiting());
                }
                ConcurrentCardListener concurrentCardListener = this.getConcurrentCardListener();
                concurrentCardListener.onConcurrentCard(true);
                for(Planet p: this.planets){

                    if(p.isOccupied()){

                        System.out.println(p.getOccupied().GetID()+" is occupying this planet");
                        this.getBoard().movePlayer(p.getOccupied().GetID(), -this.getTime());

                        p.getOccupied().setState(new HandleCargo());
                        p.getOccupied().getmyPlayerBoard().setRewards(p.getGoods());
                    }
                }
            }
            }
    }

    /**
     * Finalizes the processing of the current card and updates the game state accordingly.
     *
     * This method determines whether all necessary player actions for the card have been completed.
     * If all players have completed their actions (based on the size of the player list retrieved
     * from the game board), the card is marked as finished and the following actions occur:
     * - Notifies the ConcurrentCardListener that the card processing is complete.
     * - Checks for any losing players in the game using the `checkLosers()` method.
     * - Logs a message indicating that the card has been finished.
     *
     * If all players have not yet completed their actions, the `done` field is incremented to
     * reflect the progress towards completing the card.
     *
     * Preconditions:
     * - The game board and its associated players are available and correctly initialized.
     *
     * Postconditions:
     * - If all player actions are completed, the card is marked as finished and the game proceeds.
     * - If not all player actions are completed, the method tracks progress via the `done` field.
     */
    @Override
    public void finishCard() {
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        if(this.done>=PlayerList.size()-1) {
            ConcurrentCardListener concurrentCardListener = this.getConcurrentCardListener();
            concurrentCardListener.onConcurrentCard(false);
            checkLosers();

            System.out.println("card finished");
            this.setFinished(true);
        }
        else{
            done++;
        }
    }


    /**
     * Allows the current player to select a planet. Validates the planet index and occupancy status
     * before finalizing the choice. If the selected planet does not exist or is already occupied,
     * throws a {@code WrongPlanetExeption} and resets the player's state to "ChoosingPlanet".
     * If the player chooses a valid planet, it updates the planet state to occupied by the player,
     * logs the selection, and sends a notification to all players.
     *
     * Additionally, handles scenarios where the player declines to choose a planet or all players
     * have declined. Calls {@code finishCard()} and/or {@code updateStates()} to manage game flow.
     *
     * @param planet the index of the planet the player wants to choose
     *        Must be a non-negative number within the bounds of the planets list.
     *        If the planet index is out of bounds or the selected planet is occupied,
     *        the method throws a {@code WrongPlanetExeption}.
     */
    @Override
    public void choosePlanet(int planet){
        if(planet>-1) {
            if (planet>=planets.size()) {
                this.currentPlayer.setState(new ChoosingPlanet());
                throw new WrongPlanetExeption("this planet doesn't exist");
            }
            if (this.planets.get(planet).isOccupied()) {
                this.currentPlayer.setState(new ChoosingPlanet());
                throw new WrongPlanetExeption("this planet is already occupied");

                //this.currentPlayer.setInputHandler(new ChoosingPlanet(this));
            } else {
                this.planets.get(planet).setOccupied(this.currentPlayer);
                message=message+currentPlayer.GetID()+" has chosen planet number "+planet+"\n";

                ArrayList<Player> pMsg = this.getBoard().getPlayers();
                for(Player p : pMsg){
                    if (p.GetID() == currentPlayer.GetID()){
                        this.sendRandomEffect(p.GetID(),new LogEvent(" you have chosen planet number"+planet,-1,-1,-1,-1));
                    }
                    else {
                        this.sendRandomEffect(p.GetID(), new LogEvent(currentPlayer.GetID() + " has chosen planet number " + planet, -1, -1, -1, -1));
                    }
                }
                this.updateStates();
            }
        }
        else{
            this.finishCard();
            //dovrebbe non dare problemi ne se l'ultimo rifiuta il pianeta ne se tutti rifiutano
            // se  l'ultimo rifiuta dovrebbe chiamare update come se avesse accettato cioe scorre l lista di chi ha accettato e li manda in handle cargo
            // se tutti dicono di no tecnicamente fa prima finish card e poi fa anche update state che però non dovrebbe fare nulla
            this.updateStates();
        }
    }

    /**
     * Continues the execution of the current card effect within the game.
     *
     * This method invokes the {@code finishCard()} method to check and update
     * the status of the current card. The {@code finishCard()} logic ensures that
     * the game progresses appropriately, handling any remaining players and managing
     * game states or events as necessary.
     *
     * Key functionality:
     * - Delegates control to the {@code finishCard()} method to manage state transitions.
     * - Ensures that card-related operations are completed in alignment with the game logic.
     */
    @Override
    public void keepGoing(){
        this.finishCard();
    }

    /**
     * Retrieves the player whose turn it currently is in the game.
     *
     * @return the player who is currently active in the game round
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //json required

    /**
     * Default constructor for the SolarSystem class.
     *
     * Initializes a new instance of the SolarSystem without setting specific parameters
     * or properties. This constructor can be used to create a SolarSystem instance
     * while deferring the initialization of its fields or other attributes.
     */
    public SolarSystem() {}
    /**
     * Retrieves the list of planets in the solar system.
     *
     * @return an ArrayList of Planet objects representing the planets in the solar system
     */
    public ArrayList<Planet> getSolarSystemPlanets() {return planets;}
    /**
     * Sets the list of planets in the solar system.
     *
     * @param solarSystemPlanets the list of Planet objects to be assigned to the solar system
     */
    public void setSolarSystemPlanets(ArrayList<Planet> solarSystemPlanets) {this.planets = solarSystemPlanets;}

}

