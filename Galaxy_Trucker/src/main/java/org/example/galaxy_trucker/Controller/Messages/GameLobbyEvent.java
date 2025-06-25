package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
/**
 * Represents an event for a game lobby, which includes the list of players and their readiness statuses.
 * This event is part of the larger event interface hierarchy and can be processed by an EventVisitor.
 *
 * The purpose of this event is to encapsulate the state of the game lobby, specifically
 * by providing the list of players currently in the lobby and a corresponding readiness status
 * for each player.
 *
 * Constructors:
 * - A default constructor for initialization without parameters.
 * - A parameterized constructor allowing deserialization via JSON annotations to initialize
 *   the player list and readiness status.
 *
 * Implements:
 * - Event interface, allowing the event to be visited by EventVisitor implementations.
 *
 * Methods:
 * - Accepts a visitor to process this event as part of the visitor design pattern.
 * - Returns a placeholder message.
 * - Provides access to the list of players.
 * - Provides access to the readiness statuses of the players.
 */
public class GameLobbyEvent implements Event{

    ArrayList<String> players;
    ArrayList<Boolean> ready;

    /**
     * Allows the provided EventVisitor instance to process this GameLobbyEvent.
     *
     * @param visitor the EventVisitor instance that will process this GameLobbyEvent
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Default constructor for the GameLobbyEvent class.
     *
     * This constructor initializes a new instance of the GameLobbyEvent, which
     * represents an event for managing game lobby state. It can be used to define
     * an empty event instance for initialization or deserialization purposes.
     */
    public GameLobbyEvent() {

    }

    /**
     * Constructs a GameLobbyEvent with the specified list of players and their readiness statuses.
     *
     * @param players the list of player names in the game lobby
     * @param ready the list of readiness statuses corresponding to the players; true if the player is ready, false otherwise
     */
    @JsonCreator
    public GameLobbyEvent(@JsonProperty("players") ArrayList<String> players,@JsonProperty("ready") ArrayList<Boolean> ready) {
        this.players = players;
        this.ready = ready;
    }

    /**
     * Retrieves the message associated with this event.
     *
     * This method is intended to provide a descriptive message
     * relevant to the specific event. For this implementation,
     * it returns an empty string.
     *
     * @return a descriptive string representing the message of the event, or an
     *         empty string if no specific message is associated with this event.
     */
    @Override
    public String message() {
        return "";
    }

    /**
     * Retrieves the list of players currently in the game lobby.
     *
     * @return an ArrayList containing the names of the players in the lobby
     */
    public ArrayList<String> getPlayers() {
        return players;
    }

    /**
     * Retrieves the list of readiness statuses for players in the game lobby.
     * Each element in the returned list corresponds to a player's readiness status,
     * represented as a Boolean value where {@code true} indicates readiness and {@code false}
     * indicates not ready.
     *
     * @return an ArrayList of Boolean values representing the readiness status of each player
     *         in the game lobby.
     */
    public ArrayList<Boolean> getReady() {
        return ready;
    }
}
