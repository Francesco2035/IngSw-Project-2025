package org.example.galaxy_trucker.Controller.Messages.TileSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.EventVisitor;

/**
 * Represents an event related to a covered tile set in the system.
 * This event is part of the event framework that uses the visitor
 * design pattern for handling various types of events.
 *
 * The CoveredTileSetEvent class includes details about the size of the
 * covered tile set it represents.
 */
public class CoveredTileSetEvent implements Event {

    private int size;

    /**
     * Constructs a new CoveredTileSetEvent instance with the specified size.
     * This event represents a change in a covered tile set, providing information
     * about the current size of the set.
     *
     * @param size the current size of the covered tile set
     */
    @JsonCreator
    public CoveredTileSetEvent(@JsonProperty("size") int size) {
        this.size = size;
    }

    /**
     * Default constructor for the CoveredTileSetEvent class.
     *
     * Initializes a new instance of the CoveredTileSetEvent class with default values.
     * This constructor is primarily intended for serialization or deserialization frameworks
     * or cases where an event instance is required without specific initialization.
     */
    public CoveredTileSetEvent() {
    }

    /**
     * Retrieves the size of the covered tile set represented by this event.
     *
     * @return the size of the covered tile set as an integer
     */
    public int getSize() {
        return size;
    }

    /**
     * Retrieves a descriptive message associated with this event.
     *
     * This method is intended to provide additional context or information about the event.
     * By default, the message is set to an empty string for this implementation.
     *
     * @return a string containing the event-specific message. Returns an empty string by default.
     */
    @Override
    public String message() {
        return "";
    }

    /**
     * Accepts a visitor that handles the event using the visitor design pattern.
     *
     * @param visitor the EventVisitor instance that processes this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

}
