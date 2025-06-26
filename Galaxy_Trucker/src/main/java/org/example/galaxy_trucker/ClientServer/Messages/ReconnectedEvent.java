package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an event triggered when a player reconnects to the game.
 * This event contains relevant information about the player's reconnection,
 * including a token for authentication, the game ID, player ID, and the player's level.
 * Implements the {@link Event} interface and is designed to be visited by an {@link EventVisitor}.
 */
public class ReconnectedEvent implements Event{

    String token;
    String gameId;
    String playerId;
    int lv;

    /**
     * Default constructor for the ReconnectedEvent class.
     *
     * Constructs an instance of ReconnectedEvent without initializing
     * any specific values for its properties. Primarily used for deserialization
     * or cases where the event needs to be initialized and set up later.
     */
    public ReconnectedEvent() {

    }

    /**
     * Constructs a new ReconnectedEvent.
     *
     * @param token   the authentication token for the reconnected player
     * @param gameId  the ID of the game to which the player has reconnected
     * @param playerId the ID of the player who has reconnected
     * @param lv      the level of the player at the time of reconnection
     */
    @JsonCreator
    public ReconnectedEvent(@JsonProperty("token") String token,@JsonProperty("gameId") String gameId,@JsonProperty("playerId") String playerId,@JsonProperty("lv") int lv){
        this.token = token;
        this.gameId = gameId;
        this.playerId = playerId;
        this.lv = lv;
    }

    /**
     * Accepts a visitor to process this event.
     * The visitor performs operations specific to this event type.
     *
     * @param visitor the {@link EventVisitor} instance that will process this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Retrieves the message associated with this event.
     *
     * The default implementation returns an empty string or is overridden by specific event types
     * to provide context-specific messages.
     *
     * @return the message associated with the event as a string.
     */
    @JsonIgnore
    @Override
    public String message() {
        return "";
    }

    /**
     * Retrieves the token associated with this event.
     *
     * @return a string representing the token used for authentication or identification purposes.
     */
    public String getToken() {
        return token;
    }

    /**
     * Retrieves the ID of the game associated with this event.
     *
     * @return the game ID as a string.
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Retrieves the player ID associated with the event.
     *
     * @return the player ID as a string.
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * Retrieves the player's level associated with this event.
     *
     * @return the player's level as an integer.
     */
    public int getLv() {
        return lv;
    }
}
