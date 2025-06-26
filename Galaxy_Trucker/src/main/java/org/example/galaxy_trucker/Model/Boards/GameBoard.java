package org.example.galaxy_trucker.Model.Boards;



import org.example.galaxy_trucker.Controller.Listeners.GameBoardListener;
import org.example.galaxy_trucker.ClientServer.Messages.GameBoardEvent;
import org.example.galaxy_trucker.ClientServer.Messages.ScoreboardEvent;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Cards.CardStacks;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.TileSets;

import java.lang.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;


/**
 * Represents the main game board managing the players, positions, tiles, and gameplay mechanics.
 */
public class GameBoard {

    /**
     * Represents the list of players currently participating in the game.
     * Each element in the list is a {@link Player_IntegerPair}, which contains
     * a reference to a player and their associated score or position.
     *
     * This variable is utilized to manage the state and progress of players
     * throughout the game, including tracking their scores and updating
     * their positions on the board.
     *
     * Modifications to this list can be made using methods such as
     * {@code addPlayer(Player)}, {@code removePlayer(Player)},
     * or {@code removePlayerAndShift(Player)}. This allows for dynamically
     * adding new players, removing existing ones, and adjusting their
     * positions on the board.
     */
    private ArrayList<Player_IntegerPair> players;
    /**
     * Represents the scoreboard for tracking player scores in the game.
     *
     * The scoreboard maps each player's unique identifier (String)
     * to their current score (Integer). It serves as a central record
     * for updating and retrieving player's scores during gameplay.
     *
     * This field is utilized by various methods in the GameBoard class
     * to manipulate player positions, calculate bonuses, and determine
     * the leaderboard based on scores.
     */
    private HashMap<String, Integer> scoreboard;
    /**
     * Represents the array of positions of players on the game board.
     * Each index in the array corresponds to a specific position where a player is located.
     * The order of the players in this array determines their progression on the board.
     */
    private Player[] positions;
    /**
     * Represents the number of positions available or utilized on the game board.
     * This variable is used to define or track the total positions
     * that players can occupy on the game board during gameplay.
     * It plays a crucial role in determining player placement
     * and managing movement operations across the board.
     */
    private int nPositions;
    /**
     * Represents the starting positions of players on the game board.
     * Each element in the array corresponds to the starting position
     * of a player. For example, the first element represents the starting
     * position of the first player, the second element represents the starting
     * position of the second player, and so on.
     */
    private int[] startPos;
    /**
     * Represents the set of tiles utilized on the game board.
     * This variable maintains and manages the covered and uncovered tiles used during the game.
     *
     * The associated `TileSets` object provides functionality for:
     * - Retrieving new tiles, either randomly from the covered tiles or by index from the uncovered tiles.
     * - Adding newly uncovered tiles.
     * - Managing listeners for tile set changes.
     *
     * It plays a critical role in facilitating gameplay by dynamically providing and managing the tiles
     * available for use in the game.
     */
    private TileSets tileSets;
    /**
     * Represents an hourglass used within the game board to manage timed events.
     * This object is responsible for controlling the timer, usage limits,
     * and the communication of hourglass-related events to associated listeners.
     *
     * The hourglass is used for game mechanics that require time-based constraints,
     * such as limiting actions within a specific period or enforcing gameplay rules.
     *
     * The available functionalities include starting, stopping, and monitoring
     * the hourglass timer, as well as managing its remaining usages and state.
     *
     * Access to the hourglass allows the game board to control and validate
     * time-dependent actions for players.
     */
    private Hourglass hourglass;
    /**
     * Represents the current level of the game in the GameBoard.
     *
     * This variable is integral to determining game behavior and rulesets
     * specific to different levels. Certain features, such as the hourglass
     * functionality, have level-specific restrictions. For example, the
     * hourglass cannot be started in Level 1.
     */
    private int GameLv;
    /**
     * Represents the number of players currently active on the game board.
     * This variable is integral to tracking the game's state and ensuring
     * accurate player interactions and position management.
     */
    private int PlayersOnBoard;
    /**
     * Represents the stack of cards used during the game. This variable holds an instance
     * of the `CardStacks` class, which manages the distribution, visibility, and lifecycle
     * of different card decks and cards within the game levels.
     *
     * The card stacks functionality includes:
     * - Managing visible and hidden cards for various game levels.
     * - Supporting operations related to drawing and shuffling decks.
     * - Interfacing with card listeners for specific player actions.
     *
     * This property is critical to the mechanics of the game, as it determines the flow
     * and appearance of cards in gameplay.
     */
    private CardStacks CardStack;
    /**
     * Represents the current card being drawn or played in the game.
     * This variable is part of the game state and is dynamically updated
     * during gameplay. It tracks the specific card that is currently
     * in use, allowing for the implementation of card-related game
     * mechanics such as drawing, activating effects, or resolving actions.
     *
     * It is managed internally by the game board and interacts with
     * other game elements such as players, tiles, and card stacks.
     */
    private Card CurrentCard;


