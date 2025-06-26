package org.example.galaxy_trucker.Model;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Cards.CardStacks;
import org.example.galaxy_trucker.Model.Tiles.TileSets;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a game session. The Game class manages all key components of the
 * game such as players, game board, card stacks, tile sets, and game level.
 * It allows adding and removing players, and provides access to game-related
 * properties.
 *
 * This class implements Serializable to allow game objects to be persisted
 * or transferred as needed.
 */
public class Game implements Serializable {

    /**
     * Represents the unique identifier for a specific game session.
     * This identifier is used to distinguish between different game sessions
     * and serves as a key reference in the Game class.
     */
    private String GameID;
    /**
     * A map that holds all players participating in the game, where the key is the
     * player's unique identifier (String) and the value is the Player object associated with that ID.
     *
     * Players can be added to or removed from this map during the game. It ensures
     * quick access to a player by their unique ID and manages the core player data
     * within the game session.
     */
    private HashMap<String,Player> Players;
    /**
     * A list containing all players participating in the game session.
     *
     * This list is managed by the Game class and is synchronized with the map of
     * players to ensure consistency during player addition and removal.
     * The list preserves the order in which players are added to the game.
     */
    private ArrayList<Player> PlayerList;
    /**
     * Represents the game board used within the game. The GameBoard serves as a central
     * component where the gameplay occurs, managing the tiles, interactions, and the
     * state of the game for all players. It is initialized when a new game session is
     * created and persists throughout the lifecycle of the game instance.
     *
     * Responsibilities of the GameBoard include facilitating player actions and
     * interactions, maintaining the state of the game elements, and ensuring the
     * consistency of the game rules as applied to the board.
     *
     * This variable is instantiated using the TileSets, game level, and CardStacks
     * during the creation of a `Game` instance.
     */
    private GameBoard GameBoard;
    /**
     * Represents the deck of cards available during the game.
     * It manages the initialization, distribution, and interaction
     * with the cards based on the game's progression level.
     *
     * The CardDeck is an instance of the CardStacks class, which handles
     * different categories of cards such as visible cards, hidden cards,
     * and full adventure decks. It also incorporates gameplay mechanics
     * specific to card management, such as merging and shuffling decks.
     *
     * This field is initialized when a new Game object is created, ensuring
     * that the setup of the card deck corresponds to the specified game level.
     */
    private CardStacks CardDeck;
    /**
     * Represents a collection of tile sets used in the game. The TileDecks variable
     * manages covered and uncovered tiles, allowing for tile retrieval and manipulation
     * during gameplay. It interacts with the game logic to provide tiles dynamically as
     * needed while maintaining their state.
     *
     * The TileDecks variable is initialized using a GAGen instance, which provides
     * pre-defined tile data loaded from external resources. This enables the game to
     * maintain a structured and consistent set of tiles for gameplay.
     *
     * TileDecks operates as part of the game environment, facilitating tile-related
     * operations such as drawing new tiles, handling uncovered tiles, and updating
     * the state of the tile sets when changes occur.
     */
    private TileSets TileDecks;
    /**
     * Represents the game level of the current game session.
     * This variable determines the complexity and configuration of the game,
     * including attributes like the card stack and tile deck settings.
     * It is initialized during the creation of the game instance
     * and used throughout the gameplay for various level-dependent operations.
     */
    private int  lv;
    /**
     * An instance of GAGen that is responsible for managing game assets such as
     * tiles and cards. This includes loading the assets from external JSON files
     * and providing access to the initialized tile and card decks.
     *
     * The gag variable acts as a centralized resource handler for game asset
     * management within the Game class, ensuring that the game components
     * have consistent access to these assets.
     */
    private GAGen gag;


    /**
     * Constructs a new Game instance with the specified game level and unique identifier.
     * This initializes the game's components, including the card deck, tile deck,
     * game board, and maintains an empty player list.
     *
     * @param GameLevel the difficulty level or stage of the game
     * @param id the unique identifier for this game instance
     * @throws IOException if an input or output exception occurs during initialization
     */
    public Game(int GameLevel, String id) throws IOException{
        GameID = id;
        gag = new GAGen();
        PlayerList = new ArrayList<>();
        Players = new HashMap<>();
        CardDeck = new CardStacks(gag, GameLevel);
        TileDecks = new TileSets(gag);
        lv = GameLevel;
        GameBoard = new GameBoard(TileDecks, lv, CardDeck);
    }


    /**
     * Adds a new player to the game. This method ensures that the game does not
     * exceed the maximum number of players and prevents duplicate players from
     * being added. If the player is successfully added, their board is set,
     * and they are registered in the game.
     *
     * @param newborn The Player object representing the new player to be added.
     *                The player's ID must be unique and not already exist in the game.
     * @throws IllegalArgumentException If a player with the same ID already exists.
     * @throws IndexOutOfBoundsException If the number of players reaches the maximum limit of 4.
     */
    public synchronized void NewPlayer(Player newborn)throws IllegalArgumentException, IndexOutOfBoundsException{
        if(Players.size() >= 4)
            throw new IndexOutOfBoundsException("Game is full");

        if (Players.containsKey(newborn.GetID())){
            throw new IllegalArgumentException("Player already exists");
        }
        newborn.setBoards(GameBoard);
        this.GameBoard.addPlayer(newborn);
        Players.put(newborn.GetID(), newborn);
        PlayerList.add(newborn);
    }

    /**
     * Removes a player identified by their unique ID from the game.
     *
     * @param DeadMan The unique identifier of the player to be removed.
     */
    public synchronized void RemovePlayer(String DeadMan){
        Players.remove(DeadMan);
    }

    /**
     * Retrieves the unique identifier of the game.
     *
     * @return the unique GameID as a String.
     */
    public String getID(){return GameID;}


    /**
     * Retrieves all players currently participating in the game session.
     * The returned map contains player identifiers as keys and their
     * corresponding Player objects as values.
     *
     * @return a HashMap containing the mapping of player IDs to Player objects.
     */
    public synchronized HashMap<String,Player> getPlayers(){
        return Players;
    }


    /**
     * Retrieves the GAGen instance associated with the game.
     *
     * @return the GAGen object representing the game's generator, including its
     *         tile and card decks.
     */
    public GAGen getGag() {
        return gag;
    }


    /**
     * Retrieves the game board associated with the current game session.
     *
     * @return the GameBoard instance representing the game board of the session.
     */
    public GameBoard getGameBoard() {
        return GameBoard;
    }


    /**
     * Updates the game board for the game session with the specified GameBoard object.
     *
     * @param gameBoard the GameBoard object to set as the current game board for the game session
     */
    public void setGameBoard(GameBoard gameBoard) {
        GameBoard = gameBoard;
    }


    /**
     * Retrieves the unique identifier of the current game instance.
     *
     * @return the unique game identifier as a String
     */
    public String getGameID() {
        return GameID;
    }


    /**
     * Sets the game ID for the current game session.
     *
     * @param gameID the unique identifier to be set for the game.
     */
    public void setGameID(String gameID) {
        GameID = gameID;
    }


    /**
     * Retrieves the current game level.
     *
     * @return the integer representing the level of the game.
     */
    public int getLv() {
        return lv;
    }
}

