package org.example.galaxy_trucker.Controller.Messages.TileSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.EventVisitor;

/**
 * Represents a specific type of event that pertains to a card-related action or state
 * within the system. This class serves as a concrete implementation of the Event interface,
 * allowing it to be used within the event processing framework and to support the visitor pattern.
 *
 * A CardEvent is uniquely identified by an integer ID, which can be used to differentiate
 * it from other CardEvents or to associate it with external entities (such as cards in a card game).
 *
 * This class supports JSON serialization and deserialization, enabling it to be utilized in
 * distributed systems or persistence mechanisms. JSON properties are explicitly defined for proper
 * serialization and deserialization handling.
 *
 * The primary functionality of this class includes providing the event-specific ID, delivering
 * an optional message, and accepting an EventVisitor to process its behavior.
 */
public class CardEvent  implements Event {

    private int id;

    /**
     * Retrieves a descriptive message associated with this CardEvent.
     *
     * This method provides an event-specific message, which is expected to provide
     * additional context or information about the particular state or action represented
     * by the event. In this implementation, the message is currently set to an empty string.
     *
     * @return a string containing the event-specific message. By default, an empty
     *         string is returned for CardEvent type.
     */
    @Override
    public String message() {
        return "";
    }

    /**
     * Constructs a new CardEvent instance with a specified ID.
     *
     * @param id The unique identifier of the card event.
     */
    @JsonCreator
    public CardEvent(@JsonProperty("id")int id){
        this.id = id;
    }

    /**
     * Retrieves the unique identifier of this CardEvent.
     *
     * @return the integer ID associated with this CardEvent
     */
    public int getId() {
        return id;
    }

    /**
     * Accepts an {@link EventVisitor} to process logic associated with this specific event.
     * This method enables the visitor design pattern, allowing the external visitor
     * to handle behavior particular to a {@link CardEvent}.
     *
     * @param visitor the instance of EventVisitor that will process this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Default constructor for the CardEvent class.
     *
     * Initializes a new instance of the CardEvent class with default values.
     * This constructor is primarily provided for serialization or deserialization
     * frameworks, or cases where an event instance is required without specific initialization.
     */
    public CardEvent(){

    }

}