    /**
     * A collection of active listeners that monitor changes and updates
     * within the game board. These listeners are notified whenever a
     * significant event occurs on the game board, enabling dynamic updates
     * to connected entities.
     *
     * This list stores instances of objects that implement the
     * {@link GameBoardListener} interface, ensuring that they can respond
     * to events triggered by the game board.
     *
     * The listeners are managed through methods provided by the
     * {@code GameBoard} class, such as {@code setListeners},
     * {@code removeListener}, and {@code sendUpdates}.
     */
    private ArrayList<GameBoardListener> listeners = new ArrayList<>();


    /**
     * Constructs a GameBoard instance with the specified tile sets, game level, and card stacks.
     * This class initializes the game board, including positions, start positions, and related game elements.
     *
     * @param list the set of tiles to be used in the game
     * @param lv the level of the game, determines board configuration
     * @param stack the stack of cards to be used within the game
     */
    public GameBoard(TileSets list, int lv, CardStacks stack) {
        this.players = new ArrayList<>();
        GameLv = lv;
        tileSets = list;
        startPos = new int[4];
        PlayersOnBoard = 0;
        scoreboard = new HashMap<>();

        if(lv == 2) {
            nPositions = 24;
            startPos[0] = 6;
            startPos[1] = 3;

        }
        else {
            nPositions = 18;
            startPos[0] = 4;
            startPos[1] = 2;
        }

        startPos[2] = 1;
        startPos[3] = 0;

        positions = new Player[nPositions];
        hourglass = new Hourglass(GameLv);
        this.CardStack = stack;
    }


    /**
     * adds a new player to the game
     * @param NewPlayer reference to the newborn player
     */
    public void addPlayer(Player NewPlayer){
        NewPlayer.setBoards(this);
        Player_IntegerPair NewPair = new Player_IntegerPair(NewPlayer, -1);
        this.players.add(NewPair);
//        NewPlayer.setState(new BuildingShip());
    }


