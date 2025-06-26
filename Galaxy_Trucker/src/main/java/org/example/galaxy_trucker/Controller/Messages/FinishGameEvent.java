package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an event that indicates the end of a game.
 * This event specifies whether the game concluded with a win or not,
 * and provides a message with additional details about the game's outcome.
 *
 * Implements the Event interface, allowing it to be handled by an EventVisitor.
 * It supports serialization and deserialization for external property-based JSON mappings.
 *
 * This class is primarily used to notify interested components about the game's conclusion.
 */
public class FinishGameEvent implements Event {

    boolean win;
    String message;

    /**
     * Constructs a new instance of FinishGameEvent.
     *
     * This default constructor initializes a FinishGameEvent object
     * with no specific values set for its properties. It is typically
     * used when creating an instance to populate later or for
     * deserialization purposes.
     *
     * FinishGameEvent represents the conclusion of a game and notifies
     * interested components about the game's outcome.
     */
    public FinishGameEvent() {

    }

    /**
     * Creates a new instance of the FinishGameEvent class.
     * This event indicates the conclusion of a game, specifying whether the game
     * ended in a win state and providing a message with additional details about
     * the game's outcome.
     *
     * @param win a boolean indicating whether the game ended in a win (true) or not (false)
     * @param message a string containing additional details about the game's conclusion
     */
    public FinishGameEvent(@JsonProperty("win") boolean win, @JsonProperty("message") String message) {
        this.win = win;
        this.message = message;
    }

    /**
     * Accepts a visitor that implements the EventVisitor interface to process this event.
     *
     * @param visitor the EventVisitor instance responsible for handling this specific event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Retrieves the message associated with this event.
     *
     * This method provides additional details about the specific event it belongs to,
     * typically reflecting a descriptive message tied to the outcome or state of the event.
     *
     * @return the message associated with this event as a string.
     */
    @JsonIgnore
    @Override
    public String message() {
        return message;
    }

    /**
     * Checks if the game has concluded with a win.
     *
     * @return true if the game ended with a win, false otherwise.
     */
    public boolean isWin() {
        return win;
    }

    /**
     * Retrieves the message associated with the event.
     *
     * The message provides additional details or context regarding the outcome
     * or specifics of the event. It can be used to convey information to external
     * components or as part of event processing logic.
     *
     * @return a string representing the message associated with the event.
     */
    public String getMessage() {
        return message;
    }
}
