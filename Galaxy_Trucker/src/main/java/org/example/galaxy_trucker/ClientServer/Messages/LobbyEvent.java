package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Represents an event that occurs in a game lobby.
 * This class contains information about the game lobby such as its ID, level,
 * maximum number of players, and the list of players currently in the lobby.
 * It is used to transmit lobby-related events between different parts of the system.
 */
public class LobbyEvent implements Event {

    String gameId;
    int lv;
    int maxPlayers;
    ArrayList<String> players;


    /**
     * Constructs a new instance of the LobbyEvent class.
     *
     * This no-argument constructor initializes a LobbyEvent instance with default
     * values. It is primarily used for deserialization or for creating an empty
     * event object to be populated later. The LobbyEvent represents an event
     * occurring in a game lobby, facilitating communication related to the lobby
     * in the system.
     */
    public LobbyEvent() {

    }
    /**
     * Constructs a new LobbyEvent instance with the specified game ID, level,
     * list of players, and the maximum number of players.
     *
     * @param gameId      the unique identifier of the game session
     * @param lv          the level of the game or lobby
     * @param players     the list of player names currently in the lobby
     * @param maxPlayers  the maximum number of players allowed in the lobby
     */
    @JsonCreator
    public LobbyEvent(@JsonProperty("gameId")String gameId, @JsonProperty("lv")int lv, @JsonProperty("players") ArrayList<String> players, @JsonProperty("maxPlayers")int maxPlayers) {
        this.gameId = gameId;
        this.players = players;
        this.lv = lv;
        this.maxPlayers = maxPlayers;
    }

    /**
     * Processes the current {@code LobbyEvent} instance using the provided {@link EventVisitor}.
     * This method allows for the implementation of the Visitor design pattern,
     * enabling operations to be performed on the event without modifying its structure.
     *
     * @param visitor the {@link EventVisitor} instance responsible for handling
     *                the specific {@code LobbyEvent} logic
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Retrieves the message associated with this event.
     *
     * This method returns a string that represents the message content
     * relevant to the specific event. The implementation here provides an
     * empty string, indicating no specific message is associated with this event.
     *
     * @return a string representing the event message, or an empty string if no
     *         message is defined.
     */
    @Override
    public String message() {
        return "";
    }

    /**
     * Retrieves the list of players currently in the lobby.
     *
     * @return an ArrayList containing the names of players in the lobby
     */
    public ArrayList<String> getPlayers() {

        return this.players;

    }

    /**
     * Retrieves the level (lv) associated with this event.
     *
     * @return the level of this event as an integer.
     */
    public int getLv() {
        return this.lv;
    }

    /**
     * Retrieves the identifier of the game associated with this event.
     *
     * @return the game ID as a String
     */
    public String getGameId() {
        return this.gameId;
    }

    /**
     * Retrieves the maximum number of players allowed in the game lobby.
     *
     * @return the maximum number of players as an integer.
     */
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

}