    /**
     * Invokes the hourglass functionality for the specified player.
     * Throws a RuntimeException if the hourglass can only be used once
     * and the player has not prepared their ship.
     *
     * @param player the player attempting to use the hourglass
     * @throws RuntimeException if the player's ship is not ready or if an
     *         error occurs during the hourglass operation
     */
    public void callHourglass(Player player) throws RuntimeException{
        if(hourglass.getUsages() == 1 && !player.GetReady())
            throw new RuntimeException("You need to finish your ship before using the hourglass");
        else try{
            StartHourglass();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Starts the hourglass timer within the game board. This method validates the
     * current game state and manages the hourglass lifecycle.
     *
     * This method performs the following validations:
     * 1. Ensures that the hourglass cannot be started in a level 1 game.
     * 2. Ensures that the hourglass is not already running before attempting to start it.
     * 3. Ensures that there are remaining usages available for the hourglass.
     *
     * If all validations pass, the hourglass is locked (preventing concurrent starts)
     * and then started, decrementing its remaining usages after completion.
     *
     * @throws IllegalStateException if the game is in level 1 or the hourglass is already running.
     * @throws RuntimeException if the hourglass has no remaining usages left.
     */
    public void StartHourglass() throws RuntimeException{
        System.out.println("Gameboard attempting  to call start hourglass");
        if(this.GameLv == 1){
            throw new IllegalStateException("Cannot use Hourglass in a level 1 game!");
        }
        else if(!hourglass.isStartable())
            throw new IllegalStateException("Hourglass is already running.");
        else if(hourglass.getUsages() <= 0)
            throw new RuntimeException("No Hourglass usages left.");
        else{
            hourglass.setLock();
            hourglass.startHourglass();
        }
    }


    /**
     * Removes a specified player from the game. The method identifies the player
     * within the list of active players and removes the corresponding entry.
     *
     * @param DeadMan the player to be removed from the game
     * @throws NoSuchElementException if the specified player is not found in the current list of players
     */
    public void removePlayer(Player DeadMan){

        Player_IntegerPair eliminated = players.stream()
                .filter(p -> DeadMan.equals( p.getKey()))
                .findFirst().orElseThrow();

        players.remove(eliminated);
    }


    /**
     * sets the starting position of a player on the common board:
     * the position players[0] corresponds to the starting position of the 1st player
     * the leader (1st player) will be in the first position of the arraylist
     * @param pl reference to the player
     */
    public void SetStartingPosition(Player pl){
        Player_IntegerPair cur = players.stream()
                .filter(p -> pl.equals( p.getKey()) )
                .findFirst().orElseThrow();
        if(cur.getValue() == -1){
            SetNewPosition(cur, startPos[PlayersOnBoard], startPos[PlayersOnBoard]);
            PlayersOnBoard++;
        }
        else throw new IllegalStateException("You already are on the board!");
    }


    /**
     * Sets the starting position of a player on the game board.
     * If the specified position is already taken or the player is already on the board,
     * an exception will be thrown.
     *
     * @param pl the player for whom the starting position is being set
     * @param index the index of the starting position to assign
     * @throws IllegalArgumentException if the specified index is out of bounds
     *                                  or if the position is already occupied
     * @throws IllegalStateException if the player is already positioned on the board
     */
    public void SetStartingPosition (Player pl, int index) throws IllegalArgumentException{
        Player_IntegerPair cur = players.stream()
                .filter(p -> pl.equals(p.getKey()))
                .findFirst().orElseThrow();
        if(cur.getValue() == -1) {
            if (index > players.size())
                throw new IllegalArgumentException("Cell not available!");

            if (positions[startPos[index - 1]] == null) {
                SetNewPosition(cur, startPos[index - 1], startPos[index - 1]);
                sendUpdates(new GameBoardEvent(startPos[index - 1], pl.GetID()));
                System.out.println("@@@" + startPos[index - 1] + ":" + pl.GetID());
                PlayersOnBoard++;
            } else throw new IllegalArgumentException("This cell is already taken!");

        }
        else throw new IllegalStateException("You already are on the board!");
    }


    
    /**
     * Removes a specified player from the game and shifts the positions of
     * the remaining players on the game board accordingly. This method ensures
     * the player's position is cleared, updates other players' positions, and
     * decrements the count of players on the board. Updates are sent to notify
     * relevant components of these changes.
     *
     * @param pl the player to be removed and whose position will be cleared on the game board
     * @throws RuntimeException if an error occurs during the player removal process
     */
    public void removePlayerAndShift(Player pl) throws  RuntimeException{
        sendUpdates(new GameBoardEvent(-1, pl.GetID()));
        System.out.println(pl.GetID()+ "removed");
        int[] shiftedPositions = new int[players.size()];

        Player_IntegerPair cur = players.stream()
                .filter(p -> pl.equals(p.getKey()) )
                .findFirst().orElseThrow();

        if(cur.equals(players.getLast())) {
            positions[cur.getValue()] = null;
            cur.setValue(-1);
        }
        else{
            int i=0;
            for(Player_IntegerPair p : players){
                if(p.getValue() >=0){
                    shiftedPositions[i] = p.getValue();
                    positions[shiftedPositions[i]] = null;
                    i++;
                    //qui tolgo tutto
                }
            }

            cur.setValue(-1);
            //setto quello da rimuovere a -1

            i=0;
            for(Player_IntegerPair p : players)
                if(p.getValue() >=0){
                    p.setValue(shiftedPositions[i]);
                    positions[shiftedPositions[i]] = p.getKey();
                    //System.out.println("@@@Set "+p.getKey().GetID()+" to "+shiftedPositions[i]+ " position: "+positions[shiftedPositions[i]]);
                    i++;
                }
            PlayersOnBoard--;
            updateAllPosition();




//            for(Player_IntegerPair p : players)
//                if(p.getValue() >=0)
//                    sendUpdates(new GameBoardEvent(p.getValue(), pl.GetID()));
//
//
        }



    }


    /**
     * Updates the positions of all players on the game board and sends relevant updates.
     *
     * This method iterates through a collection of player-position pairs. For each player,
     * if the associated position is non-negative, a game board event is generated and
     * sent via the `sendUpdates` method. The event contains the player's current position
     * and ID. This ensures that the game board state is updated and communicated to
     * all registered listeners.
     *
     * Preconditions:
     * - `players` is a collection of `Player_IntegerPair` objects, where each pair
     *   represents a player and their corresponding score on the game board.
     *
     * Behavior:
     * - Iterates through `players`.
     * - If a player's score is greater than or equal to 0, an update is sent
     *   via the `sendUpdates` method.
     *
     * Dependencies:
     * - Rel*/
    public void updateAllPosition(){
        for(Player_IntegerPair p : players)
            if(p.getValue() >=0){
                //System.out.println("@@@"+p.getValue()+":"+ p.getKey().GetID());
                sendUpdates(new GameBoardEvent(p.getValue(), p.getKey().GetID()));
            }
    }



    /**
     * moves the player forward or backwards on the board of a selected number of steps
     *
     * @param ID of the player to move
     * @param nSteps to run (negative if the player is moving backwards)
     * @throws IllegalArgumentException if the number of steps is 0
     */
    public void movePlayer(String ID, int nSteps) throws IllegalArgumentException{

        int NewIndex;

        Player_IntegerPair cur = players.stream()
                                        .filter(p -> ID.equals( p.getKey().GetID() ) )
                                        .findFirst()
                                        .orElseThrow();

        int NewPos = cur.getValue();
        int i = nSteps;
        if(NewPos < 0) NewIndex = nPositions - (-NewPos % nPositions);
        else NewIndex = NewPos;

        if(nSteps == 0) throw new IllegalArgumentException("Number of steps cannot be 0: must move forward or backwards");

        else if(nSteps > 0) {
            while (i > 0) {
                NewPos++;
                NewIndex++;
                if (positions[NewIndex % nPositions] == null || positions[NewIndex % nPositions].equals(cur.getKey())) i--;
//                else if (cur.getKey().equals(players.getFirst().getKey()) && players.getFirst().getValue() + nSteps - nPositions >= players.getLast().getValue()) {
//                    abandonRace(players.getLast().getKey());
//                }
            }

        }
        else while(i < 0){
            NewPos--;
            if(NewPos < 0) NewIndex = (nPositions + (NewPos % nPositions)) % nPositions;
            else NewIndex = NewPos % nPositions;

            if (positions[NewIndex % nPositions] == null || positions[NewIndex % nPositions].equals(cur.getKey()))
                i++;
//            else if(cur.getKey().equals(players.getLast().getKey()) && players.getLast().getValue() + nSteps +nPositions <= players.getFirst().getValue()){
//                abandonRace(players.getLast().getKey());
//            }
        }

        if(NewPos < 0) NewIndex = (nPositions + (NewPos % nPositions)) % nPositions;
        else NewIndex = NewPos % nPositions;

        SetNewPosition(cur, NewPos, NewIndex);
    }


    /**
     * support method: brings a player on a specified position on the board and reorders the leadboard
     * @param cur pair of: player to move and relative score (number of steps taken so far)
     * @param newPosition new score that the player will have once moved
     * @param NewIndex target index of the array to move the player on
     */
    private void SetNewPosition(Player_IntegerPair cur, int newPosition, int NewIndex){

        int CurIndex = players.indexOf(cur);
        int OldIndex = cur.getValue();

        if(OldIndex < 0 ) OldIndex = nPositions - (-OldIndex % nPositions);
        else OldIndex = OldIndex % nPositions;

        Player_IntegerPair NewPair = new Player_IntegerPair(cur.getKey(), newPosition);

        positions[OldIndex] = null;
        positions[NewIndex] = NewPair.getKey();

        players.remove(CurIndex);
        players.add(CurIndex, NewPair);


        players.sort(Comparator.comparing(Player_IntegerPair::getValue));
        //revers the order of the arraylist to have the leader in the 1st position (index = 0)
        ArrayList<Player_IntegerPair> OrderedPlayers = new ArrayList<>();
        for(int i= players.size()-1; i>=0; i--){
            OrderedPlayers.add(players.get(i));
        }
        players = OrderedPlayers;


        //listener
        sendUpdates(new GameBoardEvent(NewIndex, cur.getKey().GetID()));

    }



    /**
     * Checks and returns a list of players who have completed or exceeded a double lap
     * based on their positions relative to other players on the game board.
     *
     * Iterates through all players and evaluates if their current position meets the
     * condition for completing a double lap relative to other players. A player is
     * considered to have completed a double lap if the difference in position between
     * them and another player is greater than or equal to the configured threshold
     * (nPositions). Ensures that the same player is not added multiple times to the
     * result list.
     *
     * @return an ArrayList of players who have completed or exceeded a double lap.
     */
    public ArrayList<Player> checkDoubleLap(){
        ArrayList<Player> doubled = new ArrayList<>();

        for(Player_IntegerPair p1 : players)
            for(Player_IntegerPair p2 : players){
                if(!p1.equals(p2) && p2.getValue() - p1.getValue() >= nPositions){
                    if(!doubled.contains(p1.getKey()))
                        doubled.add(p1.getKey());
                }
            }

        return doubled;
    }


    /**
     * Picks and sets a new card as the current card in the game.
     * This method retrieves a new card from the card stack and assigns it to the current card in the game board.
     * It also updates each player by setting the newly picked card to their state and associates the card with the game board.
     * A message is printed showing the card's ID and class type.
     *
     * @return the newly selected card from the card stack
     */
    public Card NewCard() {
        CurrentCard = CardStack.PickNewCard();
        //CurrentCard = getCardStack().getGaG().getCardsDeck().get(36);
        for(Player_IntegerPair p : players){
            p.getKey().setCard(CurrentCard);
        }

        CurrentCard.setBoard(this);

        System.out.println("Id Card: " +CurrentCard.getId() + " "+ CurrentCard.getClass().getName());
        //return getCardStack().getGaG().getCardsDeck().get(36);
        return CurrentCard;
    }



    /**
     * Retrieves a copy of the list of players currently participating in the game.
     * This method iterates through the internal collection of Player_IntegerPair
     * objects, extracts each Player, and adds them to a new list.
     *
     * @return an ArrayList containing a copy of all players in the game.
     */
    public ArrayList<Player> getPlayers(){
        ArrayList<Player> PlayersCopy = new ArrayList<>();
        try{
            for (Player_IntegerPair player : players) {
                PlayersCopy.add(player.getKey());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return PlayersCopy;
    }


    /**
     * Retrieves the current level of the game.
     *
     * @return the game level as an integer
     */
    public int getLevel(){return GameLv;}
    /**
     * Retrieves the current positions of players on the game board.
     *
     * @return an array of Player objects representing the positions of all players on the board.
     */
    public Player[] getPositions(){return this.positions;}
    /**
     * Retrieves the tile sets associated with the game board.
     *
     * @return the TileSets object representing the tiles used in the game.
     */
    public TileSets getTilesSets(){return tileSets;}
    /**
     * Retrieves the card stack currently being used in the game board.
     *
     * @return the CardStacks instance representing the deck of cards associated with the game.
     */
    public CardStacks getCardStack(){return this.CardStack;}

    /**
     * Retrieves the hourglass associated with the game board.
     *
     * @return the instance of Hourglass currently used in the game.
     */
    public Hourglass getHourglass() {return hourglass;}





    /**
     * Handles the process of a player abandoning an ongoing or unstarted race.
     * It removes the player from the race board, records their final score if applicable,
     * and triggers their race completion process with a corresponding message.
     *
     * @param loser   The player who is abandoning the race.
     * @param message The message indicating the reason or context of abandonment.
     * @param started A boolean indicating whether the race had already started.
     */
    public void abandonRace(Player loser, String message,boolean started) {
        try{
            int arrayIndex;
            Player_IntegerPair pair= players.stream()
                    .filter(p -> p.getKey().equals(loser))
                    .findFirst()
                    .orElseThrow();
            if (started){
                if (pair.getValue() < 0){
                    arrayIndex = nPositions - (-pair.getValue() % nPositions);
                }
                else {
                    arrayIndex = pair.getValue() % nPositions;
                }
                sendUpdates(new GameBoardEvent(-1, loser.GetID()));
                positions[arrayIndex] = null;
                int finalScore = loser.CalculateResult(false);
                scoreboard.put(loser.GetID(), finalScore);
                loser.finishRace(finalScore,message); // ora il player si spera non esista più
            }
            else {
                message="quit";
                loser.finishRace(0,message);
            }
            players.remove(pair);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Concludes the game by calculating final scores for each player, updating the scoreboard,
     * and notifying each player about their final outcome.
     *
     * The method performs the following steps:
     * 1. Iterates through the list of players to calculate their final scores using their respective `CalculateResult` method
     *    and updates the scoreboard.
     * 2. Processes each player's finishing position in the game and clears their position in the game state.
     * 3. Constructs a `ScoreboardEvent` using the updated scoreboard and calls the `finishRace` method for each player,
     *    passing their final results, outcome, and event details.
     * 4. Clears the list of players to finalize the game state.
     */
    public void finishGame(){
            int arrayIndex;

            for(Player_IntegerPair p : players){

               Player player = p.getKey();
               int finalScore = player.CalculateResult(true);
               scoreboard.put(player.GetID(), finalScore);

           }
            for(Player_IntegerPair p:players){
                int outcome = 0;
                if(p.getValue() < 0) {
                    arrayIndex = nPositions - (-p.getValue() % nPositions);
                }
                else{
                    arrayIndex = p.getValue() % nPositions;
                }
                positions[arrayIndex] = null;
                /// mando classifica
                outcome = scoreboard.get(p.getKey().GetID());
                ScoreboardEvent scoreboardEvent = new ScoreboardEvent(scoreboard);
                p.getKey().finishRace(scoreboardEvent,outcome,"sk");

            }

           players.clear();

    }


    /**
     * Calculates the arrival bonus for a player based on their position in the player list
     * and the current level of the game.
     *
     * @param player The player for whom the arrival bonus is calculated.
     * @return The arrival bonus for the specified player.
     */
    public int arrivalBonus(Player player){

        ArrayList<Player> playersCopy = this.getPlayers();

        int i = playersCopy.indexOf(player);

        return (4-i)*getLevel();

    }


    /**
     * Calculates the beauty bonus for a specified player based on their performance
     * compared to other players in terms of exposed connectors on their player board.
     *
     * @param player The player for whom the beauty bonus is calculated.
     * @return The beauty bonus value. If the specified player has the fewest exposed
     *         connectors, it returns double the level of the current context;
     *         otherwise, it returns 0.
     */
    public int beautyBonus(Player player){
        ArrayList<Player> playersCopy = this.getPlayers();

        int best= 100000;
        String prettiest="";
        for (Player p : playersCopy) {
            if(p.getmyPlayerBoard().getExposedConnectors()<best ){
                best= p.getmyPlayerBoard().getExposedConnectors();
                prettiest=p.GetID();
            }
        }
        if(player.GetID()==prettiest){
            return 2*getLevel();
        }
        else {
            return 0;
        }

    }

//    public void finishGame(){
//        int score;
//        for(Player_IntegerPair p : players){
//            score = p.getKey().finishRace(true);
//            scoreboard.add(new Player_IntegerPair(p.getKey(), score));
//        }
//    }


    /**
     * Sends updates to all registered listeners when a GameBoardEvent occurs.
     *
     * @param event the event that contains information about the change in the game board
     */
    public void sendUpdates(GameBoardEvent event){
        if(listeners != null) {
            for (GameBoardListener listener :listeners) {
                listener.gameBoardChanged(event);

            }
        }
    }

    /**
     * Adds a GameBoardListener to the list of listeners.
     *
     * @param listener the GameBoardListener to be added
     */
    public void setListeners(GameBoardListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the specified listener from the collection of listeners
     * that are notified of changes to the game board.
     *
     * @param listener the GameBoardListener to be removed
     */
    public void removeListener(GameBoardListener listener) {
        listeners.remove(listener);
    }


}