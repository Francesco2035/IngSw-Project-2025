package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

/**
 * Represents a scoreboard update event in the application.
 * This event contains score data for various players, mapped by their identifiers.
 * It provides functionality to retrieve the scores and enables interaction with an event visitor.
 */
public class ScoreboardEvent implements Event {

    /**
     * A mapping of player identifiers to their respective scores.
     * This variable holds the score data, where the key is a player's unique identifier
     * (such as username or ID) and the value is the player's score.
     * It is used within the context of the ScoreboardEvent.
     */
    private HashMap<String, Integer> scores = new HashMap<>();

    /**
     * Retrieves the scores associated with this event. The scores are stored in a map where
     * each key is a string representing a player's identifier, and each value is an integer
     * representing the player's score.
     *
     * @return a HashMap where the keys are player identifiers (String) and the values are the
     * corresponding scores (Integer).
     */
    public HashMap<String, Integer> getScores() {
        return scores;
    }

    /**
     * Default constructor for the ScoreboardEvent class.
     *
     * Initializes an instance of ScoreboardEvent with no predefined score data.
     * This constructor can be used when creating a new scoreboard event for situations
     * where predefined scores are not required at the time of instantiation.
     */
    public ScoreboardEvent(){

    }

    /**
     * Constructs a ScoreboardEvent with the provided scores.
     * This constructor is primarily used for deserialization of JSON data into the event.
     *
     * @param scores a HashMap containing player identifiers as keys and their respective scores as values.
     */
    @JsonCreator
    public ScoreboardEvent(@JsonProperty("scores") HashMap<String, Integer> scores) {
        this.scores = scores;
    }

    /**
     * Accepts a visitor for processing this {@code ScoreboardEvent}.
     *
     * @param visitor the {@link EventVisitor} that will process this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Returns a message specific to the event.
     *
     * This method, when overridden by subclasses, can provide custom event messages.
     * Currently, it returns an empty string for this implementation.
     *
     * @return a string representing the message associated with the event.
     */
    @JsonIgnore
    @Override
    public String message() {
        return "";
    }
}
