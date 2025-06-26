package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an event on the game board.
 * This event carries information about a specific position on the board and
 * the player associated with a particular action or state at that position.
 * It is part of a hierarchy of events that are processed via the {@link EventVisitor} pattern.
 */
public class GameBoardEvent implements Event {


    private int position;
    private String playerID;


    /**
     * Constructs a GameBoardEvent instance with the specified position and player ID.
     *
     * This event represents an action or a state associated with a specific position
     * on the game board and the player responsible for it.
     *
     * @param position the position on the game board related to this event
     * @param playerID the identifier of the player associated with this event
     */
    @JsonCreator
    public GameBoardEvent(
            @JsonProperty("position") int position,
            @JsonProperty("playerID") String playerID) {

        this.position = position;
        this.playerID = playerID;
    }

    /**
     * Default constructor for the GameBoardEvent class.
     *
     * This no-argument constructor initializes a new instance of GameBoardEvent
     * with default values. It is typically used for deserialization or creating
     * a new instance to populate the event's properties later.
     */
    public GameBoardEvent() {}

    /**
     * Retrieves the position on the game board associated with this event.
     *
     * @return the position as an integer.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Retrieves the identifier of the player associated with this instance.
     *
     * @return a {@code String} representing the player's unique identifier.
     */
    public String getPlayerID() {
        return playerID;
    }


    /**
     * Accepts a visitor to process this GameBoardEvent instance.
     *
     * @param visitor the EventVisitor instance that processes this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Retrieves the message associated with this event.
     *
     * This method is intended to return a descriptive string representing the
     * message of the event. For this implementation, it returns an empty string.
     *
     * @return a descriptive string representing the event message, or an empty
     *         string if no specific message is associated with the event.
     */
    @Override
    public String message() {
        return "";
    }

}
