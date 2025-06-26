package org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.Event;
import org.example.galaxy_trucker.ClientServer.Messages.EventVisitor;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;

/**
 * RewardsEvent represents an event in the system that encapsulates a list of rewards.
 * Rewards are stored as a collection of Goods objects, which represent different
 * types of reward items. This event is used to distribute or notify about rewards in
 * the application context.
 *
 * The RewardsEvent class implements the Event interface, making it part of the
 * event-processing framework. It utilizes the visitor pattern to handle its event-specific
 * behavior and supports JSON serialization and deserialization for integration within
 * the system.
 */
public class RewardsEvent implements Event {

    /**
     * Represents the rewards associated with a particular event.
     * Rewards are encapsulated as a list of {@code Goods}, which may represent
     * different types of reward items available (e.g., BLUE, GREEN, YELLOW, RED).
     *
     * This field is serialized and deserialized using the {@code @JsonProperty} annotation,
     * allowing integration with external systems that process JSON data.
     */
    @JsonProperty("rewards")
    ArrayList<Goods> rewards;

    /**
     * Default constructor for the RewardsEvent class.
     * This constructor initializes an instance of the RewardsEvent
     * without setting any initial rewards. It can be used to create
     * an empty event, where the rewards can be set later.
     */
    public RewardsEvent() {

    }

    /**
     * Constructs a new RewardsEvent with the specified list of rewards.
     *
     * @param rewards the collection of reward items represented as an ArrayList of Goods.
     *                It encapsulates the details of the rewards associated with this event.
     */
    @JsonCreator
    public RewardsEvent(@JsonProperty("rewards") ArrayList<Goods> rewards) {
        this.rewards = rewards;
    }



    /**
     * Accepts a visitor that processes this RewardsEvent.
     *
     * This method implements the visitor pattern, allowing for different behaviors
     * to be defined based on the type of event. The visitor performs its processing
     * logic by calling the appropriate visit method for this event type.
     *
     * @param visitor the EventVisitor responsible for handling this RewardsEvent
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Returns a message associated with the event. This method is a placeholder and
     * currently returns an empty string. The actual implementation may be provided
     * in concrete subclasses or future extensions.
     *
     * @return an empty string as the message associated with the event
     */
    @JsonIgnore
    @Override
    public String message() {
        return "";
    }


    /**
     * Retrieves the list of rewards associated with the event.
     *
     * @return an ArrayList of Goods objects, representing the rewards encapsulated
     *         within the event. Each Goods object corresponds to a specific type
     *         of reward. The list may be empty if no rewards are associated with
     *         the event.
     */
    public ArrayList<Goods> getRewards() {
        return rewards;
    }
}
